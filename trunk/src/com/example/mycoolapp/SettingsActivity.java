package com.example.mycoolapp;

import com.example.mycoolapp.common.Common_lib;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnClickListener {
	private static final String TAG = "SettingsActivity";
	Button settingsCancle, settingsUpdate;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "SettingsActivity create");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		settingsCancle = (Button) findViewById(R.id.settingsCancle);
		settingsUpdate = (Button) findViewById(R.id.settingsUpdate);
	
		settingsCancle.setOnClickListener(this);
	    settingsUpdate.setOnClickListener(this);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		
		int storedPreference = preferences.getInt("periodTime", 0);
		EditText t = (EditText) findViewById(R.id.periodTime);
 	  	t.setText("" + storedPreference, TextView.BufferType.EDITABLE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.settingsUpdate:
			Log.d(TAG, "onClick settingsUpdate");
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			
			EditText t = (EditText) findViewById(R.id.periodTime);
			
			editor.putInt("periodTime", Integer.parseInt(t.getText().toString())); // value to store
			editor.commit();
			super.onBackPressed();
			break;
		case R.id.settingsCancle:
			Log.d(TAG, "onClick settingsCancle");
			super.onBackPressed();
			break;
		}
	}
}
