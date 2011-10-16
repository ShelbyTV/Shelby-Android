/**
 * Hashable-Android
 * Sep 6, 2011
 * @author aaron
 * 
 */
package com.shelby.data;

import com.shelby.data.provider.model.DbBroadcast;
import com.shelby.data.provider.model.DbChannel;

/**
 * @author aaron
 *
 */
public final class SQL {

	public static final String DB_NAME = "shelby_data";
	public static final String DB_FILE_PATH = "/data/data/com.shelby/databases/" + DB_NAME;
	public static final int DATABASE_VERSION = 1;
	

	public static class Table {
		public static final String CHANNEL = "chanel";
		public static final String BROADCAST = "broadcast";
		
		public static class Drop {
			public static final String CHANNEL = "DROP TABLE IF EXISTS " + Table.CHANNEL;
			public static final String BROADCAST = "DROP TABLE IF EXISTS " + Table.BROADCAST;
		}
	}
	
	/*
	 * These are the creates for the PARTY table structures
	 */
	public static final String CREATE_CHANNEL_TABLE = "CREATE TABLE " + Table.CHANNEL + 
		" ( " +
		"	" + DbChannel.Column._ID + "								integer primary key autoincrement" +
		"	, " + DbChannel.Column.SERVER_ID + "						varchar unique not null" +
		"	, " + DbChannel.Column.DESCRIPTION + "						varchar" +
		"	, " + DbChannel.Column.NAME + "								varchar" +
		"	, " + DbChannel.Column.PUBLIC + "							bit" +
		"	, " + DbChannel.Column.USER_ID + "							varchar" +
		"	, " + DbChannel.Column.UPDATED + "							integer" +
		"	, " + DbChannel.Column.CREATED + "							integer" +
		" );";
	
	public final static class ChannelIndices {
		public static final String IDX1 = "CREATE INDEX idx_channel_server_id ON " + Table.CHANNEL + " (" + DbChannel.Column.SERVER_ID + ");";
	}
	
	public static final String CREATE_BROADCAST_TABLE = "CREATE TABLE " + Table.BROADCAST + 
		" ( " +
		"	" + DbBroadcast.Column._ID + "									integer primary key autoincrement" +
		"	, " + DbBroadcast.Column.LOCAL_CHANNEL_ID + "					integer" +
		"	, " + DbBroadcast.Column.SERVER_CHANNEL_ID + "					varchar" +
		"	, " + DbBroadcast.Column.SERVER_ID + "							varchar unique not null" +
		"	, " + DbBroadcast.Column.DESCRIPTION + "						varchar" +
		"	, " + DbBroadcast.Column.NAME + "								varchar" +
		"	, " + DbBroadcast.Column.TOTAL_PLAYS + "						integer" +
		"	, " + DbBroadcast.Column.USER_ID + "							varchar" +
		"	, " + DbBroadcast.Column.USER_NICKNAME + "						varchar" +
		"	, " + DbBroadcast.Column.USER_THUMBNAIL + "						varchar" +
		"	, " + DbBroadcast.Column.VIDEO_DESCRIPTION + "					varchar" +
		"	, " + DbBroadcast.Column.VIDEO_ID_AT_PROVIDER + "				varchar" +
		"	, " + DbBroadcast.Column.VIDEO_ORIGINATOR_USER_IMAGE + "		varchar" +
		"	, " + DbBroadcast.Column.VIDEO_ORIGINATOR_USER_NAME + "			varchar" +
		"	, " + DbBroadcast.Column.VIDEO_ORIGINATOR_USER_NICKNAME + "		varchar" +
		"	, " + DbBroadcast.Column.VIDEO_PLAYER + "						varchar" +
		"	, " + DbBroadcast.Column.VIDEO_PROVIDER + "						varchar" +
		"	, " + DbBroadcast.Column.VIDEO_THUMBNAIL + "					varchar" +
		"	, " + DbBroadcast.Column.VIDEO_TITLE + "						varchar" +
		"	, " + DbBroadcast.Column.WATCHED_BY_OWNER + "					bit" +
		"	, " + DbBroadcast.Column.CREATED + "							integer" +
		"	, " + DbBroadcast.Column.UPDATED + "							integer" +
		"	, " + DbBroadcast.Column.LIKED_BY_OWNER + "						bit" +
		"	, " + DbBroadcast.Column.OWNER_WATCH_LATER + "					bit" +
		"	, " + DbBroadcast.Column.SHORTENED_LINK + "						varchar" +
		"	, " + DbBroadcast.Column.VIDEO_ORIGIN + "						varchar" +
		" );";
	
	public final static class BroadcastIndices {
		public static final String IDX1 = "CREATE INDEX idx_broadcast_local_channel_id ON " + Table.BROADCAST + " (" + DbBroadcast.Column.LOCAL_CHANNEL_ID + ");";		
	}
}
