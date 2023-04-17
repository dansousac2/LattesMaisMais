package com.ifpb.lattesmaismais.business;


import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.model.enums.AccountStatus;
import com.ifpb.lattesmaismais.presentation.dto.UserDtoBack;
import com.ifpb.lattesmaismais.presentation.dto.UserDtoFront;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;


public class UserConverterServiceTest {

    @Spy
    private RoleServiceImpl roleService;

    @Spy
    @InjectMocks
    private UserConverterService userConverterService;

    private static User user;

    private static UserDtoBack userDtoBack;
    @BeforeAll
    public static void setUp() {
        user = new User();

        userDtoBack = new UserDtoBack();
        userDtoBack.setName("Keilla");
        userDtoBack.setEmail("keilla@gmail.com");
        userDtoBack.setPassword("123456");
    }

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(userConverterService, "roleService", roleService);

        user.setId(1);
        user.setName("Keilla");
        user.setEmail("keilla@gmail.com");
        user.setStatus(AccountStatus.ACTIVE);
        user.setPassword("123456");
    }

    @Test
    public void testUserToDtoFrontOk() {
        UserDtoFront dtoFront = assertDoesNotThrow(() -> userConverterService.userToDtoFront(user));

        assertAll(
                () -> assertEquals(dtoFront.getId(), user.getId()),
                () -> assertEquals(dtoFront.getName(), user.getName()),
                () -> assertEquals(dtoFront.getEmail(), user.getEmail())
        );
    }

    @Test
    public void testUserToDtoFrontException() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> userConverterService.userToDtoFront(null));
        assertEquals("Erro durante conversão / Usuário --> DtoFront : usuário ou atributo de usuário nulo!", exception.getMessage());
    }

    // TODO: Corrigir
    @Test
    public void testDtoBackToUser() {
        User convertedEntity = userConverterService.dtoBackToUser(userDtoBack);

        assertAll(
                () -> assertEquals(convertedEntity.getName(), user.getName()),
                () -> assertEquals(convertedEntity.getEmail(), user.getEmail()),
                () -> assertEquals(convertedEntity.getPassword(), user.getPassword())
        );
    }

    @Test
    public void testDtoBackToUserException() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> userConverterService.dtoBackToUser(null));
        assertEquals("Erro durante conversão / DtoBack --> User : Dto ou atributo de Dto nulo!", exception.getMessage());
    }
}
