package nl.rabobank.service;

import nl.rabobank.account.PaymentAccount;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.mongo.entity.AccountAuthorization;
import nl.rabobank.mongo.entity.BankAccount;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountAuthorizationServiceHelperTest {

    @Test
    void whenInstantiatingServiceUtilsClass_shouldThrowException() {
        Throwable exception = assertThrows(IllegalStateException.class, () -> {
            new AccountAuthorizationServiceHelper();
        });
        assertEquals("Utility Classes should not have public constructors", exception.getMessage());

    }

    @Test
    void givenPaymentAccount_shouldReturnPaymentAccountType() {
        assertEquals(
                "PAYMENT",
                AccountAuthorizationServiceHelper.determineAccountType(new PaymentAccount("accNum", "accHolder", 0.0))
        );
    }

    @Test
    void givenSavingsAccount_shouldReturnSavingsAccountType() {
        assertEquals(
                "SAVINGS",
                AccountAuthorizationServiceHelper.determineAccountType(new SavingsAccount("accNum", "accHolder", 0.0))
        );
    }

    @Test
    void whenGranteeIsAlreadyPresentWithSameAccessForAccount_shouldKeepTheExistingEntryInAuthorizationList() {
        List<AccountAuthorization> accountAuthorizations = Arrays.asList(
                AccountAuthorization.builder().granteeName("ExistingGrantee").accessType("READ").build(),
                AccountAuthorization.builder().granteeName("Grantee_2").accessType("WRITE").build()
        );
        AccountAuthorization existingAuthorization = accountAuthorizations.get(0);
        BankAccount bankAccRecord = BankAccount.builder()
                .accountAuthorizations(accountAuthorizations)
                .build();
        BankAccount updatedBankAccRecord = AccountAuthorizationServiceHelper.checkIfUserIsAlreadyAuthorized(
                bankAccRecord,"ExistingGrantee", "READ"
        );
        assertEquals(updatedBankAccRecord.getAccountAuthorizations().size(), 2);
        assertTrue(updatedBankAccRecord.getAccountAuthorizations().contains(existingAuthorization));
    }

    @Test
    void whenGranteeIsAlreadyPresentWithDifferentAccessForAccount_shouldUpdateTheExistingEntryInAuthorizationList() {
        List<AccountAuthorization> accountAuthorizations = Arrays.asList(
                AccountAuthorization.builder().granteeName("ExistingGrantee").accessType("READ").build(),
                AccountAuthorization.builder().granteeName("Grantee_2").accessType("WRITE").build()
        );
        BankAccount bankAccRecord = BankAccount.builder()
                .accountAuthorizations(accountAuthorizations)
                .build();
        BankAccount updatedBankAccRecord = AccountAuthorizationServiceHelper.checkIfUserIsAlreadyAuthorized(
                bankAccRecord,"ExistingGrantee", "WRITE"
        );
        assertEquals(updatedBankAccRecord.getAccountAuthorizations().size(), 2);
        assertEquals(
                updatedBankAccRecord.getAccountAuthorizations().stream()
                        .filter(auth -> auth.getGranteeName().equals("ExistingGrantee"))
                        .findFirst().get().getAccessType(),
                Authorization.WRITE.name());
    }

    @Test
    void whenGranteeIsNotPresentInAccountAuthorizations_shouldAddNewEntryInAuthorizationList() {
        List<AccountAuthorization> accountAuthorizations = new ArrayList<AccountAuthorization>(
                Arrays.asList(AccountAuthorization.builder().granteeName("ExistingGrantee").accessType("READ").build(),
                AccountAuthorization.builder().granteeName("Grantee_1").accessType("WRITE").build()));
        BankAccount bankAccRecord = BankAccount.builder()
                .accountAuthorizations(accountAuthorizations)
                .build();
        BankAccount updatedBankAccRecord = AccountAuthorizationServiceHelper.checkIfUserIsAlreadyAuthorized(
                bankAccRecord,"Grantee_2", "WRITE"
        );
        assertEquals(updatedBankAccRecord.getAccountAuthorizations().size(), 3);
        assertEquals(
                updatedBankAccRecord.getAccountAuthorizations().stream()
                        .filter(auth -> auth.getGranteeName().equals("Grantee_2"))
                        .findFirst().get().getAccessType(),
                Authorization.WRITE.name());
    }

    @Test
    void whenExistingAuthorizationListIsEmpty_shouldAddNewEntryInAuthorizationList() {
        BankAccount bankAccRecord = BankAccount.builder()
                .accountAuthorizations(new ArrayList<AccountAuthorization>())
                .build();
        BankAccount updatedBankAccRecord = AccountAuthorizationServiceHelper.checkIfUserIsAlreadyAuthorized(
                bankAccRecord,"NewGrantee", "READ"
        );
        assertEquals(updatedBankAccRecord.getAccountAuthorizations().size(), 1);
        assertTrue(updatedBankAccRecord.getAccountAuthorizations().contains(
                AccountAuthorization.builder().granteeName("NewGrantee").accessType("READ").build()
        ));
    }

}