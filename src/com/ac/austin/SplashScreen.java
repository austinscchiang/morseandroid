package com.ac.austin;

import android.view.animation.*;
import android.widget.ImageView;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

public class SplashScreen extends Activity{
	private final int SPLASH_DISPLAY_LENGTH = 1500;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		final Animation bounce = AnimationUtils.loadAnimation(SplashScreen.this, R.animator.logo_bounce);
		final ImageView logo = (ImageView)findViewById(R.id.logoImage);
		logo.startAnimation(bounce);
		new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
		new Handler().postDelayed(new Runnable(){
			public void run(){}
			
		}, SPLASH_DISPLAY_LENGTH);
	}
	

}
