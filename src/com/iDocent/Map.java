package com.iDocent;

import java.util.Vector;
import javax.microedition.khronos.opengles.GL10;

public class Map extends GraphicsObject{
	
	private Vector<GraphicsObject> Children;
	
	public Map(){
		Children = new Vector<GraphicsObject>();
	}

	@Override
	public void Draw(GL10 GL) {
		// TODO Auto-generated method stub
		for (GraphicsObject i:Children){
			i.Draw(GL);
		}
	}

}
