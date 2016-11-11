package listener;

import org.apache.log4j.Logger;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import utils.StringFormatter;

import java.util.Date;

public class InvoceMethodListener implements IInvokedMethodListener {
    private Date start;

    private final Logger LOGGER = Logger.getLogger(this.getClass());

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        LOGGER.info("START " + method.getTestMethod().getMethodName() + " [test : " + testResult.getTestClass().getName() + "]");
        start = new Date();
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        LOGGER.info("END   " + method.getTestMethod().getMethodName()
                + " [elapsed : " + StringFormatter.elapsed(start) + "]"
                + " [result : " + (testResult.isSuccess() ? "SUCCESS" : "FAIL") + "]"
        );
    }
}