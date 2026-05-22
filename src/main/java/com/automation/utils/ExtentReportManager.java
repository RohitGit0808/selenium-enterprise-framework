package com.automation.utils;

import com.automation.constants.FrameworkConstants;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class ExtentReportManager {

    private static final Logger log = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    private ExtentReportManager() {}

    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            createInstance();
        }
        return extentReports;
    }

    private static void createInstance() {
        new File(FrameworkConstants.EXTENT_REPORT_PATH).getParentFile().mkdirs();

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(FrameworkConstants.EXTENT_REPORT_PATH);
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("Selenium Enterprise Framework - Test Report");
        sparkReporter.config().setReportName("Automation Execution Report");
        sparkReporter.config().setEncoding("UTF-8");
        sparkReporter.config().setTimeStampFormat("dd/MM/yyyy HH:mm:ss");

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        extentReports.setSystemInfo("Framework", "Selenium Enterprise Framework v2.0");
        extentReports.setSystemInfo("Author", "QA Automation Team");

        log.info("ExtentReports initialized at: {}", FrameworkConstants.EXTENT_REPORT_PATH);
    }

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    public static void setTest(ExtentTest test) {
        extentTest.set(test);
    }

    public static void removeTest() {
        extentTest.remove();
    }

    public static void flushReports() {
        if (extentReports != null) {
            extentReports.flush();
            log.info("ExtentReports flushed and saved.");
        }
    }
}
