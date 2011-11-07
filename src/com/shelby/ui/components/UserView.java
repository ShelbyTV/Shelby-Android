package com.shelby.ui.components;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shelby.R;
import com.shelby.api.UserHandler;
import com.shelby.api.bean.User;
import com.shelby.utility.DrawableManager;
import com.shelby.utility.PrefsManager;

public class UserView extends LinearLayout {

	DrawableManager mDrawableManager = new DrawableManager();
	
	public UserView(Context context) {
		super(context);
		setupView(context);
		
	}
	
	public UserView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupView(context);
	}
	
	public UserView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setupView(context);
		
	}
	
	private void setupView(Context context) {
		this.setOrientation(LinearLayout.HORIZONTAL);
		int fivePix = 5;
		try {
			DisplayMetrics metrics = new DisplayMetrics();
			((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);	 //this is dirty...		
			float density = metrics.density;		
			fivePix = (int)(5*density);
		} catch(Exception ex) { }
		User u = UserHandler.getUser(context);
		if (u.getImage() != null && !u.getImage().equals("")) {
			ImageView usr = new ImageView(context);
			usr.setPadding(fivePix, fivePix, fivePix, fivePix);
			usr.setTag(null);
			mDrawableManager.queueDrawableFetch(u.getImage(), usr, context);
			LinearLayout userData = new LinearLayout(context);
			userData.setPadding(0, fivePix, 0, fivePix);
			userData.setOrientation(LinearLayout.VERTICAL);
			userData.setGravity(Gravity.RIGHT);
			TextView tv = new TextView(context);
			tv.setText(u.getName());
			tv.setGravity(Gravity.RIGHT);
			LinearLayout ll = new LinearLayout(context);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			ll.setLayoutParams(lp);
			if (PrefsManager.getHasSocialType("twitter", context)) {
				ImageView iv = new ImageView(context);
				iv.setImageResource(R.drawable.twitter_active);
				iv.setPadding(0, 2, 0, 2);
				ll.addView(iv);
			}
			if (PrefsManager.getHasSocialType("facebook", context)) {
				ImageView iv = new ImageView(context);
				iv.setImageResource(R.drawable.facebook_active);
				iv.setPadding(0, 2, 0, 2);
				ll.addView(iv);				
			}
			if (PrefsManager.getHasSocialType("tumblr", context)) {
				ImageView iv = new ImageView(context);
				iv.setImageResource(R.drawable.tumblr_active);
				iv.setPadding(0, 2, 0, 2);
				ll.addView(iv);
			}
			ll.setGravity(Gravity.RIGHT);
			userData.addView(tv);
			userData.addView(ll);
			this.addView(userData);
			this.addView(usr);
		}
	}

}
