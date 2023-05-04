package com.ifpb.lattesmaismais.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifpb.lattesmaismais.business.ReceiptConverterService;
import com.ifpb.lattesmaismais.business.ReceiptService;
import com.ifpb.lattesmaismais.model.entity.Receipt;
import com.ifpb.lattesmaismais.presentation.dto.ReceiptDtoLink;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/receipt")
public class ReceiptController {
	
	@Autowired
	private ReceiptService service;
	
	@Autowired
	private ReceiptConverterService converter;

	@PostMapping
	public ResponseEntity save(@RequestBody @Valid ReceiptDtoLink dto) {
		try {
			Receipt entity = converter.dtoToReceiptWithUrl(dto);
			entity = service.save(entity);
			
			return new ResponseEntity(entity.getId(), HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
