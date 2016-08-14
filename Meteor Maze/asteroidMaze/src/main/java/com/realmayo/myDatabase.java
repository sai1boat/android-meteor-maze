package com.realmayo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.joao024.mystery3d.mm.game.Globals;
import com.joao024.mystery3d.mm.leveleditor.Level;

import java.util.ArrayList;

public class myDatabase extends SQLiteOpenHelper {

	static final String dbName = "asteroidMaze.db";
	static final String tLevels = "Levels";
	static final String fUUID = "uuid";
	static final String fFilename = "filename";
	static final String fIsBuiltIn = "isBuiltIn";
	static final String fCreatorId = "creatorId";
	static final String fLevelXml   = "levelXml";
	static final String fVersion    = "version";
	
	static final String tULevels = "UserLevels";
	static final String fUL_UUID = "uuid";
	static final String fUL_Filename = "filename";
	static final String fUL_IsBuiltIn = "isBuiltIn";
	static final String fUL_CreatorId = "creatorId";
	static final String fUL_LevelXml   = "levelXml";
	static final String fUL_Version    = "version";
	
	
	static final String tScores = "Scores";
	static final String fLevelUUID = "levelUUID";
	static final String fPointScore= "pointScore";
	static final String fCoinScore = "coinScore";
	static final String fCoinsMax  = "coinsMax";
	static final String fAsteroids = "asteroids";
	static final String fAsteroidsMax = "asteroidsMax";
	static final String fMultiplier = "multiplier";
	static final String fAccuracy   = "accuracy";
	static final String fFinishTimeInMilliSeconds = "finishimeInMilliSeconds";
	static final String fSpaceshipUsed       = "spaceshipUsed";
	
	
	/*
	static final String tScores = "Scores";
	static final String fType      = "type";       //code.missionComplete, 
	static final String fSpaceship = "spaceship";  //spaceship used at the time of milestone
	static final String fWeapon    = "weapon";     //weapon used at the time of milestone
	static final String fScore     = "score";
	static final String fLevelUUID = "levelUUID";  //Level where milestone was achieved
	*/
	public myDatabase(Context context) {
// THE VALUE OF 1 ON THE NEXT LINE REPRESENTS THE VERSION NUMBER OF THE DATABASE
// IN THE FUTURE IF YOU MAKE CHANGES TO THE DATABASE, YOU NEED TO INCREMENT THIS NUMBER
// DOING SO WILL CAUSE THE METHOD onUpgrade() TO AUTOMATICALLY GET TRIGGERED
		super(context, dbName, null, 8);
		
		
		if(Globals.instance().isDebug()){
			printScoreDB();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
// ESTABLISH NEW DATABASE TABLES IF THEY DON'T ALREADY EXIST IN THE DATABASE
		db.execSQL("CREATE TABLE IF NOT EXISTS "+tLevels+" (" +
					fUUID + " TEXT PRIMARY KEY , " +
					fFilename + " TEXT, " +
					fIsBuiltIn + " TEXT, " +
					fCreatorId + " TEXT, " + 
					fLevelXml   + " CLOB, " +
					fVersion + " INTEGER " +
					")");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS "+tULevels+" (" +
				fUL_UUID + " TEXT PRIMARY KEY , " +
				fUL_Filename + " TEXT, " +
				fUL_IsBuiltIn + " TEXT, " +
				fUL_CreatorId + " TEXT, " + 
				fUL_LevelXml   + " CLOB, " +
				fUL_Version + " INTEGER " +
				")");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS "+tScores+" (" +
				fLevelUUID + " TEXT, " +
				fPointScore+ " INTEGER, " +
				fCoinScore + " INTEGER, " +
				fCoinsMax  + " INTEGER, " +
				fAsteroids + " INTEGER, " +
				fAsteroidsMax + " INTEGER, " +
				fMultiplier + " INTEGER, " +
				fAccuracy   + " TEXT, " +
				fFinishTimeInMilliSeconds + " TEXT, " +
				fSpaceshipUsed + " TEXT ) " );
		
		
		/*
		db.execSQL("CREATE TABLE IF NOT EXISTS "+tScores+" (" +
					fType + "TEXT, " +
					fSpaceship + "TEXT, " +
					fWeapon + " TEX, " +
					fScore + " TEXT, " +
					fLevelUUID + " TEXT )");
		*/
	
// OPTIONALLY PREPOPULATE THE TABLE WITH SOME VALUES	
		/*
		 ContentValues cv = new ContentValues();
			cv.put(fUUID, 1);
			cv.put(fLevelUnLocked, "true");
			cv.put(fLevelBeat, "false");
			cv.put(fLevelScore, "0");
				db.insert(tLevels, null, cv);
			cv.put(fUUID, 2);
			cv.put(fLevelUnLocked, "false");
			cv.put(fLevelBeat, "false");
			cv.put(fLevelScore, "0");
				db.insert(tLevels, null, cv);
			cv.put(fUUID, 3);
			cv.put(fLevelUnLocked, "false");
			cv.put(fLevelBeat, "false");
			cv.put(fLevelScore, "0");
				db.insert(tLevels, null, cv);
			*/		
/*		
 * MORE ADVANCED EXAMPLES OF USAGE
 * 
		db.execSQL("CREATE TRIGGER fk_empdept_deptid " +
				" BEFORE INSERT "+
				" ON "+employeeTable+				
				" FOR EACH ROW BEGIN"+
				" SELECT CASE WHEN ((SELECT "+colDeptID+" FROM "+deptTable+" WHERE "+colDeptID+"=new."+colDept+" ) IS NULL)"+
				" THEN RAISE (ABORT,'Foreign Key Violation') END;"+
				"  END;");
		
		db.execSQL("CREATE VIEW "+viewEmps+
				" AS SELECT "+employeeTable+"."+colID+" AS _id,"+
				" "+employeeTable+"."+colName+","+
				" "+employeeTable+"."+colAge+","+
				" "+deptTable+"."+colDeptName+""+
				" FROM "+employeeTable+" JOIN "+deptTable+
				" ON "+employeeTable+"."+colDept+" ="+deptTable+"."+colDeptID
				);
*/				
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// THIS METHOD DELETES THE EXISTING TABLE AND THEN CALLS THE METHOD onCreate() AGAIN TO RECREATE A NEW TABLE
// THIS SERVES TO ESSENTIALLY RESET THE DATABASE
// INSTEAD YOU COULD MODIFY THE EXISTING TABLES BY ADDING/REMOVING COLUMNS/ROWS/VALUES THEN NO EXISTING DATA WOULD BE LOST
		db.execSQL("DROP TABLE IF EXISTS "+tLevels);
		db.execSQL("DROP TABLE IF EXISTS "+tScores); //might want to migrate :(
		onCreate(db);
	}
	
	/*
	 * Delete all levels from local database except where the user is
	 * the creator. This will save users progress in building his
	 * Mazes and not wipe his data he hasn't submitted to the cloud.
	 */
	public void truncateLevels(){
		SQLiteDatabase myDB = this.getWritableDatabase();
		//myDB.execSQL("DELETE FROM "+tLevels+" WHERE "+fCreatorId+"!='"+LevelUtils.getCreatorId()+"'");
		myDB.execSQL("DELETE FROM "+tLevels);
		//myDB.close();
	}
	
	public void truncateUserLevels(){
		SQLiteDatabase myDB = this.getWritableDatabase();
		//myDB.execSQL("DELETE FROM "+tLevels+" WHERE "+fCreatorId+"!='"+LevelUtils.getCreatorId()+"'");
		myDB.execSQL("DELETE FROM "+tULevels);
		//myDB.close();
		
	}
	
	public String[] getLevelList(){
		ArrayList<String> l = new ArrayList<String>();
		SQLiteDatabase myDB = this.getReadableDatabase();
		Cursor myCursor = myDB.rawQuery("SELECT " + fFilename + " FROM " +tLevels, null);
		while (myCursor.moveToNext()){
			l.add(myCursor.getString(myCursor.getColumnIndex(fFilename)));
		}
		myCursor.close();
		//myDB.close();
		
		String[] out = new String[l.size()];
		for(int i = 0; i<l.size(); i++){
			out[i] = l.get(i).toString();
		}
		return out;
	}
	
	/*
	 * IF deviceID passed in is null, it will return all user levels regardless of user.
	 */
	public String[] getUserLevelList(String userDeviceId){
		ArrayList<String> l = new ArrayList<String>();
		SQLiteDatabase myDB = this.getReadableDatabase();
		Cursor myCursor = null;
		
		if(userDeviceId!=null){
			myCursor = myDB.rawQuery("SELECT " + fUL_Filename + " FROM " + tULevels + " WHERE "+fCreatorId+" = ?", new String[]{userDeviceId});
		}
		else {
			//myCursor = myDB.rawQuery("SELECT " + fUL_Filename + " FROM " + tULevels + " WHERE "+fCreatorId+" != ?", new String[]{LevelUtils.getCreatorId()});
			myCursor = myDB.rawQuery("SELECT " + fUL_Filename + " FROM " + tULevels, null);
		}
		while (myCursor.moveToNext()){
			l.add(myCursor.getString(myCursor.getColumnIndex(fUL_Filename)));
		}
		myCursor.close();
		//myDB.close();
		
		String[] out = new String[l.size()];
		for(int i = 0; i<l.size(); i++){
			out[i] = l.get(i).toString();
		}
		return out;
	}
	
	
	public String getLevelXMLByFileName(String filename){
		SQLiteDatabase myDB = this.getReadableDatabase();
		 String[] params = new String[]{String.valueOf(filename)};
		 Cursor myCursor = myDB.rawQuery("SELECT "+ fLevelXml +" FROM "+ tLevels +" WHERE "+ fFilename +"=?",params);
		 myCursor.moveToFirst();
		 int index = myCursor.getColumnIndex(fLevelXml);
		 String theXml = myCursor.getString(index);
		 myCursor.close();
		 //myDB.close();
		 return theXml;
	}
	
	public String getUserLevelXMLByFileName(String filename){
		SQLiteDatabase myDB = this.getReadableDatabase();
		 String[] params = new String[]{String.valueOf(filename)};
		 Cursor myCursor = myDB.rawQuery("SELECT "+ fUL_LevelXml +" FROM "+ tULevels +" WHERE "+ fUL_Filename +"=?",params);
		 myCursor.moveToFirst();
		 int index = myCursor.getColumnIndex(fUL_LevelXml);
		 String theXml = myCursor.getString(index);
		 myCursor.close();
		// myDB.close();
		 return theXml;
	}
	/*
	public String insertOrUpdateLevel(){
		SQLiteDatabase myDB = this.getReadableDatabase();
		String[] p = new String[]{Level.fileName, Level.uuid};
		Cursor myC = myDB.rawQuery("SELECT count(*) FROM "+tLevels+" WHERE LOWER("+fFilename+")=LOWER(?) AND "+fUUID+"!=?" , p);
		myC.moveToFirst();
		int countNameMatches = myC.getInt(0);
		myC.close();
		if(countNameMatches>0){
			myDB.close();
			return "Duplicate file name. Please choose another name.";
		}
		
		String[] params = new String[]{String.valueOf(Level.uuid)};
		Cursor myCursor = myDB.rawQuery("SELECT count(*) FROM " + tLevels +" WHERE " +fUUID+ "=?", params);
		myCursor.moveToFirst();
		int count = myCursor.getInt(0);
		myCursor.close();
		myDB.close();
		if(count==0){
			insertLevel();
		}
		else{
			updateLevel();
		}
		
		return "SUCCESS";
	}*/
	
	public String insertOrUpdateUserLevel(){
		SQLiteDatabase myDB = this.getReadableDatabase();
		String[] p = new String[]{Level.fileName, Level.uuid};
		Cursor myC = myDB.rawQuery("SELECT count(*) FROM "+tULevels+" WHERE LOWER("+fUL_Filename+")=LOWER(?) AND "+fUL_UUID+"!=?" , p);
		myC.moveToFirst();
		int countNameMatches = myC.getInt(0);
		myC.close();
		if(countNameMatches>0){
			//myDB.close();
			return "Duplicate file name. Please choose another name.";
		}
		
		String[] params = new String[]{String.valueOf(Level.uuid)};
		Cursor myCursor = myDB.rawQuery("SELECT count(*) FROM " + tULevels +" WHERE " +fUL_UUID+ "=?", params);
		myCursor.moveToFirst();
		int count = myCursor.getInt(0);
		myCursor.close();
		//myDB.close();
		if(count==0){
			insertUserLevel();
		}
		else{
			updateUserLevel();
		}
		
		return "SUCCESS";
	}
	
	public void insertLevel(){
		SQLiteDatabase myDB = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(fCreatorId, Level.creatorId);
		cv.put(fFilename, Level.fileName);
		cv.put(fIsBuiltIn, Level.isBuiltIn);
		cv.put(fLevelXml, Level.xml);
		cv.put(fUUID, Level.uuid);
		myDB.insert(tLevels, null, cv);
		//myDB.close();
	}
	
	public void insertUserLevel(){
		SQLiteDatabase myDB = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(fUL_CreatorId, Level.creatorId); //gmail username
		cv.put(fUL_Filename, Level.fileName);
		cv.put(fUL_IsBuiltIn, Level.isBuiltIn);
		cv.put(fUL_LevelXml, Level.xml);
		cv.put(fUL_UUID, Level.uuid);
		myDB.insert(tULevels, null, cv);
		//myDB.close();
	}
	
	/*
	public void updateLevel(){
		SQLiteDatabase myDB = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(fFilename, Level.fileName);
		cv.put(fIsBuiltIn,  Level.isBuiltIn);
		cv.put(fLevelXml,  Level.xml);
		myDB.update(tLevels, cv, fUUID+"=?", new String[]{Level.uuid});
		myDB.close();
	}*/
	
	public void updateUserLevel(){
		SQLiteDatabase myDB = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(fUL_Filename, Level.fileName);
		cv.put(fUL_IsBuiltIn,  Level.isBuiltIn);
		cv.put(fUL_LevelXml,  Level.xml);
		myDB.update(tULevels, cv, fUL_UUID+"=?", new String[]{Level.uuid});
		//myDB.close();
	}
	
	
	/*
	public void deleteLevel(){
		SQLiteDatabase myDB = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(fUUID, Level.uuid);
		myDB.delete(tLevels, fUUID+"=?", new String[]{Level.uuid});
		myDB.close();
	}*/
	
	public void deleteUserLevel(){
		SQLiteDatabase myDB = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(fUL_UUID, Level.uuid);
		myDB.delete(tULevels, fUL_UUID+"=?", new String[]{Level.uuid});
		//myDB.close();
	}
	
	
	public void insertScore(int pointScore, int coinScore, int coinsMax, int multiplier, int accuracy, int asteroidsHit, int finishTimeInMilliSeconds, String spaceshipUsed){
		SQLiteDatabase myDB = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(fPointScore, pointScore); 
		cv.put(fCoinScore, coinScore);
		cv.put(fCoinsMax, coinsMax);
		cv.put(fAsteroids, asteroidsHit);
		cv.put(fAsteroidsMax, Level.powerups.size());
		cv.put(fMultiplier, multiplier);
		cv.put(fAccuracy, accuracy);
		cv.put(fFinishTimeInMilliSeconds, finishTimeInMilliSeconds);
		cv.put(fLevelUUID, Level.uuid);
		cv.put(fSpaceshipUsed, spaceshipUsed);
		myDB.insert(tScores, null, cv);
		//myDB.close();
	}
	/*
	
	fLevelUUID + " TEXT, " +
	fPointScore+ " INTEGER, " +
	fCoinScore + " INTEGER, " +
	fCoinsMax  + " INTEGER, " +
	fAsteroids + " INTEGER, " +
	fAsteroidsMax + " INTEGER, " +
	fMultiplier + " INTEGER, " +
	fFinishTimeInMilliSeconds + " TEXT, " +
	fSpaceshipUsed + " TEXT ) " );
	*/
	
	public String getSynopsisForLevel(String levelUuid){
		SQLiteDatabase myDB = this.getWritableDatabase();
		Cursor out = myDB.rawQuery("SELECT count(*) FROM "+tScores+" WHERE "+fLevelUUID+"=?", new String[]{levelUuid});
		int attempts = 0;
		if(out.moveToFirst()){
			attempts = out.getInt(0);
		}
		out.close();
		
		int maxScoreForLevel = 0;
		
		Cursor out1 = myDB.rawQuery("SELECT MAX("+fPointScore+") FROM "+tScores+" WHERE "+fLevelUUID+"=?", new String[]{levelUuid});
		if(out1.moveToFirst()){
			maxScoreForLevel = out1.getInt(0);
		}
		out1.close();
		
		return "Attempts: "+attempts+"\nHigh Score: "+maxScoreForLevel;
		
		
		
	}
	
	private void printScoreDB(){
		SQLiteDatabase myDB = this.getReadableDatabase();
		
		Cursor out = myDB.rawQuery("SELECT * FROM "+tScores, new String[]{});
		out.moveToFirst();
		while(out.moveToNext()){
			Log.w("myDatabase:printScoreDB", out.getString(0)+", "+out.getInt(1)+", "
					+out.getInt(2)+", "+out.getInt(3)+", "+out.getInt(4)+", "+out.getInt(5)+", "+out.getInt(6)+", "
					+out.getString(7)+", "+out.getString(8)+", "+out.getString(9));
		}
		out.close();
		//myDB.close();
	}
	
	public int getScoreRating(String levelName){
		int finalRating = 0;
		SQLiteDatabase myDB = this.getReadableDatabase();
		String[] p = new String[]{levelName};
		Cursor myC = myDB.rawQuery("SELECT "+fUUID+" FROM "+tLevels+" WHERE LOWER("+fFilename+")=LOWER(?)" , p);
		if(myC.moveToFirst()){
			String levelUUID = myC.getString(0);
			
	
			String[] q = new String[]{levelUUID};
			
			//3 star rating if asteroids ==100% and debris>=20%
			Cursor rating = myDB.rawQuery("SELECT CASE WHEN "+fAsteroids+"/" + fAsteroidsMax + "=1.0 AND "+fAccuracy+">=200 THEN 2 " +
			" WHEN " +fAsteroids+"/"+fAsteroidsMax+"=1.0 THEN 1 ELSE 0 END AS rating FROM "+tScores+" WHERE "+fLevelUUID+" = ? ORDER BY rating DESC", q);
			
			
			
			
			if(rating.getCount()==0){
				finalRating = 0;
			}
			else {
				rating.moveToFirst();
				finalRating = rating.getInt(0);
			}
			
		

		
		}
		
		myC.close();
		
		//myDB.close();
		return finalRating;
	}
	

}
