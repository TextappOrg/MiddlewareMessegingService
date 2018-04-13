package NotificationSenderPackageGroup.ActionPackage;

import NotificationSenderPackageGroup.ModelPackage.NotificationModelInterface;
import UtilityPackage.ConnectionDetailsBrokerClass;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import javax.naming.NamingException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SendFriendRequestNotification {

    private static final String serverToken = ConnectionDetailsBrokerClass.getServerAccessToken();

    private final NotificationModelInterface model;
    private MongoCollection mongoCollection;


    public SendFriendRequestNotification(NotificationModelInterface model) throws NamingException {
        this.model = model;
        this.mongoCollection = ConnectionDetailsBrokerClass.getMongoCollection();
    }

    public void fireToFireBaseForUser(String uId) throws IOException {
        fireToFireBaseForUser(makeJsonStringForFireBaseFromUid(uId, Boolean.FALSE), serverToken);
    }

    public void fireToFirebaseForGroup(String groupId) throws IOException {
        fireToFireBaseForUser(makeJsonStringForFireBaseFromUid(groupId, Boolean.TRUE), serverToken);
    }

    @SuppressWarnings(value = "unchecked")
    private String makeJsonStringForFireBaseFromUid(String uId, Boolean switchGroupOrUser) {
        Bson filter = new Document("uId",uId);
        List<Document> result = (List<Document>) this.mongoCollection.find(filter).into(new ArrayList());
        if(!result.isEmpty()){
            String token = result.get(0).getString("token");
            JSONObject jsonObjectInner = new JSONObject();
            jsonObjectInner.put("body",this.model.getModel().getBody());
            jsonObjectInner.put("title",this.model.getModel().getTitle());
            JSONObject jsonObjectMiddle = new JSONObject();
            if(switchGroupOrUser == Boolean.FALSE) jsonObjectMiddle.put("token",token);
            else jsonObjectMiddle.put("topic",token);
            jsonObjectMiddle.put("notification",jsonObjectInner);
            JSONObject jsonObjectOuter = new JSONObject();
            jsonObjectOuter.put("message",jsonObjectMiddle);
            return jsonObjectOuter.toString();
        }else return "";
    }

    private void fireToFireBaseForUser(String jsonInput, String serverTokenFireBase) throws IOException {
        URL url = new URL("https://fcm.googleapis.com/v1/projects/simpletextingappnotif/messages:send HTTP/1.1");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + serverTokenFireBase);

        try(OutputStreamWriter streamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream())) {
            streamWriter.write(jsonInput);
            streamWriter.flush();
            httpURLConnection.connect();
            System.out.println(httpURLConnection.getResponseCode()+ "\n" + httpURLConnection.getResponseMessage());
        }
    }
}
