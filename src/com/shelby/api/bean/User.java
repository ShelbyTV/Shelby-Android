package com.shelby.api.bean;

import java.util.Date;

public class User {
	
	public User() { super(); }
	
	private String id;
	private Date created;
	private String name;
	private String nickname;
	private Integer totalVideosPlayed;
	private Date updated;
	private String image;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Integer getTotalVideosPlayed() {
		return totalVideosPlayed;
	}
	public void setTotalVideosPlayed(Integer totalVideosPlayed) {
		this.totalVideosPlayed = totalVideosPlayed;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
}
