package com.automation.listeners;

import com.automation.annotations.FrameworkAnnotation;
import com.automation.utils.ExtentReportManager;
import com.automation.utils.ScreenshotUtils;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;

import java.util.Arrays;

public class TestListener implements ITestListener, ISuiteListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);

    // ─── Suite ───────────────────────────────────────────────────────────────

    @Override
    public void onStart(ISuite suite) {
        log.info("===== TEST SUITE STARTED: {} =====", suite.getName());
    }

    @Override
    public void onFinish(ISuite suite) {
        log.info("===== TEST SUITE FINISHED: {} =====", suite.getName());
        ExtentReportManager.flushReports();
    }

    // ─── Test ─────────────────────────────────────────────────────────────────

    @Override
    public void onTestStart(ITestResult result) {
        log.info(">>> TEST STARTED: {}.{}", result.getTestClass().getName(), result.getName());

        String testName = result.getName();
        ExtentTest test = ExtentReportManager.getInstance()
            .createTest(testName, getTestDescription(result));
        ExtentReportManager.setTest(test);

        FrameworkAnnotation annotation = result.getMethod()
            .getConstructorOrMethod()
            .getMethod()
            .getAnnotation(FrameworkAnnotation.class);

        if (annotation != null) {
            test.assignAuthor(annotation.author());
            test.assignCategory(annotation.category());
        }

        Allure.step("Test started: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info(">>> TEST PASSED: {}", result.getName());
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.PASS, "Test passed successfully");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error(">>> TEST FAILED: {} — {}", result.getName(), result.getThrowable().getMessage());

        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            String base64Screenshot = ScreenshotUtils.captureAsBase64();
            if (base64Screenshot != null) {
                test.fail("Test failed",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
            }
            test.log(Status.FAIL, result.getThrowable());
        }

        ScreenshotUtils.captureAndAttachToAllure("Failure - " + result.getName());
        ScreenshotUtils.attachPageSourceToAllure();
        Allure.step("Test failed: " + result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn(">>> TEST SKIPPED: {}", result.getName());
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.SKIP, "Test skipped: " + result.getThrowable());
        }
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        log.error(">>> TEST TIMED OUT: {}", result.getName());
        onTestFailure(result);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private String getTestDescription(ITestResult result) {
        FrameworkAnnotation annotation = result.getMethod()
            .getConstructorOrMethod()
            .getMethod()
            .getAnnotation(FrameworkAnnotation.class);

        if (annotation != null && !annotation.description().isEmpty()) {
            return annotation.description();
        }

        return "Test: " + result.getName() + " | Parameters: " +
            Arrays.toString(result.getParameters());
    }
}
