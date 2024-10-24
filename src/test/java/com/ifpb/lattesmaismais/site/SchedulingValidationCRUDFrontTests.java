package com.ifpb.lattesmaismais.site;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SchedulingValidationCRUDFrontTests {

    private static WebDriver driver;

    private static JavascriptExecutor jse;

    private static String versionText;

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
    public void updatingCurriculumVersion() throws InterruptedException {
        // Login do solicitante:
        login("teste@gmail.com", "12345678");

        driver.get("http://localhost:3000/versionlisting");

        Thread.sleep(2000);

        WebElement editButton = getElementByXPath("//*[@id=\"root\"]/div/div/div[2]/div/div[1]/div[3]/button[1]");
        clickElement(editButton);

        // Checando se a tela de edição foi acessada:
        assertTrue(driver.getCurrentUrl().startsWith("http://localhost:3000/updateversions/"));

        Thread.sleep(1000);
        WebElement entry = getElementById("1");
        clickElement(entry);

        Thread.sleep(2000);

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
        // Salvando versão com comprovante físico
        WebElement saveVersion = getElementById("buttonUpdate");
        saveVersion.click();

        Thread.sleep(2000);
        WebElement backButton = getElementByXPath("//*[@id=\"ico-comeBack\"]");
        clickElement(backButton);

        Thread.sleep(2000);
        assertEquals("http://localhost:3000/versionlisting", driver.getCurrentUrl().toString());
    }

    @Test
    @Order(2)
    public void solicitingValidation() throws InterruptedException {
        login("teste@gmail.com", "12345678");
        // Consultando versão do currículo atualizado:
        driver.get("http://localhost:3000/versionlisting");

        Thread.sleep(5000);
        driver.get("http://localhost:3000/updateversions/1");
        Thread.sleep(2000);
        versionText = getElementById("versionCurriculum").getText();
        Thread.sleep(2000);

        driver.get("http://localhost:3000/shedulingvalidation");

        Thread.sleep(5000);
        // Selecionando versão:
        WebElement version = getElementByXPath("//*[@id=\"curricTable01\"]/tbody/tr[3]/td[3]/input");
        clickElement(version);

        // Selecionando validador:
        WebElement validator = getElementByXPath("//*[@id=\"validtb01\"]/tbody/tr/td[2]/input");
        clickElement(validator);

        // Selecionando data:
        String dateString = LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString();
        WebElement date = getElementById("date01");
        date.sendKeys(dateString);

        // Selecionando horário:
        WebElement time = getElementById("hour01");
        time.sendKeys("15:00");

        // Salvando solicitação de validação:
        WebElement soliciteValidation = getElementByXPath("//*[@id=\"root\"]/div/div/div[2]/div/div[1]/button");
        soliciteValidation.click();

        // Validando criação do agendamento:
        driver.get("http://localhost:3000/scheduling");

        Thread.sleep(2000);

        WebElement buttonOpen = getElementById("tabButton01");
        assertNotEquals("ABERTOS(0)", buttonOpen.getText());

        String tableBody = getElementByTagName("TBODY").getText();
        String dateToCompare = LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();

        assertAll("Verificando se agendamento salvo está na listagem de agendamentos",
                () -> assertTrue(tableBody.contains("15:00")),
                () -> assertTrue(tableBody.contains(dateToCompare)),
                () -> assertTrue(tableBody.contains(versionText))
        );

        Thread.sleep(2000);

        // Deslogando da conta do solicitante:
        clickElement(getElementByClass("b6"));
        Thread.sleep(2000);
    }

    @Test
    @Order(3)
    public void acceptingAValidation() throws InterruptedException {
        // Login do validador:
        login("testevalidador@gmail.com", "12345678");

        clickElement(getElementByXPath("//*[@id=\"buttonSSV\"]"));

        Thread.sleep(2000);

        // Confirmando informações do agendamento (versão, data, hora):
        assertEquals(versionText, getElementByXPath("//*[@id=\"root\"]/div/div/div[2]/div/div/div[2]/h3[2]").getText());
        assertEquals(LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString(), getElementByXPath("//*[@id=\"root\"]/div/div/div[2]/div/div/div[1]/h3[1]").getText());
        assertEquals("15:00", getElementByXPath("//*[@id=\"root\"]/div/div/div[2]/div/div/div[1]/h3[2]").getText());

        // Aceitando agendamento:
        Thread.sleep(2000);
        clickElement(getElementByXPath("//*[@id=\"root\"]/div/div/div[2]/div/div/div[3]/button[1]"));
        Thread.sleep(2000);
        clickElement(getElementByXPath("//*[@id=\"root\"]/div/div/div[3]/div/div/button[1]"));

        Thread.sleep(1000);
        // Confirmando que solicitação foi aceita:
        WebElement statusRequest = getElementByXPath("//*[@id=\"root\"]/div/div/div[2]/div/div/div[3]/h3");
        assertEquals("Solicitação: Aceita", statusRequest.getText());

        Thread.sleep(1000);
    }

    @Test
    @Order(4)
    public void reviewingACurriculum() throws InterruptedException {
        driver.get("http://localhost:3000/reviewcurriculum");

        Thread.sleep(1000);
        // Clicando em botão para avaliar:
        clickElement(getElementByXPath("//*[@id=\"root\"]/div/div/div[2]/div/div/div[3]/button"));
        Thread.sleep(1000);

        // Checando versão do currículo:
        assertEquals(versionText, getElementByXPath("//*[@id=\"root\"]/div/div/div[2]/div[2]/h2[2]/b").getText());

        WebElement entry = getElementById("1");
        clickElement(entry);

        Thread.sleep(2000);
        assertEquals("\"Comprovante de atuação\"", getElementByXPath("//*[@id=\"root\"]/div/div/div[2]/div[3]/div[2]/div/div/b").getText());

        // Validando comprovante:
        clickElement(getElementByXPath("//*[@id=\"root\"]/div/div/div[2]/div[3]/div[2]/div/div/div/button[1]"));

        Thread.sleep(1000);

        // Salvando validação:
        clickElement(getElementById("ico-comeSaveRA"));
        Thread.sleep(2000);

        driver.get("http://localhost:3000/solicitedschedule");
        Thread.sleep(2000);

        // Confirmando que solicitação foi resolvida:
        WebElement statusRequest = getElementByXPath("//*[@id=\"root\"]/div/div/div[2]/div/div/div[3]/h3");
        assertEquals("Solicitação: Resolvida", statusRequest.getText());

        Thread.sleep(2000);
        // Deslogando da conta do validador:
        clickElement(getElementByClass("b6"));
        Thread.sleep(2000);

        // Verificando a resolução do agendamento do lado do solicitante:
        login("teste@gmail.com", "12345678");

        driver.get("http://localhost:3000/scheduling");

        Thread.sleep(2000);

        WebElement buttonOpen = getElementById("tabButton01");
        assertEquals("ABERTOS(0)", buttonOpen.getText());

        WebElement buttonClosed = getElementById("tabButton04");
        assertNotEquals("CONCLUÍDOS(0)", buttonClosed.getText());
        clickElement(buttonClosed);

        Thread.sleep(2000);

        String tableBody = getElementByTagName("TBODY").getText();
        String dateToCompare = LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();

        assertAll("Verificando se agendamento concluído está na listagem de agendamentos",
                () -> assertTrue(tableBody.contains("15:00")),
                () -> assertTrue(tableBody.contains(dateToCompare)),
                () -> assertTrue(tableBody.contains(versionText))
        );

        Thread.sleep(2000);

        // Verificando mudança de ícone do comprovante para "Validado por validador"
        driver.get("http://localhost:3000/updateversions/1");
        Thread.sleep(2000);

        assertEquals("http://localhost:3000/static/media/Proven.f4a137e92b0f8c10d620fe9a66ed80c9.svg", getElementById("icon12").getAttribute("src"));

        // Deslogando da conta do solicitante:
        clickElement(getElementByClass("b6"));
        Thread.sleep(2000);
    }


    @AfterAll
    static void tearDown() throws InterruptedException {
        Thread.sleep(2000);
        driver.quit();
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
