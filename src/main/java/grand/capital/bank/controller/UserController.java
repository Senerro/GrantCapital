package grand.capital.bank.controller;

import grand.capital.bank.domain.dto.CreateUserDTO;
import grand.capital.bank.domain.dto.EmailDTO;
import grand.capital.bank.domain.dto.PhoneDTO;
import grand.capital.bank.domain.dto.UpdateUserDTO;
import grand.capital.bank.domain.model.Email;
import grand.capital.bank.domain.model.User;
import grand.capital.bank.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserDetails(@PathVariable Long id,
                                                  @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        User updatedUser = userService.updateUserDetails(id, updateUserDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // Добавление нового email
    @PostMapping("/{id}/emails")
    public ResponseEntity<Void> addEmail(@PathVariable Long id,
                                         @Valid @RequestBody EmailDTO email) {
        userService.addEmail(id, email);
        return ResponseEntity.ok().build();
    }

    // Обновление существующего email
    @PutMapping("/{id}/emails/{emailId}")
    public ResponseEntity<Void> updateEmail(@PathVariable Long id,
                                            @PathVariable Long emailId,
                                            @Valid @RequestBody EmailDTO emailDTO) {
        userService.updateEmail(id, emailId, emailDTO);
        return ResponseEntity.ok().build();
    }

    // Удаление email (если у пользователя больше одного)
    @DeleteMapping("/{id}/emails/{emailId}")
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id,
                                            @PathVariable Long emailId) {
        userService.deleteEmail(id, emailId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/phones")
    public ResponseEntity<Void> addPhone(@PathVariable Long id,
                                         @Valid @RequestBody PhoneDTO phoneDTO) {
        userService.addPhone(id, phoneDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/phones/{phoneId}")
    public ResponseEntity<Void> updatePhone(@PathVariable Long id,
                                            @PathVariable Long phoneId,
                                            @Valid @RequestBody PhoneDTO request) {
        userService.updatePhone(id, phoneId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/phones/{phoneId}")
    public ResponseEntity<Void> deletePhone(@PathVariable Long id,
                                            @PathVariable Long phoneId) {
        userService.deletePhone(id, phoneId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/balance")
    public ResponseEntity<Void> updateBalance(@PathVariable Long id,
                                              @RequestParam BigDecimal amountChange) {
        userService.updateAccountBalance(id, amountChange);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<User>> searchUsers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "dateOfBirth", required = false)
            @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate dateOfBirth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<User> result = userService.searchUsers(name, email, phone, dateOfBirth, page, size);
        return ResponseEntity.ok(result);
    }
}
