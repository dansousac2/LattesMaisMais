package com.ifpb.lattesmaismais.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifpb.lattesmaismais.business.PasswordEncoderService;
import com.ifpb.lattesmaismais.business.UserConverterService;
import com.ifpb.lattesmaismais.business.UserService;
import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.presentation.dto.UserDtoBack;
import com.ifpb.lattesmaismais.presentation.dto.UserDtoFront;
import com.ifpb.lattesmaismais.presentation.exception.ObjectNotFoundException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService service;
	@Autowired
	private UserConverterService userConverter;
	@Autowired
	private PasswordEncoderService passEncoder;
	
	@GetMapping
	public ResponseEntity findByEmail(@RequestBody String email) {
		try {
			User entity = service.findByEmail(email);
			UserDtoFront dto = userConverter.userToDtoFront(entity);
			
			return ResponseEntity.ok().body(dto);
		} catch (ObjectNotFoundException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity save(@RequestBody @Valid UserDtoBack dto) {
		try {
			if(service.existsEmail(dto.getEmail())) {
				throw new IllegalArgumentException("O cadastro não pode ser concluído. Email já registrado!");
			}
			User user = userConverter.dtoBackToUser(dto);
			passEncoder.encryptPassword(user);
			user = service.save(user);
			
			UserDtoFront dtoToReturn = userConverter.userToDtoFront(user);
			
			return new ResponseEntity(dtoToReturn, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
