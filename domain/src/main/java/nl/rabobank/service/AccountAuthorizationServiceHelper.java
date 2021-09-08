package nl.rabobank.service;

import nl.rabobank.account.Account;
import nl.rabobank.account.PaymentAccount;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.mongo.entity.AccountAuthorization;
import nl.rabobank.mongo.entity.BankAccount;

import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * This class contains all the utility methods which are needed at service layer.
 *
 * @author Sayali G
 */
public class AccountAuthorizationServiceHelper {
    /**
     * Instantiates a new Account authorization service helper.
     */
    AccountAuthorizationServiceHelper() {
        throw new IllegalStateException("Helper Classes should not have public constructors");
    }

    /**
     * Determine account type string.
     *
     * @param account the account
     * @return the string
     */
    public static String determineAccountType(Account account) {
        if (account instanceof PaymentAccount) {
            return "PAYMENT";
        }
        if (account instanceof SavingsAccount) {
            return "SAVINGS";
        }
        return "";
    }

    /**
     * Check if user is already authorized bank account.
     *
     * @param bankAccountRecord the bank account record
     * @param granteeName       the grantee name
     * @param accessType        the access type
     * @return the bank account
     */
    public static BankAccount checkIfUserIsAlreadyAuthorized(BankAccount bankAccountRecord, String granteeName, String accessType) {
        AccountAuthorization accountAuthorization = AccountAuthorization.builder()
                .granteeName(granteeName)
                .accessType(accessType)
                .build();

        if (!bankAccountRecord.getAccountAuthorizations().isEmpty()
                && bankAccountRecord.getAccountAuthorizations().stream()
                .anyMatch(p -> p.getGranteeName().equals(granteeName))) {
            OptionalInt userIndex = IntStream.range(0, bankAccountRecord.getAccountAuthorizations().size())
                    .filter(i -> granteeName.equals(bankAccountRecord.getAccountAuthorizations().get(i).getGranteeName()))
                    .findFirst();
            bankAccountRecord.getAccountAuthorizations().set(userIndex.getAsInt(), accountAuthorization);
        } else {
            bankAccountRecord.getAccountAuthorizations().add(accountAuthorization);
        }
        return bankAccountRecord;
    }
}
