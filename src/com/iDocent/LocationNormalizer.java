package com.iDocent;

public class LocationNormalizer {
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
		
		float[] l = {posX, -posY, posZ};
		return l;
	}
}
