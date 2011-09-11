package com.shelby.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.shelby.R;

public class WebVideoPlayerFragment extends Fragment {
	
	private WebView mWebView;
	private String data; 
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View root =  inflater.inflate(R.layout.fragment_web_video_player, container, false);
		mWebView = (WebView)root.findViewById(R.id.video_view);
		mWebView.clearCache(true);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setLoadsImagesAutomatically(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setAppCacheEnabled(false);
		
		//String data = "<iframe src=\"http://player.vimeo.com/video/19767943?title=0&amp;byline=0&amp;portrait=0\" width=\"400\" height=\"225\" frameborder=\"0\" webkitAllowFullScreen allowFullScreen></iframe><p><a href=\"http://vimeo.com/19767943\">Through the Middle</a> from <a href=\"http://vimeo.com/cloudedvision\">Clouded Vision</a> on <a href=\"http://vimeo.com\">Vimeo</a>.</p>";
		//mWebView.loadUrl("http://vimeo.com/19767943");
        return root;
    }
	
	public void onCreate(Bundle grabber){
		super.onCreate(grabber);
		
	}
	
	public void loadVideo(String videoId){
		data = "<iframe class=\"youtube-player\" type=\"text/html\" width=\"640\" height=\"385\" src=\"http://www.youtube.com/embed/"+videoId+"\" frameborder=\"0\">";
		mWebView.loadData(data,"text/html","utf-8");
	}


}