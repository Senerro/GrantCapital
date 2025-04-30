package grand.capital.bank.repository;

import grand.capital.bank.domain.model.Account;
import jakarta.persistence.Id;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Id> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.user.id = :userId")
    Optional<Account> findByUserIdForUpdate(@Param("userId") Long userId);
}
