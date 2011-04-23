package com.iDocent;

//A simple object to hold both distance and direction
//and the end point of the path
public class DistanceAndDirection {
	public DistanceAndDirection(String string) {
		direction = string;
	}
	public DistanceAndDirection() {
		// TODO Auto-generated constructor stub
	}
	public DistanceAndDirection(float x1, float y1) {
		x=x1;y=y1;
	}
	public String direction;
	public int distance;
	public float x;
	public float y;
}
