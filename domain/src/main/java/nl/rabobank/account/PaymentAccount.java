package nl.rabobank.account;

import lombok.Value;

/**
 * This is class for PAYMENT type of accounts.
 *
 * @author Sayali G
 */
@Value
public class PaymentAccount implements Account
{
    String accountNumber;
    String accountHolderName;
    Double balance;
}
