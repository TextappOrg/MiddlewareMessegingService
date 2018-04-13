package RegisterTokensPackageGroup.ModelPackage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegisterTokensModel implements RegisterTokensModelInterface {
    @XmlElement private final String uId;
    @XmlElement private final String token;

    public RegisterTokensModel(){
        this.uId = "";
        this.token = "";
    }

    public RegisterTokensModel(String uId, String token) {
        this.uId = uId;
        this.token = token;
    }

    @Override
    public String getuId() {
        return uId;
    }

    @Override
    public String getToken() {
        return token;
    }
}