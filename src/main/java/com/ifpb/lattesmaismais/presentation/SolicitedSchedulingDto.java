package com.ifpb.lattesmaismais.presentation;

import java.time.LocalDate;
import java.time.LocalTime;

import com.ifpb.lattesmaismais.model.Status;

public class SolicitedSchedulingDto {
	
	private Integer id;

	private LocalDate date;
	
	private LocalTime time;
	
	private String address;
	
	private String version;
	
	private Integer validatorId;
	
	private Status status;

	public SolicitedSchedulingDto() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
}
