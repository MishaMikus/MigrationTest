import client.BaseClient;
import client.BaseHTTPClient;
import client.BaseRestAssureClient;
import com.jayway.restassured.RestAssured;
import listener.InvoceMethodListener;
import model.ResponseModel;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import provider.UserProvider;
import business.MicoachBusinessObject;

import java.io.IOException;
import java.util.Date;
import static org.testng.Assert.assertNotNull;

@Listeners(InvoceMethodListener.class)
public class MicoachGenerateUserTest extends BaseTest {

    private static final int USER_COUNT = 1;
    private static final int USER_THREAD_COUNT = 1;
    private static final int WORKOUT_COUNT = 1;

    //private static final BaseClient CLIENT = new BaseHTTPClient();
    private static final BaseClient CLIENT = new BaseRestAssureClient();

    private String BASE_USER_NAME;
    private String BASE_USER_PASSWORD;
    private String PROTOCOL = "https://";
    private String HOST;

    @BeforeClass
    public void setUp() throws IOException {
        initProperties();
        BASE_USER_NAME = prop.getProperty("authorization.basic.web.userName");
        BASE_USER_PASSWORD = prop.getProperty("authorization.basic.web.password");
        HOST=prop.getProperty("server.host");
    }

    @Test(invocationCount = USER_COUNT, threadPoolSize = USER_THREAD_COUNT)
    public void generateUserTest() throws IOException, ParseException, InterruptedException {
        MicoachBusinessObject micoachBusinessObject = new MicoachBusinessObject(CLIENT, PROTOCOL, HOST);
        micoachBusinessObject.setCurrentUser(UserProvider.generateNewUser(BASE_USER_NAME, BASE_USER_PASSWORD));
        ResponseModel response = micoachBusinessObject.signUp();
        assertNotNull(micoachBusinessObject.getCurrentUser().getAccessToken(), "AccessToken not found. signUp error : " + response.getBody());
        micoachBusinessObject.login();
        micoachBusinessObject.makeAndPostSCWorkouts(WORKOUT_COUNT);
        micoachBusinessObject.makeAndPostGameWorkouts(WORKOUT_COUNT);
        micoachBusinessObject.makeAndPostFreeRunWorkouts(WORKOUT_COUNT);
        micoachBusinessObject.storeUser(micoachBusinessObject.getCurrentUser().getEmail()
                + "\t" + RestAssured.baseURI
                + "\t" + new Date()
                + "\t" + WORKOUT_COUNT + " workouts");
    }
}
