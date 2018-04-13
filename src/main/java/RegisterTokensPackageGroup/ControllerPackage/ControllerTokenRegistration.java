package RegisterTokensPackageGroup.ControllerPackage;

import RegisterTokensPackageGroup.ActionPackage.RegisterTokenAction;
import RegisterTokensPackageGroup.ModelPackage.RegisterTokensModel;
import RegisterTokensPackageGroup.ModelPackage.RegisterTokensModelInterface;

import javax.naming.NamingException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/RegisterToken")
public class ControllerTokenRegistration {

    /**
     * @apiNote Endpoint for android client Firebase token registration
     * @param uId user UUID
     * @param token Firebase client token
     */
    @POST
    @Path("/Register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void registerToken(@NotNull @FormParam("uId") String uId, @NotNull @FormParam("token") String token){
        RegisterTokensModelInterface RTMI = new RegisterTokensModel(uId,token);
        try {
            RegisterTokenAction tokenAction = new RegisterTokenAction(RTMI);
            tokenAction.fireToMongoDb();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @POST
    @Path("/ChangeUUID")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void changeUUID(@NotNull @FormParam("oldUUID") String oldUUID,
                           @NotNull @FormParam("newUUID") String newUUID){
        RegisterTokenAction.changeUID(oldUUID,newUUID);
    }
}