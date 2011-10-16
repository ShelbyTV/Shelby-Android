package com.shelby.ui.fragments;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.shelby.Constants;
import com.shelby.R;
import com.shelby.api.ShareHandler;

public class ShareFragment extends DialogFragment {

	private EditText mShareText;
	private CheckBox mShareFacebook;
	private CheckBox mShareTwitter;
	private Button mShareButton;
	private String serverBroadcastId;
	private String sharerName;
	private String sharerType;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_share, null);
		mShareText = (EditText) v.findViewById(R.id.share_text);
		mShareFacebook = (CheckBox) v.findViewById(R.id.facebook);
		mShareTwitter = (CheckBox) v.findViewById(R.id.twitter);
		mShareButton = (Button) v.findViewById(R.id.share);
		mShareButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				new ShareTask().execute();
			}
		});
		getDialog().setTitle("Share Video");
		return v;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getArguments();
		if (extras != null && extras.getString("server_broadcast_id") != null) {
			serverBroadcastId = extras.getString("server_broadcast_id");
			sharerName = extras.getString("share_name");
			sharerType = extras.getString("share_type");
			if (sharerType != null) {
				if (sharerType.equals("twitter")) {
					mShareText.append("+° @" + sharerName);
				} else {
					mShareText.append("+° " + sharerName);
				}
			}
		}
		
	}
	
	
    public static ShareFragment newInstance(String broadcastId, String sharerName, String sharerType) {
    	ShareFragment f = new ShareFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("server_broadcast_id", broadcastId);
        args.putString("share_name", sharerName);
        args.putString("share_type", sharerType);        
        f.setArguments(args);

        return f;
    }

    public class ShareTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				if (serverBroadcastId != null && !serverBroadcastId.equals(""))
					ShareHandler.shareVideo(serverBroadcastId, mShareText.getText().toString(), mShareFacebook.isChecked(), mShareTwitter.isChecked(), getActivity());
			} catch(Exception ex) {
				if (Constants.DEBUG) ex.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			getDialog().dismiss();
		}
    	
    }
    
	
}
