package com.automation.stepdefs;

import com.automation.config.ConfigManager;
import com.automation.config.DriverManager;
import com.automation.constants.FrameworkConstants;
import com.automation.pages.saucedemo.*;
import io.cucumber.java.en.*;
import org.assertj.core.api.Assertions;

public class CheckoutSteps {

    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutStepOnePage checkoutStep1;
    private CheckoutStepTwoPage checkoutStep2;
    private CheckoutCompletePage checkoutCompletePage;

    @Given("the user is logged in as standard user")
    public void theUserIsLoggedInAsStandardUser() {
        DriverManager.getDriver().get(ConfigManager.getInstance().getBaseUrl());
        LoginPage loginPage = new LoginPage();
        inventoryPage = loginPage.loginAs(FrameworkConstants.STANDARD_USER, FrameworkConstants.PASSWORD);
        Assertions.assertThat(inventoryPage.isInventoryPageDisplayed()).isTrue();
    }

    @When("the user adds {string} to the cart")
    public void theUserAddsProductToCart(String productId) {
        inventoryPage.addProductToCart(productId);
    }

    @When("the user opens the cart")
    public void theUserOpensTheCart() {
        cartPage = inventoryPage.openCart();
    }

    @Then("the cart should contain {int} item(s)")
    public void theCartShouldContainItems(int expectedCount) {
        if (cartPage != null) {
            Assertions.assertThat(cartPage.getCartItemCount())
                .as("Cart should have " + expectedCount + " items")
                .isEqualTo(expectedCount);
        } else {
            Assertions.assertThat(inventoryPage.getCartItemCount())
                .as("Cart badge should show " + expectedCount)
                .isEqualTo(expectedCount);
        }
    }

    @When("the user proceeds to checkout")
    public void theUserProceedsToCheckout() {
        checkoutStep1 = cartPage.checkout();
        Assertions.assertThat(checkoutStep1.isCheckoutStep1Displayed()).isTrue();
    }

    @When("the user fills in checkout information with first name {string}, last name {string}, postal code {string}")
    public void theUserFillsInCheckoutInformation(String firstName, String lastName, String postalCode) {
        checkoutStep2 = checkoutStep1.completeCheckoutInfo(firstName, lastName, postalCode);
    }

    @Then("the user should see the order overview")
    public void theUserShouldSeeTheOrderOverview() {
        Assertions.assertThat(checkoutStep2.isCheckoutStep2Displayed())
            .as("Should be on checkout overview").isTrue();
    }

    @Then("the order total should be calculated correctly")
    public void theOrderTotalShouldBeCalculatedCorrectly() {
        Assertions.assertThat(checkoutStep2.isTotalCorrect())
            .as("Order total = subtotal + tax").isTrue();
    }

    @When("the user clicks the Finish button")
    public void theUserClicksTheFinishButton() {
        checkoutCompletePage = checkoutStep2.finishOrder();
    }

    @Then("the order confirmation page should be displayed")
    public void theOrderConfirmationPageShouldBeDisplayed() {
        Assertions.assertThat(checkoutCompletePage.isOrderConfirmationDisplayed())
            .as("Order confirmation page should be shown").isTrue();
        Assertions.assertThat(checkoutCompletePage.isOrderSuccessful())
            .as("Order should show thank-you message").isTrue();
    }

    @When("the user enters checkout info without first name")
    public void theUserEntersCheckoutInfoWithoutFirstName() {
        checkoutStep1.enterCheckoutInfo("", "Doe", "12345").clickContinueExpectingError();
    }

    @Then("the checkout error {string} should be displayed")
    public void theCheckoutErrorShouldBeDisplayed(String expectedError) {
        Assertions.assertThat(checkoutStep1.isErrorDisplayed())
            .as("Checkout error should be displayed").isTrue();
        Assertions.assertThat(checkoutStep1.getErrorMessage())
            .as("Error message text").contains(expectedError);
    }
}
