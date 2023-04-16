package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.model.entity.SolicitedScheduling;
import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.model.enums.SchedulingStatus;
import com.ifpb.lattesmaismais.model.repository.SolicitedSchedulingRepository;
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
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class SolicitedSchedulingServiceTest {

    @Mock
    private static SolicitedSchedulingRepository repository;

    @Spy
    @InjectMocks
    private static SolicitedSchedulingService service;

    private static User requester;

    private static User validator;

    private static SolicitedScheduling entity;

    @BeforeAll
    public static void setUp() {
        requester = new User();
        requester.setId(1);
        requester.setName("Keilla");
        requester.setEmail("keilla@gmail.com");

        validator = new User();
        validator.setId(2);
        validator.setName("Ayanne");
        validator.setEmail("ayanne@gmail.com");

        entity = new SolicitedScheduling();
        entity.setId(1);
        entity.setDate(LocalDate.now());
        entity.setTime(LocalTime.now());
        entity.setAddress("Rua das batatas");
        entity.setVersion("V_202304072000");
        entity.setRequester(requester);
        entity.setValidator(validator);
        entity.setStatus(SchedulingStatus.OPEN);
    }

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "schedulingRepository", repository);
    }


    @Test
    public void testFindByIdOk() {
        when(repository.existsById(any())).thenReturn(true);
        when(repository.findById(any())).thenReturn(Optional.ofNullable(entity));

        assertDoesNotThrow(() -> service.findById(1));
        verify(repository).findById(any());
    }

    @Test
    public void testFindByIdInvalid() {
        when(repository.existsById(any())).thenReturn(false);

        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.findById(1));
        assertEquals("Não foi possível encontrar SolicitedScheduling com id 1", exception.getMessage());

        verify(repository).existsById(anyInt());
        verify(repository, times(0)).findById(anyInt());
    }

    @Test
    public void testSave() {
        when(repository.save(any(SolicitedScheduling.class))).thenReturn(entity);

        assertDoesNotThrow(() -> service.save(entity));
    }

    @Test
    public void deleteByIdOk() {
        when(repository.existsById(anyInt())).thenReturn(true);

        assertDoesNotThrow(() -> service.deleteById(1));

        verify(repository).existsById(anyInt());
        verify(repository).deleteById(anyInt());
    }

    @Test
    public void deleteByIdInvalid() {
        when(repository.existsById(anyInt())).thenReturn(false);

        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.deleteById(1));
        assertEquals("Não foi possível encontrar SolicitedScheduling com id 1", exception.getMessage());

        verify(repository).existsById(anyInt());
        verify(repository, times(0)).deleteById(anyInt());
    }
}
