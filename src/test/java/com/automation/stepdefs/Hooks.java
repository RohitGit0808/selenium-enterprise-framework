package com.automation.stepdefs;

import com.automation.config.DriverManager;
import com.automation.utils.ScreenshotUtils;
import io.cucumber.java.*;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;

public class Hooks {

    private static final Logger log = LogManager.getLogger(Hooks.class);

    @Before(order = 0)
    public void setUp(Scenario scenario) {
        log.info("===== BDD SCENARIO STARTING: {} =====", scenario.getName());
        DriverManager.initDriver();
    }

    @After(order = 0)
    public void tearDown(Scenario scenario) {
        log.info("===== BDD SCENARIO FINISHED: {} — Status: {} =====",
            scenario.getName(), scenario.getStatus());

        if (scenario.isFailed()) {
            log.error("Scenario failed: {}", scenario.getName());
            byte[] screenshot = DriverManager.getDriver()
                .getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failure Screenshot");

            Allure.addAttachment("Failure Screenshot - " + scenario.getName(),
                new ByteArrayInputStream(screenshot));
        }

        DriverManager.quitDriver();
    }

    @BeforeStep
    public void beforeStep() {
        // Intentionally empty — can be used for step-level setup
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        if (scenario.isFailed()) {
            log.warn("Step failed in scenario: {}", scenario.getName());
        }
    }
}
