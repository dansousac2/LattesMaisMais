package com.ifpb.lattesmaismais.model.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "VALIDATOR_COMMENTARY")
@Entity
public class ValidatorCommentary {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VALIDCOMM_ID", nullable = false)
	private Integer id;
	
	@Column(name = "VALIDATOR_ID")
	private Integer validatorId;
	
	@Column(name = "VALIDATOR_NAME")
	private String validatorName;
	
	@Column(name = "VALIDATOR_COMMENTARY")
	private String commentary;
	
	@Column(name = "COMMETARY_DATE")
	private LocalDate date;
	
	public ValidatorCommentary() {
		this.date = LocalDate.now();
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getValidatorId() {
		return validatorId;
	}

	public void setValidatorId(Integer validatorId) {
		this.validatorId = validatorId;
	}

	public String getValidatorName() {
		return validatorName;
	}

	public void setValidatorName(String validatorName) {
		this.validatorName = validatorName;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}
	
}
