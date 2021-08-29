package nl.rabobank.account;

import lombok.Value;

/**
 * This is class for SAVINGS type of accounts.
 *
 * @author Sayali G
 */
@Value
public class SavingsAccount implements Account
{
    String accountNumber;
    String accountHolderName;
    Double balance;
}
