package com.ifpb.lattesmaismais.business;

import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.model.entity.ValidatorCommentary;
import com.ifpb.lattesmaismais.presentation.dto.ValidatorCommentaryDto;

import jakarta.validation.Valid;

@Service
public class CommentaryConverterService {

	public ValidatorCommentary dtoToCommentary(@Valid ValidatorCommentaryDto dto) {

		ValidatorCommentary entity = new ValidatorCommentary();
		
		entity.setValidatorId(dto.getValidatorId());
		entity.setValidatorName(dto.getValidatorName());
		entity.setCommentary(dto.getCommentary());
		
		return entity;
	}
	
}
