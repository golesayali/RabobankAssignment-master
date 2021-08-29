package nl.rabobank.controller;

import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.dto.CreateAuthorizationRequest;
import nl.rabobank.service.AccountAuthorizationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountAuthorizationController.class)
class AccountAuthorizationControllerTest {
    @MockBean
    AccountAuthorizationService accountAuthorizationService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void test_createAuthorizations() throws Exception {
        CreateAuthorizationRequest request = new CreateAuthorizationRequest(
                "","","","",""
        );
        PowerOfAttorney poa = PowerOfAttorney.builder().build();

        doNothing().when(accountAuthorizationService).createAuthorization(poa);

        mockMvc.perform(post("/")
                .requestAttr("accountNumber","NL46ABNA0123456789")
                .requestAttr("accountType", "PAYMENT")
                .requestAttr("granteeName", "TonyStark")
                .requestAttr("grantorName", "CaptainAmerica")
                .requestAttr("typeOfAuthorization", "WRITE"))
                .andExpect(status().isCreated());
    }
}