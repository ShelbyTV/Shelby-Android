package com.shelby.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.shelby.Constants;
import com.shelby.R;

public class DrawableManager {
    private LinkedList<PhotoToLoad> photoQueue = new LinkedList<PhotoToLoad>();
    private PhotosLoader photoLoaderThread;
    //private final 
    //private Integer threadCount; 

    public DrawableManager() {
    }

    public void emptyPhotoQueue() {
    	photoQueue.clear();
    	stopThread();
    }
    
    
    public Drawable fetchDrawable(String urlString, Context ctx) {        
        //try to pull from sd now
    	if (ctx != null) {
	        BitmapDrawable diskBitmap = getDrawableFromDisk(urlString, ctx);
	        if (diskBitmap != null && diskBitmap.getBitmap() != null) {
	        	return diskBitmap;
	        }        	
    	}

        if (Constants.DEBUG) Log.d(this.getClass().getSimpleName(), "photo: image url:" + urlString);
        try {
            //InputStream is = fetch(urlString);
            //Drawable drawable = Drawable.createFromStream(is, "src");
        	BitmapDrawable drawable = new BitmapDrawable(fetch(urlString));
            if (Constants.DEBUG) Log.d(this.getClass().getSimpleName(), "photo: got a thumbnail drawable: " + drawable.getBounds() + ", "
                    + drawable.getIntrinsicHeight() + "," + drawable.getIntrinsicWidth() + ", "
                    + drawable.getMinimumHeight() + "," + drawable.getMinimumWidth());
            if (ctx != null) {
            	saveDrawableToDisk(drawable.getBitmap(), urlString, ctx);
            }
            return drawable;
        } catch (MalformedURLException e) {
            if (Constants.DEBUG) Log.e(this.getClass().getSimpleName(), "photo: fetchDrawable failed", e);
            return null;
        } catch (IOException e) {
            if (Constants.DEBUG) Log.e(this.getClass().getSimpleName(), "photo: fetchDrawable failed", e);
            return null;
        } catch (Exception e) {
        	if (Constants.DEBUG) Log.e(this.getClass().getSimpleName(), "photo: fetchDrawable failed", e);
        	return null;
        }
    }
    
    public BitmapDrawable getDrawableFromDisk(String imageUrl, Context ctx) {
    	try {
    		String path = Environment.getExternalStorageDirectory().toString() + "/shelby/cache/" + md5(imageUrl) + ".jpg";
    		BitmapDrawable d = new BitmapDrawable(ctx.getResources(), path);    		
	    	return d;
    	} catch(Exception ex) {
    		if (Constants.DEBUG) ex.printStackTrace();
    	}
    	return null;
    }
    
    private void saveDrawableToDisk(Bitmap b, String imageUrl, Context ctx) {
    	try {
    		if (b == null)
    			return;
	    	String newFileName = md5(imageUrl) + ".jpg";
	    	
	    	String path = Environment.getExternalStorageDirectory().toString() + "/shelby/cache/";
	    	
	        OutputStream fOut = null;
	        File dir = new File(path);
	        dir.mkdirs();
	        File file = new File(path, newFileName);
	       
	        if (file.createNewFile()) {
		        fOut = new FileOutputStream(file);		        
		        b.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
	            fOut.flush();
	            fOut.close();
	            fOut = null;
	            //MediaStore.Images.Media.insertImage(ctx.getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
	        }
    	} catch(Exception ex) {
    		if (Constants.DEBUG) ex.printStackTrace();
    	}
    }
    
    private String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private Handler handler;
    
