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

public class SolicitedSchedulingDtoTest {

    private SolicitedSchedulingDto dto;
    private Set<ConstraintViolation<SolicitedSchedulingDto>> violations;

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        dto = new SolicitedSchedulingDto();
        violations = new HashSet<>();
    }

    @ParameterizedTest
    @ValueSource(strings = {"25/05/2023", "2023-06-03", "02-05-2023"})
    void testDateValid(String date) {
        dto.setDate(date);
        violations = validator.validateProperty(dto, "date");

        assertEquals(0, violations.size(), "Valor não permitido: " + date);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "       ", "\n  ", " \n\n ", "\n \n"})
    void testDateInvalid(String date) {
        dto.setDate(date);
        violations = validator.validateProperty(dto, "date");

        assertAll("Testando data",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + date),
                () -> assertEquals("Data não pode ser nula", violations.stream().findFirst().get().getMessage())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"18:00", "21:30:45", "22:15:58"})
    void testTimeValid(String time) {
        dto.setTime(time);
        violations = validator.validateProperty(dto, "time");

        assertEquals(0, violations.size(), "Valor não permitido: " + time);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "       ", "\n  ", " \n\n ", "\n \n"})
    void testTimeInvalid(String time) {
        dto.setTime(time);
        violations = validator.validateProperty(dto, "time");

        assertAll("Testando horário",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + time),
                () -> assertEquals("Horário não pode ser nulo", violations.stream().findFirst().get().getMessage())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"Rua das abobrinhas", "Instituto Federal - Campus Monteiro", "Avenida dos abacaxis"})
    void testAddressValid(String address) {
        dto.setAddress(address);
        violations = validator.validateProperty(dto, "address");

        assertEquals(0, violations.size(), "Valor não permitido: " + address);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "       ", "\n  ", " \n\n ", "\n \n"})
    void testAdressInvalid(String address) {
        dto.setDate(address);
        violations = validator.validateProperty(dto, "address");

        assertAll("Testando endereço",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + address),
                () -> assertEquals("Endereço não pode ser nulo", violations.stream().findFirst().get().getMessage())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"V_10052023_183000", "V_02062023_200000", "V_27052023_154500"})
    void testVersionValid(String version) {
        dto.setVersion(version);
        violations = validator.validateProperty(dto, "version");

        assertEquals(0, violations.size(), "Valor não permitido: " + version);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "       ", "\n  ", " \n\n ", "\n \n"})
    void testVersionInvalid(String version) {
        dto.setVersion(version);
        violations = validator.validateProperty(dto, "version");

        assertAll("Testando nome de usuário",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + version),
                () -> assertEquals("Versão de currículo não pode ser nula", violations.stream().findFirst().get().getMessage())
        );
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
    void testValidatorIdInvalid(int id) {
        dto.setValidatorId(id);

        violations = validator.validateProperty(dto, "validatorId");

        assertAll("Testando id do validador",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + id),
                () -> violations.forEach(v -> {
                    assertTrue(v.getMessage().equals("Id do validador deve ser positivo"));
                })
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 12, 100, 9999, 2147483647})
    void testRequesterIdValid(int id) {
        dto.setRequesterId(id);

        violations = validator.validateProperty(dto, "requesterId");


        assertEquals(0, violations.size(), "Valor não permitido: " + id);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3})
    void testRequesterIdInvalid(int id) {
        dto.setRequesterId(id);

        violations = validator.validateProperty(dto, "requesterId");

        assertAll("Testando id do solicitante",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + id),
                () -> violations.forEach(v -> {
                    assertTrue(v.getMessage().equals("Id do solicitante deve ser positivo"));
                })
        );
    }
}
