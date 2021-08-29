package nl.rabobank.authorizations;

import lombok.Builder;
import lombok.Value;
import nl.rabobank.account.Account;

/**
 * A Power of Attorney is used when someone (grantor) wants to give access to his/her account to someone else (grantee).
 * This could be read access or write access. In this way the grantee can read/write in the grantors account.
 *
 * @author Sayali G
 */
@Value
@Builder(toBuilder = true)
public class PowerOfAttorney
{
    String granteeName;
    String grantorName;
    Account account;
    Authorization authorization;
}
