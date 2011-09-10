package com.shelby.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.shelby.R;
import com.shelby.api.ApiHandler;

public class LoginActivity extends BaseActivity {

	private final int AUTH_REQUEST_TOKEN_ACTIVITY = 1;
	
	public void onCreate(Bundle grabber) {
		super.onCreate(grabber);
		setContentView(R.layout.activity_login);
		
		Button login = (Button) findViewById(R.id.login_button);
		login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new GetAuthUrlTask().execute();
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
			i.putExtra("url", ret);
			startActivityForResult(i, AUTH_REQUEST_TOKEN_ACTIVITY);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AUTH_REQUEST_TOKEN_ACTIVITY && resultCode == RESULT_OK && data != null && data.getExtras() != null) {
			String url = data.getExtras().getString("url");
			
		}
	}
}
