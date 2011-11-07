package com.shelby.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.shelby.R;
import com.shelby.api.ApiHandler;

public class LoginActivity extends BaseActivity {

	private final int AUTH_REQUEST_TOKEN_ACTIVITY = 1;
	
	public void onCreate(Bundle grabber) {
		super.onCreate(grabber);
		setContentView(R.layout.activity_login);
		
		ImageView twitterLogin = (ImageView) findViewById(R.id.twitter_login_button);
		ImageView facebookLogin = (ImageView) findViewById(R.id.facebook_login_button);
		twitterLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new GetAuthUrlTask().execute();
			}
		});
		facebookLogin.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				
			}
		});
		
	}
	
	class GetAuthUrlTask extends AsyncTask<Integer, Void, String> {

		@Override
		protected String doInBackground(Integer... params) {
			String url = ApiHandler.getAuthorizeURL(LoginActivity.this);
			return url;
		}
		
		protected void onPostExecute(String ret) {
			Intent i = new Intent().setClass(LoginActivity.this, WebViewActivity.class);
			i.putExtra("url", ret + "&provider=twitter");
			startActivityForResult(i, AUTH_REQUEST_TOKEN_ACTIVITY);
		}
	}
	
	class FinishAuthTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... data) {
			if (data.length > 0 && data[0] != null)
				return ApiHandler.getAccessTokenAndKey(data[0], LoginActivity.this);
			return false;
		}
		
		protected void onPostExecute(Boolean res) {
			if (res) finish();
			else {
				Toast t = Toast.makeText(LoginActivity.this, "Something messed up sorry yo", Toast.LENGTH_LONG);
				t.show();
			}
		}
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AUTH_REQUEST_TOKEN_ACTIVITY && resultCode == RESULT_OK && data != null && data.getExtras() != null) {
			String url = data.getExtras().getString("url");
			new FinishAuthTask().execute(url);
		}
	}
	
	
}
