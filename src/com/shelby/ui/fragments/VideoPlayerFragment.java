package com.shelby.ui.fragments;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.shelby.R;
import com.shelby.ui.components.VideoStub;

public class VideoPlayerFragment extends Fragment {
	
		private VideoStub currentVideoStub;
		private VideoView videoView;
		private ProgressBar loadingSpinner;
	
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
	        View root =  inflater.inflate(R.layout.fragment_video_player, container, false);
	        loadingSpinner = (ProgressBar) root.findViewById(R.id.loading_spinner);
			videoView = (VideoView)root.findViewById(R.id.video_view);
		    MediaController mc = new MediaController(getActivity());
		    videoView.setMediaController(mc);
		    videoView.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					currentVideoStub = currentVideoStub.getNextStub(getActivity());
					String getInfoString = "http://gdata.youtube.com/feeds/mobile/videos/"+currentVideoStub.getProviderId()+"?format=1";
				    new GetVideoInfoTask().execute(getInfoString);
				}
			});
		    videoView.setOnPreparedListener(new OnPreparedListener() {				
				public void onPrepared(MediaPlayer mp) {
					loadingSpinner.setVisibility(View.GONE);
				}
			});
		    videoView.setOnErrorListener(new OnErrorListener() {				
				public boolean onError(MediaPlayer mp, int what, int extra) {
					Toast t = Toast.makeText(getActivity(), "Woops! Something went wrong. Try another video yo.", Toast.LENGTH_LONG);
					t.show();
					return false;
				}
			});
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
				loadingSpinner.setVisibility(View.VISIBLE);
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
					Toast t = Toast.makeText(getActivity(), "Woops! Something went wrong. Playing the next video son!", Toast.LENGTH_LONG);
					t.show();
					currentVideoStub = currentVideoStub.getNextStub(getActivity());
					String getInfoString = "http://gdata.youtube.com/feeds/mobile/videos/"+currentVideoStub.getProviderId()+"?format=1";
				    new GetVideoInfoTask().execute(getInfoString);
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
		
		public void loadVideo(VideoStub vStub){
		    //String videoId = "8qLerGvlD9w";
			currentVideoStub = vStub;
		    String getInfoString = "http://gdata.youtube.com/feeds/mobile/videos/"+currentVideoStub.getProviderId()+"?format=1";
		    new GetVideoInfoTask().execute(getInfoString);
		}


}
