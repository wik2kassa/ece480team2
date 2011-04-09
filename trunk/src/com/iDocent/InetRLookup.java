package com.iDocent;

import java.net.*;
import java.io.*;

public class InetRLookup {
	private String mac;
	private final String DNS = "480team2.dyndns-server.com";
	private final int Port = 1024;
	
	double coords [];
	
	public InetRLookup(){
		coords = new double[3];
	}

	public void run() {
		try {
			Socket conn = new Socket(DNS,Port);
			DataInputStream is = new DataInputStream(conn.getInputStream());
			PrintStream os = new PrintStream(conn.getOutputStream());
			
			os.println("get-router-coord " + mac);
			String tmp;
			while((tmp = is.readLine()) != null && tmp != "INVALID COMMAND"){
				String args [] = tmp.split(" ");
				if (args[0].equals(mac)){
					coords[0] = Double.valueOf(args[1]);
					coords[1] = Double.valueOf(args[2]);
					coords[2] = Double.valueOf(args[3]);
					break;
				}
			}
			os.println("quit");			
			conn.close();
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
	
	public double[] getCoords(){
		return coords;
	}
}
