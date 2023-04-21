package com.ifpb.lattesmaismais.presentation.dto;

public class FileUploadDto {

	private String userId;
	private String userCommentary;
	private String url;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String hashUserId) {
		this.userId = hashUserId;
	}
	public String getUserCommentary() {
		return userCommentary;
	}
	public void setUserCommentary(String commentary) {
		this.userCommentary = commentary;
	}
	
}
