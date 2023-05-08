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

public class ReceiptDtoLinkTest {

    private ReceiptDtoLink dto;

    private Set<ConstraintViolation<ReceiptDtoLink>> violations;

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        dto = new ReceiptDtoLink();
        violations = new HashSet<>();
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "teste.jpg", "https://teste.com/", "^"})
    void testUrlValid(String url) {
        dto.setUrl(url);
        violations = validator.validateProperty(dto, "url");

        assertEquals(0, violations.size(), "Valor não permitido: " + url);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "       ", "\n  ", " \n\n ", "\n \n"})
    void testUrlInvalid(String url) {
        dto.setUrl(url);
        violations = validator.validateProperty(dto, "url");

        assertAll("Testando url do comprovante",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + url),
                () -> assertEquals("A url informada não deve ser nula!", violations.stream().findFirst().get().getMessage())
        );
    }
}
