package nl.rabobank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * This is class for request for Retrieve Authorization API
 *
 * @author Sayali G
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveAuthorizationRequest {
    @NotBlank(message = "granteeName should not be empty or null")
    String granteeName;
}
