package nl.rabobank.dto;

import lombok.Builder;
import lombok.Value;

/**
 * This is class for response for Create Authorization API
 *
 * @author Sayali G
 */
@Value
@Builder
public class CreateAuthorizationResponse {
    String message;
}
