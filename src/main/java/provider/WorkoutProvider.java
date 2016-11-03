package provider;

import utils.StringFormatter;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class WorkoutProvider {
    private JSONObject gameJson;
    private JSONObject scJson;

    private JSONObject scShedulJson;
    private JSONObject gameShedulJson;
    private List<JSONObject> freeRunWorkoutJsonList;
    private static final Long HOUR_MS = 1000 * 60 * 60L;
    public static final Long DAY_MS = 24 * 1000 * 60 * 60L;
    private final Logger LOGGER = Logger.getLogger(this.getClass());

    Random random = new Random();

    public String makeGameWorkout(Date date, JSONObject workoutFixture, Long workoutId) throws IOException, ParseException {
        JSONObject res = (JSONObject) getTemplateGameJson().clone();
        Date now = new Date(date.getTime() - HOUR_MS * 5);
        Date startDateTime = new Date(now.getTime() - HOUR_MS * 2);
        res.put("startDateTime", StringFormatter.makeJSONDateString(startDateTime));
        res.put("startDateTimeUTC", StringFormatter.makeJSONDateString(startDateTime));
        res.put("stopDateTime", StringFormatter.makeJSONDateString(now));
        res.put("workoutId", workoutId);
        res.put("zoneType", "hrZone");
        res.put("deviceType", "mobile");
        res.put("activityType", "americanFootball");
        res.put("fixture", workoutFixture);
        return res.toString();
    }

    public String makeScWorkout(Date date, JSONArray workoutComponent, Long workoutId) throws IOException, ParseException {
        JSONObject res = (JSONObject) getTemplateScJson().clone();
        Date now = new Date(date.getTime() - HOUR_MS * 8);
        Date startDateTime = new Date(now.getTime() - HOUR_MS * 2);
        res.put("startDateTime", StringFormatter.makeJSONDateString(startDateTime));
        res.put("startDateTimeUTC", StringFormatter.makeJSONDateString(startDateTime));
        res.put("stopDateTime", StringFormatter.makeJSONDateString(now));
        res.put("workoutId", workoutId);
        res.put("workoutType", "custom");
        res.put("deviceType", "mobile");
        res.put("zoneType", "hrZone");
        res.put("activityType", "strengthAndFlexibility");
        res.put("components", workoutComponent);
        return res.toString();
    }

    private JSONObject getTemplateGameJson() throws IOException, ParseException {
        if (gameJson == null) {
            gameJson = loadJsonFromFile("post_workout/game/Game.json");
        }
        return gameJson;
    }

    private JSONObject loadJsonFromFile(String filePath) throws IOException, ParseException {
        File file = new File(filePath);
        if (file.exists()) {
            return ((JSONObject) new JSONParser().parse(new FileReader(file)));
        } else {
            LOGGER.warn("File '" + filePath + "' does not exists");
            return null;
        }
    }

    private JSONObject getTemplateScJson() throws IOException, ParseException {
        if (scJson == null) {
            scJson = loadJsonFromFile("post_workout/sc/S&C.json");
        }
        return scJson;
    }

    private JSONObject getTemplateScShedulJson() throws IOException, ParseException {
        if (scShedulJson == null) {
            scShedulJson = loadJsonFromFile("post_workout/sc/sc_make_shedul.json");
        }
        return scShedulJson;
    }

    public String makeScShedulWorkout(Date date, Long traningId) throws IOException, ParseException {
        JSONObject res = (JSONObject) getTemplateScShedulJson().clone();
        JSONObject training = (JSONObject) res.get("training");
        training.put("id", traningId);
        res.put("training", training);
        res.put("scheduledDate", StringFormatter.makeJSONDateString(date));
        return res.toString();
    }

    public String makeGameShedulWorkout(Date date) throws IOException, ParseException {
        JSONObject res = (JSONObject) getTemplateGameShedulJson().clone();
        res.put("scheduledDate", StringFormatter.makeJSONDateString(date));
        return res.toString();
    }

    private JSONObject getTemplateGameShedulJson() throws IOException, ParseException {
        if (gameShedulJson == null) {
            gameShedulJson = loadJsonFromFile("post_workout/game/game_make_shedul.json");
        }
        return gameShedulJson;
    }

    public String makeFreeRunWorkout(Date date) throws IOException, ParseException {
        JSONObject res = (JSONObject) getTemplateFreeRunJson().clone();
        Date now = new Date(date.getTime() - HOUR_MS * 2);
        Date startDateTime = new Date(now.getTime() - HOUR_MS * 2);
        res.put("startDateTime", StringFormatter.makeJSONDateString(startDateTime));
        res.put("startDateTimeUTC", StringFormatter.makeJSONDateString(startDateTime));
        res.put("stopDateTime", StringFormatter.makeJSONDateString(now));
        return res.toString();
    }

    private JSONObject getTemplateFreeRunJson() throws IOException, ParseException {
        File[] fileList = new File("post_workout/run").listFiles();
        if (fileList != null && fileList.length > 0) {
            LOGGER.info("START READ post_workout/run dir");
            if (freeRunWorkoutJsonList == null || fileList.length != freeRunWorkoutJsonList.size()) {
                freeRunWorkoutJsonList = new ArrayList<>();
                for (File file : fileList) {
                    LOGGER.info("READ " + file.getAbsolutePath());
                    freeRunWorkoutJsonList.add(loadJsonFromFile(file.getAbsolutePath()));
                }
            }
            LOGGER.info("END READ post_workout/run dir");
        }
        int index = random.nextInt(freeRunWorkoutJsonList.size());
        LOGGER.info("POST post_workout/run " + index);
        return freeRunWorkoutJsonList.get(index);
    }
}