    public void queueDrawableFetch(final String urlString, final ImageView imageView, Context ctx) {
    	if (urlString == null || urlString.equals("") || imageView == null) {
    		imageView.setImageResource(R.drawable.icon_avatar);
    		return;
    	} else {
    		if (imageView.getTag() == null)
    			imageView.setTag(urlString);
    	}
        handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
            	try {
	            	if (message != null) {
		            	PhotoToLoad pl = (PhotoToLoad)message.obj;
		            	if (pl != null) {
			            	String tag = (String)pl.imageView.getTag();
			        		if (pl.photo != null && tag.toLowerCase().trim().equals(pl.url.toLowerCase().trim())) {
			        			pl.imageView.setImageDrawable(pl.photo);
			        			if (pl.imageView.getDrawable() == null) {
			        				pl.imageView.setImageResource(R.drawable.icon_avatar);
			        			}
			        		} else {
			        			pl.imageView.setImageResource(R.drawable.icon_avatar);
			        		}
		            	}
	            	}
            	} catch(Exception ex) { 
            		if (Constants.DEBUG) Log.e("DrawableManager", "fail", ex);
            		try {
            			PhotoToLoad pl = (PhotoToLoad)message.obj;
            			if (pl != null) {
            				pl.imageView.setImageResource(R.drawable.icon_avatar);
            			}
            		} catch(Exception ex2) {
            			if (Constants.DEBUG) Log.e("DrawableManager", "fail", ex2);
            		}
            	}
            }
        };    

    	Clean(imageView);
    	photoQueue.addLast(new PhotoToLoad(urlString, imageView));    	
    	if (!_threadRunning) {
    		if (Constants.DEBUG) Log.d(this.getClass().getSimpleName(), "starting photo loader thread");
    		_threadRunning = true;    		
    		try {
    			//photoLoaderThread.stop();
    			photoLoaderThread = new PhotosLoader(ctx);
    			photoLoaderThread.setPriority(Thread.NORM_PRIORITY-1);
    			photoLoaderThread.start();
    		} catch (Exception ex) {
    			Log.e(this.getClass().getSimpleName(), "photo: failed to start photoLoader thread...", ex);
    			_threadRunning = false;    		
    		}
    	}    	
    }
    
    public void Clean(ImageView image)
    {
    	try {
	        for(int j=0 ;j<photoQueue.size();){
	            if(photoQueue.get(j).imageView==image) {
	            	photoQueue.remove(j);
	            } else {
	                ++j;
	            }
	        }
    	} catch(Exception ex) {
    		if (Constants.DEBUG) ex.printStackTrace();
    	}
    }
    
    public void stopThread()
    {
    	if (photoLoaderThread != null) {
	    	if (Constants.DEBUG) Log.d(this.getClass().getSimpleName(), "stopping photo loader thread");
	        photoLoaderThread.interrupt();
    	}
    }
    
    
    private Bitmap fetch(String urlString) throws MalformedURLException, IOException {
    	Bitmap bitmap = null;
    	URL url = new URL(urlString);
    	URLConnection connection = url.openConnection();
    	connection.setUseCaches(true);
    	Bitmap response = BitmapFactory.decodeStream((InputStream) connection.getContent());
    	if(response instanceof Bitmap){
    		 bitmap = (Bitmap)response;
    	}
    	return bitmap;
    }
    
    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public Drawable photo;
        public PhotoToLoad(String u, ImageView i){
            url=u; 
            imageView=i;
        }
    }
    
    private boolean _threadRunning = false;
    
    class PhotosLoader extends Thread {
    	
    	private Context ctx = null;
    	
    	public PhotosLoader(Context ctx) {
    		this.ctx = ctx;
    	}
    	
        public void run() {
            try {
            	Log.d("start", "photo: thread");
                while(photoQueue.size() > 0)
                {
                    //thread waits until there are any images to load in the queue
                    if(photoQueue.size()!=0)
                    {
                        PhotoToLoad photoToLoad;
                        synchronized(photoQueue){
                            photoToLoad=photoQueue.remove(0);
                        }
                        
                        Drawable drw = null;
                        
                       
                        if (drw == null) {
                        	drw = fetchDrawable(photoToLoad.url, ctx);
	                    }
	                        
                        Object tag = photoToLoad.imageView.getTag();

                        if(drw != null && tag!=null && ((String)tag).equals(photoToLoad.url)){
                        	photoToLoad.photo = drw;
                        }
                        Message message = handler.obtainMessage(1, photoToLoad);
                        handler.sendMessage(message);             
                    }
                    if(Thread.interrupted()) {
                    	break;
                    }
                }
                _threadRunning = false;
            } catch (Exception e) {
                //allow thread to exit
            	if (Constants.DEBUG) Log.d(this.getClass().getSimpleName(), "photo: there was an error doing the photo load");
            } finally {
            	_threadRunning = false;
            	if (Constants.DEBUG) Log.d(this.getClass().getSimpleName(), "photo: finally thread photo load done");
            	this.interrupt();
            }
            this.interrupt();
        }
    }

}
