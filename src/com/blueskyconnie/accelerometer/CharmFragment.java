package com.blueskyconnie.accelerometer;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CharmFragment extends Fragment {

	private static final int COLOR_TOLERANCE = 25;
	
    private OnGestureListener gestureListener = new SimpleOnGestureListener() {

    	// must override this method to handle touch events
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
		
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			
			int x = (int) e.getX();
			int y = (int) e.getY();
			// check if the pixel of x an y is #00ffff
			if (isWithinBound(x, y)) {
				Fragment fragment = new ExplanationMediaFragment(); 
				Bundle bundle = new Bundle();
				charmIdx = 1;
				bundle.putInt(Constants.CHARMIDX, charmIdx);
				fragment.setArguments(bundle);
				FragmentManager fragmentManager = CharmFragment.this.getFragmentManager();
				fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment)
					.addToBackStack(null)
					.commit();
			}
			return true;
		}
    };
    
	private TextView txtText;
	private String[] charms;
	private Random rnd;
	private int charmIdx;
    private GestureDetectorCompat gestureDetector;
    private ImageView imgHotspot;
    
    public CharmFragment() {
		charms = new String[2];
		charms[0] = "開天闢地作良緣,\n吉日良時萬物全,\n若得此簽非小可,\n人行忠正帝王宣";
		charms[1] = "鯨魚未變守江河,\n不可昇騰更望高,\n異日崢嶸身變化,\n許君一躍跳龍門";
		rnd = new Random();
    }
    
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView =  inflater.inflate(R.layout.fragment_charm, null);
		gestureDetector = new GestureDetectorCompat(getActivity(), gestureListener);
		rootView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});
		
		charmIdx = rnd.nextInt(charms.length);
		txtText = (TextView) rootView.findViewById(R.id.tvCharm);
		txtText.setText(charms[charmIdx]);
		imgHotspot = (ImageView) rootView.findViewById(R.id.imgHotspot);
		return rootView;
	}

	private boolean isWithinBound(int x, int y) {

		  // Copy the idea from http://blahti.wordpress.com/2012/06/26/images-with-clickable-areas/
		  // Enables or disables the drawing cache. When the drawing cache is enabled, 
		  // the next call to getDrawingCache() or buildDrawingCache() will draw the view in a bitmap. 
		  imgHotspot.setDrawingCacheEnabled(true); 
		  Bitmap hotspots = Bitmap.createBitmap(imgHotspot.getDrawingCache()); 
		  imgHotspot.setDrawingCacheEnabled(false);
		  int pixel = hotspots.getPixel(x, y);
		  
		  // check the pixel is close to cyan
		  return closeMatch(Color.CYAN, pixel, COLOR_TOLERANCE);
	}

	private boolean closeMatch (int color1, int color2, int tolerance) {
		 if ((int) Math.abs (Color.red (color1) - Color.red (color2)) > tolerance ) 
		    return false;
		 if ((int) Math.abs (Color.green (color1) - Color.green (color2)) > tolerance ) 
		    return false;
		 if ((int) Math.abs (Color.blue (color1) - Color.blue (color2)) > tolerance ) 
		    return false;
		 return true;
	} // end match
	
}
