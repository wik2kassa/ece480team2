/*
 * ECE 480 Spring 2011
 * Team 2 Design Project
 * Matt Gottshall
 * Jake D'Onofrio
 * Gordie Stein
 * Andrew Kling
 */
package com.iDocent;

import java.util.LinkedList;
import java.util.List;

/**An object to hold the important information of a access point*/
public class WeightedScan {
	private int level;
	private String MAC;
	private Float posX, posY, posZ;
	
	private static int x=0; 
	private static int y=1; 
	private static int z=2; 
	
	//DEMO VARIABLE
	public int number;
	
	public WeightedScan(String mac, List<Float> loc)
	{
		MAC = mac;
		posX = loc.get(x);
		posY = loc.get(y);
		posZ = loc.get(z);
	}

	public void SetLevel(int l){level = l;}	
		
	public List<Float> GetPos(){
		List<Float> pos = new LinkedList<Float>();
		pos.add(posX);
		pos.add(posY);
		pos.add(posZ);
		return pos;
	}
	
	public String Name(){return MAC;}
	public int GetLevel() {return level;}
}
