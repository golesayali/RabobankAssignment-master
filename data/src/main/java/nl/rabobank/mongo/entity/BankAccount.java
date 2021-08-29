package nl.rabobank.mongo.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


/**
 * This class contains entity for Bank accounts collection in mongo db.
 *
 * @author Sayali G
 */
@Data
@Builder
@Document(collection = "bankaccount")
public class BankAccount {
    @Id
    private String id;
    private String accountNumber;
    private String accountHolderName;
    private double balance;
    private String accountType;
    private List<AccountAuthorization> accountAuthorizations;

}
