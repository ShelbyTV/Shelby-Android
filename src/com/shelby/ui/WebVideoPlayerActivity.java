package com.shelby.ui;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebVideoPlayerActivity extends Activity {
	
	private WebView mWebView;
	
	public void onCreate(Bundle grabber){
		super.onCreate(grabber);
		
		mWebView = new WebView(this);
		mWebView.clearCache(true);
		WebSettings webSettings = mWebView.getSettings();
		
		webSettings.setJavaScriptEnabled(true);
		webSettings.setLoadsImagesAutomatically(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setAppCacheEnabled(false);
		
		setContentView(mWebView);
		mWebView.loadUrl("http://www.youtube.com/embed/H-r5jngcaik");
		
	}

}