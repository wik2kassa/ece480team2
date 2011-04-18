package com.iDocent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import android.speech.tts.TextToSpeech;

public class DirectionsMap extends GraphicsObject{
	HashMap<Integer, Room> RoomsByNumber;
	ArrayList<Integer> nodes;
	Vector<GraphicsObject> Children = new Vector<GraphicsObject>();
	
	float posX, posY, posZ;
	private TextToSpeech tts;
	
	private int speakPos=0;
	private boolean speak = false;
	
	private LinkedList<Boolean> drawLine = new LinkedList<Boolean>();
	private LinkedList<DistanceAndDirection> path = new LinkedList<DistanceAndDirection>();
	private LinkedList<DistanceAndDirection> endPoints = new LinkedList<DistanceAndDirection>();
	
	public void setRoute(ArrayList<Integer> nodes,
			HashMap<Integer, Room> roomsByNumber) {
		drawLine = new LinkedList<Boolean>();
		path = new LinkedList<DistanceAndDirection>();
		speakPos = 0;
		speak = true;
		this.nodes = nodes;//destination at front!
		RoomsByNumber = roomsByNumber;	
		
		Children = new Vector<GraphicsObject>();
		
		String direction = "";
		int distance = 0;
		float x1=0,x2=0,y1=0,y2=0;
		
		for(int i=0; i<nodes.size()-1; i++)
		{
			x1 = RoomsByNumber.get(nodes.get(i)).getX();
			y1 = RoomsByNumber.get(nodes.get(i)).getY();
			x2 = RoomsByNumber.get(nodes.get(i+1)).getX();
			y2 = RoomsByNumber.get(nodes.get(i+1)).getY();
			
			float f[] = LocationNormalizer.Normalize(x1, y1, RoomsByNumber.get(nodes.get(i)).getZ());
			float f2[] = LocationNormalizer.Normalize(x2, y2, RoomsByNumber.get(nodes.get(i+1)).getZ());
			x1 = f[0];
			y1 = f[1];
			x2 = f2[0];
			y2 = f2[1];
			
			if(path.isEmpty())
			{
				path.add(new DistanceAndDirection());
			}
			
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
				endPoints.add(new DistanceAndDirection(x1, y1));
				distance = 0;
				path.addFirst(new DistanceAndDirection(direction));
			}
			else
			{
				path.get(0).direction = direction;
			}
			
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
			
			Line l = new Line(f[0], f[1], f2[0], f2[1]);
			l.setColor(1, 0, 0, 0);
			l.setZ(f[2]);
			Children.add(l);
//			Dot d = new Dot(f[0], f[1], f[2]);
//			d.setColor(0, 0, 1, 0);
//			Children.add(d);

			drawLine.add(new Boolean(true));
		}
		path.get(0).distance = distance;
		path.get(0).x = x1;
		path.get(0).y = y1;
		endPoints.add(new DistanceAndDirection(x1, y1));
	}
	
	@Override
	public void Draw(GL10 gl) {
		int pos = 0;
		boolean first = true;
		for(GraphicsObject g : Children)
		{
			if(drawLine.get(pos))
			{
				boolean draw = true;
				if(g instanceof Line)
				{
					float[] v = ((Line) g).getVertices();
					float z = ((Line) g).getZ();
					if(z != posZ)
						draw = false;
					else
					{
						float[] f = LocationNormalizer.Normalize(v[0], v[1], 0.0f);
						float[] f2 = LocationNormalizer.Normalize(v[2], v[3], 0.0f);
						float x1 = f[0];
						float y1 = f[1];
						float x2 = f2[0];
						float y2 = f2[1];
						DistanceAndDirection d = null;
						if(speakPos < path.size())
							d = path.get(speakPos);
						
						if(d!=null && (x2 == d.x && Math.abs(y2) == Math.abs(d.y)))
							speak = true;
						
						boolean up = Math.abs(y1) < y2;
						boolean left = x1 < x2;
						//your destination is always closer to x1,y1
						
						boolean inVertHall = x2 > 120 || x2 < -108;
						boolean inHoriHall = y2 < 28;
						
						boolean userInVertHall = posX > 120 || posX < -108;
						boolean userInHoriHall = posY < 28;
						
//						if(first)
//						{
//							if(up && posY < Math.abs(y2))
//							{
//								drawLine.set(pos, false);
//								draw = false;
//							}
//							else if(!up && posY > Math.abs(y2))
//							{
//								drawLine.set(pos, false);
//								draw = false;
//							}
//							else if(left && posX < x2)
//							{
//								drawLine.set(pos, false);
//								draw = false;
//							}
//							else if(!left && posX > x2)
//							{
//								drawLine.set(pos, false);
//								draw = false;
//							}
//						}
						
//						if((up && Math.abs(posY) < Math.abs(y2)) || (!up && Math.abs(posY) > Math.abs(y2)) && inVertHall && userInVertHall)
//						{
//							draw = false;
//							if(up && Math.abs(y1) < Math.abs(posY))
//							{
//								Line temp = new Line(x1, y1, posX, posY);
//								temp.setColor(255, 0, 0, 0);
//								temp.Draw(gl);
//							}
//							else if(!up && Math.abs(y1) > Math.abs(posY))
//							{
//								Line temp = new Line(x1, y1, posX, posY);
//								temp.setColor(255, 0, 0, 0);
//								temp.Draw(gl);
//							}
//						}
//						else if(((left && posX < x1) ||  (!left && posX > x1)) && inHoriHall && userInHoriHall)
//						{
//							draw = false;
//							if(left && x1 < posX)
//							{
//								Line temp = new Line(x1, y1, posX, posY);
//								temp.setColor(255, 0, 0, 0);
//								temp.Draw(gl);
//							}
//							else if(!left && x1 > posX)
//							{
//								Line temp = new Line(x1, y1, posX, posY);
//								temp.setColor(255, 0, 0, 0);
//								temp.Draw(gl);
//							}
//						}	
						
						if(draw)
						{							
							if(speak)
							{
								if(d!=null)
									tts.speak("Head "+d.direction+" "+Math.abs(d.distance)+" feet", TextToSpeech.QUEUE_ADD, null);
								speakPos++;
								speak = false;
							}
						}
						else if(!draw)
						{
							drawLine.set(pos, false);
							if(pos > speakPos)
								speak = false;
						}
						
						first = false;
					}
				}	
				else if(g instanceof Dot)
				{
					if(((Dot)g).getZ() != posZ)
						draw = false;
				}
				
				if(draw)
					g.Draw(gl);
			}
			pos++;
		}
	}

	public void UpdateLocation(float posX, float posY, float posZ) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}

	public void setTTS(TextToSpeech tts) {
		this.tts = tts;
	}

}
