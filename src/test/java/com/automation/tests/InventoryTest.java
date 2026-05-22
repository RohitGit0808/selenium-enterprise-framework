package com.automation.tests;

import com.automation.annotations.FrameworkAnnotation;
import com.automation.base.BaseTest;
import com.automation.constants.FrameworkConstants;
import com.automation.pages.saucedemo.InventoryPage;
import com.automation.utils.RetryAnalyzer;
import io.qameta.allure.*;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Product Catalog")
@Feature("Inventory Management")
public class InventoryTest extends BaseTest {

    @Test(
        description = "All 6 products are displayed on inventory page",
        groups = {FrameworkConstants.SMOKE, FrameworkConstants.REGRESSION},
        retryAnalyzer = RetryAnalyzer.class
    )
    @FrameworkAnnotation(author = "QA Team", category = "smoke", description = "Verifies product count on inventory")
    @Story("Product display")
    @Severity(SeverityLevel.CRITICAL)
    public void testInventoryShowsAllProducts() {
        InventoryPage inventoryPage = loginAsStandardUser();

        Assertions.assertThat(inventoryPage.getInventoryItemCount())
            .as("Should display 6 products")
            .isEqualTo(6);

        Assertions.assertThat(inventoryPage.getProductNames())
            .as("Product names should not be empty")
            .isNotEmpty()
            .hasSize(6);

        log.info("Inventory shows all {} products", inventoryPage.getInventoryItemCount());
    }

    @Test(
        description = "Sort products by price low to high",
        groups = {FrameworkConstants.REGRESSION},
        retryAnalyzer = RetryAnalyzer.class
    )
    @FrameworkAnnotation(author = "QA Team", category = "regression", description = "Verifies price sort ascending")
    @Story("Product sorting")
    @Severity(SeverityLevel.NORMAL)
    public void testSortByPriceLowToHigh() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.sortProducts("Price (low to high)");

        List<Double> prices = inventoryPage.getProductPrices();
        List<Double> sortedPrices = inventoryPage.getSortedPricesAscending();

        Assertions.assertThat(prices)
            .as("Products should be sorted by price ascending")
            .isEqualTo(sortedPrices);

        log.info("Price sort ascending verified: {}", prices);
    }

    @Test(
        description = "Sort products by price high to low",
        groups = {FrameworkConstants.REGRESSION}
    )
    @Story("Product sorting")
    @Severity(SeverityLevel.NORMAL)
    public void testSortByPriceHighToLow() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.sortProducts("Price (high to low)");

        List<Double> prices = inventoryPage.getProductPrices();
        List<Double> sortedPrices = inventoryPage.getSortedPricesDescending();

        Assertions.assertThat(prices)
            .as("Products should be sorted by price descending")
            .isEqualTo(sortedPrices);
    }

    @Test(
        description = "Sort products by name A to Z",
        groups = {FrameworkConstants.REGRESSION}
    )
    @Story("Product sorting")
    @Severity(SeverityLevel.NORMAL)
    public void testSortByNameAToZ() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.sortProducts("Name (A to Z)");

        List<String> names = inventoryPage.getProductNames();
        List<String> sortedNames = names.stream().sorted().toList();

        Assertions.assertThat(names)
            .as("Products should be sorted A-Z")
            .isEqualTo(sortedNames);
    }

    @Test(
        description = "Sort products by name Z to A",
        groups = {FrameworkConstants.REGRESSION}
    )
    @Story("Product sorting")
    @Severity(SeverityLevel.NORMAL)
    public void testSortByNameZToA() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.sortProducts("Name (Z to A)");

        List<String> names = inventoryPage.getProductNames();
        List<String> sortedDesc = names.stream().sorted((a, b) -> b.compareTo(a)).toList();

        Assertions.assertThat(names)
            .as("Products should be sorted Z-A")
            .isEqualTo(sortedDesc);
    }

    @Test(
        description = "Add product to cart increments cart badge",
        groups = {FrameworkConstants.SMOKE, FrameworkConstants.REGRESSION},
        retryAnalyzer = RetryAnalyzer.class
    )
    @FrameworkAnnotation(author = "QA Team", category = "smoke", description = "Add to cart increments badge count")
    @Story("Cart management")
    @Severity(SeverityLevel.BLOCKER)
    public void testAddProductToCartIncrementsBadge() {
        InventoryPage inventoryPage = loginAsStandardUser();

        Assertions.assertThat(inventoryPage.getCartItemCount())
            .as("Cart should be empty initially")
            .isEqualTo(0);

        inventoryPage.addProductToCart("sauce-labs-backpack");

        Assertions.assertThat(inventoryPage.getCartItemCount())
            .as("Cart badge should show 1 after adding a product")
            .isEqualTo(1);
    }

    @Test(
        description = "Add multiple products to cart",
        groups = {FrameworkConstants.REGRESSION}
    )
    @Story("Cart management")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddMultipleProductsToCart() {
        InventoryPage inventoryPage = loginAsStandardUser();

        inventoryPage
            .addProductToCart("sauce-labs-backpack")
            .addProductToCart("sauce-labs-bike-light")
            .addProductToCart("sauce-labs-bolt-t-shirt");

        Assertions.assertThat(inventoryPage.getCartItemCount())
            .as("Cart should have 3 items")
            .isEqualTo(3);
    }

    @Test(
        description = "Remove product from cart decrements cart badge",
        groups = {FrameworkConstants.REGRESSION}
    )
    @Story("Cart management")
    @Severity(SeverityLevel.NORMAL)
    public void testRemoveProductFromCartDecrementsBadge() {
        InventoryPage inventoryPage = loginAsStandardUser();

        inventoryPage
            .addProductToCart("sauce-labs-backpack")
            .addProductToCart("sauce-labs-bike-light");

        Assertions.assertThat(inventoryPage.getCartItemCount()).isEqualTo(2);

        inventoryPage.removeProductFromCart("sauce-labs-backpack");

        Assertions.assertThat(inventoryPage.getCartItemCount())
            .as("Cart should show 1 after removing one item")
            .isEqualTo(1);
    }

    @Test(
        description = "Product has add-to-cart button initially",
        groups = {FrameworkConstants.SANITY}
    )
    @Story("Cart management")
    @Severity(SeverityLevel.NORMAL)
    public void testAddToCartButtonInitiallyPresent() {
        InventoryPage inventoryPage = loginAsStandardUser();

        Assertions.assertThat(inventoryPage.isAddToCartButtonPresent("sauce-labs-backpack"))
            .as("Add to cart button should be present before adding")
            .isTrue();

        inventoryPage.addProductToCart("sauce-labs-backpack");

        Assertions.assertThat(inventoryPage.isRemoveButtonPresent("sauce-labs-backpack"))
            .as("Remove button should appear after adding to cart")
            .isTrue();

        Assertions.assertThat(inventoryPage.isAddToCartButtonPresent("sauce-labs-backpack"))
            .as("Add button should disappear after adding to cart")
            .isFalse();
    }

    @Test(
        description = "User can logout from inventory page",
        groups = {FrameworkConstants.REGRESSION, FrameworkConstants.SMOKE}
    )
    @Story("Session management")
    @Severity(SeverityLevel.CRITICAL)
    public void testLogout() {
        InventoryPage inventoryPage = loginAsStandardUser();

        Assertions.assertThat(inventoryPage.isInventoryPageDisplayed()).isTrue();

        var loginPage = inventoryPage.logout();

        Assertions.assertThat(loginPage.isLoginPageDisplayed())
            .as("Should be redirected to login page after logout")
            .isTrue();
    }
}
