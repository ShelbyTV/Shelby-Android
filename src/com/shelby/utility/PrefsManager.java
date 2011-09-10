package com.shelby.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.shelby.Constants;

public final class PrefsManager {
	public static boolean hasUserCredentials(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
		return (pref.getString("user_token", null) != null) && (pref.getString("user_secret", null) != null);
	}
}
