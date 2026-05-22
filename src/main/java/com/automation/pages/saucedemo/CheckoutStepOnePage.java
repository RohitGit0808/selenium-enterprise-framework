package com.automation.pages.saucedemo;

import com.automation.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutStepOnePage extends BasePage {

    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(id = "first-name")
    private WebElement firstNameInput;

    @FindBy(id = "last-name")
    private WebElement lastNameInput;

    @FindBy(id = "postal-code")
    private WebElement postalCodeInput;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    public boolean isCheckoutStep1Displayed() {
        return "Checkout: Your Information".equals(getText(pageTitle));
    }

    @Step("Enter checkout information: {firstName} {lastName}, {postalCode}")
    public CheckoutStepOnePage enterCheckoutInfo(String firstName, String lastName, String postalCode) {
        log.info("Entering checkout info for: {} {}", firstName, lastName);
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(postalCodeInput, postalCode);
        return this;
    }

    @Step("Click continue button")
    public CheckoutStepTwoPage clickContinue() {
        click(continueButton);
        return new CheckoutStepTwoPage();
    }

    @Step("Click continue expecting error")
    public CheckoutStepOnePage clickContinueExpectingError() {
        click(continueButton);
        return this;
    }

    @Step("Cancel checkout")
    public CartPage cancel() {
        click(cancelButton);
        return new CartPage();
    }

    public String getErrorMessage() {
        wait.waitForVisibility(errorMessage);
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    @Step("Complete checkout step 1 with: {firstName} {lastName}, {postalCode}")
    public CheckoutStepTwoPage completeCheckoutInfo(String firstName, String lastName, String postalCode) {
        return enterCheckoutInfo(firstName, lastName, postalCode).clickContinue();
    }
}
