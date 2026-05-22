package com.automation.utils;

import com.automation.config.DriverManager;
import com.automation.constants.FrameworkConstants;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class ScreenshotUtils {

    private static final Logger log = LogManager.getLogger(ScreenshotUtils.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {}

    public static String captureScreenshot(String testName) {
        WebDriver driver = DriverManager.getDriver();
        try {
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String fileName = testName + "_" + timestamp + ".png";

            Path screenshotDir = Paths.get(FrameworkConstants.SCREENSHOT_DIR);
            Files.createDirectories(screenshotDir);

            Path filePath = screenshotDir.resolve(fileName);
            Files.write(filePath, screenshotBytes);

            log.info("Screenshot saved: {}", filePath);
            return filePath.toString();
        } catch (IOException | WebDriverException e) {
            log.error("Failed to capture screenshot: {}", e.getMessage());
            return null;
        }
    }

    public static void captureAndAttachToAllure(String screenshotName) {
        WebDriver driver = DriverManager.getDriver();
        try {
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(screenshotName, new ByteArrayInputStream(screenshotBytes));
            log.info("Screenshot attached to Allure report: {}", screenshotName);
        } catch (WebDriverException e) {
            log.error("Failed to capture screenshot for Allure: {}", e.getMessage());
        }
    }

    public static String captureFullPageScreenshot(String testName) {
        WebDriver driver = DriverManager.getDriver();
        try {
            Screenshot screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(100))
                .takeScreenshot(driver);

            String timestamp = LocalDateTime.now().format(FORMATTER);
            String fileName = testName + "_fullpage_" + timestamp + ".png";

            Path screenshotDir = Paths.get(FrameworkConstants.SCREENSHOT_DIR);
            Files.createDirectories(screenshotDir);

            File outputFile = screenshotDir.resolve(fileName).toFile();
            ImageIO.write(screenshot.getImage(), "PNG", outputFile);

            log.info("Full-page screenshot saved: {}", outputFile.getAbsolutePath());
            return outputFile.getAbsolutePath();
        } catch (IOException | WebDriverException e) {
            log.error("Failed to capture full-page screenshot: {}", e.getMessage());
            return null;
        }
    }

    public static String captureElementScreenshot(WebElement element, String testName) {
        WebDriver driver = DriverManager.getDriver();
        try {
            byte[] elementBytes = element.getScreenshotAs(OutputType.BYTES);
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String fileName = testName + "_element_" + timestamp + ".png";

            Path screenshotDir = Paths.get(FrameworkConstants.SCREENSHOT_DIR);
            Files.createDirectories(screenshotDir);

            Path filePath = screenshotDir.resolve(fileName);
            Files.write(filePath, elementBytes);

            return filePath.toString();
        } catch (IOException | WebDriverException e) {
            log.error("Failed to capture element screenshot: {}", e.getMessage());
            return null;
        }
    }

    public static String captureAsBase64() {
        WebDriver driver = DriverManager.getDriver();
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (WebDriverException e) {
            log.error("Failed to capture Base64 screenshot: {}", e.getMessage());
            return null;
        }
    }

    public static void attachPageSourceToAllure() {
        WebDriver driver = DriverManager.getDriver();
        try {
            Allure.addAttachment("Page Source", "text/html", driver.getPageSource(), ".html");
        } catch (Exception e) {
            log.error("Failed to attach page source: {}", e.getMessage());
        }
    }
}
