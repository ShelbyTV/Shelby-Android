package com.shelby.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.shelby.Constants;
import com.shelby.R;
import com.shelby.api.SyncUserBroadcasts;
import com.shelby.api.UserHandler;
import com.shelby.ui.components.FlippingImageView;
import com.shelby.ui.components.VideoStub;
import com.shelby.ui.fragments.VideoChooserFragment;
import com.shelby.ui.fragments.VideoChooserFragment.VideoSelectCallbackInterface;
import com.shelby.ui.fragments.VideoPlayerFragment;
import com.shelby.ui.fragments.VideoPlayerFragment.VideoFullScreenCallbackInterface;
import com.shelby.ui.fragments.VideoPlayerFragment.VideoPlayerInterface;
import com.shelby.ui.utility.Flip3DAnimation;
import com.shelby.utility.PrefsManager;

public class HomeActivity extends BaseActivity implements VideoSelectCallbackInterface, VideoPlayerInterface, VideoFullScreenCallbackInterface {
    
	
	VideoPlayerFragment mPlayerFragment;
	VideoChooserFragment mChooserFragment;
	FlippingImageView mFlippingView;
	RelativeLayout mLoadingContainer;
	ProgressBar mLoadingProgress;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (!PrefsManager.hasUserCredentials(this)) {	
	        Intent i = new Intent().setClass(this, LoginActivity.class);
	        startActivity(i);
        }
        mPlayerFragment = (VideoPlayerFragment) getFragmentManager().findFragmentById(R.id.player_fragment);
        mChooserFragment = (VideoChooserFragment) getFragmentManager().findFragmentById(R.id.fragment_video_chooser);
        mFlippingView = (FlippingImageView) findViewById(R.id.loading_logo);
        mFlippingView.setRotationFlags(Flip3DAnimation.FLAG_ROTATE_Y);
        mFlippingView.setVisibility(View.VISIBLE);        
        mLoadingContainer = (RelativeLayout) findViewById(R.id.loading_container);
        mLoadingProgress = (ProgressBar) findViewById(R.id.loading_progress);
        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setTitle("");        
    }
    
    public void onResume() {
    	super.onResume();
    	if (PrefsManager.hasUserCredentials(this)) {
    		new InitialPopulateTask().execute();
    	}
    }

    class InitialPopulateTask extends AsyncTask<Integer, Void, String> {
    	
    	protected void onPreExecute() {
    		mLoadingContainer.setVisibility(View.VISIBLE);
    		mFlippingView.setVisibility(View.VISIBLE);
    		mLoadingProgress.setProgress(20);
    	}
    	
		@Override
		protected String doInBackground(Integer... params) {
			mLoadingProgress.setProgress(25);
			UserHandler.refreshUser(HomeActivity.this);
			mLoadingProgress.setProgress(45);
			SyncUserBroadcasts.sync(HomeActivity.this);
			mLoadingProgress.setProgress(95);
			return null;
		}
		
		protected void onPostExecute(String result) {
			try {
				mLoadingProgress.setProgress(100);
				mLoadingContainer.animate().alpha(0).scaleX(.50f).scaleY(.50f).setDuration(700);    		
	    		mChooserFragment.refreshCursor();
			} catch(Exception ex) {
				if (Constants.DEBUG) ex.printStackTrace();
			}
		}
    }

	public void onVideoSelect(VideoStub vStub) {
		mPlayerFragment.loadVideo(vStub);		
	}

	public void onVideoPlaying(VideoStub vStub) {
		mChooserFragment.setVideoSelected(vStub.getListPostion());
	}

	public void onFullScreen(VideoStub vStub) {
		mChooserFragment.getView().setVisibility(View.GONE);		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	  getMenuInflater().inflate(R.menu.action_bar, menu);
	  return super.onCreateOptionsMenu(menu);
	}
    
}