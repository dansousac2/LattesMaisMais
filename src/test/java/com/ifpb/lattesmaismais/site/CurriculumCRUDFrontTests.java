package com.ifpb.lattesmaismais.site;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CurriculumCRUDFrontTests {

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
        labelPassword.sendKeys("123456");

        Thread.sleep(2000);

        WebElement buttonLogin = getElementByXPath("//*[@id=\"root\"]/div/div/div[2]/button");
        clickElement(buttonLogin);

//        assertEquals("http://localhost:3000/home/", driver.getCurrentUrl());
    }

    @Test
    @Order(2)
    public void importCurriculum() throws InterruptedException {
        driver.get("http://localhost:3000/home/");

        WebElement buttonImport = getElementByXPath("//*[@id=\"root\"]/div/div/div[3]/button");
        clickElement(buttonImport);

        Thread.sleep(2000);

//        driver.switchTo()
//                .activeElement().sendKeys(System.getProperty("user.dir") + "\\src\\test\\java\\com\\ifpb\\lattesmaismais\\util\\teste.xml");

        WebElement selectXML = getElementByXPath("//*[@id=\"root\"]/div/div/div[3]/input");
        selectXML.sendKeys(System.getProperty("user.dir") + "\\src\\test\\java\\com\\ifpb\\lattesmaismais\\util\\teste.xml");

        Thread.sleep(2000);
    }

    @Test
    @Order(3)
    public void listEntries() {
        driver.get("http://localhost:3000/updateVersions/2");


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
