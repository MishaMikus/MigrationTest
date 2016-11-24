import business.MicoachBusinessObject;
import listener.InvoceMethodListener;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import provider.UserProvider;

import java.io.IOException;

@Listeners(InvoceMethodListener.class)
public class MicoachLoginTest extends BaseTest {

    private static final int USER_COUNT = 1000;
    private static final int USER_THREAD_COUNT = 1;

    private final Logger LOGGER = Logger.getLogger(this.getClass());

    @BeforeClass
    public void setUp() throws IOException {
        prepare(this.getClass().getSimpleName());
    }

    @Test(groups = "migration", invocationCount = USER_COUNT, threadPoolSize = USER_THREAD_COUNT)
    public void micoachMigrationTest() throws IOException, ParseException, InterruptedException {

        //PREPARE CLIENT
        LOGGER.info("init Client :" + CLIENT.getClass() + "\t" + PROTOCOL + HOST);
        MicoachBusinessObject micoachBusinessObject = new MicoachBusinessObject(CLIENT, PROTOCOL, HOST);

        //PREPARE USER
        LOGGER.info("init USER :" + BASE_USER_NAME + "\t" + BASE_USER_PASSWORD + "\t" + USER_LIST_FILE_PATH);
        micoachBusinessObject.setCurrentUser(UserProvider.migrationUser(BASE_USER_NAME, BASE_USER_PASSWORD, USER_LIST_FILE_PATH));

        //LOGIN
        LOGGER.info("LOGIN USER");
        validateLoginResponseForMigration(micoachBusinessObject.login());
    }

}
