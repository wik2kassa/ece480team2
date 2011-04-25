/*
 * ECE 480 Spring 2011
 * Team 2 Design Project
 * Matt Gottshall
 * Jake D'Onofrio
 * Gordie Stein
 * Andrew Kling
 */
package com.iDocent;


/**A simple object to hold both distance and direction
*and the end point of the path
**/
public class DistanceAndDirection {
	/**
	 * Sets the direction parameter.
	 * @param string - describes the direction "north" "south" "east" "west"
	 * Suggestion: change these to static ints to be referenced DistanceAndDirection.NORTH etc.
	 */
	public DistanceAndDirection(String string) {
		direction = string;
	}
	public DistanceAndDirection() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Set the end point of this stretch
	 * @param x1 - the end point's x location
	 * @param y1 - the end point's y location
	 */
	public DistanceAndDirection(float x1, float y1) {
		x=x1;y=y1;
	}
	public String direction;
	public int distance;
	public float x;
	public float y;
}
