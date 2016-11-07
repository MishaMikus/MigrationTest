import business.MicoachBusinessObject;
import com.jayway.restassured.RestAssured;
import listener.InvoceMethodListener;
import model.ResponseModel;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import provider.UserProvider;

import java.io.IOException;
import java.util.Date;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Listeners(InvoceMethodListener.class)
public class MicoachMigrationTest extends BaseTest {

    private static final int USER_COUNT = 100;
    private static final int USER_THREAD_COUNT = 1;

    private String BASE_USER_NAME;
    private String BASE_USER_PASSWORD;
    private String USER_LIST_FILE_PATH;

    @BeforeClass
    public void setUp() throws IOException {
        initProperties();
        BASE_USER_NAME = prop.getProperty("authorization.basic.web.userName", "Auto_client");
        BASE_USER_PASSWORD = prop.getProperty("authorization.basic.web.password", "Auto_client");
        USER_LIST_FILE_PATH = prop.getProperty("user.list.file", "users\\userList_devdesign");
        RestAssured.baseURI = "https://" + prop.getProperty("server.host", "devdesign.micoach.adidas.com");
    }

    @Test(groups = "migration", invocationCount = USER_COUNT, threadPoolSize = USER_THREAD_COUNT)
    public void micoachMigrationTest() throws IOException, ParseException, InterruptedException {
        Date startDate=new Date();
        //PREPARE
        MicoachBusinessObject micoachBusinessObject = new MicoachBusinessObject();
        micoachBusinessObject.setCurrentUser(UserProvider.migrationUser(BASE_USER_NAME, BASE_USER_PASSWORD, USER_LIST_FILE_PATH));

        //LOGIN
        ResponseModel loginResponse = micoachBusinessObject.login();
        assertEquals(loginResponse.getStatusCode(), new Integer(200), "LOGIN FAIL : " + loginResponse.getBody());

        //READ WORKOUTS
        Integer itemsPerPage = 250;
        assertTrue(micoachBusinessObject.readWorkouts(itemsPerPage), "MIGRATION ERROR for user : " + micoachBusinessObject.getCurrentUser());

        //READ CustomTrainings
        itemsPerPage = 250;
        assertTrue(micoachBusinessObject.readCustomTrainings(itemsPerPage), "MIGRATION ERROR for user : " + micoachBusinessObject.getCurrentUser());

        //STORE RESULT
        micoachBusinessObject.saveMigrationReport(startDate);
    }
}
