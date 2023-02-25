package com.ifpb.lattesmaismais.model.entity;

import java.io.Serializable;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;

@Entity
@Table(name = "ENTRY")
public class Entry implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ENTRY_ID", nullable = false)
	private Integer id;
	
	@Column(name = "ENTRY_GROUP", nullable = false)
	private String group;
	
	@Column(name = "ENTRY_NAME", nullable = false)
	private String name;
	
	@JoinTable(
			name = "ENTRY_RECEIPTS",
			joinColumns = @JoinColumn(name = "ENTRY_ID"),
			inverseJoinColumns = @JoinColumn(name = "RECEIPT_ID")
	)
	@OneToMany
	private List<Receipt> receipts;
	
	public Entry() {
		
	}

	public Entry(Integer id, String group, String name, List<Receipt> receipts) {
		super();
		this.id = id;
		this.group = group;
		this.name = name;
		this.receipts = receipts;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Receipt> getReceipts() {
		return receipts;
	}

	public void setReceipts(List<Receipt> receipts) {
		this.receipts = receipts;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, receipts);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entry other = (Entry) obj;
		return Objects.equals(name, other.name) && Objects.equals(receipts, other.receipts);
	}
	
}
