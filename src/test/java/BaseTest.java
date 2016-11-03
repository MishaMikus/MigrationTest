import listener.InvoceMethodListener;
import org.testng.annotations.Listeners;

import java.io.*;
import java.util.Properties;

@Listeners(InvoceMethodListener.class)
public class BaseTest {

    public Properties prop = new Properties();

    public void initProperties() throws IOException {
        try {
            InputStream input = new FileInputStream("config.properties");
            prop.load(input);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
