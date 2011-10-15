package com.shelby.ui;

import java.sql.Date;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.shelby.R;
import com.shelby.data.provider.model.DbBroadcast;
import com.shelby.ui.components.VideoStub;
import com.shelby.ui.fragments.VideoPlayerFragment;
import com.shelby.ui.fragments.VideoPlayerFragment.VideoPlayerInterface;
import com.shelby.utility.PrefsManager;

public class FullScreenVideoPlayerActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, VideoPlayerInterface {
	
	private final int VIDEO_LOADER = 1;
	
	VideoPlayerFragment mPlayerFragment;
	ImageView mCloseFullScreen;
	private long broadcastId = -1;
	private int currentVideoPosition = 0;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video_player);
        mCloseFullScreen = (ImageView) findViewById(R.id.close_full_screen);
        mCloseFullScreen.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				finish();
			}
		});
        if (!PrefsManager.hasUserCredentials(this)) {	
	        Intent i = new Intent().setClass(this, LoginActivity.class);
	        startActivity(i);
        }
        
        mPlayerFragment = (VideoPlayerFragment) getFragmentManager().findFragmentById(R.id.player_fragment);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getLong("local_broadcast_id", -1) > -1) {
        	broadcastId = extras.getLong("local_broadcast_id");
        	currentVideoPosition = extras.getInt("current_position", 0);
        	getLoaderManager().initLoader(VIDEO_LOADER, null, this);
        	getLoaderManager().getLoader(VIDEO_LOADER);
        }
    }
	
	@Override
	protected void onDestroy() {
		Intent i = new Intent();
		i.putExtra("current_position", mPlayerFragment.getCurrentLocation());
		setResult(RESULT_OK, i);
		super.onDestroy();
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = {
				DbBroadcast._ID
				,DbBroadcast.VIDEO_THUMBNAIL
				,DbBroadcast.VIDEO_TITLE
				,DbBroadcast.VIDEO_ID_AT_PROVIDER
				,DbBroadcast.VIDEO_ORIGINATOR_USER_NAME
				,DbBroadcast.VIDEO_ORIGINATOR_USER_IMAGE
				,DbBroadcast.UPDATED
			};
		return new CursorLoader(this, DbBroadcast.CONTENT_URI, projection, DbBroadcast._ID + " = ? ", new String[] { "" + broadcastId }, null);
	}

	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		if (cursor.moveToFirst()) {
			VideoStub vStub = new VideoStub();
			vStub.setLocalId(cursor.getLong(0));
			vStub.setProviderId(cursor.getString(3));
			vStub.setTitle(cursor.getString(2));
			vStub.setUpdated(new Date(1000l*cursor.getLong(6)));
			vStub.setListPostion(0);
			vStub.setVideoPosition(currentVideoPosition);
			mPlayerFragment.loadVideo(vStub);
		}
		
	}

	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onVideoPlaying(VideoStub vStub) {
		
	}
}
