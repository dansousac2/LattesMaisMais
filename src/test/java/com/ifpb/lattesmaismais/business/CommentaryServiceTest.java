package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.model.entity.Receipt;
import com.ifpb.lattesmaismais.model.entity.ValidatorCommentary;
import com.ifpb.lattesmaismais.model.repository.CommentaryRepository;
import com.ifpb.lattesmaismais.presentation.dto.ValidatorCommentaryDto;
import com.ifpb.lattesmaismais.presentation.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class CommentaryServiceTest {

    @Mock
    private CommentaryRepository repository;

    @InjectMocks
    @Spy
    private CommentaryService service;

    private static ValidatorCommentary entity;

    @BeforeAll
    public static void setUp() {
        entity = new ValidatorCommentary();
        entity.setId(1);
        entity.setValidatorId(1);
        entity.setValidatorName("Keilla");
        entity.setCommentary("OK");
        entity.setDate(LocalDate.now());
    }

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "repository", repository);
    }

    @Test
    public void testFindByIdOk() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(entity));

        assertDoesNotThrow(() -> service.findById(1));

        verify(repository).findById(anyInt());
    }

    @Test
    public void testFindByIdException() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> service.findById(1));
        assertEquals("Comentário não encontrado para id: 1", exception.getMessage());
    }

    @Test
    public void testSaveOk() {
        when(repository.save(any(ValidatorCommentary.class))).thenReturn(entity);

        assertDoesNotThrow(() -> service.save(entity));
    }


}
