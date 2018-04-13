package NotificationSenderPackageGroup.ControllerPackage;

import NotificationSenderPackageGroup.ActionPackage.SendFriendRequestNotification;
import NotificationSenderPackageGroup.ModelPackage.NotificationData;
import NotificationSenderPackageGroup.ModelPackage.NotificationDataInterface;
import NotificationSenderPackageGroup.ModelPackage.NotificationModel;
import NotificationSenderPackageGroup.ModelPackage.NotificationModelInterface;

import javax.naming.NamingException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/Notification")
public class NotificationController {

    /**
     * @param uId UUID of the client receiving the notification
     * @param notifBody The notification body
     * @param notifTitle The notification title
     */
    @POST
    @Path("/User")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void notifyUser(@NotNull @FormParam("uId") String uId,
                           @NotNull @FormParam("body") String notifBody,
                           @NotNull @FormParam("title") String notifTitle,
                           @NotNull @FormParam("status") String status){
        NotificationDataInterface NDI = new NotificationData(notifBody,notifTitle);
        NotificationModelInterface NMI = new NotificationModel(NDI);
        try {
            SendFriendRequestNotification requestNotification = new SendFriendRequestNotification(NMI);
            if(status.equalsIgnoreCase("user")) requestNotification.fireToFireBaseForUser(uId);
            else requestNotification.fireToFirebaseForGroup(uId);
        } catch (NamingException | IOException e) {
            e.printStackTrace(); // TODO : Debug
        }
    }
}