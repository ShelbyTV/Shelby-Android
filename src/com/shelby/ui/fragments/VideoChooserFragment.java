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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;
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
	
	private ImageView mFullListButton;
	private ImageView mFavoritesButton;
	private ImageView mBookmarkButton;
	
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
        mFullListButton = (ImageView) root.findViewById(R.id.full_list);
        mFullListButton.setSelected(true);
        mFavoritesButton = (ImageView) root.findViewById(R.id.favorite_list);
        mBookmarkButton = (ImageView) root.findViewById(R.id.bookmark_list);
		getLoaderManager().getLoader(VIDEO_STUB_LOADER);		
		mVideoLoadInterface = (VideoSelectCallbackInterface) getActivity();
		bindClickListeners();
		return root;
	}
	
	private void bindClickListeners() {
		mFullListButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				getLoaderManager().destroyLoader(VIDEO_STUB_LOADER);
				getLoaderManager().initLoader(VIDEO_STUB_LOADER, null, VideoChooserFragment.this);
				getLoaderManager().getLoader(VIDEO_STUB_LOADER);
				mFullListButton.setSelected(true);
				mFavoritesButton.setSelected(false);
				mBookmarkButton.setSelected(false);
			}
		});
		mFavoritesButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putString("filter_type", "favorites");
				getLoaderManager().destroyLoader(VIDEO_STUB_LOADER);
				getLoaderManager().initLoader(VIDEO_STUB_LOADER, b, VideoChooserFragment.this);
				getLoaderManager().getLoader(VIDEO_STUB_LOADER);
				mFullListButton.setSelected(false);
				mFavoritesButton.setSelected(true);
				mBookmarkButton.setSelected(false);
			}
		});
		mBookmarkButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putString("filter_type", "bookmark");
				getLoaderManager().destroyLoader(VIDEO_STUB_LOADER);
				getLoaderManager().initLoader(VIDEO_STUB_LOADER, b, VideoChooserFragment.this);
				getLoaderManager().getLoader(VIDEO_STUB_LOADER);
				mFullListButton.setSelected(false);
				mFavoritesButton.setSelected(false);
				mBookmarkButton.setSelected(true);
			}
		});
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

	public Loader<Cursor> onCreateLoader(int arg0, Bundle extras) {
		String[] projection = {
			DbBroadcast._ID
			,DbBroadcast.VIDEO_THUMBNAIL
			,DbBroadcast.VIDEO_TITLE
			,DbBroadcast.VIDEO_ID_AT_PROVIDER
			,DbBroadcast.VIDEO_ORIGINATOR_USER_NAME
			,DbBroadcast.VIDEO_ORIGINATOR_USER_IMAGE //5
			,DbBroadcast.CREATED
			,DbBroadcast.SERVER_ID
			,DbBroadcast.VIDEO_ORIGIN
			,DbBroadcast.SHORTENED_LINK
			,DbBroadcast.VIDEO_ORIGINATOR_USER_NICKNAME //10
			,DbBroadcast.WATCHED_BY_OWNER //11
			,DbBroadcast.VIDEO_ORIGINATOR_USER_NICKNAME
			,DbBroadcast.DESCRIPTION
			
		};
		String[] params = {
			"youtube"
		};
		String query = DbBroadcast.VIDEO_PROVIDER + " = ? ";
		if (extras != null && extras.getString("filter_type") != null) {
			if ("favorites".equals(extras.getString("filter_type"))) {
				query += " AND " + DbBroadcast.LIKED_BY_OWNER + " = 1 ";
			} else if ("bookmark".equals(extras.getString("filter_type"))) {
				query += " AND " + DbBroadcast.OWNER_WATCH_LATER + " = 1 ";
			}
		}
		return new CursorLoader(getActivity(), DbBroadcast.CONTENT_URI, projection, query, params, DbBroadcast.CREATED + " desc");
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
