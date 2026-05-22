package com.automation.config;

import com.automation.constants.FrameworkConstants;
import com.automation.enums.BrowserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigManager {

    private static final Logger log = LogManager.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private final Properties properties;

    private ConfigManager() {
        properties = new Properties();
        loadProperties();
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadProperties() {
        try (FileInputStream fis = new FileInputStream(FrameworkConstants.CONFIG_FILE_PATH)) {
            properties.load(fis);
            log.info("Configuration loaded from: {}", FrameworkConstants.CONFIG_FILE_PATH);
        } catch (IOException e) {
            log.error("Failed to load configuration file: {}", e.getMessage());
            throw new RuntimeException("Cannot load config.properties", e);
        }
    }

    public String get(String key) {
        String value = System.getProperty(key, properties.getProperty(key));
        if (value == null || value.isBlank()) {
            log.warn("Property '{}' not found in config", key);
        }
        return value;
    }

    public String getBaseUrl() {
        return get("base.url");
    }

    public BrowserType getBrowser() {
        String browser = System.getProperty("browser", get("browser")).toUpperCase();
        try {
            return BrowserType.valueOf(browser);
        } catch (IllegalArgumentException e) {
            log.warn("Unknown browser '{}', defaulting to CHROME", browser);
            return BrowserType.CHROME;
        }
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty("headless", get("headless")));
    }

    public boolean isRemote() {
        return Boolean.parseBoolean(System.getProperty("remote", get("remote")));
    }

    public String getGridUrl() {
        return get("grid.url");
    }

    public String getEnvironment() {
        return System.getProperty("env", get("environment"));
    }

    public int getImplicitWait() {
        try {
            return Integer.parseInt(get("implicit.wait"));
        } catch (NumberFormatException e) {
            return FrameworkConstants.IMPLICIT_WAIT;
        }
    }

    public int getExplicitWait() {
        try {
            return Integer.parseInt(get("explicit.wait"));
        } catch (NumberFormatException e) {
            return FrameworkConstants.EXPLICIT_WAIT;
        }
    }
}
