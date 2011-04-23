/*
 * ECE 480 Spring 2011
 * Team 2 Design Project
 * Matt Gottshall
 * Jake D'Onofrio
 * Gordie Stein
 * Andrew Kling
 */
package com.iDocent;

//A static class to normalize all locations to work with the maps
public class LocationNormalizer {
	public static final float ffz = 0;
	public static final float sfz = 20;
	public static final float tfz = 40;
	
	public static float[] Normalize(float posX, float posY, float posZ)
	{
		//Normalize X and Y
		posY = Math.abs(posY);
		if(posY > 28.4 && posX > 110)
			posX = (132f+120f)/2.0f;
		else if(posY > 28.4 && posX < -108 && posX > -120)
			posX = ((-120f-108f)/2.0f);
		else if(posY < 28.4 && posX < -108 && posX > -120)
		{
			posX = (-108f-120f)/2.0f;
			posY = (28.4f+16.4f)/2.0f;
		}
		else if(posY > 28.4 && posX < -120)
			posY = 80;
		else if(posX < 120 && posX > -108)
			posY = (28.4f+16.4f)/2.0f;
		else if(posY < 35 && posX > 120)
		{
			posX = (132f+120f)/2.0f;
			posY = (28.4f+16.4f)/2.0f;
		}
		
		//Normalize Z
		if(posZ < sfz/2)
			posZ = ffz;
		else if(posZ >= sfz/2 && posZ < sfz*3/2)
			posZ = sfz;
		else
			posZ = tfz;
		
		//posZ=0;
		
		float[] l = {posX, -posY, posZ};
		return l;
	}
}
