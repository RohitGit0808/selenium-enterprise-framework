package com.automation.pages.saucedemo;

import com.automation.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> cartItemNames;

    @FindBy(css = ".inventory_item_price")
    private List<WebElement> cartItemPrices;

    @FindBy(css = ".cart_quantity")
    private List<WebElement> cartItemQuantities;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    public boolean isCartPageDisplayed() {
        return "Your Cart".equals(getText(pageTitle));
    }

    public int getCartItemCount() {
        return cartItems.size();
    }

    public List<String> getCartItemNames() {
        return getTextsFromElements(cartItemNames);
    }

    public List<Double> getCartItemPrices() {
        return cartItemPrices.stream()
            .map(e -> Double.parseDouble(e.getText().replace("$", "")))
            .collect(Collectors.toList());
    }

    public boolean isItemInCart(String productName) {
        return getCartItemNames().contains(productName);
    }

    @Step("Remove item '{productName}' from cart")
    public CartPage removeItem(String productName) {
        log.info("Removing item from cart: {}", productName);
        String removeId = "remove-" + productName.toLowerCase().replace(" ", "-");
        click(By.id(removeId));
        return this;
    }

    @Step("Proceed to checkout")
    public CheckoutStepOnePage checkout() {
        log.info("Proceeding to checkout");
        click(checkoutButton);
        return new CheckoutStepOnePage();
    }

    @Step("Continue shopping")
    public InventoryPage continueShopping() {
        click(continueShoppingButton);
        return new InventoryPage();
    }

    public double getTotalCartValue() {
        return getCartItemPrices().stream()
            .mapToDouble(Double::doubleValue)
            .sum();
    }

    public boolean isCartEmpty() {
        return cartItems.isEmpty();
    }
}
