package com.ifpb.lattesmaismais.model;

public enum StatusScheduling {
	OPEN("open"),
	ACEITED("aceited"),
	DECLINED("declined"),
	DONE("done");
	
	private String text;

	StatusScheduling(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
}
