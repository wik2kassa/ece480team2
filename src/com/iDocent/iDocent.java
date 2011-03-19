package com.iDocent;

import java.util.Timer;

import com.iDocent.R;
import com.iDocent.ScanTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager.OnDismissListener;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.tts.*;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.content.Intent;

//Main activity class
public class iDocent extends Activity implements OnInitListener{
	static final int DIALOG_SET_SCAN_RATE = 0;
	WifiManager wifi;
	
	//TTS Stuff
	TextToSpeech tts;
	private int MY_DATA_CHECK_CODE = 0;

	//Layout objects
	TextView textStatus;
	Menu menu;
    private GLSurfaceView mGLView;
	
	//Timer objects
	Timer timer;	
	ScanTask scanner;
	Integer scanRate = 50000;
	
	//Boolean to turn off wifi at the end of the app if it was off at the start
	boolean wifiWasEnabled;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        mGLView = new GLSurfaceView(this);
        mGLView.setRenderer(new Renderer());
        setContentView(mGLView);
      
        //Get the objects described in the layout xml file main.xml
        textStatus = (TextView) findViewById(R.id.my_textview);
      
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
    	//else if(keyCode == 82)//Menu key
    	//{
    		
			//return true;
    	//}
    	else
    		return super.onKeyDown(keyCode, event);
    }
    
    /*
	 * Name : RefreshButton()
	 * Description : Handles what should happen when the refresh button is pressed
	 * Parameters : View view 
	 * Returns : void
	*/
    public void RefreshButton(){
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
		timer.cancel();
		timer.purge();
		timer = new Timer();
		scanner = new ScanTask(textStatus, wifi);
		timer.scheduleAtFixedRate(scanner, 0, scanRate);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.refresh:
	        RefreshButton();
	        return true;
	    case R.id.activate_edit:	 
	        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
	        final EditText input = new EditText(this);
	        input.setText(((Integer)(scanRate/1000)).toString());
	        input.setWidth(150);
	        input.setGravity(Gravity.RIGHT);
	        alert.setView(input);
	        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	                String value = input.getText().toString().trim();
	                scanRate = Integer.parseInt(value) * 1000;
	                Toast.makeText(getApplicationContext(), ((Integer)(scanRate/1000)).toString(),
	                        Toast.LENGTH_SHORT).show();
	                RefreshButton();
	            }
	        });
	 
	        alert.setNegativeButton("Cancel",
	                new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                        Toast.makeText(getApplicationContext(), ((Integer)(scanRate/1000)).toString(),
	                                Toast.LENGTH_SHORT).show();
	                        dialog.cancel();
	                    }
	                });
	        
	        alert.show();
	        
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }
}