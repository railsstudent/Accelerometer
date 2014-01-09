package com.blueskyconnie.accelerometer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ExplanationMediaFragment extends Fragment implements MediaPlayer.OnCompletionListener,
	MediaPlayer.OnErrorListener {

	private static final String TAG = "TAG - CharmMediaFragment";

	private Button btnSpeech;
	private Button btnIntepretation;
	private TextView txtText;
	private MediaPlayer mediaPlayer;
	private String[] explanation;
	private int charmIdx;
	
	public ExplanationMediaFragment() {
		
		explanation = new String[2];
		explanation[0] = "家宅祈禳, 六甲生男, 訟事勝訴, 自身秋好,行人將至, 失物東北, 求財冬吉, 田蠶好造, 患病東北, 六易厚利, 六畜有成, 患病東北, 婚姻成就, 尋人得見, 山墳吉祥";
		explanation[1] = "家宅不寧, 田蠶尚可, 移徒可去, 自身尚好,六畜不吉, 失物東南, 求財利無, 尋人可見, 婚姻難諧, 爭訟和解, 病宜祈禳, 行人得回, 六甲祈福, 山墳吉祥";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = (View) inflater.inflate(R.layout.fragment_explain, null);

		charmIdx = this.getArguments().getInt(Constants.CHARMIDX);
		txtText = (TextView) rootView.findViewById(R.id.textView1);
		txtText.setText(explanation[charmIdx]);
		
		initializeMediaPlayer();
		
		btnSpeech = (Button) rootView.findViewById(R.id.btnSpeech);
		btnSpeech.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				speakExplanation();
			}
			
		});
		
		btnIntepretation = (Button) rootView.findViewById(R.id.btnInterpretation);
		btnIntepretation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				 // go to next fragment
				Fragment fragment = new TeachingMediaFragment();
				Bundle bundle = new Bundle();
				bundle.putInt(Constants.CHARMIDX, charmIdx);
				fragment.setArguments(bundle);
				FragmentManager fragmentManager = ExplanationMediaFragment.this.getFragmentManager();
				fragmentManager.popBackStack(Constants.MAIN_STATE, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment)
					.addToBackStack(null)
					.commit();
			}
		});
		
		return rootView;
	}

	private void initializeMediaPlayer() {
		int resCharmId = getResources().getIdentifier("raw/explain" + charmIdx, null, getActivity().getPackageName());
		mediaPlayer = MediaPlayer.create(getActivity(), resCharmId);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnErrorListener(this);
	}

	@Override
	public void onPause() {
	    // Don't forget to release mediaplayer when exit fragment
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}
		super.onPause();
	}

	@Override
	public void onDestroy() {
		 // Don't forget to release mediaplayer
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}
        super.onDestroy();
	}
	

	@Override
	public void onResume() {
		super.onResume();
		initializeMediaPlayer();
		btnSpeech.setEnabled(true);
	}

	private void speakExplanation() {
		btnSpeech.setEnabled(false);
		playSample();
	}

	/**
	 * Play a sample with the Android MediaPLayer.
	 *
	 * @param resid Resource ID if the sample to play.
	 */
	private void playSample() 	{
//	    AssetFileDescriptor afd = getActivity().getResources().openRawResourceFd(resid);

	    try  {   
//	        mediaPlayer.reset();
//	        mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
//	        mediaPlayer.prepare();
	        mediaPlayer.start();
//	        afd.close();
	    }
	    catch (IllegalArgumentException e) {
	        Log.e(TAG, "Unable to play audio queue due to exception: " + e.getMessage(), e);
	    } catch (IllegalStateException e) {
	        Log.e(TAG, "Unable to play audio queue due to exception: " + e.getMessage(), e);
	    } /* catch (IOException e)   {
	        Log.e(TAG, "Unable to play audio queue due to exception: " + e.getMessage(), e);
	    }*/
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		mp.seekTo(0);
		btnSpeech.setEnabled(true);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		btnSpeech.setEnabled(true);
		return false;
	}
	
}
