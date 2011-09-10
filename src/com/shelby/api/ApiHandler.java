package com.shelby.api;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.content.Context;

import com.shelby.Constants;

public final class ApiHandler {

	private static final String CONSUMER_KEY = "NSvFtuLXa8rmgtzNaaJurh3NabBoDSaBt9LMjijS";
	private static final String CONSUMER_SECRET = "tuIWxJMCdV71SUFR6xZsdmlqWD39aLugbteas4qz";
	private static final String URL_REQUEST_TOKEN = "http://dev.shelby.tv/oauth/request_token";
	private static final String URL_AUTHORIZE = "http://dev.shelby.tv/oauth/authorize";
	private static final String URL_ACCESS_TOKEN = "http://dev.shelby.tv/oauth/access_token";
	public static final String CALL_BACK = "http://android.shelby.tv/callback";
	
	public static String getAuthorizeURL(Context ctx) {
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		OAuthProvider provider = new CommonsHttpOAuthProvider(URL_REQUEST_TOKEN, URL_ACCESS_TOKEN, URL_AUTHORIZE);
		provider.setOAuth10a(true);
		try {
			return provider.retrieveRequestToken(consumer, CALL_BACK);
		} catch (Exception e) {
			if (Constants.DEBUG) e.printStackTrace();
			return null;
		}
	}
	
	public static String getAccessTokenFromUrl(String url, Context ctx) {
		String[] splits = url.split("?");
		return splits[1];
	}
	
}
