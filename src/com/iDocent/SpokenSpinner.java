package com.iDocent;

import android.content.Context;
import android.content.DialogInterface;
import android.speech.tts.TextToSpeech;
import android.widget.Spinner;

public class SpokenSpinner extends Spinner {
	TextToSpeech tts;
	boolean accessibilityOn;
	
	public SpokenSpinner(Context context, TextToSpeech tts, boolean accessibilityOn) {
		super(context);
		this.tts = tts;
		this.accessibilityOn = accessibilityOn;
	}
	
	@Override
	public void onClick(DialogInterface dialog, int whichButton)
	{
		if(whichButton == 0)
		{
			if(accessibilityOn)
			{
				tts.speak(this.getAdapter().getItem(0).toString(), TextToSpeech.QUEUE_FLUSH, null);
				for(int i=1;i<this.getAdapter().getCount();i++)
				{
					tts.speak(this.getAdapter().getItem(i).toString(), TextToSpeech.QUEUE_ADD, null);
				}
			}
		}
		else
		{
			super.onClick(dialog, whichButton);
		}
	}

}
