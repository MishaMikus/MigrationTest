package utils;

import com.jayway.restassured.path.json.JsonPath;
import model.ResponseModel;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ResponseParser {
    private final Logger LOGGER = Logger.getLogger(this.getClass());

    public String parseAccessToken(ResponseModel response) {
        String res;
        if (new Integer(200).equals(response.getStatusCode())) {
            res = new JsonPath(response.getBody()).getString("accessToken");
            // res = stringBetween(new String(response.getBody().asByteArray()), "\"accessToken\":\"", "\",\"");
            if (res.isEmpty()) {
                LOGGER.warn("Cannot Find accessToken : " + response.getBody());
            }
            return res;
        } else {
            LOGGER.warn("JSON PARSING ERROR : " + response.getBody());
            return null;
        }
    }

    private String stringBetween(String input, String a, String b) {
        return input.substring(input.indexOf(a) + a.length(), input.indexOf(b));
    }

    public Long parseCustomTrainingId(ResponseModel response) throws ParseException {
        Object body = new JSONParser().parse(response.getBody());
        Object results = ((JSONObject) body).get("results");
        if (results != null) {
            for (Object jsonObject : (JSONArray) results) {
                if ("Strength & Conditioning (Sample)".equals(((JSONObject) jsonObject).get("name"))) {
                    return new Long(((JSONObject) jsonObject).get("id").toString());
                }
            }
        }
        return null;
    }

    public Long parseWorkoutlIdFromLocation(ResponseModel response) {
        if (response.getHeaderMap().get("Location") == null) {
            return null;
        }
        return new Long(response.getHeaderMap().get("Location").split("/")[8] + "");
    }

    public JSONArray parseWorkoutComponents(ResponseModel response) throws ParseException {
        try {
            return (JSONArray) ((JSONObject) new JSONParser().parse(response.getBody())).get("components");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject parseWorkoutFixtureGame(ResponseModel response) throws ParseException {
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.getBody());
        return (JSONObject) jsonObject.get("fixture");
    }

    public String parseXCsrfToken(ResponseModel response) {
        return response.getCookiesMap().get("X-Csrf-Token");
    }

    public Boolean isNextPagePresent(ResponseModel response) {
        if (response.getStatusCode() == 200) {
            //return new JsonPath(response.body().asInputStream()).get("totalResults") != null;
            return response.getBody().indexOf("\"nextPage\":\"") > 0;
        } else {
            LOGGER.warn("JSON PARSING ERROR : " + response.getBody());
            return null;
        }
    }

    public Integer parseTotalResults(ResponseModel response) {
        if (response.getStatusCode() == 200) {
            //"totalResults":260,"results":[{"workoutId"
            String input = new String(response.getBody());
            String a = "\"totalResults\":";
            String b = ",\"results\":[";
            return Integer.parseInt(input.substring(input.indexOf(a) + a.length(), input.indexOf(b)));
            // return new JsonPath(response.body().asInputStream()).get("totalResults");
        } else {
            LOGGER.warn("JSON PARSING ERROR : " + response.getBody());
            return null;
        }
    }
}
