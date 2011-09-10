package com.shelby.api;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import android.content.Context;

import com.shelby.Constants;

public final class ApiHandler {

	private static final String CONSUMER_KEY = "Kw54Dmtd8ehDcv7O0hqGhRIlTuEYVEva1IzVov0M";
	private static final String CONSUMER_SECRET = "RMLczB11RVTns7fBaZzRQRT6skJgk47vGOn3P0ZG";
	private static final String URL_REQUEST_TOKEN = "http://dev.shelby.tv/oauth/request_token";
	private static final String URL_AUTHORIZE = "http://dev.shelby.tv/oauth/authorize";
	private static final String URL_ACCESS_TOKEN = "http://dev.shelby.tv/oauth/access_token";
	public static final String CALL_BACK = "shelby://android-tablet/";
	
	public static String getAuthorizeURL(Context ctx) {
		OAuthConsumer consumer = new DefaultOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		OAuthProvider provider = new DefaultOAuthProvider(URL_REQUEST_TOKEN, URL_ACCESS_TOKEN, URL_AUTHORIZE);
		
		try {
			return provider.retrieveRequestToken(consumer, CALL_BACK);
		} catch (Exception e) {
			if (Constants.DEBUG) e.printStackTrace();
			return null;
		}
	}
	
	public static String getAccessTokenFromUrl(String url, Context ctx) {
		
		return null;
	}
	
}
