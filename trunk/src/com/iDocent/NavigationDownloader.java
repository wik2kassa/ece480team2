package com.iDocent;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Stack;

public class NavigationDownloader {
	private final String DNS = "480team2.dyndns-server.com";
	private final int Port = 1024;
	
	Socket conn;
	DataInputStream is;
	PrintStream os;
	
	private ArrayList<Integer> nodes;
	
	ArrayList<Integer> GetNodes()
	{
		return nodes;
	}
	
	public NavigationDownloader(float posX, float posY, float posZ, String destination)
	{
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
				os.println("directions "+posX+" "+posY+" "+posZ+" "+destination);
				String tmp;

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
