package com.ifpb.lattesmaismais.business;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

	public List<CurriculumDto> curriculumToDto(List<Curriculum> entityList, GenericsCurriculumService genCS) {
		List<CurriculumDto> dtoList = new ArrayList<>();

		for (Curriculum curriculum: entityList) {
			CurriculumDto dto = curriculumToDto(curriculum, genCS);
			dtoList.add(dto);
		}

		return dtoList;
	}

	public Curriculum dtoBackToCurriculum(@Valid CurriculumDtoBack dto, User owner, GenericsCurriculumService genCS, boolean maintainIds) {
		try {
			Curriculum entity = new Curriculum();
			entity.setDescription(dto.getDescription());
			entity.setEntryCount(dto.getEntryCount());
			entity.setEntries(dto.getEntryList());
			entity.setOwner(owner);
			CurriculumStatus status;
			if(maintainIds) {
				entity.setId(dto.getId());
				status = genCS.generateStatusCurriculumOnly(entity.getEntries());
			}else {
				status = genCS.organizeEntriesReceiptsAndStatus(entity.getEntries());
			}
			entity.setStatus(status);
			entity.setVersion(genCS.createVersionName());
			entity.setLastModification(LocalDateTime.parse(dto.getLastModification(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
			
			return entity;
			
		} catch(Exception e) {
			throw new IllegalArgumentException("Erro na conversão Dto -> Curr / Pode ser que algum dos Atributos seja nulo!");
		}
	}
}
