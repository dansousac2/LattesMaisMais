package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.presentation.exception.FileConversionException;
import org.junit.jupiter.api.*;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileConverterServiceTest {

    private static final Path pathReadFile = Path.of(System.getProperty("user.dir") + "\\src\\test\\java\\com\\ifpb\\lattesmaismais\\util\\teste.jpg");

    private static final Path pathWriteFile = Path.of("C:\\Users\\Public\\Documents\\testWrite.jpg");

    private static FileConverterService converterService;

    @BeforeAll
    public static void setUp() {
        converterService = new FileConverterService();
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
        try {
            // Testing with no data
            assertThrows(FileConversionException.class, () -> converterService.writeFile(pathWriteFile.toString(), null));

            // Testing with no path
            byte[] fileData = converterService.readFile(pathReadFile.toString());
            assertThrows(FileConversionException.class, () -> converterService.writeFile("", fileData));

            // Testing with no path and data
            assertThrows(FileConversionException.class, () -> converterService.writeFile("", null));

            // Reajustando arquivo
            converterService.writeFile(pathWriteFile.toString(), fileData);
        } catch (FileConversionException e) {
            fail();
        }
    }
}
