package nl.rabobank.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.dto.CreateAuthorizationRequest;
import nl.rabobank.dto.CreateAuthorizationResponse;
import nl.rabobank.dto.RetrieveAuthorizationsResponse;
import nl.rabobank.mongo.entity.BankAccount;
import nl.rabobank.service.AccountAuthorizationService;
import nl.rabobank.util.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This is controller class for Account Authorizations Service
 *
 * @author Sayali G
 */
@Api(value = AccountAuthorizationController.ACCOUNT_AUTH_API_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value = AccountAuthorizationController.ACCOUNT_AUTH_API_PATH, produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
@Slf4j
public class AccountAuthorizationController {
    public static final String ACCOUNT_AUTH_API_PATH = "/v1/account";

    @Autowired
    AccountAuthorizationService accountAuthorizationService;

    @ApiOperation(value = "Create write or read access for payments and savings accounts",
            httpMethod = "POST",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Not authorized"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping("/authorization")
    public ResponseEntity<CreateAuthorizationResponse> createAuthorization(
            @RequestBody CreateAuthorizationRequest createAuthorizationRequest) {

        log.info("Account holder [{}] is granting authorization for user [{}] on account [{}]",
                createAuthorizationRequest.getGrantorName(),
                createAuthorizationRequest.getGranteeName(),
                createAuthorizationRequest.getAccountNumber());

        accountAuthorizationService.createAuthorization(DTOMapper.mapRequestToServiceInput(createAuthorizationRequest));

        log.info("Successfully granted access to grantee : {}",
                createAuthorizationRequest.getGranteeName());

        return new ResponseEntity<>(
                CreateAuthorizationResponse.builder()
                        .message("Successfully granted access to grantee : "+ createAuthorizationRequest.getGranteeName())
                        .build(),
                HttpStatus.CREATED);
    }

    @ApiOperation(
            value = "Retrieve a list of accounts user has access for",
            httpMethod = "GET",
            response = RetrieveAuthorizationsResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Not authorized"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping("/authorization/{granteeName}")
    public ResponseEntity<RetrieveAuthorizationsResponse> retrieveAuthorizations(
            @ApiParam(name = "granteeName", value = "Grantee Name", required = true)
            @PathVariable String granteeName) {

        log.info("Retrieving list of authorized accounts for user [{}]", granteeName);
        List<BankAccount> bankAccounts = accountAuthorizationService.getAccountsForGrantee(granteeName);
        return new ResponseEntity<>(DTOMapper.mapServiceOutputToResponse(bankAccounts, granteeName),
                HttpStatus.OK);
    }

}
