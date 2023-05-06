package com.ifpb.lattesmaismais.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class FileUploadDto {

	@Positive(message = "ID de usuário deve ser valor positivo!")
	private int userId;
	
	@NotBlank(message = "Comentário de usuário não deve ser nulo!")
	private String userCommentary;
	
	private String nameOnDB;
	
	//TODO remover este atributo
	private String url;
	
	public String getNameOnDB() {
		return nameOnDB;
	}

	public void setNameOnDB(String nameInDB) {
		this.nameOnDB = nameInDB;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUserCommentary() {
		return userCommentary;
	}
	
	public void setUserCommentary(String commentary) {
		this.userCommentary = commentary;
	}
	
}
