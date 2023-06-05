package com.ifpb.lattesmaismais.presentation;

import com.ifpb.lattesmaismais.business.CommentaryConverterService;
import com.ifpb.lattesmaismais.business.CommentaryService;
import com.ifpb.lattesmaismais.model.entity.ValidatorCommentary;
import com.ifpb.lattesmaismais.presentation.dto.ValidatorCommentaryDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ValidatorCommentaryControllerTest {

    @Mock
    private CommentaryConverterService converterService;

    @Mock
    private CommentaryService service;

    @InjectMocks
    @Spy
    private ValidatorCommentaryController controller;

    private static ValidatorCommentary entity;

    private static ValidatorCommentaryDto dto;

    @BeforeAll
    public static void setUp() {
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

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(controller, "converter", converterService);
        ReflectionTestUtils.setField(controller, "service", service);
    }

    @Test
    void testSaveValid() {
        when(converterService.dtoToCommentary(any())).thenCallRealMethod();
        when(service.save(any())).thenReturn(entity);

        ResponseEntity response = assertDoesNotThrow(() -> controller.save(dto));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testSaveInvalid() {
        when(converterService.dtoToCommentary(any())).thenCallRealMethod();
        when(service.save(any())).thenCallRealMethod();

        ResponseEntity response = controller.save(dto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
