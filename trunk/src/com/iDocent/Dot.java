package com.iDocent;

import java.nio.*;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class Dot extends GraphicsObject{
	
	private float x1,y1;

	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
	
	private float[] color = new float[4];
	
	private short[] indices = {0, 1, 2, 3};
	
	public void setColor(float r, float g, float b, float a)
	{
		color[0] = r;
		color[1] = g;
		color[2] = b;
		color[3] = a;
	}
	
	public Dot(float x1, float y1){
		UpdateLocation(x1, y1);
		indexBuffer.position(0);
		
		color[0] = 0;
		color[1] = 0;
		color[2] = 0;
		color[3] = 0;
	}
	@Override
	public void Draw(GL10 gl) {
		gl.glColor4f(color[0], color[1], color[2], color[3]);
		// Counter-clockwise winding.
		gl.glFrontFace(GL10.GL_CCW);
		// Enable face culling.
		gl.glEnable(GL10.GL_CULL_FACE);
		// What faces to remove with the face culling.
		gl.glCullFace(GL10.GL_BACK);
		
		// Enabled the vertices buffer for writing and to be used during 
		// rendering.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		
		gl.glDrawElements(GL10.GL_TRIANGLE_FAN, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		// Disable face culling.
		gl.glDisable(GL10.GL_CULL_FACE);
		
	}
	
	public void UpdateLocation(float x, float y){
		x1 = x;
		y1 = y;
		
		float vertices[] = {x1-2, y1+2,0,
				x1+2, y1-2,0,
				x1+2, y1+2,0,
				x1-2, y1-2,0};
		//ArrayList<Float> v = new ArrayList<Float>();
		//double angle = 0;
		//Push Back Center		
		//v.add(x);
		//v.add(y);
		
		//Outside vertices
		//float x1 = 2;
		//float y2 = 2;
		//for(int i = 0; i < 360.0; i++){
		//	x1 = (float) (x * Math.cos(i * (3.14159/180.)) - (y * Math.sin(i * (3.14159/180.))));
		//	y1 = (float) (x * Math.sin(i * (3.14159/180.)) + (y * Math.cos(i * (3.14159/180.))));
		//	v.add(x1);
		//	v.add(y1);
		//}
		
		//float[] vertices = new float [v.size()];
		//for(int i = 0; i< v.size(); i++){
		//	vertices[i] = v.get(i);
		//}

		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		// 	short is 2 bytes, therefore we multiply the number if 
		// 	vertices with 2.
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}
	
	

}
