package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.model.entity.ValidatorCommentary;
import com.ifpb.lattesmaismais.presentation.dto.ValidatorCommentaryDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentaryConverterServiceTest {

    private static CommentaryConverterService converterService;

    private static ValidatorCommentary entity;

    private static ValidatorCommentaryDto dto;

    @BeforeAll
    public static void setUp() {
        converterService = new CommentaryConverterService();

        entity = new ValidatorCommentary();
        entity.setId(1);
        entity.setValidatorId(1);
        entity.setValidatorName("Keilla");
        entity.setCommentary("OK");
        entity.setDate(LocalDate.now());

        dto = new ValidatorCommentaryDto();
        dto.setValidatorId(1);
        dto.setValidatorName("Keilla");
        dto.setCommentary("OK");
    }

    @Test
    void testDtoToCommentaryValid() {
        ValidatorCommentary convertedEntity = converterService.dtoToCommentary(dto);

        assertAll(
                () -> assertEquals(convertedEntity.getValidatorId(), entity.getValidatorId()),
                () -> assertEquals(convertedEntity.getValidatorName(), entity.getValidatorName()),
                () -> assertEquals(convertedEntity.getCommentary(), entity.getCommentary())
        );
    }
}
