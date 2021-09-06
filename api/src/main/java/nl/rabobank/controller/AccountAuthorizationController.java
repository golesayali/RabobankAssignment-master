package nl.rabobank.controller;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.dto.CreateAuthorizationRequest;
import nl.rabobank.dto.CreateAuthorizationResponse;
import nl.rabobank.dto.RetrieveAuthorizationRequest;
import nl.rabobank.dto.RetrieveAuthorizationsResponse;
import nl.rabobank.mongo.entity.BankAccount;
import nl.rabobank.service.AccountAuthorizationService;
import nl.rabobank.util.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * This is controller class for Account Authorizations Service
 *
 * @author Sayali G
 */
@Api(value = AccountAuthorizationController.ACCOUNT_AUTH_API_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value = AccountAuthorizationController.ACCOUNT_AUTH_API_PATH, produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
@AllArgsConstructor
@Slf4j
public class AccountAuthorizationController {
    /**
     * The constant ACCOUNT_AUTH_API_PATH.
     */
    public static final String ACCOUNT_AUTH_API_PATH = "/v1/account/authorization";

    /**
     * The Account authorization service.
     */
    @Autowired
    AccountAuthorizationService accountAuthorizationService;

    /**
     * Users can create write or read access for payments and savings accounts.
     *
     * @param createAuthorizationRequest the create authorization request
     * @return the response entity
     */
    @ApiOperation(value = "Create write or read access for payments and savings accounts",
            httpMethod = "POST",
            response = CreateAuthorizationResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Not authorized"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping
    public ResponseEntity<CreateAuthorizationResponse> createAuthorization(
            @RequestBody @Valid CreateAuthorizationRequest createAuthorizationRequest) {

        log.info("Account holder [{}] is granting authorization for user [{}] on account [{}]",
                createAuthorizationRequest.getGrantorName(),
                createAuthorizationRequest.getGranteeName(),
                createAuthorizationRequest.getAccountNumber());

        accountAuthorizationService.createAuthorization(DTOMapper.mapRequestToServiceInput(createAuthorizationRequest));

        log.info("Successfully granted access to grantee : {}",
                createAuthorizationRequest.getGranteeName());

        return new ResponseEntity<>(
                CreateAuthorizationResponse.builder()
                        .message("Successfully granted access to grantee : " + createAuthorizationRequest.getGranteeName())
                        .build(),
                HttpStatus.CREATED);
    }

    /**
     * Users can retrieve a list of accounts they have read or write access for.
     *
     * @param retrieveAuthorizationRequest the retrieve authorization request
     * @return the response entity
     */
    @ApiOperation(
            value = "Retrieve a list of accounts user has access for",
            httpMethod = "GET",
            response = RetrieveAuthorizationsResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Not authorized"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping
    public ResponseEntity<RetrieveAuthorizationsResponse> retrieveAuthorizations(
            @ApiParam(name = "granteeName", value = "Grantee Name", required = true)
            @RequestBody @Valid RetrieveAuthorizationRequest retrieveAuthorizationRequest) {

        log.info("Retrieving list of authorized accounts for user [{}]", retrieveAuthorizationRequest.getGranteeName());
        List<BankAccount> bankAccounts = accountAuthorizationService.getAccountsForGrantee(retrieveAuthorizationRequest.getGranteeName());
        return new ResponseEntity<>(DTOMapper.mapServiceOutputToResponse(bankAccounts, retrieveAuthorizationRequest.getGranteeName()),
                HttpStatus.OK);
    }

}
