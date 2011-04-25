/*
 * ECE 480 Spring 2011
 * Team 2 Design Project
 * Matt Gottshall
 * Jake D'Onofrio
 * Gordie Stein
 * Andrew Kling
 */
package com.iDocent;

import java.nio.*;
import javax.microedition.khronos.opengles.GL10;

/**A class to draw an object to show location of user
*This is currently an X
**/
public class Dot extends GraphicsObject{
	
	private float x1,y1,z1;

	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
	
	private float[] color = new float[4];
	
	private short[] indices = {0, 1, 2, 3};
	
	/**
	 * Set the color of the "Dot"
	 * @param r - red
	 * @param g - green
	 * @param b - blue
	 * @param a - alpha
	 */
	public void setColor(float r, float g, float b, float a)
	{
		color[0] = r;
		color[1] = g;
		color[2] = b;
		color[3] = a;
	}
	
	public Dot(float x1, float y1, float z1){
		UpdateLocation(x1, y1, z1);
		indexBuffer.position(0);
		
		color[0] = 0;
		color[1] = 0;
		color[2] = 0;
		color[3] = 0;
	}
	
	/**
	 * This draws the dot on the screen using OpenGL
	 */
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
		
		gl.glDrawElements(GL10.GL_LINES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		// Disable face culling.
		gl.glDisable(GL10.GL_CULL_FACE);
		
	}
	
	/**
	 * Update the stored location of the user
	 * @param x - the user's x location
	 * @param y - the user's y location
	 * @param z - the user's z location
	 */
	public void UpdateLocation(float x, float y, float z){
		x1 = x;
		y1 = y;
		z1 = z;
		
		float vertices[] = {x1-2, y1+2,0,
				x1+2, y1-2,0,
				x1+2, y1+2,0,
				x1-2, y1-2,0};


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

	public float getZ() {
		return z1;
	}
}
