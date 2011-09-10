package com.shelby.api;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

public class ShelbyApi extends DefaultApi10a {

	@Override
	public String getAccessTokenEndpoint() {
		return ApiHandler.URL_ACCESS_TOKEN;
	}

	@Override
	public String getAuthorizationUrl(Token arg0) {
		return ApiHandler.URL_AUTHORIZE;
	}

	@Override
	public String getRequestTokenEndpoint() {
		return ApiHandler.URL_REQUEST_TOKEN;
	}

}
