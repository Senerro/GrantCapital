package grand.capital.bank.service;

import grand.capital.bank.domain.model.Email;
import grand.capital.bank.domain.model.Phone;
import grand.capital.bank.domain.model.User;
import grand.capital.bank.repository.EmailRepository;
import grand.capital.bank.repository.PhoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private PhoneRepository phoneRepository;
    public User authenticateByEmail(String email, String password) {
        Email foundEmail = emailRepository.findByEmail(email).orElse(null);
        if (foundEmail != null) {
            User user = foundEmail.getUser();
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }


    public User authenticateByPhone(String phone, String password) {
        Phone foundPhone = phoneRepository.findByPhone(phone).orElse(null);
        if (foundPhone != null) {
            User user = foundPhone.getUser();
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
