package com.automation.utils;

import com.automation.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    private static final Logger log = LogManager.getLogger(WaitUtils.class);
    private final WebDriver driver;
    private final WebDriverWait explicitWait;
    private final FluentWait<WebDriver> fluentWait;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
        this.explicitWait = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.EXPLICIT_WAIT));
        this.fluentWait = new FluentWait<>(driver)
            .withTimeout(Duration.ofSeconds(FrameworkConstants.FLUENT_WAIT_TIMEOUT))
            .pollingEvery(Duration.ofMillis(FrameworkConstants.FLUENT_WAIT_POLLING))
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(ElementNotInteractableException.class);
    }

    // ─── Visibility ──────────────────────────────────────────────────────────

    public WebElement waitForVisibility(WebElement element) {
        log.debug("Waiting for element visibility: {}", element);
        return explicitWait.until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement waitForVisibility(By locator) {
        return explicitWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForVisibility(WebElement element, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
            .until(ExpectedConditions.visibilityOf(element));
    }

    // ─── Clickability ────────────────────────────────────────────────────────

    public WebElement waitForElementToBeClickable(WebElement element) {
        return explicitWait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public WebElement waitForElementToBeClickable(By locator) {
        return explicitWait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // ─── Presence ────────────────────────────────────────────────────────────

    public WebElement waitForPresence(By locator) {
        return explicitWait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    // ─── Invisibility ────────────────────────────────────────────────────────

    public boolean waitForInvisibility(WebElement element) {
        return explicitWait.until(ExpectedConditions.invisibilityOf(element));
    }

    public boolean waitForInvisibility(By locator) {
        return explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // ─── Text ────────────────────────────────────────────────────────────────

    public boolean waitForTextToBe(WebElement element, String text) {
        return explicitWait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public boolean waitForTextToContain(By locator, String text) {
        return explicitWait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    // ─── URL ─────────────────────────────────────────────────────────────────

    public boolean waitForUrlContains(String urlFragment) {
        return explicitWait.until(ExpectedConditions.urlContains(urlFragment));
    }

    public boolean waitForUrlToBe(String url) {
        return explicitWait.until(ExpectedConditions.urlToBe(url));
    }

    // ─── Title ───────────────────────────────────────────────────────────────

    public boolean waitForTitleContains(String title) {
        return explicitWait.until(ExpectedConditions.titleContains(title));
    }

    // ─── Page Load ───────────────────────────────────────────────────────────

    public void waitForPageLoad() {
        explicitWait.until((ExpectedCondition<Boolean>) wd ->
            ((JavascriptExecutor) wd)
                .executeScript("return document.readyState")
                .equals("complete")
        );
        log.debug("Page fully loaded");
    }

    // ─── Alert ───────────────────────────────────────────────────────────────

    public Alert waitForAlert() {
        return explicitWait.until(ExpectedConditions.alertIsPresent());
    }

    // ─── Fluent Wait ─────────────────────────────────────────────────────────

    public WebElement fluentWaitForElement(By locator) {
        return fluentWait.until(d -> d.findElement(locator));
    }

    public <T> T fluentWaitUntil(ExpectedCondition<T> condition) {
        return fluentWait.until(condition);
    }

    // ─── Custom Condition ────────────────────────────────────────────────────

    public void waitForElementCount(By locator, int expectedCount) {
        explicitWait.until(d -> d.findElements(locator).size() >= expectedCount);
    }

    public void waitForAttributeContains(WebElement element, String attribute, String value) {
        explicitWait.until(ExpectedConditions.attributeContains(element, attribute, value));
    }

    public void waitForAttributeToBe(WebElement element, String attribute, String value) {
        explicitWait.until(ExpectedConditions.attributeToBe(element, attribute, value));
    }

    public void waitForStaleness(WebElement element) {
        explicitWait.until(ExpectedConditions.stalenessOf(element));
    }

    // ─── Hard Wait (use sparingly) ───────────────────────────────────────────

    public void hardWait(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
