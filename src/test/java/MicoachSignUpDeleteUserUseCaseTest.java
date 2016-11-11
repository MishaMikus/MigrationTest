import client.BaseClient;
import client.BaseHTTPClient;
import client.BaseRestAssureClient;
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
    private String PROTOCOL = "https://";
    private String HOST;

    private static final int USER_COUNT = 1000;
    private static final int USER_THREAD_COUNT = 10;

    //private static final BaseClient CLIENT = new BaseHTTPClient();
    private static final BaseClient CLIENT = new BaseRestAssureClient();

    @BeforeClass
    public void setUp() throws IOException {
        initProperties();
        BASE_USER_NAME = prop.getProperty("authorization.basic.web.userName", "Auto_client");
        BASE_USER_PASSWORD = prop.getProperty("authorization.basic.web.password", "Auto_client");
        HOST=prop.getProperty("server.host");
    }

    @Test(groups = "migration", invocationCount = USER_COUNT, threadPoolSize = USER_THREAD_COUNT)
    public void signUpDeleteUserUseCaseTest() throws IOException {
        MicoachBusinessObject micoachBusinessObject = new MicoachBusinessObject(CLIENT,PROTOCOL,HOST);
        micoachBusinessObject.setCurrentUser(UserProvider.generateNewUser(BASE_USER_NAME, BASE_USER_PASSWORD));
        micoachBusinessObject.checkNewUser();
        micoachBusinessObject.signUp();
        micoachBusinessObject.login();
        micoachBusinessObject.loginWeb();
        micoachBusinessObject.delete();
    }
}
