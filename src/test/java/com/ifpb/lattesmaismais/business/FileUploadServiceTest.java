package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.presentation.exception.EncryptionException;
import com.ifpb.lattesmaismais.presentation.exception.FileConversionException;
import com.ifpb.lattesmaismais.model.entity.Receipt;

import com.ifpb.lattesmaismais.presentation.exception.FileWithoutNameException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class FileUploadServiceTest {

    private static final String path = "C:\\Users\\Public\\Documents";
    private static final String pathReadFile = path + "\\teste.jpg";

    private static MultipartFile multipartFile;
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

           receipt = new Receipt();
           receipt.setId(1);
           receipt.setName(multipartFile.getName());
           receipt.setExtension(multipartFile.getContentType());

       } catch (FileConversionException e) {
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
    public void testUploadFileOk() {
        try {
            when(converterService.fileToEntity(any(MultipartFile.class))).thenReturn(receipt);
            when(receiptService.save(any(Receipt.class))).thenReturn(receipt);
            when(encryptionService.encryptData(any())).thenReturn(multipartFile.getBytes());
            doCallRealMethod().when(fileConverterService).writeFile(any(), any());
            doCallRealMethod().when(uploadService).createDiretory(anyString());

            assertDoesNotThrow(() -> uploadService.uploadFile(multipartFile, "teste"));

            verify(converterService).fileToEntity(any(MultipartFile.class));
            verify(receiptService).save(any(Receipt.class));
            verify(encryptionService).encryptData(any());
            verify(fileConverterService).writeFile(any(), any());
            verify(uploadService).createDiretory(anyString());

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testUploadFileWithoutNameException() {
        try {
            when(converterService.fileToEntity(any(MultipartFile.class))).thenThrow(FileWithoutNameException.class);
            when(receiptService.save(any(Receipt.class))).thenReturn(receipt);
            when(encryptionService.encryptData(any())).thenReturn(multipartFile.getBytes());
            doCallRealMethod().when(fileConverterService).writeFile(any(), any());
            doCallRealMethod().when(uploadService).createDiretory(anyString());


            MultipartFile multipartFileFail = new MockMultipartFile("teste", "", "", setupFileConverter.readFile(pathReadFile));

            assertThrows(FileWithoutNameException.class, () -> uploadService.uploadFile(multipartFileFail, "teste"));

            verify(converterService).fileToEntity(any(MultipartFile.class));
            verify(receiptService, times(0)).save(any(Receipt.class));
            verify(encryptionService, times(0)).encryptData(any());
            verify(fileConverterService, times(0)).writeFile(any(), any());
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testUploadFileEncryptionException() {
        try {
            when(converterService.fileToEntity(any(MultipartFile.class))).thenReturn(receipt);
            when(receiptService.save(any(Receipt.class))).thenReturn(receipt);
            when(encryptionService.encryptData(any())).thenThrow(EncryptionException.class);
            doCallRealMethod().when(fileConverterService).writeFile(any(), any());
            doCallRealMethod().when(uploadService).createDiretory(anyString());

            assertThrows(EncryptionException.class, () -> uploadService.uploadFile(multipartFile, "teste"));

            verify(converterService).fileToEntity(any(MultipartFile.class));
            verify(receiptService).save(any(Receipt.class));
            verify(encryptionService).encryptData(any());
            verify(fileConverterService, times(0)).writeFile(any(), any());

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testUploadFileConversionException() {
        try {
            when(converterService.fileToEntity(any(MultipartFile.class))).thenReturn(receipt);
            when(receiptService.save(any(Receipt.class))).thenReturn(receipt);
            when(encryptionService.encryptData(any())).thenReturn(multipartFile.getBytes());
            doThrow(FileConversionException.class).when(fileConverterService).writeFile(any(), any());
            doCallRealMethod().when(uploadService).createDiretory(anyString());

            assertThrows(FileConversionException.class, () -> uploadService.uploadFile(multipartFile, "teste"));

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testReadFileOk() {

    }

    @Test
    public void testReadFileNotFoundException() {

    }

    @Test
    public void testReadFileConversionException() {

    }

    @Test
    public void testReadFileDecryptionException() {

    }

    @AfterAll
    public static void tearDown() {
       Path pathToDelete = Path.of(path + "\\teste");
        try {
            Files.deleteIfExists(Path.of(pathToDelete + "\\" + receipt.getId() + receipt.getExtension()));
            Files.deleteIfExists(pathToDelete);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
