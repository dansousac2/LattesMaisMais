package com.ifpb.lattesmaismais.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class ReceiptDtoValidator {
	
	@Positive(message = "Id de comprovante deve ser positivo")
	private Integer id;
	
	@NotBlank(message = "Status do comprovante não deve ser nulo")
	private String status;
	
	@Positive(message = "Id de comentário de comprovante deve ser positivo")
	private Integer commentaryId;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCommentaryId() {
		return commentaryId;
	}

	public void setCommentaryId(Integer commentaryId) {
		this.commentaryId = commentaryId;
	}

}
