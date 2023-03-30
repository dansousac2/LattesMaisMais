package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.presentation.exception.FileConversionException;
import com.ifpb.lattesmaismais.model.entity.Receipt;

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

import static org.junit.jupiter.api.Assertions.fail;

public class FileUploadServiceTest {

    private static final String pathReadFile = "C:\\Users\\Public\\Documents\\teste.jpg";

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

   @BeforeAll
   public static void setUp() {
       FileConverterService fileConverter = new FileConverterService();
       try {
           byte[] fileData = fileConverter.readFile(pathReadFile);
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

        ReflectionTestUtils.setField(uploadService, "fileConverterService", fileConverterService);
        ReflectionTestUtils.setField(uploadService, "fileEncryptionService", encryptionService);
        ReflectionTestUtils.setField(uploadService, "receiptService", receiptService);
        ReflectionTestUtils.setField(uploadService, "converterService", converterService);
    }

    @Test
    public void testUploadFileOk() {
//        try {
//
//            when(converterService.fileToEntity(any(MultipartFile.class))).thenReturn(receipt);
//            when(receiptService.save(any(Receipt.class))).thenReturn(receipt);
//            when(encryptionService.encryptData(any())).thenReturn(multipartFile.getBytes());
//            doCallRealMethod().when(fileConverterService).writeFile(any(), any());
//            doCallRealMethod().when(uploadService).createDiretory(anyString());
//
//            uploadService.uploadFile(multipartFile, "1");
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            fail();
//        }
    }
}
