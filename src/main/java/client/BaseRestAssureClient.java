package client;

import com.jayway.restassured.internal.http.Method;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;
import com.jayway.restassured.specification.RequestSpecification;
import model.RequestModel;
import model.ResponseModel;
import utils.StringFormatter;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;

public class BaseRestAssureClient extends BaseClient{

    private Map<String, String> cookies = new HashMap<>();

    private final Logger LOGGER = Logger.getLogger(this.getClass());


    ResponseModel call(RequestModel request) {
        startLog(request.getMethod().toString(), request.getPath());
        Date start = new Date();

        RequestSpecification requestSpecification = given();

        //PATH
        if (request.getPath() != null) {
            requestSpecification = requestSpecification.basePath(request.getPath());
        }

        //CONTENT_TYPE
        if (request.getContentType() != null) {
            requestSpecification = requestSpecification.contentType(request.getContentType());
        }

        //HEADERS
        if (request.getHeaders().values().size() > 0) {
            requestSpecification = requestSpecification.headers(request.getHeaders());
        }

        //BODY
        if (request.getBody() != null) {
            requestSpecification = requestSpecification.body(request.getBody());
        }

        //PARAMS
        if (request.getParams().values().size() > 0) {
            requestSpecification = requestSpecification.parameters(request.getParams());
        }

        //COOKIES
        if (request.getUseCookie() != null && request.getUseCookie()) {
            requestSpecification = requestSpecification.cookies(cookies);
        }

        //AUTH
        if (request.getBaseUserName() != null && request.getBaseUserPassword() != null) {
            requestSpecification = requestSpecification.auth().basic(request.getBaseUserName(), request.getBaseUserPassword());
        }

        //REQUEST_LOG
        if (request.getRequestLog() != null && request.getRequestLog()) {
            requestSpecification = requestSpecification.log().all();
        }

        //RESPONSE BY METHOD
        Response response = getResponseByMethod(requestSpecification, request.getMethod());


        //RESPONSE
        ValidatableResponse validatableResponse = null;
        if (response != null) {
            validatableResponse = response.then();
        }

        //RESPONSE_LOG
        if (validatableResponse != null && request.getResponseLog() != null && request.getResponseLog()) {
            validatableResponse = validatableResponse.log().all();
        } else {
            //RESPONSE_LOG_IF_ERROR
            if (validatableResponse != null && request.getResponseIfErrorLog() != null && request.getResponseIfErrorLog()) {
                validatableResponse = validatableResponse.log().ifError();
            }
        }

        //RETURN
        ResponseModel res = null;
        if (validatableResponse != null) {
            res = ResponseModel.transform(validatableResponse.extract().response());
            cookies = res.getCookies();
            endLog(request.getMethod().toString(), request.getPath(), res.getStatusCode(), start);
        } else {
            endLog(request.getMethod().toString(), request.getPath(), "FAIL", start);
        }
        return res;
    }

    private Response getResponseByMethod(RequestSpecification requestSpecification, Method method) {
        requestSpecification = requestSpecification.when();
        Response response = null;
        if (method == null) {
            LOGGER.warn("requestModel HTTP method cannot be null");
        } else {
            switch (method) {
                case GET: {
                    response = requestSpecification.get();
                    break;
                }
                case PUT: {
                    response = requestSpecification.put();
                    break;
                }
                case POST: {
                    response = requestSpecification.post();
                    break;
                }
                case DELETE: {
                    response = requestSpecification.delete();
                    break;
                }
                case HEAD: {
                    response = requestSpecification.head();
                    break;
                }
                case TRACE: {
                    LOGGER.warn("TRACE is not yet implemented");
                    break;
                }
                case OPTIONS: {
                    response = requestSpecification.options();
                    break;
                }
                case PATCH: {
                    response = requestSpecification.patch();
                    break;
                }
            }
        }
        return response;
    }
}
