package grand.capital.bank.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(name = "EMAIL_DATA", uniqueConstraints = @UniqueConstraint(columnNames = "EMAIL"))
@Data
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EMAIL", length = 200, nullable = false, unique = true)
    @jakarta.validation.constraints.Email
    private String email;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;


}