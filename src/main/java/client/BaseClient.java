package client;

import org.apache.log4j.Logger;
import utils.StringFormatter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaseClient {

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
}
