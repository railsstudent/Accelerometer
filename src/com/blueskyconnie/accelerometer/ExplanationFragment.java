package com.blueskyconnie.accelerometer;

import java.util.Locale;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ExplanationFragment extends Fragment implements TextToSpeech.OnInitListener {

	private Button btnSpeech;
	private TextView txtText;
	private TextToSpeech tts;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = (View) inflater.inflate(R.layout.fragment_teaching, null);
		txtText = (TextView) rootView.findViewById(R.id.textView1);
		btnSpeech = (Button) rootView.findViewById(R.id.btnSpeech);
		btnSpeech.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				speakOut();
			}
			
		});
		return rootView;
	}
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tts = new TextToSpeech(getActivity(), this);
	}



	@Override
	public void onDestroy() {
		 // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
		super.onDestroy();
	}


	private void speakOut() {
		 
        String text = txtText.getText().toString();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
	
	@Override
	public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
 
            int result = tts.setLanguage(Locale.CHINESE);
 
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
            	btnSpeech.setEnabled(true);
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
	}
}
