import io.restassured.RestAssured;
import listener.InvoceMethodListener;
import model.ResponseModel;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import provider.UserProvider;
import business.MicoachBusinessObject;

import java.io.IOException;
import java.util.Date;
import static org.testng.Assert.assertNotNull;

@Listeners(InvoceMethodListener.class)
public class MicoachGenerateUserTest extends BaseTest {
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
