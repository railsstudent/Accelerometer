package com.blueskyconnie.accelerometer;

import java.util.Random;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TeachingMediaFragment extends Fragment implements MediaPlayer.OnCompletionListener,
	MediaPlayer.OnErrorListener {

	private static final String TAG = "TAG - " + TeachingMediaFragment.class.getName();
	
	private Button btnSpeech;
	private TextView txtText;
	private String[][] teachings;
	private Random rnd;
	private MediaPlayer mediaPlayer;
	private int teachingIdx;
	private int charmIdx;
	private String audioFile;
	
	public TeachingMediaFragment() {

		teachings = new String[2][3];
		teachings[0][0] = "貪心不能致富, 喜捨才能多福, 利他不忘自謙, 自謙才能度眾";
		teachings[0][1] = "正義行事, 公理不會長久湮沒, 心存善念, 果報必然早晚現前";
		teachings[0][2] = "船行的路是海洋, 明燈的路是夜晚, 人行的路是善事, 吉祥的路是慈悲";
		teachings[1][0] = "平安就是福報, 功德就是壽命, 知足就是富貴, 適情就是自在";
		teachings[1][1] = "聰明者不迷, 正見者不邪, 有容者不妒, 心靜者不煩";
		teachings[1][2] = "貧病之時知朋友, 患難之時識真情, 進退之時懂分寸, 得失之時通因果";
		rnd = new Random();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = (View) inflater.inflate(R.layout.fragment_teaching, null);

		charmIdx = this.getArguments().getInt(Constants.CHARMIDX);
		teachingIdx = rnd.nextInt(3);
		
		audioFile = "raw/t" + charmIdx + teachingIdx;
		initializeMediaPlayer();
		
		txtText = (TextView) rootView.findViewById(R.id.textView1);
		txtText.setText(teachings[charmIdx][teachingIdx]);
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
	public void onResume() {
		super.onResume();
		initializeMediaPlayer();
		btnSpeech.setEnabled(true);
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

	private void speakOut() {
		btnSpeech.setEnabled(false);
		//int resInterpretationId = getResources().getIdentifier(audioFile, null, getActivity().getPackageName());
		playSample();
	//	btnSpeech.setEnabled(true);
    }
	
	private void initializeMediaPlayer() {
		int resInterpretationId = getResources().getIdentifier(audioFile, null, getActivity().getPackageName());
		mediaPlayer = MediaPlayer.create(getActivity(), resInterpretationId);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnErrorListener(this);
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
	public boolean onError(MediaPlayer mp, int what, int extra) {
		btnSpeech.setEnabled(true);
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		mp.seekTo(0);
		btnSpeech.setEnabled(true);
	}
}
