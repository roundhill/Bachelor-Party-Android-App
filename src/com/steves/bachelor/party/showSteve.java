package com.steves.bachelor.party;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class showSteve extends Activity{
    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.showsteve);
        
        ImageView stevePic = (ImageView) findViewById(R.id.stevePic);
        
        int randomPic = 1 + (int)(Math.random() * ((4 - 1) + 1));
        
        switch (randomPic){
        
        case 1:
        	stevePic.setBackgroundDrawable(getResources().getDrawable(R.drawable.steve1));
        	break;
        case 2:
        	stevePic.setBackgroundDrawable(getResources().getDrawable(R.drawable.steve2));
        	break;
        case 3:
        	stevePic.setBackgroundDrawable(getResources().getDrawable(R.drawable.steve3));
        	break;
        case 4:
        	stevePic.setBackgroundDrawable(getResources().getDrawable(R.drawable.steve4));
        	break;
        }
        
        stevePic.setVisibility(View.VISIBLE);
        
        new Handler().postDelayed(new Runnable(){ 
            public void run() { 
            	Bundle bundle = new Bundle();
				bundle.putString("returnStatus", "OK");
				Intent mIntent = new Intent();
				mIntent.putExtras(bundle);
				setResult(RESULT_OK, mIntent);
				finish(); 
            } 
       }, 5000); 
    }
    
    @Override public boolean onKeyDown(int i, KeyEvent event) {

		// only intercept back button press
		if (i == KeyEvent.KEYCODE_BACK) {

			Bundle bundle = new Bundle();
			bundle.putString("returnStatus", "OK");
			Intent mIntent = new Intent();
			mIntent.putExtras(bundle);
			setResult(RESULT_OK, mIntent);
			finish(); 

		}

		return false; // propagate this keyevent
	}
   
    
}