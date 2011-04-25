/*
 * ECE 480 Spring 2011
 * Team 2 Design Project
 * Matt Gottshall
 * Jake D'Onofrio
 * Gordie Stein
 * Andrew Kling
 */
package com.iDocent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;

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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**Main activity class*/
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
	Integer scanRate = 500;
	
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
	
	private boolean mShowRoomNums = true;
	private boolean stayActive = false;
	
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
        
        //Start the timer running the scanner
		timer = new Timer();		
		scanner = new ScanTask(wifi, this);	
		
		//Set up to capture Wi-Fi scan results ready event
        SRR = new ScanResultReceiver(this);
		IntentFilter i = new IntentFilter();
		i.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		registerReceiver(SRR,i);
        
        if(wifiWasEnabled)
        {
        	networkID = wifi.getConnectionInfo().getNetworkId();
//        	wifi.disconnect();
//        	wifi.enableNetwork(networkID, true);
        	wifi.startScan();
        }
        else
        {        
        	wifi.setWifiEnabled(true);
    		timer.scheduleAtFixedRate(scanner, 0, scanRate);
        }
        AccessibilityManager access = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        accessibilityOn = access.isEnabled();
		
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
			wifi.setWifiEnabled(false);
			wifi.setWifiEnabled(wifiWasEnabled);
			if(wifiWasEnabled)
			{
				wifi.enableNetwork(networkID, true);
			}
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
    
    /**
     * Handles touch events to change the position of the view.
     */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Handle touch events to move the map around
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
    			mRenderer.setTTS(tts);
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
	    	SelectButton();
	  		  
	    	return true;
	    	
	    case R.id.options:
	    	//Handle the options menu
	    	if(accessibilityOn)
				tts.speak("Options", TextToSpeech.QUEUE_FLUSH, null);
	        final AlertDialog.Builder alert2 = new AlertDialog.Builder(this);
	        
	        //get the toold to change the media volume of the phone
	        final AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	        double maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	        final double curVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
	        
	        LinearLayout ll = new LinearLayout(this);
	        ll.setOrientation(LinearLayout.VERTICAL);
	        final VolumeBar b = new VolumeBar(this, tts, am);
	        
	        b.measure(150, 30);
	        b.layout(0, 0, 150, 30);
	        b.forceLayout();
	        b.setPadding(15, 10, 15, 10);
	        
	        //Set up the bar to start at the current volume
	        b.setProgress((int) (b.getMax()*curVol/maxVol));
	        TextView tv = new TextView(this);
	        tv.setText("  Speech Volume:");
	        ll.addView(tv);
	        ll.addView(b);
	        //a check box to disable/enable room number drawing
	        final CheckBox cb = new CheckBox(this);
	        cb.setText("Show room numbers");
	        cb.setChecked(mShowRoomNums);
	        ll.addView(cb);
	        alert2.setView(ll);
	        
          alert2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
        	  mShowRoomNums = cb.isChecked();
          }
      });

      alert2.setNegativeButton("Cancel",
              new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                	  dialog.cancel();
                	  am.setStreamVolume(AudioManager.STREAM_MUSIC, (int) curVol, 0);
                  }
              });
	              
		Dialog alertDialog = alert2.create();
		alertDialog.setTitle("Options...");
		//alertDialog.getWindow().setLayout(15, 10);
		alertDialog.show();
		        
		return true;
	    	
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
    private void SelectButton() {  	
    	//handle room selection
    	stayActive = true;
    	wifi.enableNetwork(networkID, true);
    	wifi.reconnect();
      final AlertDialog.Builder alert = new AlertDialog.Builder(this);
      Spinner spinner = new RoomSelectSpinner(this, tts, accessibilityOn, rooms);
      
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
            if(!selected.toLowerCase().contains("clear"))
            {
            tts.speak("Navigating to "+selected, TextToSpeech.QUEUE_FLUSH, null);
            String tmp [] = selected.split(" - ");
            String roomNum = tmp[0];	              
            
            NavigationDownloader route = new NavigationDownloader(posX, posY, posZ, roomNum);	    		  
  		  
            mRenderer.setRoute(route.GetNodes(), RoomsByNumber, roomNum); 
            stayActive = false;
            
            //disable the network connection
			wifi.disconnect();
			wifi.disableNetwork(networkID);
			wifi.startScan();
			SRR.startLocationDLG();
            }
            else
            	mRenderer.clearRoute();
  	  }
    }
});

alert.setNegativeButton("Cancel",
        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
          	  tts.speak("Canceling new room selection", TextToSpeech.QUEUE_FLUSH, null);
                dialog.cancel();
                stayActive = false;
    			wifi.disconnect();
    			wifi.disableNetwork(networkID);
            }
        });
      
		AlertDialog aDLG = alert.create();
		aDLG.setCancelable(false);
		aDLG.setTitle("Navigate to Room...");
      aDLG.show();
      
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
    	if(wifi != null && !stayActive)
    	{
//			networkID  = wifi.getConnectionInfo().getNetworkId();
//			wifi.disconnect();
//			wifi.disableNetwork(networkID);
    	}
    	float[] normalLoc = LocationNormalizer.Normalize(x, y, z);
    	posX = normalLoc[0];
    	posY = normalLoc[1];
    	posZ = normalLoc[2];
    	
    	mRenderer.UpdateLocation(posX, posY, posZ);
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

    /**
     * End the timer running if it is no longer needed.
     */
	public void EndTimer() {
		scanner.cancel();
		timer.cancel();	
		timer.purge();	
		
		networkID = wifi.getConnectionInfo().getNetworkId();
	}

	/**
	 * Creates a new thread to download all the rooms from the server
	 */
	public void DownloadRooms() {
			Thread t = new Thread(new RoomDownloader(this));
			t.start();
	}

	/**
	 * This is called when the list of rooms has been downloaded from the server
	 * @param rooms - the list of the rooms
	 */
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
		wifi.disableNetwork(networkID);
	}

	/**
	 * Activates the Dialog box that states that the app is 
	 * "Downloading Information"
	 */
	public void showLoadingAPDLG() {
		TextView tv = new TextView(this);
		downloadingDLG = new Dialog(this);
		tv.setText("\tDownloading Information     \n");
		tv.setTextSize(20);
		//tv.setHeight(70);
		downloadingDLG.setContentView(tv);
		downloadingDLG.setCancelable(false);
		//downloadingDLG.getWindow().setLayout(300, 120);
		downloadingDLG.setTitle("Please wait....");
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
	
	public boolean ShowRoomNums()
	{
		return mShowRoomNums;
	}

	public WifiManager getWifi() {
		// TODO Auto-generated method stub
		return wifi;
	}
}