package nl.rabobank.util;

import nl.rabobank.account.Account;
import nl.rabobank.account.PaymentAccount;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.exception.AccountAuthorizationException;

/**
 * This class contains utility methods for Account Authorization Application
 *
 * @author Sayali G
 */
public class ApplicationUtils {

    ApplicationUtils() {
        throw new IllegalStateException("Utility Classes should not have public constructors");
    }

    static Account determineAccountType(String accountType, String accountNumber, String grantorName) {
        switch (accountType) {
            case "PAYMENT": {
                return new PaymentAccount(accountNumber, grantorName, 0.0);
            }
            case "SAVINGS": {
                return new SavingsAccount(accountNumber, grantorName, 0.0);
            }
            default:
                throw new AccountAuthorizationException("Invalid account type provided in request. Allowed values are PAYMENT,SAVINGS");
        }
    }

    static Authorization determineAuthorization(String typeOfAuthorization) {
        switch (typeOfAuthorization) {
            case "READ": {
                return Authorization.READ;
            }
            case "WRITE": {
                return Authorization.WRITE;
            }
            default:
                throw new AccountAuthorizationException("Invalid access type provided in request. Allowed values are READ, WRITE");
        }
    }
}
