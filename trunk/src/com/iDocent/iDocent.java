package com.iDocent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Timer;
import java.util.jar.Attributes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityManager;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//Main activity class
public class iDocent extends Activity implements OnInitListener{
	static final int DIALOG_SET_SCAN_RATE = 0;
	WifiManager wifi;
	
	//TTS Stuff
	TextToSpeech tts;
	private int MY_DATA_CHECK_CODE = 0;

	//Layout objects
	Menu menu;
    private GLSurfaceView mGLView;
    private Renderer mRenderer;
	
	//Timer objects
	Timer timer;	
	ScanTask scanner;
	Integer scanRate = 250;
	
	//locations
	float posX=20;
	float posY=-20;
	float posZ=0;
	
	float xStart=0;
	float yStart=0;
	
	//Boolean to turn off wifi at the end of the app if it was off at the start
	boolean wifiWasEnabled;
	
	ScanResultReceiver SRR;
	
	boolean accessibilityOn;
	
	Dialog downloadingAlert;
	
	LinkedList<Room> rooms;
	HashMap<Integer, Room> RoomsByNumber;
	private Dialog downloadingDLG;
	private boolean downloadedRooms = false;
	private int networkID = -1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        mGLView = new GLSurfaceView(this);
        mRenderer = new Renderer(this);
        mGLView.setRenderer(mRenderer);
        mGLView.setKeepScreenOn(true);
        setContentView(mGLView);
        
        UpdateLocation(posX, posY, posZ);
        
        //Obtain access to and turn on WiFi
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiWasEnabled = wifi.isWifiEnabled();
        wifi.setWifiEnabled(true);
        
        AccessibilityManager access = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        accessibilityOn = access.isEnabled();
        
		//Set up to capture Wi-Fi scan results ready event
        SRR = new ScanResultReceiver(this);
		IntentFilter i = new IntentFilter();
		i.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		registerReceiver(SRR,i);
		
        //Start the timer running the scanner
		timer = new Timer();		
		scanner = new ScanTask(wifi, this);	
		timer.scheduleAtFixedRate(scanner, 0, scanRate);
		
