package com.shelby.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.shelby.Constants;

public final class PrefsManager {
	public static boolean hasUserCredentials(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
		return (pref.getString("user_token", null) != null) && (pref.getString("user_secret", null) != null);
	}
	
	public static void saveUserJSON(String json, Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
		Editor e = pref.edit();
		e.putString("user_json", json);
		e.commit();
	}
	
	public static String getUserJSON(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
		return pref.getString("user_json", "");
	}
}
