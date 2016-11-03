package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserModel {
    private String email;
    private String webScreenName;
    private String password;
    private String xCsrfToken;
    private Long customTrainingIdSC;
    private String baseUserName;
    private String baseUserPassword;
    private String accessToken;
    private Map<String, Object> signUpMap = new HashMap<>();
    private Long workoutId;
    private JSONArray workoutComponents;
    private JSONObject workoutFixtureGame;
    private String freeRunStatus;

    public UserModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebScreenName() {
        return webScreenName;
    }

    public void setWebScreenName(String webScreenName) {
        this.webScreenName = webScreenName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setXCsrfToken(String xCsrfToken) {
        this.xCsrfToken = xCsrfToken;
    }

    public String getXCsrfToken() {
        return xCsrfToken;
    }

    public Map<String, Object> getSignUpMap() {
        signUpMap.put("flow", "simple");
        signUpMap.put("email", this.getEmail());
        signUpMap.put("password", this.getPassword());
        signUpMap.put("screenname", this.getWebScreenName());
        signUpMap.put("weightInGrammes", 80000);
        signUpMap.put("heightInCentimeters", "177");
        signUpMap.put("dateOfBirth", "1973-09-05T00:00:00.000Z");
        signUpMap.put("age", 43);
        signUpMap.put("gender", "male");
        signUpMap.put("country", "GM");
        signUpMap.put("minAge", 13);
        signUpMap.put("zipCode", "43344");
        signUpMap.put("acceptNewletterSubscribtion", false);
        signUpMap.put("locale", "au");
        return signUpMap;
    }

    public JSONArray getWorkoutComponents() {
        return workoutComponents;
    }

    public void setWorkoutComponents(JSONArray workoutComponents) {
        this.workoutComponents = workoutComponents;
    }

    public void setCustomTrainingIdSC(Long customTrainingIdSC) {
        this.customTrainingIdSC = customTrainingIdSC;
    }

    public Long getCustomTrainingIdSC() {
        return customTrainingIdSC;
    }

    public JSONObject getWorkoutFixtureGame() {
        return workoutFixtureGame;
    }

    public void setWorkoutFixtureGame(JSONObject workoutFixtureGame) {
        this.workoutFixtureGame = workoutFixtureGame;
    }

    public Map<String, Object> getLoginMapBody() {
        Map<String, Object> res = new HashMap<>();
        res.put("email", email);
        res.put("password", password);
        return res;
    }

    public void setBaseUserName(String baseUserName) {
        this.baseUserName = baseUserName;
    }

    public void setBaseUserPassword(String baseUserPassword) {
        this.baseUserPassword = baseUserPassword;
    }

    public String getBaseUserName() {
        return baseUserName;
    }

    public String getBaseUserPassword() {
        return baseUserPassword;
    }

    public Object getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
    }

    public void setFreeRunStatus(String freeRunStatus) {
        this.freeRunStatus = freeRunStatus;
    }

    public String getFreeRunStatus() {
        return freeRunStatus;
    }

    public Map<String, Object> getCheckEmailBody() {
        Map<String, Object> body = new HashMap<>();
        body.put("email", getEmail());
        return body;
    }

    public Map<String, Object> getUsersCheckScreenNameBody() {
        Map<String, Object> body = new HashMap<>();
        body.put("screenname", getWebScreenName());
        return body;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
