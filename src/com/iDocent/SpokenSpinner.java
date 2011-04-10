package com.iDocent;

import android.content.Context;
import android.content.DialogInterface;
import android.speech.tts.TextToSpeech;
import android.widget.Spinner;

public class SpokenSpinner extends Spinner {
	TextToSpeech tts;
	
	public SpokenSpinner(Context context, TextToSpeech tts) {
		super(context);
		this.tts = tts;
	}
	
	@Override
	public void onClick(DialogInterface dialog, int whichButton)
	{
		if(whichButton == 0)
		{
			tts.speak(this.getAdapter().getItem(0).toString(), TextToSpeech.QUEUE_FLUSH, null);
			for(int i=1;i<this.getAdapter().getCount();i++)
			{
				tts.speak(this.getAdapter().getItem(i).toString(), TextToSpeech.QUEUE_ADD, null);
			}
		}
		else
		{
			super.onClick(dialog, whichButton);
		}
	}

}
