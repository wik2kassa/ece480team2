/*
 * ECE 480 Spring 2011
 * Team 2 Design Project
 * Matt Gottshall
 * Jake D'Onofrio
 * Gordie Stein
 * Andrew Kling
 */
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

/**An object for drawing the room number texture.*/
public class TexturedSquare extends GraphicsObject {
	// The order we like to connect them.
	private short[] indices = { 0, 1, 3, 0, 3, 2 };
	
	private final int stair = 8000;
	private final int spartys = 6000;
	
	private int textureNum;
	private int roomNum;
	
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
		
		float sizer = 6.0f;
		vertices = new float[]{
			      left+x/sizer, bottom+y/sizer, 0.0f,  // 1, Bottom Left
			      right-x/sizer, bottom+y/sizer, 0.0f,  // 2, Bottom Right
			      left+x/sizer,  top-y/sizer, 0.0f,  // 0, Top Left
			      right-x/sizer, top-y/sizer, 0.0f,  // 3, Top Right
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
//		gl.glEnable(GL10.GL_ALPHA_BITS);
//		
//		gl.glEnable(GL10.GL_BLEND);    
//		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
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
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
	
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
		//Load the right texture
		setTextureNumber();
		//Get the texture from the Android resource directory
		InputStream is = context.getResources().openRawResource(textureNum);
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

	private void setTextureNumber() {
		//Set the texture reference for this room
		//based on the room number
		switch(roomNum){
		case 1200:
			textureNum = R.drawable.r1200;
			break;
		case 1202:
			textureNum = R.drawable.r1202;
			break;
		case 1203:
			textureNum = R.drawable.rwomens;
			break;
		case 1204:
			textureNum = R.drawable.r1204;
			break;
		case 1206:
			textureNum = R.drawable.r1206;
			break;
		case 1208:
			textureNum = R.drawable.r1208;
			break;
		case 1210:
			textureNum = R.drawable.r1210;
			break;
		case 1211:
			textureNum = R.drawable.r1211;
			break;
		case 1213:
			textureNum = R.drawable.r1213;
			break;
		case 1215:
			textureNum = R.drawable.r1215;
			break;
		case 1216:
			textureNum = R.drawable.r1216;
			break;
		case 1217:
			textureNum = R.drawable.r1217;
			break;
		case 1218:
			textureNum = R.drawable.r1218;
			break;
		case 1219:
			textureNum = R.drawable.r1219;
			break;
		case 1220:
			textureNum = R.drawable.r1220;
			break;
		case 1225:
			textureNum = R.drawable.r1225;
			break;
		case 1226:
			textureNum = R.drawable.r1226;
			break;
		case 1228:
			textureNum = R.drawable.r1228;
			break;
		case 1230:
			textureNum = R.drawable.r1230;
			break;
		case 1231:
			textureNum = R.drawable.r1231;
			break;
		case 1232:
			textureNum = R.drawable.r1232;
			break;
		case 1233:
			textureNum = R.drawable.r1233d;
			break;
		case 1234:
			textureNum = R.drawable.r1234;
			break;
		case 1235:
			textureNum = R.drawable.r1235;
			break;
		case 1237:
			textureNum = R.drawable.r1237;
			break;
		case 1300:
			textureNum = R.drawable.r1300;
			break;
		case 1303:
			textureNum = R.drawable.r1303;
			break;
		case 1307:
			textureNum = R.drawable.r1307;
			break;
		case 1312:
			textureNum = R.drawable.r1312;
			break;
		case 1314:
			textureNum = R.drawable.r1314;
			break;
		case 1318:
			textureNum = R.drawable.r1318;
			break;
		case 1320:
			textureNum = R.drawable.r1320;
			break;
		case 1325:
			textureNum = R.drawable.r1325;
			break;
		case 1328:
			textureNum = R.drawable.r1328d;
			break;
		case 1335:
			textureNum = R.drawable.r1335;
			break;
		case 1338:
			textureNum = R.drawable.r1338;
			break;
		case 1340:
			textureNum = R.drawable.r1340;
			break;
		case 1345:
			textureNum = R.drawable.r1345;
			break;
		case 1405:
			textureNum = R.drawable.r1405;
			break;
		case 2200:
			textureNum = R.drawable.r2200;
			break;
		case 2201:
			textureNum = R.drawable.rmens;
			break;
		case 2203:
			textureNum = R.drawable.r2203;
			break;
		case 2205:
			textureNum = R.drawable.r2205;
			break;
		case 2210:
			textureNum = R.drawable.r2210;
			break;
		case 2211:
			textureNum = R.drawable.r2211;
			break;
		case 2212:
			textureNum = R.drawable.r2212;
			break;
		case 2214:
			textureNum = R.drawable.r2214;
			break;
		case 2215:
			textureNum = R.drawable.r2215;
			break;
		case 2216:
			textureNum = R.drawable.r2216;
			break;
		case 2218:
			textureNum = R.drawable.r2218;
			break;
		case 2219:
			textureNum = R.drawable.r2219;
			break;
		case 2221:
			textureNum = R.drawable.r2221;
			break;
		case 2231:
			textureNum = R.drawable.r2231;
			break;
		case 2234:
			textureNum = R.drawable.r2234;
			break;
		case 2243:
			textureNum = R.drawable.r2243;
			break;
		case 2245:
			textureNum = R.drawable.r2245;
			break;
		case 2250:
			textureNum = R.drawable.r2250d;
			break;
		case 2251:
			textureNum = R.drawable.r2251;
			break;
		case 2308:
			textureNum = R.drawable.r2308;
			break;
		case 2314:
			textureNum = R.drawable.r2314;
			break;
		case 2400:
			textureNum = R.drawable.r2400;
			break;
		case stair:
			textureNum = R.drawable.rstairs;
			break;
		case spartys:
			textureNum = R.drawable.rspartys;
			break;
		default:
			textureNum = -1;
			break;
		}
	}

	public void setRoomNumber(int i) {
		roomNum = i;
		setTextureNumber();
	}
	
	public int getTextureNum(){
		return textureNum;
	}
}
