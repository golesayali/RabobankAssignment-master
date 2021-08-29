package nl.rabobank;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import nl.rabobank.mongo.MongoConfiguration;

/**
 * This is class for Account Authorization Application
 * which will setup the initial configurations for application
 *
 * @author Sayali G
 */
@SpringBootApplication
@Import(MongoConfiguration.class)
@Slf4j
public class AccountAuthorizationApplication
{
    public static void main(final String[] args)
    {
        SpringApplication.run(AccountAuthorizationApplication.class, args);

        log.info("Account Authorization Application Started");
    }

}
