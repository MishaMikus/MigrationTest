import listener.InvoceMethodListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import provider.UserProvider;
import business.MicoachBusinessObject;

import java.io.IOException;

@Listeners(InvoceMethodListener.class)
public class MicoachSignUpDeleteUserUseCaseTest extends BaseTest {

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
