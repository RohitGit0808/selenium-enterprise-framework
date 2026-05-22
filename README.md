# Selenium Enterprise Automation Framework

> **Advanced Java + Selenium 4 test automation framework** built for enterprise-scale quality engineering. Featuring BDD with Cucumber, parallel cross-browser execution, dual reporting (Allure + ExtentReports), data-driven testing, Selenium Grid 4 support, and full CI/CD pipeline.

[![CI Pipeline](https://github.com/RohitGit0808/selenium-enterprise-framework/actions/workflows/ci.yml/badge.svg)](https://github.com/RohitGit0808/selenium-enterprise-framework/actions)
[![Nightly Regression](https://github.com/RohitGit0808/selenium-enterprise-framework/actions/workflows/nightly.yml/badge.svg)](https://github.com/RohitGit0808/selenium-enterprise-framework/actions)
[![Java Version](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.18-green)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.9-blue)](https://testng.org/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7.15-brightgreen)](https://cucumber.io/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Project Structure](#-project-structure)
- [Quick Start](#-quick-start)
- [Running Tests](#-running-tests)
- [Configuration](#-configuration)
- [Reporting](#-reporting)
- [Docker & Selenium Grid](#-docker--selenium-grid)
- [CI/CD Pipeline](#-cicd-pipeline)
- [Design Patterns](#-design-patterns)
- [Test Coverage](#-test-coverage)

---

## Features

| Feature | Implementation |
|---|---|
| **Page Object Model** | Fluent Page Objects with PageFactory |
| **BDD / Gherkin** | Cucumber 7 with parallel scenario execution |
| **Cross-Browser** | Chrome, Firefox, Edge (local + remote) |
| **Parallel Execution** | TestNG thread-safe `ThreadLocal<WebDriver>` |
| **Allure Reports** | Rich HTML reports with screenshots, steps, and history |
| **ExtentReports** | Dark-theme HTML reports with Base64 screenshots on failure |
| **Data-Driven** | JSON files + Apache POI Excel reader |
| **Fake Test Data** | JavaFaker-powered data factory |
| **Retry Mechanism** | Configurable `IRetryAnalyzer` on flaky tests |
| **Smart Waits** | Explicit, fluent, and custom `ExpectedConditions` |
| **Screenshot Utils** | On-failure capture + full-page AShot screenshots |
| **Selenium Grid 4** | Docker Compose with Hub + 3 browser nodes |
| **CI/CD** | GitHub Actions (push/PR smoke + nightly regression) |
| **Allure on GitHub Pages** | Auto-deployed test history after every merge |
| **Custom Annotations** | `@FrameworkAnnotation` for author/category metadata |
| **Log4j2** | Structured rolling-file + console logging |

---

## Tech Stack

```
Core
├── Java 17               — Modern language features (switch expressions, records)
├── Selenium 4.18         — WebDriver, CDP, BiDi support
├── WebDriverManager 5.7  — Automatic driver binary management
└── Maven 3.9             — Build and dependency management

Test Frameworks
├── TestNG 7.9            — Suites, parallel execution, data providers
└── Cucumber 7.15         — BDD with Gherkin, parallel scenarios

Reporting
├── Allure 2.25           — Interactive reports, GitHub Pages deployment
└── ExtentReports 5.1     — Dark-theme HTML report with Base64 screenshots

Utilities
├── Jackson 2.16          — JSON deserialization for test data
├── Apache POI 5.2        — Excel (.xlsx) data-driven test support
├── JavaFaker 1.0         — Realistic fake data generation
├── RestAssured 5.4       — API layer testing (hybrid tests)
├── AssertJ 3.25          — Fluent, readable assertions
├── Lombok 1.18           — Boilerplate reduction (@Builder, @Data)
└── Log4j2 2.22           — Structured application logging

Infrastructure
├── Docker + Compose      — Selenium Grid 4 (Hub + Chrome/Firefox/Edge)
└── GitHub Actions        — CI/CD with matrix browser strategy
```

---

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Test Execution Layer                      │
│   TestNG Suites ──────── Cucumber Runners                   │
│   (smoke/regression/parallel/bdd)                           │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│                    Test Classes Layer                        │
│   LoginTest ── InventoryTest ── CheckoutE2ETest             │
│   extends BaseTest                                          │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│                  Page Object Model Layer                     │
│   BasePage                                                  │
│   ├── LoginPage                                             │
│   ├── InventoryPage                                         │
│   ├── CartPage                                              │
│   ├── CheckoutStepOnePage                                   │
│   ├── CheckoutStepTwoPage                                   │
│   └── CheckoutCompletePage                                  │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│                   Framework Core Layer                      │
│   DriverManager (ThreadLocal) ── ConfigManager (Singleton)  │
│   WaitUtils ── ScreenshotUtils ── JsonUtils ── ExcelUtils   │
│   TestDataFactory ── ExtentReportManager                    │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│                  Infrastructure Layer                        │
│   Selenium Grid 4 (Docker) ── Allure ── GitHub Actions      │
└─────────────────────────────────────────────────────────────┘
```

---

## Project Structure

```
selenium-enterprise-framework/
├── .github/
│   └── workflows/
│       ├── ci.yml                        # CI: code quality + smoke + regression + BDD
│       └── nightly.yml                   # Nightly: full cross-browser regression
│
├── src/
│   ├── main/java/com/automation/
│   │   ├── annotations/
│   │   │   └── FrameworkAnnotation.java  # Custom @FrameworkAnnotation (author/category)
│   │   ├── config/
│   │   │   ├── ConfigManager.java        # Singleton config loader from properties
│   │   │   └── DriverManager.java        # ThreadLocal WebDriver factory
│   │   ├── constants/
│   │   │   └── FrameworkConstants.java   # All timeouts, URLs, user credentials
│   │   ├── enums/
│   │   │   └── BrowserType.java          # CHROME, FIREFOX, EDGE, HEADLESS, REMOTE
│   │   ├── listeners/
│   │   │   └── TestListener.java         # TestNG + Allure integration listener
│   │   ├── pages/
│   │   │   ├── BasePage.java             # Fluent base with wait, JS, scroll utilities
│   │   │   └── saucedemo/
│   │   │       ├── LoginPage.java
│   │   │       ├── InventoryPage.java
│   │   │       ├── ProductDetailPage.java
│   │   │       ├── CartPage.java
│   │   │       ├── CheckoutStepOnePage.java
│   │   │       ├── CheckoutStepTwoPage.java
│   │   │       └── CheckoutCompletePage.java
│   │   └── utils/
│   │       ├── WaitUtils.java            # Explicit, fluent, custom waits
│   │       ├── ScreenshotUtils.java      # Capture, full-page, Allure attachment
│   │       ├── JsonUtils.java            # Jackson-based JSON reader
│   │       ├── ExcelUtils.java           # Apache POI Excel reader
│   │       ├── RetryAnalyzer.java        # IRetryAnalyzer implementation
│   │       ├── ExtentReportManager.java  # ThreadLocal ExtentTest manager
│   │       └── TestDataFactory.java      # JavaFaker data builders
│   │
│   └── test/
│       ├── java/com/automation/
│       │   ├── base/
│       │   │   └── BaseTest.java         # @Before/@After + helper login methods
│       │   ├── tests/
│       │   │   ├── LoginTest.java        # 7 login tests (data-driven)
│       │   │   ├── InventoryTest.java    # 8 inventory + cart tests
│       │   │   └── CheckoutE2ETest.java  # 8 E2E checkout tests (data-driven)
│       │   ├── stepdefs/
│       │   │   ├── Hooks.java            # Cucumber @Before/@After with screenshot
│       │   │   ├── LoginSteps.java
│       │   │   └── CheckoutSteps.java
│       │   └── runners/
│       │       ├── CucumberTestRunner.java   # Full parallel Cucumber runner
│       │       └── SmokeTestRunner.java      # @smoke tag only
│       │
│       └── resources/
│           ├── features/
│           │   ├── login.feature         # 7 BDD scenarios + Scenario Outline
│           │   └── checkout.feature      # 3 BDD scenarios (E2E + negative)
│           ├── testdata/
│           │   └── testdata.json         # Users, products, checkout data
│           ├── config/
│           │   └── config.properties     # All configuration
│           └── log4j2.xml                # Structured rolling-file logging
│
├── testng.xml                            # Default full suite
├── testng-smoke.xml                      # Smoke tests only
├── testng-regression.xml                 # Regression (parallel methods)
├── testng-parallel.xml                   # 3 browsers in parallel
├── testng-bdd.xml                        # Cucumber BDD suite
├── docker-compose.yml                    # Selenium Grid 4 + Allure server
├── Dockerfile                            # Multi-stage Maven + JRE image
├── pom.xml                               # Maven with 5 run profiles
└── README.md
```

---

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.9+
- Chrome / Firefox / Edge browser
- Docker Desktop (for Grid execution)

### Installation

```bash
git clone https://github.com/RohitGit0808/selenium-enterprise-framework.git
cd selenium-enterprise-framework
mvn dependency:resolve
```

---

## Running Tests

### Smoke Suite (default: Chrome)
```bash
mvn test -Psmoce
```

### Smoke Suite on specific browser
```bash
mvn test -Psmoce -Dbrowser=firefox
```

### Smoke Suite headless
```bash
mvn test -Psmoce -Dbrowser=chrome_headless -Dheadless=true
```

### Full Regression Suite
```bash
mvn test -Pregression
```

### Regression with parallel methods (4 threads)
```bash
mvn test -Pregression -Dbrowser=chrome
```

### Cross-browser parallel (Chrome + Firefox + Edge simultaneously)
```bash
mvn test -Pparallel
```

### BDD / Cucumber Suite
```bash
mvn test -Pbdd
```

### BDD with specific tags
```bash
mvn test -Pbdd -Dcucumber.filter.tags="@smoke and @critical"
```

### Against Selenium Grid (remote)
```bash
mvn test -Psmoce -Dremote=true -Dgrid.url=http://localhost:4444/wd/hub
```

### Generate Allure Report
```bash
mvn allure:serve       # opens in browser
mvn allure:report      # saves to target/site/allure-maven-plugin/
```

---

## Configuration

All settings in [`src/test/resources/config/config.properties`](src/test/resources/config/config.properties):

| Property | Default | Description |
|---|---|---|
| `base.url` | `https://www.saucedemo.com` | AUT base URL |
| `browser` | `chrome` | Browser type |
| `headless` | `false` | Headless mode |
| `remote` | `false` | Use Selenium Grid |
| `grid.url` | `http://localhost:4444/wd/hub` | Grid endpoint |
| `implicit.wait` | `10` | Implicit wait (seconds) |
| `explicit.wait` | `20` | Explicit wait (seconds) |
| `retry.count` | `1` | Retry failed tests |

Any property can be overridden at runtime via `-D` system properties:

```bash
mvn test -Dbrowser=firefox -Dheadless=true -Dremote=true
```

---

## Reporting

### Allure Report
```bash
mvn allure:serve
```
Opens an interactive report at `http://localhost:PORT` with:
- Test execution timeline
- Pass/fail/skip trends
- Per-test steps from `@Step` annotations
- Failure screenshots attached automatically
- Test history across runs (GitHub Pages)

### ExtentReports
Opens automatically after test run: `test-output/ExtentReport.html`
- Dark theme
- Per-test Base64 screenshot embedded on failure
- Author and category from `@FrameworkAnnotation`
- System info (OS, Java version)

### Cucumber HTML Report
```
target/cucumber-reports/cucumber-html-report.html
```

---

## Docker & Selenium Grid

### Start Selenium Grid 4 (Hub + Chrome + Firefox + Edge)
```bash
docker-compose up -d selenium-hub chrome-node firefox-node edge-node
```

### Check Grid status
```bash
curl http://localhost:4444/wd/hub/status | python -m json.tool
# or open http://localhost:4444/ui in your browser
```

### Watch test execution via noVNC
- Chrome: http://localhost:7900 (password: `secret`)
- Firefox: http://localhost:7901
- Edge: http://localhost:7902

### Run tests against the Grid
```bash
mvn test -Psmoce -Dremote=true -Dbrowser=chrome -Dheadless=false
```

### Start Allure server in Docker
```bash
docker-compose up -d allure allure-ui
# UI at http://localhost:5252
```

### Run full test suite in Docker
```bash
docker-compose --profile run-tests up --build
```

### Teardown
```bash
docker-compose down -v
```

---

## CI/CD Pipeline

### GitHub Actions Workflows

#### `ci.yml` — Triggered on push/PR
```
push / PR → code-quality
               ↓
         smoke-tests (Chrome + Firefox matrix)
               ↓
     ┌─────────┴──────────┐
regression-tests      bdd-tests
     └─────────┬──────────┘
               ↓
         publish-report (Allure → GitHub Pages)
```

#### `nightly.yml` — Runs at 1 AM UTC daily
```
schedule (cron) → nightly-regression (Chrome + Firefox + Edge)
                         ↓
                    nightly-bdd (all scenarios)
```

### Setting up GitHub Pages for Allure
1. Go to repository **Settings → Pages**
2. Set source to `gh-pages` branch
3. Reports auto-deploy after every merge to `main`

---

## Design Patterns

| Pattern | Where Used |
|---|---|
| **Page Object Model** | All page classes under `pages/saucedemo/` |
| **Page Factory** | `PageFactory.initElements()` in `BasePage` |
| **Fluent Interface** | Page methods return `this` for chaining |
| **Factory Method** | `DriverManager.createLocalDriver()` / `createRemoteDriver()` |
| **Singleton** | `ConfigManager.getInstance()`, `ExtentReportManager.getInstance()` |
| **ThreadLocal** | `DriverManager.driverThreadLocal` — safe parallel execution |
| **Builder** | `TestDataFactory.CheckoutData.builder()` via Lombok |
| **Strategy** | `IRetryAnalyzer` — pluggable retry logic |
| **Observer** | `TestListener implements ITestListener` |

---

## Test Coverage

| Module | Tests | Groups |
|---|---|---|
| **Login** | 7 tests + Scenario Outline (3 users) | smoke, regression, negative |
| **Inventory** | 8 tests | smoke, regression, sanity |
| **Checkout E2E** | 8 tests + data-driven (3 users×2 products) | smoke, regression, critical, e2e |
| **BDD — Login** | 7 scenarios | smoke, regression, negative |
| **BDD — Checkout** | 3 scenarios | smoke, critical, e2e, negative |

**Total: 30+ automated test cases across 5 test suites**

---

## Key Engineering Decisions

- **ThreadLocal WebDriver** — enables true parallel test execution without race conditions
- **Headless-first CI** — all CI runs use `--headless=new` (Chromium BiDi-compatible)
- **No hard waits** — all synchronisation uses explicit/fluent waits; `Thread.sleep` only as last resort
- **Dual reporting** — Allure for interactive history, ExtentReports for quick HTML share
- **Data isolation** — each test generates unique data via JavaFaker; no shared state
- **Multi-stage Docker** — builder stage compiles; runtime stage uses JRE-only image (~200 MB smaller)

---

## License

[MIT](LICENSE) © 2024
