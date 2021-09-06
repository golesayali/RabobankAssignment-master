package nl.rabobank.service;

import lombok.extern.slf4j.Slf4j;
import nl.rabobank.account.Account;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.exception.ResourceNotFoundException;
import nl.rabobank.mongo.entity.BankAccount;
import nl.rabobank.mongo.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Account authorization service consists of implementations for below two operation.
 * 1. Create authorization for existing Payments/Saving account.
 * 2. Get accounts for which given grantee has READ/WRITE access
 *
 * @author Sayali G
 */
@Service
@Slf4j
public class AccountAuthorizationServiceImpl implements AccountAuthorizationService {
    /**
     * The Bank account repository.
     */
    @Autowired
    BankAccountRepository bankAccountRepository;

    @Override
    public void createAuthorization(PowerOfAttorney grantee) throws ResourceNotFoundException {
        log.debug("Creating authorization for account number : [{}]", grantee.getAccount().getAccountNumber());
        BankAccount bankAccountRecord = Optional.of(retrieveBankAccount(grantee.getAccount())).get()
                .orElseThrow(() ->
                {
                    log.error("Bank account with provided account number:[{}] and account type:[{}] not found",
                            grantee.getAccount().getAccountNumber(),
                            AccountAuthorizationServiceHelper.determineAccountType(grantee.getAccount()));
                    throw new ResourceNotFoundException("Bank account with provided account number and account type not found");
                });

        BankAccount accountWithUpdatedAuthorization =
                AccountAuthorizationServiceHelper.checkIfUserIsAlreadyAuthorized(
                        bankAccountRecord, grantee.getGranteeName(), grantee.getAuthorization().name());

        log.debug("Persisting authorizations to database for account number : [{}]", grantee.getAccount().getAccountNumber());
        bankAccountRepository.save(accountWithUpdatedAuthorization);
    }


    @Override
    public List<BankAccount> getAccountsForGrantee(String name) {
        log.debug("Getting authorized accounts for user : [{}]", name);
        return bankAccountRepository.findByAccountAuthorizationsGranteeName(name);

    }

    private Optional<BankAccount> retrieveBankAccount(Account account) {
        log.debug("Retrieving bank account : [{}]", account);
        String accountType = AccountAuthorizationServiceHelper.determineAccountType(account);
        return bankAccountRepository
                .findByAccountNumberAndAccountType(account.getAccountNumber(), accountType);
    }
}
