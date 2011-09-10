package com.shelby.ui.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.shelby.R;
import com.shelby.data.provider.model.DbBroadcast;
import com.shelby.ui.components.VideoStubAdapter;

public class VideoChooserFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private final int VIDEO_STUB_LOADER = 1;
	private VideoStubAdapter mVideoStubAdapter;
	private ListView mListView;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View root =  inflater.inflate(R.layout.fragment_video_chooser, container, false);
		getLoaderManager().initLoader(VIDEO_STUB_LOADER, null, this);
		
		mVideoStubAdapter = new VideoStubAdapter(getActivity(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mListView = (ListView) root.findViewById(R.id.video_listview);
        mListView.setAdapter(mVideoStubAdapter);
		getLoaderManager().getLoader(VIDEO_STUB_LOADER);
		return root;
	}

	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = {
			DbBroadcast._ID
			,DbBroadcast.VIDEO_THUMBNAIL
			,DbBroadcast.VIDEO_TITLE
		};
		return new CursorLoader(getActivity(), DbBroadcast.CONTENT_URI, projection, null, null, null);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
		mVideoStubAdapter.swapCursor(c);
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		mVideoStubAdapter.swapCursor(null);
	}
	
	
}