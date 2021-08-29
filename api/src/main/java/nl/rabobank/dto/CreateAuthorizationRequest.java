package nl.rabobank.dto;

import lombok.Value;

/**
 * This is class for request for Create Authorization API
 *
 * @author Sayali G
 */
@Value
public class CreateAuthorizationRequest {
    private String granteeName;
    private String grantorName;
    private String accountNumber;
    private String accountType;
    private String typeOfAuthorization;
}
