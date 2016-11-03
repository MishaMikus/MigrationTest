import com.jayway.restassured.RestAssured;
import listener.InvoceMethodListener;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import provider.UserProvider;
import business.MicoachBusinessObject;

import java.io.IOException;

@Listeners(InvoceMethodListener.class)
public class MicoachSignUpDeleteUserUseCaseTest extends BaseTest {

    private String BASE_USER_NAME;
    private String BASE_USER_PASSWORD;


    @BeforeClass
    public void setUp() throws IOException {
        initProperties();
        BASE_USER_NAME = prop.getProperty("authorization.basic.web.userName", "Auto_client");
        BASE_USER_PASSWORD = prop.getProperty("authorization.basic.web.password", "Auto_client");
        RestAssured.baseURI = "https://" + prop.getProperty("server.host", "devdesign.micoach.adidas.com");
    }

    @Test
    public void signUpDeleteUserUseCaseTest() throws IOException {
        MicoachBusinessObject micoachBusinessObject = new MicoachBusinessObject();
        micoachBusinessObject.setCurrentUser(UserProvider.generateNewUser(BASE_USER_NAME, BASE_USER_PASSWORD));
        micoachBusinessObject.checkNewUser();
        micoachBusinessObject.signUp();
        micoachBusinessObject.login();
        micoachBusinessObject.loginWeb();
        micoachBusinessObject.delete();
    }
}
