package client;

import model.RequestModel;
import model.ResponseModel;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import utils.StringFormatter;

import javax.xml.ws.Response;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class BaseHTTPClient extends BaseClient {

    private final Logger LOGGER = Logger.getLogger(this.getClass());

    ResponseModel call(RequestModel requestModel) {
        Date startDate = new Date();
        startLog(requestModel.getMethod(), requestModel.getPath());

        ResponseModel responseModel = new ResponseModel();
        //PATH
        String fullPath = requestModel.getProtocol() + requestModel.getHost() + requestModel.getPath();

        //METHOD
        HttpPost request = new HttpPost(fullPath);

        try {
            StringEntity entity = new StringEntity(requestModel.getBody().toString());

            //CONTENT_TYPE
            entity.setContentType(requestModel.getContentType());

            //ENCODING
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, requestModel.getContentType()));

            //BODY
            request.setEntity(entity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //AUTH
        String basic = "Basic " + StringFormatter.credentialsToBase64(requestModel.getBaseUserName(), requestModel.getBaseUserPassword());
        request.addHeader("Authorization", basic);

        HttpResponse response = null;
        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            response = httpclient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //HEADERS
        //not yet implemented

        //PARAMS
        //not yet implemented

        //COOKIES
        //not yet implemented

        //REQUEST_LOG
        //not yet implemented

        //RESPONSE BY METHOD
        //not yet implemented

        //RESPONSE
        //not yet implemented

        //RESPONSE_LOG
        //not yet implemented

        //RESPONSE_LOG_IF_ERROR
        //not yet implemented

        //RETURN
        //not yet implemented
        responseModel = ResponseModel.transformHTTPClientResponse(response);
        endLog(requestModel.getMethod(), requestModel.getPath(), responseModel.getStatusCode(), startDate);
        return responseModel;
    }


    private ResponseModel getResponseByMethod(String method) {
        if (method == null) {
            LOGGER.warn("requestModel HTTP method cannot be null");
        } else {
            switch (method) {
                case "GET": {
                    break;
                }
                case "PUT": {
                    break;
                }
                case "POST": {
                    break;
                }
                case "DELETE": {
                    break;
                }
                case "HEAD": {
                    break;
                }
                case "TRACE": {
                    break;
                }
                case "OPTIONS": {
                    break;
                }
                case "PATCH": {
                    break;
                }
            }
        }
        return null;
    }
}

