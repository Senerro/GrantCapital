package grand.capital.bank.service;

import grand.capital.bank.domain.model.Account;
import grand.capital.bank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferService {

    @Autowired
    private final AccountRepository accountRepository;

    @Transactional
    public void transferMoney(Long senderUserId, Long recipientUserId, BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть больше 0");
        }
        if (senderUserId.equals(recipientUserId)) {
            throw new IllegalArgumentException("Отправитель и получатель не должны совпадать");
        }

        Account sender = accountRepository.findByUserIdForUpdate(senderUserId)
                .orElseThrow(() -> new IllegalArgumentException("Аккаунт отправителя не найден"));
        Account recipient = accountRepository.findByUserIdForUpdate(recipientUserId)
                .orElseThrow(() -> new IllegalArgumentException("Аккаунт получателя не найден"));

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Недостаточно средств для перевода");
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        recipient.setBalance(recipient.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(recipient);
    }
}
