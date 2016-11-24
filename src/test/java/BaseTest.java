import client.BaseClient;
import client.BaseRestAssureClient;
import listener.InvoceMethodListener;
import model.ResponseModel;
import org.json.simple.JSONArray;
import org.testng.annotations.Listeners;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Listeners(InvoceMethodListener.class)
public class BaseTest {

    static final int USER_COUNT = 1;
    static final int USER_THREAD_COUNT = 1;

    static final int WORKOUT_COUNT = 50;

    Integer ITEMS_PER_PAGE = 250;
    String BASE_USER_NAME;
    String BASE_USER_PASSWORD;
    String PROTOCOL = "https://";
    String HOST;
    String USER_LIST_FILE_PATH;

    //private static final BaseClient CLIENT = new BaseHTTPClient();
    static final BaseClient CLIENT = new BaseRestAssureClient();

    Properties prop = new Properties();

    void initProperties() throws IOException {
        try {
            InputStream input = new FileInputStream("config.properties");
            prop.load(input);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void prepare(String simpleName) throws IOException {
        initProperties();
        BASE_USER_NAME = prop.getProperty("authorization.basic.web.userName", "Auto_client");
        BASE_USER_PASSWORD = prop.getProperty("authorization.basic.web.password", "Auto_client");
        USER_LIST_FILE_PATH = prop.getProperty("user.list.file", "users\\userList_devdesign");
        HOST = prop.getProperty("server.host");
        CLIENT.makeNewSummaryFile(simpleName);
        new File("target/report.csv").delete();
    }

    public void validateLoginResponseForMigration(ResponseModel loginResponse) {
        assertEquals(loginResponse.getStatusCode(), new Integer(200), "LOGIN FAIL : " + loginResponse.getBody());
        JSONArray scopeArray = (JSONArray) loginResponse.getBodyAsJson().get("scope");
        assertTrue(scopeArray.contains("scope_user_trainings_read"),"scopeArray : "+scopeArray);
        assertTrue(scopeArray.contains("scope_user_migrate"),"scopeArray : "+scopeArray);
    }

}
