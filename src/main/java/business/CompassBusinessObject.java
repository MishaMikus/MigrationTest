package business;

import client.BaseClient;
import client.CompassClient;
import utils.ResponseParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CompassBusinessObject {
    private ResponseParser responseParser = new ResponseParser();
    private final String healthkit = "healthkit";
    private final String jawbone = "jawbone";
    private final String fit = "fit";
    private final String iOSSensor = "iOSSensor";

    private final String ds_AM = "data_sources_ActiveMinutes";
    private final String ds_Workouts = "data_sources_Workouts";
    private final String ds_Steps = "data_sources_Steps";
    private final String ds_Weight = "data_sources_Weight";
    private final String ds_Sleep = "data_sources_Sleep";
    private Map<String, Map<String, String>> profile_map = new HashMap<>();

    public CompassBusinessObject() {
        String[] profile_array = new String[]{"I__", "I_Hw1s1_", "I_Hw0s1_", "I_J_", "I_JHw0s1_", "A__", "A_J_", "A_JGw1s0_"};

//I__
        profile_map.put(profile_array[0], new HashMap<String, String>() {{
            put(ds_AM, iOSSensor);
            put(ds_Workouts, iOSSensor);
            put(ds_Steps, iOSSensor);
            put(ds_Weight, iOSSensor);
            put(ds_Sleep, "");
        }});

//I_Hw1s1_
        profile_map.put(profile_array[1], new HashMap<String, String>() {{
            put(ds_AM, iOSSensor);
            put(ds_Workouts, healthkit);
            put(ds_Steps, healthkit);
            put(ds_Weight, iOSSensor);
            put(ds_Sleep, healthkit);
        }});

//I_Hw0s1_
        profile_map.put(profile_array[2], new HashMap<String, String>() {{
            put(ds_AM, iOSSensor);
            put(ds_Workouts, iOSSensor);
            put(ds_Steps, healthkit);
            put(ds_Weight, iOSSensor);
            put(ds_Sleep, healthkit);
        }});

//I_J_
        profile_map.put(profile_array[3], new HashMap<String, String>() {{
            put(ds_AM, iOSSensor);
            put(ds_Workouts, jawbone);
            put(ds_Steps, jawbone);
            put(ds_Weight, iOSSensor);
            put(ds_Sleep, jawbone);
        }});

//I_JHw0s1_
        profile_map.put(profile_array[4], new HashMap<String, String>() {{
            put(ds_AM, iOSSensor);
            put(ds_Workouts, jawbone);
            put(ds_Steps, healthkit);
            put(ds_Weight, iOSSensor);
            put(ds_Sleep, jawbone);
        }});

//A__
        profile_map.put(profile_array[5], new HashMap<String, String>() {{
            put(ds_AM, fit);
            put(ds_Workouts, fit);
            put(ds_Steps, fit);
            put(ds_Weight, fit);
            put(ds_Sleep, "");
        }});

//A_J_
        profile_map.put(profile_array[6], new HashMap<String, String>() {{
            put(ds_AM, fit);
            put(ds_Workouts, jawbone);
            put(ds_Steps, jawbone);
            put(ds_Weight, fit);
            put(ds_Sleep, jawbone);
        }});

//A_JGw1s0_
        profile_map.put(profile_array[7], new HashMap<String, String>() {{
            put(ds_AM, fit);
            put(ds_Workouts, fit);
            put(ds_Steps, jawbone);
            put(ds_Weight, fit);
            put(ds_Sleep, jawbone);
        }});
    }

    public Boolean create(BaseClient client, String protocol, String email) throws UnsupportedEncodingException {
        try {
            CompassClient compassClient = new CompassClient(client, protocol, null);
            String token = responseParser.parseAccessTokenCompass(compassClient.scvCreateAccount(email));
            compassClient.update(token);
            Integer code = compassClient.patchProfile(token).getStatusCode();
            if (code != 200) return false;
            code = compassClient.patchSettings(token, makeSourcesMap(email)).getStatusCode();
            if (code != 200) return false;
            code = compassClient.getDataSources(token).getStatusCode();
            return code == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String reLogin(CompassClient compassClient, String email) throws UnsupportedEncodingException {
        return responseParser.parseAccessTokenPF(compassClient.login(email));
    }

    private Map<String, String> makeSourcesMap(String email) {
        String current_profile = email.replaceAll("[SML]_", "").replaceAll("[\\d]+_[\\d]+@mailinator.com", "");
        return profile_map.get(current_profile);
    }

    public boolean loginPatch(BaseClient client, String protocol, String email) throws UnsupportedEncodingException {
        CompassClient compassClient = new CompassClient(client, protocol, null);
        String token = responseParser.parseAccessTokenPF(compassClient.login(email));
        Integer code = compassClient.patchProfile(token).getStatusCode();
        if (code != 200) return false;
        code = compassClient.patchSettings(token, makeSourcesMap(email)).getStatusCode();
        if (code != 200) return false;
        code = compassClient.getDataSources(token).getStatusCode();
        return code == 200;
    }
}
