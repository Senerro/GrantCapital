package grand.capital.bank.util;

import grand.capital.bank.domain.model.Account;
import grand.capital.bank.domain.model.User;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class TestUtilsGenerator {
    private final String SENDER_NAME = "Sender";
    private final String RECEIPT_NAME = "Receipt";
    private final BigDecimal SENDER_BALANCE = new BigDecimal("1000.00");
    private final BigDecimal RECEIPT_BALANCE = new BigDecimal("500.00");

    public User getSenderUser() {
        return getUser(SENDER_NAME, SENDER_BALANCE);
    }

    public User getRecieptUser() {
        return getUser(RECEIPT_NAME, RECEIPT_BALANCE);
    }

    private User getUser(String name, BigDecimal balance) {
        User recipient = new User();
        recipient.setName(name);
        recipient.setPassword("password");

        Account recipientAccount = new Account();
        recipientAccount.setBalance(balance);
        recipientAccount.setInitBalance(balance);
        recipientAccount.setUser(recipient);
        recipient.setAccount(recipientAccount);

        return recipient;
    }
}
