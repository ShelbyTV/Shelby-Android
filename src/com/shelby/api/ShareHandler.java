package com.shelby.api;

import java.io.IOException;
import java.util.ArrayList;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.text.TextUtils;

public final class ShareHandler {

	public static void shareVideo(String broadcastId, String comment, boolean facebook, boolean twitter, Context ctx) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException {
		ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
		ArrayList<String> destinations = new ArrayList<String>();
		if (facebook)
			destinations.add("facebook");
		if (twitter)
			destinations.add("twitter");
		nvps.add(new BasicNameValuePair("destination", TextUtils.join(",", destinations)));
		nvps.add(new BasicNameValuePair("comment", comment));
		nvps.add(new BasicNameValuePair("broadcast_id", broadcastId));
		ApiHandler.makeSignedPostRequest("v2/socializations.json", nvps, ctx);
	}
	
}
