package com.shelby.ui.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shelby.R;
import com.shelby.data.provider.model.DbBroadcast;
import com.shelby.ui.components.VideoStub;
import com.shelby.ui.components.VideoStubAdapter;
import com.shelby.ui.components.VideoStubAdapter.VideoStubAdapterContainer;

public class VideoChooserFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, VideoStubAdapterContainer {
	
	private final int VIDEO_STUB_LOADER = 1;
	private VideoStubAdapter mVideoStubAdapter;
	private ListView mListView;
	private VideoSelectCallbackInterface mVideoLoadInterface;
	private int currentSelectedVideoPositon = -1;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View root =  inflater.inflate(R.layout.fragment_video_chooser, container, false);
		getLoaderManager().initLoader(VIDEO_STUB_LOADER, null, this);
		
		mVideoStubAdapter = new VideoStubAdapter(getActivity(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER, (VideoStubAdapterContainer) this);
        mListView = (ListView) root.findViewById(R.id.video_listview);
        mListView.setAdapter(mVideoStubAdapter);
        mListView.setFastScrollEnabled(true);
        mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
				setVideoSelected(pos);
				TextView title = (TextView) v.findViewById(R.id.video_title);
				VideoStub prov = (VideoStub) title.getTag();
				mVideoLoadInterface.onVideoSelect(prov);
			}
		});
		getLoaderManager().getLoader(VIDEO_STUB_LOADER);		
		mVideoLoadInterface = (VideoSelectCallbackInterface) getActivity();
		return root;
	}
	
	public void setVideoSelected(int position) {
		if (currentSelectedVideoPositon != position) {
			currentSelectedVideoPositon = position;
			for(int i=0; i<mListView.getChildCount(); i++) {
				View resetView = (View) mListView.getChildAt(i);
				resetView.setBackgroundResource(R.drawable.clickable_background_bevel_gray);
			}
			mListView.setSelection(position);
			setTopIsSelected.start();		
		}
	}
	
	CountDownTimer setTopIsSelected = new CountDownTimer(300, 300) {
		
		@Override
		public void onTick(long millisUntilFinished) {}
		
		@Override
		public void onFinish() {
			View v = (View) mListView.getChildAt(0);
			v.setBackgroundResource(R.drawable.clickable_background_bevel_light_gray);
		}
	};

	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = {
			DbBroadcast._ID
			,DbBroadcast.VIDEO_THUMBNAIL
			,DbBroadcast.VIDEO_TITLE
			,DbBroadcast.VIDEO_ID_AT_PROVIDER
			,DbBroadcast.VIDEO_ORIGINATOR_USER_NAME
			,DbBroadcast.VIDEO_ORIGINATOR_USER_IMAGE
			,DbBroadcast.UPDATED
			,DbBroadcast.SERVER_ID
			,DbBroadcast.VIDEO_ORIGIN
			,DbBroadcast.SHORTENED_LINK
			,DbBroadcast.VIDEO_ORIGINATOR_USER_NICKNAME
		};
		String[] query = {
			"youtube"
		};
		return new CursorLoader(getActivity(), DbBroadcast.CONTENT_URI, projection, " " + DbBroadcast.VIDEO_PROVIDER + " = ? ", query, DbBroadcast.UPDATED + " desc");
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
		mVideoStubAdapter.swapCursor(c);
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		mVideoStubAdapter.swapCursor(null);
	}
	
	public interface VideoSelectCallbackInterface {
		public void onVideoSelect(VideoStub vStub);
	}
	
	public VideoStub getFirstVideo() {
		if (mListView.getChildCount() > 0) {
			View v = (View) mListView.getChildAt(0);
			TextView title = (TextView) v.findViewById(R.id.video_title);
			return (VideoStub) title.getTag();			
		}
		return null;
	}
	
	public void refreshCursor() {
		mVideoStubAdapter.setFirstItemHasntLoaded();
		getLoaderManager().restartLoader(VIDEO_STUB_LOADER, null, VideoChooserFragment.this);
	}

	public void onFirstStubLoaded() {
		VideoStub vs = getFirstVideo();
		if (vs != null)
			mVideoLoadInterface.onVideoSelect(vs);
	}
	
}
