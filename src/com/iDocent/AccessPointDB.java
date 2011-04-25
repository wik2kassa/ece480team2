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

/**
 * This is an object to hold the list of dowloaded APs and their location
 *
 */
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
	
	@return List<Float> - the access point's location stored in a list (x y z)
	 *   
	 */
	public List<Float> GetLocation(String mac)
	{
		return accessPoints.get(mac.toLowerCase());
	}

	/**
	 * Start scanning APs.  Checks if there is a live connection with the server then downloads the list
	 * of APs.
	 * @return boolean - true if the list was downloaded
	 */
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

	/**
	 * Disconnect from the server.
	 */
	public void EndScanLoop() {
		irl.Disconnect();
	}

	/**
	 * Connect to the server
	 * @return boolean - true if connection was successful.
	 */
	public boolean Connect() {
		irl.Connect();
		return irl.isConnected();
	}
}
