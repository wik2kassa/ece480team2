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
import java.util.Vector;
import javax.microedition.khronos.opengles.GL10;

/**The map with all rooms and halls for all floors*/
public class Map extends GraphicsObject{
	
	private Vector<GraphicsObject> Children;
	private Vector<GraphicsObject> FirstFloor;
	private Vector<GraphicsObject> SecondFloor;
	private Vector<GraphicsObject> ThirdFloor;
	
	private HashMap<Integer, Integer> roomSquarePos = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> roomSquarePos2 = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> roomSquarePos3 = new HashMap<Integer, Integer>();
	
	private float posX;
	private float posY;
	private float posZ;
	
	private Dot mDot;
	
	private RoomSquare destination = null;
	
	public Map(iDocent miD){
		Children = new Vector<GraphicsObject>();
		FirstFloor = new Vector<GraphicsObject>();
		
		mDot = new Dot(20, -20, 0);
		
		int pos1 = 0;
		FirstFloor.add(new Line(-120,-16.4f, 12,-16.4f));pos1++;
		FirstFloor.add(new Line(24.5f,-16.4f, 143.5f,-16.4f));pos1++;
		FirstFloor.add(new Line(-108,-28.4f, 120,-28.4f));pos1++;
		FirstFloor.add(new Line(143.5f,-16.4f, 143.5f,-28.4f));pos1++;
		FirstFloor.add(new Line(132,-28.4f, 143.5f,-28.4f));pos1++;
		FirstFloor.add(new Line(120,-28.4f, 120,-468.4f));pos1++;
		FirstFloor.add(new Line(132,-28.4f, 132,-468.4f));pos1++;
		FirstFloor.add(new Line(120, -468.4f, 132, -468.4f));pos1++;
		//FirstFloor.add(new Line(132, -153.4f, 142, -153.4f));
		
		//Previous halls are shared in all floors
		SecondFloor = new Vector<GraphicsObject>(FirstFloor);
		int pos2 = pos1;
		
		FirstFloor.add(new Line(-120, -16.4f, -120, -64.4f));pos1++;
		FirstFloor.add(new Line(-108, -28.4f, -108, -91.4f));pos1++;
		FirstFloor.add(new Line(-120, -64.4f, -165, -64.4f));pos1++;
		FirstFloor.add(new Line(12,0, 12,-16.4f));pos1++;
		FirstFloor.add(new Line(24.5f,0, 24.5f,-16.4f));pos1++;
		FirstFloor.add(new Line(-188, -61.4f, -188, -95.4f));pos1++;
		FirstFloor.add(new Line(-188, -61.4f, -165, -59.4f));pos1++;
		FirstFloor.add(new Line(-188, -95.4f, -165, -97.4f));pos1++;
		FirstFloor.add(new Line(-165, -59.4f, -165, -64.4f));pos1++;
		FirstFloor.add(new Line(-165, -97.4f, -165, -91.4f));pos1++;
		FirstFloor.add(new Line(-165, -91.4f, -108, -91.4f));pos1++;
		
		SecondFloor.add(new Line(12, -16.4f, 24.5f, -16.4f));pos2++;
		
		ThirdFloor = new Vector<GraphicsObject>(SecondFloor);
		int pos3 = pos2;
		
		//add rooms
		String classroom = "blue", office = "green", restroom = "white", lab = "yellow",
		stair = "white", unknown = "gray", spartys = "white", thecenter = "white";
		
		FirstFloor.add(new RoomSquare(-188f, -61.4f, "left", "top", 70f, 34f, classroom, miD, 1345));//1345
		roomSquarePos.put(1345, pos1++);
		FirstFloor.add(new RoomSquare(-165f, -91.4f, "top", "left", 28.6f, 5f, spartys, miD, 6000));//6000
		roomSquarePos.put(6000, pos1++); 
		FirstFloor.add(new RoomSquare(-165f, -64.4f, "top", "left", 45f, 30f, thecenter, miD, 1340));//1340
		roomSquarePos.put(1340, pos1++); 
		
		FirstFloor.add(new RoomSquare(120, -40f, "left", "bottom", 23, 11.6f, restroom, miD, 1203));//1203
		roomSquarePos.put(1203, pos1++);
		FirstFloor.add(new RoomSquare(120, -71f, "left", "bottom", 23, 31, stair, miD, 8000));//8000
		roomSquarePos.put(8000, pos1++);
		FirstFloor.add(new RoomSquare(120, -71f, "left", "top", 23, 10, office, miD, 1210));//1210
		roomSquarePos.put(1210, pos1++);
		FirstFloor.add(new RoomSquare(120, -91f, "left", "bottom", 19, 10, office, miD, 1211));//1211
		roomSquarePos.put(1211, pos1++);
		FirstFloor.add(new RoomSquare(120, -105f, "left", "bottom", 19, 14, office, miD, 1213));//1213
		roomSquarePos.put(1213, pos1++);
		FirstFloor.add(new RoomSquare(120, -111f, "left", "bottom", 19, 6, office, miD, 1216));//1216
		roomSquarePos.put(1216, pos1++);
		FirstFloor.add(new RoomSquare(120, -121f, "left", "bottom", 19, 10, office, miD, 1217));//1217
		roomSquarePos.put(1217, pos1++);
		FirstFloor.add(new RoomSquare(120, -131f, "left", "bottom", 19, 10, office, miD, 1218));//1218
		roomSquarePos.put(1218, pos1++);
		FirstFloor.add(new RoomSquare(120, -139f, "left", "bottom", 19, 8, office, miD, 1219));//1219
		roomSquarePos.put(1219, pos1++);
		FirstFloor.add(new RoomSquare(120, -151f, "left", "bottom", 22, 12, office, miD, 1226));//1226
		roomSquarePos.put(1226, pos1++);
		FirstFloor.add(new RoomSquare(120, -178f, "left", "bottom", 22, 27, unknown, miD, 1228));//1228
		roomSquarePos.put(1228, pos1++);
		FirstFloor.add(new RoomSquare(120, -197f, "left", "bottom", 22, 19, unknown, miD, 1231));//1231
		roomSquarePos.put(1231, pos1++);
		FirstFloor.add(new RoomSquare(120, -220f, "left", "bottom", 22, 23, unknown, miD, 1232));//1232
		roomSquarePos.put(1232, pos1++);
		FirstFloor.add(new RoomSquare(120, -241f, "left", "bottom", 27, 21, unknown, miD, 1235));//1235
		roomSquarePos.put(1235, pos1++);
		FirstFloor.add(new RoomSquare(120, -265f, "left", "bottom", 27, 24, stair, miD, 8000));//stair
		roomSquarePos.put(-2, pos1++);
		FirstFloor.add(new RoomSquare(120, -288f, "left", "bottom", 27, 23, unknown, miD, 1237));//1237
		roomSquarePos.put(1237, pos1++);
		FirstFloor.add(new RoomSquare(120, -311f, "left", "bottom", 27, 23, unknown, miD, 1242));//1242
		roomSquarePos.put(1242, pos1++);
		FirstFloor.add(new RoomSquare(120, -322f, "left", "bottom", 27, 11, unknown, miD, 1243));//1243
		roomSquarePos.put(1243, pos1++);
		FirstFloor.add(new RoomSquare(120, -330f, "left", "bottom", 27, 8, unknown, miD, 1245));//1245
		roomSquarePos.put(1245, pos1++);
		FirstFloor.add(new RoomSquare(120, -342f, "left", "bottom", 27, 12, unknown, miD, 1248));//1248
		roomSquarePos.put(1248, pos1++);
		FirstFloor.add(new RoomSquare(120, -357f, "left", "bottom", 27, 15, unknown, miD, 1250));//1250
		roomSquarePos.put(1250, pos1++);
		FirstFloor.add(new RoomSquare(120, -371f, "left", "bottom", 27, 14, stair, miD, 8000));//stair
		roomSquarePos.put(-1, pos1++);
		FirstFloor.add(new RoomSquare(120, -468.4f, "left", "bottom", 22, 97, unknown, miD, -1));//group
		roomSquarePos.put(-10, pos1++);
		
		FirstFloor.add(new RoomSquare(132, -28.4f, "right", "top", 30, 30.6f, unknown, miD, 1204));//1204
		roomSquarePos.put(1204, pos1++);
		FirstFloor.add(new RoomSquare(132, -59f, "right", "top", 30, 11f, unknown, miD, 1206));//1206
		roomSquarePos.put(1206, pos1++);
		FirstFloor.add(new RoomSquare(132, -70f, "right", "top", 30, 35f, unknown, miD, 1208));//1208
		roomSquarePos.put(1208, pos1++);
		FirstFloor.add(new RoomSquare(132, -105f, "right", "top", 30, 12f, unknown, miD, 1215));//1215
		roomSquarePos.put(1215, pos1++);
		FirstFloor.add(new RoomSquare(132, -117f, "right", "top", 30, 32f, unknown, miD, 1220));//1220
		roomSquarePos.put(1220, pos1++);
		FirstFloor.add(new RoomSquare(132, -149f, "right", "top", 30, 36.5f, classroom, miD, 1225));//1225
		roomSquarePos.put(1225, pos1++);
		FirstFloor.add(new RoomSquare(132, -219f, "right", "bottom", 30, 33.5f, classroom, miD, 1230));//1230
		roomSquarePos.put(1230, pos1++);
		FirstFloor.add(new RoomSquare(132, -219f, "right", "top", 30, 38f, classroom, miD, 1234));//1234
		roomSquarePos.put(1234, pos1++);
		FirstFloor.add(new RoomSquare(132, -468.4f, "right", "bottom", 30, 199f, unknown, miD, 10000));//group
		roomSquarePos.put(-3, pos1++);
		
		FirstFloor.add(new RoomSquare(97, -28.4f, "bottom", "right", 22, 30f, unknown, miD, 8000));//stair/1306?
		roomSquarePos.put(-4, pos1++);
		FirstFloor.add(new RoomSquare(27f, -28.4f, "bottom", "left", 48f, 30f, lab, miD, 1307));//1307
		roomSquarePos.put(1307, pos1++);
		FirstFloor.add(new RoomSquare(27f, -28.4f, "bottom", "right", 12f, 30f, stair, miD, 8000));//stair
		roomSquarePos.put(-6, pos1++);
		FirstFloor.add(new RoomSquare(-16f, -28.4f, "bottom", "left", 31f, 30f, lab, miD, 1320));//1320
		roomSquarePos.put(1320, pos1++);
		
		FirstFloor.add(new RoomSquare(130f, -16.4f, "top", "right", 26f, 30f, classroom, miD, 1202));//1202
		roomSquarePos.put(1202, pos1++);
		FirstFloor.add(new RoomSquare(104f, -16.4f, "top", "right", 30f, 30f, classroom, miD, 1300));//1300
		roomSquarePos.put(1300, pos1++);
		FirstFloor.add(new RoomSquare(74f, -16.4f, "top", "right", 9f, 30f, unknown, miD, 1303));//1303
		roomSquarePos.put(1303, pos1++);
		FirstFloor.add(new RoomSquare(65f, -16.4f, "top", "right", 22f, 30f, classroom, miD, 1312));//1312
		roomSquarePos.put(1312, pos1++);
		FirstFloor.add(new RoomSquare(43f, -16.4f, "top", "right", 18.5f, 30f, lab, miD, 1314));//1314
		roomSquarePos.put(1314, pos1++);
		FirstFloor.add(new RoomSquare(12f, -16.4f, "top", "right", 32f, 30f, lab, miD, 1318));//1318
		roomSquarePos.put(1318, pos1++);
		
		FirstFloor.add(new RoomSquare(-20f, -16.4f, "top", "right", 100f, 30f, unknown, miD, -1));//junk
		roomSquarePos.put(-20, pos1++); 
		FirstFloor.add(new RoomSquare(-16f, -28.4f, "bottom", "right", 92f, 30f, unknown, miD, -1));//junk
		roomSquarePos.put(-21, pos1++); 
		
		SecondFloor.add(new RoomSquare(120, -40f, "left", "bottom", 30, 11.6f, restroom, miD, 2201));//2201
		roomSquarePos2.put(2201, pos2++);
		SecondFloor.add(new RoomSquare(120f, -40f, "left", "top", 30f, 31f, stair, miD, 8000));//stair
		roomSquarePos2.put(-11, pos2++); 
		SecondFloor.add(new RoomSquare(120f, -71f, "left", "top", 30f, 20f, unknown, miD, 2206));//2206
		roomSquarePos2.put(2206, pos2++); 
		SecondFloor.add(new RoomSquare(120f, -91f, "left", "top", 20f, 7.5f, office, miD, 2210));//2210
		roomSquarePos2.put(2210, pos2++);
		SecondFloor.add(new RoomSquare(120f, -106f, "left", "bottom", 20f, 7.5f, office, miD, 2211));//2211
		roomSquarePos2.put(2211, pos2++); 
		SecondFloor.add(new RoomSquare(120f, -106f, "left", "top", 20f, 12f, office, miD, 2212));//2212
		roomSquarePos2.put(2212, pos2++); 
		SecondFloor.add(new RoomSquare(120f, -130f, "left", "bottom", 20f, 12f, office, miD, 2215));//2215
		roomSquarePos2.put(2215, pos2++); 
		SecondFloor.add(new RoomSquare(120f, -130f, "left", "top", 20f, 9f, office, miD, 2216));//2216
		roomSquarePos2.put(2216, pos2++); 
		SecondFloor.add(new RoomSquare(120f, -139f, "left", "top", 20f, 9f, office, miD, 2218));//2218
		roomSquarePos2.put(2218, pos2++); 
		SecondFloor.add(new RoomSquare(120f, -148f, "left", "top", 30f, 20f, classroom, miD, 2219));//2219
		roomSquarePos2.put(2219, pos2++);
		SecondFloor.add(new RoomSquare(120f, -344f, "left", "bottom", 30f, 176f, unknown, miD, -1));//2251
		roomSquarePos2.put(-16, pos2++); 
		SecondFloor.add(new RoomSquare(120f, -356f, "left", "top", 30f, 20f, unknown, miD, 2251));//2251
		roomSquarePos2.put(2251, pos2++); 
		SecondFloor.add(new RoomSquare(120f, -376f, "left", "top", 30f, 92.4f, unknown, miD, -1));//-12
		roomSquarePos2.put(-12, pos2++); 
		
		SecondFloor.add(new RoomSquare(132f, -28.4f, "right", "top", 30f, 29.6f, unknown, miD, -1));//stuff
		roomSquarePos2.put(-13, pos2++); 
		SecondFloor.add(new RoomSquare(132f, -58f, "right", "top", 30f, 12f, classroom, miD, 2203));//2203
		roomSquarePos2.put(2203, pos2++); 
		SecondFloor.add(new RoomSquare(132f, -70f, "right", "top", 30f, 35f, classroom, miD, 2205));//2205
		roomSquarePos2.put(2205, pos2++); 
		SecondFloor.add(new RoomSquare(132f, -105f, "right", "top", 30f, 16f, unknown, miD, -1));//something
		roomSquarePos2.put(-14, pos2++); 
		SecondFloor.add(new RoomSquare(132f, -121f, "right", "top", 30f, 45f, classroom, miD, 2214));//2214
		roomSquarePos2.put(2214, pos2++);
		SecondFloor.add(new RoomSquare(132f, -166f, "right", "top", 30f, 58f, lab, miD, 2221));//2221
		roomSquarePos2.put(2221, pos2++); 
		SecondFloor.add(new RoomSquare(132f, -224f, "right", "top", 30f, 39f, lab, miD, 2231));//2231
		roomSquarePos2.put(2231, pos2++); 
		SecondFloor.add(new RoomSquare(132f, -263f, "right", "top", 30f, 44f, lab, miD, 2234));//2234
		roomSquarePos2.put(2234, pos2++); 
		SecondFloor.add(new RoomSquare(132f, -307f, "right", "top", 30f, 28f, lab, miD, 2243));//2243
		roomSquarePos2.put(2243, pos2++); 
		SecondFloor.add(new RoomSquare(132f, -335f, "right", "top", 30f, 14f, unknown, miD, 2245));//2245
		roomSquarePos2.put(2245, pos2++); 
		SecondFloor.add(new RoomSquare(132f, -349f, "right", "top", 30f, 17f, unknown, miD, 2250));//2250
		roomSquarePos2.put(2250, pos2++); 
		SecondFloor.add(new RoomSquare(132f, -468.4f, "right", "bottom", 30f, 102.4f, unknown, miD, -1));//roomNum
		roomSquarePos2.put(-15, pos2++); 
		
		SecondFloor.add(new RoomSquare(90f, -28.4f, "bottom", "right", 50f, 30f, office, miD, 2308));//2308 group
		roomSquarePos2.put(2308, pos2++);  
		SecondFloor.add(new RoomSquare(27f, -28.4f, "bottom", "right", 12f, 30f, stair, miD, 8000));//stair
		roomSquarePos.put(-6, pos1++);
		
		SecondFloor.add(new RoomSquare(47f, -16.4f, "top", "left", 30f, 30f, lab, miD, 2314));//2314
		roomSquarePos2.put(2314, pos2++); 
		SecondFloor.add(new RoomSquare(77f, -16.4f, "top", "left", 43f, 30f, unknown, miD, -1));//junk
		roomSquarePos2.put(-16, pos2++); 
		SecondFloor.add(new RoomSquare(-120f, -16.4f, "top", "left", 167f, 30f, unknown, miD, -1));//junk
		roomSquarePos2.put(-18, pos2++); 
		SecondFloor.add(new RoomSquare(40f, -28.4f, "bottom", "right", 13f, 30f, unknown, miD, -1));//junk
		roomSquarePos2.put(-17, pos2++); 
		SecondFloor.add(new RoomSquare(15f, -28.4f, "bottom", "right", 123f, 30f, unknown, miD, -1));//junk
		roomSquarePos2.put(-17, pos2++); 
		
		SecondFloor.add(new RoomSquare(-120f, -16.4f, "left", "top", 40f, 30f, classroom, miD, 2400));//2400
		roomSquarePos2.put(2400, pos2++); 
		SecondFloor.add(new RoomSquare(120f, -16.4f, "top", "left", 45f, 30f, classroom, miD, 2200));//2200
		roomSquarePos2.put(2200, pos2++); 
		SecondFloor.add(new RoomSquare(165f, -16.4f, "top", "left", 43f, 30f, unknown, miD, -1));//junk
		roomSquarePos2.put(-20, pos2++); 
		
		Children = FirstFloor;
	}

