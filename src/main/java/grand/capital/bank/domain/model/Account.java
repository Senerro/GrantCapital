package grand.capital.bank.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "ACCOUNT")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_ID", nullable = false, unique = true)
    private User user;

    @Column(name = "BALANCE", nullable = false)
    private BigDecimal balance;

    @Column(name = "INITIAL_BALANCE", nullable = false)
    private BigDecimal initBalance;
}
