package grand.capital.bank.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Table(name = "PHONE_DATA", uniqueConstraints = @UniqueConstraint(columnNames = "PHONE"))
@Data
public class Phone{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PHONE", length = 13, nullable = false, unique = true)
    @Pattern(regexp = "\\d{1,13}")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;
}