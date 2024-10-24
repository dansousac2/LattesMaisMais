package com.ifpb.lattesmaismais.site;

import com.ifpb.lattesmaismais.business.FileConverterService;
import com.ifpb.lattesmaismais.business.HashService;
import com.ifpb.lattesmaismais.presentation.exception.HashException;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
class ExportCurriculumFrontTests {

    private static WebDriver driver;

    private static JavascriptExecutor jse;

    private static String userFolder;

    private static HashService hashService = new HashService();

    private static final FileConverterService fileConverter = new FileConverterService();

    @BeforeAll
    static void setUp() throws HashException {
        System.out.println(System.getProperty("user.dir"));
        System.setProperty("webdriver.chrome.driver",
                System.getProperty("user.dir") + "\\src\\test\\files\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        jse = (JavascriptExecutor)driver;

        String hashUserId = hashService.hashingSHA256("1");
        userFolder = System.getProperty("user.dir") + "\\src\\main\\resources\\server\\dr\\" + hashUserId;
    }

    @Test
    void testExportCurriculum() throws InterruptedException {
        login("teste@gmail.com", "12345678");

        Thread.sleep(2000);

        // Consultando versão do currículo atualizado:
        driver.get("http://localhost:3000/updateversions/1");
        Thread.sleep(2000);
        String versionText = getElementById("versionCurriculum").getText();
        Thread.sleep(2000);

        driver.get("http://localhost:3000/exportpdf");

        Thread.sleep(2000);

        // Escolhendo versão de currículo para exportar:
        clickElement(getElementByXPath("//*[@id=\"root\"]/div/div/div[2]/div/table/tbody/tr[3]/td[3]/input"));

        Thread.sleep(2000);

        // Clicando em exportar pdf:
        clickElement(getElementByXPath("//*[@id=\"root\"]/div/div/div[2]/div/div/button"));

        String timeOfExportation = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss"));
        Thread.sleep(10000);

        // Verificando que pdf foi gerado e salvo na pasta correspondente:
        try {
            byte[] pdfData = fileConverter.readFile(userFolder + "\\Curriculum.pdf");

            // Verificando que arquivo possui conteúdo:
            assertNotNull(pdfData);

            // Comparando conteúdo:
            PdfReader reader = new PdfReader(userFolder + "\\Curriculum.pdf");
            String pdfContent = PdfTextExtractor.getTextFromPage(reader, 1);


            assertTrue(pdfContent.contains("Teste"));
            assertTrue(pdfContent.contains(versionText));
            assertTrue(pdfContent.contains(timeOfExportation));
        } catch (Exception e) {
            fail();
        }
    }

    @AfterEach
    void beforeEach() throws InterruptedException {
        Thread.sleep(2000);
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

    private void login(String user, String password) throws InterruptedException {
        driver.get("http://localhost:3000/");

        WebElement labelEmail = getElementById("lab04");
        labelEmail.sendKeys(user);

        Thread.sleep(2000);

        WebElement labelPassword = getElementById("lab05");
        labelPassword.sendKeys(password);

        Thread.sleep(2000);

        WebElement buttonLogin = getElementByXPath("//*[@id=\"root\"]/div/div/div[2]/button");
        clickElement(buttonLogin);

        Thread.sleep(2000);
    }
}
