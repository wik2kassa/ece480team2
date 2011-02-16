package com.iDocent;

import java.util.Timer;

import com.iDocent.R;
import com.iDocent.ScanTask;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.*;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.content.Intent;

//Main activity class
public class iDocent extends Activity implements OnInitListener{
	WifiManager wifi;
	
	//TTS Stuff
	TextToSpeech tts;
	private int MY_DATA_CHECK_CODE = 0;

	//Layout objects
	TextView textStatus;
	EditText edit;
	
	//Timer objects
	Timer timer;	
	ScanTask scanner;
	int scanRate = 50000;
	
	//Boolean to turn off wifi at the end of the app if it was off at the start
	boolean wifiWasEnabled;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
      
        //Get the objects described in the layout xml file main.xml
        textStatus = (TextView) findViewById(R.id.my_textview);
<<<<<<< .mine
        //mapStatus = (TextView) findViewById(R.id.my_mapview);
=======
>>>>>>> .r17
        edit = (EditText) findViewById(R.id.editText1);
        
        //Initialize the text fields in the layout objects
        Integer val = scanRate / 1000;
        edit.setText(val.toString());       
        //mapStatus.setText("");
        
        //Obtain access to and turn on WiFi
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiWasEnabled = wifi.isWifiEnabled();
        wifi.setWifiEnabled(true);
		
        //Start the timer running the scanner
		timer = new Timer();		
		scanner = new ScanTask(textStatus, wifi);	
		timer.scheduleAtFixedRate(scanner, 0, scanRate);
		
		//Test for TTS
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
    }
    /*
	 * Name : onKeyDown()
	 * Description : 	Captures key down events
	 * Parameters : keyCode - the code of the key that was pressed 
	 * 				event - an object describing the event
	 * Returns : boolean
	*/
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	if(keyCode == 4)//Back key
    	{
    		//End the app gracefully
			timer.cancel();	
			wifi.setWifiEnabled(wifiWasEnabled);
			tts.speak("Goodbye", TextToSpeech.QUEUE_FLUSH, null);
			tts.shutdown();
			this.finish();
			return true;
    	}
    	else
    		return super.onKeyDown(keyCode, event);
    }
    
    /*
	 * Name : RefreshButton()
	 * Description : Handles what should happen when the refresh button is pressed
	 * Parameters : View view 
	 * Returns : void
	*/
    public void RefreshButton(View view){
    	ResetTimer();
    	scanner.run();
    }
    
    /*
	 * Name : ResetTimer()
	 * Description : Resets the timer to the new desired value if there is an updated value in the edit text
	 * Returns : void
	*/
    private void ResetTimer(){
    	scanner.SetTextView(textStatus);
    	//tts.speak("Timer Reset", TextToSpeech.QUEUE_ADD, null);
    	if(Integer.parseInt(edit.getText().toString()) != scanRate / 1000){
    		scanRate = Integer.parseInt(edit.getText().toString()) * 1000;
    		timer.cancel();
    		timer.purge();
    		timer = new Timer();
    		scanner = new ScanTask(textStatus, wifi);
    		timer.scheduleAtFixedRate(scanner, 0, scanRate);
    	}
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent Data){
    	if(requestCode == MY_DATA_CHECK_CODE){
    		if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
    			//Libraries are there
    			tts = new TextToSpeech(this, this);
    		}
    		else{
    			///Missing proper libraries, download them
    			Intent installIntent = new Intent();
    			installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
    			startActivity(installIntent);
    		}
    	}
    }
	@Override
	public void onInit(int status) {
		if(status == TextToSpeech.SUCCESS){
			
		}
		else{
			
		}
		
	}


}