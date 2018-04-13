package NotificationSenderPackageGroup.ModelPackage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NotificationData implements NotificationDataInterface {

    @XmlElement private final String body;
    @XmlElement private final String title;

    public NotificationData(String body, String title) {
        this.body = body;
        this.title = title;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
