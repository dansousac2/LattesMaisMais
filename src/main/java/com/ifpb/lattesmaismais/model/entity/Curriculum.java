package com.ifpb.lattesmaismais.model.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.ifpb.lattesmaismais.model.enums.CurriculumStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "CURRICULUM")
public class Curriculum implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CURRICULUM_ID", nullable = false)
	private Integer id;
	
	@Column(name = "CURRICULUM_ENTRY_QTD", nullable = false)
	private int entryCount;
	
	@Column(name = "STATUS_CURRICULUM", nullable = false)
	private CurriculumStatus status;

	@Column(name = "VERSION_CURRICULUM", nullable = false, unique = true)
	private String version;

	@Column(name = "DESCRIPTION_CURRICULUM", nullable = false)
	private String description;

	@ManyToOne
	@JoinColumn(name = "OWNER_ID", nullable = false)
	private User owner;
	
	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinTable(
			name = "CURRICULUM_ENTRIES",
			joinColumns = @JoinColumn(name = "CURRICULUM_ID"),
			inverseJoinColumns = @JoinColumn(name = "ENTRY_ID")
	)
	private List<Entry> entries;

	public Curriculum() {
		
	}

	public Curriculum(Integer id, int entryCount, List<Entry> entries, User owner) {
		this.id = id;
		this.entryCount = entryCount;
		this.entries = entries;
		this.owner = owner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public CurriculumStatus getStatus() {
		return status;
	}

	public void setStatus(CurriculumStatus status) {
		this.status = status;
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
		return Objects.hash(description, entries, entryCount, id, owner, status, version);
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
		return Objects.equals(description, other.description) && Objects.equals(entries, other.entries)
				&& entryCount == other.entryCount && Objects.equals(id, other.id) && Objects.equals(owner, other.owner)
				&& status == other.status && Objects.equals(version, other.version);
	}

}
