package com.ifpb.lattesmaismais.presentation.dto;

import com.ifpb.lattesmaismais.model.enums.ReceiptStatus;

import jakarta.validation.constraints.NotBlank;

public class ReceiptDtoLink {

    @NotBlank(message = "A url informada não deve ser nula!")
	private String url;
    private String commentary;
    private ReceiptStatus status;
    
    public ReceiptDtoLink() {
    	this.status = ReceiptStatus.WAITING_VALIDATION;
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

	public ReceiptStatus getStatus() {
		return status;
	}

	public void setStatus(ReceiptStatus status) {
		this.status = status;
	}
    
}
