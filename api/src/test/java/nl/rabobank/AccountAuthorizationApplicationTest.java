package nl.rabobank;

import nl.rabobank.controller.AccountAuthorizationController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountAuthorizationApplicationTest {
    @Autowired
    AccountAuthorizationController accountAuthorizationController;

    @Test
    public void contextLoads() {
        Assertions.assertThat(accountAuthorizationController).isNotNull();
    }
}