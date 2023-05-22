package com.ifpb.lattesmaismais.presentation.dto;

import com.ifpb.lattesmaismais.presentation.CurriculumDtoBack;
import com.ifpb.lattesmaismais.model.entity.Entry;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CurriculumDtoBackTest {

    private CurriculumDtoBack dtoBack;

    private Set<ConstraintViolation<CurriculumDtoBack>> violations;

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        dtoBack = new CurriculumDtoBack();
        violations = new HashSet<>();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 12, 100, 9999, 2147483647})
    void testCurriculumIdValid(int id) {
        dtoBack.setId(id);

        violations = validator.validateProperty(dtoBack, "id");


        assertEquals(0, violations.size(), "Valor não permitido: " + id);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3})
    void testCurriculumIdInvalid(int id) {
        dtoBack.setId(id);

        violations = validator.validateProperty(dtoBack, "id");

        assertAll("Testando id do currículo",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + id),
                () -> violations.forEach(v -> {
                    assertTrue(v.getMessage().equals("id de currículo não deve ser nulo!"));
                })
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 12, 100, 9999, 2147483647})
    void testOwnerIdValid(int id) {
        dtoBack.setOwnerId(id);

        violations = validator.validateProperty(dtoBack, "ownerId");


        assertEquals(0, violations.size(), "Valor não permitido: " + id);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3})
    void testOwnerIdInvalid(int id) {
        dtoBack.setOwnerId(id);

        violations = validator.validateProperty(dtoBack, "ownerId");

        assertAll("Testando id do proprietário",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + id),
                () -> violations.forEach(v -> {
                    assertTrue(v.getMessage().equals("CurriculumDto não deve ter id do proprietário nulo!"));
                })
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"24/05/2023 - 10:30:00", "20/05/2023 - 17:45:30", "17/08/2024 - 12:00:00"})
    void testLastModificationValid(String lastModification) {
        dtoBack.setLastModification(lastModification);
        violations = validator.validateProperty(dtoBack, "lastModification");

        assertEquals(0, violations.size(), "Valor não permitido: " + lastModification);
    }

    @ParameterizedTest
    @ValueSource(strings = {"24-05-2023 10:30:00", "2023/05/20 17:45:30", "17082024_120000"})
    void testLastModificationInvalid(String lastModification) {
        dtoBack.setLastModification(lastModification);
        violations = validator.validateProperty(dtoBack, "lastModification");

        assertAll("",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + lastModification),
                () -> assertEquals("Data de modificação em formato errado!", violations.stream().findFirst().get().getMessage())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"Sem comentários", "Versão final", "Versão sem comprovantes"})
    void testDescriptionValid(String description) {
        dtoBack.setDescription(description);
        violations = validator.validateProperty(dtoBack, "description");

        assertEquals(0, violations.size(), "Valor não permitido: " + description);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "       ", "\n  ", " \n\n ", "\n \n"})
    void testDescriptionInvalid(String description) {
        dtoBack.setDescription(description);
        violations = validator.validateProperty(dtoBack, "description");

        assertAll("Testando nome de usuário",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + description),
                () -> assertEquals("Descrição de currículo não deve ser nula!", violations.stream().findFirst().get().getMessage())
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 12, 100, 9999, 2147483647})
    void testEntryCountValid(int entryCount) {
        dtoBack.setEntryCount(entryCount);

        violations = validator.validateProperty(dtoBack, "entryCount");


        assertEquals(0, violations.size(), "Valor não permitido: " + entryCount);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3})
    void testEntryCountInvalid(int entryCount) {
        dtoBack.setEntryCount(entryCount);

        violations = validator.validateProperty(dtoBack, "entryCount");

        assertAll("Testando a quantidade de entradas",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + entryCount),
                () -> violations.forEach(v -> {
                    assertTrue(v.getMessage().equals("Contador de entradas de currículo deve ser no mínimo 1"));
                })
        );
    }

    @Test
    void testEntryListValid() {
        Entry entry = new Entry();
        Entry entry2 = new Entry();

        List<Entry> entryList = new ArrayList<>();

        entryList.add(entry);
        entryList.add(entry2);

        dtoBack.setEntryList(entryList);

        violations = validator.validateProperty(dtoBack, "entryList");

        assertEquals(0, violations.size(), "Valor não permitido: " + entryList);
    }

    @Test
    void testEntryListInvalid() {
        List<Entry> entryList = new ArrayList<>();

        // Adicionando uma lista vazia:
        dtoBack.setEntryList(entryList);

        violations = validator.validateProperty(dtoBack, "entryList");

        assertAll("Testando a quantidade de entradas",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + entryList),
                () -> violations.forEach(v -> {
                    assertTrue(v.getMessage().equals("Lista de entradas deve conter ao menos 1 entrada!"));
                })
        );
    }
}
