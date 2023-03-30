package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.model.entity.Curriculum;
import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.model.enums.CurriculumStatus;
import com.ifpb.lattesmaismais.model.repository.CurriculumRepository;
import com.ifpb.lattesmaismais.presentation.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Optional;


class CurriculumServiceTest {

    @Mock
    private CurriculumRepository repository;

    @InjectMocks
    @Spy
    private CurriculumService service;

    private static Curriculum entity;

    @BeforeEach
    public  void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "curriculumRepository", repository);

        User owner = new User();
        owner.setId(1);
        owner.setName("Keilla");

        entity = new Curriculum();
        entity.setId(1);
        entity.setEntries(new ArrayList<>());
        entity.setEntryCount(0);
        entity.setOwner(owner);
        entity.setStatus(CurriculumStatus.UNCHECKED);
    }

    @Test
    public void testFindByIdOk() {
        when(repository.existsById(anyInt())).thenReturn(true);
        when(repository.findById(anyInt())).thenReturn(Optional.of(entity));

        assertDoesNotThrow(() -> service.findById(1));

        verify(repository).existsById(anyInt());
        verify(repository).findById(anyInt());
    }

    @Test
    public void testFindByIdException() {
        when(repository.existsById(anyInt())).thenReturn(false);

        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.findById(1));
        assertEquals("Currículo com id 1 não encontrado!", exception.getMessage());

        verify(repository).existsById(anyInt());
        verify(repository, times(0)).findById(anyInt());
    }

    @Test
    public void testSaveOk() {
        when(repository.save(any(Curriculum.class))).thenReturn(entity);

        assertDoesNotThrow(() -> service.save(entity));
    }

    @Test
    public void testDeleteByIdOk() {
        when(repository.existsById(anyInt())).thenReturn(true);

        assertDoesNotThrow(() -> service.deleteById(1));

        verify(repository).existsById(anyInt());
        verify(repository).deleteById(anyInt());
    }

    @Test
    public void testDeleteByIdException() {
        when(repository.existsById(anyInt())).thenReturn(false);

        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.deleteById(1));
        assertEquals("Currículo com id 1 não encontrado!", exception.getMessage());

        verify(repository).existsById(anyInt());
        verify(repository, times(0)).deleteById(anyInt());
    }
}
