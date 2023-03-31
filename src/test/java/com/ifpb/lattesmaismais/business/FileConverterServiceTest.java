package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.presentation.exception.FileConversionException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileConverterServiceTest {

    private static final Path pathReadFile = Path.of("C:\\Users\\Public\\Documents\\testRead.txt");

    private static final Path pathWriteFile = Path.of("C:\\Users\\Public\\Documents\\testWrite.txt");

    private static FileConverterService converterService;

    @BeforeAll
    public static void setUp() {
        converterService = new FileConverterService();

        try {
            if (!Files.exists(pathReadFile)) {
                Files.createFile(pathReadFile);
            }
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    @Order(1)
    public void testReadFileOk() {
        try {
            byte[] fileData = converterService.readFile(pathReadFile.toString());

            assertNotNull(fileData);
        } catch (FileConversionException e) {
           fail();
        }
    }

    @Test
    @Order(2)
    public void testReadFileException() {
        // Testing with no path
        assertThrows(FileConversionException.class, () -> converterService.readFile(""));

        // Testing with wrong path
        assertThrows(FileConversionException.class, () -> converterService.readFile(pathReadFile + ".aaa"));
    }

    @Test
    @Order(3)
    public void testWriteFileOk() {
        try {
            // Reading file content
            byte[] fileData = converterService.readFile(pathReadFile.toString());

            // Writing new file
            converterService.writeFile(pathWriteFile.toString(), fileData);

            // Reading new file content
            byte[] writedData = converterService.readFile(pathWriteFile.toString());

            assertAll("Asserting file content",
                () -> assertNotNull(writedData),
                () -> assertTrue(Files.exists(Path.of(pathWriteFile.toString()))),
                () -> assertArrayEquals(fileData, writedData)
            );

        } catch (FileConversionException e) {
            fail();
        }
    }

    @Test
    @Order(4)
    public void testWriteFileException() {
        // Testing with no data
        assertThrows(FileConversionException.class, () -> converterService.writeFile(pathReadFile.toString(), null));

        // Testing with no path
        try {
            byte[] fileData = converterService.readFile(pathReadFile.toString());
            assertThrows(FileConversionException.class, () -> converterService.writeFile("", fileData));
        } catch (FileConversionException e) {
            fail();
        }

        // Testing with no path and data
        assertThrows(FileConversionException.class, () -> converterService.writeFile("", null));
    }

    @AfterAll
    public static void tearDown() {
        try {
            System.out.println("Deleting created files");
//            Files.deleteIfExists(pathReadFile);
            Files.deleteIfExists(pathWriteFile);
        } catch (IOException e) {
            fail();
        }
    }
}
