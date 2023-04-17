package com.ifpb.lattesmaismais.business;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.model.enums.AccountStatus;
import com.ifpb.lattesmaismais.presentation.dto.UserDtoBack;
import com.ifpb.lattesmaismais.presentation.dto.UserDtoFront;

@Service
public class UserConverterService {
	
	@Autowired
	private RoleServiceImpl roleService;
	
	public UserDtoFront userToDtoFront(User entity) {
		try {
			UserDtoFront dto = new UserDtoFront();
			dto.setEmail(entity.getEmail());
			dto.setId(entity.getId());
			dto.setName(entity.getName());

			return dto;
		} catch (Exception e) {
			throw new IllegalArgumentException("Erro durante conversão / Usuário --> DtoFront : usuário ou atributo de usuário nulo!");
		}
	}

	public User dtoBackToUser(UserDtoBack dto) {
		try {
			User entity = new User();
			entity.setEmail(dto.getEmail());
			entity.setPassword(dto.getPassword());
			entity.setName(dto.getName());
			entity.setStatus(AccountStatus.ACTIVE);
			entity.setRoles(Arrays.asList(roleService.findDefault()));
			
			return entity;
		} catch (Exception e) {
			throw new IllegalArgumentException("Erro durante conversão / DtoBack --> User : Dto ou atributo de Dto nulo!");
		}
	}
}
