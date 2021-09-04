package nl.rabobank.util;

import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.dto.AccountAuthorizationDTO;
import nl.rabobank.dto.CreateAuthorizationRequest;
import nl.rabobank.dto.RetrieveAuthorizationsResponse;
import nl.rabobank.mongo.entity.BankAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains mapper methods for converting data objects to responses
 * and requests to data objects
 *
 * @author Sayali G
 */
public class DTOMapper {

    /**
     * Instantiates a new Dto mapper.
     */
    DTOMapper() {
        throw new IllegalStateException("Utility Classes should not have public constructors");
    }

    /**
     * Map service output to response retrieve authorizations response.
     *
     * @param authorizedAccountsForGrantee the authorized accounts for grantee
     * @param granteeName                  the grantee name
     * @return the retrieve authorizations response
     */
    public static RetrieveAuthorizationsResponse mapServiceOutputToResponse(
            List<BankAccount> authorizedAccountsForGrantee,
            String granteeName) {
        List<AccountAuthorizationDTO> authorizedAccountsList = new ArrayList<>();
        authorizedAccountsForGrantee.stream()
                .forEach(account ->
                        {
                            String accessType = account.getAccountAuthorizations().stream()
                                    .filter(p -> p.getGranteeName().equals(granteeName))
                                    .collect(Collectors.toList())
                                    .get(0).getAccessType();
                            authorizedAccountsList.add(AccountAuthorizationDTO.builder()
                                    .accountNumber(account.getAccountNumber())
                                    .accountType(account.getAccountType())
                                    .accessType(accessType)
                                    .build());
                        }
                );
        return RetrieveAuthorizationsResponse.builder()
                .granteeName(granteeName)
                .authorizedAccounts(authorizedAccountsList)
                .build();

    }

    /**
     * Map request to service input power of attorney.
     *
     * @param createAuthorizationRequest the create authorization request
     * @return the power of attorney
     */
    public static PowerOfAttorney mapRequestToServiceInput(
            CreateAuthorizationRequest createAuthorizationRequest) {

        return PowerOfAttorney.builder()
                .granteeName(createAuthorizationRequest.getGranteeName())
                .grantorName(createAuthorizationRequest.getGrantorName())
                .account(AccountAuthorizationApplicationHelper.determineAccountType(
                        createAuthorizationRequest.getAccountType(),
                        createAuthorizationRequest.getAccountNumber(),
                        createAuthorizationRequest.getGrantorName()))
                .authorization(AccountAuthorizationApplicationHelper.determineAuthorization(createAuthorizationRequest.getTypeOfAuthorization()))
                .build();
    }


}
