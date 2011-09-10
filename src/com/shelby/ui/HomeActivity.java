package com.shelby.ui;

import android.content.Intent;
import android.os.Bundle;

import com.shelby.R;

public class HomeActivity extends BaseActivity {
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Intent i = new Intent().setClass(this, LoginActivity.class);
        startActivity(i);
        
    }

}