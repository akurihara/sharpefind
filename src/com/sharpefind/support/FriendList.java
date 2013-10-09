package com.sharpefind.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.sharpefind.main.CheckInActivity;
import com.sharpefind.main.R;


public class FriendList {

	private String _accessToken;
	private Resources _res;
	private int _count;
	private FriendsComparator _friendsComparator;
	private ArrayList<FriendNode> _friendList;
	private FriendNode _self;
	
	private final static int PHOTO_DIM = 80;
	
	public FriendList(String accessToken, Resources res){
		_accessToken = accessToken;
		_res = res;
		_friendList = new ArrayList<FriendNode>();
		_count = 0;
		_friendsComparator = new FriendsComparator();
		_self = null;
	}
	
	public ArrayList<FriendNode> getFriends() throws IOException, NullPointerException, APIUpdatingException, ParseException{
		
		String url = _res.getString(R.string.friends_url) + _accessToken + "&v=" + getVersion();		
		JSONObject json = this.getJSON(url);
		
		/* When Foursquare is processing a new check in, the API is momentarily unresponsive. */
		if (json == null){
			throw new APIUpdatingException();
		}
		
		JSONObject response = (JSONObject) json.get("response");
		JSONObject friends = (JSONObject) response.get("friends");
		long temp = (Long) friends.get("count");
		int count = (int) temp;
		_count = count;
		JSONArray friendArray = (JSONArray) friends.get("items");
		
		//String[] friendList = new String[count];
		for (int i = 0; i < count; i++){
			JSONObject friend = (JSONObject) friendArray.get(i);
			
			//get first name and last name
			//friendList[i] = friend.get("firstName") + " " + friend.get("lastName");
			
			//get and convert friend id
			String tempId = (String) friend.get("id");
			int id = Integer.parseInt(tempId);
					
			//create new friendNode object
			FriendNode friendNode = new FriendNode(id, (String)friend.get("firstName"), (String)friend.get("lastName"));
			
			//get friend's photo
			String photoUrl = (String) friend.get("photo");
			Bitmap photo = downloadPhoto(photoUrl);
			friendNode.setPhoto(photo);
				
			_friendList.add(friendNode);
		}
		
		String url2 = _res.getString(R.string.self_url) + _accessToken + "&v=" + getVersion();		
		JSONObject json2 = this.getJSON(url2);
		
		/* When Foursquare is processing a new check in, the API is momentarily unresponsive. */
		if (json2 == null){
			throw new APIUpdatingException();
		}
		
		JSONObject response2 = (JSONObject) json2.get("response");
		JSONObject user = (JSONObject) response2.get("user");
		
		String tempId = (String) user.get("id");
		int id = Integer.parseInt(tempId);
		
		FriendNode selfNode = new FriendNode(id, (String)user.get("firstName"), (String)user.get("lastName"));
		String photoUrl = (String) user.get("photo");
		Bitmap photo = downloadPhoto(photoUrl);
		selfNode.setPhoto(photo);
		
		_friendList.add(selfNode);
		_self = selfNode;
		_self.setIsUser(true);
		return _friendList;
	}	

	public void getCheckIns(ArrayList<FriendNode> list) throws IOException, APIUpdatingException, ParseException{
		
		/* For all of the friends in the friends list */
		for(FriendNode friendNode : list){
			String url = _res.getString(R.string.friend_check_ins_url) + 
						friendNode.getId() + "?oauth_token=" + _accessToken +
						"&v=" + getVersion();
			
			//Log.v("LOG", "URL: " + url);
			JSONObject json = getJSON(url);
			
			/* When Foursquare is processing a new check in, the API is momentarily unresponsive. */
			if (json == null){
				throw new APIUpdatingException();
			}
			
			JSONObject response = (JSONObject) json.get("response");
			JSONObject user = (JSONObject) response.get("user");
			JSONObject checkins = (JSONObject) user.get("checkins");
			long temp = (Long) checkins.get("count");
			int count = (int) temp;
			
			/* If the friend has recent check ins listed, get their information */
			if (count > 0){
				JSONArray checkinArray = (JSONArray) checkins.get("items");
				JSONObject lastCheckin = (JSONObject) checkinArray.get(0);	//get friend's last check in
				JSONObject venue = (JSONObject) lastCheckin.get("venue");
				String venueName = (String) venue.get("name"); 			//get location
				String venueId = (String) venue.get("id");
				friendNode.setVenue(venueName);	//set location
				friendNode.setTimeRemaining(-2);
				friendNode.setVenueId(venueId);
				
				long createdAt = (Long) lastCheckin.get("createdAt");	//get timestamp
				Date date = new Date(createdAt*1000);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				friendNode.setLastCheckInTime(calendar);				
				
				String shout = (String) lastCheckin.get("shout");
				
				if(shout == null){
					continue;
				}

				String delims = "\\s+";
				String[] tokens = shout.split(delims);
			
				if (verifyShout(tokens)){
					String venueSpecific = tokens[6].replaceAll("_", " ");
					friendNode.setVenue(venueSpecific);
					friendNode.setPlannedTime(Integer.parseInt(tokens[8]));
					friendNode.calcTimeRemaining();
				}
				
			/* Friend has no check ins, set their location to "Nowhere" */
			} else{
				friendNode.setVenue("Nowhere");
				//friendNode.setTimeRemaining(-3);
			}
			
		}
		
		/* Sort the list of friends by their time remaining at their venue */
		list.remove(_self);
		Collections.sort(list, _friendsComparator);
		this.updateSelf(_self);
		list.add(_self);
		Collections.reverse(list);
		
	}
	
