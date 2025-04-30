package grand.capital.bank.service;

import grand.capital.bank.domain.dto.EmailDTO;
import grand.capital.bank.domain.dto.PhoneDTO;
import grand.capital.bank.domain.dto.UpdateUserDTO;
import grand.capital.bank.domain.model.Account;
import grand.capital.bank.domain.model.Email;
import grand.capital.bank.domain.model.Phone;
import grand.capital.bank.domain.model.User;
import grand.capital.bank.repository.AccountRepository;
import grand.capital.bank.repository.EmailRepository;
import grand.capital.bank.repository.PhoneRepository;
import grand.capital.bank.repository.UserRepository;
import grand.capital.bank.specifications.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmailRepository EmailRepository;
    @Autowired
    private PhoneRepository PhoneRepository;

    @Transactional
    public User updateUserDetails(Long userId, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        if (updateUserDTO.getName() != null) {
            user.setName(updateUserDTO.getName());
        }
        if (updateUserDTO.getDateOfBirth() != null) {
            user.setDateOfBirth(updateUserDTO.getDateOfBirth());
        }
        return userRepository.save(user);
    }

    @Transactional
    public void addEmail(Long userId, EmailDTO emailDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        EmailRepository.findByEmail(emailDTO.getEmail()).ifPresent(ed -> {
            throw new IllegalArgumentException("Email уже используется");
        });
        Email Email = new Email();
        Email.setEmail(emailDTO.getEmail());
        Email.setUser(user);
        user.getEmails().add(Email);
        userRepository.save(user);
    }

    @Transactional
    public void updateEmail(Long userId, Long emailId, EmailDTO emailDTO) {
        EmailRepository.findByEmail(emailDTO.getEmail()).ifPresent(ed -> {
            if (!ed.getUser().getId().equals(userId)) {
                throw new IllegalArgumentException("Указанный Email уже используется другим пользователем");
            }
        });
        Email Email = EmailRepository.findById(emailId)
                .orElseThrow(() -> new IllegalArgumentException("Email не найден"));
        if (!Email.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Email не принадлежит пользователю");
        }
        Email.setEmail(emailDTO.getEmail());
        EmailRepository.save(Email);
    }

    @Transactional
    public void deleteEmail(Long userId, Long emailId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        if (user.getEmails().size() <= 1) {
            throw new IllegalArgumentException("У пользователя должен быть минимум один email");
        }
        Email Email = EmailRepository.findById(emailId)
                .orElseThrow(() -> new IllegalArgumentException("Email не найден"));
        if (!Email.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Email не принадлежит пользователю");
        }
        user.getEmails().remove(Email);
        EmailRepository.delete(Email);
    }

    @Transactional
    public void addPhone(Long userId, PhoneDTO phoneDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        PhoneRepository.findByPhone(phoneDTO.getPhone()).ifPresent(pd -> {
            throw new IllegalArgumentException("Телефон уже используется");
        });
        Phone Phone = new Phone();
        Phone.setPhone(phoneDTO.getPhone());
        Phone.setUser(user);
        user.getPhones().add(Phone);
        userRepository.save(user);
    }

    @Transactional
    public void updatePhone(Long userId, Long phoneId, PhoneDTO phoneDTO) {
        PhoneRepository.findByPhone(phoneDTO.getPhone()).ifPresent(pd -> {
            if (!pd.getUser().getId().equals(userId)) {
                throw new IllegalArgumentException("Телефон уже используется другим пользователем");
            }
        });
        Phone Phone = PhoneRepository.findById(phoneId)
                .orElseThrow(() -> new IllegalArgumentException("Телефон не найден"));
        if (!Phone.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Телефон не принадлежит пользователю");
        }
        Phone.setPhone(phoneDTO.getPhone());
        PhoneRepository.save(Phone);
    }

    @Transactional
    public void deletePhone(Long userId, Long phoneId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        if (user.getPhones().size() <= 1) {
            throw new IllegalArgumentException("У пользователя должен быть минимум один телефон");
        }
        Phone Phone = PhoneRepository.findById(phoneId)
                .orElseThrow(() -> new IllegalArgumentException("Телефон не найден"));
        if (!Phone.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Телефон не принадлежит пользователю");
        }
        user.getPhones().remove(Phone);
        PhoneRepository.delete(Phone);
    }


    @Transactional
    public void updateAccountBalance(Long userId, BigDecimal amountChange) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        Account account = user.getAccount();
        BigDecimal newBalance = account.getBalance().add(amountChange);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Операция не позволит балансу стать отрицательным");
        }
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    @Transactional
    @Cacheable(value = "usersSearch", key = "{#name, #email, @phone, #dateOdBirth, #page, #size}")
    public Page<User> searchUsers(String name, String email, String phone,
                                  LocalDate dateOfBirth, int page, int size) {
        UserSpecification specification = new UserSpecification(name, email, phone, dateOfBirth);
        return userRepository.findAll(specification, PageRequest.of(page, size));
    }
}