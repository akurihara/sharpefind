package com.sharpefind.main;

import com.sharpefind.support.CheckIn;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class CheckInActivity extends Activity{
	
	private String _accessToken;
	private String[] _thumbNames = {
    		"Sharpe Refectory", "Verney-Woolley",
    		"Josiah's", "The Gate",
    		"Blue Room", "Ivy Room"
    };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    Bundle bundle = this.getIntent().getExtras();
	    String accessToken = bundle.getString("accessToken");			//gets user access token from intent extra
	    _accessToken = accessToken;
	    int venuePosition = bundle.getInt("venue");						//gets the selected eatery from extra
	    int venueSpecificPosition = bundle.getInt("venueSpecific"); 
	    setContentView(R.layout.activity_check_in);

	    //get resources
	    Resources res = getResources();
	    
	    String currEatery = "";
	    if (venuePosition == SharpeFindActivity.SHARPE_REFECTORY){
	    	currEatery = res.getStringArray(R.array.sharpe_refectory_array)[venueSpecificPosition];
	    } else if (venuePosition == SharpeFindActivity.VERNEY_WOOLLEY){
	    	currEatery = res.getStringArray(R.array.verney_woolley_array)[venueSpecificPosition];
	    } else {
	    	currEatery = _thumbNames[venuePosition];
	    }
	    
	    //display Eatery string by indexing eateries_array with eatery position from bundle
	    TextView header = (TextView) findViewById(R.id.header);
	    header.setText("You are about to check into:");
	    
	    TextView header2 = (TextView) findViewById(R.id.header2);
	    header2.setText(currEatery);
	    
	    //create a new CheckIn
	    CheckIn checkIn = new CheckIn(accessToken, venuePosition, currEatery, res);
	    
	    //set up the spinner to select duration to spend dining
	    Spinner spinner = (Spinner) findViewById(R.id.spinner);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.time_array, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    spinner.setOnItemSelectedListener(new TimeSelectListener(checkIn, res.getStringArray(R.array.time_value_array)));
	    
	    //set up button
	    Button btn = (Button)findViewById(R.id.confirm_button);
        btn.setOnClickListener(new ClickListener(checkIn, this));
	}
	
	private class ClickListener implements OnClickListener {

		private CheckIn _checkIn;
		private CheckInActivity _activity;
		
		private ClickListener(CheckIn checkIn, CheckInActivity activity){
			_checkIn = checkIn;
			_activity = activity;
		}
		
		public void onClick(View v) {	
			boolean success = _checkIn.checkIn();
			if (success){
				Toast.makeText(CheckInActivity.this, "Check In Success!",Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(CheckInActivity.this, "Check In Failed! Try again soon.",Toast.LENGTH_LONG).show();
			}
			Intent intent = new Intent(CheckInActivity.this, MainViewActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("accessToken", _accessToken);
			startActivity(intent);
			_activity.finish();
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	private class TimeSelectListener implements OnItemSelectedListener {
		
		private CheckIn _checkIn;
		private String[] _times;
		
		public TimeSelectListener(CheckIn checkIn, String[] times){
			super();
			_checkIn = checkIn;
			_times = times;
		}
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				_checkIn.setTime(Integer.parseInt(_times[pos]));
		    }
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
}


