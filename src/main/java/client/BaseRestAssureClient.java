package client;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import model.RequestModel;
import model.ResponseModel;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BaseRestAssureClient extends BaseClient {

    private Map<String, String> cookies = new HashMap<>();

    private final Logger LOGGER = Logger.getLogger(this.getClass());

    ResponseModel call(RequestModel requestModel) {
        ResponseModel responseModel = new ResponseModel();
        startLog(requestModel.getMethod(), requestModel.getPath());

        //Basic PATH
        RestAssured.baseURI = requestModel.getProtocol() + requestModel.getHost();

        RequestSpecification requestSpecification = given();

        //PATH
        if (requestModel.getPath() != null) {
            requestSpecification = requestSpecification.basePath(requestModel.getPath());
        }

        //CONTENT_TYPE
        if (requestModel.getContentType() != null) {
            requestSpecification = requestSpecification.contentType(requestModel.getContentType());
        }

        //HEADERS
        if (requestModel.getHeaders().values().size() > 0) {
            requestSpecification = requestSpecification.headers(requestModel.getHeaders());
        }

        //BODY
        if (requestModel.getBody() != null) {
            requestSpecification = requestSpecification.body(requestModel.getBody());
        }

        //PARAMS
        if (requestModel.getParams().values().size() > 0) {
            requestSpecification = requestSpecification.parameters(requestModel.getParams());
        }

        //COOKIES
        if (requestModel.getUseCookie() != null && requestModel.getUseCookie()) {
            requestSpecification = requestSpecification.cookies(cookies);
        }

        //AUTH
        if (requestModel.getBaseUserName() != null && requestModel.getBaseUserPassword() != null) {
            requestSpecification = requestSpecification.auth().basic(requestModel.getBaseUserName(), requestModel.getBaseUserPassword());
        }

        //REQUEST_LOG
        if (requestModel.getRequestLog() != null && requestModel.getRequestLog()) {
            requestSpecification = requestSpecification.log().all();
        }

        //===========SEND============
        Date start=new Date();

        //RESPONSE BY METHOD
        Response response = getResponseByMethod(requestSpecification, requestModel.getMethod());

        //TIME
        responseModel.setResponseTime(response.time());

        //RESPONSE
        ValidatableResponse validatableResponse = null;
        if (response != null) {
            validatableResponse = response.then();
        }

        //RESPONSE_LOG
        validatableResponse.log();
        if (validatableResponse != null && requestModel.getResponseLog() != null && requestModel.getResponseLog()) {
            validatableResponse = validatableResponse.log().all();
        } else {
            //RESPONSE_LOG_IF_ERROR
            if (validatableResponse != null && requestModel.getResponseIfErrorLog() != null && requestModel.getResponseIfErrorLog()) {
                validatableResponse = validatableResponse.log().ifError();
            }
        }

        //RETURN
        if (validatableResponse != null) {
            responseModel.transform(validatableResponse.extract().response());
            cookies.putAll(responseModel.getCookiesMap());
            try {
                endLog(requestModel, responseModel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseModel;
    }

    private Response getResponseByMethod(RequestSpecification requestSpecification, String method) {
        requestSpecification = requestSpecification.when();
        Response response = null;
        if (method == null) {
            LOGGER.warn("requestModel HTTP method cannot be null");
        } else {
            switch (method) {
                case "GET": {
                    response = requestSpecification.get();
                    break;
                }
                case "PUT": {
                    response = requestSpecification.put();
                    break;
                }
                case "POST": {
                    response = requestSpecification.post();
                    break;
                }
                case "DELETE": {
                    response = requestSpecification.delete();
                    break;
                }
                case "HEAD": {
                    response = requestSpecification.head();
                    break;
                }
                case "TRACE": {
                    LOGGER.warn("TRACE is not yet implemented");
                    break;
                }
                case "OPTIONS": {
                    response = requestSpecification.options();
                    break;
                }
                case "PATCH": {
                    response = requestSpecification.patch();
                    break;
                }
            }
        }
        return response;
    }
}
