package com.sharpefind.support;

import java.util.List;

import com.sharpefind.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VenueImageAdapter extends BaseAdapter {
    private Context _context;
    private List<VenueNode> _venueList;
    
    // references to our images
    private Integer[] _thumbImages = {
            R.drawable.sharpe_refectory_thumb2, R.drawable.verney_woolley_thumb,
            R.drawable.josiahs_thumb, R.drawable.gate_thumb,
            R.drawable.blue_room_thumb, R.drawable.ivy_room_thumb 
    };
    
    private String[] _thumbNames = {
    		"Sharpe Refectory", "Verney-Woolley",
    		"Josiah's", "The Gate",
    		"Blue Room", "Ivy Room"
    };

    public VenueImageAdapter(Context c) {
        _context = c;
    }

    public int getCount() {
        //return _venueList.size();
    	return _thumbImages.length;
    }

    public Object getItem(int position) {
        return _venueList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes	
        	LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.venue_list_item, null);     	
        }

        imageView = (ImageView) convertView.findViewById(R.id.venue_thumbnail);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(140, 140);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(_thumbImages[position]);
        
        TextView textView = (TextView) convertView.findViewById(R.id.venue_name);
        textView.setText(_thumbNames[position]);
        return convertView;
    }
}
