package com.shelby.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.shelby.R;
import com.shelby.api.SyncUserBroadcasts;
import com.shelby.api.UserHandler;
import com.shelby.ui.fragments.VideoChooserFragment.VideoSelectCallbackInterface;
import com.shelby.ui.fragments.VideoPlayerFragment;
import com.shelby.utility.PrefsManager;

public class HomeActivity extends BaseActivity implements VideoSelectCallbackInterface {
    
	VideoPlayerFragment mPlayerFragment;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (!PrefsManager.hasUserCredentials(this)) {
	        Intent i = new Intent().setClass(this, LoginActivity.class);
	        startActivity(i);
        } else {
        	//new InitialPopulateTask().execute();
        }
        mPlayerFragment = (VideoPlayerFragment) getFragmentManager().findFragmentById(R.id.player_fragment);
        
    }

    class InitialPopulateTask extends AsyncTask<Integer, Void, String> {

		@Override
		protected String doInBackground(Integer... params) {
			UserHandler.getUser(HomeActivity.this);
			SyncUserBroadcasts.sync(HomeActivity.this);
			return null;
		}
    	
    }

	public void onVideoSelect(String providerAsId) {
		mPlayerFragment.loadVideo(providerAsId);
	}
    
}