package com.steves.bachelor.party;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class splashScreen extends Activity {
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splashscreen);

        new Handler().postDelayed(new Runnable(){ 
            public void run() { 
                 /* Create an Intent that will start the Main WordPress Activity. */ 
                 Intent mainIntent = new Intent(splashScreen.this,larp.class); 
                 splashScreen.this.startActivity(mainIntent); 
                 splashScreen.this.finish(); 
            } 
       }, 2900); //2900 for release
        
}
}



