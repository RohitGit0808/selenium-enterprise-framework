package com.automation.utils;

import com.automation.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger log = LogManager.getLogger(RetryAnalyzer.class);
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < FrameworkConstants.RETRY_COUNT) {
            retryCount++;
            log.warn("Retrying test '{}' — attempt {}/{}", result.getName(), retryCount, FrameworkConstants.RETRY_COUNT);
            return true;
        }
        return false;
    }
}
