package com.ifpb.lattesmaismais.model.entity;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CURRICULUM_LMM")
public class Curriculum {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CURRICULUM_ID", nullable = false)
	private Integer id;
	
	@Column(name = "CURRICULUM_OWNER", nullable = false)
	private String ownerName;
	
	@Column(name = "CURRICULUM_ENTRY_QTD")
	private int entryCount;
	
	@ManyToMany
	@JoinTable(
			name = "CURRICULUM_ENTRIES",
			joinColumns = @JoinColumn(name = "CURRICULUM_ID"),
			inverseJoinColumns = @JoinColumn(name = "ENTRY_ID")
	)
	private List<Entry> entries;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User owner;
	
	public Curriculum() {
		
	}

	public Curriculum(Integer id, String ownerName, int entryCount, List<Entry> entries, User owner) {
		this.id = id;
		this.ownerName = ownerName;
		this.entryCount = entryCount;
		this.entries = entries;
		this.owner = owner;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
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

	public void setOwnerName(String owner) {
		this.ownerName = owner;
	}

	public int getEntryCount() {
		return entryCount;
	}

	public void setEntryCount(int entryCount) {
		this.entryCount = entryCount;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	@Override
	public int hashCode() {
		return Objects.hash(entries, entryCount, id, ownerName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Curriculum other = (Curriculum) obj;
		return Objects.equals(entries, other.entries) && entryCount == other.entryCount && Objects.equals(id, other.id)
				&& Objects.equals(ownerName, other.ownerName);
	}
	
}
