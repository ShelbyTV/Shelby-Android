package com.shelby.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.shelby.R;
import com.shelby.api.SyncUserBroadcasts;
import com.shelby.ui.components.VideoStub;
import com.shelby.ui.fragments.VideoChooserFragment;
import com.shelby.ui.fragments.VideoChooserFragment.VideoSelectCallbackInterface;
import com.shelby.ui.fragments.VideoPlayerFragment;
import com.shelby.ui.fragments.VideoPlayerFragment.VideoFullScreenCallbackInterface;
import com.shelby.utility.PrefsManager;

public class HomeActivity extends BaseActivity implements VideoSelectCallbackInterface, VideoFullScreenCallbackInterface {
    
	VideoPlayerFragment mPlayerFragment;
	VideoChooserFragment mChooserFragment;
	
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
    }

    class InitialPopulateTask extends AsyncTask<Integer, Void, String> {

		@Override
		protected String doInBackground(Integer... params) {
			//UserHandler.getUser(HomeActivity.this);
			SyncUserBroadcasts.sync(HomeActivity.this);
			return null;
		}
    	
    }

	public void onVideoSelect(VideoStub vStub) {
		mPlayerFragment.loadVideo(vStub);
	}

	public void onFullScreen(VideoStub vStub) {
		mChooserFragment.getView().setVisibility(View.GONE);
		
	}
    
}