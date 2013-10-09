package com.sharpefind.main;

import java.util.ArrayList;
import com.sharpefind.support.VenueNode;
import com.sharpefind.support.VenueImageAdapter;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

public class VenueSelectActivity extends Activity {
	
	private ArrayList<VenueNode> _venueList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String accessToken = this.getIntent().getExtras().getString("accessToken");	   
		
		_venueList = new ArrayList<VenueNode>();
		
		setContentView(R.layout.activity_venue_select);
		GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new VenueImageAdapter(this));
	    gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
	    gridview.setOnItemClickListener(new ClickListener(accessToken));

		//Bitmap rattyImage = BitmapFactory.decodeResource(getResources(), R.drawable.ratty_front3);
		//EateryNode rattyNode = new EateryNode(rattyImage);
		
		/*
		for (int i = 0; i<6; i++){
			EateryNode rattyNode = new EateryNode(rattyImage);
			_eateryList.add(rattyNode);
		}
		
		this.setListAdapter(new EateryListAdapter(this, _eateryList));
		ListView listview = this.getListView();
		listview.setTextFilterEnabled(true);
		listview.setOnItemClickListener(new ClickListener(accessToken));	
		*/
	}
	
	
	private class ClickListener implements OnItemClickListener {
		
		private String _accessToken;
		
		private ClickListener(String token){
			_accessToken = token;
		}

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (position == SharpeFindActivity.SHARPE_REFECTORY || position == SharpeFindActivity.VERNEY_WOOLLEY){
				 Intent intent = new Intent(VenueSelectActivity.this, VenueActivity.class);
		         intent.putExtra("accessToken", _accessToken);
		         intent.putExtra("venue", position);
		         
		         startActivity(intent);
			} else {
				Intent intent = new Intent(VenueSelectActivity.this, CheckInActivity.class);
		         intent.putExtra("accessToken", _accessToken);
		         intent.putExtra("venue", position);
		         intent.putExtra("venueSpecific", -1);

		         startActivity(intent);
			}
		}	
	}
}
