package com.automation.tests;

import com.automation.annotations.FrameworkAnnotation;
import com.automation.base.BaseTest;
import com.automation.constants.FrameworkConstants;
import com.automation.pages.saucedemo.InventoryPage;
import com.automation.pages.saucedemo.LoginPage;
import com.automation.utils.RetryAnalyzer;
import io.qameta.allure.*;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("Authentication")
@Feature("Login Functionality")
public class LoginTest extends BaseTest {

    @Test(
        description = "Standard user can login successfully",
        groups = {FrameworkConstants.SMOKE, FrameworkConstants.REGRESSION, FrameworkConstants.CRITICAL},
        retryAnalyzer = RetryAnalyzer.class
    )
    @FrameworkAnnotation(author = "QA Team", category = "smoke", description = "Verifies standard user login")
    @Story("Valid user login")
    @Severity(SeverityLevel.BLOCKER)
    public void testStandardUserLogin() {
        LoginPage loginPage = openApp();

        Assertions.assertThat(loginPage.isLoginPageDisplayed())
            .as("Login page should be displayed")
            .isTrue();

        InventoryPage inventoryPage = loginPage.loginAs(
            FrameworkConstants.STANDARD_USER,
            FrameworkConstants.PASSWORD
        );

        Assertions.assertThat(inventoryPage.isInventoryPageDisplayed())
            .as("Inventory page should be displayed after successful login")
            .isTrue();

        Assertions.assertThat(inventoryPage.getInventoryItemCount())
            .as("Inventory should have 6 products")
            .isEqualTo(6);

        log.info("Standard user login test passed");
    }

    @Test(
        description = "Login with invalid username shows error",
        groups = {FrameworkConstants.REGRESSION},
        retryAnalyzer = RetryAnalyzer.class
    )
    @FrameworkAnnotation(author = "QA Team", category = "regression", description = "Verifies error for invalid username")
    @Story("Invalid credentials")
    @Severity(SeverityLevel.CRITICAL)
    public void testInvalidUsernameShowsError() {
        LoginPage loginPage = openApp()
            .loginWithInvalidCredentials("invalid_user", FrameworkConstants.PASSWORD);

        Assertions.assertThat(loginPage.isErrorMessageDisplayed())
            .as("Error message should be displayed for invalid username")
            .isTrue();

        Assertions.assertThat(loginPage.getErrorMessage())
            .as("Error message text should match expected")
            .contains("Username and password do not match");
    }

    @Test(
        description = "Login with invalid password shows error",
        groups = {FrameworkConstants.REGRESSION},
        retryAnalyzer = RetryAnalyzer.class
    )
    @FrameworkAnnotation(author = "QA Team", category = "regression")
    @Story("Invalid credentials")
    @Severity(SeverityLevel.CRITICAL)
    public void testInvalidPasswordShowsError() {
        LoginPage loginPage = openApp()
            .loginWithInvalidCredentials(FrameworkConstants.STANDARD_USER, "wrong_password");

        Assertions.assertThat(loginPage.isErrorMessageDisplayed()).isTrue();
        Assertions.assertThat(loginPage.getErrorMessage())
            .contains("Username and password do not match");
    }

    @Test(
        description = "Locked out user cannot login",
        groups = {FrameworkConstants.REGRESSION},
        retryAnalyzer = RetryAnalyzer.class
    )
    @FrameworkAnnotation(author = "QA Team", category = "regression", description = "Verifies locked user error message")
    @Story("Locked user account")
    @Severity(SeverityLevel.NORMAL)
    public void testLockedOutUserCannotLogin() {
        LoginPage loginPage = openApp()
            .loginWithInvalidCredentials(FrameworkConstants.LOCKED_USER, FrameworkConstants.PASSWORD);

        Assertions.assertThat(loginPage.isErrorMessageDisplayed()).isTrue();
        Assertions.assertThat(loginPage.getErrorMessage())
            .contains("Sorry, this user has been locked out");
    }

    @Test(
        description = "Login with empty username shows error",
        groups = {FrameworkConstants.REGRESSION},
        retryAnalyzer = RetryAnalyzer.class
    )
    @Story("Empty credentials validation")
    @Severity(SeverityLevel.MINOR)
    public void testEmptyUsernameShowsError() {
        LoginPage loginPage = openApp()
            .loginWithInvalidCredentials("", FrameworkConstants.PASSWORD);

        Assertions.assertThat(loginPage.isErrorMessageDisplayed()).isTrue();
        Assertions.assertThat(loginPage.getErrorMessage())
            .contains("Username is required");
    }

    @Test(
        description = "Login with empty password shows error",
        groups = {FrameworkConstants.REGRESSION}
    )
    @Story("Empty credentials validation")
    @Severity(SeverityLevel.MINOR)
    public void testEmptyPasswordShowsError() {
        LoginPage loginPage = openApp()
            .loginWithInvalidCredentials(FrameworkConstants.STANDARD_USER, "");

        Assertions.assertThat(loginPage.isErrorMessageDisplayed()).isTrue();
        Assertions.assertThat(loginPage.getErrorMessage())
            .contains("Password is required");
    }

    @Test(
        description = "Login page UI elements are visible",
        groups = {FrameworkConstants.SANITY}
    )
    @Story("Login page UI")
    @Severity(SeverityLevel.MINOR)
    public void testLoginPageElements() {
        LoginPage loginPage = openApp();

        Assertions.assertThat(loginPage.isLoginPageDisplayed())
            .as("Login page should be visible").isTrue();
        Assertions.assertThat(loginPage.isLoginButtonEnabled())
            .as("Login button should be enabled").isTrue();
        Assertions.assertThat(loginPage.getUsernamePlaceholder())
            .as("Username placeholder").isEqualTo("Username");
        Assertions.assertThat(loginPage.getPasswordPlaceholder())
            .as("Password placeholder").isEqualTo("Password");
    }

    @Test(
        description = "Multiple user types can login (data-driven)",
        groups = {FrameworkConstants.REGRESSION},
        dataProvider = "validUsers"
    )
    @Story("Multiple user types")
    @Severity(SeverityLevel.NORMAL)
    public void testMultipleUserTypesLogin(String username, String expectedResult) {
        log.info("Testing login for user: {} — expected: {}", username, expectedResult);

        LoginPage loginPage = openApp();
        loginPage.enterUsername(username).enterPassword(FrameworkConstants.PASSWORD);

        if ("success".equals(expectedResult)) {
            InventoryPage inventoryPage = loginPage.clickLogin();
            Assertions.assertThat(inventoryPage.isInventoryPageDisplayed()).isTrue();
        } else {
            loginPage.clickLoginExpectingError();
            Assertions.assertThat(loginPage.isErrorMessageDisplayed()).isTrue();
        }
    }

    @DataProvider(name = "validUsers")
    public Object[][] validUsersProvider() {
        return new Object[][]{
            {FrameworkConstants.STANDARD_USER, "success"},
            {FrameworkConstants.PROBLEM_USER, "success"},
            {FrameworkConstants.PERFORMANCE_USER, "success"},
            {FrameworkConstants.LOCKED_USER, "failure"}
        };
    }
}
