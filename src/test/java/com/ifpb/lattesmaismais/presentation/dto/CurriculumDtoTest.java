package com.ifpb.lattesmaismais.presentation.dto;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.ifpb.lattesmaismais.model.entity.Entry;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@TestMethodOrder(MethodOrderer.DisplayName.class)
class CurriculumDtoTest {
	
	private CurriculumDto dto;
	private Set<ConstraintViolation<CurriculumDto>> violations;
	private static Validator validator;

	@BeforeAll
	static void setup() throws Exception {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void beforeEach() throws Exception {
		dto = new CurriculumDto();
		violations = new HashSet<>();
	}

	@AfterEach
	void afterEach() throws Exception {
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"1", "1 _ e R", "d@n1l0´´~", "^"})
	void ownerNameValid(String s) {
		dto.setOwnerName(s);
		violations = validator.validateProperty(dto, "ownerName");
		
		assertEquals(0, violations.size(), "nome não permitido: " + s);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"", "       ", "\n  ", " \n\n ", "\n \n"})
	void ownerNameInvalid(String s) {
		dto.setOwnerName(s);
		violations = validator.validateProperty(dto, "ownerName");
		
		assertAll("Testando nome e mensagem recebida",
				() -> assertEquals(1, violations.size(), "nome permitido encontrado: " + s),
				() -> assertEquals("Nome de usuário não pode ser nulo!", violations.stream().findFirst().get().getMessage()
						, "O campo do Dto não possui mensagem de violação!")
		);
	}

	// id and countEntry valids
	@ParameterizedTest
	@ValueSource(ints = {1, 12, 100, 9999, 2147483647})
	void idOwnerAndCountEntryValid(int n) {
		dto.setOwnerId(n);
		dto.setEntryCount(n);
		
		Optional<ConstraintViolation<CurriculumDto>> opViolationsOwnerId = validator.validateProperty(dto, "ownerId").stream().findFirst();
		Optional<ConstraintViolation<CurriculumDto>> opViolationsCountEntry = validator.validateProperty(dto, "entryCount").stream().findFirst();
		
		if(opViolationsOwnerId.isPresent()) {
			violations.add(opViolationsOwnerId.get());
		}
		if(opViolationsCountEntry.isPresent()) {
			violations.add(opViolationsCountEntry.get());
		}
		
		assertEquals(0, violations.size(), "id/count não permitido: " + n);
	}
	// colocando apenas constrainsts específicas na lista de violações = id do dono e contador de entradas
	@ParameterizedTest
	@ValueSource(ints = {0, -1})
	void idOwnerAndCountEntryInvalid(int n) {
		dto.setOwnerId(n);
		dto.setEntryCount(n);
		
		Optional<ConstraintViolation<CurriculumDto>> opViolationsOwnerId = validator.validateProperty(dto, "ownerId").stream().findFirst();
		Optional<ConstraintViolation<CurriculumDto>> opViolationsCountEntry = validator.validateProperty(dto, "entryCount").stream().findFirst();
		
		if(opViolationsOwnerId.isPresent()) {
			violations.add(opViolationsOwnerId.get());
		}
		if(opViolationsCountEntry.isPresent()) {
			violations.add(opViolationsCountEntry.get());
		}
		
		assertAll("Testando id do proprietário e mensagem recebida",
				() -> assertEquals(2, violations.size(), "id/count permitido encontrado: " + n),
				() -> violations.forEach(v -> {
					assertTrue(v.getMessage().equals("O ID de proprietário deve ser um valor positivo!")
							|| v.getMessage().equals("O entryCount de proprietário deve ser um valor positivo!"));
				}) 
		);
	}

	@Test
	void entryListValid() {
		List<Entry> list = new ArrayList<>();
		list.add(new Entry());
		
		dto.setEntryList(list);
		
		violations = validator.validateProperty(dto, "entryList");
		
		assertEquals(0, violations.size(), "Lista<Entry> contém elementos, mas não são reconhecidos!");
	}
	
	@Test
	void entryListInvalid() {
		List<Entry> list = new ArrayList<>();
		
		dto.setEntryList(list);
		
		violations = validator.validateProperty(dto, "entryList");
		
		assertAll("Testando lista vazia de Entry",
				() -> assertEquals(1, violations.size(), "Lista<Entry> NÃO contém elementos e não dispara erro"),
				() -> assertEquals("O currículo deve conter ao menos uma entrada identificada!", violations.stream().findFirst().get().getMessage())
		);
		
	}
}
