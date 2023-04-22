package com.ifpb.lattesmaismais.business;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.model.entity.Curriculum;
import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.model.enums.CurriculumStatus;
import com.ifpb.lattesmaismais.presentation.CurriculumDtoBack;
import com.ifpb.lattesmaismais.presentation.dto.CurriculumDto;

import jakarta.validation.Valid;

@Service
public class CurriculumConverterService {
	
	public CurriculumDto curriculumToDto(Curriculum entity, GenericsCurriculumService genCS) {
		try {
			CurriculumDto dto = new CurriculumDto();
			dto.setDescription(entity.getDescription());
			dto.setEntryCount(entity.getEntryCount());
			dto.setEntryList(entity.getEntries());
			dto.setId(entity.getId());
			dto.setLastModification(genCS.dateTimeToString(entity.getLastModification()));
			dto.setOwnerId(entity.getOwner().getId());
			dto.setOwnerName(entity.getOwner().getName());
			dto.setStatus(entity.getStatus().name());
			dto.setVersion(entity.getVersion());
			
			return dto;
			
		} catch (Exception e) {
			throw new IllegalArgumentException("Erro na conversão Curr -> Dto / Pode ser que algum dos Atributos seja nulo!");
		}
		
	}

	public Curriculum dtoBackToCurriculum(@Valid CurriculumDtoBack dto, User owner, GenericsCurriculumService genCS) {
		try {
			Curriculum entity = new Curriculum();
			entity.setVersion(genCS.createVersionName());
			entity.setDescription(dto.getDescription());
			entity.setEntryCount(dto.getEntryCount());
			entity.setLastModification(LocalDateTime.now());
			entity.setOwner(owner);
			
			entity.setEntries(dto.getEntryList());
			CurriculumStatus status = genCS.organizeEntriesReceiptsAndStatus(entity.getEntries());
			entity.setStatus(status);
			
			
			return entity;
			
		} catch(Exception e) {
			throw new IllegalArgumentException("Erro na conversão Dto -> Curr / Pode ser que algum dos Atributos seja nulo!");
		}
	}

	
}
