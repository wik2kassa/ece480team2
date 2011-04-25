/*
 * ECE 480 Spring 2011
 * Team 2 Design Project
 * Matt Gottshall
 * Jake D'Onofrio
 * Gordie Stein
 * Andrew Kling
 */
package com.iDocent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import android.speech.tts.TextToSpeech;

/**
 * This object stores and draws the path from the user to their desired destination
 *
 */
//A map to draw the navigation path
public class DirectionsMap extends GraphicsObject{
	//HashMap to map a room number to the corresponding room object
	HashMap<Integer, Room> RoomsByNumber;
	
	//the nodes in the path to the destination
	ArrayList<Integer> nodes;
	
	//Vectors to hold the lines to draw
	Vector<GraphicsObject> Children = new Vector<GraphicsObject>();
	Vector<Line> FirstFloorLines = new Vector<Line>();
	Vector<Line> SecondFloorLines = new Vector<Line>();
	Vector<Line> ThirdFloorLines = new Vector<Line>();
	
	//current location
	float posX, posY, posZ;
	
	private TextToSpeech tts;
	
	private int speakPos=0;
	private boolean speak = false;
	
	private LinkedList<DistanceAndDirection> path = new LinkedList<DistanceAndDirection>();
	private LinkedList<Boolean> drawLine = new LinkedList<Boolean>();
	
	/**
	 * Set the route from the user to the destination
	 * @param nodes - the rooms in the path from the destination back to the user
	 * @param roomsByNumber - a HashMap that maps room objects to their room number
	 * see also: {@link Room}
	 */
	public void setRoute(ArrayList<Integer> nodes,
			HashMap<Integer, Room> roomsByNumber) {
		/*
		 * init fields
		 */
		FirstFloorLines = new Vector<Line>();
		SecondFloorLines = new Vector<Line>();
		ThirdFloorLines = new Vector<Line>();
		
		path = new LinkedList<DistanceAndDirection>();
		drawLine = new LinkedList<Boolean>();
		speakPos = 0;
		speak = true;
		this.nodes = nodes;//destination at front!
		RoomsByNumber = roomsByNumber;	
		
		Children = new Vector<GraphicsObject>();
		
		String direction = "";
		int distance = 0;
		float x1=0,x2=0,y1=0,y2=0;
		//int z=0;
		
		String prevType = "";
		
		/*
		 * End init
		 */
		
		//iterate through the list of nodes on the path and add lines
		//between them
		for(int i=0; i<nodes.size()-1; i++)
		{	
			Room room1 = RoomsByNumber.get(nodes.get(i));
			Room room2 = RoomsByNumber.get(nodes.get(i+1));
			x1 = room1.getX();
			y1 = room1.getY();
			x2 = room2.getX();
			y2 = room2.getY();
			
			float f[] = LocationNormalizer.Normalize(x1, y1, room1.getZ());
			float f2[] = LocationNormalizer.Normalize(x2, y2, room2.getZ());
			x1 = f[0];
			y1 = f[1];
			x2 = f2[0];
			y2 = f2[1];
			
			if(i==0)
			{
				Dot d = new Dot(x1, y1, f[2]);
				d.setColor(1, 0, 0, 0);
				Children.add(d);
			}
			
			if(path.isEmpty())
			{
				path.add(new DistanceAndDirection());
			}
			
			//determine the direction of the path
			if(x1>x2 && Math.abs(y1-y2) < 0.01)
			{
				direction = "east";
			}
			else if(x1<x2 && Math.abs(y1-y2) < 0.01)
			{
				direction = "west";
			}
			else if(Math.abs(x1-x2) < 0.01 && Math.abs(y1)>Math.abs(y2))
			{
				direction = "south";
			}
			else if(Math.abs(x1-x2) < 0.01 && Math.abs(y1)<Math.abs(y2))
			{
				direction = "north";
			}
			
			//An intersection means a new branch of the path
			//store the distance from last intersection to this one
			if(RoomsByNumber.get(nodes.get(i)).getType().toLowerCase().equals("intersection"))
			{
				if(direction.equals("east"))
				{
					distance += x1-x2;
				}
				else if(direction.equals("west"))
				{
					distance += x2-x1;
				}
				else if(direction.equals("north"))
				{
					distance += y2-y1;
				}
				else if(direction.equals("south"))
				{
					distance += y1-y2;
				}	
				
				path.get(0).distance = distance;
				path.get(0).x = x1;
				path.get(0).y = y1;
				distance = 0;
				path.addFirst(new DistanceAndDirection(direction));
			}
			else
			{
				path.get(0).direction = direction;
			}
			
			//Keep track of the distance of this portion of the path
			if(direction.equals("east"))
			{
				distance += x1-x2;
			}
			else if(direction.equals("west"))
			{
				distance += x2-x1;
			}
			else if(direction.equals("north"))
			{
				distance += y2-y1;
			}
			else if(direction.equals("south"))
			{
				distance += y1-y2;
			}	
			
			//If it is a stair case connect it to itself so it draws
			//draw a blue X to indicate the path traverses a staircase
			if((room1.getType().toLowerCase().contains("stair") && room2.getType().toLowerCase().contains("stair")) || 
			   (room1.getType().toLowerCase().contains("stair") && prevType.toLowerCase().contains("stair")))
			{
				Line line = new Line(f[0], f[1], f[0], f[1]);
				line.setColor(1, 0, 0, 0);
				line.setZ(f[2]);
				if(f[2] == 0)
					FirstFloorLines.add(line);
				Dot d = new Dot(f[0], f[1], f[2]);
				d.setColor(0, 0, 1, 0);
				Children.add(d);
			}
			
			//Add the line to the correct floor list
			prevType = room1.getType();
			
			Line l = new Line(f[0], f[1], f2[0], f2[1]);
			l.setColor(1, 0, 0, 0);
			l.setZ(f[2]);
//			z=(int) f[2];
			if(f[2] == LocationNormalizer.ffz)
				FirstFloorLines.add(l);
			else if(f[2] == LocationNormalizer.sfz)
				SecondFloorLines.add(l);
			else if(f[2] == LocationNormalizer.tfz)
				SecondFloorLines.add(l);
			drawLine.add(new Boolean(true));
		}
		if(!path.isEmpty())
		{
			path.get(0).distance = distance;
			path.get(0).x = x1;
			path.get(0).y = y1;
		}
	}
	
