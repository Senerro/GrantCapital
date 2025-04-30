package grand.capital.bank.controller;

import grand.capital.bank.domain.dto.TransferDTO;
import grand.capital.bank.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<String> transferFunds(@Valid @RequestBody TransferDTO transferDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long senderUserId;
        if (principal instanceof Long) {
            senderUserId = (Long) principal;
        } else {
            try {
                senderUserId = Long.parseLong(principal.toString());
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Неверный идентификатор отправителя");
            }
        }

        transferService.transferMoney(senderUserId, transferDTO.getRecipientUserId(), transferDTO.getAmount());

        return ResponseEntity.ok("Перевод успешно выполнен");
    }
}
