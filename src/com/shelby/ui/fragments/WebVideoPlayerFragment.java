package com.shelby.ui.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.Toast;

import com.shelby.R;
import com.shelby.ui.components.VideoStub;
import com.shelby.ui.fragments.VideoPlayerFragment.GetVideoInfoTask;

public class WebVideoPlayerFragment extends Fragment {
	
	private static final int BUFFER_SIZE = 1000;
	private static final double ONE_HUNDRED = 100;
	private static final double KB = 8;
	private WebView mWebView;
	private String data; 
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View root =  inflater.inflate(R.layout.fragment_web_video_player, container, false);
		mWebView = (WebView)root.findViewById(R.id.video_view);
		mWebView.clearCache(true);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setPluginState(PluginState.ON);
		webSettings.setLoadsImagesAutomatically(true);
		webSettings.setUseWideViewPort(true);
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
		//data = "<iframe id=\"player\" class=\"youtube-player\" type=\"text/html\" width=\"640\" height=\"385\" src=\"http://www.youtube.com/embed/"+vStub.getProviderId()+"?enablejsapi=1&fs=1&origin=shelby.tv\" frameborder=\"0\">";
		//data = "<object width=\"120\" height=\"73\"><param name=\"movie\" value=\"http://www.youtube.com/watch?v=ZVYIBIlTIQs&feature=youtube_gdata\"></param><param name=\"allowFullScreen\" value=\"true\"></param><param name=\"allowscriptaccess\" value=\"always\"></param><embed src=\"http://www.youtube.com/watch?v=ZVYIBIlTIQs&feature=youtube_gdata\" type=\"application/x-shockwave-flash\" allowscriptaccess=\"always\" allowfullscreen=\"true\" width=\"120\" height=\"73\"></embed></object>";
		
		//mWebView.loadUrl("http://www.youtube.com/embed/ZVYIBIlTIQs");
		//mWebView.loadData(data,"text/html","utf-8");
		
		//String YOUTUBE_PLAYLIST_ID = vStub.getProviderId();
		//Intent lVideoIntent = new Intent(null, Uri.parse("ytv://"+YOUTUBE_PLAYLIST_ID), getActivity(), OpenYouTubePlayerActivity.class);
		//startActivity(lVideoIntent);
		File out = new File(Environment.getExternalStorageDirectory().toString() + "/shelby/");
		try {
			play(vStub.getProviderId(),18,"mp4","",out,"mp4");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void play(String videoId, int format, String encoding,String userAgent, File outputdir, String extension)
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
	                } else if (key.equals("fmt_url_map")) {
	                    String commaPattern = "";
						String[] formats = commaPattern.split(val);
	                    boolean found = false;
	                    for (String fmt : formats) {
	                        String pipePattern = null;
							String[] fmtPieces = pipePattern.split(fmt);
	                        if (fmtPieces.length == 2) {
	                            int pieceFormat = Integer
	                                    .parseInt(fmtPieces[0]);
	                           // log.fine("Available format=" + pieceFormat);
	                            if (pieceFormat == format) {
	                                // found what we want
	                                downloadUrl = fmtPieces[1];
	                                found = true;
	                                break;
	                            }
	                        }
	                    }
	                    if (!found) {
	                       // log.warning("Could not find video matching specified format, however some formats of the video do exist (use -verbose).");
	                    }
	                }
	            }

	            filename = cleanFilename(filename);
	            if (filename.length() == 0) {
	                filename = videoId;
	            } else {
	                filename += "_" + videoId;
	            }
	            filename += "." + extension;


	            File outputfile = new File(outputdir, filename);
	            if (!outputfile.exists()) {
	                outputfile.createNewFile();

	            }
	            //downloadedFile = outputdir.getPath() + "/" + filename;

	            if (downloadUrl != null) {
	                downloadWithHttpClient(userAgent, downloadUrl, outputfile);

	            } else {
	                //log.severe("Could not find video");
	            }
	        } else {
	           // log.severe("Did not receive content from youtube");
	        }
	    } else {
	       // log.severe("Could not contact youtube: " + response.getStatusLine());
	    }
	}

	private static URI getUri(String string, List<NameValuePair> qparams) {
		// TODO Auto-generated method stub
		return null;
	}

	private static String getStringFromInputStream(String encoding,
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

	private static String cleanFilename(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void downloadWithHttpClient(String userAgent,String downloadUrl, File outputfile) throws Throwable {
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
	                outstream.write(buffer, 0, count);
	            }
	            outstream.flush();
	        } finally {
	            outstream.close();
	        }
	        System.out.println("Done");
	    }
	}
	
	
	
	public class GetVideoInfoTask extends AsyncTask<String, Void, String> {
		
		public GetVideoInfoTask() {
			super();
		}
		
		protected void onPreExecute() {
			//loadingSpinner.setVisibility(View.VISIBLE);
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
				//retStr = getRTSPUri(resp);

			}catch(Exception ex){
				ex.printStackTrace();
			}
			return retStr;

		}
		
		protected void onPostExecute(String resp) {
			if (resp!=null){
				//Uri uri = Uri.parse("rtsp://v7.cache1.c.youtube.com/CiILENy73wIaGQn81c-5VsP-JhMYDSANFEgGUgZ2aWRlb3MM/0/0/0/video.3gp");
				//Uri uri = Uri.parse("http://o-o.preferred.lga15s22.v16.lscache4.c.youtube.com/videoplayback?sparams=id,expire,ip,ipbits,itag,ratebypass,oc:U0hQSldSUl9FSkNOMF9PTFZJ&fexp=905228,903114,912603,910207&itag=18&ip=0.0.0.0&signature=C053A1BBD77282D0B983CE68C4243EC9450BE44C.5CC38D9298F4914352A7D461914457387A556815&sver=3&ratebypass=yes&expire=1315774800&key=yt1&ipbits=0&id=e0d5b744b9d79936&quality=medium&fallback_host=tc.v16.cache4.c.youtube.com&type=video/mp4");
				Uri uri = Uri.parse(resp);
				//videoView.setVideoURI(uri);
			    //videoView.requestFocus();
			    //videoView.start();
			}else{
				Toast t = Toast.makeText(getActivity(), "Woops! Something went wrong. Playing the next video son!", Toast.LENGTH_LONG);
				t.show();
				//currentVideoStub = currentVideoStub.getNextStub(getActivity());
				//String getInfoString = "http://gdata.youtube.com/feeds/mobile/videos/"+currentVideoStub.getProviderId()+"?format=1";
			    //new GetVideoInfoTask().execute(getInfoString);
			}
				
		}
		}


}