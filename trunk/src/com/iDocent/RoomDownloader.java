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
import java.util.LinkedList;

//Download all rooms from the server.
public class RoomDownloader implements Runnable {
	iDocent miD;
	
	private final String DNS = "480team2.dyndns-server.com";
	private final int Port = 1024;
	
	Socket conn;
	DataInputStream is;
	PrintStream os;
	
	LinkedList<Room> rooms;
	
	public RoomDownloader(iDocent iDocent) {
		miD = iDocent;
		rooms = new LinkedList<Room>();
	}

	/**
	 * This is run when the thread is started and downloads the list of rooms from the server
	 */
	@Override
	public void run() {
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
				os.println("get-room-list");
				String tmp;
				Room r;
				while((tmp = is.readLine()) != null && !tmp.equals("DONE")){
					String args [] = tmp.split(" ");
					//received String in format:
					//number, type, x, y, z
					r = new Room(Integer.parseInt(args[0]), args[1], Float.parseFloat(args[2]),
							Float.parseFloat(args[3]),Float.parseFloat(args[4]));
					rooms.add(r);
				}
				conn.close();
				miD.RoomsReady(rooms);
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
