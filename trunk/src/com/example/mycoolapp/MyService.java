package com.example.mycoolapp;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mycoolapp.common.*;

public class MyService extends Service {
	private static final String TAG = "MyService";
	private Intent intentBroadcast;
	final Handler handler = new Handler();
	Timer t = new Timer();

	AudioManager mAudioManager;
	private int nCounter = 0;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate");
		
		intentBroadcast = new Intent(Common_lib.BROADCAST_ACTION);
		mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
		stopTask();
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");
		doTimerTask();
	}
	
	 public void doTimerTask(){
		 		 
		 TimerTask mTimerTask = new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						nCounter++;
						Log.d(TAG, "TimerTask run " + nCounter);
						
						SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
						String freeMode = preferences.getString("freeMode", null);		 
						String meetingMode = preferences.getString("meetingMode", null);
						 
						boolean amIFree = Common_Calendar.amIFreeNow(getApplication());
						Log.d(TAG,"Am I Free? " + amIFree);
						Common_AudioManager ca = new Common_AudioManager();
						
						if (amIFree){
							ca.setRinger(getApplication(), Common_AudioManager.RingerMode.getCodeByString(freeMode));
						}
						else{
							ca.setRinger(getApplication(), Common_AudioManager.RingerMode.getCodeByString(meetingMode));
						}
					}
				});
			}
		}; 
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		int periodInMins = preferences.getInt("periodTime", 0);
		Log.d(TAG,"periodInMins - " + periodInMins);
		
	    Calendar c = Calendar.getInstance();	    	    
	    GregorianCalendar gc = new GregorianCalendar (c.get(Calendar.YEAR),
	    		c.get(Calendar.MONTH),
	    		c.get(Calendar.DAY_OF_MONTH),
	    		c.get(Calendar.HOUR_OF_DAY),
	    		periodInMins - (c.get(Calendar.MINUTE)%periodInMins) + c.get(Calendar.MINUTE));  
	    
	    if ((t = Common_Scheduler.setNewTask(mTimerTask, gc.getTime(),periodInMins*1000*60)) == null){
	    	Log.d(TAG,"Task didn't schedule");
	    }
	    else{
	    	Log.d(TAG,"New Task is schedule");
	    }
		
		intentBroadcast.putExtra("serviceDetails", t.toString());
    	sendBroadcast(intentBroadcast);

	    mTimerTask.run();
	}
 
	public void stopTask(){ 
		if(t!=null){						
			if (Common_Scheduler.delTask(t)){
				Log.d(TAG, "timer canceled");
			}
			else{
				Log.d(TAG, "Error with timer cancellation");
			}
		}
	}
}

