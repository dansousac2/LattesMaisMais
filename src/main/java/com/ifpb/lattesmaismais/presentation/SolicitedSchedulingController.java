package com.ifpb.lattesmaismais.presentation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ifpb.lattesmaismais.business.SolicitedSchedulingConverterService;
import com.ifpb.lattesmaismais.business.SolicitedSchedulingService;
import com.ifpb.lattesmaismais.model.SolicitedScheduling;

@RestController
@RequestMapping("api/solicitedsheduling")
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
			SolicitedScheduling filter = schedulingConverter.dtoToScheduling(null, null, address, status, requesterId, validatorId, date, time);
			// O filtro não utiliza ID nem VERSION
			List<SolicitedScheduling> entityList = schedulingService.findAll(filter);
			List<SolicitedSchedulingDto> dtoList = schedulingConverter.schedulingToDtoList(entityList);
			
			return ResponseEntity.ok().body(dtoList);

		} catch (Exception e) {
			//TODO talvez ao invés de cod 400, usar 200 incluindo a mensagem de erro
			// tendo em vista que ao passar uma ID de USER inválida será disparada uma exceção, quando poderia retornar apenas uma lista vazia
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
