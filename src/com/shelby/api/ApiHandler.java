package com.shelby.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.PlainTextMessageSigner;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.shelby.Constants;

public final class ApiHandler {

	public static final String CALL_BACK = "http://dev.shelby.tv/callback";
	
	
	private static final String CONSUMER_KEY = "lBtEAt7k1qTDCYmh5pXTupPUxAmgSdxakGhIzMDN";
	private static final String CONSUMER_SECRET = "b8AiGugseNC05GlwYO5ImlfQrQT00gWZ3Nv3s8ER";
	public static final String URL_REQUEST_TOKEN = "http://dev.shelby.tv/oauth/request_token";
	public static final String URL_AUTHORIZE = "http://dev.shelby.tv/oauth/authorize";
	public static final String URL_ACCESS_TOKEN = "http://dev.shelby.tv/oauth/access_token";
	public static final String BASE_URL = "http://api.shelby.tv/";
	
	/*
	private static final String CONSUMER_KEY = "jWMcAmyjJiJx63cKaGin7A";
	private static final String CONSUMER_SECRET = "gEQh8zcwoZ4h3YbyEoTRQNXYPs5CBFfTzooRC5hY4o";
	public static final String URL_REQUEST_TOKEN = "https://api.twitter.com/oauth/request_token";
	public static final String URL_AUTHORIZE = "https://api.twitter.com/oauth/authorize";
	public static final String URL_ACCESS_TOKEN = "https://api.twitter.com/oauth/access_token";
	
	public static final String BASE_URL = "http://api.twitter.com/1/";
	*/
	private static CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
	private static CommonsHttpOAuthProvider provider = new CommonsHttpOAuthProvider(URL_REQUEST_TOKEN, URL_ACCESS_TOKEN, URL_AUTHORIZE);
	
	public static CommonsHttpOAuthConsumer getConsumer(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
		consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		String tkn = pref.getString("user_token", ""); 
		//tkn = "MSmipoekUaaGMCor64ltPcMoS1raRrrnXyHt5CwZ";
		String scrt = pref.getString("user_secret", ""); 
		//scrt = "7pkVgJI7rqJLnxfkYaXp6d1Q3QkRY3S7CUxhXHXS";
		consumer.setMessageSigner(new PlainTextMessageSigner());
        consumer.setTokenWithSecret(tkn, scrt);
     
		return consumer;
	}
	
	public static OAuthService getOauthService() {
		OAuthService service =  new ServiceBuilder()
	         .provider(ShelbyApi.class)
	         .apiKey(CONSUMER_KEY)
	         .apiSecret(CONSUMER_SECRET)
	         .build();
		
		return service;
	}
	
	public static Token getAccessToken(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
		String tkn = pref.getString("user_token", ""); //"MSmipoekUaaGMCor64ltPcMoS1raRrrnXyHt5CwZ";
		String scrt = pref.getString("user_secret", ""); //"7pkVgJI7rqJLnxfkYaXp6d1Q3QkRY3S7CUxhXHXS";
		return new Token(tkn, scrt);
	}
	
	public static String makeSignedGetRequest(String urlLoc, Context ctx) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException {
		CommonsHttpOAuthConsumer consumer = getConsumer(ctx);
		
		// create an HTTP request to a protected resource
        HttpGet request = new HttpGet(BASE_URL + urlLoc);

        // sign the request
        HttpRequest hr = consumer.sign(request);
        // send the request
        HttpClient httpClient = new DefaultHttpClient();

        //request.setHeader("oauth_signature", request.getHeaders("oauth_signature")[0] + consumer.getTokenSecret());
        HttpResponse response = httpClient.execute(request);
        InputStream in = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        in.close();
        return sb.toString();
	}
	
	public static String getAuthorizeURL(Context ctx) {
		
		provider.setOAuth10a(true);
		try {
			return provider.retrieveRequestToken(consumer, CALL_BACK);
		} catch (Exception e) {
			if (Constants.DEBUG) e.printStackTrace();
			return null;
		}
	}
	
	public static boolean getAccessTokenAndKey(String url, Context ctx) {
		provider.setOAuth10a(true);
		String[] urlTokens = getAccessTokenFromUrl(url);
		try {
			provider.retrieveAccessToken(consumer, urlTokens[1]);
			String tkn = consumer.getToken();
			String scrt = consumer.getTokenSecret();
			SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
			Editor e = pref.edit();
			e.putString("user_token", tkn);
			e.putString("user_secret", scrt);
			e.commit();
			return true;
		} catch (Exception e) {
			if (Constants.DEBUG) e.printStackTrace();
		}
		return false;
	}
	
	private static String[] getAccessTokenFromUrl(String url) {
		String[] splits = url.split("\\?");
		if (splits.length == 2) {
			String[] split2 = splits[1].split("&oauth_verifier=");
			if (split2.length == 2) {
				String verifier = split2[1];
				String[] split3 = split2[0].split("oauth_token=");
				if (split3.length == 2) {
					return new String[] {
						split3[1]
						,verifier
					};
				}
			}
		}
		return null;
	}
	
}
