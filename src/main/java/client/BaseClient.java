package client;

import model.RequestModel;
import model.ResponseModel;
import org.apache.log4j.Logger;
import utils.StringFormatter;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseClient {

    abstract ResponseModel call(RequestModel requestModel) throws UnsupportedEncodingException;

    private Map<String, String> cookies = new HashMap<>();

    private final Logger LOGGER = Logger.getLogger(this.getClass());

    public void startLog(String method, String path) {
        LOGGER.info("START [" + method + "]\t[" + path + "]");
    }

    public void endLog(RequestModel requestModel, ResponseModel responseModel, Date start) throws IOException {
        LOGGER.info("END [" + requestModel.getMethod() + "]\t" +
                "[" + requestModel.getPath() + "]\t" +
                "[code:" + responseModel.getStatusCode() + "]\t" +
                "[responseTime : " + StringFormatter.elapsed(start) + "]");
        storeSummary(requestModel, responseModel, start);
    }

    private void storeSummary(RequestModel requestModel, ResponseModel responseModel, Date start) throws IOException {
        Date now=new Date();
        FileWriter fw = new FileWriter("summary.csv", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);

        int bodySize=responseModel.getBody().length();

        out.println(requestModel.getMethod()+"\t"
                   +(now.getTime()-start.getTime())+"\t"
                   +bodySize+"\t"
                   +now.getTime()+"\t"
                   +requestModel.getProtocol()+requestModel.getHost()+requestModel.getPath()+"\t"
                   +responseModel.getStatusCode());
        out.close();
        bw.close();
        fw.close();
    }

}
