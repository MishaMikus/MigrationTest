package client;

import model.RequestModel;
import model.ResponseModel;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public abstract class BaseClient {
    abstract ResponseModel call(RequestModel requestModel) throws UnsupportedEncodingException;

    private final Logger LOGGER = Logger.getLogger(this.getClass());

    public void startLog(String method, String path) {
        LOGGER.info("START [" + method + "]\t[" + path + "]");
    }

    public void endLog(RequestModel requestModel, ResponseModel responseModel) throws IOException {
        LOGGER.info("END [" + requestModel.getMethod() + "]\t" +
                "[" + requestModel.getPath() + "]\t" +
                "[code : " + responseModel.getStatusCode() + "]\t" +
                "[responseTime : " + responseModel.getResponseTime() + "]");
        storeSummary(requestModel, responseModel);
    }

    private void storeSummary(RequestModel requestModel, ResponseModel responseModel) throws IOException {
        Date now = new Date();
        FileWriter fw = new FileWriter(generateSummaryFileName(), true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        int bodySize = responseModel.getBody().length();
        Long elapsed = responseModel.getResponseTime();
        String date = responseModel.getHeaderMap().get("Date");
        SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
        Date parsedDate= now;
        try {
            parsedDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long latency=now.getTime()-parsedDate.getTime();
        if(latency>elapsed){
            latency=0L;
        }

        String url = requestModel.getProtocol() + requestModel.getHost() + requestModel.getPath();

        String timestamp = new Date(now.getTime()) + "";
        out.println(requestModel.getMethod() + "\t"
                + elapsed + "\t"
                + bodySize + "\t"
                + timestamp + "\t"
                + url + "\t"
                + responseModel.getStatusCode() + "\t"
                + latency + "\t"
                + date + "\t");
        out.close();
        bw.close();
        fw.close();
    }

    public static String getMainClassName() {
        Map<Thread, StackTraceElement[]> stackTraceMap = Thread.getAllStackTraces();
        for (Thread t : stackTraceMap.keySet()) {
            {
                StackTraceElement[] mainStackTrace = stackTraceMap.get(t);
                for (StackTraceElement element : mainStackTrace) {
                    String stackTraseElement = element.toString();
                    if (stackTraseElement.contains("Test.java:")) {
                        return stackTraseElement.split("\\.")[0];
                    }
                }
            }
        }
        return null;
    }

    public void makeNewSummaryFile(String simpleName) throws IOException {
        File summaryFile = new File("target" + File.separator + simpleName + "_summary.csv");
        FileWriter fw = new FileWriter(summaryFile, false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        out.println("method\telapsed\tsize\ttimestamp\turl\tstatusCode\tlatency\tdate");
        out.close();
        bw.close();
        fw.close();
    }

    private String generateSummaryFileName() {
        return "target" + File.separator + getMainClassName() + "_summary.csv";
    }
}
