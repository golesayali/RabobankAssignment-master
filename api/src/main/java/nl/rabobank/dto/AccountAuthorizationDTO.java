package nl.rabobank.dto;

import lombok.Builder;
import lombok.Value;

/**
 * This is DTO for account authorization object
 *
 * @author Sayali G
 */
@Value
@Builder
public class AccountAuthorizationDTO {
    private String accountNumber;
    private String accountType;
}
