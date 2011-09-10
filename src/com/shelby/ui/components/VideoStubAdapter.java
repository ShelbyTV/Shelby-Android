package com.shelby.ui.components;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shelby.R;
import com.shelby.utility.DrawableManager;

/**
 * @author aaron
 *
 */
public class VideoStubAdapter extends CursorAdapter {

	private final LayoutInflater mInflater;
	private final DrawableManager mDrawableManger;
	
	private final int clmVideoThumbnail = 1;
	private final int clmVideoTitle = 2;
	
	/**
	 * @param context
	 * @param c
	 */
	public VideoStubAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);	
		mInflater = LayoutInflater.from(context);
		mDrawableManger = new DrawableManager();
		//clmFirstName = c.getColumnIndex(Party.FIRST_NAME);
		//clmLastName = c.getColumnIndex(Party.LAST_NAME);		
	}

	/* (non-Javadoc)
	 * @see android.support.v4.widget.CursorAdapter#bindView(android.view.View, android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View v, Context ctx, Cursor c) {
		ViewHolder vh = null;
		if (v.getTag() != null) {
			vh = (ViewHolder)v.getTag();
		} else {
			vh = new ViewHolder();
			vh.setVideoThumbnail((ImageView) v.findViewById(R.id.video_thumb));
			vh.setVideoTitle((TextView) v.findViewById(R.id.video_title));
		}
		String title = c.getString(clmVideoTitle);
		String thumb = c.getString(clmVideoThumbnail);
		vh.getVideoThumbnail().setTag(null);
		vh.getVideoTitle().setText(title);
		mDrawableManger.queueDrawableFetch(thumb, vh.getVideoThumbnail(), ctx);		
	}

	/* (non-Javadoc)
	 * @see android.support.v4.widget.CursorAdapter#newView(android.content.Context, android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final RelativeLayout view = (RelativeLayout) mInflater.inflate(R.layout.component_video_stub, parent, false);
		return view;
	}

	private class ViewHolder {
		
		public ViewHolder() { super(); }
		
		public void setVideoThumbnail(ImageView videoThumbnail) {
			this.videoThumbnail = videoThumbnail;
		}

		public ImageView getVideoThumbnail() {
			return videoThumbnail;
		}

		public void setVideoTitle(TextView videoTitle) {
			this.videoTitle = videoTitle;
		}

		public TextView getVideoTitle() {
			return videoTitle;
		}

		private ImageView videoThumbnail;
		private TextView videoTitle;
			
	}
}