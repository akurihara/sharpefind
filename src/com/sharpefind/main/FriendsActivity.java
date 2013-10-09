package com.sharpefind.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.parser.ParseException;

import com.sharpefind.support.APIUpdatingException;
import com.sharpefind.support.FriendListAdapter;
import com.sharpefind.support.FriendNode;
import com.sharpefind.support.FriendList;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/* 
 * This activity displays a list of the user's friends.  It contains a FriendsList instance
 * which is called to retrieve a LinkedList of friend information.
 */
public class FriendsActivity extends Activity{
	
	 private PullToRefreshListView _listView;
	 private ArrayList<FriendNode> _list;
	 private FriendList _friendsList;
	 private ProgressBar _spinner;
	 private TextView _loadingText;
	 
	 private static Context _context;

	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_context = this;
        setContentView(R.layout.activity_friends);

        String accessToken = this.getIntent().getExtras().getString("accessToken");	//gets user access token from intent extra
        Resources res = getResources();
        
        _friendsList = new FriendList(accessToken, res);
        _listView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_listview);
        _listView.setVisibility(View.GONE);
        
        _spinner = (ProgressBar) this.findViewById(R.id.friends_spinner);
        _loadingText = (TextView) this.findViewById(R.id.friends_loading);
        
        new DownloadFriends().execute();
	 }
	 
	 public static Context getContext(){
		 return _context;
	 }
	 
	 private class DownloadFriends extends AsyncTask<Void, Integer, ArrayList<FriendNode>> {

		 	private ProgressDialog _dialog;
		 
		 	@Override
			protected ArrayList<FriendNode> doInBackground(Void... arg0) {
				try {
					_list = _friendsList.getFriends();
					_friendsList.getCheckIns(_list);
				} catch (IOException e) {
					Log.v("LOG", "Error: " + e.getMessage());
					return null;
				} catch (APIUpdatingException e) {
					Log.v("LOG", "Error: " + e.getMessage());
					return null;
				} catch (ParseException e){
					Log.v("LOG", "Error: " + e.getMessage());
					return null;
				}
				return _list;
			}
		 
			@Override
			protected void onPostExecute(ArrayList<FriendNode> result) {
				if (result == null){
					return;
				}
				_listView.setAdapter((new FriendListAdapter(FriendsActivity.this, result)));
				_listView.setTextFilterEnabled(true);
				_listView.setOnRefreshListener(new RefreshListener(_listView));
				_spinner.setVisibility(View.GONE);
				_loadingText.setVisibility(View.GONE);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				params.topMargin = 0;
				params.rightMargin = 10;
				_loadingText.setLayoutParams(params);
				_listView.setVisibility(View.VISIBLE);
			}
		}
	 
	 
	 /* This listener is called when the user invokes the pull-to-refresh functionality */
	 private class RefreshListener implements OnRefreshListener{
	
		private View _view;
		 
		public RefreshListener(View view){
			_view = view;
		}
		 
		public void onRefresh() {	
			new Thread(new Runnable() {
			    public void run() {
			    	
			    	try{
				    	 _friendsList.getCheckIns(_list);
					} catch (IOException e) {
						Toast.makeText(FriendsActivity.this, "Could not connect to Foursquare!", Toast.LENGTH_SHORT).show();
					} catch (APIUpdatingException e){
						Toast.makeText(FriendsActivity.this, "Foursquare API is updating, try refreshing again soon.", Toast.LENGTH_SHORT).show();
					} catch (ParseException e){
						
					}			
					_view.post(new Runnable() {
						public void run() {
							_listView.onRefreshComplete();
						}
					});
			    }			    
			}).start();	
		}
	 }
}
