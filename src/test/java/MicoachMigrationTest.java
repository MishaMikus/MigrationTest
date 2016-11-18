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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


@Listeners(InvoceMethodListener.class)
public class MicoachMigrationTest extends BaseTest {

    private static final int USER_COUNT = 100;
    private static final int USER_THREAD_COUNT = 5;

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

        //MIGRATION_STATUS_START
        String migrationStatus="started";
        LOGGER.info("MIGRATION STATUS START : "+migrationStatus);
        ResponseModel migrateResponse = micoachBusinessObject.migration(migrationStatus);
        assertEquals(migrateResponse.getStatusCode(), new Integer(201), "MIGRATION_STATUS("+migrationStatus+")_POST FAIL : " + migrateResponse.getBody());

        //READ WORKOUTS
        LOGGER.info("READ WORKOUTS");
        assertTrue(micoachBusinessObject.readWorkouts(ITEMS_PER_PAGE), "MIGRATION ERROR for user : " + micoachBusinessObject.getCurrentUser());

//        //READ CustomTrainings
//        LOGGER.info("READ CustomTrainings");
//        assertTrue(micoachBusinessObject.readCustomTrainings(ITEMS_PER_PAGE), "MIGRATION ERROR for user : " + micoachBusinessObject.getCurrentUser());

        //MIGRATION_STATUS_COMPLETED
        migrationStatus="completed";
        LOGGER.info("MIGRATION STATUS COMPLETED : "+migrationStatus);
        migrateResponse = micoachBusinessObject.migration(migrationStatus);
        assertEquals(migrateResponse.getStatusCode(), new Integer(201), "MIGRATION_STATUS("+migrationStatus+")_POST FAIL : " + migrateResponse.getBody());
    }

    private void validateLoginResponseForMigration(ResponseModel loginResponse) {
        assertEquals(loginResponse.getStatusCode(), new Integer(200), "LOGIN FAIL : " + loginResponse.getBody());
        assertThat(loginResponse.getBody(), containsString("scope_user_trainings_read"));
        assertThat(loginResponse.getBody(), containsString("scope_user_migrate"));
    }

}
