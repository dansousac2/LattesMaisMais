package com.ifpb.lattesmaismais.site;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import static org.junit.jupiter.api.Assertions.*;

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
        labelPassword.sendKeys("12345678");

        Thread.sleep(2000);

        WebElement buttonLogin = getElementByXPath("//*[@id=\"root\"]/div/div/div[2]/button");
        clickElement(buttonLogin);

        Thread.sleep(2000);

        assertEquals("http://localhost:3000/home", driver.getCurrentUrl());
    }

    @Test
    @Order(2)
    public void importCurriculum() throws InterruptedException {
        driver.get("http://localhost:3000/home");

        WebElement buttonImport = getElementByXPath("//*[@id=\"root\"]/div/div/div[3]/button");
        clickElement(buttonImport);

        Thread.sleep(4000);

//        driver.switchTo()
//                .activeElement().sendKeys(System.getProperty("user.dir") + "\\src\\test\\java\\com\\ifpb\\lattesmaismais\\util\\teste.xml");

        WebElement selectXML = getElementByXPath("//*[@id=\"root\"]/div/div/div[3]/input");
        selectXML.sendKeys(System.getProperty("user.dir") + "\\src\\test\\java\\com\\ifpb\\lattesmaismais\\util\\teste.xml");

        Thread.sleep(4000);

        WebElement buttonConfirm = getElementByXPath("//*[@id=\"root\"]/div/div/div[4]/div/div/button[1]");
        clickElement(buttonConfirm);

        Thread.sleep(2000);
    }

    @Test
    @Order(3)
    public void listEntries() throws InterruptedException {
        driver.get("http://localhost:3000/updateVersions/1");

        Thread.sleep(1000);

        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy_HHmm"));

        assertEquals(getElementById("countEntry").getText(), "(Entradas identificadas: 45)");
        assertTrue(getElementById("versionCurriculum").getText().startsWith("V_" + dateTime));
        assertEquals(getElementById("descriptionCurriculum").getText(), "Primeira versão criada");
        assertEquals(getElementById("nameCurriculumOwner").getText(), "Teste");
    }

    @Test
    @Order(4)
    public void addingAndRemovingReceipts() throws InterruptedException {
        driver.get("http://localhost:3000/updateVersions/1");

        WebElement newVersion = getElementById("buttonNewVersion");
        newVersion.click();

        WebElement commentaryVersion = getElementByClass("Commentary");
        commentaryVersion.sendKeys("Versão com comprovante físico");

        WebElement saveNewVersion = getElementById("buttonSaveNewVersion");
        saveNewVersion.click();

        Thread.sleep(2000);

        // Clicando na primeira entrada do currículo
        WebElement entry = getElementById("81");
        clickElement(entry);

        Thread.sleep(2000);

        // Confirmando presença de icon "sem comprovantes"
        assertEquals("http://localhost:3000/static/media/WithoutProof.84fc0f66c1fa6e0cc4b3a4d6ba81d729.svg", getElementById("icon81").getAttribute("src"));

        WebElement addValidatorAuthentication = getElementById("butonAuthValidator");
        clickElement(addValidatorAuthentication);

        Thread.sleep(1000);
        WebElement getReceiptArchive = getElementById("buttonSendFisicalReceipt");
        clickElement(getReceiptArchive);

        WebElement selectArchive = getElementByXPath("//*[@id=\"root\"]/div/div/div[6]/div/div[1]/input[2]");
        selectArchive.sendKeys(System.getProperty("user.dir") + "\\src\\test\\java\\com\\ifpb\\lattesmaismais\\util\\teste.jpg");

        Thread.sleep(2000);

        WebElement addCommentary = getElementByClass("Input-commentary");
        addCommentary.sendKeys("Comprovante de atuação");

        Thread.sleep(1000);
        WebElement addReceiptArchive = getElementById("buttonAddFisicalReceipt");
        clickElement(addReceiptArchive);

        Thread.sleep(2000);
        assertAll(
                () -> assertEquals("teste", getElementByXPath("//*[@id=\"root\"]/div/div/div[7]/div/div/a").getText()),
                () -> assertEquals(".jpg", getElementById("extRecnew1").getText()),
                () -> assertEquals("\"Comprovante de atuação\"", getElementById("commRecnew1").getText()),
                // Confirmando alteração do icon para "aguardando validação"
                () -> assertEquals("http://localhost:3000/static/media/Waiting.256efcc25c115d72468487505c96e7bc.svg", getElementById("icon812").getAttribute("src"))
        );

        // Salvando versão com comprovante físico
        WebElement saveVersion = getElementById("buttonUpdate");
        saveVersion.click();

        // Removendo comprovante adicionado
        WebElement buttonRemoveReceipt = getElementByXPath("//*[@id=\"btRecnew1\"]");
        clickElement(buttonRemoveReceipt);

        Thread.sleep(2000);

        // Confirmando troca de icon de "aguardando validação" para "sem comprovantes"
        assertThrows(NoSuchElementException.class, () -> getElementById("icon812").getAttribute("src"));
        assertEquals("http://localhost:3000/static/media/WithoutProof.84fc0f66c1fa6e0cc4b3a4d6ba81d729.svg", getElementById("icon81").getAttribute("src"));

        // Confirmando se versão foi realmente salva:
        driver.get("http://localhost:3000/shedulingvalidation");

        Thread.sleep(2000);
        String tableBody = getElementByTagName("TBODY").getText();

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString();

        assertAll("Verificando se versão salva está na listagem de versões",
                () -> assertTrue(tableBody.contains(date)),
                () -> assertTrue(tableBody.contains("Versão com comprovante físico"))
        );
    }

    @Test
    @Order(5)
    public void addingAndRemovingEletronicAuthentication() throws InterruptedException {
        driver.get("http://localhost:3000/updateVersions/1");

        WebElement newVersion = getElementById("buttonNewVersion");
        newVersion.click();

        WebElement commentaryVersion = getElementByClass("Commentary");
        commentaryVersion.sendKeys("Versão com comprovante eletrônico");

        WebElement saveNewVersion = getElementById("buttonSaveNewVersion");
        saveNewVersion.click();

        Thread.sleep(2000);

        // Clicando na primeira entrada do currículo
        WebElement entry = getElementById("91");
        clickElement(entry);

        Thread.sleep(2000);

        // Confirmando presença de icon "sem comprovantes"
        assertEquals("http://localhost:3000/static/media/WithoutProof.84fc0f66c1fa6e0cc4b3a4d6ba81d729.svg", getElementById("icon91").getAttribute("src"));

        WebElement addEletronicAuthentication = getElementById("buttonAuthEletronic");
        clickElement(addEletronicAuthentication);

        Thread.sleep(1000);
        WebElement addLink = getElementByClass("Paragraph-field");
        addLink.sendKeys("www.teste.com/teste.pdf");

        Thread.sleep(2000);

        WebElement addCommentary = getElementByClass("Commentary");
        addCommentary.sendKeys("Comprovante eletrônico");

        Thread.sleep(1000);
        WebElement addReceipt = getElementById("buttonAddReceiptLink");
        clickElement(addReceipt);

        Thread.sleep(2000);
        assertAll(
                () -> assertEquals("Comprovante eletrônico", getElementById("commRecnew1").getText()),
                () -> assertEquals("http://localhost:3000/updateversions/www.teste.com/teste.pdf", getElementByXPath("//*[@id=\"root\"]/div/div/div[7]/div/div/a").getAttribute("href")),
                // Confirmando alteração do icon para "aguardando validação"
                () -> assertEquals("http://localhost:3000/static/media/Waiting.256efcc25c115d72468487505c96e7bc.svg", getElementById("icon912").getAttribute("src"))
        );

        // Removendo comprovante adicionado
        WebElement buttonRemoveReceipt = getElementById("btRecnew1");
        clickElement(buttonRemoveReceipt);

        Thread.sleep(2000);

        // Confirmando troca de icon de "aguardando validação" para "sem comprovantes"
        assertThrows(NoSuchElementException.class, () -> getElementById("icon912").getAttribute("src"));
        assertEquals("http://localhost:3000/static/media/WithoutProof.84fc0f66c1fa6e0cc4b3a4d6ba81d729.svg", getElementById("icon91").getAttribute("src"));

        // Confirmando se versão foi realmente salva:
        driver.get("http://localhost:3000/shedulingvalidation");

        Thread.sleep(2000);
        String tableBody = getElementByTagName("TBODY").getText();

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString();

        assertAll("Verificando se versão salva está na listagem de versões",
                () -> assertTrue(tableBody.contains(date)),
                () -> assertTrue(tableBody.contains("Versão com comprovante eletrônico"))
        );
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
