package nl.rabobank.mongo.entity;

import lombok.Builder;
import lombok.Data;

/**
 * This class contains entity for Account Authorizations.
 *
 * @author Sayali G
 */
@Data
@Builder
public class AccountAuthorization {
    private String granteeName;
    private String accessType;
}
