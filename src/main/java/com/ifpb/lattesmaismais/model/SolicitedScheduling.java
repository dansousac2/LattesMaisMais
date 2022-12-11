package com.ifpb.lattesmaismais.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.*;

@Entity
@Table(name = "SOLICITED_SCHEDULING")
public class SolicitedScheduling {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SOLICITED_SCHEDULING_ID")
	private Integer id;

	@Column(name = "SCHEDULED_DATE", nullable = false)
	private LocalDate date;
	
	@Column(name = "SCHEDULED_TIME", nullable = false)
	private LocalTime time;
	
	@Column(name = "SCHEDULED_ADDRESS", nullable = false)
	private String address;
	
	@Column(name = "SCHEDULED_VERSION", nullable = false)
	private String version;
	
	@OneToOne
	@JoinTable(
			name = "SOLICITED_VALIDATOR",
			joinColumns = @JoinColumn(name = "SOLICITED_SCHEDULING_ID"),
			inverseJoinColumns = @JoinColumn(name = "USER_ID")
			)
	private User validator;
	
	@Column(name = "SCHEDULED_STATUS", nullable = false)
	private Status status;
	
	public SolicitedScheduling(LocalDate date, LocalTime time, String address, String version, User validator) {
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

	public User getValidator() {
		return validator;
	}

	public void setValidator(User validator) {
		this.validator = validator;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
}
