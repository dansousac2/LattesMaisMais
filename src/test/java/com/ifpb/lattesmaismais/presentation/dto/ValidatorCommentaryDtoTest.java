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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidatorCommentaryDtoTest {

    private ValidatorCommentaryDto dto;
    private Set<ConstraintViolation<ValidatorCommentaryDto>> violations;

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        dto = new ValidatorCommentaryDto();
        violations = new HashSet<>();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 12, 100, 9999, 2147483647})
    void testValidatorIdValid(int id) {
        dto.setValidatorId(id);

        violations = validator.validateProperty(dto, "validatorId");


        assertEquals(0, violations.size(), "Valor não permitido: " + id);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3})
    void testIdInvalid(int id) {
        dto.setValidatorId(id);

        violations = validator.validateProperty(dto, "validatorId");

        assertAll("Testando id de validador",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + id),
                () -> violations.forEach(v -> {
                    assertTrue(v.getMessage().equals("Id de validador deve ser positivo"));
                })
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"Keilla Vitória", "Ayanne Prata", "Danilo Sousa"})
    void testValidatorNameValid(String validatorName) {
        dto.setValidatorName(validatorName);
        violations = validator.validateProperty(dto, "validatorName");

        assertEquals(0, violations.size(), "Valor não permitido: " + validatorName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "       ", "\n  ", " \n\n ", "\n \n"})
    void testValidatorNameInvalid(String validatorName) {
        dto.setValidatorName(validatorName);
        violations = validator.validateProperty(dto, "validatorName");

        assertAll("Testando status",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + validatorName),
                () -> assertEquals("Nome de validador não pode ser nulo", violations.stream().findFirst().get().getMessage())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"Comprovante inválido", "Faltam informações", "Aceito", "OK"})
    void testCommentaryValid(String commentary) {
        dto.setCommentary(commentary);
        violations = validator.validateProperty(dto, "commentary");

        assertEquals(0, violations.size(), "Valor não permitido: " + commentary);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "       ", "\n  ", " \n\n ", "\n \n"})
    void testCommentaryInvalid(String commentary) {
        dto.setCommentary(commentary);
        violations = validator.validateProperty(dto, "commentary");

        assertAll("Testando comentário",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + commentary),
                () -> assertEquals("Comentário não pode ser nulo", violations.stream().findFirst().get().getMessage())
        );
    }
}
