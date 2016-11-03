package client;

import model.RequestModel;
import model.ResponseModel;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.StringFormatter;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class BaseHTTPClient {

    private Map<String, String> cookies = new HashMap<>();

    private final Logger LOGGER = Logger.getLogger(this.getClass());

    private void startLog(String method, String path) {
        LOGGER.info("START [" + method + "]\t[" + path + "]");
    }

    private void endLog(String method, String path, Object code, Date start) {
        LOGGER.info("END [" + method + "]\t" +
                "[" + path + "]\t" +
                "[code:" + code + "]\t" +
                "[responseTime : " + StringFormatter.elapsed(start) + "]");
    }

    ResponseModel call(RequestModel request) throws IOException {

        //PATH
        URL url = new URL(request.getURL());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);

        //CONTENT_TYPE
        con.setRequestProperty("Content-Type", request.getContentType());

        //HEADERS

        //AUTH
        con.setRequestProperty("Authorization", "Basic " + DatatypeConverter.printBase64Binary((request.getBaseUserName() + ":" + request.getBaseUserPassword()).getBytes()));

        //BODY
        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        wr.write(request.getBody().toString());
        wr.flush();

        //PARAMS
        setParams(request, con);

        //COOKIES

        //REQUEST_LOG

        //RESPONSE BY METHOD

        //RESPONSE

        return ResponseModel.transformHTTPResponse(con);
    }

    private void setParams(RequestModel request, HttpURLConnection con) throws IOException {

        JSONObject body = new JSONObject();
        for (Map.Entry<String, Object> entry : request.getParams().entrySet()) {
            body.put(entry.getKey(), entry.getValue());
        }
        OutputStream os = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(body.toString());
        writer.flush();
        writer.close();
        os.close();
    }
}
