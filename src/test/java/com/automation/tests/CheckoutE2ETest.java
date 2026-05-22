package com.automation.tests;

import com.automation.annotations.FrameworkAnnotation;
import com.automation.base.BaseTest;
import com.automation.constants.FrameworkConstants;
import com.automation.pages.saucedemo.*;
import com.automation.utils.RetryAnalyzer;
import com.automation.utils.TestDataFactory;
import io.qameta.allure.*;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

@Epic("E-Commerce")
@Feature("Checkout Flow")
public class CheckoutE2ETest extends BaseTest {

    private static final String PRODUCT_1 = "sauce-labs-backpack";
    private static final String PRODUCT_2 = "sauce-labs-bike-light";

    @Test(
        description = "Complete end-to-end purchase flow for a single item",
        groups = {FrameworkConstants.SMOKE, FrameworkConstants.REGRESSION, FrameworkConstants.CRITICAL},
        retryAnalyzer = RetryAnalyzer.class
    )
    @FrameworkAnnotation(
        author = {"QA Team", "Senior QA"},
        category = {"e2e", "smoke"},
        description = "Full purchase flow: login → add to cart → checkout → confirm order"
    )
    @Story("Single item purchase")
    @Severity(SeverityLevel.BLOCKER)
    @Link(name = "SauceDemo App", url = "https://www.saucedemo.com")
    public void testCompleteCheckoutFlowSingleItem() {
        TestDataFactory.CheckoutData checkoutData = TestDataFactory.generateCheckoutData();

        // Login
        InventoryPage inventoryPage = loginAsStandardUser();
        Assertions.assertThat(inventoryPage.isInventoryPageDisplayed()).isTrue();

        // Add to cart
        inventoryPage.addProductToCart(PRODUCT_1);
        Assertions.assertThat(inventoryPage.getCartItemCount()).isEqualTo(1);

        // Navigate to cart
        CartPage cartPage = inventoryPage.openCart();
        Assertions.assertThat(cartPage.isCartPageDisplayed()).isTrue();
        Assertions.assertThat(cartPage.getCartItemCount()).isEqualTo(1);

        // Checkout step 1
        CheckoutStepOnePage checkoutStep1 = cartPage.checkout();
        Assertions.assertThat(checkoutStep1.isCheckoutStep1Displayed()).isTrue();

        // Fill checkout info
        CheckoutStepTwoPage checkoutStep2 = checkoutStep1.completeCheckoutInfo(
            checkoutData.getFirstName(),
            checkoutData.getLastName(),
            checkoutData.getPostalCode()
        );
        Assertions.assertThat(checkoutStep2.isCheckoutStep2Displayed()).isTrue();

        // Verify order summary
        Assertions.assertThat(checkoutStep2.getOrderItemCount()).isEqualTo(1);
        Assertions.assertThat(checkoutStep2.isTotalCorrect())
            .as("Total = subtotal + tax").isTrue();

        // Finish order
        CheckoutCompletePage confirmPage = checkoutStep2.finishOrder();
        Assertions.assertThat(confirmPage.isOrderConfirmationDisplayed()).isTrue();
        Assertions.assertThat(confirmPage.isOrderSuccessful())
            .as("Order should be confirmed with thank-you message").isTrue();
        Assertions.assertThat(confirmPage.isDeliveryImageDisplayed()).isTrue();

        log.info("E2E checkout flow completed successfully for user: {} {}",
            checkoutData.getFirstName(), checkoutData.getLastName());
    }

    @Test(
        description = "Complete checkout flow with multiple items",
        groups = {FrameworkConstants.REGRESSION, FrameworkConstants.CRITICAL},
        retryAnalyzer = RetryAnalyzer.class
    )
    @FrameworkAnnotation(author = "QA Team", category = "regression", description = "Multi-item cart checkout flow")
    @Story("Multi-item purchase")
    @Severity(SeverityLevel.BLOCKER)
    public void testCheckoutWithMultipleItems() {
        TestDataFactory.CheckoutData checkoutData = TestDataFactory.generateCheckoutData();

        InventoryPage inventoryPage = loginAsStandardUser();

        inventoryPage
            .addProductToCart(PRODUCT_1)
            .addProductToCart(PRODUCT_2);

        Assertions.assertThat(inventoryPage.getCartItemCount()).isEqualTo(2);

        CartPage cartPage = inventoryPage.openCart();
        List<String> cartItems = cartPage.getCartItemNames();
        Assertions.assertThat(cartItems).hasSize(2);

        CheckoutStepTwoPage checkoutStep2 = cartPage.checkout()
            .completeCheckoutInfo(
                checkoutData.getFirstName(),
                checkoutData.getLastName(),
                checkoutData.getPostalCode()
            );

        Assertions.assertThat(checkoutStep2.getOrderItemCount()).isEqualTo(2);
        Assertions.assertThat(checkoutStep2.isTotalCorrect()).isTrue();

        CheckoutCompletePage confirmPage = checkoutStep2.finishOrder();
        Assertions.assertThat(confirmPage.isOrderSuccessful()).isTrue();
    }

