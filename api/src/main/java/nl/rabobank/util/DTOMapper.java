package nl.rabobank.util;

import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.dto.AccountAuthorizationDTO;
import nl.rabobank.dto.CreateAuthorizationRequest;
import nl.rabobank.dto.RetrieveAuthorizationsResponse;
import nl.rabobank.mongo.entity.BankAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains mapper methods for converting data objects to responses
 * and requests to data objects
 *
 * @author Sayali G
 */
public class DTOMapper {

    DTOMapper() {
        throw new IllegalStateException("Utility Classes should not have public constructors");
    }

    public static RetrieveAuthorizationsResponse mapServiceOutputToResponse(
            List<BankAccount> authorizedAccountsForGrantee,
            String granteeName) {
        List<AccountAuthorizationDTO> authorizedAccountsList = new ArrayList<>();
        authorizedAccountsForGrantee.stream()
                .forEach(account ->
                        authorizedAccountsList.add(AccountAuthorizationDTO.builder()
                                .accountNumber(account.getAccountNumber())
                                .accountType(account.getAccountType())
                                .build())
                );
        return RetrieveAuthorizationsResponse.builder()
                .granteeName(granteeName)
                .authorizedAccounts(authorizedAccountsList)
                .build();

    }

    public static PowerOfAttorney mapRequestToServiceInput(
            CreateAuthorizationRequest createAuthorizationRequest) {

        return PowerOfAttorney.builder()
                .granteeName(createAuthorizationRequest.getGranteeName())
                .grantorName(createAuthorizationRequest.getGrantorName())
                .account(ApplicationUtils.determineAccountType(
                        createAuthorizationRequest.getAccountType(),
                        createAuthorizationRequest.getAccountNumber(),
                        createAuthorizationRequest.getGrantorName()))
                .authorization(ApplicationUtils.determineAuthorization(createAuthorizationRequest.getTypeOfAuthorization()))
                .build();
    }


}
