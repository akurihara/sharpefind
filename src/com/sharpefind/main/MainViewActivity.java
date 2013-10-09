package com.sharpefind.main;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TabHost;

public class MainViewActivity extends TabActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		
		final String accessToken = this.getIntent().getExtras().getString("accessToken");	//gets user access token from intent extra
		setContentView(R.layout.activity_main);
		
		Resources res = getResources();
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)        
        intent = new Intent().setClass(this, VenueSelectActivity.class);
        intent.putExtra("accessToken", accessToken);
        
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("check_in").setIndicator("Check In",res.getDrawable(R.layout.tab_venues)).setContent(intent);
        tabHost.addTab(spec);
        
        // Do the same for the other tabs
        intent = new Intent().setClass(this, FriendsActivity.class);
        intent.putExtra("accessToken", accessToken);
        spec = tabHost.newTabSpec("friends").setIndicator("Friends",res.getDrawable(R.layout.tab_friends)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, MenuActivity.class);
        spec = tabHost.newTabSpec("menu").setIndicator("Menu",res.getDrawable(R.layout.tab_menu)).setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(1);
        
        this.getWindow().setFormat(PixelFormat.RGBA_8888); getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
	}
	
	private void restartFirstActivity(){
		 Intent i = getApplicationContext().getPackageManager()
		 .getLaunchIntentForPackage(getApplicationContext().getPackageName() );
	
		 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
		 startActivity(i);
	 }
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}
