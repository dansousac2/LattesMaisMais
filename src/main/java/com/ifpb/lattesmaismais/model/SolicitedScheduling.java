package com.ifpb.lattesmaismais.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;

@Table(name = "SOLICITED_SCHEDULING")
@Entity
public class SolicitedScheduling implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SOLICITED_SCHEDULING_ID")
	private Integer id;

	@Column(name = "SCHEDULING_DATE", nullable = false)
	private LocalDate date;
	
	@Column(name = "SCHEDULING_TIME", nullable = false)
	private LocalTime time;
	
	@Column(name = "SCHEDULING_ADDRESS", nullable = false)
	private String address;
	
	@Column(name = "SCHEDULING_VERSION", nullable = false)
	private String version;
	
	//TODO colocar User (validador) aqui
	@Column(name = "SCHEDULING_VALIDATOR", nullable = false)
	private String validator;
	
	@Column(name = "SCHEDULING_STATUS", nullable = false)
	private Status status;
	
	public SolicitedScheduling(LocalDate date, LocalTime time, String address, String version, String validator) {
		super();
		this.date = date;
		this.time = time;
		this.address = address;
		this.version = version;
		this.validator = validator;
		this.status = Status.OPEN;
	}
	
	public SolicitedScheduling() {
		
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

	public String getValidator() {
		return validator;
	}

	public void setValidator(String validator) {
		this.validator = validator;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
}
