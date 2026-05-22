package com.automation.stepdefs;

import com.automation.config.ConfigManager;
import com.automation.config.DriverManager;
import com.automation.pages.saucedemo.InventoryPage;
import com.automation.pages.saucedemo.LoginPage;
import io.cucumber.java.en.*;
import org.assertj.core.api.Assertions;

public class LoginSteps {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        DriverManager.getDriver().get(ConfigManager.getInstance().getBaseUrl());
        loginPage = new LoginPage();
        Assertions.assertThat(loginPage.isLoginPageDisplayed())
            .as("Login page should be displayed").isTrue();
    }

    @When("the user enters username {string} and password {string}")
    public void theUserEntersUsernameAndPassword(String username, String password) {
        loginPage.enterUsername(username).enterPassword(password);
    }

    @When("the user clicks the login button")
    public void theUserClicksTheLoginButton() {
        inventoryPage = loginPage.clickLogin();
    }

    @When("the user attempts to login with username {string} and password {string}")
    public void theUserAttemptsLoginWithCredentials(String username, String password) {
        loginPage = loginPage.loginWithInvalidCredentials(username, password);
    }

    @Then("the user should be on the inventory page")
    public void theUserShouldBeOnTheInventoryPage() {
        Assertions.assertThat(inventoryPage.isInventoryPageDisplayed())
            .as("Should be on inventory page").isTrue();
    }

    @Then("the login error message {string} should be displayed")
    public void theLoginErrorMessageShouldBeDisplayed(String expectedError) {
        Assertions.assertThat(loginPage.isErrorMessageDisplayed())
            .as("Error message should be visible").isTrue();
        Assertions.assertThat(loginPage.getErrorMessage())
            .as("Error message text").contains(expectedError);
    }

    @Then("the login page should still be displayed")
    public void theLoginPageShouldStillBeDisplayed() {
        Assertions.assertThat(loginPage.isLoginPageDisplayed())
            .as("Login page should still be visible").isTrue();
    }
}
