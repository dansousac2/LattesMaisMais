package com.ifpb.lattesmaismais.presentation;

import com.ifpb.lattesmaismais.business.SolicitedSchedulingConverterService;
import com.ifpb.lattesmaismais.business.SolicitedSchedulingService;
import com.ifpb.lattesmaismais.business.UserService;
import com.ifpb.lattesmaismais.model.entity.SolicitedScheduling;
import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.model.enums.SchedulingStatus;
import com.ifpb.lattesmaismais.model.repository.SolicitedSchedulingRepository;
import com.ifpb.lattesmaismais.presentation.dto.SolicitedSchedulingDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class SolicitedSchedulingControllerTest {

    @Spy
    private SolicitedSchedulingRepository repository;

    @Mock
    private UserService userService;

    @InjectMocks
    @Spy
    private SolicitedSchedulingConverterService converterService;

    @InjectMocks
    @Spy
    private static SolicitedSchedulingService service;

    @InjectMocks
    @Spy
    private SolicitedSchedulingController controller;

    private static User requester;

    private static User validator;

    private static SolicitedScheduling entity;

    @BeforeAll
    public static void setUp() {
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
        ReflectionTestUtils.setField(service, "schedulingRepository", repository);
        ReflectionTestUtils.setField(controller, "schedulingService", service);
        ReflectionTestUtils.setField(converterService, "userService", userService);
        ReflectionTestUtils.setField(controller, "schedulingConverter", converterService);
    }

    @Test
    public void testFindAll() {
        ResponseEntity response = assertDoesNotThrow(() -> controller.findAll());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testFindByIdOk() {
        try {
            doReturn(entity).when(service).findById(any());

            ResponseEntity response = assertDoesNotThrow(() -> controller.findById(1));
            assertEquals(HttpStatus.OK, response.getStatusCode());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testFindByIdException() {
        try {
            doCallRealMethod().when(service).findById(any());

            ResponseEntity response = controller.findById(1);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Não foi possível encontrar SolicitedScheduling com id 1", response.getBody().toString());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void testSaveOk() {
        try {
            doReturn(entity).when(service).save(any());
            when(userService.findById(any())).thenReturn(requester);

            SolicitedSchedulingDto dto = converterService.schedulingToDto(entity);

            ResponseEntity response = assertDoesNotThrow(() -> controller.save(dto));
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testSaveException() {
        try {
            doReturn(entity).when(service).save(any());
            doCallRealMethod().when(userService).findById(any());

            entity.setId(999);
            SolicitedSchedulingDto dto = converterService.schedulingToDto(entity);

            ResponseEntity response = controller.save(dto);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testDeleteOk() {
        try {
            doNothing().when(service).deleteById(any());
        } catch (Exception e) {
            fail();
        }

        ResponseEntity response = assertDoesNotThrow(() -> controller.delete(1));
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteException() {
        try {
            doCallRealMethod().when(service).deleteById(any());
        } catch (Exception e) {
            fail();
        }

        ResponseEntity response = controller.delete(1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
