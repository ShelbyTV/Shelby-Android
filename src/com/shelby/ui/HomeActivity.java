package com.shelby.ui;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.shelby.Constants;
import com.shelby.R;
import com.shelby.api.SyncUserBroadcasts;
import com.shelby.api.UserHandler;
import com.shelby.ui.components.FlippingImageView;
import com.shelby.ui.components.VideoStub;
import com.shelby.ui.fragments.ShareFragment;
import com.shelby.ui.fragments.VideoChooserFragment;
import com.shelby.ui.fragments.VideoChooserFragment.VideoSelectCallbackInterface;
import com.shelby.ui.fragments.VideoPlayerFragment;
import com.shelby.ui.fragments.VideoPlayerFragment.VideoFullScreenCallbackInterface;
import com.shelby.ui.fragments.VideoPlayerFragment.VideoPlayerInterface;
import com.shelby.ui.utility.Flip3DAnimation;
import com.shelby.utility.PrefsManager;

public class HomeActivity extends BaseActivity implements VideoSelectCallbackInterface, VideoPlayerInterface, VideoFullScreenCallbackInterface {
    
	private final int FULL_SCREEN_ACTIVITY = 1;
	
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
    	try {
    		if (mChooserFragment.getView() != null && mChooserFragment.getView().animate() != null)
    			mChooserFragment.getView().animate().translationX(0f).setDuration(500).setListener(new AnimatorListener() {					
					public void onAnimationStart(Animator animation) {}
					public void onAnimationRepeat(Animator animation) {}
					public void onAnimationEnd(Animator animation) {}
					public void onAnimationCancel(Animator animation) {}
				});
    	} catch(Exception ex) {
    		if (Constants.DEBUG) ex.printStackTrace();
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
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {		
			case R.id.full_screen:
				mChooserFragment.getView().animate().translationXBy(400f).setDuration(500).setListener(new AnimatorListener() {					
					public void onAnimationStart(Animator animation) {}					
					public void onAnimationRepeat(Animator animation) {}					
					public void onAnimationEnd(Animator animation) {
						Intent i = new Intent().setClass(HomeActivity.this, FullScreenVideoPlayerActivity.class);
						i.putExtra("local_broadcast_id", mPlayerFragment.getCurrentVideoStub().getLocalId());
						i.putExtra("current_position", mPlayerFragment.getCurrentLocation());
						startActivityForResult(i, FULL_SCREEN_ACTIVITY);
					}					
					public void onAnimationCancel(Animator animation) {}
				});
			break;
			case R.id.share:
				FragmentTransaction ft = getFragmentManager().beginTransaction();
			    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
			    if (prev != null) {
			        ft.remove(prev);
			    }
			    ft.addToBackStack(null);

			    // Create and show the dialog.
			    DialogFragment newFragment = ShareFragment.newInstance(1);
			    newFragment.show(ft, "dialog");
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FULL_SCREEN_ACTIVITY && resultCode == RESULT_OK) {
			if (data.getExtras() != null && data.getExtras().getInt("current_position", 0) > 0) {
				mPlayerFragment.setCurrentLocation(data.getExtras().getInt("current_position", 0));
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
    
}