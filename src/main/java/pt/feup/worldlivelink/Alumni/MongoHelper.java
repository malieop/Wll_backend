package pt.feup.worldlivelink.Alumni;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoHelper {

    private static final String mongoURL = "mongodb+srv://diogocoelho:lgp5d2019@mongocloud-jf8zo.azure.mongodb.net/test?retryWrites=true";
    private static final String mongoDataBase = "LGP5D";
    private static final String mongoDocument = "users";

    public static MongoCollection getCollection(MongoClient mongoClient) {

        MongoDatabase database = mongoClient.getDatabase(mongoDataBase);
        MongoCollection collection = database.getCollection(mongoDocument);

        return collection;
    }

    public static MongoClient getMongoClient() {
        return MongoClients.create(mongoURL);
    }

}
