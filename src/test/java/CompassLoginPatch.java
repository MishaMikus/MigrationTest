import business.CompassBusinessObject;
import listener.InvoceMethodListener;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.StringFormatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Listeners(InvoceMethodListener.class)
public class CompassLoginPatch extends BaseTest {
    private final String COMPASS_OUTPUT_DIR = "compass_output";
    private final String FS = File.separator;
    private final Logger LOGGER = Logger.getLogger(this.getClass());

    @BeforeClass
    public void setUp() throws IOException {
        prepare(this.getClass().getSimpleName());
        if (!new File(COMPASS_OUTPUT_DIR).exists()) {
            new File(COMPASS_OUTPUT_DIR).mkdir();
        }
    }

    @Test(groups = "migration")
    public void test() throws IOException, ParseException, InterruptedException {
        String email = "M_A_JGw1s0_6_1482939195920@mailinator.com";
        CompassBusinessObject compassBusinessObject = new CompassBusinessObject();
        LOGGER.info("TRY create " + email);
        String res = compassBusinessObject.loginPatch(CLIENT, PROTOCOL, email) ? "OK" : "ERROR";
        FileWriter fw = new FileWriter(COMPASS_OUTPUT_DIR + FS + "out.csv", true);
        fw.write(email + "\t" + res + System.getProperty("line.separator"));
        fw.close();
    }
}
