package com.ifpb.lattesmaismais.presentation;

import com.ifpb.lattesmaismais.business.*;
import com.ifpb.lattesmaismais.model.entity.Curriculum;
import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.model.enums.CurriculumStatus;
import com.ifpb.lattesmaismais.presentation.FileUploadController;
import com.ifpb.lattesmaismais.presentation.exception.FileConversionException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FileUploadControllerTest {

    private static final String pathReadFile = "C:\\Users\\Public\\Documents\\teste.txt";

    private static final String pathReadCurriculum = "C:\\Users\\Public\\Documents\\teste.xml";

    private static MultipartFile multipartFile;

    private static MultipartFile multipartCurriculum;

    @Mock
    private FileUploadService uploadService;

    @Mock
    private HashService hashService;

    @Mock
    private CurriculumXmlParseService curriculumXmlParseService;

    @Mock
    private CurriculumConverterService curriculumConverterService;

    @InjectMocks
    @Spy
    private FileUploadController uploadController;

    private static final FileConverterService setupFileConverter = new FileConverterService();

    private static Curriculum curriculum;

    @BeforeAll
    public static void setUp() {
        try {
            Path pathFile = Path.of(pathReadFile);
            Path pathCurriculum = Path.of(pathReadCurriculum);

            if (!Files.exists(pathFile)) {
                Files.createFile(pathFile);
                Files.write(pathFile, "Isso é um teste".getBytes());
            }

            if (!Files.exists(pathCurriculum)) {
                Files.createFile(pathCurriculum);
            }

            byte[] fileData = setupFileConverter.readFile(pathReadFile);
            multipartFile = new MockMultipartFile("teste", "teste.jpg", ".jpg", fileData);

            byte[] curriculumData = setupFileConverter.readFile(pathReadCurriculum);
            multipartCurriculum = new MockMultipartFile("teste", "teste.xml", ".xml", curriculumData);

            User owner = new User();
            owner.setId(1);
            owner.setName("Keilla");

            curriculum = new Curriculum();
            curriculum.setId(1);
            curriculum.setEntries(new ArrayList<>());
            curriculum.setEntryCount(0);
            curriculum.setOwner(owner);
            curriculum.setStatus(CurriculumStatus.UNCHECKED);

        } catch (Exception e) {
            fail();
        }
    }

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(uploadController, "uploadService", uploadService);
        ReflectionTestUtils.setField(uploadController, "hashService", hashService);
        ReflectionTestUtils.setField(uploadController, "cXmlParseService", curriculumXmlParseService);
        ReflectionTestUtils.setField(uploadController, "curriculumConverterService", curriculumConverterService);
    }

    @Test
    public void testUploadFileOk() {
        try {
            when(hashService.hashingSHA256(anyString())).thenCallRealMethod();
            doNothing().when(uploadService).uploadFile(any(), any());

            assertDoesNotThrow(() -> uploadController.uploadFile(multipartFile, "teste"));

            ResponseEntity response = uploadController.uploadFile(multipartFile, "teste");
            assertEquals(HttpStatus.CREATED, response.getStatusCode());

            verify(hashService, times(2)).hashingSHA256(anyString());
            verify(uploadService, times(2)).uploadFile(any(), any());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testUploadFileException() {
        try {
            doThrow(FileConversionException.class).when(uploadService).uploadFile(any(), any());
            when(hashService.hashingSHA256(anyString())).thenCallRealMethod();

            ResponseEntity response = uploadController.uploadFile(multipartFile, "teste");
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

            verify(hashService).hashingSHA256(anyString());
            verify(uploadService).uploadFile(any(), any());

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testUploadCurriculumOk() {
        try {
            when(hashService.hashingSHA256(anyString())).thenCallRealMethod();
            doNothing().when(uploadService).uploadCurriculum(any(), any());
            when(curriculumXmlParseService.xmlToCurriculum(anyString())).thenReturn(curriculum);
            doCallRealMethod().when(curriculumConverterService).curriculumToDto(any(Curriculum.class));

            assertDoesNotThrow(() -> uploadController.uploadCurriculum(multipartCurriculum, "teste"));

            ResponseEntity response = uploadController.uploadCurriculum(multipartCurriculum, "teste");
            assertEquals(HttpStatus.CREATED, response.getStatusCode());

            verify(hashService, times(2)).hashingSHA256(anyString());
            verify(uploadService, times(2)).uploadCurriculum(any(), any());
            verify(curriculumXmlParseService, times(2)).xmlToCurriculum(anyString());
            verify(curriculumConverterService, times(2)).curriculumToDto(any(Curriculum.class));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testUploadCurriculumConversionException() {
        try {
            when(hashService.hashingSHA256(anyString())).thenCallRealMethod();
            doThrow(FileConversionException.class).when(uploadService).uploadCurriculum(any(), any());
            when(curriculumXmlParseService.xmlToCurriculum(anyString())).thenReturn(curriculum);
            doCallRealMethod().when(curriculumConverterService).curriculumToDto(any(Curriculum.class));

            ResponseEntity response = uploadController.uploadCurriculum(multipartFile, "teste");
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

            verify(hashService).hashingSHA256(anyString());
            verify(uploadService).uploadCurriculum(any(), any());
            verify(curriculumXmlParseService, times(0)).xmlToCurriculum(anyString());
            verify(curriculumConverterService, times(0)).curriculumToDto(any(Curriculum.class));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testUploadCurriculumIllegalArgumentException() {
        try {
            when(hashService.hashingSHA256(anyString())).thenCallRealMethod();
            doNothing().when(uploadService).uploadCurriculum(any(), any());
            when(curriculumXmlParseService.xmlToCurriculum(anyString())).thenReturn(null);
            doCallRealMethod().when(curriculumConverterService).curriculumToDto(any());

            ResponseEntity response = uploadController.uploadCurriculum(null, "teste");
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertTrue(response.getBody().toString().startsWith("Erro na conversão Curr -> Dto / Pode ser que algum dos Atributos seja nulo:"));

            verify(hashService).hashingSHA256(anyString());
            verify(uploadService).uploadCurriculum(any(), any());
            verify(curriculumXmlParseService).xmlToCurriculum(anyString());
            verify(curriculumConverterService).curriculumToDto(any());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testReadFileOk() {
        try {
            when(hashService.hashingSHA256(anyString())).thenCallRealMethod();
            doNothing().when(uploadService).readFile(any(), any());

            assertDoesNotThrow(() -> uploadController.readFile(multipartFile.getOriginalFilename(), "teste"));

            ResponseEntity response = uploadController.readFile(multipartFile.getOriginalFilename(), "teste");
            assertEquals(HttpStatus.OK, response.getStatusCode());

            verify(hashService, times(2)).hashingSHA256(anyString());
            verify(uploadService, times(2)).readFile(any(), any());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testReadFileNotFoundException() {
        try {
            when(hashService.hashingSHA256(anyString())).thenCallRealMethod();
            doCallRealMethod().when(uploadService).readFile(any(), any());

            assertThrows(FileNotFoundException.class, () -> uploadService.readFile(any(), any()));

            ResponseEntity response = uploadController.readFile("arquivoinexistente.jpg", "teste");
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

            verify(hashService).hashingSHA256(anyString());
            verify(uploadService, times(2)).readFile(any(), any());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testReadFileConversionException() {
        try {
            when(hashService.hashingSHA256(anyString())).thenCallRealMethod();
            doThrow(FileConversionException.class).when(uploadService).readFile(any(), any());

            ResponseEntity response = uploadController.readFile(multipartFile.getOriginalFilename(), "teste");
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

            verify(hashService).hashingSHA256(anyString());
            verify(uploadService).readFile(any(), any());
        } catch (Exception e) {
            fail();
        }
    }

    @AfterAll
    public static void tearDown() {
        try {
            System.out.println("Deleting created files");
            Files.deleteIfExists(Path.of(pathReadFile));
            Files.deleteIfExists(Path.of(pathReadCurriculum));
        } catch (IOException e) {
            fail();
        }
    }
}
