package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.model.entity.User;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;


class CurriculumXmlParseServiceTest {

    @Mock
    private HashService hashService;

    @Mock
    private UserService userService;

    @Mock
    private CurriculumService curriculumService;

    @InjectMocks
    @Spy
    private CurriculumXmlParseService parseService;

    private static final HashService setupHashService = new HashService();

    private static final FileConverterService setupFileConverterService = new FileConverterService();

    private static User owner;


    private static final String path = "C:\\Users\\Public\\Documents";

    // Path para arquivo XML
    // System.getProperty("user.dir") -> Pegando o diretório raíz do projeto
    private static final String xmlPath = System.getProperty("user.dir") + "\\src\\test\\java\\com\\ifpb\\lattesmaismais\\util\\teste.xml";

    @BeforeAll
    public static void setUp() {
        owner = new User();
        owner.setId(1);
        owner.setName("Keilla");

        // Salvando arquivo xml para teste
        try {
            byte[] curriculumData = setupFileConverterService.readFile(xmlPath);

           String hashUserId = setupHashService.hashingSHA256(String.valueOf(owner.getId()));

           // Criando diretório com hashUserId
           String pathToSave = path + "\\" + hashUserId;
            if(!new File(pathToSave).exists()) {
                new File(pathToSave).mkdir();
            }

            setupFileConverterService.writeFile(pathToSave + "\\curriculum.xml", curriculumData);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(parseService, "hashService", hashService);
        ReflectionTestUtils.setField(parseService, "userService", userService);
        ReflectionTestUtils.setField(parseService, "curriculumService", curriculumService);
        ReflectionTestUtils.setField(parseService, "pathXmlCurriculum", path);
    }

    @Test
    public void testXmlToCurriculum() {
        try {
            doCallRealMethod().when(hashService).hashingSHA256(any());
            when(userService.findById(any())).thenReturn(owner);

            assertDoesNotThrow(() -> parseService.xmlToCurriculum(String.valueOf(owner.getId())));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testXmlToCurriculumException() {
        try {
            doCallRealMethod().when(hashService).hashingSHA256(any());
            when(userService.findById(any())).thenReturn(owner);

            // Passando id inválido:
            assertThrows(FileNotFoundException.class, () -> parseService.xmlToCurriculum(String.valueOf(owner.getId() + 100)));

        } catch (Exception e) {
            fail();
        }
    }


    @AfterAll
    public static void tearDown() {
        try {
            System.out.println("Deleting created files");

            String hashUserId = setupHashService.hashingSHA256(String.valueOf(owner.getId()));
            String pathToSave = path + "\\" + hashUserId;

            Files.deleteIfExists(Path.of(pathToSave + "\\curriculum.xml"));
            Files.deleteIfExists(Path.of(pathToSave));
        } catch (Exception e) {
            fail();
        }
    }
}
