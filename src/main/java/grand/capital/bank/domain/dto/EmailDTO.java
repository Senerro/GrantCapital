package grand.capital.bank.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailDTO {
    @NotNull(message = "Email обязателен")
    @Email(message = "Email указан неверно")
    private String email;
}