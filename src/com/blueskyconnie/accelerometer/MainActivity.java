package com.blueskyconnie.accelerometer;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

	private final float NOISE = (float) 5.0;
	
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private boolean mInitialized = false;
	private float mLastX;
	private float mLastY;
	private float mLastZ;
	private int numOfShake;
	private ImageView imgCounter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		imgCounter = (ImageView) findViewById(R.id.imgCounter);
		numOfShake = 0;
	}


	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		TextView tvX= (TextView)findViewById(R.id.x_axis);
		TextView tvY= (TextView)findViewById(R.id.y_axis);
		TextView tvZ= (TextView)findViewById(R.id.z_axis);
		ImageView iv = (ImageView)findViewById(R.id.image);

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
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			tvX.setText(Float.toString(deltaX));
			tvY.setText(Float.toString(deltaY));
			tvZ.setText(Float.toString(deltaZ));
			iv.setVisibility(View.VISIBLE);
			if (deltaX > deltaY) {
				// shake horizontally
				iv.setImageResource(R.drawable.horizontal);
				switch (numOfShake) {
					case 0:
						// show image 0
						imgCounter.setImageResource(R.drawable.icon0);
					case 1:
						// show image 1
						imgCounter.setImageResource(R.drawable.icon1);
						break;
					case 2:
						// show image 2
						imgCounter.setImageResource(R.drawable.icon2);
						break;
					case 3:
						imgCounter.setImageResource(R.drawable.icon3);
						
				}
				numOfShake++;
				if (numOfShake >  3) {
					numOfShake = 0;
				}
			} else {
				iv.setVisibility(View.INVISIBLE);
			} 

			
			/*else if (deltaY > deltaX) {
				iv.setImageResource(R.drawable.vertical);
			} else {
				iv.setVisibility(View.INVISIBLE);
			}*/
		}		
	}

}
