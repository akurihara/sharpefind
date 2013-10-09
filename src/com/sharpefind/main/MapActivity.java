package com.sharpefind.main;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

public class MapActivity extends Activity{
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        TextView textview = new TextView(this);
	        textview.setText("This is the Map tab");
	        setContentView(textview);
	    }
	 
	 @Override
	 public void onConfigurationChanged(Configuration newConfig) {
	     super.onConfigurationChanged(newConfig);
	     setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	 }
}
