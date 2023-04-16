package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.model.entity.SolicitedScheduling;
import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.model.enums.SchedulingStatus;
import com.ifpb.lattesmaismais.model.repository.UserRepository;
import com.ifpb.lattesmaismais.presentation.dto.SolicitedSchedulingDto;
import com.ifpb.lattesmaismais.presentation.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SolicitedSchedulingConverterServiceTest {

    @Spy
    private UserRepository userRepository;

    @InjectMocks
    @Spy
    private UserService userService;

    @InjectMocks
    @Spy
    private static SolicitedSchedulingConverterService converterService;

    private static User requester;

    private static User validator;

    private static SolicitedScheduling entity;

    @BeforeAll
    public static void setUp() {
        converterService = new SolicitedSchedulingConverterService();

        requester = new User();
        requester.setId(1);
        requester.setName("Keilla");
        requester.setEmail("keilla@gmail.com");

        validator = new User();
        validator.setId(2);
        validator.setName("Ayanne");
        validator.setEmail("ayanne@gmail.com");

        entity = new SolicitedScheduling();
        entity.setId(1);
        entity.setDate(LocalDate.now());
        entity.setTime(LocalTime.now());
        entity.setAddress("Rua das batatas");
        entity.setVersion("V_202304072000");
        entity.setRequester(requester);
        entity.setValidator(validator);
        entity.setStatus(SchedulingStatus.OPEN);
    }

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(userService, "repository", userRepository);
        ReflectionTestUtils.setField(converterService, "userService", userService);
    }

    @Test
    public void testSchedulingToDtoOk() {
        SolicitedSchedulingDto dto = converterService.schedulingToDto(entity);

        assertAll(
                () -> assertEquals(dto.getId(), entity.getId()),
                () -> assertEquals(dto.getRequesterId(), entity.getRequester().getId()),
                () -> assertEquals(dto.getValidatorId(), entity.getValidator().getId()),
                () -> assertEquals(dto.getStatus(), entity.getStatus().name()),
                () -> assertEquals(dto.getDate(), entity.getDate().toString()),
                () -> assertEquals(dto.getTime(), entity.getTime().toString()),
                () -> assertEquals(dto.getAddress(), entity.getAddress()),
                () -> assertEquals(dto.getVersion(), entity.getVersion())
        );
    }

    @Test
    public void testSchedulingToDtoException() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> converterService.schedulingToDto(null));

        assertEquals("Não foi possível converter pois o objeto é nulo", exception.getMessage());
    }

    @Test
    public void testDtoToSchedulingOk() {
        when(userRepository.findById(any())).thenReturn(Optional.of(requester));

        try {
            assertDoesNotThrow(() -> converterService.dtoToScheduling(1, "V_202304072000", "Rua das batatas", SchedulingStatus.OPEN.name(), 1, 1, LocalDate.now().toString(), LocalTime.now().toString()));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testDtoToSchedulingException() {
        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> converterService.dtoToScheduling(null, null, null, null, 999, 999, null, null));

        assertEquals("Usuário com não encontrado/ id: 999", exception.getMessage());
    }
}
