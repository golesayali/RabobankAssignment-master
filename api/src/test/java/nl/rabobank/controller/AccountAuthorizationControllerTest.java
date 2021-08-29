package nl.rabobank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.account.PaymentAccount;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.dto.CreateAuthorizationRequest;
import nl.rabobank.service.AccountAuthorizationService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc*/
class AccountAuthorizationControllerTest {

    /*@MockBean
    AccountAuthorizationService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test_createAuthorization() throws Exception {
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .granteeName("granteeName")
                .grantorName("grantorName")
                .accountType("PAYMENT")
                .accountNumber("0123456789")
                .typeOfAuthorization("RAED")
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
    void test_retrieveAuthorizations() {

    }*/
}