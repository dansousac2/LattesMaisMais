package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.model.entity.Curriculum;
import com.ifpb.lattesmaismais.model.entity.Entry;
import com.ifpb.lattesmaismais.model.entity.Receipt;
import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.model.enums.CurriculumStatus;
import com.ifpb.lattesmaismais.model.enums.ReceiptStatus;
import com.ifpb.lattesmaismais.model.repository.CurriculumRepository;
import com.ifpb.lattesmaismais.presentation.CurriculumDtoBack;
import com.ifpb.lattesmaismais.presentation.exception.ObjectNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenericsCurriculumServiceTest {

    @Mock
    private CurriculumRepository repository;

    @InjectMocks
    private CurriculumService service;

    private GenericsCurriculumService genericsCurriculumService = new GenericsCurriculumService();

    private static Curriculum curriculumOk;
    private static Curriculum curriculumInvalid;
    private static CurriculumDtoBack curriculumDto;
    private static User userOk;

    private static List<Entry> entriesList;
    private static Entry entry1;
    private static Entry entry2;

    @BeforeAll
    static void setUp() {
        curriculumDto = new CurriculumDtoBack();
        curriculumDto.setId(1);
        curriculumDto.setEntryCount(10);
        curriculumDto.setDescription("Sem comentários");
        curriculumDto.setOwnerId(2);
        curriculumDto.setLastModification("10/05/2023 - 18:30:00");

        userOk = new User();
        userOk.setId(2);

        curriculumOk = new Curriculum();
        curriculumOk.setId(1);
        curriculumOk.setEntryCount(10);
        curriculumOk.setStatus(CurriculumStatus.CHECKED);
        curriculumOk.setVersion("V_10052023_183000");
        curriculumOk.setDescription("Sem comentários");
        curriculumOk.setOwner(userOk);
        curriculumOk.setLastModification(LocalDateTime.of(2023, 05, 10, 18, 30));

        User userInvalid = new User();
        userInvalid.setId(100);

        curriculumInvalid = new Curriculum();
        curriculumInvalid.setId(1);
        curriculumInvalid.setEntryCount(10);
        curriculumInvalid.setStatus(CurriculumStatus.CHECKED);
        curriculumInvalid.setVersion("V_10052023_183000");
        curriculumInvalid.setDescription("Sem comentários");
        curriculumInvalid.setOwner(userInvalid);
        curriculumInvalid.setLastModification(LocalDateTime.now());

        entriesList = new ArrayList<>();

        entry1 = new Entry();
        entry1.setId(1);
        entry1.setName("Licenciatura em Letras");
        entry1.setGroup("Graduação");
        entry1.setStatus(ReceiptStatus.CHECKED_BY_VALIDATOR);

        Receipt receipt1 = new Receipt();
        receipt1.setId(1);
        receipt1.setName("certificado");
        receipt1.setExtension(".pdf");
        receipt1.setStatus(ReceiptStatus.CHECKED_BY_VALIDATOR);

        List<Receipt> receipts = new ArrayList<>();
        receipts.add(receipt1);
        entry1.setReceipts(receipts);

        entry2 = new Entry();
        entry2.setId(2);
        entry2.setName("Licenciatura em Matemática");
        entry2.setGroup("Graduação");
        entry2.setStatus(ReceiptStatus.WITHOUT_RECEIPT);

        List<Receipt> receipts2 = new ArrayList<>();
        entry2.setReceipts(receipts2);
    }

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(service, "repository", repository);
    }

    @Test
    @Order(1)
    void testCreateVersionNameOk() {
        String versionName = genericsCurriculumService.createVersionName();
        String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss"));

        assertTrue(versionName.equals("V_" + timeNow));
    }

    @Test
    @Order(2)
    void testDateTimeToStringOk() {
        LocalDateTime lastModification = LocalDateTime.of(2023, 05, 10, 18, 30);
        String localDateTimeString = genericsCurriculumService.dateTimeToString(lastModification);

        assertEquals("10/05/2023 - 18:30:00", localDateTimeString);
    }

    @Test
    @Order(3)
    void testVerifyCurriculumDtoOk() {
        when(repository.existsById(anyInt())).thenReturn(true);
        when(repository.findById(anyInt())).thenReturn(Optional.of(curriculumOk));

        try {
            User user = genericsCurriculumService.verifyCurriculumDto(curriculumDto, service);
            assertEquals(user.getId(), userOk.getId());

            verify(repository).findById(anyInt());
            verify(repository).existsById(anyInt());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    @Order(4)
    void testVerifyCurriculumDtoIllegalArgumentException() {
        when(repository.existsById(anyInt())).thenReturn(true);
        when(repository.findById(anyInt())).thenReturn(Optional.of(curriculumInvalid));

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> genericsCurriculumService.verifyCurriculumDto(curriculumDto, service));

        assertEquals(exception.getMessage(), "Currículo não pertencente ao usuário logado!");
    }

    @Test
    @Order(5)
    void testVerifyCurriculumDtoObjectNotFoundException() {
        when(repository.existsById(anyInt())).thenReturn(false);

        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> genericsCurriculumService.verifyCurriculumDto(curriculumDto, service));

        assertEquals(exception.getMessage(), "Currículo com id 1 não encontrado!");
    }

    @Test
    @Order(6)
    void testOrganizeEntriesReceiptsAndStatusChecked() {
        List<Entry> entries = new ArrayList<>();
        // Passando uma entry válida com receipt e status CHECKED_BY_VALIDATOR
        entries.add(entry1);

        CurriculumStatus status = genericsCurriculumService.organizeEntriesReceiptsAndStatus(entries);
        assertEquals(CurriculumStatus.CHECKED, status);
    }

    @Test
    @Order(7)
    void testOrganizeEntriesReceiptsAndStatusUnchecked() {
        List<Entry> entries = new ArrayList<>();
        // Passando uma entry válida sem receipts
        entries.add(entry2);

        CurriculumStatus status = genericsCurriculumService.organizeEntriesReceiptsAndStatus(entries);
        assertEquals(CurriculumStatus.UNCHECKED, status);
    }

    @Test
    @Order(8)
    void testGenerateStatusCurriculumOnlyChecked() {
        List<Entry> entries = new ArrayList<>();
        // Passando uma entry válida com receipt e status CHECKED_BY_VALIDATOR
        entries.add(entry1);

        CurriculumStatus status = genericsCurriculumService.generateStatusCurriculumOnly(entries);
        assertEquals(CurriculumStatus.CHECKED, status);
    }

    @Test
    @Order(9)
    void testGenerateStatusCurriculumOnlyUnchecked() {
        List<Entry> entries = new ArrayList<>();
        // Passando uma entry válida sem receipts
        entries.add(entry2);

        CurriculumStatus status = genericsCurriculumService.generateStatusCurriculumOnly(entries);
        assertEquals(CurriculumStatus.UNCHECKED, status);
    }
}
