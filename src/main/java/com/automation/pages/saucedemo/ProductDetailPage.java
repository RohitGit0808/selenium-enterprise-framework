package com.automation.pages.saucedemo;

import com.automation.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductDetailPage extends BasePage {

    @FindBy(css = ".inventory_details_name")
    private WebElement productName;

    @FindBy(css = ".inventory_details_price")
    private WebElement productPrice;

    @FindBy(css = ".inventory_details_desc")
    private WebElement productDescription;

    @FindBy(css = ".inventory_details_img")
    private WebElement productImage;

    @FindBy(css = ".btn_inventory")
    private WebElement addToCartButton;

    @FindBy(css = ".btn_secondary")
    private WebElement backButton;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartIcon;

    public String getProductName() {
        return getText(productName);
    }

    public String getProductPrice() {
        return getText(productPrice);
    }

    public String getProductDescription() {
        return getText(productDescription);
    }

    @Step("Add product to cart from detail page")
    public ProductDetailPage addToCart() {
        click(addToCartButton);
        return this;
    }

    @Step("Go back to inventory")
    public InventoryPage goBack() {
        click(backButton);
        return new InventoryPage();
    }

    public boolean isProductImageDisplayed() {
        return isDisplayed(productImage);
    }

    public String getAddToCartButtonText() {
        return getText(addToCartButton);
    }
}
