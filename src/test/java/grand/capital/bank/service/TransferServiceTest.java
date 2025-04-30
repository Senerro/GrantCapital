package grand.capital.bank.service;

import grand.capital.bank.domain.model.Account;
import grand.capital.bank.domain.model.User;
import grand.capital.bank.repository.AccountRepository;
import grand.capital.bank.repository.UserRepository;
import grand.capital.bank.util.TestUtilsGenerator;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private TransferService transferService;
    @Autowired
    private UserRepository userRepository;

    private User sender;
    private User recipient;
    private Account senderAccount;
    private Account recipientAccount;


    @BeforeEach
    public void setup() {
        userRepository.save(TestUtilsGenerator.getSenderUser());
        userRepository.save(TestUtilsGenerator.getRecieptUser());
    }

    @Test
    public void testSuccessfulTransfer() {
        Long senderId = 1L;
        Long recipientId = 2L;
        BigDecimal transferAmount = new BigDecimal("200.00");

        when(accountRepository.findByUserIdForUpdate(senderId))
                .thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByUserIdForUpdate(recipientId))
                .thenReturn(Optional.of(recipientAccount));

        transferService.transferMoney(senderId, recipientId, transferAmount);

        assertEquals(new BigDecimal("800.00"), senderAccount.getBalance());
        assertEquals(new BigDecimal("700.00"), recipientAccount.getBalance());

        verify(accountRepository, times(1)).save(senderAccount);
        verify(accountRepository, times(1)).save(recipientAccount);
    }

    @Test
    public void testTransferInsufficientFunds() {
        Long senderId = 1L;
        Long recipientId = 2L;
        BigDecimal transferAmount = new BigDecimal("1200.00");

        when(accountRepository.findByUserIdForUpdate(senderId))
                .thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByUserIdForUpdate(recipientId))
                .thenReturn(Optional.of(recipientAccount));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                transferService.transferMoney(senderId, recipientId, transferAmount));
        assertEquals("Недостаточно средств для перевода", ex.getMessage());
    }

    @Test
    public void testConcurrentTransfersIntegration() throws InterruptedException {
        Long senderId = sender.getId();
        Long recipientId = recipient.getId();
        BigDecimal transferAmount = new BigDecimal("300.00");
        int transfersAmount = 5;
        int expectedCountOfSuccessfulTransfers = 3;

        ExecutorService executorService = Executors.newFixedThreadPool(transfersAmount);
        CountDownLatch latch = new CountDownLatch(1);
        List<Future<Boolean>> futures = new ArrayList<>();

        for (int i = 0; i < transfersAmount; i++) {
            futures.add(executorService.submit(() -> {
                try {
                    latch.await();
                    transferService.transferMoney(senderId, recipientId, transferAmount);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }));
        }
        latch.countDown();

        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);

        long successfulTransfers = futures.stream().filter(future -> {
            try {
                return future.get();
            } catch (Exception e) {
                return false;
            }
        }).count();


        assertEquals(expectedCountOfSuccessfulTransfers, successfulTransfers);


        Account updatedSenderAccount = accountRepository.findByUserIdForUpdate(senderId)
                .orElseThrow();
        Account updatedRecipientAccount = accountRepository.findByUserIdForUpdate(recipientId)
                .orElseThrow();

        final var multiply = new BigDecimal("300.00").multiply(BigDecimal.valueOf(successfulTransfers));
        BigDecimal expectedSenderBalance = new BigDecimal("1000.00").subtract(multiply);

        BigDecimal expectedRecipientBalance = new BigDecimal("500.00").add(multiply);

        assertEquals(expectedSenderBalance, updatedSenderAccount.getBalance());
        assertEquals(expectedRecipientBalance, updatedRecipientAccount.getBalance());
    }
}
