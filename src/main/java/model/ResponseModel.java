package model;

import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseModel {
    private static final Logger LOGGER = Logger.getLogger(ResponseModel.class);

    private String body;
    private Headers header;
    private Integer statusCode;
    private Map<String, String> cookies;

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Headers getHeader() {
        return header;
    }

    public void setHeader(Headers header) {
        this.header = header;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public static ResponseModel transform(Response response) {
        ResponseModel res = new ResponseModel();
        //body
        java.util.Scanner s = new java.util.Scanner(response.body().asInputStream()).useDelimiter("\\A");
        try {
            res.setBody(s.hasNext() ? s.next() : "");
            s.close();
        } catch (Exception e) {
            LOGGER.warn("response.body().asInputStream() parse ERROR");
            e.printStackTrace();
        }

        //header
        res.setHeader(response.headers());

        //statusCode
        res.setStatusCode(response.statusCode());

        //cookies
        res.setCookies(response.cookies());
        return res;
    }

    public static ResponseModel transformHTTPResponse(HttpURLConnection con) throws IOException {
        System.setProperty("javax.net.debug", "all");
        ResponseModel res = new ResponseModel();
        //statusCode
        res.setStatusCode(con.getResponseCode());

        if (res.getStatusCode() != 200) {
            java.util.Scanner s = new java.util.Scanner(con.getErrorStream()).useDelimiter("\\A");
            res.setBody(s.hasNext() ? s.next() : "");
            s.close();
        } else {
            //body
            java.util.Scanner s = new java.util.Scanner(con.getInputStream()).useDelimiter("\\A");
            res.setBody(s.hasNext() ? s.next() : "");
            s.close();
        }
        //header
        res.setHeader(transformHeader(con.getHeaderFields()));
        //cookies
        res.setCookies(transformCookie(con));
        return res;
    }

    private static Map<String, String> transformCookie(HttpURLConnection con) {
        Map<String, List<String>> headerFields = con.getHeaderFields();
        List<String> cookiesHeader = headerFields.get("Set-Cookie");
        Map<String, String> res = new HashMap<>();
        if (cookiesHeader != null) {
            for (String cookie : cookiesHeader) {
                HttpCookie.parse(cookie);
                res.put(cookie.split("=")[0], cookie.split("=")[1]);
            }
        }
        return res;
    }

    private static Headers transformHeader(Map<String, List<String>> headerFields) {
        System.out.println(headerFields);
        Headers res = new Headers();
        return res;
    }
}
