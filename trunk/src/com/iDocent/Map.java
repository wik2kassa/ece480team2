package com.iDocent;

import java.util.Vector;
import javax.microedition.khronos.opengles.GL10;

public class Map extends GraphicsObject{
	
	private Vector<GraphicsObject> Children;
	
	public Map(){
		Children = new Vector<GraphicsObject>();
		
		Children.add(new Line(0,16.4f, 0,28.4f));
		Children.add(new Line(0,16.4f, 12,16.4f));
		Children.add(new Line(0,28.4f, 12,28.4f));
		Children.add(new Line(12,0, 12,16.4f));
		Children.add(new Line(24.5f,0, 24.5f,16.4f));
		Children.add(new Line(24.5f,16.4f, 143.5f,16.4f));
		Children.add(new Line(24.5f,28.4f, 120,28.4f));
		Children.add(new Line(143.5f,16.4f, 143.5f,28.4f));
		Children.add(new Line(132,28.4f, 143.5f,28.4f));
		Children.add(new Line(120,28.4f, 120,153.4f));
		Children.add(new Line(132,28.4f, 132,153.4f));
	}

	@Override
	public void Draw(GL10 GL) {
		// TODO Auto-generated method stub
		for (GraphicsObject i:Children){
			i.Draw(GL);
		}
	}

}
