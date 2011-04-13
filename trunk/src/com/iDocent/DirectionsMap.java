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
		
		this.nodes = nodes;
		RoomsByNumber = roomsByNumber;	
		
		Children = new Vector<GraphicsObject>();
		
		for(int i=0; i<nodes.size()-1; i++)
		{
			float x1,x2,y1,y2;
			x1 = RoomsByNumber.get(nodes.get(i)).getX();
			y1 = RoomsByNumber.get(nodes.get(i)).getY();
			x2 = RoomsByNumber.get(nodes.get(i+1)).getX();
			y2 = RoomsByNumber.get(nodes.get(i+1)).getY();
			
			if(y1 > 28.4)
				x1 = (132f+120f)/2.0f;
			else if(x1 < 120)
				y1 = (28.4f+16.4f)/2.0f;
			else if(y1 < 28.4 && x1 > 120)
			{
				x1 = (132f+120f)/2.0f;
				y1 = (28.4f+16.4f)/2.0f;
			}
			
			if(y2 > 28.4)
				x2 = (132f+120f)/2.0f;
			else if(x2 < 120)
				y2 = (28.4f+16.4f)/2.0f;
			else if(y2 < 28.4 && x2 > 120)
			{
				x2 = (132f+120f)/2.0f;
				y2 = (28.4f+16.4f)/2.0f;
			}
			
			Line l = new Line(x1, -y1, x2, -y2);
			l.setColor(255, 0, 0, 0);
			Children.add(l);
			Dot d = new Dot(x1, -y1, 0);
			d.setColor(0, 0, 255, 0);
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
				float x2 = v[0];
				float y2 = -v[1];
				float x1 = v[2];
				float y1 = -v[3];
				
				if(y1 > 28.4)
					x1 = (132f+120f)/2.0f;
				else if(x1 < 120)
					y1 = (28.4f+16.4f)/2.0f;
				else if(y1 < 28.4 && x1 > 120)
				{
					x1 = (132f+120f)/2.0f;
					y1 = (28.4f+16.4f)/2.0f;
				}
				
				if(y2 > 28.4)
					x2 = (132f+120f)/2.0f;
				else if(x2 < 120)
					y2 = (28.4f+16.4f)/2.0f;
				else if(y2 < 28.4 && x2 > 120)
				{
					x2 = (132f+120f)/2.0f;
					y2 = (28.4f+16.4f)/2.0f;
				}
				
				boolean up = (Math.abs(y2)-Math.abs(y1)) < 0;
				boolean right = (x2-x1) > 0;
				
				if((up && Math.abs(posY) < Math.abs(y1)) || (!up && Math.abs(posY) > Math.abs(y1)) && x2 > 120)
				{
					draw = false;
					if((up && Math.abs(y2) - Math.abs(posY) < 0) || (!up && Math.abs(y2)-Math.abs(posY) > 0 && posX > 120))
					{
						Line temp = new Line(x2, -y2, posX, posY);
						temp.setColor(255, 0, 0, 0);
						temp.Draw(gl);
					}
				}
				else if(((right && posX > x1) ||  (!right && posX < x1)) && y2 < 28)
				{
					draw = false;
					if((right && x2 - posX > 0) || (!right && x2 - posX < 0 && Math.abs(posY) < 28))
					{
						Line temp = new Line(x2, -y2, posX, posY);
						temp.setColor(255, 0, 0, 0);
						temp.Draw(gl);
					}
				}						
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
