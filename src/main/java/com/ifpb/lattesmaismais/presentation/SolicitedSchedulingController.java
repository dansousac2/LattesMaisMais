package com.ifpb.lattesmaismais.presentation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ifpb.lattesmaismais.business.SolicitedSchedulingConverter;
import com.ifpb.lattesmaismais.business.SolicitedSchedulingService;
import com.ifpb.lattesmaismais.model.SolicitedScheduling;

@RestController
@RequestMapping("api/solicitedsheduling")
public class SolicitedSchedulingController {

	@Autowired
	private SolicitedSchedulingService schedulingService;

	@Autowired
	private SolicitedSchedulingConverter schedulingConverter;

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
			@RequestParam String status,
			@RequestParam Integer validatorId,
			@RequestParam Integer requesterId,
			@RequestParam String date,
			@RequestParam String address, 
			@RequestParam String time
	) {
		try {
			SolicitedSchedulingDto dto = new SolicitedSchedulingDto();
			dto.setStatus(status);
			dto.setValidatorId(validatorId);
			dto.setRequesterId(requesterId);
			dto.setDate(date);
			dto.setAddress(address);
			dto.setTime(time);
			
			SolicitedScheduling filter = schedulingConverter.dtoToScheduling(dto);
			// O filtro não utiliza ID nem VERSION
			List<SolicitedScheduling> entityList = schedulingService.findAll(filter);
			List<SolicitedSchedulingDto> dtoList = schedulingConverter.schedulingToDtoList(entityList);
			
			return ResponseEntity.ok().body(dtoList);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
