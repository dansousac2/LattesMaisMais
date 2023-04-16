package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.model.repository.UserRepository;
import com.ifpb.lattesmaismais.presentation.exception.ObjectNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Spy
    @InjectMocks
    private UserService service;

    private static User user;

    @BeforeAll
    public static void setUp() {
        user = new User();
        user.setId(1);
        user.setName("Keilla");
        user.setEmail("keilla@gmail.com");
    }

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "repository", repository);
    }

    @Test
    public void testFindByIdOk() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> service.findById(user.getId()));

        verify(repository).findById(anyInt());
    }

    @Test
    public void testFindByIdException() {
        assertThrows(ObjectNotFoundException.class, () -> service.findById(9999));
    }

    @Test
    public void testFindByEmailOk() {
        when(repository.findByEmail(any())).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> service.findByEmail(user.getEmail()));

        verify(repository).findByEmail(any());
    }

    @Test
    public void testFindByEmailException() {
        assertThrows(ObjectNotFoundException.class, () -> service.findByEmail("emailerrado@gmail.com"));
    }

    @Test
    public void testLoadUserByUsernameOk() {
        when(repository.findByEmail(any())).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> service.loadUserByUsername(user.getEmail()));

        verify(repository).findByEmail(any());
    }

    @Test
    public void testLoadUserByUsernameException() {
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("emailerrado@gmail.com"));
    }

}
