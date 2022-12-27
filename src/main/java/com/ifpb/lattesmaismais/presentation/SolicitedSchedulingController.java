package com.ifpb.lattesmaismais.presentation;

import java.util.List;

import com.ifpb.lattesmaismais.presentation.dto.SolicitedSchedulingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ifpb.lattesmaismais.business.SolicitedSchedulingConverterService;
import com.ifpb.lattesmaismais.business.SolicitedSchedulingService;
import com.ifpb.lattesmaismais.model.entity.SolicitedScheduling;

@RestController
@RequestMapping("api/solicitedscheduling")
public class SolicitedSchedulingController {

	@Autowired
	private SolicitedSchedulingService schedulingService;

	@Autowired
	private SolicitedSchedulingConverterService schedulingConverter;

	@GetMapping
	public ResponseEntity findAll() {
		try {
			List<SolicitedScheduling> entityList = schedulingService.findAll();
			List<SolicitedSchedulingDto> dtoList = schedulingConverter.schedulingToDtoList(entityList);

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
			SolicitedScheduling filter = schedulingConverter.dtoToScheduling(null, null, address, status, requesterId, validatorId, date, time);
			
			List<SolicitedScheduling> entityList = schedulingService.findAll(filter);
			List<SolicitedSchedulingDto> dtoList = schedulingConverter.schedulingToDtoList(entityList);
			
			return ResponseEntity.ok().body(dtoList);

		} catch (Exception e) {
			//TODO talvez ao invés de cod 400, usar 200 incluindo a mensagem de erro
			// tendo em vista que ao passar uma ID de USER inválida será disparada uma exceção, quando poderia retornar apenas uma lista vazia
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity findById(@PathVariable Integer id) {
		try {
			SolicitedScheduling entity = schedulingService.findById(id);
			SolicitedSchedulingDto dto = schedulingConverter.schedulingToDto(entity);

			return ResponseEntity.ok().body(dto);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity save(@RequestBody SolicitedSchedulingDto dto) {

		try {
			//TODO verificar validações do DTO
			SolicitedScheduling entity = schedulingConverter.dtoToScheduling(dto.getId(), 
					dto.getVersion(), dto.getAddress(), dto.getStatus(), dto.getRequesterId(), 
					dto.getValidatorId(), dto.getDate(), dto.getTime());

			//TODO verificar validações da ENTIDADE
			entity = schedulingService.save(entity);
			dto = schedulingConverter.schedulingToDto(entity);

			return new ResponseEntity(dto, HttpStatus.CREATED);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable Integer id) {
		try {
			schedulingService.deleteById(id);
			return ResponseEntity.noContent().build();

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
