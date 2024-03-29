package com.shelby.ui.components;

import java.util.Date;

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
import com.shelby.utility.DateUtil;
import com.shelby.utility.DrawableManager;

/**
 * @author aaron
 *
 */
public class VideoStubAdapter extends CursorAdapter {

	private final LayoutInflater mInflater;
	private final DrawableManager mDrawableManger;
	private boolean firstItemHasLoaded = false;
	private VideoStubAdapterContainer mVideoStubAdapterContainer;
	
	private final int clmLocalId = 0;
	private final int clmVideoThumbnail = 1;
	private final int clmVideoTitle = 2;
	private final int clmVideoIdAtProvider = 3;
	private final int clmVideoOrigName = 4;
	private final int clmVideoOrigThumb = 5;
	private final int clmUpdated = 6;
	private final int clmServerId = 7;
	private final int clmVideoOrigin = 8;
	//private final int clmShortenedLink = 9;
	private final int clmVideoOriginatorUserNickname = 10;
	private final int clmWatched = 11;
	private final int clmNickName = 12;
	private final int clmDescription = 13;
	
	/**
	 * @param context
	 * @param c
	 */
	public VideoStubAdapter(Context context, Cursor c, int flags, VideoStubAdapterContainer vsac) {
		super(context, c, flags);	
		mInflater = LayoutInflater.from(context);
		mDrawableManger = new DrawableManager();	
		mVideoStubAdapterContainer = vsac; //lol
	}

	@Override
	public void bindView(View v, Context ctx, Cursor c) {
		ViewHolder vh = null;
		if (v.getTag() != null) {
			vh = (ViewHolder)v.getTag();
		} else {
			vh = new ViewHolder();
			vh.setVideoThumbnail((ImageView) v.findViewById(R.id.video_thumb));
			vh.setVideoTitle((TextView) v.findViewById(R.id.video_title));
			vh.setSharerName((TextView) v.findViewById(R.id.sharer_name));
			vh.setSharerPhoto((ImageView) v.findViewById(R.id.sharer_photo));
			vh.setSharerSharedSince((TextView) v.findViewById(R.id.sharer_shared_since));
			vh.setMarker((ImageView) v.findViewById(R.id.marker));
			vh.setDescription((TextView) v.findViewById(R.id.description));
		}
		String title = c.getString(clmVideoTitle);
		String thumb = c.getString(clmVideoThumbnail);
		String videoAtProvider = c.getString(clmVideoIdAtProvider);
		String sharerName = c.getString(clmVideoOrigName);
		String sharerPhoto = c.getString(clmVideoOrigThumb);
		String videoOrigin = c.getString(clmVideoOrigin);
		String nickname = c.getString(clmNickName);
		boolean watched = c.getInt(clmWatched) == 1;
		String description = c.getString(clmDescription);
		Long updated = c.getLong(clmUpdated)*1000l;
		VideoStub vStub = new VideoStub();
		vStub.setLocalId(c.getLong(clmLocalId));
		vStub.setProviderId(videoAtProvider);
		vStub.setUpdated(new Date(updated));
		vStub.setListPostion(c.getPosition());
		vStub.setServerBroadcastId(c.getString(clmServerId));
		vStub.setSharerType(c.getString(clmVideoOrigin));
		vStub.setSharerName(c.getString(clmVideoOriginatorUserNickname));
		vStub.setTitle(title);
		vStub.setSharerThumb(sharerPhoto);
		vStub.setDescription(description);
		vh.getVideoTitle().setTag(vStub);
		vh.getVideoThumbnail().setTag(null);
		vh.getVideoTitle().setText(title);		
		vh.getSharerPhoto().setTag(null);
		vh.getSharerSharedSince().setText(DateUtil.formatDateForStream("" + (updated/1000l), true));
		mDrawableManger.queueDrawableFetch(thumb, vh.getVideoThumbnail(), ctx);
		mDrawableManger.queueDrawableFetch(sharerPhoto, vh.getSharerPhoto(), ctx);
		if (!firstItemHasLoaded) {
			firstItemHasLoaded = true;
			mVideoStubAdapterContainer.onFirstStubLoaded();
		}
		vh.getSharerName().setText(nickname);
		if (videoOrigin != null) {			
			if ("twitter".equals(videoOrigin)) {
				if (watched)
					vh.getMarker().setImageResource(R.drawable.marker_twitter_watched);
				else
					vh.getMarker().setImageResource(R.drawable.marker_twitter_new);
				vh.getSharerName().setText("@" + nickname);
			} else if ("facebook".equals(videoOrigin)) {
				if (watched)
					vh.getMarker().setImageResource(R.drawable.marker_facebook_watched);
				else
					vh.getMarker().setImageResource(R.drawable.marker_facebook_new);				
			} else if ("tumblr".equals(videoOrigin)) {
				if (watched)
					vh.getMarker().setImageResource(R.drawable.marker_tumblr_watched);
				else
					vh.getMarker().setImageResource(R.drawable.marker_tumblr_new);
			}
		}
		if (description != null && !description.equals("null")) {
			vh.getDescription().setText(description);
		}
		
	}

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
		
		public ImageView getSharerPhoto() {
			return sharerPhoto;
		}

		public void setSharerPhoto(ImageView sharerPhoto) {
			this.sharerPhoto = sharerPhoto;
		}

		public TextView getSharerName() {
			return sharerName;
		}

		public void setSharerName(TextView sharerName) {
			this.sharerName = sharerName;
		}

		public void setSharerSharedSince(TextView sharerSharedSince) {
			this.sharerSharedSince = sharerSharedSince;
		}

		public TextView getSharerSharedSince() {
			return sharerSharedSince;
		}

		public ImageView getMarker() {
			return marker;
		}

		public void setMarker(ImageView marker) {
			this.marker = marker;
		}



		public TextView getDescription() {
			return description;
		}

		public void setDescription(TextView description) {
			this.description = description;
		}



		private ImageView videoThumbnail;
		private TextView videoTitle;
		private ImageView sharerPhoto;
		private TextView sharerName;
		private TextView sharerSharedSince;
		private ImageView marker;
		private TextView description;
			
	}
	
	public interface VideoStubAdapterContainer {
		public void onFirstStubLoaded();
	}
	
	public void setFirstItemHasntLoaded() {
		firstItemHasLoaded = false;
	}
}