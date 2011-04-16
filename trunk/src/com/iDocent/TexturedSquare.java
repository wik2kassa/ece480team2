package com.iDocent;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class TexturedSquare extends GraphicsObject {
	// The order we like to connect them.
	private short[] indices = { 0, 1, 3, 0, 3, 2 };
	
    private float texture[];
    private float landscape[]= {    		
    		//Mapping coordinates for the vertices
    		0.0f, 1.0f,
    		1.0f, 1.0f,
    		0.0f, 0.0f,
    		1.0f, 0.0f, 
    };
    private float portrait[]= {    		
    		//Mapping coordinates for the vertices
    		0.0f, 0.0f,
    		0.0f, 1.0f,
    		1.0f, 0.0f,
    		1.0f, 1.0f, 
    };

	private int[] textures = new int[1];
	private float[] vertices;
	
	private FloatBuffer textureBuffer;
	
	// Our vertex buffer.
	private FloatBuffer vertexBuffer;

	// Our index buffer.
	private ShortBuffer indexBuffer;
	
	public TexturedSquare(float left, float right, float top, float bottom) {
		float x = Math.abs(right-left);
		float y = Math.abs(top-bottom);
		if(x > y)
			texture = landscape;
		else
			texture = portrait;
		vertices = new float[]{
			      left+x/4.0f, bottom+y/4.0f, 0.0f,  // 1, Bottom Left
			      right-x/4.0f, bottom+y/4.0f, 0.0f,  // 2, Bottom Right
			      left+x/4.0f,  top-y/4.0f, 0.0f,  // 0, Top Left
			      right-x/4.0f, top-y/4.0f, 0.0f,  // 3, Top Right
			};
		
		// a float is 4 bytes, therefore we multiply the number if 
		// vertices with 4.
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		//
		vbb = ByteBuffer.allocateDirect(texture.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		textureBuffer = vbb.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
		
		// short is 2 bytes, therefore we multiply the number if 
		// vertices with 2.
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}

	@Override
	public void Draw(GL10 gl) {
		gl.glColor4f(1, 1, 1, 1);
		//Bind our only previously generated texture in this case
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		// Counter-clockwise winding.
		gl.glFrontFace(GL10.GL_CCW);
		// Enable face culling.
		gl.glEnable(GL10.GL_CULL_FACE);
		// What faces to remove with the face culling.
		gl.glCullFace(GL10.GL_BACK);
		
		// Enabled the vertices buffer for writing and to be used during 
		// rendering.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, 
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// Disable face culling.
		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glDisable(GL10.GL_ALPHA_BITS);
		gl.glDisable(GL10.GL_BLEND); 
	}
	
	public void loadGLTexture(GL10 gl, Context context)
	{
		//Get the texture from the Android resource directory
		InputStream is = context.getResources().openRawResource(R.drawable.r1200);
		Bitmap bitmap = null;
		try {
			//BitmapFactory is an Android graphics utility for images
			bitmap = BitmapFactory.decodeStream(is);

		} finally {
			//Always clear and close
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}
		//Generate one texture pointer...
		gl.glGenTextures(1, textures, 0);
		//...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		//Create Nearest Filtered Texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		//Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		
		//Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		//Clean up
		bitmap.recycle();
	}
}
