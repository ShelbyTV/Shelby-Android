package com.shelby.data.provider.model;

import android.net.Uri;

import com.shelby.data.SQL.Table;
import com.shelby.data.provider.BroadcastProvider;

public class DbBroadcast {
	
	public DbBroadcast() { super(); }
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + BroadcastProvider.AUTHORITY + "/broadcast");
	
	public static final String CONTENT_TYPE = "vnd.android.cursor/dir/vnd.shelby.broadcast";
	
	public static final String _ID = Table.BROADCAST + "." + Column._ID;
	public static final String LOCAL_CHANNEL_ID = Table.BROADCAST + "." + Column.LOCAL_CHANNEL_ID;
	public static final String SERVER_CHANNEL_ID = Table.BROADCAST + "." + Column.SERVER_CHANNEL_ID;
	public static final String SERVER_ID = Table.BROADCAST + "." + Column.SERVER_ID; 
	public static final String DESCRIPTION = Table.BROADCAST + "." + Column.DESCRIPTION;
	public static final String NAME = Table.BROADCAST + "." + Column.NAME;
	public static final String TOTAL_PLAYS = Table.BROADCAST + "." + Column.TOTAL_PLAYS;
	public static final String USER_ID = Table.BROADCAST + "." + Column.USER_ID;
	public static final String USER_NICKNAME = Table.BROADCAST + "." + Column.USER_NICKNAME;
	public static final String USER_THUMBNAIL = Table.BROADCAST + "." + Column.USER_THUMBNAIL;
	public static final String VIDEO_DESCRIPTION = Table.BROADCAST + "." + Column.VIDEO_DESCRIPTION;
	public static final String VIDEO_ID_AT_PROVIDER = Table.BROADCAST + "." + Column.VIDEO_ID_AT_PROVIDER;
	public static final String VIDEO_ORIGINATOR_USER_IMAGE = Table.BROADCAST + "." + Column.VIDEO_ORIGINATOR_USER_IMAGE;
	public static final String VIDEO_ORIGINATOR_USER_NAME = Table.BROADCAST + "." + Column.VIDEO_ORIGINATOR_USER_NAME;
	public static final String VIDEO_ORIGINATOR_USER_NICKNAME = Table.BROADCAST + "." + Column.VIDEO_ORIGINATOR_USER_NICKNAME;
	public static final String VIDEO_PLAYER = Table.BROADCAST + "." + Column.VIDEO_PLAYER;
	public static final String VIDEO_PROVIDER = Table.BROADCAST + "." + Column.VIDEO_PROVIDER;
	public static final String VIDEO_THUMBNAIL = Table.BROADCAST + "." + Column.VIDEO_THUMBNAIL;
	public static final String VIDEO_TITLE = Table.BROADCAST + "." + Column.VIDEO_TITLE;
	public static final String WATCHED_BY_OWNER = Table.BROADCAST + "." + Column.WATCHED_BY_OWNER;
	public static final String UPDATED = Table.BROADCAST + "." + Column.UPDATED;
	public static final String CREATED = Table.BROADCAST + "." + Column.CREATED;
	public static final String LIKED_BY_OWNER = Table.BROADCAST + "." + Column.LIKED_BY_OWNER;
	public static final String OWNER_WATCH_LATER = Table.BROADCAST + "." + Column.OWNER_WATCH_LATER;
	public static final String SHORTENED_LINK = Table.BROADCAST + "." + Column.SHORTENED_LINK;
	public static final String VIDEO_ORIGIN = Table.BROADCAST + "." + Column.VIDEO_ORIGIN;
	
	public static final class Column {
		public static final String _ID = "_id";
		public static final String LOCAL_CHANNEL_ID = "local_channel_id";
		public static final String SERVER_CHANNEL_ID = "server_channel_id";
		public static final String SERVER_ID = "server_id"; 
		public static final String DESCRIPTION = "description";
		public static final String NAME = "name";
		public static final String TOTAL_PLAYS = "total_plays";
		public static final String USER_ID = "user_id";
		public static final String USER_NICKNAME = "user_nickname";
		public static final String USER_THUMBNAIL = "user_thumbnail";
		public static final String VIDEO_DESCRIPTION = "video_description";
		public static final String VIDEO_ID_AT_PROVIDER = "video_id_at_provider";
		public static final String VIDEO_ORIGINATOR_USER_IMAGE = "video_orig_user_image";
		public static final String VIDEO_ORIGINATOR_USER_NAME = "video_orig_user_name";
		public static final String VIDEO_ORIGINATOR_USER_NICKNAME = "video_orig_user_nickname";
		public static final String VIDEO_PLAYER = "video_player";
		public static final String VIDEO_PROVIDER = "video_provider";
		public static final String VIDEO_THUMBNAIL = "video_thumbnail";
		public static final String VIDEO_TITLE = "video_title";
		public static final String WATCHED_BY_OWNER = "watched_by_owner";
		public static final String UPDATED = "updated";
		public static final String CREATED = "created";
		public static final String LIKED_BY_OWNER = "liked_by_owner";
		public static final String OWNER_WATCH_LATER = "owner_watch_later";
		public static final String SHORTENED_LINK = "shortened_link";
		public static final String VIDEO_ORIGIN = "video_origin";
	}
	
}
