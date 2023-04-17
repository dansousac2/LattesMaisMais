package com.ifpb.lattesmaismais.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserDtoBack {

	@Email(message = "O email deve ser válido!")
	private String email;
	
	@Pattern(regexp = "^\\S{8,}$", message = "A senha não deve conter espaços e deve ter no mínimo 8 dígitos!")
	private String password;
	
	@NotBlank(message = "O nome de usuário não pode ser nulo!")
	private String name;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
