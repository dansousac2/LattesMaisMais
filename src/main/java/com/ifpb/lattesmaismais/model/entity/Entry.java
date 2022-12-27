package com.ifpb.lattesmaismais.model.entity;

import java.util.List;
import java.util.Objects;

public class Entry {

	private Integer id;
	private String group;
	private String name;
	private List<Receipt> receipts;
	
	public Entry() {
		
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
