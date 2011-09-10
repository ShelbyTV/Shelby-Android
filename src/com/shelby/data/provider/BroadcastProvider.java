package com.shelby.data.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.shelby.data.SQL;
import com.shelby.data.ShelbyDatabase;
import com.shelby.data.provider.model.DbBroadcast;

public class BroadcastProvider extends ContentProvider {
	
	public static final String AUTHORITY = "com.shelby.data.provider.BroadcastProvider";

	private static final int BROADCAST = 1;
	private ShelbyDatabase database;
	private static final UriMatcher mUriMatcher;
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getType(Uri uri) {
		switch(mUriMatcher.match(uri)) {
			case BROADCAST:
				return DbBroadcast.CONTENT_TYPE;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		throw new UnsupportedOperationException("Insert is not supported directly on Parties");
	}
	@Override
	public boolean onCreate() {
		database = new ShelbyDatabase(getContext());
		return true;
	}
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase d = database.getDbHelper().getReadableDatabase();
		SQLiteQueryBuilder sql = new SQLiteQueryBuilder();
		sql.setTables(SQL.Table.BROADCAST);
		return sql.query(d, projection, selection, selectionArgs, null, null, sortOrder);
	}
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int count = 0;
		switch(mUriMatcher.match(uri)) {
			case BROADCAST: {
				SQLiteDatabase d = database.getDbHelper().getWritableDatabase();
				count = d.update(SQL.Table.BROADCAST, values, selection, selectionArgs);
			}
			break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		return count;
	}
	
	static {
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		mUriMatcher.addURI(AUTHORITY, SQL.Table.BROADCAST, BROADCAST);
	}
}
