package com.iDocent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//A database that will map an ssid to an xyz location
public class AccessPointDB {
	private Map<String, Integer[]> accessPoints;
	
	private static int x=0; 
	private static int y=1; 
	private static int z=2; 
	
	public AccessPointDB()
	{
		accessPoints = new HashMap<String, Integer[]>();
		FillMap();
	}
	
	/*
	* Name : FillMap()
	* Description : Fill the map with ssid and xyz location sets
	* Parameters :  
	* Returns : void
	*/
	private void FillMap()
	{
		
	}
	
	/*
	 * Name : GetLocation()
	 * Description : 
	 * Parameters : String ssid 
	 * Returns : List<Integer>
	 */
	public List<Integer> GetLocation(String ssid)
	{
		Integer[] pos = accessPoints.get(ssid);
		if(pos != null)
		{
			List<Integer> loc = new LinkedList<Integer>();
			loc.add(pos[x]);
			loc.add(pos[y]);
			loc.add(pos[z]);
			return loc;
		}
		
		return null;
	}
}
