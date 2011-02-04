package com.test;

import java.util.Timer;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Wifi_Test extends Activity {
	WifiManager wifi;

	TextView textStatus;
	TextView mapStatus;
	EditText edit;
	
	Timer timer;
	
	ScanTask scanner;
	int scanRate = 5000;
	
	boolean enabled;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        textStatus = (TextView) findViewById(R.id.my_textview);
        mapStatus = (TextView) findViewById(R.id.my_mapview);
        edit = (EditText) findViewById(R.id.editText1);
        
        Integer val = scanRate / 1000;
        edit.setText(val.toString());
        
        mapStatus.setText("");
        
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        
        enabled = wifi.isWifiEnabled();
        
        wifi.setWifiEnabled(true);
        
		//setContentView(textStatus);
		
		timer = new Timer();
		
		scanner = new ScanTask(textStatus, mapStatus, wifi);
		
		timer.scheduleAtFixedRate(scanner, 0, scanRate);
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	if(keyCode == 4)
    	{
			timer.cancel();	
			wifi.setWifiEnabled(enabled);
			this.finish();
			return true;
    	}
    	else
    		return super.onKeyDown(keyCode, event);
    }
    
    public void RefreshButton(View view){
    	ResetTimer();
    	scanner.run();
    }
    
    private void ResetTimer(){
    	if(Integer.parseInt(edit.getText().toString()) != scanRate / 1000){
    		scanRate = Integer.parseInt(edit.getText().toString()) * 1000;
    		timer.cancel();
    		timer.purge();
    		timer = new Timer();
    		scanner = new ScanTask(textStatus, mapStatus, wifi);
    		timer.scheduleAtFixedRate(scanner, 0, scanRate);
    	}
    }
 
}