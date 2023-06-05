package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.model.entity.Curriculum;
import com.ifpb.lattesmaismais.model.entity.Entry;
import com.ifpb.lattesmaismais.model.entity.Receipt;
import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.model.enums.CurriculumStatus;
import com.ifpb.lattesmaismais.model.enums.ReceiptStatus;
import com.ifpb.lattesmaismais.presentation.CurriculumDtoBack;
import com.ifpb.lattesmaismais.presentation.dto.CurriculumDto;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CurriculumConverterServiceTest {

    private static CurriculumConverterService converterService;

    private static Curriculum entity;
    private static Curriculum entity2;
    private static CurriculumDtoBack dtoBack;
    private static User owner;

    private static List<Entry> entriesList;
    private static Entry entry1;
    private static Entry entry2;

    private static GenericsCurriculumService genericsCurriculumService;

    @BeforeAll
    public static void setUp() {
        converterService = new CurriculumConverterService();

        owner = new User();
        owner.setId(1);
        owner.setName("Keilla");

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
        entry2.setStatus(ReceiptStatus.CHECKED_BY_VALIDATOR);
        entry2.setReceipts(receipts);

        entriesList.add(entry1);
        entriesList.add(entry2);

        entity = new Curriculum();
        entity.setId(1);
        entity.setEntries(entriesList);
        entity.setEntryCount(entriesList.size());
        entity.setOwner(owner);
        entity.setDescription("Versão sem comentários");
        entity.setStatus(CurriculumStatus.CHECKED);

        entity2 = new Curriculum();
        entity2.setId(2);
        entity2.setEntries(new ArrayList<>());
        entity2.setEntryCount(0);
        entity2.setOwner(owner);
        entity2.setDescription("Versão sem comentários");
        entity2.setLastModification(LocalDateTime.now());
        entity2.setStatus(CurriculumStatus.UNCHECKED);

        dtoBack = new CurriculumDtoBack();
        dtoBack.setId(1);
        dtoBack.setEntryList(entriesList);
        dtoBack.setEntryCount(entriesList.size());
        dtoBack.setOwnerId(1);
        dtoBack.setDescription("Versão sem comentários");
        dtoBack.setLastModification(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        entity.setLastModification(LocalDateTime.parse(dtoBack.getLastModification(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        genericsCurriculumService = new GenericsCurriculumService();
    }

    @Test
    @Order(1)
    public void testConvertEntityToDto() {
        CurriculumDto dto = converterService.curriculumToDto(entity, genericsCurriculumService);

        assertAll(
                () -> assertEquals(dto.getId(), entity.getId()),
                () -> assertEquals(dto.getEntryList(), entity.getEntries()),
                () -> assertEquals(dto.getEntryCount(), entity.getEntryCount()),
                () -> assertEquals(dto.getOwnerId(), entity.getOwner().getId()),
                () -> assertEquals(dto.getOwnerName(), entity.getOwner().getName()),
                () -> assertEquals(dto.getStatus(), entity.getStatus().name())
        );
    }

    @Test
    @Order(2)
    public void testConvertEntityToDtoException() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> converterService.curriculumToDto((Curriculum) null, null));
        assertTrue(exception.getMessage().startsWith("Erro na conversão Curr -> Dto / Pode ser que algum dos Atributos seja nulo!"));
    }

    @Test
    @Order(3)
    public void testConvertListEntityToDto() {
        List<Curriculum> entityList = new ArrayList<>();
        entityList.add(entity);
        entityList.add(entity2);

        List<CurriculumDto> dtoList = converterService.curriculumToDto(entityList,genericsCurriculumService);

        for (int i = 0; i < entityList.size(); i++) {
            Curriculum entity = entityList.get(i);
            CurriculumDto dto = dtoList.get(i);

            assertAll(
                    () -> assertEquals(dto.getId(), entity.getId()),
                    () -> assertEquals(dto.getEntryList(), entity.getEntries()),
                    () -> assertEquals(dto.getEntryCount(), entity.getEntryCount()),
                    () -> assertEquals(dto.getOwnerId(), entity.getOwner().getId()),
                    () -> assertEquals(dto.getOwnerName(), entity.getOwner().getName()),
                    () -> assertEquals(dto.getStatus(), entity.getStatus().name())
            );
        }
    }

    @Test
    @Order(4)
    public void testConvertListEntityToDtoException() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> converterService.curriculumToDto((List<Curriculum>) null, null));
        assertTrue(exception.getMessage().startsWith("Erro na conversão Curr -> Dto / Pode ser que algum dos Atributos seja nulo!"));
    }

    @Test
    @Order(5)
    public void testDtoBackToCurriculumValid() {
        Curriculum curriculum = converterService.dtoBackToCurriculum(dtoBack, owner, genericsCurriculumService, true);

        assertAll(
                () -> assertEquals(curriculum.getId(), entity.getId()),
                () -> assertEquals(curriculum.getEntryCount(), entity.getEntryCount()),
                () -> assertTrue(curriculum.getLastModification().toString().startsWith(entity.getLastModification().toString())),
                () -> assertEquals(curriculum.getOwner(), entity.getOwner()),
                () -> assertEquals(curriculum.getStatus(), entity.getStatus())
        );

        for (int i = 0; i < curriculum.getEntries().size(); i++) {
            Entry entityEntry = entity.getEntries().get(i);
            Entry curriculumEntry = curriculum.getEntries().get(i);

            assertAll(
                    () -> assertEquals(entityEntry.getId(), curriculumEntry.getId()),
                    () -> assertEquals(entityEntry.getName(), curriculumEntry.getName()),
                    () -> assertEquals(entityEntry.getGroup(), curriculumEntry.getGroup())
            );
        }
    }

    @Test
    @Order(6)
    public void testDtoBackToCurriculumInvalid() {
        dtoBack.setEntryList(null);
        dtoBack.setId(null);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> converterService.dtoBackToCurriculum(dtoBack, owner, genericsCurriculumService, true));
        assertTrue(exception.getMessage().startsWith("Erro na conversão Dto -> Curr / Pode ser que algum dos Atributos seja nulo!"));
    }
}
