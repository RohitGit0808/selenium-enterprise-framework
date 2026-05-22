package com.automation.constants;

public final class FrameworkConstants {

    private FrameworkConstants() {}

    // Paths
    public static final String CONFIG_FILE_PATH = "src/test/resources/config/config.properties";
    public static final String TESTDATA_JSON_PATH = "src/test/resources/testdata/testdata.json";
    public static final String TESTDATA_EXCEL_PATH = "src/test/resources/testdata/testdata.xlsx";
    public static final String EXTENT_REPORT_PATH = System.getProperty("user.dir") + "/test-output/ExtentReport.html";
    public static final String SCREENSHOT_DIR = System.getProperty("user.dir") + "/test-output/screenshots/";
    public static final String ALLURE_RESULTS_DIR = "target/allure-results";

    // Timeouts
    public static final int EXPLICIT_WAIT = 20;
    public static final int IMPLICIT_WAIT = 10;
    public static final int PAGE_LOAD_TIMEOUT = 60;
    public static final int FLUENT_WAIT_TIMEOUT = 30;
    public static final int FLUENT_WAIT_POLLING = 500;
    public static final int RETRY_COUNT = 1;

    // URLs
    public static final String SAUCEDEMO_URL = "https://www.saucedemo.com";
    public static final String SAUCEDEMO_INVENTORY_URL = "https://www.saucedemo.com/inventory.html";
    public static final String SAUCEDEMO_CART_URL = "https://www.saucedemo.com/cart.html";

    // Test Users
    public static final String STANDARD_USER = "standard_user";
    public static final String LOCKED_USER = "locked_out_user";
    public static final String PROBLEM_USER = "problem_user";
    public static final String PERFORMANCE_USER = "performance_glitch_user";
    public static final String ERROR_USER = "error_user";
    public static final String VISUAL_USER = "visual_user";
    public static final String PASSWORD = "secret_sauce";

    // Test Tags
    public static final String SMOKE = "smoke";
    public static final String REGRESSION = "regression";
    public static final String CRITICAL = "critical";
    public static final String SANITY = "sanity";

    // Browser
    public static final String CHROME = "chrome";
    public static final String FIREFOX = "firefox";
    public static final String EDGE = "edge";

    // Selenium Grid
    public static final String GRID_URL = "http://localhost:4444/wd/hub";
    public static final String REMOTE_CHROME_VERSION = "latest";
}
