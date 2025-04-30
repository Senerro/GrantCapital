package grand.capital.bank.controller;

import grand.capital.bank.domain.model.User;
import grand.capital.bank.security.JwtTokenUtil;
import grand.capital.bank.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam(required = false) String email,
                                   @RequestParam(required = false) String phone,
                                   @RequestParam String password) {
        User user = null;
        if (email != null && !email.isEmpty()) {
            user = authenticationService.authenticateByEmail(email, password);
        } else if (phone != null && !phone.isEmpty()) {
            user = authenticationService.authenticateByPhone(phone, password);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body("Укажите email или phone для аутентификации");
        }
        if (user == null) {
            return ResponseEntity
                    .status(401)
                    .body("Неверные учетные данные");
        }

        String token = jwtTokenUtil.createToken(user.getId());
        return ResponseEntity.ok(token);
    }
}
