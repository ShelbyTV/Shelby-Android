package com.shelby.ui.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.shelby.R;
import com.shelby.ui.components.FlippingImageView;
import com.shelby.ui.components.VideoStub;
import com.shelby.ui.utility.Flip3DAnimation;

public class VideoPlayerFragment extends Fragment {
	
	private static final int BUFFER_SIZE = 1024;
	private static final double ONE_HUNDRED = 100;
	private static final double KB = 8;
	
		private VideoStub currentVideoStub;
		private VideoView videoView;
		private FlippingImageView loadingSpinner;
	
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
	        View root =  inflater.inflate(R.layout.fragment_video_player, container, false);
	        loadingSpinner = (FlippingImageView) root.findViewById(R.id.loading_video_logo);
	        loadingSpinner.setRotationFlags(Flip3DAnimation.FLAG_ROTATE_Y);
	        loadingSpinner.setVisibility(View.GONE);
			videoView = (VideoView)root.findViewById(R.id.video_view);
		    MediaController mc = new MediaController(getActivity());
		    videoView.setMediaController(mc);
		    videoView.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					currentVideoStub = currentVideoStub.getNextStub(getActivity());
					//String getInfoString = "http://gdata.youtube.com/feeds/mobile/videos/"+currentVideoStub.getProviderId()+"?format=1";
				    //new GetVideoInfoTask().execute(getInfoString);
					new GetFileTask().execute(currentVideoStub);
				}
			});
		    videoView.setOnPreparedListener(new OnPreparedListener() {				
				public void onPrepared(MediaPlayer mp) {
					loadingSpinner.setVisibility(View.GONE);
					//videoView.setVisibility(View.VISIBLE);
					((VideoPlayerInterface) getActivity()).onVideoPlaying(currentVideoStub);
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
				videoView.setVisibility(View.GONE);
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
					//Uri uri = Uri.parse("http://o-o.preferred.lga15s22.v16.lscache4.c.youtube.com/videoplayback?sparams=id,expire,ip,ipbits,itag,ratebypass,oc:U0hQSldSUl9FSkNOMF9PTFZJ&fexp=905228,903114,912603,910207&itag=18&ip=0.0.0.0&signature=C053A1BBD77282D0B983CE68C4243EC9450BE44C.5CC38D9298F4914352A7D461914457387A556815&sver=3&ratebypass=yes&expire=1315774800&key=yt1&ipbits=0&id=e0d5b744b9d79936&quality=medium&fallback_host=tc.v16.cache4.c.youtube.com&type=video/mp4");
					videoView.setVisibility(View.VISIBLE);
					Uri uri = Uri.parse(resp);
					//Uri uri = Uri.parse("http://o-o.preferred.lga16g05.v13.lscache1.c.youtube.com/videoplayback?sparams=id,expire,ip,ipbits,itag,ratebypass,oc:U0hQSldSUl9FSkNOMF9PTFZJ&fexp=904539,904435,913300&itag=18&ip=0.0.0.0&signature=8121DDEDEE1022BA2234DF722C734F82A2559412.6A554BE29849154638F56021409C9F9CF6FB7E5A&sver=3&ratebypass=yes&expire=1315774800&key=yt1&ipbits=0&id=7f91adbfb00a401d&quality=medium");
				    videoView.setVideoURI(uri);

				    videoView.requestFocus();
				    videoView.start();
				} else {
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
		/*
		public void loadVideo(VideoStub vStub){
		    //String videoId = "8qLerGvlD9w";
			currentVideoStub = vStub;
		    String getInfoString = "http://gdata.youtube.com/feeds/mobile/videos/"+currentVideoStub.getProviderId()+"?format=1";
		    new GetVideoInfoTask().execute(getInfoString);
		}
		*/
		
		public interface VideoFullScreenCallbackInterface {
			public void onFullScreen(VideoStub vStub);
		}
		
		public void onFullScreen(){
			
		}


		public interface VideoPlayerInterface {
			public void onVideoPlaying(VideoStub vStub);
		}

		public void loadVideo(VideoStub vStub){
			currentVideoStub = vStub;
			new GetFileTask().execute(currentVideoStub);			
		}
		
		class Thing {
			public Thing() { super(); }
			
			private File file;
			private VideoStub stub;
			public File getFile() {
				return file;
			}
			public void setFile(File file) {
				this.file = file;
			}
			public VideoStub getStub() {
				return stub;
			}
			public void setStub(VideoStub stub) {
				this.stub = stub;
			}
			
		}
		
		class GetFileTask extends AsyncTask<VideoStub, Void, Boolean> {

			protected void onPreExecute() {
				loadingSpinner.setVisibility(View.VISIBLE);
				videoView.setVisibility(View.GONE);
			}
			
			@Override
			protected Boolean doInBackground(VideoStub... params) {
				try {
					return play(params[0].getProviderId(),18,"UTF-8","");					
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
			
			protected void onPostExecute(Boolean happened) {
				if (!happened) {
					Toast t = Toast.makeText(getActivity(), "Woops! Something went wrong. Playing the next video son!", Toast.LENGTH_LONG);
					t.show();
					currentVideoStub = currentVideoStub.getNextStub(getActivity());
					//String getInfoString = "http://gdata.youtube.com/feeds/mobile/videos/"+currentVideoStub.getProviderId()+"?format=1";
				    new GetFileTask().execute(currentVideoStub);
				}
			}
			
		}
		
		public class PlayFileTask extends AsyncTask<String, String, Uri> {

			@Override
			protected Uri doInBackground(String... params) {
				Uri uri = Uri.parse(params[0]);				
				return uri;
			}
			
			protected void onPostExecute(Uri uri) {
				if (uri!=null){
					videoView.setVisibility(View.VISIBLE);
					videoView.setVideoURI(uri);
					videoView.requestFocus();
					videoView.start();
				} else {
					Toast t = Toast.makeText(getActivity(), "Woops! Something went wrong. Playing the next video son!", Toast.LENGTH_LONG);
					t.show();
					currentVideoStub = currentVideoStub.getNextStub(getActivity());
				    new GetFileTask().execute(currentVideoStub);
				}
			}
			
		}
		
		private boolean play(String videoId, int format, String encoding,String userAgent)
		        throws Throwable {
		    //log.fine("Retrieving " + videoId);
		    //List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		    //qparams.add(new BasicNameValuePair("video_id", videoId));
		    //qparams.add(new BasicNameValuePair("fmt", "" + format));
		    //URI uri = getUri("get_video_info", qparams);
		    URI uri = URI.create("http://youtube.com/get_video_info?video_id=" + videoId);
		    System.out.println("************JavaYoutubeDownloade.play() Uri = "
		            + uri.toString());
		    System.out.println("JavaYoutubeDownloade.play() User Agent = "
		            + userAgent);
		    CookieStore cookieStore = new BasicCookieStore();
		    HttpContext localContext = new BasicHttpContext();
		    localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		    HttpClient httpclient = new DefaultHttpClient();
		    HttpGet httpget = new HttpGet(uri);
		    if (userAgent != null && userAgent.length() > 0) {
		        httpget.setHeader("User-Agent", userAgent);
		    }

		    //log.finer("Executing " + uri);
		    HttpResponse response = httpclient.execute(httpget, localContext);
		    HttpEntity entity = response.getEntity();
		    if (entity != null && response.getStatusLine().getStatusCode() == 200) {
		        InputStream instream = entity.getContent();
		        String videoInfo = getStringFromInputStream(encoding, instream);
		        if (videoInfo != null && videoInfo.length() > 0) {
		            List<NameValuePair> infoMap = new ArrayList<NameValuePair>();
		            URLEncodedUtils
		                    .parse(infoMap, new Scanner(videoInfo), encoding);
		            String downloadUrl = null;
		            String filename = videoId;

		            for (NameValuePair pair : infoMap) {
		                String key = pair.getName();
		                String val = pair.getValue();
		               // log.finest(key + "=" + val);
		                if (key.equals("title")) {
		                    filename = val;
		                } else if (key.equals("url_encoded_fmt_stream_map")) {
		                    String[] urls = val.split(",url=");
		                    for(String aUrl : urls) {
		                    	if (aUrl.contains("mp4a")) {
		                    		//good..
		                    		downloadUrl = URLDecoder.decode(aUrl.split("fallback_host")[0]);
		                    		
		                    	}
		                    }
		                }
		            }
		            /*
		            filename = videoId;
		            filename += "." + extension;


		            File outputfile = new File(outputdir, filename);
		            if (!outputfile.exists()) {
		                outputfile.createNewFile();
		            }
		            //downloadedFile = outputdir.getPath() + "/" + filename;
					*/
		            if (downloadUrl != null) {
		            	new PlayFileTask().execute(downloadUrl);
		            	return true;
		                //downloadWithHttpClient(userAgent, downloadUrl, outputfile);

		            } else {
		                //log.severe("Could not find video");
		            }
		        } else {
		           // log.severe("Did not receive content from youtube");
		        }
		    } else {
		       // log.severe("Could not contact youtube: " + response.getStatusLine());
		    }
		    return false;
		}

		private URI getUri(String string, List<NameValuePair> qparams) {
			// TODO Auto-generated method stub
			return null;
		}

		private String getStringFromInputStream(String encoding,
				InputStream instream) {
			String retStr = "";
			try{ 
				byte[] data = new byte[512];
				int len = 0;
				StringBuffer buffer = new StringBuffer();
				while (-1 != (len = instream.read(data)) )
				{
					buffer.append(new String(data, 0, len)); //converting to string and appending  to stringbuffer�..
				}
				instream.close();
				
				return buffer.toString();

			}catch(Exception ex){
				ex.printStackTrace();
			}
			return retStr;
		}

		private void downloadWithHttpClient(String userAgent,String downloadUrl, File outputfile) throws Throwable {
		    HttpGet httpget2 = new HttpGet(downloadUrl);
		    if (userAgent != null && userAgent.length() > 0) {
		        httpget2.setHeader("User-Agent", userAgent);
		    }

		    //log.finer("Executing " + httpget2.getURI());
		    HttpClient httpclient2 = new DefaultHttpClient();
		    HttpResponse response2 = httpclient2.execute(httpget2);
		    HttpEntity entity2 = response2.getEntity();
		    if (entity2 != null && response2.getStatusLine().getStatusCode() == 200) {
		        double length = entity2.getContentLength();
		        if (length <= 0) {
		            // Unexpected, but do not divide by zero
		            length = 1;
		        }
		        InputStream instream2 = entity2.getContent();

		        System.out.println("Writing "
		               // + commaFormatNoPrecision.format(length) + " bytes to "
		                + outputfile);
		        if (outputfile.exists()) {
		            outputfile.delete();
		        }

		        FileOutputStream outstream = new FileOutputStream(outputfile);
		        try {
		        	boolean fileNotPlayed = true;
		            byte[] buffer = new byte[BUFFER_SIZE];
		            double total = 0;
		            int count = -1;
		            int progress = 10;
		            long start = System.currentTimeMillis();
		            while ((count = instream2.read(buffer)) != -1) {
		                total += count;
		                int p = (int) ((total / length) * ONE_HUNDRED);
		                if (p >= progress) {
		                    long now = System.currentTimeMillis();
		                    double s = (now - start) / 1000;
		                    int kbpers = (int) ((total / KB) / s);
		                    System.out.println(progress + "% (" + kbpers + "KB/s)");
		                    progress += 10;
		                }
		                if (progress == 20 && fileNotPlayed) {
		                	new PlayFileTask().execute(outputfile.getAbsolutePath());
		                	fileNotPlayed = false;
		                }
		                outstream.write(buffer, 0, count);
		            }
		            outstream.flush();
		        } finally {
		            outstream.close();
		        }
		        System.out.println("Done");
		    }
		}
}
