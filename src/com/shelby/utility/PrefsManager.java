package com.shelby.utility;

import java.util.Date;

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
	
	public static void saveSocializationsJSON(String json, Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
		Editor e = pref.edit();
		e.putString("user_socializations", json);
		e.commit();
	}
	
	public static void saveHasSocializationType(String type, Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
		Editor e = pref.edit();
		e.putBoolean("has_" + type, true);
		e.commit();
	}
	
	public static boolean getHasSocialType(String type, Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
		return pref.getBoolean("has_" + type, false);
	}
	
	public static String getUserJSON(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
		return pref.getString("user_json", "");
	}
	
	public static long getSinceBroadcasts(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
		return pref.getLong("since_broadcasts", 0);
	}
	
	public static void setSinceBroadcastToNow(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
		Editor e = pref.edit();
		e.putLong("since_broadcasts", (new Date()).getTime()/1000l);
		e.commit();
	}
}
