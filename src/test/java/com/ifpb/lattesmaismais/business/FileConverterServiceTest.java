package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.presentation.exception.FileConversionException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileConverterServiceTest {

    private static final String pathReadFile = "C:\\Users\\Public\\Documents\\testRead.txt";

    private static final String pathWriteFile = "C:\\Users\\Public\\Documents\\testWrite.txt";

    private static FileConverterService converterService;

    @BeforeAll
    public static void setUp() {
        converterService = new FileConverterService();

        try {
            if (!Files.exists(Path.of(pathReadFile))) {
                Files.createFile(Path.of(pathReadFile));
            }
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    @Order(1)
    public void testReadFileOk() {
        try {
            byte[] fileData = converterService.readFile(pathReadFile);

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
            byte[] fileData = converterService.readFile(pathReadFile);

            // Writing new file
            converterService.writeFile(pathWriteFile, fileData);

            // Reading new file content
            byte[] writedData = converterService.readFile(pathWriteFile);

            assertAll("Asserting file content",
                () -> assertNotNull(writedData),
                () -> assertTrue(Files.exists(Path.of(pathWriteFile))),
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
        assertThrows(FileConversionException.class, () -> converterService.writeFile(pathReadFile, null));

        // Testing with no path
        try {
            byte[] fileData = converterService.readFile(pathReadFile);
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
            Files.deleteIfExists(Path.of(pathWriteFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
