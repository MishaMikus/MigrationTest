package client;

import model.RequestModel;
import model.ResponseModel;
import org.apache.log4j.Logger;
import utils.StringFormatter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseClient {

    abstract ResponseModel call(RequestModel requestModel);

    private Map<String, String> cookies = new HashMap<>();

    private final Logger LOGGER = Logger.getLogger(this.getClass());

    public void startLog(String method, String path) {
        LOGGER.info("START [" + method + "]\t[" + path + "]");
    }

    public void endLog(String method, String path, Object code, Date start) {
        LOGGER.info("END [" + method + "]\t" +
                "[" + path + "]\t" +
                "[code:" + code + "]\t" +
                "[responseTime : " + StringFormatter.elapsed(start) + "]");
    }
}
