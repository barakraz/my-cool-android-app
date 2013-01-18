package com.example;

import java.text.Format;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;
import android.provider.CalendarContract;

public class MyService extends Service {
	private static final String TAG = "MyService";
	
	TimerTask mTimerTask;
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
		 mTimerTask = new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						nCounter++;
						Log.d(TAG, "TimerTask run " + nCounter);
						amIFree();
					}
				});
			}
		};
 
			// public void schedule (TimerTask task, long delay, long period)
		int periodInMins = 1;
	    Calendar c = Calendar.getInstance();
	    	    
	    GregorianCalendar gc = new GregorianCalendar (c.get(Calendar.YEAR),
	    		c.get(Calendar.MONTH),
	    		c.get(Calendar.DAY_OF_MONTH),
	    		c.get(Calendar.HOUR_OF_DAY),
	    		periodInMins - (c.get(Calendar.MINUTE)%periodInMins) + c.get(Calendar.MINUTE));
	    Log.d(TAG, gc.getTime().toString());
	    
	    //t.schedule(mTimerTask, gc.getTime(),periodInMins*60*1000);
	    t.schedule(mTimerTask, gc.getTime(),periodInMins*2000);
		amIFree();
	}
 
	public void stopTask(){ 
		if(mTimerTask!=null){ 
			Log.d(TAG, "timer canceled");
			mTimerTask.cancel();
		}
	}
	
	private void amIFree(){
		Cursor mCursor = getContentResolver().query(
				Uri.parse("content://com.android.calendar/events"),
				new String[]{ "calendar_id", "title", "description", "dtstart", "dtend", "eventLocation", "availability"},
				null,
				null,
				CalendarContract.Events.DTSTART + " ASC");
		
		mCursor.moveToFirst();
		
		long ntime = System.currentTimeMillis();
		String[] CalNames = new String[mCursor.getCount()];
		int[] CalIds = new int[mCursor.getCount()];  
		int i;
		for (i = 0; i < CalNames.length; i++) {  
			CalIds[i] = mCursor.getInt(0);             
			CalNames[i] = "Event"+mCursor.getInt(0)+": \nTitle: "+ mCursor.getString(1)+"\nDescription: "+mCursor.getString(2)+"\nStart Date: "+mCursor.getLong(3)+"\nEnd Date : "+mCursor.getLong(4)+"\nLocation : "+mCursor.getString(5);  
			long StartTime = mCursor.getLong(3);  
			long EndTime = mCursor.getLong(4);  
			//do compare here  
			//if we are on the middle of something stop checking and leave the loop  
			if ((StartTime<ntime)&&(ntime<EndTime)&&mCursor.getInt(6)==CalendarContract.Events.AVAILABILITY_BUSY) {
				Format df = DateFormat.getDateFormat(this);
				Format tf = DateFormat.getTimeFormat(this);
				String title;
				long start;
				try {
					title = mCursor.getString(1);
					start = mCursor.getLong(3);
					System.out.println("CalendarID - " + mCursor.getString(0)+ " " +title+" on "+df.format(start)+" at "+tf.format(start) + " Avaliability "+ mCursor.getInt(6));
					System.out.println("In the middle of something - " + mCursor.getInt(6));
					
					//mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
					
					Log.d(TAG, "In the middle of something - " + mCursor.getInt(6)+"\n" + "CalendarID - " + mCursor.getInt(0)+ " " +title+" on "+df.format(start)+" at "+tf.format(start));
				} catch (Exception e) {
				//ignore
				}				
				break;  
			}
			mCursor.moveToNext();  
		}
		if (i==CalNames.length){
			Log.d(TAG, "The Calendar is Free");
			//mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		}
	}
}
