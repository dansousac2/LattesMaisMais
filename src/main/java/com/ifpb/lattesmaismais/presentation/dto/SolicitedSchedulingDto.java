package com.ifpb.lattesmaismais.presentation.dto;

import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class SolicitedSchedulingDto {
	
	private Integer id;

	@NotBlank(message = "Data não pode ser nula")
	private String date;
	
	@NotBlank(message = "Horário não pode ser nulo")
	private String time;
	
	@NotBlank(message = "Endereço não pode ser nulo")
	private String address;
	
	@NotBlank(message = "Versão de currículo não pode ser nula")
	private String version;
	
	@Positive(message = "Id do validador deve ser positivo")
	private Integer validatorId;
	
	@Positive(message = "Id do solicitante deve ser positivo")
	private Integer requesterId;
	
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getValidatorId() {
		return validatorId;
	}

	public void setValidatorId(Integer validatorId) {
		this.validatorId = validatorId;
	}

	public Integer getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(Integer requesterId) {
		this.requesterId = requesterId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, date, id, requesterId, status, time, validatorId, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SolicitedSchedulingDto other = (SolicitedSchedulingDto) obj;
		return Objects.equals(address, other.address) && Objects.equals(date, other.date)
				&& Objects.equals(id, other.id) && Objects.equals(requesterId, other.requesterId)
				&& Objects.equals(status, other.status) && Objects.equals(time, other.time)
				&& Objects.equals(validatorId, other.validatorId) && Objects.equals(version, other.version);
	}
	
}
