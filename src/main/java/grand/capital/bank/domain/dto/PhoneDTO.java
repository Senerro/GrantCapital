package grand.capital.bank.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhoneDTO {
    @NotNull(message = "Телефон обязателен")
    @Pattern(regexp = "\\d{1,13}", message = "Телефон должен содержать только цифры")
    private String phone;
}