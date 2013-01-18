package com.example.common;

import android.media.AudioManager;
import android.content.Context; 

public class Common_AudioManager {
	
	public int setRinger(Context con, int ringerMode){
		AudioManager mAudioManager = (AudioManager)con.getSystemService(Context.AUDIO_SERVICE);
		switch(ringerMode){
		case AudioManager.RINGER_MODE_SILENT:
			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			return 0;
		case AudioManager.RINGER_MODE_VIBRATE:
			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			return 0;
		case AudioManager.RINGER_MODE_NORMAL:
			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			return 0;
		default:
			return -1;
		}		
	}	
}
