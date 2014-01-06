package com.blueskyconnie.accelerometer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class MainActivity extends FragmentActivity  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Fragment fragment = new MainFragment();
		FragmentManager fragmentManager  = this.getSupportFragmentManager();
		fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment)
			.commit();
	}
}
