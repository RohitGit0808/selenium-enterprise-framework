package com.automation.pages.saucedemo;

import com.automation.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryPage extends BasePage {

    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(css = ".inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> productNames;

    @FindBy(css = ".inventory_item_price")
    private List<WebElement> productPrices;

    @FindBy(css = "[data-test='product_sort_container']")
    private WebElement sortDropdown;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartIcon;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement hamburgerMenu;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    @FindBy(id = "about_sidebar_link")
    private WebElement aboutLink;

    @FindBy(id = "reset_app_state_sidebar_link")
    private WebElement resetAppLink;

    public boolean isInventoryPageDisplayed() {
        wait.waitForVisibility(pageTitle);
        return "Products".equals(getText(pageTitle));
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public int getInventoryItemCount() {
        return inventoryItems.size();
    }

    public List<String> getProductNames() {
        return getTextsFromElements(productNames);
    }

    public List<Double> getProductPrices() {
        return productPrices.stream()
            .map(e -> Double.parseDouble(e.getText().replace("$", "")))
            .collect(Collectors.toList());
    }

    @Step("Add product '{productName}' to cart")
    public InventoryPage addProductToCart(String productName) {
        log.info("Adding product to cart: {}", productName);
        String buttonId = "add-to-cart-" + productName.toLowerCase().replace(" ", "-");
        WebElement addButton = driver.findElement(By.id(buttonId));
        click(addButton);
        return this;
    }

    @Step("Add first product to cart")
    public InventoryPage addFirstProductToCart() {
        WebElement firstAddButton = inventoryItems.get(0)
            .findElement(By.cssSelector(".btn_inventory"));
        click(firstAddButton);
        return this;
    }

    @Step("Remove product '{productName}' from cart")
    public InventoryPage removeProductFromCart(String productName) {
        log.info("Removing product from cart: {}", productName);
        String buttonId = "remove-" + productName.toLowerCase().replace(" ", "-");
        WebElement removeButton = driver.findElement(By.id(buttonId));
        click(removeButton);
        return this;
    }

    @Step("Sort products by '{sortOption}'")
    public InventoryPage sortProducts(String sortOption) {
        log.info("Sorting products by: {}", sortOption);
        selectByVisibleText(sortDropdown, sortOption);
        return this;
    }

    @Step("Open cart")
    public CartPage openCart() {
        log.info("Opening cart");
        click(cartIcon);
        return new CartPage();
    }

    public int getCartItemCount() {
        if (isDisplayed(cartBadge)) {
            return Integer.parseInt(getText(cartBadge));
        }
        return 0;
    }

    @Step("Click on product '{productName}'")
    public ProductDetailPage clickOnProduct(String productName) {
        log.info("Clicking on product: {}", productName);
        productNames.stream()
            .filter(e -> e.getText().equals(productName))
            .findFirst()
            .ifPresent(this::click);
        return new ProductDetailPage();
    }

    @Step("Open sidebar menu")
    public InventoryPage openSidebarMenu() {
        click(hamburgerMenu);
        return this;
    }

    @Step("Logout via sidebar")
    public LoginPage logout() {
        openSidebarMenu();
        wait.waitForElementToBeClickable(logoutLink);
        click(logoutLink);
        return new LoginPage();
    }

    @Step("Reset app state")
    public InventoryPage resetAppState() {
        openSidebarMenu();
        wait.waitForElementToBeClickable(resetAppLink);
        click(resetAppLink);
        return this;
    }

    public boolean isAddToCartButtonPresent(String productName) {
        String buttonId = "add-to-cart-" + productName.toLowerCase().replace(" ", "-");
        return isPresent(By.id(buttonId));
    }

    public boolean isRemoveButtonPresent(String productName) {
        String buttonId = "remove-" + productName.toLowerCase().replace(" ", "-");
        return isPresent(By.id(buttonId));
    }

    public List<Double> getSortedPricesAscending() {
        return getProductPrices().stream().sorted().collect(Collectors.toList());
    }

    public List<Double> getSortedPricesDescending() {
        return getProductPrices().stream().sorted((a, b) -> Double.compare(b, a)).collect(Collectors.toList());
    }
}
