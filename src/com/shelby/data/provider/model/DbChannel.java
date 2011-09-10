package com.shelby.data.provider.model;

import com.shelby.data.SQL.Table;

public class DbChannel {

	public DbChannel() { super(); }
	
	public static final String _ID = Table.BROADCAST + "." + Column._ID;
	
	public static final class Column {
		public static final String _ID = "_id";
		public static final String SERVER_ID = "server_id"; 
		public static final String DESCRIPTION = "description";
		public static final String NAME = "name";
		public static final String PUBLIC = "is_public";
		public static final String USER_ID = "user_id";
		public static final String UPDATED = "updated";
		public static final String CREATED = "created";
	}
	
	
}
