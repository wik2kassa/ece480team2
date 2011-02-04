package com.test;

public class WeightedScan {
	private int count;
	private String SSID;
	public int number;
	
	public WeightedScan(String ssid)
	{
		SSID = ssid;
	}
	
	public void SetCount(int c){count = c;}
	
	public String Name(){return SSID;}
	public int Num() {return count;}
}
