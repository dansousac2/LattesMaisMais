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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDtoBackTest {

    private UserDtoBack dto;

    private Set<ConstraintViolation<UserDtoBack>> violations;

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        dto = new UserDtoBack();
        violations = new HashSet<>();
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

    @ParameterizedTest
    @ValueSource(strings = {"teste123456", "12345678", "fulanodetal1999", "senhasegura"})
    void testPasswordValid(String password) {
        dto.setPassword(password);
        violations = validator.validateProperty(dto, "password");

        assertEquals(0, violations.size(), "Valor não permitido: " + password);
    }

    @ParameterizedTest
    @ValueSource(strings = {"          ", "teste 12345", "123456", "s e n h a"})
    void testPasswordInvalid(String password) {
        dto.setPassword(password);
        violations = validator.validateProperty(dto, "password");

        assertAll("Testando a senha",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + password),
                () -> assertEquals("A senha não deve conter espaços e deve ter no mínimo 8 dígitos!", violations.stream().findFirst().get().getMessage())
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
                () -> assertEquals("O nome de usuário não pode ser nulo!", violations.stream().findFirst().get().getMessage())
        );
    }
}
