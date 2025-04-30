package grand.capital.bank.domain.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferDTO {

    @NotNull(message = "Id получателя обязателен")
    private Long recipientUserId;

    @NotNull(message = "Сумма перевода обязательна")
    @DecimalMin(value = "0.01", inclusive = true, message = "Сумма должна быть положительной")
    private BigDecimal amount;
}
