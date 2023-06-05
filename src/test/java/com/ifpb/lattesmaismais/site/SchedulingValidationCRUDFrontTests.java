package com.ifpb.lattesmaismais.site;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SchedulingValidationCRUDFrontTests {

    private static WebDriver driver;

    private static JavascriptExecutor jse;

    @BeforeAll
    static void setUp() {
        System.out.println(System.getProperty("user.dir"));
        System.setProperty("webdriver.chrome.driver",
                System.getProperty("user.dir") + "\\src\\test\\files\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        jse = (JavascriptExecutor)driver;
    }

    @AfterEach
    void beforeEach() throws InterruptedException {
        Thread.sleep(2000);
    }

    @Test
    @Order(1)
    public void login() throws InterruptedException {
        driver.get("http://localhost:3000/");

        WebElement labelEmail = getElementById("lab04");
        labelEmail.sendKeys("teste@gmail.com");

        Thread.sleep(2000);

        WebElement labelPassword = getElementById("lab05");
        labelPassword.sendKeys("12345678");

        Thread.sleep(2000);

        WebElement buttonLogin = getElementByXPath("//*[@id=\"root\"]/div/div/div[2]/button");
        clickElement(buttonLogin);

        Thread.sleep(2000);

        Alert alt = driver.switchTo().alert();
        alt.accept();

        assertEquals("http://localhost:3000/home", driver.getCurrentUrl());
    }

    @Test
    @Order(2)
    public void solicitingValidation() {
        driver.get("http://localhost:3000/shedulingvalidation");

        // Selecionando versão:
        WebElement version = getElementByXPath("//*[@id=\"curricTable01\"]/tbody/tr[2]/td[3]/input");
        clickElement(version);

        // Selecionando validador:


        // Selecionando data:
        String dateString = LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString();
        WebElement date = getElementById("date01");
        date.sendKeys(dateString);

        // Selecionando horário:
        WebElement time = getElementById("hour01");
        time.sendKeys("15:30");

        // Salvando solicitação de validação:
        WebElement soliciteValidation = getElementByXPath("//*[@id=\"root\"]/div/div/div[2]/div/div[1]/button");
        soliciteValidation.click();
    }


    @AfterAll
    static void tearDown() throws InterruptedException {
        Thread.sleep(2000);
        driver.quit();
    }

    private WebElement getElementById(String id) {
        return driver.findElement(By.id(id));
    }

    private WebElement getElementByClass(String className) {
        return driver.findElement(By.className(className));
    }

    private WebElement getElementByXPath(String xPath) {
        return driver.findElement(By.xpath(xPath));
    }

    private WebElement getElementByTagName(String tag) {
        return driver.findElement(By.tagName(tag));
    }

    private void clickElement(WebElement we) {
        try {
            we.click();
        } catch (Exception e) {
            jse.executeScript("arguments[0].click()", we);
        }
    }
}
