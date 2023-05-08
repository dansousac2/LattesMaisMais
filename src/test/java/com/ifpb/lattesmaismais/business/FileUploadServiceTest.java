package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.presentation.exception.DecryptionException;
import com.ifpb.lattesmaismais.presentation.exception.EncryptionException;
import com.ifpb.lattesmaismais.presentation.exception.FileConversionException;
import com.ifpb.lattesmaismais.model.entity.Receipt;

import com.ifpb.lattesmaismais.presentation.exception.FileWithoutNameException;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileUploadServiceTest {

    private static final String path = "C:\\Users\\Public\\Documents";
    private static final String pathReadFile = System.getProperty("user.dir") + "\\src\\test\\java\\com\\ifpb\\lattesmaismais\\util\\teste.jpg";
    private static final String pathReadCurriculum = System.getProperty("user.dir") + "\\src\\test\\java\\com\\ifpb\\lattesmaismais\\util\\teste.xml";

    private static MultipartFile multipartFile;

    private static MultipartFile multipartCurriculum;

    private static Receipt receipt;

    @Mock
    private FileConverterService fileConverterService;

    @Mock
    private FileEncryptionService encryptionService;

    @Mock
    private ReceiptService receiptService;

    @Mock
    private ReceiptConverterService converterService;

    @InjectMocks
    @Spy
    private FileUploadService uploadService;

    private static final FileConverterService setupFileConverter = new FileConverterService();

   @BeforeAll
   public static void setUp() {
       try {
           byte[] fileData = setupFileConverter.readFile(pathReadFile);
           multipartFile = new MockMultipartFile("teste", "teste.jpg", ".jpg", fileData);

           byte[] curriculumData = setupFileConverter.readFile(pathReadCurriculum);
           multipartCurriculum = new MockMultipartFile("teste", "teste.xml", ".xml", curriculumData);

           receipt = new Receipt();
           receipt.setId(1);
           receipt.setName(multipartFile.getName());
           receipt.setExtension(multipartFile.getContentType());

       } catch (Exception e) {
           fail();
       }
   }

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(uploadService, "directoryFile", path);
        ReflectionTestUtils.setField(uploadService, "fileConverterService", fileConverterService);
        ReflectionTestUtils.setField(uploadService, "fileEncryptionService", encryptionService);
        ReflectionTestUtils.setField(uploadService, "receiptService", receiptService);
        ReflectionTestUtils.setField(uploadService, "converterService", converterService);
    }

    @Test
    @Order(1)
    public void testUploadFileOk() {
        try {
            when(converterService.fileToEntity(any(MultipartFile.class), anyString(), anyString())).thenReturn(receipt);
            when(receiptService.save(any(Receipt.class))).thenReturn(receipt);
            when(encryptionService.encryptData(any())).thenReturn(multipartFile.getBytes());
            doCallRealMethod().when(fileConverterService).writeFile(any(), any());
            doCallRealMethod().when(uploadService).createDiretory(anyString());

            assertDoesNotThrow(() -> uploadService.uploadFile(multipartFile, "teste", "Sem comentários", "https://www.teste.com/"));

            verify(converterService).fileToEntity(any(MultipartFile.class), anyString(), anyString());
            verify(receiptService).save(any(Receipt.class));
            verify(encryptionService).encryptData(any());
            verify(fileConverterService).writeFile(any(), any());
            verify(uploadService).createDiretory(anyString());

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @Order(2)
    public void testUploadFileWithoutNameException() {
        try {
            when(converterService.fileToEntity(any(MultipartFile.class), anyString(), anyString())).thenThrow(FileWithoutNameException.class);
            when(receiptService.save(any(Receipt.class))).thenReturn(receipt);
            when(encryptionService.encryptData(any())).thenReturn(multipartFile.getBytes());
            doCallRealMethod().when(fileConverterService).writeFile(any(), any());
            doCallRealMethod().when(uploadService).createDiretory(anyString());


            MultipartFile multipartFileFail = new MockMultipartFile("teste", "", "", setupFileConverter.readFile(pathReadFile));

            assertThrows(FileWithoutNameException.class, () -> uploadService.uploadFile(multipartFileFail, "teste", "Sem comentários", "https://www.teste.com/"));

            verify(encryptionService, times(1)).encryptData(any());
            verify(converterService).fileToEntity(any(MultipartFile.class), anyString(), anyString());
            verify(receiptService, times(0)).save(any(Receipt.class));
            verify(fileConverterService, times(0)).writeFile(any(), any());
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    @Order(3)
    public void testUploadFileEncryptionException() {
        try {
            when(converterService.fileToEntity(any(MultipartFile.class), anyString(), anyString())).thenReturn(receipt);
            when(receiptService.save(any(Receipt.class))).thenReturn(receipt);
            when(encryptionService.encryptData(any())).thenThrow(EncryptionException.class);
            doCallRealMethod().when(fileConverterService).writeFile(any(), any());
            doCallRealMethod().when(uploadService).createDiretory(anyString());

            assertThrows(EncryptionException.class, () -> uploadService.uploadFile(multipartFile, "teste", "Sem comentários", "https://www.teste.com/"));

            verify(encryptionService).encryptData(any());
            verify(converterService, times(0)).fileToEntity(any(MultipartFile.class), anyString(), anyString());
            verify(receiptService, times(0)).save(any(Receipt.class));
            verify(fileConverterService, times(0)).writeFile(any(), any());

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @Order(4)
    public void testUploadFileConversionException() {
        try {
            when(converterService.fileToEntity(any(MultipartFile.class), anyString(), anyString())).thenReturn(receipt);
            when(receiptService.save(any(Receipt.class))).thenReturn(receipt);
            when(encryptionService.encryptData(any())).thenReturn(multipartFile.getBytes());
            doThrow(FileConversionException.class).when(fileConverterService).writeFile(any(), any());
            doCallRealMethod().when(uploadService).createDiretory(anyString());

            assertThrows(FileConversionException.class, () -> uploadService.uploadFile(multipartFile, "teste", "Sem comentários", "https://www.teste.com/"));

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @Order(5)
    public void testUploadCurriculumOk() {
        try {
            doCallRealMethod().when(fileConverterService).writeFile(any(), any());

            assertDoesNotThrow(() -> uploadService.uploadCurriculum(multipartCurriculum, "teste"));

            verify(fileConverterService).writeFile(any(), any());
            verify(uploadService).createDiretory(anyString());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @Order(6)
    public void testUploadCurriculumConversionException() {
        try {
            doThrow(FileConversionException.class).when(fileConverterService).writeFile(any(), any());

            assertThrows(FileConversionException.class, () -> uploadService.uploadCurriculum(multipartCurriculum, "teste"));

            verify(fileConverterService).writeFile(any(), any());
            verify(uploadService).createDiretory(anyString());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @Order(7)
    public void testReadFileOk() {
        try {
            when(encryptionService.decryptData(any())).thenReturn(multipartFile.getBytes());
            doCallRealMethod().when(fileConverterService).readFile(any());
            doCallRealMethod().when(fileConverterService).writeFile(any(), any());

            assertDoesNotThrow(() -> uploadService.readFile(receipt.getId() + receipt.getExtension(), "teste"));

            verify(encryptionService).decryptData(any());
            verify(fileConverterService).writeFile(any(), any());
            verify(fileConverterService).readFile(any());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @Order(8)
    public void testReadFileNotFoundException() {
        try {
            when(encryptionService.decryptData(any())).thenReturn(multipartFile.getBytes());
            doCallRealMethod().when(fileConverterService).readFile(any());
            doCallRealMethod().when(fileConverterService).writeFile(any(), any());

            assertThrows(FileNotFoundException.class, () -> uploadService.readFile("arquivoinexistente.jpg", "teste"));


            verify(encryptionService, times(0)).decryptData(any());
            verify(fileConverterService, times(0)).writeFile(any(), any());
            verify(fileConverterService, times(0)).readFile(any());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @Order(9)
    public void testReadFileConversionException() {
        try {
            when(encryptionService.decryptData(any())).thenReturn(multipartFile.getBytes());
            doThrow(FileConversionException.class).when(fileConverterService).readFile(any());
            doCallRealMethod().when(fileConverterService).writeFile(any(), any());

            assertThrows(FileConversionException.class, () -> uploadService.readFile(receipt.getId() + receipt.getExtension(), "teste"));

            verify(fileConverterService).readFile(any());
            verify(encryptionService, times(0)).decryptData(any());
            verify(fileConverterService, times(0)).writeFile(any(), any());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @Order(10)
    public void testReadFileDecryptionException() {
        try {
            when(encryptionService.decryptData(any())).thenThrow(DecryptionException.class);
            doCallRealMethod().when(fileConverterService).readFile(any());
            doCallRealMethod().when(fileConverterService).writeFile(any(), any());

            assertThrows(DecryptionException.class, () -> uploadService.readFile(receipt.getId() + receipt.getExtension(), "teste"));


            verify(encryptionService).decryptData(any());
            verify(fileConverterService).readFile(any());
            verify(fileConverterService, times(0)).writeFile(any(), any());
        } catch (Exception e) {
            fail();
        }
    }

    @AfterAll
    public static void tearDown() {
       Path pathToDelete = Path.of(path + "\\teste");
        try {
            System.out.println("Deleting created files and directories");
            Files.deleteIfExists(Path.of(pathToDelete + "\\" + receipt.getId() + receipt.getExtension()));
            Files.deleteIfExists(Path.of(pathToDelete + "\\curriculum.xml"));
            Files.deleteIfExists(Path.of(pathToDelete + "_decrypted" + "\\" + receipt.getId() + receipt.getExtension()));
            Files.deleteIfExists(Path.of(pathToDelete + "_decrypted"));
            Files.deleteIfExists(pathToDelete);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
