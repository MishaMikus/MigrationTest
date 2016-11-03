package client;

import model.RequestModel;
import model.ResponseModel;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
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

        //CONTENT_TYPE
        con.setRequestProperty("Content-Type", request.getContentType());

        //HEADERS

        //BODY
        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        wr.write(request.getBody().toString());
        wr.flush();

        //PARAMS
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("firstParam", paramValue1));
        params.add(new BasicNameValuePair("secondParam", paramValue2));
        params.add(new BasicNameValuePair("thirdParam", paramValue3));

        OutputStream os = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getQuery(params));
        writer.flush();
        writer.close();
        os.close();

        //COOKIES

        //AUTH
        con.setRequestProperty("Authorization", "Basic " + DatatypeConverter.printBase64Binary((request.getBaseUserName() + ":" + request.getBaseUserPassword()).getBytes()));

        //REQUEST_LOG

        //RESPONSE BY METHOD

        //RESPONSE

        return ResponseModel.transformHTTPResponse(con);
    }
}
