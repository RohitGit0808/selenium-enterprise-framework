package com.automation.base;

import com.automation.config.ConfigManager;
import com.automation.config.DriverManager;
import com.automation.constants.FrameworkConstants;
import com.automation.pages.saucedemo.LoginPage;
import com.automation.pages.saucedemo.InventoryPage;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.*;

@Listeners(com.automation.listeners.TestListener.class)
public abstract class BaseTest {

    protected final Logger log = LogManager.getLogger(getClass());

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {
        log.info("========== SUITE SETUP: {} ==========", context.getSuite().getName());
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional String browser) {
        log.info("---------- Test Setup Starting ----------");

        if (browser != null && !browser.isBlank()) {
            System.setProperty("browser", browser);
        }

        DriverManager.initDriver();
        log.info("Driver initialized for thread: {}", Thread.currentThread().getId());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        log.info("---------- Test Teardown Starting ----------");
        DriverManager.quitDriver();
        log.info("Driver quit for thread: {}", Thread.currentThread().getId());
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        log.info("========== SUITE TEARDOWN ==========");
    }

    // ─── Helper Methods ───────────────────────────────────────────────────────

    @Step("Open application and navigate to login page")
    protected LoginPage openApp() {
        DriverManager.getDriver().get(ConfigManager.getInstance().getBaseUrl());
        return new LoginPage();
    }

    @Step("Login as standard user")
    protected InventoryPage loginAsStandardUser() {
        return openApp().loginAs(FrameworkConstants.STANDARD_USER, FrameworkConstants.PASSWORD);
    }

    @Step("Login as '{username}'")
    protected InventoryPage loginAs(String username) {
        return openApp().loginAs(username, FrameworkConstants.PASSWORD);
    }

    protected String getBaseUrl() {
        return ConfigManager.getInstance().getBaseUrl();
    }
}
