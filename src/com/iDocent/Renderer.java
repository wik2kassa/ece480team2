/*
 * ECE 480 Spring 2011
 * Team 2 Design Project
 * Matt Gottshall
 * Jake D'Onofrio
 * Gordie Stein
 * Andrew Kling
 */
package com.iDocent;

import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.speech.tts.TextToSpeech;

/**The main OpenGL renderer
Handles drawing the maps
 * 
 *
 */
class Renderer implements GLSurfaceView.Renderer {
	private Map map;
	private DirectionsMap dMap;
	
	private float posX = 75;
	private float posY = 100;
	private float posZ = 0;
	private float xOffset = 0;
	private float yOffset = 0;
	private float zoom = -200;
	
	boolean routeSet = false;
	iDocent miD;
	
	public Renderer(iDocent iDocent){
		map = new Map(iDocent);
		dMap = new DirectionsMap();
		miD = iDocent;
	}
	
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    	gl.glEnable(GL10.GL_TEXTURE_2D);
    	map.loadTexture(gl, miD);
    	// Set the background color to black ( rgba ).
		gl.glClearColor(255.0f, 255.0f, 255.0f, 0.5f);
		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH);
		// Depth buffer setup.
		gl.glClearDepthf(1.0f);
		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);
		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);
		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

    public void onSurfaceChanged(GL10 gl, int w, int h) {
    	// Sets the current view port to the new size.
		gl.glViewport(0, 0, w, h);
		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// Reset the projection matrix
		gl.glLoadIdentity();
		// Calculate the aspect ratio of the window
		GLU.gluPerspective(gl, 45.0f, (float) w / (float) h, 0.1f,
				400.0f);
		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// Reset the modelview matrix
		gl.glLoadIdentity();
    }

    public void onDrawFrame(GL10 gl) {
    	// Clears the screen and depth buffer.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// Replace the current matrix with the identity matrix
		gl.glLoadIdentity();
		//draw frame at right zoom and temporary translation
		gl.glTranslatef(-(posX+xOffset), -(posY+yOffset), zoom); 
		map.Draw(gl);
        
        if(routeSet)
        	dMap.Draw(gl);
		
		// Replace the current matrix with the identity matrix
		gl.glLoadIdentity(); // OpenGL docs        
    }

	/**
	 * Update the stored location of the user
	 * @param x - the user's x location
	 * @param y - the user's y location
	 * @param z - the user's z location
	 */
	public void UpdateLocation(float x, float y, float z) {
		posX = x;
		posY = y;
		posZ = z;
		map.UpdateLoction(posX, posY, posZ);
		if(routeSet)
			dMap.UpdateLocation(posX, posY, posZ);
	}

	public void zoomOut() {
		zoom -= 50;
		if(zoom <= -350)
			zoom = -350;
	}

	public void zoomIn() {
		zoom += 50;
		if(zoom >= -50)
			zoom = -50;
	}

	public void MoveCamera(float x, float y) {
		xOffset = -x*zoom/-350f;
		yOffset = y*zoom/-350f;
	}
	
	public void CenterCamera()
	{
		xOffset = yOffset = 0;
	}

	/**
	 * Pass the route to the destination to the directions map
	 * @param nodes - the list of rooms along the path from destination to ther user
	 * @param roomsByNumber - a HashMap mapping Room objects to their room number
	 * @param destination - the destination room number
	 * see also: {@link Room}
	 * 			 {@link DirectionsMap}
	 */
	public void setRoute(ArrayList<Integer> nodes,
			HashMap<Integer, Room> roomsByNumber, String destination) {
		routeSet = false;
		map.setDestination(Integer.parseInt(destination));
		if(nodes != null && roomsByNumber != null)
			dMap.setRoute(nodes, roomsByNumber);
		dMap.UpdateLocation(posX, posY, posZ);
		routeSet = true;
	}

	public void setTTS(TextToSpeech tts) {
		dMap.setTTS(tts);
	}

	public void clearRoute() {
		dMap.clearRoute();
		map.clearDestination();
	}
}
