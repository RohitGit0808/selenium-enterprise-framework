package com.automation.pages;

import com.automation.config.DriverManager;
import com.automation.utils.WaitUtils;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BasePage {

    protected final Logger log = LogManager.getLogger(getClass());
    protected WebDriver driver;
    protected WaitUtils wait;
    protected Actions actions;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WaitUtils(driver);
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    // ─── Navigation ──────────────────────────────────────────────────────────

    @Step("Navigate to URL: {url}")
    public void navigateTo(String url) {
        log.info("Navigating to: {}", url);
        driver.get(url);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    // ─── Element Interaction ─────────────────────────────────────────────────

    @Step("Click on element")
    protected void click(WebElement element) {
        wait.waitForElementToBeClickable(element);
        log.debug("Clicking element: {}", element);
        element.click();
    }

    @Step("Click on element by locator")
    protected void click(By locator) {
        wait.waitForElementToBeClickable(locator);
        driver.findElement(locator).click();
    }

    @Step("Type '{text}' into element")
    protected void type(WebElement element, String text) {
        wait.waitForVisibility(element);
        element.clear();
        element.sendKeys(text);
        log.debug("Typed '{}' into element", text);
    }

    @Step("Type '{text}' into element by locator")
    protected void type(By locator, String text) {
        WebElement element = wait.waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    @Step("Clear and type '{text}' into element")
    protected void clearAndType(WebElement element, String text) {
        wait.waitForVisibility(element);
        element.clear();
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(text);
    }

    protected String getText(WebElement element) {
        wait.waitForVisibility(element);
        return element.getText().trim();
    }

    protected String getAttribute(WebElement element, String attribute) {
        wait.waitForVisibility(element);
        return element.getAttribute(attribute);
    }

    // ─── Select / Dropdown ───────────────────────────────────────────────────

    @Step("Select '{visibleText}' from dropdown")
    protected void selectByVisibleText(WebElement dropdown, String visibleText) {
        wait.waitForVisibility(dropdown);
        new Select(dropdown).selectByVisibleText(visibleText);
    }

    protected void selectByValue(WebElement dropdown, String value) {
        wait.waitForVisibility(dropdown);
        new Select(dropdown).selectByValue(value);
    }

    protected void selectByIndex(WebElement dropdown, int index) {
        wait.waitForVisibility(dropdown);
        new Select(dropdown).selectByIndex(index);
    }

    // ─── Visibility Checks ───────────────────────────────────────────────────

    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected boolean isEnabled(WebElement element) {
        try {
            return element.isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected boolean isPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    // ─── Scrolling ───────────────────────────────────────────────────────────

    @Step("Scroll element into view")
    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    protected void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
    }

    protected void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    // ─── JavaScript Executor ─────────────────────────────────────────────────

    protected void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected Object executeJS(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    // ─── Hover & Drag ────────────────────────────────────────────────────────

    @Step("Hover over element")
    protected void hoverOver(WebElement element) {
        wait.waitForVisibility(element);
        actions.moveToElement(element).perform();
    }

    protected void dragAndDrop(WebElement source, WebElement target) {
        actions.dragAndDrop(source, target).perform();
    }

    // ─── List Operations ─────────────────────────────────────────────────────

    protected List<String> getTextsFromElements(List<WebElement> elements) {
        return elements.stream()
            .map(WebElement::getText)
            .map(String::trim)
            .collect(Collectors.toList());
    }

    protected List<WebElement> getElements(By locator) {
        return driver.findElements(locator);
    }

    // ─── Wait Helpers ────────────────────────────────────────────────────────

    protected void waitForPageLoad() {
        wait.waitForPageLoad();
    }

    protected void waitForUrlContains(String urlFragment) {
        wait.waitForUrlContains(urlFragment);
    }

    protected void waitForTitleContains(String title) {
        wait.waitForTitleContains(title);
    }

    // ─── Frame Handling ──────────────────────────────────────────────────────

    protected void switchToFrame(WebElement frame) {
        driver.switchTo().frame(frame);
    }

    protected void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    // ─── Alert Handling ──────────────────────────────────────────────────────

    protected void acceptAlert() {
        wait.waitForAlert();
        driver.switchTo().alert().accept();
    }

    protected String getAlertText() {
        wait.waitForAlert();
        return driver.switchTo().alert().getText();
    }

    // ─── Window Handling ─────────────────────────────────────────────────────

    protected void switchToNewWindow(String originalHandle) {
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalHandle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    protected String getWindowHandle() {
        return driver.getWindowHandle();
    }
}
