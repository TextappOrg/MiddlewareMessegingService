package UtilityPackage;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static com.google.cloud.firestore.FirestoreOptions.getDefaultInstance;


public class ConnectionDetailsBrokerClass {

    public static String getServerToken() throws NamingException{
        return (String) returnContextProperties().getOrDefault("Firebase_Server_Token", "");
    }

    @SuppressWarnings("unchecked")
    public static String getServerAccessToken()  {
        GoogleCredential googleCredential = new GoogleCredential();
        Set<String> scope;
        try {
            scope =(Set<String>)getDefaultInstance()
                    .getClass()
                    .getDeclaredField("SCOPES").get(new HashSet<String>());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            scope = new HashSet<>();
            e.printStackTrace();
        }

        try {
            googleCredential = GoogleCredential
                    .fromStream(new FileInputStream("simpletextingappnotif-firebase-adminsdk-6afhs-1d1b182f7d.json"))
                    .createScoped(scope);
            googleCredential.refreshToken();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(googleCredential.getAccessToken());
        return googleCredential.getAccessToken();
    }

    public static MongoCollection<? extends org.bson.Document> getMongoCollection() throws NamingException{
        Properties properties = returnContextProperties();
        String connectionString = (String) properties.getOrDefault("Mongodb_Connection_String","");
        String collectionName = (String) properties.getOrDefault("Collection_Name","");
        String dbName = (String) properties.getOrDefault("DBName","");
        return new MongoClient(new MongoClientURI(connectionString)).getDatabase(dbName).getCollection(collectionName);
    }

    private static Properties returnContextProperties() throws NamingException {
        InitialContext ctx = new InitialContext();
        return  (Properties) ctx.lookup(" FirebaseCloudMessagingMiddlewareCredentials");
    }

}
