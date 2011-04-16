package com.iDocent;

public class Room {
	private int number;
	private String type;
	private Float x,y,z;
	
	public Room(int n, String t, Float x, Float y, Float z)
	{
		number=n;
		type=t;
		
		float[] normalLoc = LocationNormalizer.Normalize(x, Math.abs(y), z);
    	this.x = normalLoc[0];
    	this.y = normalLoc[1];
    	this.z = normalLoc[2];
	}
	
	public int getNumber()
	{
		return number;
	}
	
	public String getType()
	{
		return type;
	}
	
	public Float getX()
	{
		return x;
	}
	
	public Float getY()
	{
		return y;
	}
	
	public Float getZ()
	{
		return z;
	}
}
