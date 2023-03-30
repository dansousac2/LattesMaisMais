package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.presentation.exception.DecryptionException;
import com.ifpb.lattesmaismais.presentation.exception.EncryptionException;
import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileEncryptionServiceTest {

    private static FileEncryptionService encryptionService;

    private static final String pathReadFile = "C:\\Users\\Public\\Documents\\teste.jpg";

    private static FileConverterService fileConverterService;


    @BeforeAll
    public static void setUp() {
        encryptionService = new FileEncryptionService();
        fileConverterService = new FileConverterService();
    }

    @Test
    @Order(1)
    public void testEncryptDataOk() {
        try {
            byte[] fileData = fileConverterService.readFile(pathReadFile);

            assertDoesNotThrow(() -> encryptionService.encryptData(fileData));

            byte[] encryptedData = encryptionService.encryptData(fileData);

            assertFalse(Arrays.equals(fileData, encryptedData));

        } catch (Exception e) {
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
            byte[] fileData = fileConverterService.readFile(pathReadFile);
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
}