package nl.rabobank.mongo.repository;

import nl.rabobank.mongo.entity.BankAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This interface is responsible for CRUD operations for bankaccounts collection in mongodb.
 *
 * @author Sayali G
 */
@Repository
public interface BankAccountRepository extends MongoRepository<BankAccount, String> {

    Optional<BankAccount> findByAccountNumberAndAccountType(String accountNumber, String accountType);

    List<BankAccount> findByAccountAuthorizationsGranteeName(String granteeName);
}
