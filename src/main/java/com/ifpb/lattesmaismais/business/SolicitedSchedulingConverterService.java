package com.ifpb.lattesmaismais.business;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.model.entity.SolicitedScheduling;
import com.ifpb.lattesmaismais.model.enums.StatusScheduling;
import com.ifpb.lattesmaismais.presentation.exception.ObjectNotFoundException;
import com.ifpb.lattesmaismais.presentation.dto.SolicitedSchedulingDto;

@Service
public class SolicitedSchedulingConverterService {

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
			dto.setStatus(entity.getStatus().toString());
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

	public SolicitedScheduling dtoToScheduling(Integer id, String version, String address, String status, Integer requesterId, 
			Integer validatorId, String date, String time) throws ObjectNotFoundException  {

		SolicitedScheduling entity = new SolicitedScheduling();

		entity.setId(id);
		entity.setVersion(version);
		entity.setAddress(address);

		if (status != null) {
			entity.setStatus(StatusScheduling.valueOf(status));
		}
		if (requesterId != null) {
			entity.setRequester(userService.findById(requesterId));
		}
		if (validatorId != null) {
			entity.setValidator(userService.findById(validatorId));
		}
		if (date != null) {
			entity.setDate(LocalDate.parse(date));
		}
		if (time != null) {
			entity.setTime(LocalTime.parse(time));
		}

		return entity;
	}

}
