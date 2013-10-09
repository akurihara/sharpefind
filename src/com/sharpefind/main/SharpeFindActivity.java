package com.sharpefind.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SharpeFindActivity extends Activity {
	
	public static final int SHARPE_REFECTORY = 0, VERNEY_WOOLLEY = 1, 
							JOSIAHS = 2, THE_GATE = 3,
							BLUE_ROOM = 4, IVY_ROOM = 5;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Resources res = this.getResources();   
              
        String url =
        		"https://foursquare.com/oauth2/authenticate" + 
          		"?client_id=" + res.getString(R.string.client_id) + 
                "&response_type=token" + 
                "&redirect_uri=" + res.getString(R.string.callback_url);
        
        WebView webview = (WebView) this.findViewById(R.id.webview_login);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
        
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String fragment = "#access_token=";
                int start = url.indexOf(fragment);
                if (start > -1){
                    String accessToken = url.substring(start + fragment.length(), url.length());
                    
                    Intent intent = new Intent(SharpeFindActivity.this, MainViewActivity.class);
                    intent.putExtra("accessToken", accessToken);
                    startActivity(intent);
                    finish();
                }
            }

        });
        
        webview.loadUrl(url);               
    }
}