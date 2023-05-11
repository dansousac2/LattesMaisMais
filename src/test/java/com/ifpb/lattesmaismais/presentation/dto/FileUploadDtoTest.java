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
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FileUploadDtoTest {

    private FileUploadDto dto;
    private Set<ConstraintViolation<FileUploadDto>> violations;

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        dto = new FileUploadDto();
        violations = new HashSet<>();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 12, 100, 9999, 2147483647})
    void testUserIdValid(int id) {
        dto.setUserId(id);

        Optional<ConstraintViolation<FileUploadDto>> violationsUserId = validator.validateProperty(dto, "userId").stream().findFirst();

        if(violationsUserId.isPresent()) {
            violations.add(violationsUserId.get());
        }

        assertEquals(0, violations.size(), "id não permitido: " + id);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3})
    void testUserIdInvalid(int id) {
        dto.setUserId(id);

        violations = validator.validateProperty(dto, "userId");

        assertAll("Testando id do usuário",
                () -> assertEquals(1, violations.size(), "id não permitido encontrado: " + id),
                () -> violations.forEach(v -> {
                    assertTrue(v.getMessage().equals("ID de usuário deve ser valor positivo!"));
                })
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "Sem comentários", "Versão com comprovantes", "^"})
    void testCommentaryValid(String commentary) {
        dto.setUserCommentary(commentary);
        violations = validator.validateProperty(dto, "userCommentary");

        assertEquals(0, violations.size(), "Valor não permitido: " + commentary);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "       ", "\n  ", " \n\n ", "\n \n"})
    void testCommentaryInvalid(String commentary) {
        dto.setUserCommentary(commentary);
        violations = validator.validateProperty(dto, "userCommentary");

        assertAll("Testando nome e mensagem recebida",
                () -> assertEquals(1, violations.size(), "Valor não permitido encontrado: " + commentary),
                () -> assertEquals("Comentário de usuário não deve ser nulo!", violations.stream().findFirst().get().getMessage())
        );
    }
}
