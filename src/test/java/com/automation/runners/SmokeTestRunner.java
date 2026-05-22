package com.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.automation.stepdefs"},
    tags = "@smoke",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/smoke-report.html",
        "json:target/cucumber-reports/smoke.json",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
    },
    monochrome = true
)
public class SmokeTestRunner extends AbstractTestNGCucumberTests {
}
