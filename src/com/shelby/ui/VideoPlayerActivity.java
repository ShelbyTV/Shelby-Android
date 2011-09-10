package com.shelby.ui;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_video_player);
	}
	
	public void onResume(){
		super.onResume();
		/*
		String video_path = "http://www.youtube.com/watch?v=opZ69P-0Jbc";
		Uri uri = Uri.parse(video_path);

		// With this line the Youtube application, if installed, will launch immediately.
		// Without it you will be prompted with a list of the application to choose.
		uri = Uri.parse("vnd.youtube:"  + uri.getQueryParameter("v"));

		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
			    */
		VideoView videoView = (VideoView)findViewById(R.id.your_video_view);
	    MediaController mc = new MediaController(this);
	    videoView.setMediaController(mc);

	    String str = "rtsp://rtsp2.youtube.com/CiILENy73wIaGQkDwpjrUxOWQBMYESARFEgGUgZ2aWRlb3MM/0/0/0/video.3gp";
	    String videoId = "8qLerGvlD9w";
	    
	    String getInfoString = "http://www.youtube.com/get_video_info?video_id="+videoId;
	    
	    new GetVideoInfoTask(this).execute(getInfoString);
	    
	    
	    
	    Uri uri = Uri.parse(str);
	    videoView.setVideoURI(uri);

	    videoView.requestFocus();
	    videoView.start();

	}
	
	public class GetVideoInfoTask extends AsyncTask<String, Void, String> {
		
		Context context;
		
		
		public GetVideoInfoTask(Context ctx) {
			super();
			context = ctx;
		}
		
		protected void onPreExecute() {
			//this.dialog.setMessage("Sending response...");
			//this.dialog.show();
		}
		protected String doInBackground(String... params) {
			String resp = "";
			try{
			HttpClient client = new DefaultHttpClient();		
			HttpGet getReq = new HttpGet(params[0]);
			//addUserAgentHeader(getReq, appContext);
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
			
			resp = buffer.toString();

			}catch(Exception ex){
				ex.printStackTrace();
			}
			return resp;

		}
		
		protected void onPostExecute(Boolean success) {
			
			//if (!success) {
			//	new GetNotificationsTask().execute();
			//}
				
		}
	}
}
