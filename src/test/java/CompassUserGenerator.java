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
public class CompassUserGenerator extends BaseTest {
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

//S-small-15%
//M-medium-50%
//L-large-35%
        String[] size_array = new String[]{"S", "M", "L"};
        Map<String, Integer> size_map = new HashMap<>();
        size_map.put(size_array[0], 15);
        size_map.put(size_array[1], 50);
        size_map.put(size_array[2], 35);

//I__ : 25 %
//I_Hw1s1_ : 15%
//I_Hw0s1_ : 5%
//I_J_ : 5%
//I_JHw0s1_ : 5%

//A__ : 20%
//A_Gw0s1_ : 5%
//A_Gw1s1_ : 10%
//A_J_ : 5%
//A_JGw1s0_ : 5%
        String[] profile_array = new String[]{"I__", "I_Hw1s1_", "I_Hw0s1_", "I_J_", "I_JHw0s1_", "A__", "A_J_", "A_JGw1s0_"};

        Map<String, Integer> profile_map = new HashMap<>();
        profile_map.put(profile_array[0], 25);
        profile_map.put(profile_array[1], 15);
        profile_map.put(profile_array[2], 5);
        profile_map.put(profile_array[3], 5);
        profile_map.put(profile_array[4], 5);

        profile_map.put(profile_array[5], 25);
        profile_map.put(profile_array[6], 15);
        profile_map.put(profile_array[7], 5);

        Map<String, Integer> user_Map = new HashMap<>();
        for (Map.Entry<String, Integer> size_entry : size_map.entrySet()) {
            for (Map.Entry<String, Integer> profile_entry : profile_map.entrySet()) {
                String user_Key = size_entry.getKey() + "_" + profile_entry.getKey();
                Integer user_Number = size_entry.getValue() * profile_entry.getValue();
                user_Map.put(user_Key, user_Number);
            }
        }


        Map<String, Double> output_user_Map = new HashMap<>();
        Integer userMinNumber = 100 * 100;
        for (Map.Entry<String, Integer> user_entry : user_Map.entrySet()) {
            Integer user_count = user_entry.getValue();

            for (Integer i = 1; i <= user_count; i++) {
                String output_user_mail = user_entry.getKey() + i + "_" + new Date().getTime() + "@mailinator.com";
                Double order_value = ((double) userMinNumber * (double) i) / ((double) user_count);
                output_user_Map.put(output_user_mail, order_value);
            }
        }

        List<Map.Entry<String, Double>> output_user_list = new ArrayList<>();
        output_user_list.addAll(output_user_Map.entrySet());
        Collections.sort(output_user_list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> e1, Map.Entry<String, Double> e2) {
                return new Double(e1.getValue() + "").compareTo(new Double(e2.getValue() + ""));
            }
        });

        CompassBusinessObject compassBusinessObject = new CompassBusinessObject();

        int i = 0;
        Date startDate = new Date();
        for (Map.Entry<String, Double> user_entry : output_user_list) {
            i++;
            String res = "ERROR";
            String email = user_entry.getKey();
            while ("ERROR".equals(res)) {
                LOGGER.info("TRY create " + email);
                res = compassBusinessObject.create(CLIENT, PROTOCOL, email) ? "OK" : "ERROR";
                LOGGER.info(StringFormatter.makeProgressLogString("create user " + email + " " + res, startDate, i, output_user_list.size()));
                email = email.replaceAll("\\d{13}", new Date().getTime() + "");
            }
            FileWriter fw = new FileWriter(COMPASS_OUTPUT_DIR + FS + "out.csv", true);
            fw.write(email + "\t" + res + System.getProperty("line.separator"));
            fw.close();
        }

    }

}
