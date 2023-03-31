package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.presentation.exception.DecryptionException;
import com.ifpb.lattesmaismais.presentation.exception.EncryptionException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileEncryptionServiceTest {

    private static FileEncryptionService encryptionService;

    private static final String path = "C:\\Users\\Public\\Documents\\teste.jpg";

    private static final String pathReadFile = System.getProperty("user.dir") + "\\src\\test\\java\\com\\ifpb\\lattesmaismais\\util\\teste.jpg";

    private static FileConverterService fileConverterService;


    @BeforeAll
    public static void setUp() {
        encryptionService = new FileEncryptionService();
        fileConverterService = new FileConverterService();

        try {
            byte[] fileData = fileConverterService.readFile(pathReadFile);
            if (!Files.exists(Path.of(path))) {
                fileConverterService.writeFile(path, fileData);
            }
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @Order(1)
    public void testEncryptDataOk() {
        try {
            byte[] fileData = fileConverterService.readFile(path);

            assertDoesNotThrow(() -> encryptionService.encryptData(fileData));

            byte[] encryptedData = encryptionService.encryptData(fileData);

            assertFalse(Arrays.equals(fileData, encryptedData));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    @Order(2)
    public void testEncryptDataException() {
        byte[] emptyData = new byte[0];

        assertThrows(EncryptionException.class, () -> encryptionService.encryptData(null));
        assertThrows(EncryptionException.class, () -> encryptionService.encryptData(emptyData));
    }

    @Test
    @Order(3)
    public void testDecryptDataOk() {
        try {
            byte[] fileData = fileConverterService.readFile(path);
            byte[] encryptedData = encryptionService.encryptData(fileData);

            assertDoesNotThrow(() -> encryptionService.decryptData(encryptedData));

            byte[] decryptedData = encryptionService.decryptData(encryptedData);
            assertArrayEquals(fileData, decryptedData);

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @Order(4)
    public void testDecryptDataException() {
        byte[] emptyData = new byte[0];

        assertThrows(DecryptionException.class, () -> encryptionService.decryptData(null));
        assertThrows(DecryptionException.class, () -> encryptionService.decryptData(emptyData));
    }

    @AfterAll
    public static void tearDown() {
        try {
            System.out.println("Deleting created files");
            Files.deleteIfExists(Path.of(path));
        } catch (IOException e) {
            fail();
        }
    }
}