package client;

import model.RequestModel;
import model.ResponseModel;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import utils.StringFormatter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class BaseHTTPClient extends BaseClient {

    private final Logger LOGGER = Logger.getLogger(this.getClass());

    ResponseModel call(RequestModel requestModel) {
        Date startDate = new Date();
        //PATH
        String fullPath = makeParamPath(requestModel);
        startLog(requestModel.getMethod(), fullPath);

        HttpRequestBase request;

        //METHOD
        if ("GET".equals(requestModel.getMethod())) {
            request = new HttpGet(fullPath);
        } else {
            HttpPost requestPost = new HttpPost(fullPath);

            //BODY
            Object body = requestModel.getBody();
            if (body instanceof Map) {
                try {
                    requestPost.setEntity(new StringEntity(new JSONObject((Map) requestModel.getBody()).toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                if (body != null)
                    try {
                        requestPost.setEntity(new StringEntity(requestModel.getBody().toString()));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
            }
            request = requestPost;
        }

        //AUTH
        if (requestModel.getBaseUserName() != null && requestModel.getBaseUserPassword() != null) {
            String basic = "Basic " + StringFormatter.credentialsToBase64(requestModel.getBaseUserName(), requestModel.getBaseUserPassword());
            request.addHeader("Authorization", basic);
        }

        //HEADERS
        for (Map.Entry<String, Object> entry : requestModel.getHeaders().entrySet()) {
            request.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
        }

        //CONTENT TYPE
        request.addHeader("Content-Type", requestModel.getContentType());
        HttpResponse response = null;
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            response = httpClient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResponseModel responseModel = new ResponseModel();
        responseModel = responseModel.transformHTTPClientResponse(response);
        try {
            endLog(requestModel,responseModel, startDate);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseModel;
    }

    private String makeParamPath(RequestModel requestModel) {
        String res = requestModel.getProtocol() + requestModel.getHost() + requestModel.getPath();

        if (requestModel.getParams() != null && requestModel.getParams().size() > 0) {
            res = res + "?";
            for (Map.Entry<String, Object> entry : requestModel.getParams().entrySet()) {
                res = res + entry.getKey() + "=" + entry.getValue() + "&";
            }
            res = res.substring(0, res.length() - 1);
        }
        return res;
    }
}

