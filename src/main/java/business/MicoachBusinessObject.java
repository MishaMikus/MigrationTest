package business;

import client.MicoachClient;
import model.ResponseModel;
import utils.StringFormatter;
import model.UserModel;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import provider.WorkoutProvider;
import utils.ResponseParser;

import java.io.*;
import java.util.Date;

import java.util.Random;

import static org.testng.Assert.assertEquals;

public class MicoachBusinessObject {

    private ResponseParser responseParser = new ResponseParser();
    private WorkoutProvider workoutProvider = new WorkoutProvider();
    private MicoachClient micoachClient = new MicoachClient();
    private UserModel currentUser;
    private ResponseModel response;

    private static final long ONE_DAY_MS = 24L * 60L * 60L * 1000L;
    private static final int HISTORY_DEEP_DAY = 700;
    private static final int WORKOUT_COUNT = 1;
    private Random random = new Random();
    private final Logger LOGGER = Logger.getLogger(this.getClass());

    public ResponseModel signUp() throws IOException {
        response = micoachClient.postUser(currentUser.getSignUpMap(), currentUser.getBaseUserName(), currentUser.getBaseUserPassword());
        currentUser.setAccessToken(responseParser.parseAccessToken(response));
        return response;
    }

    public ResponseModel login() throws IOException {
        response = micoachClient.postAuthorize(currentUser.getLoginMapBody(), currentUser.getBaseUserName(), currentUser.getBaseUserPassword());
        currentUser.setAccessToken(responseParser.parseAccessToken(response));
        LOGGER.info("[" + currentUser.getEmail() + "] [LOGIN] " + (currentUser.getAccessToken() == null ? "[FAIL]" : "[SUCCESS]"));
        return response;
    }

    public void makeAndPostSCWorkouts() throws IOException, ParseException {
        LOGGER.info("START makeAndPostSCWorkouts : " + currentUser.getEmail());
        Date startDate = new Date();
        for (int i = 0; i < WORKOUT_COUNT; i++) {
            Date date = makeRandDate();
            postSCWorkouts(date);
            if (currentUser.getWorkoutId() == null) {
                login();
                postSCWorkouts(date);
            }
            LOGGER.info(StringFormatter.makeProgressLogString("makeAndPostSCWorkouts : " + currentUser.getEmail(), startDate, i, WORKOUT_COUNT));
        }
        LOGGER.info("END makeAndPostSCWorkouts : " + currentUser.getEmail());
    }

    public void makeAndPostGameWorkouts() throws IOException, ParseException {
        LOGGER.info("START makeAndPostGameWorkouts : " + currentUser.getEmail());
        Date startDate = new Date();
        for (int i = 0; i < WORKOUT_COUNT; i++) {
            Date date = makeRandDate();
            postGameWorkouts(date);
            if (currentUser.getWorkoutId() == null) {
                login();
                postGameWorkouts(date);
            }
            LOGGER.info(StringFormatter.makeProgressLogString("makeAndPostGameWorkouts : " + currentUser.getEmail(), startDate, i, WORKOUT_COUNT));
        }
        LOGGER.info("END makeAndPostGameWorkouts : " + currentUser.getEmail());
    }

    private void postGameWorkouts(Date date) throws IOException, ParseException {
        response = micoachClient.postWorkouts(workoutProvider.makeGameShedulWorkout(date), currentUser.getAccessToken());
        Long workoutId = responseParser.parseWorkoutlIdFromLocation(response);
        currentUser.setWorkoutId(workoutId);
        response = micoachClient.getWorkoutsById(workoutId, currentUser.getAccessToken());
        currentUser.setWorkoutFixtureGame(responseParser.parseWorkoutFixtureGame(response));
        micoachClient.postWorkouts(workoutProvider.makeGameWorkout(date, currentUser.getWorkoutFixtureGame(), workoutId), currentUser.getAccessToken());
    }

    public void makeAndPostFreeRunWorkouts() throws IOException, ParseException {
        LOGGER.info("START makeAndPostFreeRunWorkouts : " + currentUser.getEmail());
        Date startDate = new Date();
        for (int i = 0; i < WORKOUT_COUNT; i++) {
            Date date = makeRandDate();
            postFreeRunWorkouts(date);
            if ("201".equals(currentUser.getFreeRunStatus())) {
                login();
                postFreeRunWorkouts(date);
            }
            LOGGER.info(StringFormatter.makeProgressLogString("makeAndPostFreeRunWorkouts : " + currentUser.getEmail(), startDate, i, WORKOUT_COUNT));
        }
        LOGGER.info("END makeAndPostFreeRunWorkouts : " + currentUser.getEmail());
    }

