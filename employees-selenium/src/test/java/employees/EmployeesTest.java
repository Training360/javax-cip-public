package employees;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeesTest {

    String url;

    WebDriver driver;

    @BeforeAll
    static void setupClass() {
        String driverClass =
                Optional.ofNullable(System.getenv("SELENIUM_DRIVER")).orElse("ChromeDriver");
        if (driverClass.equals("ChromeDriver")) {
            WebDriverManager.chromedriver().setup();
        }
    }

    @BeforeEach
    void setupTest() throws MalformedURLException {
        String driverClass =
                Optional.ofNullable(System.getenv("SELENIUM_DRIVER")).orElse("ChromeDriver");

        if (driverClass.equals("ChromeDriver")) {
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
            System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
            driver = new ChromeDriver(options);
        }
        else if (driverClass.equals("RemoteWebDriver")) {
            String hubUrl = Optional.ofNullable(System.getenv("SELENIUM_HUB_URL")).orElse("http://localhost:4444");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-dev-shm-usage");
            driver = new RemoteWebDriver(new URL(hubUrl), options);
        }
        else {
            throw new IllegalArgumentException("Illegal driver: " + driverClass);
        }

        url = Optional.ofNullable(System.getenv("SELENIUM_SUT_URL")).orElse("http://localhost:8080");
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testCreate() {
        driver.get(url);
        driver.findElement(By.id("name-input")).sendKeys("John Doe");
        driver.findElement(By.id("create-button")).click();
        String message = driver.findElement(By.id("message-div")).getText();
        assertEquals("Employee has been created: John Doe", message);
    }

}
