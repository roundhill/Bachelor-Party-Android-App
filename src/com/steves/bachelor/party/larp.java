package com.steves.bachelor.party;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class larp extends ListActivity implements SensorEventListener{
    /** Called when the activity is first created. */
	public ArrayList<CharSequence> textArray = new ArrayList<CharSequence>();
	public ArrayList<CharSequence> loadTextArray = new ArrayList<CharSequence>();
	long[] checkedQuests;
	private SensorManager sensorMgr;    
    private long lastUpdate = -1;
    private float x, y, z;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 1700;
    boolean showingSteve = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        final ListView lv = getListView();
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); 
        lv.setItemsCanFocus(false);
        
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        boolean accelSupported = sensorMgr.registerListener(this,sensorMgr.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
   
       if (!accelSupported) {
    	   // non accelerometer on this device
    	   sensorMgr.unregisterListener(this); 
       }
        
        loadQuests();
        
        updateScore();
    }
    
 
    private void loadQuests() {
        loadTextArray.clear();
        larpDB larpDB = new larpDB(this);
    	Vector questsVector = larpDB.getQuests(this);
    	Vector checked = new Vector();
    	if (questsVector.size() > 0)
    	{
    		checkedQuests = new long[questsVector.size()];
	    	for(int i=0; i < questsVector.size(); i++)
	        {
	    		HashMap rowQuest = (HashMap) questsVector.get(i);
	    		loadTextArray.add(rowQuest.get("quest").toString());
	    		if (rowQuest.get("completed").toString().equals("1")){
	    			checked.add(i);
	    		}
	        }
	    	
	        ArrayAdapter<CharSequence> quests = new ArrayAdapter<CharSequence>(this, R.layout.categories_row, loadTextArray);
	        
	          //categories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	          
	        this.setListAdapter(quests);
	        
	        final ListView listView = getListView();
	        
	        for (int x=0;x < checked.size(); x++){
	    		int rowID = Integer.parseInt(checked.get(x).toString());
	    		listView.setItemChecked(rowID, true);
	    	}
	        
	        listView.setOnItemClickListener(new OnItemClickListener() {
				   

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int lineID, long arg3) {
					
					larpDB larpDB = new larpDB(larp.this);
					CheckedTextView ctv = (CheckedTextView) arg1;
					
					if (lineID == 15){
						String msisdn = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
						if (msisdn == null){
							msisdn = "";
						}
						if (msisdn.equals("12345678")){ //The bachelor's phone number :)
							larpDB.updateQuestStatus(larp.this, lineID, !ctv.isChecked());
							Toast.makeText(larp.this, "Welcome, Steve! YOU KICK ASS! 100,000 POINTS ADDED!!!", Toast.LENGTH_LONG).show();
						}
						else{
							Toast.makeText(larp.this, "You're not Steve, asshole!", Toast.LENGTH_LONG).show();
							listView.setItemChecked(15, false);
						}
					}
					else{
						larpDB.updateQuestStatus(larp.this, lineID, !ctv.isChecked());
					}
					
					//Toast.makeText(larp.this, "Index: " + lineID, Toast.LENGTH_SHORT).show();
					
					updateScore();
				}

            });
	        

    	}
		
	}


	protected void updateScore() {
		larpDB larpDB = new larpDB(larp.this);
		
		int score = larpDB.getScore(larp.this);
		TextView tv = (TextView) findViewById(R.id.score);
		tv.setText("Awesome Meter: " + score);
		
	}


	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}


	public void onSensorChanged(SensorEvent event) {
		Sensor mySensor = event.sensor;
		       	if (mySensor.getType() == SensorManager.SENSOR_ACCELEROMETER) {
		       		long curTime = System.currentTimeMillis();
		       	    // only allow one update every 100ms.
		       	    if ((curTime - lastUpdate) > 100) {
		   	    		long diffTime = (curTime - lastUpdate);
		   	    		lastUpdate = curTime;
		  	    		
		  	    		x = event.values[SensorManager.DATA_X];
		  	    		y = event.values[SensorManager.DATA_Y];
		  	    		z = event.values[SensorManager.DATA_Z];
		  	    		
		  	    		float speed = Math.abs(x+y+z - last_x - last_y - last_z)/ diffTime * 10000;
		 	    		if (speed > SHAKE_THRESHOLD) {
		 	    			// yes, this is a shake action! Do something about it!
		 	    			showSteve();
		 	    			//Toast.makeText(larp.this, "SHAKED!", Toast.LENGTH_SHORT).show();
		 	    		}
		  	    		last_x = x;
		  	    		last_y = y;
		  	    		last_z = z;
		      	    }
		      	}  
		
	}


	private void showSteve() {
		// TODO Auto-generated method stub
		if (!showingSteve){
			Intent i = new Intent(larp.this, showSteve.class);
			startActivityForResult(i, 0);
			showingSteve = true;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK){
			showingSteve = false;
		}

	}
	
	@Override
	protected void onPause() {
		if (sensorMgr != null) {
		    sensorMgr.unregisterListener(this);
		    sensorMgr = null;
	        }
		super.onPause();
	    }
	
	@Override
	protected void onResume() {
	    super.onResume();
	    sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        boolean accelSupported = sensorMgr.registerListener(this,sensorMgr.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
   
       if (!accelSupported) {
    	   // non accelerometer on this device
    	   sensorMgr.unregisterListener(this); 
       }
	}

	@Override
 	public boolean onCreateOptionsMenu(Menu menu) {
 		super.onCreateOptionsMenu(menu);
 		menu.add(0, 0, 0, "Show Me The Steve!");
 		MenuItem menuItem1 = menu.findItem(0);
 		menuItem1.setIcon(R.drawable.icon);
 		return true;
 	}
	
	@Override
 	public boolean onOptionsItemSelected(final MenuItem item){
 		switch (item.getItemId()) {
 		case 0:
 			showSteve();
 		}
 		return true;
	}
    
}