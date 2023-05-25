package com.ifpb.lattesmaismais.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifpb.lattesmaismais.business.CommentaryConverterService;
import com.ifpb.lattesmaismais.business.CommentaryService;
import com.ifpb.lattesmaismais.model.entity.ValidatorCommentary;
import com.ifpb.lattesmaismais.presentation.dto.ValidatorCommentaryDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/validatorcommentary")
public class ValidatorCommentaryController {
	
	@Autowired
	private CommentaryConverterService converter;
	
	@Autowired
	private CommentaryService service;

	@PostMapping
	public ResponseEntity save(@RequestBody @Valid ValidatorCommentaryDto dto) {
		try {
			ValidatorCommentary entity = converter.dtoToCommentary(dto);
			
			entity = service.save(entity);
			
			return new ResponseEntity(entity, HttpStatus.CREATED);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
