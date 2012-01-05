package com.steves.bachelor.party;

import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class larpDB {

	private static final int DATABASE_VERSION = 1;
	
	private static final String CREATE_TABLE_QUESTS = "create table if not exists quests (id integer primary key autoincrement, "
			+ "quest text, points integer, completed boolean default false);";
	private static final String QUESTS_TABLE = "quests";
	private static final String DATABASE_NAME = "larp";
	
	private SQLiteDatabase db;

	public larpDB(Context ctx) {
		db = ctx.openOrCreateDatabase(DATABASE_NAME, 0, null);
		db.execSQL(CREATE_TABLE_QUESTS);
		
		if (db.getVersion() == 0){
			populateQuests();
		}
		
		db.close();	
	}
	
	public boolean populateQuests() {
		//db = ctx.openOrCreateDatabase(DATABASE_NAME, 0, null);
		//ugly ass way to initialize the quests on first run
		
		for (int i=1; i <= 18; i++){
			ContentValues values = new ContentValues();
			switch (i) {
				case 1:
					values.put("quest", "Chug & Chuck");
					values.put("points", 5);
					break;	
				case 2:
					values.put("quest", "Balancing of beer/shot on top of head for 30 seconds.");
					values.put("points", 5);
					break;
				case 3:
					values.put("quest", "Sing a song about whiskey and women.");
					values.put("points", 3);
					break;
				case 4:
					values.put("quest", "Tell the Night Elf Story at Dinner.");
					values.put("points", 2);
					break;
				case 5:
					values.put("quest", "Burn a book on relationships.");
					values.put("points", 10);
					break;
				case 6:
					values.put("quest", "Hole in one! (AT MINI-GOLF)");
					values.put("points", 3);
					break;
				case 7:
					values.put("quest", "Beat Steve at mini-golf.");
					values.put("points", 15);
					break;
				case 8:
					values.put("quest", "Ball in water hazard at mini-golf.");
					values.put("points", -5);
					break;
				case 9:
					values.put("quest", "Your best Happy Gilmore impersonation, must be crowd approved.");
					values.put("points", 3);
					break;
				case 10:
					values.put("quest", "Get Steve to do his best Night Elf /dance impersonation.");
					values.put("points", 10);
					break;
				case 11:
					values.put("quest", "Getting kicked out of any establishment (without trying).");
					values.put("points", 8);
					break;
				case 12:
					values.put("quest", "Eating Foie Gras.");
					values.put("points", 5);
					break;
				case 13:
					values.put("quest", "Quickest car-bomb consumer.");
					values.put("points", 10);
					break;
				case 14:
					values.put("quest", "Get someone to autograph a part of you.");
					values.put("points", 3);
					break;
				case 15:
					values.put("quest", "Ralphing...lolz");
					values.put("points", 7);
					break;
				case 16:
					values.put("quest", "I'm Steve!");
					values.put("points", 100000);
					break;
				case 17:
					values.put("quest", "Karoake by yourself @ one of the bars");
					values.put("points", 12);
					break;
				case 18:
					values.put("quest", "Karoake w/ others @ one of the bars");
					values.put("points", 7);
					break;
			}
			db.insert(QUESTS_TABLE, null, values);
		}
		db.setVersion(1);
		db.close();
		return (true); //lol, who cares about truth?
	}	
	public Vector getQuests(Context ctx) {
		db = ctx.openOrCreateDatabase(DATABASE_NAME, 0, null);
		Cursor c = db.query(QUESTS_TABLE, new String[] { "id", "quest", "points", "completed"}, null, null, null, null, null);
		String quest, points, completed;
		String id;
		int numRows = c.getCount();
		c.moveToFirst();
		Vector quests = new Vector();
		for (int i = 0; i < numRows; i++) {
			
			id = c.getString(0);
			quest = c.getString(1);
			points = c.getString(2);
			completed = c.getString(3);
			if (id != null)
			{	
				HashMap thisHash = new HashMap();
				thisHash.put("quest", quest);
				thisHash.put("points", points);
				thisHash.put("completed", completed);
				quests.add(thisHash);
			}
			c.moveToNext();
		}
		c.close();
		db.close();
		
		return quests;
	}

	public void updateQuestStatus(Context ctx, int id, boolean checked) {
		db = ctx.openOrCreateDatabase(DATABASE_NAME, 0, null);
		
		ContentValues values = new ContentValues();
		values.put("completed", checked);
		boolean test = db.update(QUESTS_TABLE, values, "id=" + (id + 1), null) > 0;
		int x = 1;
		x=4;
		x++;
		
	}
	
	public int getScore(Context ctx){
		
		db = ctx.openOrCreateDatabase(DATABASE_NAME, 0, null);
		SQLiteDatabase sqliteDatabase2 = null;
		Cursor cursor = db.rawQuery(
			    "SELECT SUM(points) FROM quests WHERE completed = 1", null);
			if(cursor.moveToFirst()) {
			    return cursor.getInt(0);
			}
		
		
		return 0;
	}
	

}