		//Test for TTS
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);	
		
		rooms = new LinkedList<Room>();
		RoomsByNumber = new HashMap<Integer, Room>();
    }

	/**
	 * Overrides the default key down event processing function to handle events
	 * specific to this application.
	                          
	{@link onKeyDown}. 
	 *
	                         
	@param  keyCode - the integer code value of the key that was pressed
			event - an object describing the event
	 *  
	   
	@return boolean - success of the event processing
	@see iDocent 
	 */
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	if(keyCode == 4)//Back key
    	{
    		//End the app gracefully
    		scanner.cancel();
			timer.cancel();	
			timer.purge();
			wifi.setWifiEnabled(wifiWasEnabled);
			tts.speak("Goodbye", TextToSpeech.QUEUE_FLUSH, null);
			tts.shutdown();
			mGLView.setKeepScreenOn(false);
			SRR.End();
			this.onBackPressed();
			this.finish();
			System.exit(0);
			return true;
    	}
    	else
    		return super.onKeyDown(keyCode, event);
    }
    
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			xStart=event.getX();
			yStart=event.getY();
			return true;
		}
		else if(event.getAction()==MotionEvent.ACTION_MOVE)
		{
			mRenderer.MoveCamera(event.getX()-xStart, event.getY()-yStart);
			return true;
		}
		else if(event.getAction()==MotionEvent.ACTION_UP)
		{
			mRenderer.CenterCamera();
			xStart = yStart = 0;
			return true;
		}
		return false;
	}
    
	/**
	 * Handles the event caused by the user pressing the Zoom in button
	                          
	{@link ZoomInButton}. 
	 *
	                         
	@param  none
	 *   
	 */
    public void ZoomInButton()
    {
    	mRenderer.zoomIn();
    }
    
	/**
	 * Handles the event caused by the user pressing the Zoom out button
	                          
	{@link ZoomOutButton}. 
	 *
	                         
	@param  none
	 *   
	 */
    public void ZoomOutButton()
    {
    	mRenderer.zoomOut();
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

	/**
	 * Redirects menu button press events to the appropriate functions.
	                          
	{@link onOptionsItemSelected}. 
	 *
	                         
	@param  item - the menu item that was pressed
	 *   
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
		case R.id.zoom_in:
			if(accessibilityOn)
				tts.speak("Zoom In", TextToSpeech.QUEUE_FLUSH, null);
	    	ZoomInButton();
	    	return true;
		case R.id.zoom_out:
			if(accessibilityOn)
				tts.speak("Zoom Out", TextToSpeech.QUEUE_FLUSH, null);
			ZoomOutButton();
			return true;
	    case R.id.select:
	    	if(accessibilityOn)
	    	{
	    		tts.stop();
				tts.speak("Select Destination", TextToSpeech.QUEUE_FLUSH, null);
	    	}
	        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
	        Spinner spinner = new RoomSelectSpinner(this, tts, accessibilityOn, rooms);
	        
//	        adapter.add("Select Room Number");
//	        for(Room r : rooms)
//	        {
//	        	adapter.add(r.getNumber()+" - "+r.getType().charAt(0)+r.getType().substring(1).toLowerCase());
//	        }
	        
	        alert.setView(spinner);
	        
	        final Spinner s = spinner;
	        
          alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
  	        final ArrayAdapter<CharSequence> a = ((RoomSelectSpinner)s).getAdapter();

        	  String selected = (a.getItem(s.getLastVisiblePosition()).toString());
        	  if(s.getLastVisiblePosition() != 0)
        	  {
	              Toast.makeText(getApplicationContext(), selected,
	                      Toast.LENGTH_SHORT).show();
	              tts.speak("Navigating to "+selected, TextToSpeech.QUEUE_FLUSH, null);
	              String tmp [] = selected.split(" - ");
	              String roomNum = tmp[0];
	              wifi.reconnect();
	              NavigationDownloader route = new NavigationDownloader(posX, posY, posZ, roomNum);
	              wifi.disconnect();
	              mRenderer.setRoute(route.GetNodes(), RoomsByNumber);     
        	  }
          }
      });

      alert.setNegativeButton("Cancel",
              new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                	  tts.speak("Canceling new room selection", TextToSpeech.QUEUE_FLUSH, null);
                      dialog.cancel();
                  }
              });
	        
	        alert.show();
	        
	    	return true;
	    	
	    case R.id.sound:
	    	if(accessibilityOn)
				tts.speak("Sound Options", TextToSpeech.QUEUE_FLUSH, null);
	        final AlertDialog.Builder alert2 = new AlertDialog.Builder(this);
	        
	        final SeekBar b = new SeekBar(this);
	        
	        b.measure(150, 30);
	        b.layout(0, 0, 150, 30);
	        b.forceLayout();
	        b.setPadding(15, 10, 15, 10);
	        
	        final AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	        double maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	        double curVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
	        b.setProgress((int) (b.getMax()*curVol/maxVol));
	        
	        alert2.setView(b);
	        
          alert2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
	             double curVol = b.getProgress();
	             double maxVol = b.getMax();
	             am.setStreamVolume(AudioManager.STREAM_MUSIC, 
	            		 (int) (curVol/maxVol*am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)), 0);
          }
      });

      alert2.setNegativeButton("Cancel",
              new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                	  dialog.cancel();
                  }
              });
	        
	        alert2.show();
	        
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
    
	/**
	 * Updates the location of the user in iDocent and in the renderer
	                          
	{@link UpdateLocation}. 
	 *
	                         
	@param  x - the location in the x direction
			y - the location in the y direction
	 *   
	 */
    public void UpdateLocation(float x, float y, float z)
    {
    	posX = x;
    	posY = Math.abs(y);
    	posZ = z;
    	
		if(posY > 28.4)
			posX = (132f+120f)/2.0f;
		else if(posX < 120)
			posY = (28.4f+16.4f)/2.0f;
		else if(posY < 28.4 && posX > 120)
		{
			posX = (132f+120f)/2.0f;
			posY = (28.4f+16.4f)/2.0f;
		}
    	
    	mRenderer.UpdateLocation(posX, -posY, posZ);
    }
    
	/**
	 * Obtain the location of the user in the x direction
	                          
	{@link getPosX}. 
	 *
	                         
	@param  none
	
	@return float - the user's location in the x direction
	 *   
	 */
    public float getPosX()
    {
    	return(posX);
    }
    
	/**
	 * Obtain the location of the user in the y direction
	                          
	{@link getPosY}. 
	 *
	                         
	@param  none
	
	@return float - the user's location in the y direction
	 *   
	 */
    public float getPosY()
    {
    	return(posY);
    }

	public void EndTimer() {
		scanner.cancel();
		timer.cancel();	
		timer.purge();		
	}

	public void DownloadRooms() {
			Thread t = new Thread(new RoomDownloader(this));
			t.start();
//			downloadingAlert = new Dialog(this);
//			TextView v = new TextView(this);
//			v.setText(" Downloading Room Locations ");
//			v.setTextSize(16);
//			v.setHeight(70);
//
//			downloadingAlert.setContentView(v);
//			
//			downloadingAlert.show();
	}

	public void RoomsReady(LinkedList<Room> rooms) {
		downloadedRooms  = true;
		this.rooms = rooms;
		for(Room r : rooms)
		{
			RoomsByNumber.put(r.getNumber(), r);
		}
		downloadingDLG.dismiss();	     
		networkID  = wifi.getConnectionInfo().getNetworkId();
		wifi.disconnect();
	}

	public void showLoadingAPDLG() {
		TextView tv = new TextView(this);
		downloadingDLG = new Dialog(this);
		tv.setText(" Downloading Information ");
		tv.setTextSize(16);
		tv.setHeight(70);
		downloadingDLG.setContentView(tv);
		downloadingDLG.setCancelable(false);
		downloadingDLG.show();
	}

	public void APsReady() {
//		downloadingDLG.dismiss();
//		downloadingDLG = null;
		DownloadRooms();
	}

	public boolean DownloadedRooms() {
		// TODO Auto-generated method stub
		return downloadedRooms;
	}
}