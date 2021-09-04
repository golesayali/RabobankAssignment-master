package nl.rabobank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.account.PaymentAccount;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.dto.CreateAuthorizationRequest;
import nl.rabobank.dto.RetrieveAuthorizationRequest;
import nl.rabobank.mongo.entity.AccountAuthorization;
import nl.rabobank.mongo.entity.BankAccount;
import nl.rabobank.service.AccountAuthorizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountAuthorizationControllerTest {

    @MockBean
    AccountAuthorizationService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test_createAuthorization_successfullyGrantsAuthorization() throws Exception {
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .granteeName("granteeName")
                .grantorName("grantorName")
                .accountType("PAYMENT")
                .accountNumber("0123456789")
                .typeOfAuthorization("READ")
                .build();

        doNothing().when(service)
                .createAuthorization(PowerOfAttorney.builder()
                        .grantorName("grantorName")
                        .granteeName("granteeName")
                        .account(new PaymentAccount("0123456789", "grantorName", 0.0))
                        .authorization(Authorization.READ)
                        .build());

        mockMvc.perform(post("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo("Successfully granted access to grantee : granteeName")));
    }

    @Test
    void test_createAuthorization_whenInvalidAccountType_shouldReturnErrorResponse() throws Exception {
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .granteeName("granteeName")
                .grantorName("grantorName")
                .accountType("Payment")
                .accountNumber("0123456789")
                .typeOfAuthorization("READ")
                .build();

        doNothing().when(service)
                .createAuthorization(PowerOfAttorney.builder()
                        .grantorName("grantorName")
                        .granteeName("granteeName")
                        .account(new PaymentAccount("0123456789", "grantorName", 0.0))
                        .authorization(Authorization.READ)
                        .build());

        mockMvc.perform(post("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",
                        equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message",
                        equalTo("Invalid account type provided in request. Allowed values are PAYMENT,SAVINGS")));
    }

    @Test
    void test_createAuthorization_whenInvalidAuthorizationType_shouldReturnErrorResponse() throws Exception {
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .granteeName("granteeName")
                .grantorName("grantorName")
                .accountType("PAYMENT")
                .accountNumber("0123456789")
                .typeOfAuthorization("RE")
                .build();

        doNothing().when(service)
                .createAuthorization(PowerOfAttorney.builder()
                        .grantorName("grantorName")
                        .granteeName("granteeName")
                        .account(new PaymentAccount("0123456789", "grantorName", 0.0))
                        .authorization(Authorization.READ)
                        .build());

        mockMvc.perform(post("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",
                        equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message",
                        equalTo("Invalid access type provided in request. Allowed values are READ, WRITE")));
    }

    @Test
    void test_createAuthorization_whenNullGranteeName_shouldReturnErrorResponse() throws Exception {
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .granteeName(null)
                .grantorName("grantorName")
                .accountType("PAYMENT")
                .accountNumber("0123456789")
                .typeOfAuthorization("READ")
                .build();

        doNothing().when(service)
                .createAuthorization(PowerOfAttorney.builder()
                        .grantorName("grantorName")
                        .granteeName(null)
                        .account(new PaymentAccount("0123456789", "grantorName", 0.0))
                        .authorization(Authorization.READ)
                        .build());

        mockMvc.perform(post("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",
                        equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message",
                        equalTo("granteeName should not be empty or null")));
    }

    @Test
    void test_createAuthorization_whenBlankBlankAccountType_shouldReturnErrorResponse() throws Exception {
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .granteeName("test_granteeName")
                .grantorName("grantorName")
                .accountType("")
                .accountNumber("0123456789")
                .typeOfAuthorization("READ")
                .build();

        doNothing().when(service)
                .createAuthorization(PowerOfAttorney.builder()
                        .grantorName("grantorName")
                        .granteeName("")
                        .account(new PaymentAccount("0123456789", "grantorName", 0.0))
                        .authorization(Authorization.READ)
                        .build());

        mockMvc.perform(post("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",
                        equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message",
                        equalTo("accountType should not be empty or null")));
    }

    @Test
    void test_retrieveAuthorizations_successfullyRetrievesAuthorizationsForProvidedGrantee() throws Exception {
        List<BankAccount> list = new ArrayList<>(
                Arrays.asList(
                        BankAccount.builder()
                                .accountType("PAYMENT").accountNumber("1234567890").accountHolderName("accHolder_1")
                                .accountAuthorizations(
                                        Arrays.asList(
                                                AccountAuthorization.builder().granteeName("test_grantee").accessType("READ").build()
                                        )
                                )
                                .build(),
                        BankAccount.builder().accountType("SAVINGS").accountNumber("2345678901").accountHolderName("accHolder_2")
                                .accountAuthorizations(
                                        Arrays.asList(
                                                AccountAuthorization.builder().granteeName("test_grantee").accessType("READ").build(),
                                                AccountAuthorization.builder().granteeName("grantee_1").accessType("WRITE").build()

                                        )
                                )
                                .build(),
                        BankAccount.builder().accountType("PAYMENT").accountNumber("3456789101").accountHolderName("accHolder_3")
                                .accountAuthorizations(
                                        Arrays.asList(
                                                AccountAuthorization.builder().granteeName("test_grantee").accessType("WRITE").build(),
                                                AccountAuthorization.builder().granteeName("grantee_1").accessType("READ").build(),
                                                AccountAuthorization.builder().granteeName("grantee_2").accessType("READ").build()
                                        )
                                )
                                .build()
                )
        );
        RetrieveAuthorizationRequest request = RetrieveAuthorizationRequest.builder()
                .granteeName("test_grantee")
                .build();

        when(service.getAccountsForGrantee("test_grantee"))
                .thenReturn(list);
        mockMvc.perform(get("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorizedAccounts", hasSize(3)))
                .andExpect(jsonPath("$.authorizedAccounts[0].accountNumber", equalTo(list.get(0).getAccountNumber())))
                .andExpect(jsonPath("$.authorizedAccounts[0].accountType", equalTo(list.get(0).getAccountType())))
                .andExpect(jsonPath("$.authorizedAccounts[0].accessType", equalTo("READ")))
                .andExpect(jsonPath("$.authorizedAccounts[1].accountNumber", equalTo(list.get(1).getAccountNumber())))
                .andExpect(jsonPath("$.authorizedAccounts[1].accountType", equalTo(list.get(1).getAccountType())))
                .andExpect(jsonPath("$.authorizedAccounts[1].accessType", equalTo("READ")))
                .andExpect(jsonPath("$.authorizedAccounts[2].accountNumber", equalTo(list.get(2).getAccountNumber())))
                .andExpect(jsonPath("$.authorizedAccounts[2].accountType", equalTo(list.get(2).getAccountType())))
                .andExpect(jsonPath("$.authorizedAccounts[2].accessType", equalTo("WRITE")));
    }

    @Test
    void test_retrieveAuthorizations_whenNoAuthorizationsPresent_shouldReturnEmptyList() throws Exception {
        RetrieveAuthorizationRequest request = RetrieveAuthorizationRequest.builder()
                .granteeName("test_grantee")
                .build();
        List<BankAccount> list = new ArrayList<>();
        when(service.getAccountsForGrantee(request.getGranteeName()))
                .thenReturn(list);
        mockMvc.perform(get("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorizedAccounts", hasSize(0)));
    }


    @Test
    void test_retrieveAuthorizations_whenNullGranteeName_shouldReturnErrorResponse() throws Exception {
        RetrieveAuthorizationRequest request = RetrieveAuthorizationRequest.builder()
                .granteeName(null)
                .build();
        List<BankAccount> list = new ArrayList<>();
        when(service.getAccountsForGrantee(null))
                .thenReturn(list);
        mockMvc.perform(get("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",
                        equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message",
                        equalTo("granteeName should not be empty or null")));
    }
}