package com.ifpb.lattesmaismais.presentation.dto;

public class TokenDto {
	
	private String token;
	
	private UserDtoFront user;
	
	public TokenDto(String token, UserDtoFront user) {
		this.token = token;
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserDtoFront getUser() {
		return user;
	}

	public void setUser(UserDtoFront user) {
		this.user = user;
	}
}
