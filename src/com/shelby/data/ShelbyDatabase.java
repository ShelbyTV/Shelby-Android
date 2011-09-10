/**
 * Hashable-Android
 * Sep 6, 2011
 * @author aaron
 * 
 */
package com.shelby.data;

import java.io.IOException;
import java.nio.channels.NonWritableChannelException;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.shelby.api.bean.Broadcast;
import com.shelby.api.bean.Channel;
import com.shelby.data.provider.model.DbBroadcast;
import com.shelby.data.provider.model.DbChannel;

/**
 * @author aaron
 *
 */
public class ShelbyDatabase {
	
	private Context context;
	private ShelbyDatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	public ShelbyDatabase(Context ctx) {
		this.context = ctx;
		this.setDbHelper(new ShelbyDatabaseHelper(this.context));
	}

	/**
	 * @param dbHelper the dbHelper to set
	 */
	public void setDbHelper(ShelbyDatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	/**
	 * @return the dbHelper
	 */
	public ShelbyDatabaseHelper getDbHelper() {
		return dbHelper;
	}
	
	public void openRW() {
		this.db = this.dbHelper.getWritableDatabase();
	}
	
	public void beginTransaction() {
		this.db.beginTransaction();
	}
	
	public void endTransaction() {
		this.db.endTransaction();
	}
	
	public void setTransactionSuccessful() {
		this.db.setTransactionSuccessful();
	}
	
	public void reset() {
		this.dbHelper.reset();
	}
	
	public void exportDb(String path, String file) {
		try {
			this.dbHelper.exportDatabase(path, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close() {
		if (this.db != null)
			this.db.close();
	}
	
	public long insertChannel(Channel channel) {
		if (this.db == null)
			throw new NonWritableChannelException();
		ContentValues content = new ContentValues();
		if (channel.getName() != null && !channel.getName().equals(""))
			content.put(DbChannel.Column.NAME, channel.getName());
		if (channel.getDescription() != null && !channel.getDescription().equals(""))
			content.put(DbChannel.Column.DESCRIPTION, channel.getDescription());
		if (channel.getServerId() != null && !channel.getServerId().equals(""))
			content.put(DbChannel.Column.SERVER_ID, channel.getServerId());
		if (channel.getCreated() != null)
			content.put(DbChannel.Column.CREATED, (channel.getCreated().getTime()/1000)); //store in seconds i guesss
		//if (channel.getName() != null && !channel.getName().equals(""))
		//	content.put(DbChannel.Column.UPDATED, channel.getCreated());
		if (channel.getIsPublic() != null)
			content.put(DbChannel.Column.PUBLIC, channel.getIsPublic());
		
		return this.db.insert(SQL.Table.CHANNEL, null, content);
	}
	
	public boolean insertBroadcast(Broadcast broadcast) {
		if (this.db == null)
			throw new NonWritableChannelException();
		ContentValues content = new ContentValues();
		if (broadcast.getLocalChannelId() != null)
			content.put(DbBroadcast.Column.LOCAL_CHANNEL_ID, broadcast.getLocalChannelId());
		if (broadcast.getServerChannelId() != null && !broadcast.getServerChannelId().equals(""))
			content.put(DbBroadcast.Column.SERVER_CHANNEL_ID, broadcast.getServerChannelId());
		if (broadcast.getServerId() != null && !broadcast.getServerId().equals(""))
			content.put(DbBroadcast.Column.SERVER_ID, broadcast.getServerId());
		if (broadcast.getDescription() != null && !broadcast.getDescription().equals(""))
			content.put(DbBroadcast.Column.DESCRIPTION, broadcast.getDescription());
		if (broadcast.getName() != null && !broadcast.getName().equals(""))
			content.put(DbBroadcast.Column.NAME, broadcast.getName());
		if (broadcast.getTotalPlays() != null)
			content.put(DbBroadcast.Column.TOTAL_PLAYS, broadcast.getTotalPlays());
		if (broadcast.getUserId() != null && !broadcast.getUserId().equals(""))
			content.put(DbBroadcast.Column.USER_ID, broadcast.getUserId());
		if (broadcast.getUserNickName() != null && !broadcast.getUserNickName().equals(""))
			content.put(DbBroadcast.Column.USER_NICKNAME, broadcast.getUserNickName());
		if (broadcast.getUserThumbnail() != null && !broadcast.getUserThumbnail().equals(""))
			content.put(DbBroadcast.Column.USER_THUMBNAIL, broadcast.getUserThumbnail());
		if (broadcast.getVideoDescription() != null && !broadcast.getVideoDescription().equals(""))
			content.put(DbBroadcast.Column.VIDEO_DESCRIPTION, broadcast.getVideoDescription());
		if (broadcast.getVideoIdAtProvider() != null && !broadcast.getVideoIdAtProvider().equals(""))
			content.put(DbBroadcast.Column.VIDEO_ID_AT_PROVIDER, broadcast.getVideoIdAtProvider());
		if (broadcast.getVideoOriginatorUserImage() != null && !broadcast.getVideoOriginatorUserImage().equals(""))
			content.put(DbBroadcast.Column.VIDEO_ORIGINATOR_USER_IMAGE, broadcast.getVideoOriginatorUserImage());
		if (broadcast.getVideoOriginatorUserName() != null && !broadcast.getVideoOriginatorUserName().equals(""))
			content.put(DbBroadcast.Column.VIDEO_ORIGINATOR_USER_NAME, broadcast.getVideoOriginatorUserName());
		if (broadcast.getVideoOriginatorUserNickName() != null && !broadcast.getVideoOriginatorUserNickName().equals(""))
			content.put(DbBroadcast.Column.VIDEO_ORIGINATOR_USER_NICKNAME, broadcast.getVideoOriginatorUserNickName());
		if (broadcast.getVideoPlayer() != null && !broadcast.getVideoPlayer().equals(""))
			content.put(DbBroadcast.Column.VIDEO_PLAYER, broadcast.getVideoPlayer());
		if (broadcast.getVideoProvider() != null && !broadcast.getVideoProvider().equals(""))
			content.put(DbBroadcast.Column.VIDEO_PROVIDER, broadcast.getVideoProvider());
		if (broadcast.getVideoThumbnail() != null && !broadcast.getVideoThumbnail().equals(""))
			content.put(DbBroadcast.Column.VIDEO_THUMBNAIL, broadcast.getVideoThumbnail());
		if (broadcast.getVideoTitle() != null && !broadcast.getVideoTitle().equals(""))
			content.put(DbBroadcast.Column.VIDEO_TITLE, broadcast.getVideoTitle());
		if (broadcast.getVideoUserId() != null && !broadcast.getVideoUserId().equals(""))
			content.put(DbBroadcast.Column.VIDEO_USER_ID, broadcast.getVideoUserId());
		if (broadcast.getVideoUserNickName() != null && !broadcast.getVideoUserNickName().equals(""))
			content.put(DbBroadcast.Column.VIDEO_USER_NICKNAME, broadcast.getVideoUserNickName());
		if (broadcast.getWatchedByOwner() != null)
			content.put(DbBroadcast.Column.WATCHED_BY_OWNER, broadcast.getWatchedByOwner());
		if (broadcast.getUpdated() != null)
			content.put(DbBroadcast.Column.UPDATED, broadcast.getUpdated().getTime()/1000l);
		if (broadcast.getCreated() != null)
			content.put(DbBroadcast.Column.CREATED, broadcast.getCreated().getTime()/1000l);
		this.db.insert(SQL.Table.BROADCAST, null, content);
		return true;
	}
	
}