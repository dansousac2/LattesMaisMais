package com.ifpb.lattesmaismais.model;

public enum AccountStatus {
	ACTIVE("active"),
	INATIVE("inative"),
	CANCELED("canceled");
	
	private String status;

	AccountStatus(String status) {
		this.status = status;
	}
	
	public String getAccountStatus() {
		return this.status;
	}
}
