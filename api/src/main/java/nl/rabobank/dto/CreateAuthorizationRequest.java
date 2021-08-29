package nl.rabobank.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is class for request for Create Authorization API
 *
 * @author Sayali G
 */
@Data
@Builder
public class CreateAuthorizationRequest {
    private String granteeName;
    private String grantorName;
    private String accountNumber;
    private String accountType;
    private String typeOfAuthorization;
}
