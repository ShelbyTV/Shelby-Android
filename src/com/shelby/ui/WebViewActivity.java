package com.shelby.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shelby.R;
import com.shelby.api.ApiHandler;

public class WebViewActivity extends BaseActivity {
	protected void onCreate(Bundle grabber) {
		super.onCreate(grabber);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_web_view);
		WebView wv = (WebView) findViewById(R.id.webview);
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.getString("url") != null) {
			wv.clearCache(true);
			wv.loadUrl(extras.getString("url"));
			wv.setWebViewClient(new WebViewClient() {
				   public boolean shouldOverrideUrlLoading(WebView view, String url) {
					   if (url.contains(ApiHandler.CALL_BACK)) {
						   Intent data = new Intent();
						   data.putExtra("url", url);
						   setResult(RESULT_OK, data);
						   finish();
						   return true;
					   }
					   return false;
				   }
				   public void onLoadResource(WebView view, String url) {
					   if (url.contains(ApiHandler.CALL_BACK)) {
						   Intent data = new Intent();
						   data.putExtra("url", url);
						   setResult(RESULT_OK, data);
						   finish();
					   }
				   }
				   public void onPageStarted(WebView view, String url, Bitmap favicon) {
					   if (url.contains(ApiHandler.CALL_BACK)) {
						   Intent data = new Intent();
						   data.putExtra("url", url);
						   setResult(RESULT_OK, data);
						   finish();
					   }
				   }
				   
				   public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
					   if (url.contains(ApiHandler.CALL_BACK)) {
						   Intent data = new Intent();
						   data.putExtra("url", url);
						   setResult(RESULT_OK, data);
						   finish();
					   }
					   return super.shouldInterceptRequest(view, url);
				   }
			});
		}
	}
}
