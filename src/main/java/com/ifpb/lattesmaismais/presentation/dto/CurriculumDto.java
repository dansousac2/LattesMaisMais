package com.ifpb.lattesmaismais.presentation.dto;

import java.util.List;

import com.ifpb.lattesmaismais.model.entity.Entry;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CurriculumDto {

	@Positive(message = "O id do currículo deve ser um valor positivo!")
	private int id;
	
	@Positive(message = "O entryCount de proprietário deve ser um valor positivo!")
	private int entryCount;

	@NotBlank(message = "Status do currículo não pode ser nulo!")
	private String status;

	@NotBlank(message = "Versão não pode ser nula!")
	private String version;

	@NotBlank(message = "Descrição não pode ser nula!")
	private String description;

	@Positive(message = "O ID de proprietário deve ser um valor positivo!")
	private int ownerId;
	
	@NotBlank(message = "Nome de usuário não pode ser nulo!")
	private String ownerName;
	
	// dd/MM/yyyy - HH:mm:ss
	@Pattern(regexp = "^\\d\\d\\/\\d\\d\\/\\d{4}\\s-\\s\\d\\d:\\d\\d:\\d\\d$", message = "Data de modificação em formato errado!")
	private String lastModification;
	
	@Size(min = 1, message = "O currículo deve conter ao menos uma entrada identificada!")
	private List<Entry> entryList;
	
	public CurriculumDto() {
		
	}
	
	public String getLastModification() {
		return lastModification;
	}

	public void setLastModification(String lastModification) {
		this.lastModification = lastModification;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
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
