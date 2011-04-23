/*
 * ECE 480 Spring 2011
 * Team 2 Design Project
 * Matt Gottshall
 * Jake D'Onofrio
 * Gordie Stein
 * Andrew Kling
 */
package com.iDocent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//A database that will map an ssid to an xyz location
public class AccessPointDB {
	private HashMap<String, LinkedList<Float>> accessPoints;
	
    InetRLookup irl;
	
//	private static final int x=0; 
//	private static final int y=1; 
//	private static final int z=2; 
//	
//	private iDocent miD;
	
	public AccessPointDB(iDocent miD)
	{
		//this.miD = miD;
		accessPoints = new HashMap<String, LinkedList<Float>>();
		irl = new InetRLookup();
	}
	
	/**
	 * Obtain the location of an access point given the mac address
	                          
	{@link GetLocation}. 
	 *
	                         
	@param  mac - the MAC address of the desired access point
	
	@return List<Integer> - the access point's location stored in a list (x y z)
	 *   
	 */
	public List<Float> GetLocation(String mac)
	{
		return accessPoints.get(mac.toLowerCase());
	}

	public boolean StartScanLoop() {		
		//Download the list of access points from the server
		if(!irl.isConnected())
			return false;
		irl.setMode(InetRLookup.Modes.All);
		irl.run();
		accessPoints = irl.getList();

		irl.Disconnect();
		return true;
	}

	public void EndScanLoop() {
		irl.Disconnect();
	}

	public boolean Connect() {
		irl.Connect();
		return irl.isConnected();
	}
}
