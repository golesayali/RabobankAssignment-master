package nl.rabobank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * This is class for request for Create Authorization API
 *
 * @author Sayali G
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAuthorizationRequest {

    @NotBlank(message = "granteeName should not be empty or null")
    private String granteeName;

    @NotBlank(message = "grantorName should not be empty or null")
    private String grantorName;

    @NotBlank(message = "accountNumber should not be empty or null")
    private String accountNumber;

    @NotBlank(message = "accountType should not be empty or null")
    private String accountType;

    @NotBlank(message = "typeOfAuthorization should not be empty or null")
    private String typeOfAuthorization;
}
