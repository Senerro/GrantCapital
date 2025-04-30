package grand.capital.bank.domain.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class CreateUserDTO {
    @NotNull
    @Size(min = 8)
    private String password;
    private String name;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate dateOfBirth;

    @NotEmpty(message = "Необходимо указать email")
    private List<@Email String> emails;

    @NotEmpty(message = "Необходимо указать телефон")
    private List<@Pattern(regexp = "\\d{1,13}", message = "Телефон должен содержать только цифры") String> phones;

    @NotNull(message = "Начальный баланс обязателен")
    private BigDecimal initialBalance;
}