	public void updateSelf(FriendNode self) throws APIUpdatingException, IOException, ParseException {
		
		String url = _res.getString(R.string.self_url) + _accessToken + "&v=" + getVersion();		
		JSONObject json = this.getJSON(url);
		
		/* When Foursquare is processing a new check in, the API is momentarily unresponsive. */
		if (json == null){
			throw new APIUpdatingException();
		}
		
		JSONObject response = (JSONObject) json.get("response");
		JSONObject user = (JSONObject) response.get("user");
		JSONObject checkins = (JSONObject) user.get("checkins");
		long temp = (Long) checkins.get("count");
		int count = (int) temp;
		
		/* If the friend has recent check ins listed, get their information */
		if (count > 0){
			JSONArray checkinArray = (JSONArray) checkins.get("items");
			JSONObject lastCheckin = (JSONObject) checkinArray.get(0);	//get friend's last check in
			JSONObject venue = (JSONObject) lastCheckin.get("venue");
			String venueName = (String) venue.get("name"); 			//get location
			String venueId = (String) venue.get("id");
			self.setVenue(venueName);	//set location
			self.setTimeRemaining(-2);
			self.setVenueId(venueId);
			
			long createdAt = (Long) lastCheckin.get("createdAt");	//get timestamp
			Date date = new Date(createdAt*1000);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			self.setLastCheckInTime(calendar);				
			
			String shout = (String) lastCheckin.get("shout");
			String delims = "\\s+";
			String[] tokens = shout.split(delims);
		
			if (verifyShout(tokens)){
				String venueSpecific = tokens[6].replaceAll("_", " ");
				self.setVenue(venueSpecific);
				self.setPlannedTime(Integer.parseInt(tokens[8]));
				self.calcTimeRemaining();
			}
			
		/* Friend has no check ins, set their location to "Nowhere" */
		} else{
			self.setVenue("Nowhere");
			
		}
	}
	
	public String getVersion(){
		GregorianCalendar calendar = new GregorianCalendar();
		String version = Integer.toString(calendar.get(GregorianCalendar.YEAR)) +
		Integer.toString(calendar.get(GregorianCalendar.MONTH)) + 
		Integer.toString(calendar.get(GregorianCalendar.DAY_OF_MONTH));
		return version;
	}
	
	public JSONObject getJSON(String url) throws IOException, ParseException{
		String line;
		StringBuilder message = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()), 8*1024);
		while((line = reader.readLine()) != null){
			message.append(line);
		}
		reader.close();
		return (JSONObject) JSONValue.parseWithException(message.toString());
	}
	
	public Bitmap downloadPhoto(String url) throws IOException {
			URL newurl;
			newurl = new URL(url);
			Bitmap photo = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
			if (photo != null){
				if (photo.getHeight() != PHOTO_DIM || photo.getWidth() != PHOTO_DIM){
					photo = Bitmap.createScaledBitmap(photo, PHOTO_DIM, PHOTO_DIM, true);
				}
			}
			return photo;
	}
	
	public String[] getNames(){
		String[] names = new String[_count];
		for (int i =0; i < _count; i++){
			names[i] = _friendList.get(i).getFirstName() + " " + _friendList.get(i).getLastName();
		}
		return names;
	}
	
	public boolean verifyShout(String[] tokens){
		if (tokens.length != 10){
			return false;
		}
		
		if (tokens[0].equals("Sharpefind:") &&
				tokens[1].equals("I") &&
				tokens[2].equals("will") &&
				tokens[3].equals("be") &&
				tokens[4].equals("at") &&
				tokens[5].equals("the") &&
				tokens[7].equals("for") &&
				tokens[9].equals("minutes.")){
			return true;
		} else {
			return false;
		}
	}
	
	private class FriendsComparator implements Comparator<Object> {
		
		public int compare(Object o1, Object o2) {
           FriendNode friend1 = (FriendNode) o1;  
           FriendNode friend2 = (FriendNode) o2;
           Integer time1 = friend1.getTimeRemaining();
           Integer time2 = friend2.getTimeRemaining();
           
           return time1.compareTo(time2);
        }
	}
}
