package grand.capital.bank.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UpdateUserDTO {
    private String name;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate dateOfBirth;

}