    private void postFreeRunWorkouts(Date date) throws IOException, ParseException {
        response = micoachClient.postWorkouts(workoutProvider.makeFreeRunWorkout(date), currentUser.getAccessToken());
        currentUser.setFreeRunStatus(response.getStatusCode() + "");
    }

    public void checkNewUser() throws IOException {
        micoachClient.postApiV3UsersCheck(currentUser.getCheckEmailBody(), currentUser.getBaseUserName(), currentUser.getBaseUserPassword());
        micoachClient.postApiV3UsersCheck(currentUser.getUsersCheckScreenNameBody(), currentUser.getBaseUserName(), currentUser.getBaseUserPassword());
    }

    public void loginWeb() throws IOException {
        response = micoachClient.getAuthorize(currentUser.getAccessToken(), currentUser.getBaseUserName(), currentUser.getBaseUserPassword());
        currentUser.setXCsrfToken(responseParser.parseXCsrfToken(response));
    }

    public void delete() throws IOException {
        micoachClient.delete(currentUser.getXCsrfToken(), currentUser.getBaseUserName(), currentUser.getBaseUserPassword());
    }

    private void postSCWorkouts(Date date) throws ParseException, IOException {
        if (currentUser.getCustomTrainingIdSC() == null) {
            response = micoachClient.getCustomtrainings(currentUser.getAccessToken());
            currentUser.setCustomTrainingIdSC(responseParser.parseCustomTrainingId(response));
        }
        response = micoachClient.postWorkouts(workoutProvider.makeScShedulWorkout(date, currentUser.getCustomTrainingIdSC()), currentUser.getAccessToken());
        Long workoutId = responseParser.parseWorkoutlIdFromLocation(response);
        currentUser.setWorkoutId(workoutId);
        response = micoachClient.getWorkoutsById(workoutId, currentUser.getAccessToken());
        currentUser.setWorkoutComponents(responseParser.parseWorkoutComponents(response));
        micoachClient.postWorkouts(workoutProvider.makeScWorkout(date, currentUser.getWorkoutComponents(), workoutId), currentUser.getAccessToken());
    }

    private Date makeRandDate() {
        return new Date(new Date().getTime() - ONE_DAY_MS - ONE_DAY_MS * random.nextInt(HISTORY_DEEP_DAY));
    }

    public void setCurrentUser(UserModel currentUser) {
        this.currentUser = currentUser;
    }

    public UserModel getCurrentUser() {
        return currentUser;
    }

    public void storeUser(String message) {
        try (FileWriter fw = new FileWriter("users/userList_devdesign.txt", true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readWorkouts(Date start, Integer page, Integer itemsPerPage) throws IOException {
        String fields = "general,details,stats,events,dataPoints,trainingComponents,publicPageUrl," +
                "fitnessZoneStats,intervals,achievements,plan,movements,trainingNotes,modifiedMetadata," +
                "images,feedback,modifications";
        String type = "free,custom,plan,orphan,v1Assessment,manual,v3Assessment,paceAssessment,pack,fixture,type2SportsChallenge,progression,raw";
        response = micoachClient.getWorkouts(fields, type, currentUser.getAccessToken(), itemsPerPage, page);
        Integer totalResults = responseParser.parseTotalResults(response);
        LOGGER.info("[user : " + currentUser.getEmail() + "]\t" +
                "[total workout count : " + totalResults + "]\t" +
                "[total size  : " + StringFormatter.toGbMbKbString(response.getBody().getBytes().length) + "]\t" +
                "[page : " + page + "]\t"
        );
        storeWorkouts(response.getBody(), page);
        if (responseParser.isNextPagePresent(response) != null && responseParser.isNextPagePresent(response)) {
            LOGGER.info(StringFormatter.makeProgressLogString("getWorkouts", start, page * itemsPerPage, totalResults));
            readWorkouts(start, page + 1, itemsPerPage);
        }
        assertEquals(response.getStatusCode(), new Integer(200), "GET WORKOUTS ERROR : " + response.getBody());
    }

    public boolean readWorkouts(Integer itemsPerPage) throws IOException {
        Date start = new Date();
        readWorkouts(start, 1, itemsPerPage);
        FileWriter fw = new FileWriter("users/migrateUserList.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        out.println(currentUser.getEmail() + "\tmigrate\t[elapsed : " + StringFormatter.elapsed(start) + "]\t" + new Date());
        out.close();
        bw.close();
        fw.close();
        return true;
    }

    public void storeWorkouts(String workouts, Integer page) throws IOException {
        FileOutputStream fos = new FileOutputStream("users/workout/" + currentUser.getEmail() + "_p" + page + ".json");
        OutputStreamWriter w = new OutputStreamWriter(fos, "UTF-8");
        BufferedWriter bw = new BufferedWriter(w);
        bw.write(workouts);
        bw.flush();
        bw.close();
        w.close();
        fos.close();
    }
}
