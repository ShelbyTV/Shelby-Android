package com.shelby.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.shelby.R;
import com.shelby.ui.components.VideoStub;

public class WebVideoPlayerFragment extends Fragment {
	
	private WebView mWebView;
	private String data; 
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View root =  inflater.inflate(R.layout.fragment_web_video_player, container, false);
		mWebView = (WebView)root.findViewById(R.id.video_view);
		mWebView.clearCache(true);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setPluginsEnabled(true);
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
	
	public void loadVideo(VideoStub vStub){
		//data = "<iframe class=\"youtube-player\" type=\"text/html\" width=\"640\" height=\"385\" src=\"http://www.youtube.com/embed/"+videoId+"\" frameborder=\"0\">";
		//data = "<video width=\"320\" height=\"240\" controls=\"controls\"><source src=";
		//data += "http://o-o.preferred.lga15s22.v16.lscache4.c.youtube.com/videoplayback?sparams=id,expire,ip,ipbits,itag,ratebypass,oc:U0hQSldSUl9FSkNOMF9PTFZJ&fexp=905228,903114,912603,910207&itag=18&ip=0.0.0.0&signature=4FB4E329FA137154198E103CB3800EB8C7196996.9C7905A2C20C77A30E87CB77E0E54818F106B64D&sver=3&ratebypass=yes&expire=1315774800&key=yt1&ipbits=0&id=e0d5b744b9d79936&quality=medium&fallback_host=tc.v16.cache4.c.youtube.com&type=video/mp4";
		//data += "type=\"video/mp4\" /><source src=\"movie.ogg\" type=\"video/ogg\" />Your browser does not support the video tag.</video>";
		data = "<iframe class=\"youtube-player\" type=\"text/html\" width=\"640\" height=\"385\" src=\"http://www.youtube.com/embed/Jv7DVrnP1fw?autoplay=1\" frameborder=\"0\">";
		//mWebView.loadUrl("http://www.youtube.com/embed/Jv7DVrnP1fw?autplay=1");
		mWebView.loadData(data,"text/html","utf-8");
	}


}