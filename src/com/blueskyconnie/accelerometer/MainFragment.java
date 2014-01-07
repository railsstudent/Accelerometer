package com.blueskyconnie.accelerometer;

import java.util.Random;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class MainFragment extends Fragment implements SensorEventListener {

	private final float NOISE = (float) 5.0;
	
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private boolean mInitialized = false;
	private float mLastX;
	private float mLastY;
	private float mLastZ;
	private int numOfShake;
	private int count = 0;
	private Random rnd;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = (View) inflater.inflate(R.layout.fragment_main, null);
		
		mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
		rnd = new Random();
		
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
		count = (rnd.nextInt(5) + 1) * 2;
		Toast.makeText(getActivity(), "MainFragment - On Resume, shake at least " + (count / 2) + " times.", 
					Toast.LENGTH_SHORT).show();
		numOfShake = 0;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		if (!mInitialized) {
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			mInitialized = true;
		} else {
			float deltaX = Math.abs(mLastX - x);
			float deltaY = Math.abs(mLastY - y);
			float deltaZ = Math.abs(mLastZ - z);
			if (deltaX < NOISE) deltaX = (float)0.0;
			if (deltaY < NOISE) deltaY = (float)0.0;
			if (deltaZ < NOISE) deltaZ = (float)0.0;
			if ( (deltaX > deltaY) && (mLastX < x) ) {
				// shake horizontally
				if (numOfShake >=  count) {
					// replace to charm fragment
					Fragment frag = new CharmMediaFragment();
					FragmentManager fragmentManager = this.getFragmentManager();
					fragmentManager.beginTransaction()
						.replace(R.id.frame_container, frag)
						.addToBackStack(null)
						.commit();
				}
				numOfShake++;
			} 
			mLastX = x;
			mLastY = y;
			mLastZ = z;
		}
	}
}
