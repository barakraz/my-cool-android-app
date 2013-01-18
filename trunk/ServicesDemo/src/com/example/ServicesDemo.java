package com.example;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.example.common.*;

public class ServicesDemo extends Activity implements OnClickListener {
  private static final String TAG = "ServicesDemo";
  Button buttonStart, buttonStop;
  
  int myCounter = 0;
  protected View handler;
  Timer myt = new Timer();  
  TimerTask myTimerTask;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    buttonStart = (Button) findViewById(R.id.buttonStart);
    buttonStop = (Button) findViewById(R.id.buttonStop);

    buttonStart.setOnClickListener(this);
    buttonStop.setOnClickListener(this);
        
  }
  
  public void onClick(View src) {
    switch (src.getId()) {
    case R.id.buttonStart:
      Log.d(TAG, "onClick: starting srvice");
      Common_Calendar cc = new Common_Calendar();
      if(cc.amIFreeNow(getApplication())==false){
    	  Common_AudioManager ca = new Common_AudioManager();
    	  Log.d(TAG, "" + ca.setRinger(getApplication(), 1));
      }
      else{
    	  Common_AudioManager ca = new Common_AudioManager();
    	  Log.d(TAG, "" + ca.setRinger(getApplication(), 2));
      }
      //startService(new Intent(this, MyService.class));
      break;
    case R.id.buttonStop:
      Log.d(TAG, "onClick: stopping srvice");
      //stopService(new Intent(this, MyService.class));
      break;
    }
  }
}