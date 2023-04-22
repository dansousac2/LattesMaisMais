package com.ifpb.lattesmaismais.presentation;

import java.util.List;

import com.ifpb.lattesmaismais.model.entity.Entry;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CurriculumDtoBack {

	@Positive(message = "id de currículo não deve ser nulo!")
	private Integer id;
	
	@Positive(message = "CurriculumDto não deve ter id do proprietário nulo!")
	private Integer ownerId;
	
	@Pattern(regexp = "^\\d\\d\\/\\d\\d\\/\\d{4}\\s-\\s\\d\\d:\\d\\d:\\d\\d$", message = "Data de modificação em formato errado!")
	private String lastModification;
	
	@NotBlank(message = "Descrição de currículo não deve ser nula!")
	private String description;
	
	@Min(value = 1, message = "Contador de entradas de currículo deve ser no mínimo 1")
	private int entryCount;
	
	@Size(min = 1, message = "Lista de entradas deve conter ao menos 1 entrada!")
	private List<Entry> entryList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public String getLastModification() {
		return lastModification;
	}

	public void setLastModification(String lastModification) {
		this.lastModification = lastModification;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getEntryCount() {
		return entryCount;
	}

	public void setEntryCount(int entryCount) {
		this.entryCount = entryCount;
	}

	public List<Entry> getEntryList() {
		return entryList;
	}

	public void setEntryList(List<Entry> entryList) {
		this.entryList = entryList;
	}

}
