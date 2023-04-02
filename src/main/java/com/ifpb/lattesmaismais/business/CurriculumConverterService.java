package com.ifpb.lattesmaismais.business;

import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.model.entity.Curriculum;
import com.ifpb.lattesmaismais.presentation.dto.CurriculumDto;

@Service
public class CurriculumConverterService {
	
	public CurriculumDto curriculumToDto(Curriculum entity) {
		
		try {
			CurriculumDto dto = new CurriculumDto();
			dto.setEntryCount(entity.getEntryCount());
			dto.setId(entity.getId());
			dto.setOwnerId(entity.getOwner().getId());
			dto.setOwnerName(entity.getOwner().getName());
			dto.setEntryList(entity.getEntries());
			dto.setStatus(entity.getStatus().name());
			dto.setDescription(entity.getDescription());
			dto.setVersion(entity.getVersion());
			
			return dto;
			
		} catch (Exception e) {
			throw new IllegalArgumentException("Erro na conversão Curr -> Dto / Pode ser que algum dos Atributos seja nulo:\n" + e.getMessage());
		}
		
	}
}
