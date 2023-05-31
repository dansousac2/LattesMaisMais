package com.ifpb.lattesmaismais.model.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import com.ifpb.lattesmaismais.model.enums.SchedulingStatus;
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
	
	@ManyToOne
	@JoinColumn(name = "VALIDATOR_ID")
	private User validator;
	
	@ManyToOne
	@JoinColumn(name = "REQUESTER_ID")
	private User requester;
	
	@Column(name = "SCHEDULING_STATUS", nullable = false)
	private SchedulingStatus status;
	
	@Column(name = "SCHEDULING_VALIDATOR_MESSAGE")
	private String returnedValidatorMessage;

	public SolicitedScheduling() {
		this.status = SchedulingStatus.OPEN;
	}

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

	public User getRequester() {
		return requester;
	}

	public void setRequester(User requester) {
		this.requester = requester;
	}

	public SchedulingStatus getStatus() {
		return status;
	}

	public void setStatus(SchedulingStatus status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, date, id, status, time, validator, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SolicitedScheduling other = (SolicitedScheduling) obj;
		return Objects.equals(address, other.address) && Objects.equals(date, other.date)
				&& Objects.equals(id, other.id) && status == other.status && Objects.equals(time, other.time)
				&& Objects.equals(validator, other.validator) && Objects.equals(version, other.version);
	}
	
}
