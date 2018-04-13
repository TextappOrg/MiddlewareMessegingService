package RegisterTokensPackageGroup.ActionPackage;

import RegisterTokensPackageGroup.ModelPackage.RegisterTokensModel;
import RegisterTokensPackageGroup.ModelPackage.RegisterTokensModelInterface;
import UtilityPackage.ConnectionDetailsBrokerClass;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.naming.NamingException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterTokenAction {
    private final RegisterTokensModelInterface tokensModel;
    private MongoCollection mongoCollection;

    public RegisterTokenAction(RegisterTokensModelInterface tokensModel) throws NamingException {
        this.tokensModel = tokensModel;
        this.mongoCollection = ConnectionDetailsBrokerClass.getMongoCollection();
    }

    public RegisterTokenAction() throws NamingException {
        this.tokensModel = new RegisterTokensModel("","");
        this.mongoCollection = ConnectionDetailsBrokerClass.getMongoCollection();
    }

    @SuppressWarnings(value = "unchecked")
    public void fireToMongoDb(){
        Bson insertionForm = new Document("uId",this.tokensModel.getuId()).append("token",this.tokensModel.getToken());
        List<Document> result = (List<Document>) this.mongoCollection
                .find(new Document("uId",this.tokensModel.getuId()))
                .limit(1)
                .into(new ArrayList());
        if(!result.isEmpty()){
            String token = result.get(0).getString("token");
            if(!token.equals(this.tokensModel.getToken()))
                this.mongoCollection.updateOne(new Document("uId",this.tokensModel.getuId()),
                        new Document("token", this.tokensModel.getToken()));
        }else
            this.mongoCollection.insertOne(insertionForm);
    }

    @SuppressWarnings(value = "unchecked")
    public static void changeUID(@NotNull String oldUUID, @NotNull String newUUID) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                MongoCollection collection = ConnectionDetailsBrokerClass.getMongoCollection();
                Bson filter = new Document("uId",oldUUID);
                collection.findOneAndUpdate(filter,new Document("uId",newUUID));
            } catch (NamingException e) {
                e.printStackTrace();
            }
        });
    }
}