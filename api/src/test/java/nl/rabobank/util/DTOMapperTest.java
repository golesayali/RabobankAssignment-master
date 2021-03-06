package nl.rabobank.util;

import nl.rabobank.account.PaymentAccount;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.dto.AccountAuthorizationDTO;
import nl.rabobank.dto.CreateAuthorizationRequest;
import nl.rabobank.dto.RetrieveAuthorizationsResponse;
import nl.rabobank.mongo.entity.AccountAuthorization;
import nl.rabobank.mongo.entity.BankAccount;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DTOMapperTest {

    @Test
    void whenInstantiatingDTOMapperClass_shouldThrowException() {
        Throwable exception = assertThrows(IllegalStateException.class, DTOMapper::new);
        assertEquals("Helper Classes should not have public constructors", exception.getMessage());

    }

    @Test
    void givenAuthorizedAccountsList_shouldMapToRetrieveAuthorizationsResponse() {
        List<BankAccount> authorizedAccountsForGrantee = new ArrayList<>();
        authorizedAccountsForGrantee.add(BankAccount.builder()
                .accountNumber("test_AccNumber")
                .accountType("PAYMENT")
                .accountHolderName("test_accountHolderName")
                .accountAuthorizations(
                        Collections.singletonList(AccountAuthorization.builder().accessType("READ").granteeName("test_Grantee").build())
                )
                .build());
        String granteeName = "test_Grantee";
        RetrieveAuthorizationsResponse response = DTOMapper.mapServiceOutputToResponse(authorizedAccountsForGrantee, granteeName);
        assertNotNull(response);
        assertEquals(RetrieveAuthorizationsResponse.builder()
                        .authorizedAccounts(Collections.singletonList(AccountAuthorizationDTO.builder()
                                .accountNumber("test_AccNumber").accountType("PAYMENT").accessType("READ").build()))
                        .granteeName(granteeName)
                        .build(),
                response);
    }

    @Test
    void givenCreateAuthorizationRequest_shouldMapToServiceInput() {
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .granteeName("test_GranteeName")
                .grantorName("tests_grantorName")
                .accountNumber("tests_AccNumber")
                .accountType("PAYMENT")
                .typeOfAuthorization("READ")
                .build();
        PowerOfAttorney serviceInput = DTOMapper.mapRequestToServiceInput(request);
        assertNotNull(serviceInput);
        assertEquals(PowerOfAttorney.builder()
                        .grantorName("tests_grantorName")
                        .granteeName("test_GranteeName")
                        .account(new PaymentAccount("tests_AccNumber", "tests_grantorName", 0.0))
                        .authorization(Authorization.READ)
                        .build(),
                serviceInput);
    }
}