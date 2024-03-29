package com.shelby.api;

import java.io.IOException;
import java.util.ArrayList;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.shelby.Constants;
import com.shelby.api.bean.User;
import com.shelby.utility.PrefsManager;

public final class UserHandler {

	public static User getUser(Context ctx) {
		
		User u = new User();
		try {
		JSONArray job = new JSONArray(PrefsManager.getUserJSON(ctx));
			if (job.length() > 0) {
				JSONObject user = job.getJSONObject(0);
				if (user.has("_id"))
					u.setId(user.getString("_id"));
				//if (user.has("created_at"))
					//u.setId(user.getString("_id"));
				if (user.has("name"))
					u.setName(user.getString("name"));
				if (user.has("nickname"))
					u.setNickname(user.getString("nickname"));
				if (user.has("total_videos_played"))
					u.setTotalVideosPlayed(user.getInt("total_videos_played"));
				//if (user.has("updated_at"))
					
				if (user.has("user_image"))
					u.setImage(user.getString("user_image"));
			}
		} catch(Exception ex) {
			if (Constants.DEBUG) ex.printStackTrace();
		}		
		return u;
	}
	
	public static void refreshUser(Context ctx) {
		String url = "v2/users.json";
		try {
			String resp = ApiHandler.makeSignedGetRequest(url, ctx);
			JSONArray job = new JSONArray(resp);
			if (job.length() > 0) {
				JSONObject user = job.getJSONObject(0);
				if (user.has("_id"))
					PrefsManager.saveUserJSON(resp, ctx);
			}
		} catch(Exception ex) {
			if (Constants.DEBUG) ex.printStackTrace();
		}
	}
	
	public static void pullAuthentications(Context ctx) {
		String url = "v2/authentications.json";
		try {
			String resp = ApiHandler.makeSignedGetRequest(url, ctx);
			JSONArray job = new JSONArray(resp);
			if (job.length() > 0) {
				for(int i=0; i<job.length(); i++) {
					String type = job.getString(i);
					PrefsManager.saveHasSocializationType(type, ctx);
				}
			}
		} catch(Exception ex) {
			if (Constants.DEBUG) ex.printStackTrace();
		}
	}
	
	public static void markVideoViewed(String broadcastId, Context ctx) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException {
		ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("watched_by_owner", "true"));
		try{
			String resp = ApiHandler.makeSignedPostRequest("v2/broadcasts/"+broadcastId+".json", nvps, ctx);
			JSONArray job = new JSONArray(resp);
			if (job.length() > 0) {
				JSONObject user = job.getJSONObject(0);
				if (user.has("_id"))
					PrefsManager.saveUserJSON(resp, ctx);
			}
		} catch(Exception ex){
			if (Constants.DEBUG) ex.printStackTrace();
		}
	}
	
	public static void markVideoLoved(String broadcastId, Context ctx) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException {
		ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("liked_by_owner", "true"));
		try{
			String resp = ApiHandler.makeSignedPostRequest("v2/broadcasts/"+broadcastId+".json", nvps, ctx);
			JSONArray job = new JSONArray(resp);
			if (job.length() > 0) {
				JSONObject user = job.getJSONObject(0);
				if (user.has("_id"))
					PrefsManager.saveUserJSON(resp, ctx);
			}
		} catch(Exception ex){
			if (Constants.DEBUG) ex.printStackTrace();
		}
	}
	
}
