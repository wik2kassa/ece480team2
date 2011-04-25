/*
 * ECE 480 Spring 2011
 * Team 2 Design Project
 * Matt Gottshall
 * Jake D'Onofrio
 * Gordie Stein
 * Andrew Kling
 */
package com.iDocent;

import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 *An object for changing the phone's volume
 *This object adds text to speech to test the new volume
 */
public class VolumeBar extends SeekBar {

	private TextToSpeech tts;
	AudioManager am;

	public VolumeBar(iDocent iDocent, TextToSpeech tts, AudioManager am) {
		super(iDocent);
		this.tts = tts;
		this.am = am;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(event.getAction() == MotionEvent.ACTION_UP)
		{
            double curVol = getProgress();
            double maxVol = getMax();
            am.setStreamVolume(AudioManager.STREAM_MUSIC, 
           		 (int) (curVol/maxVol*am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)), 0);
			tts.speak("", TextToSpeech.QUEUE_FLUSH, null);
			tts.speak("i docent", TextToSpeech.QUEUE_FLUSH, null);
		}
		return super.onTouchEvent(event);
	}
}
