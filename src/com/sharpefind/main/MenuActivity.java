package com.sharpefind.main;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

public class MenuActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		/*
		try {
			Document doc = Jsoup.connect("http://www.brown.edu/Student_Services/Food_Services/eateries/refectory.php").get();
			Elements elems = doc.getElementsByClass("title");
			Element foodStations = null;
			Log.v("LOG", "Test");
			for (Element elem : elems){
				Log.v("LOG", "Text: " + elem.text());
				if (elem.text().equals("Food Stations")){
					foodStations = elem;
					break;
				}
			}
			
			Element bistro = foodStations.nextElementSibling();
			Log.v("LOG", "Test: " + bistro.ownText());
		
			//Element bistro = bistros.first();
			//Log.v("LOG", "Bistro? " + bistro.text());
			//String bistroMenu = bistro.nextElementSibling().text();
			
			//Log.v("LOG", "Bistro " + bistroMenu);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		//TextView text = new TextView(this);
		//text.setText("text");
		//this.setContentView(text);
		WebView webview = new WebView(this);
		webview.getSettings().setJavaScriptEnabled(true);
		this.setContentView(webview);
		//webview.loadUrl("http://sharpefind.phpfogapp.com/sharpefind.html");
		webview.loadUrl("file:///android_asset/menu/sharpefind.html");
		
		
	}

}