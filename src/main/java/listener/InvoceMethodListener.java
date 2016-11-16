package listener;

import org.apache.log4j.Logger;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import utils.StringFormatter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InvoceMethodListener implements IInvokedMethodListener {

    private final Logger LOGGER = Logger.getLogger(this.getClass());

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        LOGGER.info("START " + method.getTestMethod().getMethodName() + " [test : " + testResult.getTestClass().getName() + "]");
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        LOGGER.info("END   " + method.getTestMethod().getMethodName()
                + " [elapsed : " + StringFormatter.elapsed(new Date(testResult.getStartMillis())) + "]"
                + " [result : " + (testResult.isSuccess() ? "SUCCESS" : "FAIL") + "]");

        Date now = new Date();
        FileWriter fw = null;
        try {
            fw = new FileWriter("target/report.csv", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        out.println(now+"\t"
                +method.getTestMethod().getMethodName() + "\t"
                +(now.getTime()-testResult.getStartMillis()) + "\t"
                + (testResult.isSuccess() ? "SUCCESS" : "FAIL") );
        out.close();
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}