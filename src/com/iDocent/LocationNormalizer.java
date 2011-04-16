package com.iDocent;

public class LocationNormalizer {
	public static final float ffz = 0;
	public static final float sfz = 20;
	public static final float tfz = 40;
	
	public static float[] Normalize(float posX, float posY, float posZ)
	{
		posY = Math.abs(posY);
		if(posY > 28.4)
			posX = (132f+120f)/2.0f;
		else if(posX < 120 && posX > -108 && posY < 35)
			posY = (28.4f+16.4f)/2.0f;
		else if(posY < 28.4 && posX > 120)
		{
			posX = (132f+120f)/2.0f;
			posY = (28.4f+16.4f)/2.0f;
		}
		
		if(posZ < sfz/2)
			posZ = ffz;
		else if(posZ >= sfz/2 && posZ < sfz*3/2)
			posZ = sfz;
		else
			posZ = tfz;
		
		posZ=0;
		
		float[] l = {posX, -posY, posZ};
		return l;
	}
}
