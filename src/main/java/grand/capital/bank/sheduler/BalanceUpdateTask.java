package grand.capital.bank.sheduler;

import grand.capital.bank.domain.model.Account;
import grand.capital.bank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BalanceUpdateTask {
    @Autowired
    private final AccountRepository accountRepository;
    @Value("${balance.increment.rate}")
    private BigDecimal incrementRate;
    @Value("${balance.max.rate}")
    private BigDecimal maxRate;

    @Scheduled(fixedRate = 30000)
    public void updateBalances() {
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            BigDecimal currentBalance = account.getBalance();
            BigDecimal initialBalance = account.getInitBalance();

            BigDecimal multiplier = BigDecimal.ONE.add(incrementRate.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
            BigDecimal proposedBalance = currentBalance.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);

            BigDecimal maxAllowed = initialBalance.multiply(maxRate.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP))
                    .setScale(2, RoundingMode.HALF_UP);

            if (proposedBalance.compareTo(maxAllowed) > 0) {
                proposedBalance = maxAllowed;
            }

            if (proposedBalance.compareTo(currentBalance) > 0) {
                account.setBalance(proposedBalance);
                accountRepository.save(account);
            }
        }
    }
}
