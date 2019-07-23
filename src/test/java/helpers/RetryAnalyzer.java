package helpers;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

//https://www.toolsqa.com/selenium-webdriver/retry-failed-tests-testng/
// This listener is used for retrying tests if they are failed

public class RetryAnalyzer implements IRetryAnalyzer {

    int counter = 0;
    int limit = 3;

    @Override
    public boolean retry(ITestResult result) {

        if(counter < limit) {
            counter++;
            return true;
        }
        return false;
    }
}
