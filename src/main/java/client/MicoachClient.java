package client;

import model.RequestModel;
import model.ResponseModel;

import java.io.IOException;
import java.util.Map;

import static com.jayway.restassured.internal.http.Method.DELETE;
import static com.jayway.restassured.internal.http.Method.GET;
import static com.jayway.restassured.internal.http.Method.POST;

//public class MicoachClient extends BaseHTTPClient {
public class MicoachClient extends BaseRestAssureClient {
    private static final String PROTOCOL = "https://";
    private static final String HOST = "staging.micoach.adidas.com";

    public ResponseModel postUser(Map<String, Object> signUpMap, String base_user_name, String base_user_password) throws IOException {
        RequestModel requestModel = new RequestModel();
        requestModel.setProtocol(PROTOCOL);
        requestModel.setProtocol(HOST);
        requestModel.setPath("/api/v3/users");
        requestModel.setContentType("application/json");
        requestModel.setBody(signUpMap);
        requestModel.setBaseUserName(base_user_name);
        requestModel.setBaseUserPassword(base_user_password);
        requestModel.setHTTPMethod(POST);
        requestModel.setUseCookie(true);
        return call(requestModel);

    }

    public ResponseModel postAuthorize(Map<String, Object> loginMapBody, String base_user_name, String base_user_password) throws IOException {
        RequestModel requestModel = new RequestModel();
        requestModel.setProtocol(PROTOCOL);
        requestModel.setHost(HOST);
        requestModel.setPath("/api/v3/OAuth2/Authorize");
        requestModel.setContentType("application/json");
        requestModel.setBody(loginMapBody);
        requestModel.setBaseUserName(base_user_name);
        requestModel.setBaseUserPassword(base_user_password);
        requestModel.setHTTPMethod(POST);
        requestModel.setUseCookie(true);
        return call(requestModel);
    }

    public ResponseModel getCustomtrainings(String accessToken) throws IOException {
        RequestModel requestModel = new RequestModel();
        requestModel.setProtocol(PROTOCOL);
        requestModel.setHost(HOST);
        requestModel.setPath("/api/v3/users/me/customtrainings");
        requestModel.setContentType("application/json");
        requestModel.putHeader("Authorization", "Bearer " + accessToken);
        requestModel.setHTTPMethod(GET);
        requestModel.setUseCookie(true);
        return call(requestModel);
    }

    public ResponseModel postWorkouts(String workout, String accessToken) throws IOException {
        RequestModel requestModel = new RequestModel();
        requestModel.setProtocol(PROTOCOL);
        requestModel.setHost(HOST);
        requestModel.setPath("/api/v3/users/me/workouts");
        requestModel.setContentType("application/json");
        requestModel.putHeader("Authorization", "Bearer " + accessToken);
        requestModel.setBody(workout);
        requestModel.setHTTPMethod(POST);
        requestModel.setUseCookie(true);
        return call(requestModel);
    }

    public ResponseModel getWorkouts(String fields, String type, String accessToken, Integer itemsPerPage, Integer page) throws IOException {
        RequestModel requestModel = new RequestModel();
        requestModel.setProtocol(PROTOCOL);
        requestModel.setHost(HOST);
        requestModel.setPath("/api/v3/users/me/workouts");
        requestModel.setContentType("application/json");
        requestModel.putHeader("Authorization", "Bearer " + accessToken);
        requestModel.putParam("fields", fields);
        requestModel.putParam("type", type);
        requestModel.putParam("itemsPerPage", itemsPerPage);
        requestModel.putParam("page", page);
        requestModel.setHTTPMethod(GET);
        requestModel.setUseCookie(true);
        return call(requestModel);
    }

    public ResponseModel getWorkoutsById(Long id, String accessToken) throws IOException {
        RequestModel requestModel = new RequestModel();
        requestModel.setProtocol(PROTOCOL);
        requestModel.setHost(HOST);
        requestModel.setPath("/api/v3/users/me/workouts/" + id);
        requestModel.setContentType("application/json");
        requestModel.putHeader("Authorization", "Bearer " + accessToken);
        requestModel.setHTTPMethod(GET);
        requestModel.setUseCookie(true);
        return call(requestModel);
    }

    public ResponseModel postApiV3UsersCheck(Map<String, Object> body, String base_user_name, String base_user_password) throws IOException {
        RequestModel requestModel = new RequestModel();
        requestModel.setProtocol(PROTOCOL);
        requestModel.setHost(HOST);
        requestModel.setPath("/api/v3/users/check");
        requestModel.setContentType("application/json");
        requestModel.setBaseUserName(base_user_name);
        requestModel.setBaseUserPassword(base_user_password);
        requestModel.setBody(body);
        requestModel.setHTTPMethod(POST);
        requestModel.setUseCookie(true);
        return call(requestModel);
    }

    public ResponseModel getAuthorize(String accessToken, String base_user_name, String base_user_password) throws IOException {
        RequestModel requestModel = new RequestModel();
        requestModel.setProtocol(PROTOCOL);
        requestModel.setHost(HOST);
        requestModel.setPath("/Account/Authorize");
        requestModel.setContentType("application/json");
        requestModel.setBaseUserName(base_user_name);
        requestModel.setBaseUserPassword(base_user_password);
        requestModel.putParam("ot", accessToken);
        requestModel.setHTTPMethod(GET);
        requestModel.setUseCookie(true);
        return call(requestModel);
    }

    public ResponseModel delete(String xCsrfToken, String base_user_name, String base_user_password) throws IOException {
        RequestModel requestModel = new RequestModel();
        requestModel.setProtocol(PROTOCOL);
        requestModel.setHost(HOST);
        requestModel.setPath("/api/v3/users/me");
        requestModel.setContentType("application/json");
        requestModel.setBaseUserName(base_user_name);
        requestModel.setBaseUserPassword(base_user_password);
        requestModel.putParam("status", "pending");
        requestModel.putHeader("X-Csrf-Token", xCsrfToken);
        requestModel.setHTTPMethod(DELETE);
        requestModel.setUseCookie(true);
        return call(requestModel);
    }

    public ResponseModel getCustomTrainings(String accessToken, Integer itemsPerPage, Integer page) {
        RequestModel requestModel = new RequestModel();
        requestModel.setProtocol(PROTOCOL);
        requestModel.setHost(HOST);
        requestModel.setPath("/api/v3/users/me/CustomTrainings");
        requestModel.setContentType("application/json");
        requestModel.putHeader("Authorization", "Bearer " + accessToken);
        requestModel.putParam("itemsPerPage", itemsPerPage);
        requestModel.putParam("page", page);
        requestModel.setHTTPMethod(GET);
        requestModel.setUseCookie(true);
        return call(requestModel);
    }
}
