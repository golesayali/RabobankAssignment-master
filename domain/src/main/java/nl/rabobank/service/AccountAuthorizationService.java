package nl.rabobank.service;

import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.mongo.entity.BankAccount;

import java.util.List;

/**
 * Account authorization service consists of two operation.
 * 1. Create authorization for existing Payments/Saving account.
 * 2. Get accounts for which given grantee has READ/WRITE access
 *
 * @author Sayali G
 */
public interface AccountAuthorizationService {
    void createAuthorization(PowerOfAttorney grantee);

    List<BankAccount> getAccountsForGrantee(String name);
}
