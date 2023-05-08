package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.model.entity.Curriculum;
import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.model.enums.CurriculumStatus;
import com.ifpb.lattesmaismais.presentation.dto.CurriculumDto;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CurriculumConverterServiceTest {

    private static CurriculumConverterService converterService;

    private static Curriculum entity;

    private static GenericsCurriculumService genericsCurriculumService;

    @BeforeAll
    public static void setUp() {
        converterService = new CurriculumConverterService();

        User owner = new User();
        owner.setId(1);
        owner.setName("Keilla");

        entity = new Curriculum();
        entity.setId(1);
        entity.setEntries(new ArrayList<>());
        entity.setEntryCount(0);
        entity.setOwner(owner);
        entity.setDescription("Versão sem comentários");
        entity.setLastModification(LocalDateTime.now());
        entity.setStatus(CurriculumStatus.UNCHECKED);
        entity.setVersion("V_20230508");

        genericsCurriculumService = new GenericsCurriculumService();
    }

    @Test
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
    public void testConvertEntityToDtoException() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> converterService.curriculumToDto(null, null));
        assertTrue(exception.getMessage().startsWith("Erro na conversão Curr -> Dto / Pode ser que algum dos Atributos seja nulo!"));
    }
}
