package com.ifpb.lattesmaismais.presentation.dto;

import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class SolicitedSchedulingDtoValidator {
	
	@Positive(message = "Id da solicitação de validação deve ser positivo")
	private Integer id;

	@NotBlank(message = "status da solicitação de validação não deve ser nula")
	private String status;
	
	private String returnedValidatorMessage;
	
	public String getReturnedValidatorMessage() {
		return returnedValidatorMessage;
	}

	public void setReturnedValidatorMessage(String returnedValidatorMessage) {
		this.returnedValidatorMessage = returnedValidatorMessage;
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(id, returnedValidatorMessage, status);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SolicitedSchedulingDtoValidator other = (SolicitedSchedulingDtoValidator) obj;
		return Objects.equals(id, other.id) && Objects.equals(returnedValidatorMessage, other.returnedValidatorMessage)
				&& Objects.equals(status, other.status);
	}
	
}