	@Override
	public void Draw(GL10 GL) {
		if(posZ == LocationNormalizer.ffz)
			Children = FirstFloor;
		else if(posZ == LocationNormalizer.sfz)
			Children = SecondFloor;
		else
			Children = ThirdFloor;
		
		if(destination != null)
			destination.setSelected();
		
		for (GraphicsObject i:Children){
			if(i instanceof RoomSquare)
			{
				((RoomSquare)i).DrawTexture(GL);
			}
			i.Draw(GL);
		}
		mDot.Draw(GL);
	}

	/**
	 * Update the stored location of the user
	 * @param x - the user's x location
	 * @param y - the user's y location
	 * @param z - the user's z location
	 */
	public void UpdateLoction(float x, float y, float z) {
		float[] f = LocationNormalizer.Normalize(x, y, z);
		posX = f[0];
		posY = f[1];
		posZ = f[2];
		mDot.UpdateLocation(posX, posY, posZ);
	}

	public void loadTexture(GL10 gl, iDocent miD) {
		for(GraphicsObject r : FirstFloor)
		{
			if(r instanceof RoomSquare)
				((RoomSquare)r).loadGLTexture(gl, miD);
		}
		
		for(GraphicsObject r : SecondFloor)
		{
			if(r instanceof RoomSquare)
				((RoomSquare)r).loadGLTexture(gl, miD);
		}
		
		for(GraphicsObject r : ThirdFloor)
		{
			if(r instanceof RoomSquare)
				((RoomSquare)r).loadGLTexture(gl, miD);
		}
	}

	/**
	 * Set the room to be the destination to draw it red
	 * @param num - the room number
	 */
	public void setDestination(int num) {
		if(num < 2000)
		{
			GraphicsObject g = FirstFloor.get(roomSquarePos.get(num));
			if(g instanceof RoomSquare)
				destination = ((RoomSquare) g);
		}
		else if (num >= 2000 && num < 3000)
		{
			GraphicsObject g = SecondFloor.get(roomSquarePos2.get(num));
			if(g instanceof RoomSquare)
				destination = ((RoomSquare) g);
		}
	}

	public void clearDestination() {
		destination = null;	
	}

}
