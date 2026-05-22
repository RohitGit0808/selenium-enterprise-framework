package com.automation.pages.saucedemo;

import com.automation.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutCompletePage extends BasePage {

    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(css = ".complete-header")
    private WebElement successHeader;

    @FindBy(css = ".complete-text")
    private WebElement successText;

    @FindBy(css = ".pony_express")
    private WebElement deliveryImage;

    @FindBy(id = "back-to-products")
    private WebElement backToProductsButton;

    public boolean isOrderConfirmationDisplayed() {
        return "Checkout: Complete!".equals(getText(pageTitle));
    }

    public String getSuccessHeader() {
        return getText(successHeader);
    }

    public String getSuccessText() {
        return getText(successText);
    }

    public boolean isDeliveryImageDisplayed() {
        return isDisplayed(deliveryImage);
    }

    public boolean isOrderSuccessful() {
        return isDisplayed(successHeader) &&
               getText(successHeader).contains("Thank you for your order");
    }

    @Step("Go back to products")
    public InventoryPage backToProducts() {
        click(backToProductsButton);
        return new InventoryPage();
    }
}
