package com.whatsapp.api.request;

public class UpdateUserRequest {

	public String username;
	public String profile_picture;
	public UpdateUserRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UpdateUserRequest(String username, String profile_picture) {
		super();
		this.username = username;
		this.profile_picture = profile_picture;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getProfile_picture() {
		return profile_picture;
	}
	public void setProfile_picture(String profile_picture) {
		this.profile_picture = profile_picture;
	}
	
	
}
