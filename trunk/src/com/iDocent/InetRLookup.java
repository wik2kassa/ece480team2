package com.iDocent;

import java.lang.reflect.Array;
import java.net.*;
import java.io.*;

public class InetRLookup {
	private String mac;
	private final String DNS = "480team2.dyndns-server.com";
	private final int Port = 1024;
	
	Socket conn;
	DataInputStream is;
	PrintStream os;
	
	Float coords [];
	
	boolean found;
	boolean connected;
	
	public boolean WasFound()
	{
		return found;
	}
	
	public InetRLookup(){
		coords = new Float[3];
		found = false;
		connected = false;
	}
	
	public void run() {
		try {		
			if(connected)
			{
				os.println("get-router-coord " + mac);
				String tmp;
				while((tmp = is.readLine()) != null && tmp != "INVALID COMMAND"){
					String args [] = tmp.split(" ");
					if(args[0].equals("ERROR:") || (Array.getLength(args) < 4))
						break;
					if (args[0].equals(mac)){
						coords[0] = Float.valueOf(args[1]);
						coords[1] = Float.valueOf(args[2]);
						coords[2] = Float.valueOf(args[3]);
						found = true;
						break;
					}
				}
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
	public void setMac(String x){
		mac = x;
	}
	
	public Float[] getCoords(){
		found = false;
		return coords;
	}

	public void Connect() {
		try {
			conn = new Socket(DNS,Port);
			is = new DataInputStream(conn.getInputStream());
			os = new PrintStream(conn.getOutputStream());
			if(conn != null && is != null && os != null)
				connected = true;
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

	public void Disconnect() {
		connected = false;
		os.println("quit");			
		try {
			conn.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		return connected;
	}
}
