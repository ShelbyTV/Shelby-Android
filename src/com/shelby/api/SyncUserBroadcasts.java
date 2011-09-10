package com.shelby.api;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;

import com.shelby.Constants;
import com.shelby.api.bean.Broadcast;
import com.shelby.api.bean.Channel;
import com.shelby.data.ShelbyDatabase;

public final class SyncUserBroadcasts {

	public static void sync(Context ctx) {
		try {
			ArrayList<Channel> myChannes = new ArrayList<Channel>();
			
			String channels = ApiHandler.makeSignedGetRequest("v1/channels.json", ctx);
			ShelbyDatabase shelbyDb = new ShelbyDatabase(ctx);
			try {
				shelbyDb.reset();
				shelbyDb.openRW();
				shelbyDb.beginTransaction();
				//hate using this library but whatever
				JSONArray channs = new JSONArray(channels);
				if (channs.length() > 0) {
					for(int i=0; i<channs.length(); i++) {
						JSONObject jsonChannel = channs.getJSONObject(i);
						Channel channel = new Channel();
						if (jsonChannel.has("_id"))
							channel.setServerId(jsonChannel.getString("_id"));
						if (jsonChannel.has("description"))
							channel.setDescription(jsonChannel.getString("description"));
						if (jsonChannel.has("name"))
							channel.setName(jsonChannel.getString("name"));
						if (jsonChannel.has("public"))
							channel.setIsPublic(jsonChannel.getBoolean("public"));
						if (jsonChannel.has("user_id"))
							channel.setUserId(jsonChannel.getString("user_id"));
						long localChanId = shelbyDb.insertChannel(channel);
						channel.setLocalId(localChanId);
						myChannes.add(channel);
					}
				}
			} finally {
				shelbyDb.setTransactionSuccessful();
				shelbyDb.endTransaction();
			}
			
			//now do it for each channel
			if (myChannes.size() == 0) {
				shelbyDb.close();
				return; //throw exception?
			}
			try {
				shelbyDb.beginTransaction();
				for(Channel c : myChannes) {
					String broadcasts = ApiHandler.makeSignedGetRequest("v1/channels/" + c.getServerId() + "/broadcasts.json", ctx);
					JSONArray broads = new JSONArray(broadcasts);
					if (broads.length() > 0) {
						for(int i=0; i<broads.length()-1; i++) {
							JSONObject jsonB = broads.getJSONObject(i);
							Broadcast broad = new Broadcast();
							broad.setServerChannelId(c.getServerId());
							broad.setLocalChannelId(c.getLocalId());
							if (jsonB.has("_id"))
								broad.setServerId(jsonB.getString("_id"));
							if (jsonB.has("user_nickname"))
								broad.setUserNickName(jsonB.getString("user_nickname"));
							if (jsonB.has("user_thumbnail"))
								broad.setUserThumbnail(jsonB.getString("user_thumbnail"));
							if (jsonB.has("video_title"))
								broad.setVideoTitle(jsonB.getString("video_title"));
							if (jsonB.has("video_description"))
								broad.setVideoDescription(jsonB.getString("video_description"));
							if (jsonB.has("video_thumbnail_url"))
								broad.setVideoThumbnail(jsonB.getString("video_thumbnail_url"));
							if (jsonB.has("video_provider_name"))
								broad.setVideoProvider(jsonB.getString("video_provider_name"));
							if (jsonB.has("video_id_at_provider"))
								broad.setVideoIdAtProvider(jsonB.getString("video_id_at_provider"));
							if (jsonB.has("video_player"))
								broad.setVideoPlayer(jsonB.getString("video_player"));
							if (jsonB.has("video_user_id"))
								broad.setVideoUserId(jsonB.getString("video_user_id"));
							if (jsonB.has("video_user_nickname"))
								broad.setVideoUserNickName(jsonB.getString("video_user_nickname"));
							if (jsonB.has("video_user_thumbnail"))
								broad.setVideoUserThumbnail(jsonB.getString("video_user_thumbnail"));
							if (jsonB.has("video_originator_user_image"))
								broad.setVideoOriginatorUserImage(jsonB.getString("video_originator_user_image"));
							if (jsonB.has("video_originator_user_name"))
								broad.setVideoOriginatorUserName(jsonB.getString("video_originator_user_name"));
							if (jsonB.has("video_originator_user_nickname"))
								broad.setVideoOriginatorUserNickName(jsonB.getString("video_originator_user_nickname"));
							if (jsonB.has("total_plays"))
								broad.setTotalPlays(jsonB.getInt("total_plays"));
							if (jsonB.has("watched_by_owner"))
								broad.setWatchedByOwner(jsonB.getBoolean("watched_by_owner"));
							if (jsonB.has("name"))
								broad.setName(jsonB.getString("name"));
							if (jsonB.has("description"))
								broad.setDescription(jsonB.getString("description"));
							if (jsonB.has("user_id"))
								broad.setUserId(jsonB.getString("user_id"));
							shelbyDb.insertBroadcast(broad);
						}
					}
				}
			} finally {
				shelbyDb.setTransactionSuccessful();
				shelbyDb.endTransaction();				
				String path = Environment.getExternalStorageDirectory().toString() + "/shelby/";
				shelbyDb.exportDb(path, "shelby_db.sqlite");
				shelbyDb.close();
			}
		} catch(Exception ex) {
			if (Constants.DEBUG) ex.printStackTrace();
		}
		
	}
	
}