package nl.rabobank.dto;

import lombok.Builder;
import lombok.Data;

/**
 * This is DTO for account authorization object
 *
 * @author Sayali G
 */
@Data
@Builder
public class AccountAuthorizationDTO {
    private String accountNumber;
    private String accountType;
    private String accessType;
}
