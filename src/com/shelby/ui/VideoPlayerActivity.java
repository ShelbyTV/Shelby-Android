package com.shelby.ui;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.shelby.R;

public class VideoPlayerActivity extends Activity {
	
	private VideoView videoView;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_player);
	}
	
	public void onResume(){
		super.onResume();
		videoView = (VideoView)findViewById(R.id.your_video_view);
	    MediaController mc = new MediaController(this);
	    videoView.setMediaController(mc);
	    String videoId = "8qLerGvlD9w";
	    
	    String getInfoString = "http://gdata.youtube.com/feeds/mobile/videos/"+videoId+"?format=1";
	    new GetVideoInfoTask(this).execute(getInfoString);

	}
	
	public class GetVideoInfoTask extends AsyncTask<String, Void, String> {
		
		Context context;
		
		
		public GetVideoInfoTask(Context ctx) {
			super();
			context = ctx;
		}
		
		protected void onPreExecute() {
			//this.dialog.setMessage("Loading Video..");
			//this.dialog.show();
		}
		protected String doInBackground(String... params) {
			String retStr = "";
			try{
				HttpClient client = new DefaultHttpClient();		
				HttpGet getReq = new HttpGet(params[0]);
				final HttpResponse response = client.execute(getReq);  
				InputStream inputStream;
				inputStream = response.getEntity().getContent();		
				byte[] data = new byte[512];
				int len = 0;
				StringBuffer buffer = new StringBuffer();
				while (-1 != (len = inputStream.read(data)) )
				{
					buffer.append(new String(data, 0, len)); //converting to string and appending  to stringbuffer�..
				}
				inputStream.close();
				
				String resp = buffer.toString();
				retStr = getRTSPUri(resp);

			}catch(Exception ex){
				ex.printStackTrace();
			}
			return retStr;

		}
		
		protected void onPostExecute(String resp) {
			if (resp!=null){
				Uri uri = Uri.parse(resp);
			    videoView.setVideoURI(uri);
			    videoView.requestFocus();
			    videoView.start();
			}else{
				//Toast could not load video
			}
				
		}
		
		private String getRTSPUri(String xmlString){
			//dirty parse
			String ret = null;
			try{
				Integer start = xmlString.indexOf("rtsp://");
				Integer end = xmlString.indexOf(".3gp", start);
				ret = xmlString.substring(start, end+4);
			}catch(Exception ex){
				ex.printStackTrace();
			}

			return ret;
		}
	}
}
