package com.automation.pages.saucedemo;

import com.automation.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    @FindBy(id = "user-name")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    @FindBy(css = ".login_logo")
    private WebElement loginLogo;

    @FindBy(css = ".login-box")
    private WebElement loginBox;

    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        log.info("Entering username: {}", username);
        type(usernameInput, username);
        return this;
    }

    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        log.info("Entering password");
        type(passwordInput, password);
        return this;
    }

    @Step("Click login button")
    public InventoryPage clickLogin() {
        log.info("Clicking login button");
        click(loginButton);
        return new InventoryPage();
    }

    @Step("Login as '{username}'")
    public InventoryPage loginAs(String username, String password) {
        return enterUsername(username)
            .enterPassword(password)
            .clickLogin();
    }

    @Step("Attempt login with invalid credentials")
    public LoginPage loginWithInvalidCredentials(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        click(loginButton);
        return this;
    }

    @Step("Click login button expecting error")
    public LoginPage clickLoginExpectingError() {
        click(loginButton);
        return this;
    }

    public String getErrorMessage() {
        wait.waitForVisibility(errorMessage);
        return getText(errorMessage);
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }

    public boolean isLoginPageDisplayed() {
        return isDisplayed(loginLogo) && isDisplayed(loginButton);
    }

    public boolean isLoginButtonEnabled() {
        return isEnabled(loginButton);
    }

    public String getUsernamePlaceholder() {
        return getAttribute(usernameInput, "placeholder");
    }

    public String getPasswordPlaceholder() {
        return getAttribute(passwordInput, "placeholder");
    }

    @Step("Clear login form")
    public LoginPage clearForm() {
        usernameInput.clear();
        passwordInput.clear();
        return this;
    }
}
