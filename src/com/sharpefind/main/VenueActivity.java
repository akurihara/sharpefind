package com.sharpefind.main;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;


public class VenueActivity extends Activity {
	
	 private Bitmap _bitmap;
	 private ImageView _image;
	 private int _venuePosition;
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        //get user access token from intent extra 
	        String accessToken = this.getIntent().getExtras().getString("accessToken");	   
	        _venuePosition = this.getIntent().getExtras().getInt("venue");   	
	        
	        this.setContentView(R.layout.activity_venue);
	        
	        TextView textView = (TextView) this.findViewById(R.id.venue_text);
	        textView.setText("Tap an area of the map below to check in.");
	        textView.setPadding(20, 20, 15, 15);
	        
	        int venueDrawable;
	        if (_venuePosition == SharpeFindActivity.SHARPE_REFECTORY){
	        	venueDrawable = R.drawable.sharpe_refectory;
	        	 _bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sharpe_refectory_bmp);
	        } else {
	        	venueDrawable = R.drawable.verney_woolley;
	        	 _bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.verney_woolley_bmp);
	        }
	        
	        _image = (ImageView) this.findViewById(R.id.ratty_image);
	        _image.setImageResource(venueDrawable);
	        _image.setScaleType(ScaleType.FIT_XY);
	        
	        ClickListener clickListener = new ClickListener(accessToken, _venuePosition);
	        _image.setOnClickListener(clickListener);
	        _image.setOnTouchListener(new TouchListener(clickListener));
	        
	        ViewTreeObserver vto = _image.getViewTreeObserver();
	        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
	            public boolean onPreDraw() {
	                int finalHeight = _image.getMeasuredHeight();
	                int finalWidth = _image.getMeasuredWidth();
	                if (finalHeight > 0 && finalWidth > 0){
	                	_bitmap = Bitmap.createScaledBitmap(_bitmap, finalWidth, finalHeight, true);
	                }
	                //Log.v("LOG", "IMAGE: " + finalWidth + ", " + finalHeight);
	                return true;
	            }
	        });
	    }
	 
	private class ClickListener implements OnClickListener {
		
     private String _accessToken;
	 private int _xPos, _yPos, _venuePosition;
	 
	 private ClickListener(String accessToken, int venuePosition){
		 _accessToken = accessToken;
		 _venuePosition = venuePosition;
	 }
	 
	 public void onClick(View view) {
		 if (_xPos > _bitmap.getWidth() || _xPos < 0){
			 return;
		 } else if (_yPos > _bitmap.getHeight() || _yPos < 0){
			 return;
		 } else {
			 int color= _bitmap.getPixel(_xPos, _yPos);
			 
			 int pos = 0;
			 if (color == -16776961){
				 pos = 0;
			 } else if (color == -1){
				 pos = 1;
			 } else if (color == -65536){
				 pos = 2;
			 } else if (color == -256){
				 pos = 3;
			 } else if (color == -65281){
				 pos = 4;
			 } else if (color == -16711681){
				 pos = 5;
			 } else {
				 return;
			 }
			 
			 Intent intent = new Intent(VenueActivity.this, CheckInActivity.class);
	         intent.putExtra("accessToken", _accessToken);
	         intent.putExtra("venue", _venuePosition);
	         intent.putExtra("venueSpecific", pos);
	         

	         startActivity(intent);
			 return;
		 }
		
	 } 
	 
	 public void setX(int x){
		 _xPos = x;
	 }
	 
	 public void setY(int y){
		 _yPos = y;
	 }
 }
	 
	 private class TouchListener implements OnTouchListener {
		 
		 //private String _accessToken;
		 private ClickListener _clickListener;
		 
		 private TouchListener(ClickListener clickListener){
			 //_accessToken = accessToken;
			 _clickListener = clickListener;
		 }

		 public boolean onTouch(View view, MotionEvent event) {
				
			 int x = (int) event.getX();
			 int y = (int) event.getY();
			 
			 _clickListener.setX(x);
			 _clickListener.setY(y);
			 
			 return false;
		 } 
	 }
}

