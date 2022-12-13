package com.ifpb.lattesmaismais.business;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.model.SolicitedScheduling;
import com.ifpb.lattesmaismais.model.StatusScheduling;
import com.ifpb.lattesmaismais.presentation.ObjectNotFoundException;
import com.ifpb.lattesmaismais.presentation.SolicitedSchedulingDto;

@Service
public class SolicitedSchedulingConverter {

	@Autowired
	private UserService userService;
	
	public List<SolicitedSchedulingDto> schedulingToDtoList(List<SolicitedScheduling> entityList) {

		if (entityList != null) {
			List<SolicitedSchedulingDto> dtoList = new ArrayList<>();
			SolicitedSchedulingDto dto;
			
			if (!entityList.isEmpty()) {
				for (SolicitedScheduling scheduling : entityList) {
					dto = schedulingToDto(scheduling);
					dtoList.add(dto);
				}
			}
			
			return dtoList;
		}
		throw new IllegalArgumentException("Não foi possível converter pois a lista de SolicitedScheduling é nula");
	}

	public SolicitedSchedulingDto schedulingToDto(SolicitedScheduling entity) {
		
		if (entity != null) {
			SolicitedSchedulingDto dto = new SolicitedSchedulingDto();
			
			dto.setId(entity.getId());
			dto.setStatus(entity.getStatus().getText());
			dto.setAddress(entity.getAddress());
			dto.setDate(entity.getDate().toString());
			dto.setTime(entity.getTime().toString());
			dto.setVersion(entity.getVersion());
			dto.setRequesterId(entity.getRequester().getId());
			dto.setValidatorId(entity.getValidator().getId());
			
			return dto;
		}
		
		throw new IllegalArgumentException("Não foi possível converter pois o objeto é nulo");
	}

	public SolicitedScheduling dtoToScheduling(SolicitedSchedulingDto dto) throws ObjectNotFoundException {
		
		if(dto != null) {
			
			SolicitedScheduling entity = new SolicitedScheduling();

			entity.setId(dto.getId());
			entity.setVersion(dto.getVersion());
			entity.setAddress(dto.getAddress());
			
			if(dto.getStatus() != null) {
				entity.setStatus(StatusScheduling.valueOf(dto.getStatus()));
			}
			if(dto.getRequesterId() != null) {
				entity.setRequester(userService.findById(dto.getRequesterId()));
			}
			if(dto.getRequesterId() != null) {
				entity.setValidator(userService.findById(dto.getValidatorId()));
			}
			if(dto.getDate() != null) {
				entity.setDate(LocalDate.parse(dto.getDate()));
			}
			if(dto.getTime() != null) {
				entity.setTime(LocalTime.parse(dto.getTime()));
			}

			return entity;
		}
		throw new IllegalArgumentException("O objeto SolicitedSchedulingDto não pode ser NULO!");
	}

	
}
