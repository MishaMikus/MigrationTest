package model;

import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import sun.misc.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseModel {
    private static final Logger LOGGER = Logger.getLogger(ResponseModel.class);

    private String body;
    private Integer statusCode;
    private Map<String, String> cookiesMap;
    private Map<String, String> headerMap;

    public Map<String, String> getCookiesMap() {
        return cookiesMap;
    }

    public void setCookiesMap(Map<String, String> cookiesMap) {
        this.cookiesMap = cookiesMap;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public Integer getStatusCode() {
        return statusCode;
    }

    private void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public static ResponseModel transform(Response response) {
        ResponseModel res = new ResponseModel();
        //body
        res.setBody(response.getBody().asString());

        //header
        for (com.jayway.restassured.response.Header header : response.headers()) {
            if (res.getHeaderMap() == null) {
                res.headerMap = new HashMap<>();
            }
            res.headerMap.put(header.getName(), header.getValue());
        }

        //statusCode
        res.setStatusCode(response.statusCode());

        //cookies
        res.setCookiesMap(response.cookies());
        return res;
    }

    public static ResponseModel transformHTTPClientResponse(HttpResponse response) {
        ResponseModel res = new ResponseModel();
        //body
        try {
            res.setBody(EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        //header
        for (Header header : response.getAllHeaders()) {
            if (res.getHeaderMap() == null) {
                res.headerMap = new HashMap<>();
            }
            res.headerMap.put(header.getName(), header.getValue());
        }

        //statusCode
        res.setStatusCode(response.getStatusLine().getStatusCode());

        //cookies
       // res.setCookiesMap(response);
        return res;
    }

}
