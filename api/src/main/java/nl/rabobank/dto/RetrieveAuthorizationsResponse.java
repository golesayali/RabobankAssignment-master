package nl.rabobank.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * This is class for response for Retrieve Authorization API
 *
 * @author Sayali G
 */
@Value()
@Builder()
public class RetrieveAuthorizationsResponse {
    String granteeName;
    private List<AccountAuthorizationDTO> authorizedAccounts;
}
