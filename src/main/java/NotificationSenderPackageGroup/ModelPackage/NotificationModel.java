package NotificationSenderPackageGroup.ModelPackage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NotificationModel implements NotificationModelInterface {
    @XmlElement private final NotificationDataInterface model;

    public NotificationModel(NotificationDataInterface model) {
        this.model = model;
    }

    @Override
    public NotificationDataInterface getModel() {
        return model;
    }

}
