package com.example.mycoolapp.common;

import java.util.HashMap;
import java.util.Map;

import android.media.AudioManager;
import android.content.Context; 

public class Common_AudioManager {
	
	public static enum RingerMode {
		SILENT (AudioManager.RINGER_MODE_SILENT,"Silent"),
		VIBRATE (AudioManager.RINGER_MODE_VIBRATE,"Vibrate"),
		NORMAL (AudioManager.RINGER_MODE_NORMAL,"Normal");
		
		private int code;
		private String lable;
		
		private static Map<String, RingerMode> lableToCodeMapping;
		
		private RingerMode(int c, String l){
			this.code = c;
			this.lable = l;
		}
		
		public int getCode(){
			return code;
		}
		
		public String getLable(){
			return lable;
		}
		
		private static void initMapping() {
			lableToCodeMapping = new HashMap<String, RingerMode>();
	        for (RingerMode s : values()) {
	        	lableToCodeMapping.put(s.lable, s);
	        }
	    }
		
		public static int getCodeByString(String l) {
	        if (lableToCodeMapping == null) {
	            initMapping();
	        }
	        return lableToCodeMapping.get(l).code;
	    }
	}
	
	public int setRinger(Context con, int ringerMode){
		AudioManager mAudioManager = (AudioManager)con.getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setRingerMode(ringerMode);
		switch(ringerMode){
		case AudioManager.RINGER_MODE_SILENT:
			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			return 0;
		case AudioManager.RINGER_MODE_VIBRATE:
			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
			return 0;
		case AudioManager.RINGER_MODE_NORMAL:
			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			return 0;
		default:
			return -1;
		}
	}	
}
