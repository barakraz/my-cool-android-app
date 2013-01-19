package com.example.mycoolapp.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

public class Common_Scheduler {
	private static final String TAG = "Common_Scheduler";	
	private final static int MaxTimers = 1; 
	
	private static List<Timer> timerQueue = null;
	
	protected Common_Scheduler(){
		
	}
	
	private static List<Timer> getSchedulerCurser(){
		if (timerQueue == null){
			timerQueue = new ArrayList<Timer>(MaxTimers);
			Log.d(TAG, "TimerList Created");
			return timerQueue;
		}
		return timerQueue;
	}
	
	private static int getScheduledTaskNum(){
		if (timerQueue == null){
			return 0;
		}
		else{
			return timerQueue.size();
		}
	}
	
	public static boolean delTask(Timer task){
		if (timerQueue == null){
			return false;
		}
		int i;
		if ( (i = timerQueue.indexOf(task)) == -1){
			Log.d(TAG,"Task " + task.toString() + " was not found");
			for (Timer t : timerQueue) {
				Log.d(TAG,t.toString());
			}
			return false;
		}
		timerQueue.get(i).cancel();
		Log.d(TAG,"Task - " + task.toString() + " was canceled");
		timerQueue.remove(i);
		return true;
	}
	
	public static Timer setNewTask(TimerTask task, Date when, long period){
		if (getScheduledTaskNum() ==  MaxTimers){
			Log.d(TAG,"TimerQueue is FULL!!!");
			return null;
		}
		Timer t = new Timer();
		t.schedule(task, when, period);
		if (getSchedulerCurser().add(t)){
			Log.d(TAG,"New Task is scheduled");
			return t;
		}
		return null;
	}	
}

