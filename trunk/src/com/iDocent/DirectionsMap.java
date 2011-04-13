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
			Dot d = new Dot(x1, -y1);
			d.setColor(0, 0, 255, 0);
			Children.add(d);
		}
	}
	
	@Override
	public void Draw(GL10 gl) {
		for(GraphicsObject g : Children)
			g.Draw(gl);
	}

}
