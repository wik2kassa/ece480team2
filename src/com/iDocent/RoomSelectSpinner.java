/*
 * ECE 480 Spring 2011
 * Team 2 Design Project
 * Matt Gottshall
 * Jake D'Onofrio
 * Gordie Stein
 * Andrew Kling
 */
package com.iDocent;

import java.util.LinkedList;

import android.content.Context;
import android.content.DialogInterface;
import android.speech.tts.TextToSpeech;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**The spinner to select the room
 * 
 *
 */
public class RoomSelectSpinner extends Spinner {
	TextToSpeech tts;
	boolean accessibilityOn;
	
	//possible states
	private static final int floor=0;
	private static final int first=1;
	private static final int second=2;
	private static final int third=3;
	private static final int firstH=4;
	private static final int first2H=5;
	private static final int first3H=6;
	private static final int firstR=7;
	private static final int secondH=8;
	private static final int second2H=9;
	private static final int second3H=10;
	private static final int second4H=12;
	private static final int secondR=11;
	
	
	private int state = floor;
	
	//private LinkedList<Room> rooms;
	
	ArrayAdapter<CharSequence> Floor;
	ArrayAdapter<CharSequence> FirstFloor;
	ArrayAdapter<CharSequence> SecondFloor;
	ArrayAdapter<CharSequence> ThirdFloor;
	
	ArrayAdapter<CharSequence> FirstFloorH;
	ArrayAdapter<CharSequence> FirstFloor2H;
	ArrayAdapter<CharSequence> FirstFloor3H;
	ArrayAdapter<CharSequence> FirstFloorR;
	
	ArrayAdapter<CharSequence> SecondFloorH;
	ArrayAdapter<CharSequence> SecondFloor2H;
	ArrayAdapter<CharSequence> SecondFloor3H;
	ArrayAdapter<CharSequence> SecondFloorR;
	
	ArrayAdapter<CharSequence> current;
	
	Context context;
	
