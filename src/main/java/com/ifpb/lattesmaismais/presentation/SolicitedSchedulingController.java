package com.ifpb.lattesmaismais.presentation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ifpb.lattesmaismais.business.SolicitedSchedulingConverterService;
import com.ifpb.lattesmaismais.business.SolicitedSchedulingService;
import com.ifpb.lattesmaismais.model.entity.SolicitedScheduling;
import com.ifpb.lattesmaismais.presentation.dto.SolicitedSchedulingDto;
import com.ifpb.lattesmaismais.presentation.dto.SolicitedSchedulingDtoValidator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/solicitedscheduling")
public class SolicitedSchedulingController {

	@Autowired
	private SolicitedSchedulingService service;

	@Autowired
	private SolicitedSchedulingConverterService converter;

	@GetMapping
	public ResponseEntity findAll() {
		try {
			List<SolicitedScheduling> entityList = service.findAll();
			List<SolicitedSchedulingDto> dtoList = converter.schedulingToDtoList(entityList);

			return ResponseEntity.ok(dtoList);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("/filter")
	public ResponseEntity findAllWithFilter(
			@RequestParam(required = false) String status,
			@RequestParam(required = false) Integer validatorId,
			@RequestParam(required = false) Integer requesterId,
			@RequestParam(required = false) String date,
			@RequestParam(required = false) String address, 
			@RequestParam(required = false) String time
	) {
		try {
			// O filtro não utiliza ID nem VERSION
			SolicitedScheduling filter = converter.dtoToScheduling(null, null, address, status, requesterId, validatorId, date, time);
			
			List<SolicitedScheduling> entityList = service.findAll(filter);
			List<SolicitedSchedulingDto> dtoList = converter.schedulingToDtoList(entityList);
			
			return ResponseEntity.ok().body(dtoList);

		} catch (Exception e) {
			//TODO talvez ao invés de cod 400, usar 200 incluindo a mensagem de erro
			// tendo em vista que ao passar uma ID de USER inválida será disparada uma exceção, quando poderia retornar apenas uma lista vazia
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// exemplo: findallbyuserid?id=1&isvalidator=true
	@GetMapping("/findallbyuserid")
	public ResponseEntity findAllByUserId(@RequestParam Integer id, @RequestParam(required = false) boolean isValidator) {
		try {
			if(id == null || id < 1) {
				throw new IllegalArgumentException("Id de usuário inválido");
			}
			
			List<SolicitedScheduling> entityList;
			if(isValidator) {
				entityList = service.findAllByValidatorId(id);
			} else {
				entityList = service.findAllByRequesterId(id);
			}
			
			List<SolicitedSchedulingDto> dtoList = converter.schedulingToDtoList(entityList);
			
			return ResponseEntity.ok(dtoList);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity findById(@PathVariable Integer id) {
		try {
			SolicitedScheduling entity = service.findById(id);
			SolicitedSchedulingDto dto = converter.schedulingToDto(entity);

			return ResponseEntity.ok().body(dto);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity save(@RequestBody @Valid SolicitedSchedulingDto dto) {

		try {
			SolicitedScheduling entity = converter.dtoToScheduling(dto.getId(), 
					dto.getVersion(), dto.getAddress(), dto.getStatus(), dto.getRequesterId(), 
					dto.getValidatorId(), dto.getDate(), dto.getTime());

			entity = service.save(entity);
			dto = converter.schedulingToDto(entity);

			return new ResponseEntity(dto, HttpStatus.CREATED);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping
	public ResponseEntity update(@RequestBody @Valid SolicitedSchedulingDtoValidator dto) {
		try {
			SolicitedScheduling entity = service.findById(dto.getId());
			
			entity = converter.updateSchedulingSolicitation(dto, entity);
			
			entity = service.save(entity);
			
			SolicitedSchedulingDto dtoToFront = converter.schedulingToDto(entity);
			
			return ResponseEntity.ok(dtoToFront);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable Integer id) {
		try {
			service.deleteById(id);
			return ResponseEntity.noContent().build();

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
