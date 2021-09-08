package nl.rabobank;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Disabled;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility for importing data for local testing.
 * This is temporarily added to test the application using postman if mongoimport tool does not work
 * For inserting data comment out @disabled annotation and uncomment @Test annotation.
 * Ensure that these changes are not committed to the master/main
 *
 */
class TestDataImportUtility {

    private static final String CONNECTION_STRING = "mongodb://%s:%d";
    private static final String DB_NAME = "test";
    private static final String COLLECTION_NAME = "bankaccount";
    private static final String FILE_PATH = "src/e2e-local/resources/bankaccount.json";

    @Disabled("Temporarily added to load test data in local mongodb server")
    //@Test - IF UNCOMMENTED, DO NOT COMMIT THIS TO MASTER
    void loadTestDataForPostmanTesting() {
        String ip = "localhost";
        int port = 27017;
        JSONParser jsonParser = new JSONParser();

        try {
            MongoClient client = MongoClients.create(String.format(CONNECTION_STRING, ip, port));

            MongoDatabase database = client.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            String reader = Files.readString(Paths.get(FILE_PATH), StandardCharsets.UTF_8);
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray bankAccountList = (JSONArray) obj;
            List<Document> jsonList = new ArrayList<Document>();

            //Iterate over bankaccount array
            bankAccountList.forEach(acc -> {
                Document doc = Document.parse(acc.toString());
                jsonList.add(doc);
            });
            //Insert the test data in local mongodb
            collection.insertMany(jsonList);
            client.close();

        } catch (Exception ex) {
           //do nothing. Since this is just a utility made for inserting data to local mongo
        }
    }
}
