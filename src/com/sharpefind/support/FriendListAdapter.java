package com.sharpefind.support;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.sharpefind.main.FriendsActivity;
import com.sharpefind.main.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendListAdapter extends BaseAdapter {

	 private Context _context;
	 private List<FriendNode> _friendList;
	 //private Typeface _fontNormal, _fontBold;
	 
	 public FriendListAdapter(Context context, List<FriendNode> friendList) {
		 _context = context;
		 _friendList = friendList;
		 
		// _fontNormal = Typeface.createFromAsset(_context.getAssets(),"Oswald-Regular.ttf");
		// _fontBold = Typeface.createFromAsset(_context.getAssets(),"Oswald-Bold.ttf");
	}
	 
	public int getCount() {
		return _friendList.size();
	}

	public Object getItem(int index) {
		return _friendList.get(index);
	}

	public long getItemId(int position) {
		return position;
	}
	
	@Override
    public boolean isEnabled(int position) { 
            return false; 
    } 
	
	public View getView(int index, View convertView, ViewGroup parent) {
	    FriendNode entry = _friendList.get(index);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.friends_list_item, null);
        }
        
        ImageView photo = (ImageView) convertView.findViewById(R.id.image);
        photo.setImageBitmap(entry.getPhoto());
        
        if (entry.isUser()){
        	photo.setBackgroundColor(FriendsActivity.getContext().getResources().getColor(R.color.yellow));
        } else {
        	photo.setBackgroundColor(FriendsActivity.getContext().getResources().getColor(R.color.white));
        }
        
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(entry.getFirstName() + " " + entry.getLastName());

        TextView location = (TextView) convertView.findViewById(R.id.location);
        location.setTextColor(Color.WHITE);
        if (entry.getVenue().equals("Nowhere")){
        	location.setText("Is off living life.");
        } else {
        	location.setText("Checked into " + entry.getVenue());
        }

        TextView time = (TextView) convertView.findViewById(R.id.time);
        Calendar calendar = entry.getLastCheckInTime();
        int timeRemaining = entry.getTimeRemaining();
    	
        if (calendar == null){
        	time.setText("");
        } else if (timeRemaining > 0){
        	location.setText("Checked into " + entry.getVenue());
        	location.setTextColor((FriendsActivity.getContext().getResources().getColor(R.color.yellow)));
        	SimpleDateFormat format = new SimpleDateFormat();
        	format.applyPattern("h:mm a");
        	time.setText("at " + format.format(calendar.getTime()) + " for " + timeRemaining + " more minutes.");
        	time.setTextColor((FriendsActivity.getContext().getResources().getColor(R.color.yellow)));
        } else {
        	location.setText("Last Check In: " + entry.getVenue());
        	location.setTextColor((FriendsActivity.getContext().getResources().getColor(R.color.white)));
        	SimpleDateFormat format = new SimpleDateFormat();
        	format.applyPattern("h:mm a  MMM dd, yyyy");
        	time.setText("at " + format.format(calendar.getTime()));
        	time.setTextColor((FriendsActivity.getContext().getResources().getColor(R.color.white)));
        }
        return convertView;
	}
}
