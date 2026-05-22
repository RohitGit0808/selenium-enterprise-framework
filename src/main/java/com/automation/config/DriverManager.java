package com.automation.config;

import com.automation.constants.FrameworkConstants;
import com.automation.enums.BrowserType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public final class DriverManager {

    private static final Logger log = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {}

    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            throw new IllegalStateException("WebDriver not initialized. Call initDriver() first.");
        }
        return driverThreadLocal.get();
    }

    public static void initDriver() {
        ConfigManager config = ConfigManager.getInstance();
        BrowserType browser = config.getBrowser();
        boolean headless = config.isHeadless();
        boolean remote = config.isRemote();

        log.info("Initializing WebDriver — browser: {}, headless: {}, remote: {}", browser, headless, remote);

        WebDriver driver;

        if (remote) {
            driver = createRemoteDriver(browser, headless, config.getGridUrl());
        } else {
            driver = createLocalDriver(browser, headless);
        }

        configureDriver(driver);
        driverThreadLocal.set(driver);
        log.info("WebDriver initialized successfully for thread: {}", Thread.currentThread().getId());
    }

    public static void initDriver(BrowserType browser) {
        boolean headless = ConfigManager.getInstance().isHeadless();
        WebDriver driver = createLocalDriver(browser, headless);
        configureDriver(driver);
        driverThreadLocal.set(driver);
    }

    private static WebDriver createLocalDriver(BrowserType browser, boolean headless) {
        return switch (browser) {
            case CHROME, CHROME_HEADLESS -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = getChromeOptions(headless || browser == BrowserType.CHROME_HEADLESS);
                yield new ChromeDriver(options);
            }
            case FIREFOX, FIREFOX_HEADLESS -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = getFirefoxOptions(headless || browser == BrowserType.FIREFOX_HEADLESS);
                yield new FirefoxDriver(options);
            }
            case EDGE, EDGE_HEADLESS -> {
                WebDriverManager.edgedriver().setup();
                EdgeOptions options = getEdgeOptions(headless || browser == BrowserType.EDGE_HEADLESS);
                yield new EdgeDriver(options);
            }
            default -> {
                log.warn("Browser {} not supported for local, defaulting to Chrome", browser);
                WebDriverManager.chromedriver().setup();
                yield new ChromeDriver(getChromeOptions(headless));
            }
        };
    }

    private static WebDriver createRemoteDriver(BrowserType browser, boolean headless, String gridUrl) {
        try {
            return switch (browser) {
                case REMOTE_CHROME, CHROME -> {
                    ChromeOptions options = getChromeOptions(headless);
                    yield new RemoteWebDriver(new URL(gridUrl), options);
                }
                case REMOTE_FIREFOX, FIREFOX -> {
                    FirefoxOptions options = getFirefoxOptions(headless);
                    yield new RemoteWebDriver(new URL(gridUrl), options);
                }
                default -> {
                    ChromeOptions options = getChromeOptions(headless);
                    yield new RemoteWebDriver(new URL(gridUrl), options);
                }
            };
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Selenium Grid URL: " + gridUrl, e);
        }
    }

    private static ChromeOptions getChromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments(
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--disable-gpu",
            "--window-size=1920,1080",
            "--disable-notifications",
            "--disable-popup-blocking",
            "--disable-extensions",
            "--disable-blink-features=AutomationControlled",
            "--remote-allow-origins=*"
        );
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        return options;
    }

    private static FirefoxOptions getFirefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        options.addArguments("--width=1920", "--height=1080");
        options.addPreference("dom.webnotifications.enabled", false);
        return options;
    }

    private static EdgeOptions getEdgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments(
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--window-size=1920,1080",
            "--disable-notifications"
        );
        return options;
    }

    private static void configureDriver(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(FrameworkConstants.IMPLICIT_WAIT));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(FrameworkConstants.PAGE_LOAD_TIMEOUT));
        driver.manage().window().maximize();
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            log.info("WebDriver quit for thread: {}", Thread.currentThread().getId());
        }
    }

    public static boolean isDriverActive() {
        return driverThreadLocal.get() != null;
    }
}
