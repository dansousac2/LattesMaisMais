package com.ifpb.lattesmaismais.presentation.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserDtoFrontTest {

    private UserDtoFront dto;

    private Set<ConstraintViolation<UserDtoFront>> violations;

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        dto = new UserDtoFront();
        violations = new HashSet<>();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 12, 100, 9999, 2147483647})
    void testUserIdValid(int id) {
        dto.setId(id);

        violations = validator.validateProperty(dto, "id");


        assertEquals(0, violations.size(), "Valor não permitido: " + id);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3})
    void testUserIdInvalid(int id) {
        dto.setId(id);

        violations = validator.validateProperty(dto, "id");

        assertAll("Testando id do usuário",
                () -> assertEquals(1, violations.size(), "id não permitido encontrado: " + id),
                () -> violations.forEach(v -> {
                    assertTrue(v.getMessage().equals("O id do usuário deve ser um valor positivo!"));
                })
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"Keilla", "Fulano de Tal", "Ayanne", "Aparecido Fulano da Silva Conceição "})
    void testNameValid(String name) {
        dto.setName(name);
        violations = validator.validateProperty(dto, "name");

        assertEquals(0, violations.size(), "Valor não permitido: " + name);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "       ", "\n  ", " \n\n ", "\n \n"})
    void testNameInvalid(String name) {
        dto.setName(name);
        violations = validator.validateProperty(dto, "name");

        assertAll("Testando nome de usuário",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + name),
                () -> assertEquals("O nome do usuário não pode ser nulo!", violations.stream().findFirst().get().getMessage())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"a@gmail.com", "fulanotteste@hotmail.com", "teste@gmail.com"})
    void testEmailValid(String email) {
        dto.setEmail(email);
        violations = validator.validateProperty(dto, "email");

        assertEquals(0, violations.size(), "Valor não permitido: " + email);
    }

    @ParameterizedTest
    @ValueSource(strings = {"       ", "fulanodetal", "@gmail.com", "testegmail.com"})
    void testEmailInvalid(String email) {
        dto.setEmail(email);
        violations = validator.validateProperty(dto, "email");

        assertAll("Testando email",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + email),
                () -> assertEquals("O email deve ser válido!", violations.stream().findFirst().get().getMessage())
        );
    }

}
