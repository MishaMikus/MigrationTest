package client;

import model.RequestModel;
import model.ResponseModel;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class BaseHTTPClient extends BaseClient {

    private final String USER_AGENT = "Mozilla/5.0";

    private Map<String, String> cookies = new HashMap<>();

    private final Logger LOGGER = Logger.getLogger(this.getClass());
    Date startDate = new Date();

    ResponseModel call(RequestModel requestModel) throws IOException {
        startLog(requestModel.getMethod().toString(), requestModel.getPath());


        String url = "http://www.google.com/search?q=httpClient";

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

// add request header
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response = client.execute(request);

        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }


        //CONTENT_TYPE
       // con.setRequestProperty("Content-Type", request.getContentType());

        //HEADERS
       // con.setRequestProperty("User-Agent", USER_AGENT);

        //AUTH
      //  con.setRequestProperty("Authorization", "Basic " + DatatypeConverter.printBase64Binary((request.getBaseUserName() + ":" + request.getBaseUserPassword()).getBytes()));

        //PATH
       // URL url = new URL(request.getURL());
       // HttpURLConnection con = (HttpURLConnection) url.getContent();
      //  con.setDoOutput(true);

        //BODY
      //  OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
      //  wr.write(request.getBody().toString());
      //  wr.flush();

        //PARAMS
      //  setParams(request, con);

        //COOKIES

        //REQUEST_LOG

        //RESPONSE BY METHOD

        //RESPONSE
      //  ResponseModel response = ResponseModel.transformHTTPResponse(con);

       // endLog(request.getMethod().toString(), request.getPath(), response.getStatusCode(), startDate);

      //  return response;
        return null;
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

