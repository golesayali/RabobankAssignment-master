package nl.rabobank.service;

import nl.rabobank.account.PaymentAccount;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.exception.ResourceNotFoundException;
import nl.rabobank.mongo.entity.AccountAuthorization;
import nl.rabobank.mongo.entity.BankAccount;
import nl.rabobank.mongo.repository.BankAccountRepository;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountAuthorizationServiceImplTest {
    @InjectMocks
    AccountAuthorizationServiceImpl service;

    @Mock
    BankAccountRepository dao;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_createAuthorization() {
        PowerOfAttorney grantee = PowerOfAttorney.builder()
                .granteeName("GranteeName")
                .grantorName("GrantorName")
                .account(new PaymentAccount("0123456789", "accHolder", 0.0))
                .authorization(Authorization.READ)
                .build();

        BankAccount bankAccountDao = BankAccount.builder()
                .accountNumber("0123456789")
                .accountType("PAYMENT")
                .accountHolderName("accHolder")
                .accountAuthorizations(new ArrayList<AccountAuthorization>())
                .build();
        when(dao.findByAccountNumberAndAccountType("0123456789", "PAYMENT"))
                .thenReturn(Optional.of(bankAccountDao));
        service.createAuthorization(grantee);

        verify(dao, times(1)).findByAccountNumberAndAccountType("0123456789", "PAYMENT");
        verify(dao, times(1)).save(bankAccountDao);
    }

    @Test
    void whenCreatingAuthorization_ifAccountNotPresent_throwResourceNotFoundException() {
        PowerOfAttorney grantee = PowerOfAttorney.builder()
                .granteeName("GranteeName")
                .grantorName("GrantorName")
                .account(new PaymentAccount("0123456789", "accHolder", 0.0))
                .authorization(Authorization.READ)
                .build();
        when(dao.findByAccountNumberAndAccountType("0123456789", "PAYMENT"))
                .thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                service.createAuthorization(grantee));
    }

    @Test
    void test_getAccountsForGrantee_shouldReturnAllAccountsUserHasAccessTo() {
        PowerOfAttorney grantee = PowerOfAttorney.builder()
                .granteeName("GranteeName")
                .grantorName("GrantorName")
                .account(new PaymentAccount("0123456789", "accHolder", 0.0))
                .authorization(Authorization.READ)
                .build();

        List<BankAccount> authorizedAccList = new ArrayList<>(
                Arrays.asList(
                        BankAccount.builder()
                                .accountNumber("0123456789")
                                .accountType("PAYMENT")
                                .accountHolderName("accHolder_1")
                                .build(),
                        BankAccount.builder()
                                .accountNumber("0123456790")
                                .accountType("SAVINGS")
                                .accountHolderName("accHolder_2")
                                .build(),
                        BankAccount.builder()
                                .accountNumber("0123456791")
                                .accountType("SAVINGS")
                                .accountHolderName("accHolder_3")
                                .build()
                )
        );
        when(dao.findByAccountAuthorizationsGranteeName("GranteeName"))
                .thenReturn(authorizedAccList);
        List<BankAccount> accList = service.getAccountsForGrantee("GranteeName");

        verify(dao, times(1)).findByAccountAuthorizationsGranteeName("GranteeName");
        assertEquals(authorizedAccList.size(), accList.size());
    }

}