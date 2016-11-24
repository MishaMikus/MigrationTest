package utils;

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
            //res = new JsonPath(response.getBody()).getString("accessToken");
             res = stringBetween(response.getBody(), "\"accessToken\":\"", "\",\"");
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

    public Long parseWorkoutIdFromLocation(ResponseModel response) {
        String location = response.getHeaderMap().get("Location");
        if (location == null) {
            return null;
        }
        if (location.split("/").length >= 8) {
            return new Long(location.split("/")[8] + "");
        } else {
            LOGGER.warn("CANNOT PARSE workout_id FROM : " + location);
            return null;
        }
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
            String a = "\"nextPage\":\"";
            int res= response.getBody().indexOf(a);
            return res> 0;
        } else {
            LOGGER.warn("JSON PARSING ERROR : " + response.getBody());
            return null;
        }
    }

    public Integer parseTotalResults(ResponseModel response) {
        if (response.getStatusCode() == 200) {
            String a = "\"totalResults\":";
            String b = ",\"results\":[";
            return Integer.parseInt(response.getBody().substring(response.getBody().indexOf(a) + a.length(), response.getBody().indexOf(b)));
        } else {
            LOGGER.warn("JSON PARSING ERROR : " + response.getBody());
            return null;
        }
    }
}
