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
import android.widget.ImageView;
import android.widget.TextView;
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
	private TextView tvX;
	private TextView tvY;
	private TextView tvZ;
	private ImageView iv;
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
		
		tvX= (TextView) rootView.findViewById(R.id.x_axis);
		tvY= (TextView) rootView.findViewById(R.id.y_axis);
		tvZ= (TextView) rootView.findViewById(R.id.z_axis);
		iv = (ImageView) rootView.findViewById(R.id.image);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
		count = rnd.nextInt(16) + 1;
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
			tvX.setText("0.0");
			tvY.setText("0.0");
			tvZ.setText("0.0");
			mInitialized = true;
		} else {
			float deltaX = Math.abs(mLastX - x);
			float deltaY = Math.abs(mLastY - y);
			float deltaZ = Math.abs(mLastZ - z);
			if (deltaX < NOISE) deltaX = (float)0.0;
			if (deltaY < NOISE) deltaY = (float)0.0;
			if (deltaZ < NOISE) deltaZ = (float)0.0;
			tvX.setText(Float.toString(deltaX));
			tvY.setText(Float.toString(deltaY));
			tvZ.setText(Float.toString(deltaZ));
			iv.setVisibility(View.VISIBLE);
			if ( (deltaX > deltaY) && (mLastX < x) ) {
				// shake horizontally
				iv.setImageResource(R.drawable.horizontal);
				if (numOfShake >=  count) {
					// replace to explanation fragment
					Fragment frag = new ExplanationFragment();
					FragmentManager fragmentManager = this.getFragmentManager();
					fragmentManager.beginTransaction()
						.replace(R.id.frame_container, frag)
						.addToBackStack(null)
						.commit();
				}
				numOfShake++;
			} else {
				iv.setVisibility(View.INVISIBLE);
			}
			mLastX = x;
			mLastY = y;
			mLastZ = z;
		}
	}
}
