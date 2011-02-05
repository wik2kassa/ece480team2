package com.iDocent;

import java.util.LinkedList;
import java.util.List;

//An object to hold the important information of a access point scan
public class WeightedScan {
	private int level;
	private String SSID;
	int posX, posY, posZ;
	
	private static int x=0; 
	private static int y=1; 
	private static int z=2; 
	
	//DEMO VARIABLE
	public int number;
	
	public WeightedScan(String ssid, List<Integer> pos)
	{
		SSID = ssid;
		posX = pos.get(x);
		posY = pos.get(y);
		posZ = pos.get(z);
	}

	public void SetLevel(int l){level = l;}
	
		
	public List<Integer> GetPos(){
		List<Integer> pos = new LinkedList<Integer>();
		pos.add(posX);
		pos.add(posY);
		pos.add(posZ);
		return pos;
	}
	
	public String Name(){return SSID;}
	public int Num() {return level;}
}
