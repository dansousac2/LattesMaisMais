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

public class ReceiptDtoValidatorTest {

    private ReceiptDtoValidator dto;
    private Set<ConstraintViolation<ReceiptDtoValidator>> violations;

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        dto = new ReceiptDtoValidator();
        violations = new HashSet<>();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 12, 100, 9999, 2147483647})
    void testIdValid(int id) {
        dto.setId(id);

        violations = validator.validateProperty(dto, "id");


        assertEquals(0, violations.size(), "Valor não permitido: " + id);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3})
    void testIdInvalid(int id) {
        dto.setId(id);

        violations = validator.validateProperty(dto, "id");

        assertAll("Testando id",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + id),
                () -> violations.forEach(v -> {
                    assertTrue(v.getMessage().equals("Id de comprovante deve ser positivo"));
                })
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"WITHOUT_RECEIPT", "WAITING_VALIDATION", "CHECKED_BY_VALIDATOR"})
    void testStatusValid(String status) {
        dto.setStatus(status);
        violations = validator.validateProperty(dto, "status");

        assertEquals(0, violations.size(), "Valor não permitido: " + status);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "       ", "\n  ", " \n\n ", "\n \n"})
    void testStatusInvalid(String status) {
        dto.setStatus(status);
        violations = validator.validateProperty(dto, "status");

        assertAll("Testando status",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + status),
                () -> assertEquals("Status do comprovante não deve ser nulo", violations.stream().findFirst().get().getMessage())
        );
    }
}
