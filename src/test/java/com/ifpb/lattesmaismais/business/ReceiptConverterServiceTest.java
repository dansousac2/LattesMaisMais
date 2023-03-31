package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.model.entity.Receipt;
import com.ifpb.lattesmaismais.presentation.exception.FileConversionException;
import com.ifpb.lattesmaismais.presentation.exception.FileWithoutNameException;
import org.junit.jupiter.api.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReceiptConverterServiceTest {

    private static ReceiptConverterService converterService;

    private static final String pathReadFile = "C:\\Users\\Public\\Documents\\teste.txt";

    private static FileConverterService fileConverterService;

    private static MultipartFile multipartFile;

    @BeforeAll
    public static void setUp() {
        converterService = new ReceiptConverterService();
        fileConverterService = new FileConverterService();

        try {
            Path path = Path.of(pathReadFile);
            if (!Files.exists(path)) {
                Files.createFile(path);
                Files.write(path, "Isso é um teste".getBytes());
            }

            byte[] fileData = fileConverterService.readFile(pathReadFile);
            multipartFile = new MockMultipartFile("teste", "teste.txt", ".txt", fileData);

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @Order(1)
    public void testFileToEntityOk() {
        try {
            Receipt entity = converterService.fileToEntity(multipartFile);

            assertAll(
                () -> assertEquals(entity.getName(), multipartFile.getName()),
                () -> assertEquals(entity.getExtension(), multipartFile.getContentType()));
        } catch (FileWithoutNameException e) {
            fail();
        }
    }

    @Test
    @Order(2)
    public void testFileToEntityException() {
        byte[] fileData = null;
        try {
            fileData = fileConverterService.readFile(pathReadFile);
        } catch (FileConversionException e) {
            fail();
        }

        MultipartFile multipartFileFail = new MockMultipartFile("teste", "", "", fileData);

        Throwable exception = assertThrows(FileWithoutNameException.class, () -> converterService.fileToEntity(multipartFileFail));
        assertEquals("O arquivo enviado não possui nome!", exception.getMessage());
    }

    @AfterAll
    public static void tearDown() {
        try {
            System.out.println("Deleting created files");
            Files.deleteIfExists(Path.of(pathReadFile));
        } catch (IOException e) {
            fail();
        }
    }
}
