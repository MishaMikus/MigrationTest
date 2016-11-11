package model;

import com.jayway.restassured.response.Response;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
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

    public ResponseModel transform(Response response) {
        //body
        body = response.getBody().asString();

        //header
        for (com.jayway.restassured.response.Header header : response.headers()) {
            if (headerMap == null) {
                headerMap = new HashMap<>();
            }
            headerMap.put(header.getName(), header.getValue());
        }

        //statusCode
        statusCode = response.statusCode();

        //cookies
        cookiesMap = response.getCookies();
        return this;
    }

    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public ResponseModel transformHTTPClientResponse(HttpResponse response) {
        //body
        try {
            body = convertStreamToString(response.getEntity().getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }


        //header
        for (Header header : response.getAllHeaders()) {
            if (headerMap == null) {
                headerMap = new HashMap<>();
            }
            headerMap.put(header.getName(), header.getValue());
        }

        //statusCode
        statusCode = response.getStatusLine().getStatusCode();

        //cookies
        cookiesMap = parseCookieMap(headerMap.get("Set-Cookie"));

        return this;
    }

    private Map<String, String> parseCookieMap(String cookies) {
        Map<String, String> res = new HashMap<>();
        if (cookies != null) {
            for (String cookie : cookies.split(";")) {
                res.put(cookie.split("=")[0].trim(), cookie.split("=")[1].trim());
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return "ResponseModel{" +
                "body='" + body + '\'' +
                ", statusCode=" + statusCode +
                ", cookiesMap=" + cookiesMap +
                ", headerMap=" + headerMap +
                '}';
    }
}
