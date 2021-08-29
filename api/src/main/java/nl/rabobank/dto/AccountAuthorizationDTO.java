package nl.rabobank.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

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
}
