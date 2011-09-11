package com.shelby.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.shelby.R;
import com.shelby.api.SyncUserBroadcasts;
import com.shelby.api.UserHandler;
import com.shelby.ui.components.FlippingImageView;
import com.shelby.ui.components.VideoStub;
import com.shelby.ui.fragments.VideoChooserFragment;
import com.shelby.ui.fragments.VideoChooserFragment.VideoSelectCallbackInterface;
import com.shelby.ui.fragments.VideoPlayerFragment;
import com.shelby.ui.fragments.VideoPlayerFragment.VideoPlayerInterface;
import com.shelby.ui.utility.Flip3DAnimation;
import com.shelby.utility.PrefsManager;

public class HomeActivity extends BaseActivity implements VideoSelectCallbackInterface, VideoPlayerInterface {
    
	VideoPlayerFragment mPlayerFragment;
	VideoChooserFragment mChooserFragment;
	FlippingImageView mFlippingView;
	RelativeLayout mLoadingContainer;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (!PrefsManager.hasUserCredentials(this)) {
	        Intent i = new Intent().setClass(this, LoginActivity.class);
	        startActivity(i);
        } else {
        	new InitialPopulateTask().execute();
        }
        mPlayerFragment = (VideoPlayerFragment) getFragmentManager().findFragmentById(R.id.player_fragment);
        mChooserFragment = (VideoChooserFragment) getFragmentManager().findFragmentById(R.id.fragment_video_chooser);
        mFlippingView = (FlippingImageView) findViewById(R.id.loading_logo);
        mFlippingView.setRotationFlags(Flip3DAnimation.FLAG_ROTATE_Y);
        mFlippingView.setVisibility(View.VISIBLE);
        mLoadingContainer = (RelativeLayout) findViewById(R.id.loading_container);
    }

    class InitialPopulateTask extends AsyncTask<Integer, Void, String> {
    	
    	protected void onPreExecute() {
    		
    	}
    	
		@Override
		protected String doInBackground(Integer... params) {
			UserHandler.getUser(HomeActivity.this);
			SyncUserBroadcasts.sync(HomeActivity.this);
			return null;
		}
		
		protected void onPostExecute(String result) {
			
		}
    }

	public void onVideoSelect(VideoStub vStub) {
		mPlayerFragment.loadVideo(vStub);		
	}

	public void onVideoPlaying(VideoStub vStub) {
		mChooserFragment.setVideoSelected(vStub.getListPostion());
	}
    
}