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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnClickListener, OnItemSelectedListener {
	private static final String TAG = "SettingsActivity";
	Button settingsCancle, settingsUpdate;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
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
 	  	
 	  	Spinner spinner_Meeting_Mode = (Spinner) findViewById(R.id.planets_spinner_Meeting_Mode);
 	  	// Create an ArrayAdapter using the string array and a default spinner layout
	 	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	 	        R.array.planets_array, android.R.layout.simple_spinner_item);
	 	// Specify the layout to use when the list of choices appears
	 	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 	
	 	// Apply the adapter to the spinner
	 	spinner_Meeting_Mode.setAdapter(adapter);
	 	String ringerMode = preferences.getString("meetingMode", null);
	 	if (ringerMode != null){
	 		spinner_Meeting_Mode.setSelection(adapter.getPosition(ringerMode));
	 	}
	 	spinner_Meeting_Mode.setOnItemSelectedListener(this);
	 	
	 	Spinner spinner_Free_Mode = (Spinner) findViewById(R.id.planets_spinner_Free_Mode);
	 	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 	// Apply the adapter to the spinner
	 	spinner_Free_Mode.setAdapter(adapter);
	 	ringerMode = preferences.getString("freeMode", null);
	 	if (ringerMode != null){
	 		spinner_Free_Mode.setSelection(adapter.getPosition(ringerMode));
	 	}
	 	spinner_Free_Mode.setOnItemSelectedListener(this);
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
			
			Spinner s = (Spinner) findViewById(R.id.planets_spinner_Free_Mode);
			editor.putString("freeMode", s.getSelectedItem().toString());
			
			s = (Spinner) findViewById(R.id.planets_spinner_Meeting_Mode);
			editor.putString("meetingMode", s.getSelectedItem().toString());
			
			editor.commit();
			super.onBackPressed();
			break;
		case R.id.settingsCancle:
			Log.d(TAG, "onClick settingsCancle");
			super.onBackPressed();
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Log.d(TAG,"onItemSelected - " + parent.getItemAtPosition(position).toString());
		switch(parent.getId()){
		case R.id.planets_spinner_Free_Mode:
			break;
		case R.id.planets_spinner_Meeting_Mode:
			break;
		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
