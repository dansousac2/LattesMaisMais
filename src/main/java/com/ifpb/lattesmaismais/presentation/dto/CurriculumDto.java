package com.ifpb.lattesmaismais.presentation.dto;

import java.util.List;

import com.ifpb.lattesmaismais.model.entity.Entry;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CurriculumDto {

	private Integer id;
	
	@NotBlank(message = "Nome de usuário não pode ser nulo!")
	private String ownerName;
	
	@Positive(message = "O ID de proprietário deve ser um valor positivo!")
	private int ownerId;
	
	@Positive(message = "O entryCount de proprietário deve ser um valor positivo!")
	private int entryCount;
	
	@Size(min = 1, message = "O currículo deve conter ao menos uma entrada identificada!")
	private List<Entry> entryList;
	
	@NotBlank(message = "Status do currículo não pode ser nulo!")
	private String status;
	
	public CurriculumDto() {
		
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Entry> getEntryList() {
		return entryList;
	}

	public void setEntryList(List<Entry> entryList) {
		this.entryList = entryList;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	public int getOwnerId() {
		return ownerId;
	}
	
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	
	public int getEntryCount() {
		return entryCount;
	}
	
	public void setEntryCount(int entryCount) {
		this.entryCount = entryCount;
	}


	
}
