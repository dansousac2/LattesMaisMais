package com.ifpb.lattesmaismais.model;

public enum Status {
	OPEN("open"),
	ACEITED("aceited"),
	DECLINED("declined"),
	DONE("done");
	
	private String text;

	private Status(String text) {
		this.text = text;
	}
	
	private String getText() {
		return this.text;
	}
}
