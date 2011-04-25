/*
 * ECE 480 Spring 2011
 * Team 2 Design Project
 * Matt Gottshall
 * Jake D'Onofrio
 * Gordie Stein
 * Andrew Kling
 */
package com.iDocent;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**download the route to the destination*/
public class NavigationDownloader {
	private final String DNS = "480team2.dyndns-server.com";
	private final int Port = 1024;
	
	Socket conn;
	DataInputStream is;
	PrintStream os;
	
	private static enum Modes {Room, Exit, Mens, Womens};
	private Modes mode = Modes.Room;
	
	private ArrayList<Integer> nodes;
	
	ArrayList<Integer> GetNodes()
	{
		return nodes;
	}
	
	public NavigationDownloader(float posX, float posY, float posZ, String destination)
	{
		float[] f = LocationNormalizer.Normalize(posX, posY, posZ);
		posX = f[0];
		posY = f[1];
		posZ = f[2];
		//find out which type of room to navigate to
		if(destination.toLowerCase().contains("men's"))
		{
			mode = Modes.Mens;
		}
		else if(destination.toLowerCase().contains("women's"))
		{
			mode = Modes.Womens;
		}
		else if(destination.toLowerCase().contains("exit"))
		{
			mode = Modes.Exit;
		}
		nodes = new ArrayList<Integer>(); 
		try {	
			try {
				conn = new Socket(DNS,Port);
				is = new DataInputStream(conn.getInputStream());
				os = new PrintStream(conn.getOutputStream());
			} 
			catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(conn != null && conn.isConnected())
			{
				//print the appropriate navigation request
				String tmp;
				posY = Math.abs(posY);
				switch(mode){
				case Room:
					os.println("directions "+posX+" "+posY+" "+posZ+" "+destination);
					break;
					
				case Mens:
					os.println("nearest-mens-restroom "+posX+" "+posY+" "+posZ);
					break;
					
				case Womens:
					os.println("nearest-womens-restroom "+posX+" "+posY+" "+posZ);
					break;
					
				case Exit:
					os.println("nearest-exit "+posX+" "+posY+" "+posZ);
					break;
				}

				while((tmp = is.readLine()) != null && !tmp.equals("DONE")){
					nodes.add(Integer.parseInt(tmp));
				}
				conn.close();
			}
		} 
		catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