	public RoomSelectSpinner(Context context, TextToSpeech tts, boolean accessibilityOn, LinkedList<Room> rooms) {
		super(context);
		this.context = context;
		this.tts = tts;
		this.accessibilityOn = accessibilityOn;
		//this.rooms = rooms;
		
        Floor = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item);
        FirstFloor = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item);
        SecondFloor = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item);
        ThirdFloor = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item);

        FirstFloorH = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item);
        FirstFloor2H = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item);
        FirstFloor3H = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item);
        FirstFloorR = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item);
        
        SecondFloorH = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item);
        SecondFloor2H = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item);
        SecondFloor3H = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item);
        SecondFloorR = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item);
        
        Floor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.setAdapter(Floor);
        current = Floor;

        //Fill in the lists for each drop down menu list
        Floor.add("Select Floor");
        Floor.add("Nearest Exit");
        Floor.add("First Floor");
        Floor.add("Second Floor");
        Floor.add("Third Floor");
        Floor.add("Clear Route");
        
        FirstFloor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FirstFloor.add("Select Room Range");
        FirstFloor.add("Back to Floor Selection");
        FirstFloor.add("Nearest Exit");
        FirstFloor.add("1100 - 1199");
        FirstFloor.add("1200 - 1299");
        FirstFloor.add("1300 - 1399");
        FirstFloor.add("Restroom");
        
        SecondFloor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SecondFloor.add("Select Room Range");
        SecondFloor.add("Back to Floor Selection");
        SecondFloor.add("Nearest Exit");
        SecondFloor.add("2100 - 2199");
        SecondFloor.add("2200 - 2299");
        SecondFloor.add("2300 - 2399");
       // SecondFloor.add("2400 - 2499");
        
        ThirdFloor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ThirdFloor.add("Select Room Range");
        ThirdFloor.add("Back to Floor Selection");
        ThirdFloor.add("Nearest Exit");
        ThirdFloor.add("3100 - 3199");
        ThirdFloor.add("3200 - 3299");
        ThirdFloor.add("3300 - 3399");
        
        FirstFloorH.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FirstFloor2H.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FirstFloor3H.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FirstFloorR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        FirstFloorH.add("Select Room Number");
        FirstFloorH.add("Back to Range Select");
        FirstFloor2H.add("Select Room Number");
        FirstFloor2H.add("Back to Range Select");
        FirstFloor3H.add("Select Room Number");
        FirstFloor3H.add("Back to Range Select");
        FirstFloorR.add("Select Restroom");
        FirstFloorR.add("Back to Range Select");
        FirstFloorR.add("Men's Restroom");
        FirstFloorR.add("Women's Restroom");
        
        SecondFloorH.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SecondFloor2H.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SecondFloor3H.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SecondFloorR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        SecondFloorH.add("Select Room Number");
        SecondFloorH.add("Back to Range Select");
        SecondFloor2H.add("Select Room Number");
        SecondFloor2H.add("Back to Range Select");
        SecondFloor3H.add("Select Room Number");
        SecondFloor3H.add("Back to Range Select");
        SecondFloorR.add("Select Restroom");
        SecondFloorR.add("Back to Range Select");
        SecondFloorR.add("Men's Restroom");
        SecondFloorR.add("Women's Restroom");
        
        //add all the possible rooms
        for(Room r : rooms)
        {
        	int num = r.getNumber();
        	String type = r.getType().charAt(0)+r.getType().substring(1).toLowerCase();
        	if(type.toLowerCase().contains("restroom"))
        		;
        	else if(num < 1200)
        		FirstFloorH.add(num+" - "+type);
        	else if(num >= 1200 && num < 1300 )
        		FirstFloor2H.add(num+" - "+type);
        	else if(num >= 1300 && num < 1400)
        		FirstFloor3H.add(num+" - "+type);
        	else if(num >= 2100 && num < 2200)
        		SecondFloorH.add(num+" - "+type);
        	else if(num >= 2200 && num < 2300)
        		SecondFloor2H.add(num+" - "+type);
        	else if(num >= 2300 && num < 2400)
        		SecondFloor3H.add(num+" - "+type);
        }
	}
	
	@Override
	public ArrayAdapter<CharSequence> getAdapter()
	{
		return current;
	}
	
	/**
	 * Handle the event caused when an item is selected.  This will transition the machine to
	 * the appropriate new state.
	 */
	@Override
	public void onClick(DialogInterface dialog, int whichButton)
	{
		tts.stop();
		if(accessibilityOn)
			tts.speak(this.getAdapter().getItem(whichButton).toString(), TextToSpeech.QUEUE_FLUSH, null);
		
		boolean end = false;
		if(whichButton == 0)
		{
			if(accessibilityOn)
			{
				tts.speak(this.getAdapter().getItem(0).toString(), TextToSpeech.QUEUE_FLUSH, null);
				for(int i=1;i<this.getAdapter().getCount();i++)
				{
					tts.speak(this.getAdapter().getItem(i).toString(), TextToSpeech.QUEUE_ADD, null);
				}
			}
		}
		else
		{
			//Handle state switching
			switch(state){
			case floor:
				if(whichButton == 1)
				{
					super.onClick(dialog, whichButton);
					end = true;
					break;
				}
				else if(whichButton == 2)
				{
					state = first;
					this.setAdapter(FirstFloor);
					current = FirstFloor;
					break;
				}
				else if(whichButton == 3)
				{
					state = second;
					this.setAdapter(SecondFloor);
					current = SecondFloor;
					break;
				}
				else if(whichButton == 4)
				{
					state = third;
					this.setAdapter(ThirdFloor);
					current = ThirdFloor;
					break;
				}
				if(whichButton == 5)
				{
					super.onClick(dialog, whichButton);
					end = true;
					break;
				}
				
			case first:
				if(whichButton == 1)
				{
					state = floor;
					this.setAdapter(Floor);
					current = Floor;
					break;
				}
				else if(whichButton == 3)
				{
					state = firstH;
					this.setAdapter(FirstFloorH);
					current = FirstFloorH;
					break;
				}
				else if(whichButton == 4)
				{
					state = first2H;
					this.setAdapter(FirstFloor2H);
					current = FirstFloor2H;
					break;
				}
				else if(whichButton == 5)
				{
					state = first3H;
					this.setAdapter(FirstFloor3H);
					current = FirstFloor3H;
					break;
				}
				else if(whichButton == 6)
				{
					state = firstR;
					this.setAdapter(FirstFloorR);
					current = FirstFloorR;
					break;
				}
				
			case firstH:
				if(whichButton == 1)
				{
					state = first;
					this.setAdapter(FirstFloor);
					current = FirstFloor;
					break;
				}
				else
				{
					super.onClick(dialog, whichButton);
					end = true;
					break;
				}
				
			case first2H:
				if(whichButton == 1)
				{
					state = first;
					this.setAdapter(FirstFloor);
					current = FirstFloor;
					break;
				}
				else
				{
					super.onClick(dialog, whichButton);
					end = true;
					break;
				}
				
			case first3H:
				if(whichButton == 1)
				{
					state = first;
					this.setAdapter(FirstFloor);
					current = FirstFloor;
					break;
				}
				else
				{
					super.onClick(dialog, whichButton);
					end = true;
					break;
				}
				
			case firstR:
				if(whichButton == 1)
				{
					state = first;
					this.setAdapter(FirstFloor);
					current = FirstFloor;
					break;
				}
				else
				{
					super.onClick(dialog, whichButton);
					end = true;
					break;
				}
				
			case second:
				if(whichButton == 1)
				{
					state = floor;
					this.setAdapter(Floor);
					current = Floor;
					break;
				}
				else if(whichButton == 2)
				{
					super.onClick(dialog, whichButton);
					end = true;
					break;
				}
				else if(whichButton == 3)
				{
					state = secondH;
					this.setAdapter(SecondFloorH);
					current = SecondFloorH;
					break;
				}
				else if(whichButton == 4)
				{
					state = second2H;
					this.setAdapter(SecondFloor2H);
					current = SecondFloor2H;
					break;
				}
				else if(whichButton == 5)
				{
					state = second3H;
					this.setAdapter(SecondFloor3H);
					current = SecondFloor3H;
					break;
				}
				else if(whichButton == 6)
				{
					state = secondR;
					this.setAdapter(SecondFloorR);
					current = SecondFloorR;
					break;
				}
				
			case secondH:
				if(whichButton == 1)
				{
					state = second;
					this.setAdapter(SecondFloor);
					current = SecondFloor;
					break;
				}
				else
				{
					super.onClick(dialog, whichButton);
					end = true;
					break;
				}
				
			case second2H:
				if(whichButton == 1)
				{
					state = second;
					this.setAdapter(SecondFloor);
					current = SecondFloor;
					break;
				}
				else
				{
					super.onClick(dialog, whichButton);
					end = true;
					break;
				}
				
			case second3H:
				if(whichButton == 1)
				{
					state = second;
					this.setAdapter(SecondFloor);
					current = SecondFloor;
					break;
				}
				else
				{
					super.onClick(dialog, whichButton);
					end = true;
					break;
				}
				
			case second4H:
				if(whichButton == 1)
				{
					state = second;
					this.setAdapter(SecondFloor);
					current = SecondFloor;
					break;
				}
				else
				{
					super.onClick(dialog, whichButton);
					end = true;
					break;
				}
				
			case secondR:
				if(whichButton == 1)
				{
					state = second;
					this.setAdapter(SecondFloor);
					current = SecondFloor;
					break;
				}
				else
				{
					super.onClick(dialog, whichButton);
					end = true;
					break;
				}
				
			case third:
				if(whichButton == 1)
				{
					state = floor;
					this.setAdapter(Floor);
					current = Floor;
					break;
				}
				else if(whichButton == 2)
				{
					super.onClick(dialog, whichButton);
					end = true;
					break;
				}
			}
			
			if(!end)
			{
				super.onClick(dialog, 0);
				this.performClick();
			}
		}
	}

}