    @Test(
        description = "Checkout fails with missing first name",
        groups = {FrameworkConstants.REGRESSION}
    )
    @Story("Checkout validation")
    @Severity(SeverityLevel.CRITICAL)
    public void testCheckoutFailsWithMissingFirstName() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.addProductToCart(PRODUCT_1);

        CartPage cartPage = inventoryPage.openCart();
        CheckoutStepOnePage checkoutStep1 = cartPage.checkout();

        checkoutStep1.enterCheckoutInfo("", "Doe", "12345").clickContinueExpectingError();

        Assertions.assertThat(checkoutStep1.isErrorDisplayed()).isTrue();
        Assertions.assertThat(checkoutStep1.getErrorMessage())
            .contains("First Name is required");
    }

    @Test(
        description = "Checkout fails with missing last name",
        groups = {FrameworkConstants.REGRESSION}
    )
    @Story("Checkout validation")
    @Severity(SeverityLevel.CRITICAL)
    public void testCheckoutFailsWithMissingLastName() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.addProductToCart(PRODUCT_1);

        CartPage cartPage = inventoryPage.openCart();
        CheckoutStepOnePage checkoutStep1 = cartPage.checkout();

        checkoutStep1.enterCheckoutInfo("John", "", "12345").clickContinueExpectingError();

        Assertions.assertThat(checkoutStep1.isErrorDisplayed()).isTrue();
        Assertions.assertThat(checkoutStep1.getErrorMessage())
            .contains("Last Name is required");
    }

    @Test(
        description = "Checkout fails with missing postal code",
        groups = {FrameworkConstants.REGRESSION}
    )
    @Story("Checkout validation")
    @Severity(SeverityLevel.CRITICAL)
    public void testCheckoutFailsWithMissingPostalCode() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.addProductToCart(PRODUCT_1);

        CartPage cartPage = inventoryPage.openCart();
        CheckoutStepOnePage checkoutStep1 = cartPage.checkout();

        checkoutStep1.enterCheckoutInfo("John", "Doe", "").clickContinueExpectingError();

        Assertions.assertThat(checkoutStep1.isErrorDisplayed()).isTrue();
        Assertions.assertThat(checkoutStep1.getErrorMessage())
            .contains("Postal Code is required");
    }

    @Test(
        description = "User can cancel checkout and return to cart",
        groups = {FrameworkConstants.REGRESSION}
    )
    @Story("Checkout cancellation")
    @Severity(SeverityLevel.NORMAL)
    public void testCancelCheckoutReturnsToCart() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.addProductToCart(PRODUCT_1);

        CartPage cartPage = inventoryPage.openCart();
        CheckoutStepOnePage checkoutStep1 = cartPage.checkout();

        CartPage cancelledCart = checkoutStep1.cancel();
        Assertions.assertThat(cancelledCart.isCartPageDisplayed()).isTrue();
        Assertions.assertThat(cancelledCart.getCartItemCount())
            .as("Cart items should persist after cancel")
            .isEqualTo(1);
    }

    @Test(
        description = "Cart is empty after removing item before checkout",
        groups = {FrameworkConstants.REGRESSION}
    )
    @Story("Cart management")
    @Severity(SeverityLevel.NORMAL)
    public void testRemoveItemFromCart() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.addProductToCart(PRODUCT_1).addProductToCart(PRODUCT_2);

        CartPage cartPage = inventoryPage.openCart();
        Assertions.assertThat(cartPage.getCartItemCount()).isEqualTo(2);

        cartPage.removeItem("Sauce Labs Backpack");
        Assertions.assertThat(cartPage.getCartItemCount())
            .as("Cart should have 1 item after removal")
            .isEqualTo(1);
    }

    @Test(
        description = "Order total math is correct (data-driven)",
        groups = {FrameworkConstants.REGRESSION},
        dataProvider = "checkoutUsers"
    )
    @Story("Order summary validation")
    @Severity(SeverityLevel.NORMAL)
    public void testOrderTotalCalculation(String username, String product) {
        log.info("Testing order total for user: {} with product: {}", username, product);
        TestDataFactory.CheckoutData data = TestDataFactory.generateCheckoutData();

        InventoryPage inventoryPage = loginAs(username);
        inventoryPage.addProductToCart(product);

        CartPage cartPage = inventoryPage.openCart();
        CheckoutStepTwoPage checkoutStep2 = cartPage.checkout()
            .completeCheckoutInfo(data.getFirstName(), data.getLastName(), data.getPostalCode());

        Assertions.assertThat(checkoutStep2.isTotalCorrect())
            .as("Subtotal + Tax should equal Total for user: " + username)
            .isTrue();
    }

    @DataProvider(name = "checkoutUsers")
    public Object[][] checkoutUsersProvider() {
        return new Object[][]{
            {FrameworkConstants.STANDARD_USER, PRODUCT_1},
            {FrameworkConstants.STANDARD_USER, PRODUCT_2},
            {FrameworkConstants.PROBLEM_USER, PRODUCT_1}
        };
    }
}
