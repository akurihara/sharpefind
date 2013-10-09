package com.sharpefind.support;

import android.graphics.Bitmap;

public class VenueNode {

	private Bitmap _photo;
	private String _name;
	
	public VenueNode(Bitmap photo, String name){
		_photo = photo;
		_name = name;
	}
	
	public void setPhoto(Bitmap photo){
		_photo = photo;
	}
	
	public Bitmap getPhoto(){
		return _photo;
	}
	
	public void setName(String s){
		_name = s;
	}
	public String getName(){
		return _name;
	}
}
