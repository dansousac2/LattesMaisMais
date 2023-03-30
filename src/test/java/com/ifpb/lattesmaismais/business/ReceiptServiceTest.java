package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.model.entity.Receipt;
import com.ifpb.lattesmaismais.model.repository.ReceiptRepository;
import com.ifpb.lattesmaismais.presentation.exception.ObjectNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ReceiptServiceTest {

    @Mock
    private ReceiptRepository repository;

    @InjectMocks
    @Spy
    private ReceiptService service;

    private static Receipt entity;

    @BeforeAll
    public static void setUp() {
        entity = new Receipt();

        entity.setId(1);
        entity.setName("teste");
        entity.setExtension(".pdf");
    }

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "receiptRepository", repository);
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
        assertEquals("Não foi possível encontrar comprovante com id 1", exception.getMessage());

        verify(repository).existsById(anyInt());
        verify(repository, times(0)).findById(anyInt());
    }

    @Test
    public void testSaveOk() {
        when(repository.save(any(Receipt.class))).thenReturn(entity);

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
        assertEquals("Não foi possível encontrar comprovante com id 1", exception.getMessage());

        verify(repository).existsById(anyInt());
        verify(repository, times(0)).deleteById(anyInt());
    }
}
