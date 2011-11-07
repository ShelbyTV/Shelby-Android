package com.shelby.ui.components;

import java.util.Date;

import android.content.Context;

import com.shelby.data.ShelbyDatabase;

public class VideoStub {

	public VideoStub() { super(); listPostion = 0; videoPosition = 0; }
	
	private Long localId;
	private String providerId;
	private String serverBroadcastId;
	private Date updated;
	private Integer listPostion;
	private String title;
	private int videoPosition;
	private String sharerName;
	private String sharerType;
	private String sharerThumb;
	private String description;
	
	public String getSharerName() {
		return sharerName;
	}
	public void setSharerName(String sharerName) {
		this.sharerName = sharerName;
	}
	public String getSharerType() {
		return sharerType;
	}
	public void setSharerType(String sharerType) {
		this.sharerType = sharerType;
	}
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
		if (vs != null)
			vs.setListPostion(listPostion+1);
		sd.close();
		return vs;
	}
	public VideoStub getPrevStub(Context ctx) {
		ShelbyDatabase sd = new ShelbyDatabase(ctx);
		sd.openRead();
		VideoStub vs = sd.getPrevStub(updated.getTime()/1000l);
		if (vs != null)
			vs.setListPostion(listPostion-1);
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
	public int getVideoPosition() {
		return videoPosition;
	}
	public void setVideoPosition(int videoPosition) {
		this.videoPosition = videoPosition;
	}
	public String getServerBroadcastId() {
		return serverBroadcastId;
	}
	public void setServerBroadcastId(String serverBroadcastId) {
		this.serverBroadcastId = serverBroadcastId;
	}
	public String getSharerThumb() {
		return sharerThumb;
	}
	public void setSharerThumb(String sharerThumb) {
		this.sharerThumb = sharerThumb;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
