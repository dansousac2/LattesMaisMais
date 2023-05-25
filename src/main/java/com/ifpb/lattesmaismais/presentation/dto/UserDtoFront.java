package com.ifpb.lattesmaismais.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class UserDtoFront {

	@Positive(message = "O id do usuário deve ser um valor positivo!")
	private Integer id;
	
	@NotBlank(message = "O nome do usuário não pode ser nulo!")
	private String name;
	
	@Email(message = "O email deve ser válido!")
	private String email;
	
	private String validatorAddress;
	
	public String getValidatorAddress() {
		return validatorAddress;
	}

	public void setValidatorAddress(String validatorsAddress) {
		this.validatorAddress = validatorsAddress;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
