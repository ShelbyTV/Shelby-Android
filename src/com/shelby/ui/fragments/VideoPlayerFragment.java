package com.shelby.ui.fragments;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.shelby.R;

public class VideoPlayerFragment extends Fragment {
		private VideoView videoView;
	
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
	        View root =  inflater.inflate(R.layout.fragment_video_player, container, false);
			videoView = (VideoView)root.findViewById(R.id.video_view);
		    MediaController mc = new MediaController(getActivity());
		    videoView.setMediaController(mc);
	        return root;
	    }
	  
	  public void onResume(){
			super.onResume();
	  }
		
		public class GetVideoInfoTask extends AsyncTask<String, Void, String> {
						
			public GetVideoInfoTask() {
				super();
			}
			
			protected void onPreExecute() {

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
					//Uri uri = Uri.parse("rtsp://v7.cache1.c.youtube.com/CiILENy73wIaGQn81c-5VsP-JhMYDSANFEgGUgZ2aWRlb3MM/0/0/0/video.3gp");
					//Uri uri = Uri.parse("rtsp://v4.cache5.c.youtube.com/CiILENy73wIaGQn81c-5VsP-JhMYESARFEgGUgZ2aWRlb3MM/0/0/0/video.3gp");
					Uri uri = Uri.parse(resp);
					videoView.setVideoURI(uri);
				    videoView.requestFocus();
				    videoView.start();
				}else{
					//Toast could not load video
				}
					
			}
			
			private String getRTSPUri(String xmlString){
				//dirty parse the rtsp video from gdata
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
		
		public void loadVideo(String videoId){
			
		    //String videoId = "8qLerGvlD9w";
		    String getInfoString = "http://gdata.youtube.com/feeds/api/videos/"+videoId+"?format=6";
		    new GetVideoInfoTask().execute(getInfoString);
		}


}
