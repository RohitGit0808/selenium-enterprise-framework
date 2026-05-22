package com.automation.pages.saucedemo;

import com.automation.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CheckoutStepTwoPage extends BasePage {

    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(css = ".cart_item")
    private List<WebElement> orderItems;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> itemNames;

    @FindBy(css = ".summary_subtotal_label")
    private WebElement subtotalLabel;

    @FindBy(css = ".summary_tax_label")
    private WebElement taxLabel;

    @FindBy(css = ".summary_total_label")
    private WebElement totalLabel;

    @FindBy(css = ".summary_info_label.summary_payment_info_value")
    private WebElement paymentInfo;

    @FindBy(css = ".summary_info_label.summary_shipping_info_value")
    private WebElement shippingInfo;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    public boolean isCheckoutStep2Displayed() {
        return "Checkout: Overview".equals(getText(pageTitle));
    }

    public String getSubtotal() {
        return getText(subtotalLabel);
    }

    public String getTax() {
        return getText(taxLabel);
    }

    public String getTotal() {
        return getText(totalLabel);
    }

    public double getTotalAmount() {
        String total = getText(totalLabel).replace("Total: $", "");
        return Double.parseDouble(total);
    }

    public double getSubtotalAmount() {
        String subtotal = getText(subtotalLabel).replace("Item total: $", "");
        return Double.parseDouble(subtotal);
    }

    public double getTaxAmount() {
        String tax = getText(taxLabel).replace("Tax: $", "");
        return Double.parseDouble(tax);
    }

    public int getOrderItemCount() {
        return orderItems.size();
    }

    public List<String> getOrderItemNames() {
        return getTextsFromElements(itemNames);
    }

    @Step("Finish order")
    public CheckoutCompletePage finishOrder() {
        log.info("Finishing order");
        click(finishButton);
        return new CheckoutCompletePage();
    }

    @Step("Cancel order overview")
    public InventoryPage cancelOrder() {
        click(cancelButton);
        return new InventoryPage();
    }

    public boolean isTotalCorrect() {
        double expectedTotal = Math.round((getSubtotalAmount() + getTaxAmount()) * 100.0) / 100.0;
        return expectedTotal == getTotalAmount();
    }
}
