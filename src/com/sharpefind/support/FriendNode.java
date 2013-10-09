package com.sharpefind.support;

import java.util.Calendar;
import java.util.GregorianCalendar;
import android.graphics.Bitmap;
import android.util.Log;

public class FriendNode {
	
	private int _id, _plannedTime, _timeRemaining;
	private String _firstName, _lastName, _venue, _venueId;
	private Bitmap _photo;
	private Calendar _lastCheckInTime, _checkOutTime;
	private boolean _isEating, _isUser;
	
	public FriendNode(int id, String firstName, String lastName){
		_id = id;
		_firstName = firstName;
		_lastName = lastName;
		_plannedTime = 0;
		_venue = "Nowhere";
		_venueId = "";
		_isEating = false;
		_timeRemaining = -3;
		_isUser = false;
	}
	
	public void calcTimeRemaining(){
		_checkOutTime = (GregorianCalendar) _lastCheckInTime.clone();
		_checkOutTime.add(Calendar.MINUTE, _plannedTime);
		
		Calendar now = Calendar.getInstance();
		long milliseconds1 = now.getTimeInMillis();
		long milliseconds2 = _checkOutTime.getTimeInMillis();
		
		long diff = milliseconds2 - milliseconds1;
		_timeRemaining = (int)(diff / (60 * 1000));
		
		if (_timeRemaining <= 0){
			_timeRemaining = -1;
		}
	}
	 
	/* Accessors and Mutators */
	public void setVenue(String s){ 
		_venue = s;
	}
	public String getVenue(){
		return _venue;
	}
	public Calendar getLastCheckInTime(){
		return _lastCheckInTime;
	}
	public void setLastCheckInTime(Calendar time){
		_lastCheckInTime = time;
	}
	public void setPlannedTime(int time){
		_plannedTime = time;
	}
	public int getPlannedTime(){
		return _plannedTime;
	}
	public void setTimeRemaining(int i){
		_timeRemaining = i;
	}
	public int getTimeRemaining(){
		return _timeRemaining;
	}
	public Calendar getCheckOutTime(){
		return _checkOutTime;
	}
	public int getId(){
		return _id;
	}
	public void setPhoto(Bitmap photo){
		_photo = photo;
	}
	public Bitmap getPhoto(){
		return _photo;
	}
	public void setId(int id){
		_id = id;
	}
	public String getFirstName(){
		return _firstName;
	}
	public String getLastName(){
		return _lastName;
	}
	public void setFirstName(String firstName){
		_firstName = firstName;
	}
	public void setLastName(String lastName){
		_lastName = lastName;
	}
	public void setEating(boolean b){
		_isEating = b;
	}
	public boolean isEating(){
		return _isEating;
	}
	public void setVenueId(String id){
		_venueId = id;
	}
	public String getVenueId(){
		return _venueId;
	}
	public void setIsUser(boolean b){
		_isUser = b;
	}
	public boolean isUser(){
		return _isUser; 
	}
}
