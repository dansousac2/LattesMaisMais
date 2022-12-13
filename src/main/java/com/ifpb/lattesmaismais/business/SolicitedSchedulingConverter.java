package com.ifpb.lattesmaismais.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.model.SolicitedScheduling;
import com.ifpb.lattesmaismais.presentation.SolicitedSchedulingDto;

@Service
public class SolicitedSchedulingConverter {

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

	
}
