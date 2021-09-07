package nl.rabobank.util;

import nl.rabobank.account.PaymentAccount;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.exception.AccountAuthorizationException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AccountAuthorizationApplicationHelperTest {
    @Test
    void whenInstantiatingUtilsClass_shouldThrowException() {
        Throwable exception = assertThrows(IllegalStateException.class, AccountAuthorizationApplicationHelper::new);
        assertEquals("Helper Classes should not have public constructors", exception.getMessage());

    }

    @Test
    void whenAccountTypeIsPayment_shouldReturnPaymentAccountInstance() {
        assertThat(
                AccountAuthorizationApplicationHelper.determineAccountType("SAVINGS", "NL12RABO0214789630", "test")
                , instanceOf(SavingsAccount.class));
    }

    @Test
    void whenAccountTypeIsSavings_shouldReturnSavingsAccountInstance() {
        assertThat(
                AccountAuthorizationApplicationHelper.determineAccountType("PAYMENT", "NL12RABO0214789630", "test")
                , instanceOf(PaymentAccount.class));
    }

    @Test
    void whenAccountTypeIsInvalid_shouldThrowException() {
        Throwable exception = assertThrows(AccountAuthorizationException.class, () -> AccountAuthorizationApplicationHelper.determineAccountType("invalid", "NL12RABO0214789630", "test"));
        assertEquals("Invalid account type provided in request. Allowed values are PAYMENT,SAVINGS", exception.getMessage());
    }

    @Test
    void whenProvidedAuthorizationIsRead_shouldReturnREADEnum() {
        assertEquals(Authorization.READ,
                AccountAuthorizationApplicationHelper.determineAuthorization("READ"));
    }

    @Test
    void whenProvidedAuthorizationIsWrite_shouldReturnWRITEEnum() {
        assertEquals(Authorization.WRITE,
                AccountAuthorizationApplicationHelper.determineAuthorization("WRITE"));
    }

    @Test
    void whenProvidedAuthorizationIsInvalid_shouldThrowException() {
        Throwable exception = assertThrows(AccountAuthorizationException.class, () -> AccountAuthorizationApplicationHelper.determineAuthorization("read"));
        assertEquals("Invalid access type provided in request. Allowed values are READ, WRITE", exception.getMessage());

    }
}