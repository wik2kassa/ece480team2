package com.iDocent;

public class Room {
	private int number;
	private String type;
	private Float x,y,z;
	
	public Room(int n, String t, Float x, Float y, Float z)
	{
		number=n;
		type=t;
		this.x=x;
		this.y=y;
		this.z=z;
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
