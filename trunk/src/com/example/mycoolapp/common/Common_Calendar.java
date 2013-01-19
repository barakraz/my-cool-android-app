package com.example.mycoolapp.common;

import java.text.Format;

import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.content.Context;
import android.content.ContextWrapper;

public class Common_Calendar {
	private static final String TAG = "Common_Calendar";
	
	public static boolean amIFreeNow(Context con){
		boolean result = true;
		
		ContextWrapper contextWrapper = new ContextWrapper(con.getApplicationContext());
		Cursor mCursor = contextWrapper.getContentResolver().query(
				Uri.parse("content://com.android.calendar/events"),
				new String[]{ "calendar_id", "title", "description", "dtstart", "dtend", "eventLocation", "availability"},
				null,
				null,
				CalendarContract.Events.DTSTART + " ASC");
		
		mCursor.moveToFirst();
		
		long ntime = System.currentTimeMillis();
		String[] CalNames = new String[mCursor.getCount()];
		int[] CalIds = new int[mCursor.getCount()];
		Format df = DateFormat.getDateFormat(con);
		Format tf = DateFormat.getTimeFormat(con);
		
		int i;
		for (i = 0; i < CalNames.length; i++) { 
			CalIds[i] = mCursor.getInt(0);             
			CalNames[i] = "CalendarID: " 		+ mCursor.getInt(0) +
							" Title: " 			+ mCursor.getString(1) +
							" Description: " 	+ mCursor.getString(2) +
							" Start Date: " 	+ df.format(mCursor.getLong(3)) + " on " + tf.format(mCursor.getLong(3)) + 
							" End Date : " 		+ df.format(mCursor.getLong(4)) + " on " + tf.format(mCursor.getLong(4)) +
							" Location : " 		+ mCursor.getString(5);  
			long StartTime = mCursor.getLong(3);  
			long EndTime = mCursor.getLong(4);  
			//do compare here  
			//if we are on the middle of something stop checking and leave the loop  
			if (((StartTime < ntime)&&(ntime < EndTime)) &&
					mCursor.getInt(6)==CalendarContract.Events.AVAILABILITY_BUSY) {
				try {
					Log.d(TAG, CalNames[i]);					
					result = false;
				} catch (Exception e) {
				//ignore
				}				
				break;  
			}
			mCursor.moveToNext();  
		}
		if (i==CalNames.length){
			Log.d(TAG, "The Calendar is Free");
			result = true;
		}		
		return result;
	}
}