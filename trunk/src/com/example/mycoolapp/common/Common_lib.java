package com.example.mycoolapp.common;

import java.util.HashMap;
import java.util.Map;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.media.AudioManager;

public class Common_lib {	
	public static boolean isMyServiceRunning(Context con, String className) {
	    ActivityManager manager = (ActivityManager) con.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (className.equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public static final String BROADCAST_ACTION = "com.example.displayevent";
}

