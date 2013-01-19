package com.example.mycoolapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.preference.PreferenceManager;

import com.example.mycoolapp.common.*;

public class MainActivity extends Activity implements OnClickListener{
	private static final String TAG = "MainActivity";
	Button buttonStart, buttonStop;
	  
	int myCounter = 0;
	protected View handler;
	Timer myt = new Timer();  
	TimerTask myTimerTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		buttonStart = (Button) findViewById(R.id.buttonStart);
		buttonStop = (Button) findViewById(R.id.buttonStop);
	
	    buttonStart.setOnClickListener(this);
	    buttonStop.setOnClickListener(this);
	    
	    initEnv();
	        
	}
	
	private void initEnv(){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("periodTime", 50); // value to store
		editor.commit();
	}
	  
	public void onClick(View src) {
		switch (src.getId()) {
		case R.id.buttonStart:   
      
      /*****
      if(Common_Calendar.amIFreeNow(getApplication())==false){
    	  Common_AudioManager ca = new Common_AudioManager();
    	  Log.d(TAG, "" + ca.setRinger(getApplication(), 1));
      }
      else{
    	  Common_AudioManager ca = new Common_AudioManager();
    	  Log.d(TAG, "" + ca.setRinger(getApplication(), 2));
      }   
      ******/     
      
			if (Common_lib.isMyServiceRunning(getApplication(), MyService.class.getName()) == false){
				startService(new Intent(this, MyService.class));
				Log.d(TAG, "onClick: starting srvice");
			}
			else{
				Log.d(TAG, "onClick: Service is already runing");
			}
      
			break;
		case R.id.buttonStop:
			Log.d(TAG, "onClick: stopping srvice");
			stopService(new Intent(this, MyService.class));
			break;
		}
	}
	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		return true;
	}
	  
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.settings:
				//startActivity(new Intent(this, About.class));
				Log.d(TAG, "onOptionsItemSelected: Settings.class");
				Intent k = new Intent(this, SettingsActivity.class);
		        startActivity(k);
				return true;
			case R.id.help:
				//startActivity(new Intent(this, Help.class));
				Log.d(TAG, "onOptionsItemSelected: Help.class");
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
