package com.iDocent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

public class DirectionsMap extends GraphicsObject{
	HashMap<Integer, Room> RoomsByNumber;
	ArrayList<Integer> nodes;
	Vector<GraphicsObject> Children = new Vector<GraphicsObject>();
	
	float posX, posY, posZ;
	
	public void setRoute(ArrayList<Integer> nodes,
			HashMap<Integer, Room> roomsByNumber) {
		
		this.nodes = nodes;//destination at front!
		RoomsByNumber = roomsByNumber;	
		
		Children = new Vector<GraphicsObject>();
		
		for(int i=0; i<nodes.size()-1; i++)
		{
			float x1,x2,y1,y2;
			x1 = RoomsByNumber.get(nodes.get(i)).getX();
			y1 = RoomsByNumber.get(nodes.get(i)).getY();
			x2 = RoomsByNumber.get(nodes.get(i+1)).getX();
			y2 = RoomsByNumber.get(nodes.get(i+1)).getY();
			
			float f[] = LocationNormalizer.Normalize(x1, y1, RoomsByNumber.get(nodes.get(i)).getZ());
			float f2[] = LocationNormalizer.Normalize(x2, y2, RoomsByNumber.get(nodes.get(i+1)).getZ());
			
			Line l = new Line(f[0], f[1], f2[0], f2[1]);
			l.setColor(1, 0, 0, 0);
			l.setZ(f[2]);
			Children.add(l);
			Dot d = new Dot(f[0], f[1], f[2]);
			d.setColor(0, 0, 1, 0);
			Children.add(d);
		}
	}
	
	@Override
	public void Draw(GL10 gl) {
		for(GraphicsObject g : Children)
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
					
					boolean up = Math.abs(y1) < Math.abs(y2);
					boolean left = x1 < x2;
					//your destination is always closer to x1,y1
					
					boolean inVertHall = x2 > 120 || x2 < -108;
					boolean inHoriHall = y2 < 28;
					
					boolean userInVertHall = posX > 120 || posX < -108;
					boolean userInHoriHall = posY < 28;
					
					if((up && Math.abs(posY) < Math.abs(y2)) || (!up && Math.abs(posY) > Math.abs(y2)) && inVertHall)
					{
						draw = false;
						if(up && Math.abs(y1) < Math.abs(posY) && userInVertHall)
						{
							Line temp = new Line(x1, y1, posX, posY);
							temp.setColor(255, 0, 0, 0);
							temp.Draw(gl);
						}
						else if(!up && Math.abs(y1) > Math.abs(posY) && userInVertHall)
						{
							Line temp = new Line(x1, y1, posX, posY);
							temp.setColor(255, 0, 0, 0);
							temp.Draw(gl);
						}
					}
					else if(((left && posX < x1) ||  (!left && posX > x1)) && inHoriHall)
					{
						draw = false;
						if(left && x1 < posX && userInHoriHall)
						{
							Line temp = new Line(x1, y1, posX, posY);
							temp.setColor(255, 0, 0, 0);
							temp.Draw(gl);
						}
						else if(!left && x1 > posX && userInHoriHall)
						{
							Line temp = new Line(x1, y1, posX, posY);
							temp.setColor(255, 0, 0, 0);
							temp.Draw(gl);
						}
					}	
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
	}

	public void UpdateLocation(float posX, float posY, float posZ) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}

}
