import client.BaseClient;
import client.BaseRestAssureClient;
import listener.InvoceMethodListener;
import org.testng.annotations.Listeners;

import java.io.*;
import java.util.Properties;

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


}
