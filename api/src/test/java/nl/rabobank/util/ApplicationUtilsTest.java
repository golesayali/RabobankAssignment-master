package nl.rabobank.util;

import nl.rabobank.account.PaymentAccount;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.exception.AccountAuthorizationException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationUtilsTest {
    @Test
    void whenInstantiatingUtilsClass_shouldThrowException() {
        Throwable exception = assertThrows(IllegalStateException.class, () -> {
            new ApplicationUtils();
        });
        assertEquals("Utility Classes should not have public constructors", exception.getMessage());

    }

    @Test
    void whenAccountTypeIsPayment_shouldReturnPaymentAccountInstance() {
        assertThat(
                ApplicationUtils.determineAccountType("SAVINGS", "NL12ABNA0214789630", "test")
                , instanceOf(SavingsAccount.class));
    }

    @Test
    void whenAccountTypeIsSavings_shouldReturnSavingsAccountInstance() {
        assertThat(
                ApplicationUtils.determineAccountType("PAYMENT", "NL12ABNA0214789630", "test")
                , instanceOf(PaymentAccount.class));
    }

    @Test
    void whenAccountTypeIsInvalid_shouldThrowException() {
        Throwable exception = assertThrows(AccountAuthorizationException.class, () -> {
            ApplicationUtils.determineAccountType("invalid", "NL12ABNA0214789630", "test");
        });
        assertEquals("Invalid account type provided in request. Allowed values are PAYMENT,SAVINGS", exception.getMessage());
    }

    @Test
    void whenProvidedAuthorizationIsRead_shouldReturnREADEnum() {
        assertEquals(
                ApplicationUtils.determineAuthorization("READ")
                , Authorization.READ);
    }

    @Test
    void whenProvidedAuthorizationIsWrite_shouldReturnWRITEEnum() {
        assertEquals(
                ApplicationUtils.determineAuthorization("WRITE")
                , Authorization.WRITE);
    }

    @Test
    void whenProvidedAuthorizationIsInvalid_shouldThrowException() {
        Throwable exception = assertThrows(AccountAuthorizationException.class, () -> {
            ApplicationUtils.determineAuthorization("read");
        });
        assertEquals("Invalid access type provided in request. Allowed values are READ, WRITE", exception.getMessage());

    }
}