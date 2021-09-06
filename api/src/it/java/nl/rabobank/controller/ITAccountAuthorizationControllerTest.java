package nl.rabobank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import nl.rabobank.dto.CreateAuthorizationRequest;
import nl.rabobank.dto.RetrieveAuthorizationRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * The type Account authorization controller integ test.
 */
@AutoConfigureMockMvc
@AutoConfigureDataMongo
@SpringBootTest
class ITAccountAuthorizationControllerTest {

    private static final String CONNECTION_STRING = "mongodb://%s:%d";
    private static final String COLLECTION_NAME = "bankaccount";
    private static final String FILE_PATH = "src/it/resources/bankaccount_it.txt";

    private static MongodExecutable mongodExecutable;

    @Autowired
    private static MongoTemplate mongoTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    /**
     * Init all.
     *
     * @throws Exception the exception
     */
    @BeforeEach
    void initAll() throws Exception {
        setupEmbeddedDbAndCollection();
        loadUpInitialDataInMongo();
    }

    /**
     * Clean.
     */
    @AfterEach
    void clean() {
        mongoTemplate.dropCollection(COLLECTION_NAME);
        mongodExecutable.stop();
    }

    /**
     * Sets embedded db and collection.
     *
     * @throws Exception the exception
     */
    void setupEmbeddedDbAndCollection() throws Exception {
        String ip = "localhost";
        int port = 27017;

        ImmutableMongodConfig mongodConfig = MongodConfig
                .builder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(ip, port, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
        mongoTemplate = new MongoTemplate(MongoClients.create(String.format(CONNECTION_STRING, ip, port)), "test");
    }

    /**
     * When grantee already present should update rights.
     *
     * @throws Exception the exception
     */
    @DisplayName("Given that grantee already has WRITE rights on account"
            + " | When READ authorization is granted for same account"
            + " | Then the existing rights entry should be updated")
    @Test
    void whenGranteeAlreadyPresent_shouldUpdateRights() throws Exception {
        //given
        mockMvc.perform(get("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(RetrieveAuthorizationRequest.builder()
                        .granteeName("Vision")
                        .build())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorizedAccounts", hasSize(1)))
                .andExpect(jsonPath("$.authorizedAccounts[0].accountNumber", equalTo("NL16RABO0222222222")))
                .andExpect(jsonPath("$.authorizedAccounts[0].accountType", equalTo("PAYMENT")))
                .andExpect(jsonPath("$.authorizedAccounts[0].accessType", equalTo("WRITE")));
        //when
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .granteeName("Vision")
                .grantorName("Wanda")
                .accountType("PAYMENT")
                .accountNumber("NL16RABO0222222222")
                .typeOfAuthorization("READ")
                .build();
        mockMvc.perform(post("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo("Successfully granted access to grantee : Vision")));

        //then
        mockMvc.perform(get("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(RetrieveAuthorizationRequest.builder()
                        .granteeName("Vision")
                        .build())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorizedAccounts", hasSize(1)))
                .andExpect(jsonPath("$.authorizedAccounts[0].accountNumber", equalTo("NL16RABO0222222222")))
                .andExpect(jsonPath("$.authorizedAccounts[0].accountType", equalTo("PAYMENT")))
                .andExpect(jsonPath("$.authorizedAccounts[0].accessType", equalTo("READ")));
    }

    /**
     * When same grantee added to new account.
     *
     * @throws Exception the exception
     */
    @DisplayName("Given that grantee already has rights on some accounts"
            + " | When authorization is given to same grantee for other account "
            + " | Then we should be able to retrieve all these accounts info for the grantee")
    @Test
    void whenSameGranteeAddedToNewAccount() throws Exception {
        //given
        mockMvc.perform(get("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(RetrieveAuthorizationRequest.builder()
                        .granteeName("Captain America")
                        .build())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorizedAccounts", hasSize(2)))
                .andExpect(jsonPath("$.authorizedAccounts[0].accountNumber", equalTo("NL46RABO0123456789")))
                .andExpect(jsonPath("$.authorizedAccounts[0].accountType", equalTo("PAYMENT")))
                .andExpect(jsonPath("$.authorizedAccounts[0].accessType", equalTo("READ")))
                .andExpect(jsonPath("$.authorizedAccounts[1].accountNumber", equalTo("NL32RABO0111111111")))
                .andExpect(jsonPath("$.authorizedAccounts[1].accountType", equalTo("SAVINGS")))
                .andExpect(jsonPath("$.authorizedAccounts[1].accessType", equalTo("WRITE")));
        //when
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .granteeName("Captain America")
                .grantorName("Wanda")
                .accountType("PAYMENT")
                .accountNumber("NL16RABO0222222222")
                .typeOfAuthorization("READ")
                .build();
        mockMvc.perform(post("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo("Successfully granted access to grantee : Captain America")));

        //then
        mockMvc.perform(get("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(RetrieveAuthorizationRequest.builder()
                        .granteeName("Captain America")
                        .build())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorizedAccounts", hasSize(3)))
                .andExpect(jsonPath("$.authorizedAccounts[0].accountNumber", equalTo("NL46RABO0123456789")))
                .andExpect(jsonPath("$.authorizedAccounts[0].accountType", equalTo("PAYMENT")))
                .andExpect(jsonPath("$.authorizedAccounts[0].accessType", equalTo("READ")))
                .andExpect(jsonPath("$.authorizedAccounts[1].accountNumber", equalTo("NL32RABO0111111111")))
                .andExpect(jsonPath("$.authorizedAccounts[1].accountType", equalTo("SAVINGS")))
                .andExpect(jsonPath("$.authorizedAccounts[1].accessType", equalTo("WRITE")))
                .andExpect(jsonPath("$.authorizedAccounts[2].accountNumber", equalTo("NL16RABO0222222222")))
                .andExpect(jsonPath("$.authorizedAccounts[2].accountType", equalTo("PAYMENT")))
                .andExpect(jsonPath("$.authorizedAccounts[2].accessType", equalTo("READ")));
    }

    /**
     * When new account is added to account.
     *
     * @throws Exception the exception
     */
    @DisplayName("Given the account having some grantees with rights"
            + " | When authorization is given to new grantee for this account "
            + " | Then we should be able to retrieve this account info for the new grantee added")
    @Test
    void whenNewAccountIsAddedToAccount() throws Exception {
        //given
        mockMvc.perform(get("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(RetrieveAuthorizationRequest.builder()
                        .granteeName("Captain Marvel")
                        .build())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorizedAccounts", hasSize(0)));
        //when
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .granteeName("Captain Marvel")
                .grantorName("Wanda")
                .accountType("PAYMENT")
                .accountNumber("NL16RABO0222222222")
                .typeOfAuthorization("READ")
                .build();
        mockMvc.perform(post("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo("Successfully granted access to grantee : Captain Marvel")));

        //then
        mockMvc.perform(get("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(RetrieveAuthorizationRequest.builder()
                        .granteeName("Captain Marvel")
                        .build())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorizedAccounts", hasSize(1)))
                .andExpect(jsonPath("$.authorizedAccounts[0].accountNumber", equalTo("NL16RABO0222222222")))
                .andExpect(jsonPath("$.authorizedAccounts[0].accountType", equalTo("PAYMENT")))
                .andExpect(jsonPath("$.authorizedAccounts[0].accessType", equalTo("READ")));
    }

    @DisplayName("Given that grantee already has WRITE rights on account"
            + " | When READ authorization is granted for same account and exception is encountered"
            + " | Then the existing rights entry should not be updated")
    @Test
    void whenExceptionOccursDuringAuthorization_shouldNotUpdateBankAccount() throws Exception {
        //given
        mockMvc.perform(get("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(RetrieveAuthorizationRequest.builder()
                        .granteeName("Spiderman")
                        .build())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorizedAccounts", hasSize(1)))
                .andExpect(jsonPath("$.authorizedAccounts[0].accountNumber", equalTo("NL46RABO0123456789")))
                .andExpect(jsonPath("$.authorizedAccounts[0].accountType", equalTo("PAYMENT")))
                .andExpect(jsonPath("$.authorizedAccounts[0].accessType", equalTo("READ")));
        //when
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .granteeName("Spiderman")
                .grantorName("Tony stark")
                .accountType("invalidPayment")
                .accountNumber("NL46RABO0123456789")
                .typeOfAuthorization("WRITE")
                .build();
        mockMvc.perform(post("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",
                        equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message",
                        equalTo("Invalid account type provided in request. Allowed values are PAYMENT,SAVINGS")));

        //then
        mockMvc.perform(get("/v1/account/authorization")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(RetrieveAuthorizationRequest.builder()
                        .granteeName("Spiderman")
                        .build())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorizedAccounts", hasSize(1)))
                .andExpect(jsonPath("$.authorizedAccounts[0].accountNumber", equalTo("NL46RABO0123456789")))
                .andExpect(jsonPath("$.authorizedAccounts[0].accountType", equalTo("PAYMENT")))
                .andExpect(jsonPath("$.authorizedAccounts[0].accessType", equalTo("READ")));
    }

    private static void loadUpInitialDataInMongo() {
        try {
            for (Object line : Files.readAllLines(Paths.get(FILE_PATH), StandardCharsets.UTF_8)) {
                mongoTemplate.save(line, COLLECTION_NAME);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not import file: " + FILE_PATH, e);
        }
    }

}