	/**
	 * Draws the path.
	 */
	@Override
	public void Draw(GL10 gl) {	
		//Draw any non path objects like dots
		if(posZ == LocationNormalizer.ffz)
			DrawLines(FirstFloorLines, gl);
		if(posZ == LocationNormalizer.sfz)
			DrawLines(SecondFloorLines, gl);
		if(posZ == LocationNormalizer.tfz)
			DrawLines(ThirdFloorLines, gl);
		
		for(GraphicsObject g : Children)
		{
			boolean draw = true;
			if(g instanceof Dot)
			{
				if(((Dot)g).getZ() != posZ)
					draw = false;
			}
			
			if(draw)
				g.Draw(gl);
		}
	}

	private void DrawLines(Vector<Line> lines, GL10 gl) {
		//Draw the lines in the forward path
		//draw none behind the user
		float[] nl = LocationNormalizer.Normalize(posX, posY, posZ);
		this.posX = nl[0];
		this.posY = nl[1];
		this.posZ = nl[2];	
		
		int notDrawn = 0;
		
		int pos = 0;
		boolean foundFirstDrawn = false;
		for(int i=lines.size()-1;i>=0;i--)
		{
			Line l = lines.get(i);
			boolean draw = true;

			float[] v = ((Line) l).getVertices();
			//float z = ((Line) l).getZ();
			
			float[] f = LocationNormalizer.Normalize(v[0], v[1], 0.0f);
			float[] f2 = LocationNormalizer.Normalize(v[2], v[3], 0.0f);
			float x1 = f[0];
			float y1 = f[1];
			float x2 = f2[0];
			float y2 = f2[1];
			
			//All the logic to determine if a line should not be drawn
			if(x1==x2 && y1==y2)
			{
				draw = false;
				notDrawn++;
				
				if(!foundFirstDrawn && i==0)
				{
					foundFirstDrawn = true;
					Line temp = new Line(x1, y1, posX, posY);
					temp.setColor(1, 0, 0, 1);
					temp.Draw(gl);
				}
			}
			else
			{
				DistanceAndDirection d = null;
				if(speakPos < path.size())
					d = path.get(speakPos);
				
				if(d!=null && (x2 == d.x && Math.abs(y2) == Math.abs(d.y)))
					speak = true;
				
				if(!foundFirstDrawn)
				{
					boolean up = Math.abs(y1) < Math.abs(y2);
					boolean down = Math.abs(y1) > Math.abs(y2);
					boolean left = x1 < x2;
					boolean right = x1 > x2;
					//your destination is always closer to x1,y1
					
//					boolean inVertHall = x2 > 120 || x2 < -108;
//					boolean inHoriHall = y2 < 28;
//					
//					boolean userInVertHall = posX > 120 || posX < -108;
//					boolean userInHoriHall = Math.abs(posY) < 28;
					
					if(up && Math.abs(posY) < Math.abs(y2))
					{
						draw = false;
					}
					else if(down && Math.abs(posY) > Math.abs(y2))
					{
						draw = false;
					}
					else if(left && posX < x2)
					{
						draw = false;
					}
					else if(right && posX > x2)
					{
						draw = false;
					}
				}
				
				if(!draw)
					notDrawn++;
				
				if(!foundFirstDrawn && i == 0 && notDrawn >= lines.size())
				{
					Line temp = new Line(x1, y1, posX, posY);
					temp.setColor(1, 0, 0, 1);
					temp.Draw(gl);
				}
				
				if(draw)
				{		
					if(!foundFirstDrawn)
					{
						Line temp = new Line(x2, y2, posX, posY);
						temp.setColor(1, 0, 0, 1);
						temp.Draw(gl);
					}
					
					foundFirstDrawn = true;
					if(speak)
					{
						if(d!=null)
							tts.speak("Head "+d.direction+" "+Math.abs(d.distance)+" feet", TextToSpeech.QUEUE_ADD, null);
						speakPos++;
						speak = false;
					}
				}
			}	
			
			if(draw)
				l.Draw(gl);
			pos++;
		}
	}

	/**
	 * Update the stored location of the user
	 * @param posX - the user's x location
	 * @param posY - the user's y location
	 * @param posZ - the user's z location
	 */
	public void UpdateLocation(float posX, float posY, float posZ) {
		float[] f = LocationNormalizer.Normalize(posX, posY, posZ);
		this.posX = f[0];
		this.posY = f[1];
		this.posZ = f[2];
	}

	/**
	 * Sets the text-to-speech object to be used.
	 * @param tts - the TextToSpeech object
	 */
	public void setTTS(TextToSpeech tts) {
		this.tts = tts;
	}

	/**
	 * Clears the route so no lines are drawn.
	 */
	public void clearRoute() {
		FirstFloorLines = new Vector<Line>();
		SecondFloorLines = new Vector<Line>();
		ThirdFloorLines = new Vector<Line>();
		path = new LinkedList<DistanceAndDirection>();
	}

}
