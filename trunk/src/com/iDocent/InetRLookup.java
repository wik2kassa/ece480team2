package com.iDocent;

import java.lang.reflect.Array;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.io.*;

public class InetRLookup {
	private String mac;
	private final String DNS = "72.44.121.49";
	//private final String DNS = "480team2.dyndns-server.com";
	private final int Port = 1024;
	
	private Socket conn;
	private DataInputStream is;
	private PrintStream os;
	
	HashMap<String, LinkedList<Float>> APs;
	float coords [];
	
	public static enum Modes {Individual, All};
	private Modes mode;
	
	boolean found;
	
	public boolean WasFound()
	{
		return found;
	}
	
	public InetRLookup(){
		coords = new float[3];
		found = false;
	}
	
	public void run() {	
		if(conn != null && conn.isConnected())
		{
			switch(mode){
			case Individual:
				Individual();
				break;
				
			case All:
				All();
				break;
			}
		}
	}
	private void All() {
		os.println("get-routers");
		String tmp;
		APs = new HashMap<String, LinkedList<Float>>();
		try {
			while((tmp = is.readLine()) != null && !tmp.equals("INVALID COMMAND")){
				String args [] = tmp.split(" ");
				if(args[0].equals("ERROR:") || (Array.getLength(args) < 4))
					break;
				int length = Array.getLength(args);
				for(int i=0; i < length && (i + 3) < length;i+=4)
				{
					if (args[i].equals(mac)){
						LinkedList<Float> l = new LinkedList<Float>();
						float[] f=LocationNormalizer.Normalize(Float.valueOf(args[i+1]), Float.valueOf(args[i+2]), 
								Float.valueOf(args[i+3]));
						l.add(f[0]);
						l.add(f[1]);
						l.add(f[2]);
						APs.put(mac, l);
					}
				}
				found = true;
				break;
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void Individual(){
		os.println("get-router-coord " + mac);
		String tmp;
		try {
			while((tmp = is.readLine()) != null && !tmp.equals("INVALID COMMAND")){
				String args [] = tmp.split(" ");
				if(args[0].equals("ERROR:") || (Array.getLength(args) < 4))
					break;
				if (args[0].equals(mac)){
					coords = LocationNormalizer.Normalize(Float.valueOf(args[1]), Float.valueOf(args[2]), 
							Float.valueOf(args[3]));
					found = true;
					break;
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setMac(String x){
		mac = x;
	}
	
	public float[] getCoords(){
		found = false;
		return coords;
	}

	public void Connect() {
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
	}

	public void Disconnect() {
		if(conn != null && conn.isConnected())
		{
			os.println("quit");			
			try {
				conn.close();
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean isConnected() {
		return (conn != null && conn.isConnected());
	}

	public HashMap<String, LinkedList<Float>> getList() {
		found = false;
		return APs;
	}

	public void setMode(Modes m) {
		mode = m;
	}
}
