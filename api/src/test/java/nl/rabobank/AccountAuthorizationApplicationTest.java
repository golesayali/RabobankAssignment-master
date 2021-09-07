package nl.rabobank;

import nl.rabobank.controller.AccountAuthorizationController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountAuthorizationApplicationTest {
    @Autowired
    AccountAuthorizationController accountAuthorizationController;

    @Test
    void contextLoads() {
        Assertions.assertThat(accountAuthorizationController).isNotNull();
    }

}