import business.MicoachBusinessObject;
import listener.InvoceMethodListener;
import model.ResponseModel;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import provider.UserProvider;

import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


@Listeners(InvoceMethodListener.class)
public class MicoachMigrationTest extends BaseTest {

    private static final int USER_COUNT = 1;
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
        ResponseModel loginResponse = micoachBusinessObject.login();
        assertEquals(loginResponse.getStatusCode(), new Integer(200), "LOGIN FAIL : " + loginResponse.getBody());

        //MIGRATION_STATUS_START
        LOGGER.info("MIGRATION STATUS START");
        micoachBusinessObject.migration("started");

        //READ WORKOUTS
        LOGGER.info("READ WORKOUTS");
        assertTrue(micoachBusinessObject.readWorkouts(ITEMS_PER_PAGE), "MIGRATION ERROR for user : " + micoachBusinessObject.getCurrentUser());

        //READ CustomTrainings
        LOGGER.info("READ CustomTrainings");
        assertTrue(micoachBusinessObject.readCustomTrainings(ITEMS_PER_PAGE), "MIGRATION ERROR for user : " + micoachBusinessObject.getCurrentUser());

        //MIGRATION_STATUS_COMPLETED
        LOGGER.info("MIGRATION STATUS COMPLETED");
        micoachBusinessObject.migration("completed");

    }

}
