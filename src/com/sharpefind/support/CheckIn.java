package com.sharpefind.support;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import javax.net.ssl.HttpsURLConnection;

import com.sharpefind.main.R;

import fi.foyt.foursquare.api.FoursquareApi;

import android.content.res.Resources;
import android.util.Log;

public class CheckIn {

	private int _time, _venuePosition;
	private String _accessToken, _venueSpecific;
	private Resources _res;
	
	public CheckIn(String accessToken, int venuePosition, String venueSpecific, Resources res){
		_accessToken = accessToken;
		_venuePosition = venuePosition;
		_venueSpecific = venueSpecific;
		_res = res;
	}
	
	public void setTime(int time){
		_time = time;
	}
	
	public int getTime(){
		return _time;
	}
	
	public boolean checkIn(){
		Calendar calendar = Calendar.getInstance();
		String version = Integer.toString(calendar.get(Calendar.YEAR))+
			Integer.toString(calendar.get(Calendar.MONTH)) + Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
		
		String partOfVenue = URLEncoder.encode(_venueSpecific.replaceAll(" ", "_"));
		String venueId = _res.getStringArray(R.array.eateries_id_array)[_venuePosition];
		
		String url_text = _res.getString(R.string.check_in_url) + "?oauth_token=" + _accessToken
						+ "&broadcast=public" + "&venueId=" + venueId
						+ "&shout=" + URLEncoder.encode("Sharpefind: I will be at the ") + partOfVenue +
						"+for+" + _time + "+minutes." + "&v=" + version;	
		
		//Log.d("LOG", "URL: [" + url_text + "].");
			
		try {
			URL url = new URL(url_text);
			Log.v("LOG", "Check in url: " + url_text);
			URLConnection connection = url.openConnection();
			((HttpsURLConnection) connection).setRequestMethod("POST");
			connection.setDoOutput(true);

	        // Get the response
	        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String line, message= "";
	        while ((line = reader.readLine()) != null) {
	            message += line;
	        }
	        Log.v("LOG", "Check in: " + message);
	        reader.close();

	        return true;
		} catch (Exception e) {
			Log.v("LOG", "EXCEPTION: " + e.getMessage() + ".");
			return false;
		}
	}
}
