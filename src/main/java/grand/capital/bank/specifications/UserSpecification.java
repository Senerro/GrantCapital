package grand.capital.bank.specifications;

import grand.capital.bank.domain.model.Email;
import grand.capital.bank.domain.model.Phone;
import grand.capital.bank.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import org.springframework.lang.NonNullApi;

import java.time.LocalDate;

@RequiredArgsConstructor
public class UserSpecification implements Specification<User> {
    private final String name;
    private final String email;
    private final String phone;
    private final LocalDate dateOfBirth;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        query.distinct(true);
        Predicate predicate = cb.conjunction();

        if (dateOfBirth != null) {
            predicate = cb.and(predicate, cb.greaterThan(root.get("dateOfBirth"), dateOfBirth));
        }

        if (email != null && !email.isEmpty()) {
            Join<User, Email> emailJoin = root.join("emails", JoinType.INNER);
            predicate = cb.and(predicate, cb.equal(emailJoin.get("email"), email));
        }

        if (phone != null && !phone.isEmpty()) {
            Join<User, Phone> phoneJoin = root.join("phones", JoinType.INNER);
            predicate = cb.and(predicate, cb.equal(phoneJoin.get("phone"), phone));
        }

        if (name != null && !name.isEmpty()) {
            predicate = cb.and(predicate, cb.like(root.get("name"), name + "%"));
        }

        return predicate;
    }
}