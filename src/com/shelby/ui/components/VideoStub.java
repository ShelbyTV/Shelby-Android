package com.shelby.ui.components;

import java.util.Date;

import android.content.Context;

import com.shelby.data.ShelbyDatabase;

public class VideoStub {

	public VideoStub() { super(); listPostion = 0; }
	
	private Long localId;
	private String providerId;
	private Date updated;
	private Integer listPostion;
	private String title;
	
	public Long getLocalId() {
		return localId;
	}
	public void setLocalId(Long localId) {
		this.localId = localId;
	}
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
	public VideoStub getNextStub(Context ctx) {
		ShelbyDatabase sd = new ShelbyDatabase(ctx);
		sd.openRead();
		VideoStub vs = sd.getNextStub(updated.getTime()/1000l);
		vs.setListPostion(listPostion+1);
		sd.close();
		return vs;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setListPostion(Integer listPostion) {
		this.listPostion = listPostion;
	}
	public Integer getListPostion() {
		return listPostion;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	
	
}
