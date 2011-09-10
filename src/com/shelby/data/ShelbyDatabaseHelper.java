/**
 * Hashable-Android
 * Sep 6, 2011
 * @author aaron
 * 
 */
package com.shelby.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shelby.utility.FileUtils;

/**
 * @author aaron
 *
 */
public final class ShelbyDatabaseHelper extends SQLiteOpenHelper {

	ShelbyDatabaseHelper(Context ctx) {
		super(ctx, SQL.DB_NAME, null, SQL.DATABASE_VERSION);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL.CREATE_BROADCAST_TABLE);
		db.execSQL(SQL.CREATE_CHANNEL_TABLE);
		
		//indices
		db.execSQL(SQL.ChannelIndices.IDX1);
		db.execSQL(SQL.BroadcastIndices.IDX1);

	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//no versions yet so this is nonsense
		dropTables(db);
		onCreate(db);
	}
	
	public void reset() {
		SQLiteDatabase db = this.getWritableDatabase();
		dropTables(db);
		onCreate(db);
	}
	
	private void dropTables(SQLiteDatabase db) {
		db.execSQL(SQL.Table.Drop.BROADCAST);
		db.execSQL(SQL.Table.Drop.CHANNEL);
	}
	
	public boolean exportDatabase(String dbPath, String dbFileName) throws IOException {
	    // Close the SQLiteOpenHelper so it will commit the created empty
	    // database to internal storage.
	    close();
	    File dirs = new File(dbPath);
	    dirs.mkdirs();
	    File newDb = new File(dbPath + dbFileName);
	    File oldDb = new File(SQL.DB_FILE_PATH);
	    if (oldDb.exists()) {
	        FileUtils.copyFile(new FileInputStream(oldDb), new FileOutputStream(newDb));
	        getWritableDatabase().close();
	        return true;
	    }
	    return false;
	}
	
}
