package com.example.mycoolapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.preference.PreferenceManager;

import com.example.mycoolapp.common.*;

public class MainActivity extends Activity implements OnClickListener{
	private static final String TAG = "MainActivity";
	Button buttonStart, buttonStop;
	private Intent intentBroadcast;	
	  
	int myCounter = 0;
	protected View handler;
	Timer myt = new Timer();  
	TimerTask myTimerTask;
	BatteryChangeBroadcastReceiver mBatteryLevelReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		buttonStart = (Button) findViewById(R.id.buttonStart);
		buttonStop = (Button) findViewById(R.id.buttonStop);
	
	    buttonStart.setOnClickListener(this);
	    buttonStop.setOnClickListener(this);
	    
	    initEnv();
	    
	    mBatteryLevelReceiver=new BatteryChangeBroadcastReceiver();
	    registerReceiver(mBatteryLevelReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	    
	    intentBroadcast = new Intent(this, MyService.class);        
        registerReceiver(broadcastReceiver, new IntentFilter(Common_lib.BROADCAST_ACTION));
	}
	
	private void initEnv(){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("periodTime", 5); // value to store
		editor.putString("meetingMode", "Vibrate");
		editor.putString("freeMode", "Normal");
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
				TextView tv = (TextView)findViewById(R.id.textView_ServiceStatus);
				tv.setText("Service is up...");
			}
			else{
				Log.d(TAG, "onClick: Service is already runing");
			}
      
			break;
		case R.id.buttonStop:
			Log.d(TAG, "onClick: stopping srvice");
			stopService(new Intent(this, MyService.class));
			TextView tv = (TextView)findViewById(R.id.textView_ServiceStatus);
			tv.setText("Service Is Down...");
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
				Log.d(TAG, "onOptionsItemSelected: Settings.class");
				if (Common_lib.isMyServiceRunning(getApplication(), MyService.class.getName()) == false){				
					Intent k = new Intent(this, SettingsActivity.class);
			        startActivity(k);
				}
				else{
					Toast.makeText(this, "Stop service before :)", Toast.LENGTH_LONG).show();
				}
				return true;
			case R.id.help:
				//startActivity(new Intent(this, Help.class));
				Log.d(TAG, "onOptionsItemSelected: Help.class");
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	public class BatteryChangeBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
		      int batteryLevel=intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
		      int maxLevel=intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);  
		      int batteryHealth=intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 
		                         BatteryManager.BATTERY_HEALTH_UNKNOWN);                
		      float batteryPercentage=((float)batteryLevel/(float)maxLevel) * 100;
	
		      
			  //Log.d(TAG,"Health - " + batteryHealth);
			  //Log.d(TAG,"Level - " + batteryLevel);
			  //Log.d(TAG,"Percentage - " + batteryPercentage);
			  
		      //Do something with batteryPercentage and batteryHealth. 
		      //batteryPercentage here ranges 0-100
		}		
	}
	
	@Override
	public void onDestroy(){
	       super.onDestroy();
	       unregisterReceiver(mBatteryLevelReceiver);
	}
	
	private void updateServiceStatus(Intent intent){
		String serviceDetails = intent.getStringExtra("serviceDetails");
	  	Log.d(TAG, serviceDetails);
	  		
	  	TextView txtFree = (TextView) findViewById(R.id.textView_ServiceStatus);
	  	txtFree.setText(serviceDetails);
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
	      @Override
	      public void onReceive(Context context, Intent intent) {
	    	  updateServiceStatus(intent);       
	      }
	};
	
	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(broadcastReceiver, new IntentFilter(Common_lib.BROADCAST_ACTION));
		Log.d(TAG, "On Resume");  
	}
  
	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(broadcastReceiver);
		Log.d(TAG, "On Pause");
	}
}
