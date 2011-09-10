package com.shelby.api;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.shelby.Constants;
import com.shelby.api.bean.User;

public final class UserHandler {

	public static User getUser(Context ctx) {
		String url = "v1/users.json";
		try {
			String resp = ApiHandler.makeSignedGetRequest(url, ctx);
			JSONArray job = new JSONArray(resp);
			if (job.length() > 0) {
				JSONObject user = job.getJSONObject(0);
				if (user.has("_id"))
					new User();
				if (user.has("created_at"))
					new User();
				if (user.has("name"))
					new User();
				if (user.has("nickname"))
					new User();
				if (user.has("total_videos_played"))
					new User();
				if (user.has("updated_at"))
					new User();
				if (user.has("user_image"))
					new User();
			}
		} catch(Exception ex) {
			if (Constants.DEBUG) ex.printStackTrace();
		}
		return null;
	}
	
}
