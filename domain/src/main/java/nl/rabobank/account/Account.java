package nl.rabobank.account;

/**
 * This class contains interfaces for generic methods for all accounts.
 *
 * @author Sayali G
 */
public interface Account
{
    String getAccountNumber();
    String getAccountHolderName();
    Double getBalance();
}
