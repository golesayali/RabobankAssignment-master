package nl.rabobank.util;

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
public class ServiceUtils {
    ServiceUtils() {
        throw new IllegalStateException("Utility Classes should not have public constructors");
    }

    public static String determineAccountType(Account account) {
        if (account instanceof PaymentAccount) {
            return "PAYMENT";
        }
        if (account instanceof SavingsAccount) {
            return "SAVINGS";
        }
        return "";
    }

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
