package com.joao024.mystery3d.mm.game;



import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Globals {

	public static final String ACHIEVEMENT_PERFECTION = "CgkI0P6zkPwWEAIQBg";
	public static final int ACHIEVEMENT_POWERUPS_MASTER = 18338;
	public static final int ACHIEVEMENT_NO_DEATHS      = 18340;
	public static final String ACHIEVEMENT_NO_DEATHS_10   = "CgkI0P6zkPwWEAIQBQ";
	public static final String ACHIEVEMENT_NO_DEATHS_20   = "CgkI0P6zkPwWEAIQBA";
	public static final String ACHIEVEMENT_NO_DEATHS_40   = "CgkI0P6zkPwWEAIQAw";
	
	public static final String[] pointsToUnlockAchievements=new String[]{"pointsToUnlockMazeBuilder",
															            "pointsToUnlockMazeBuilderWarp",
															            "pointsToUnlockSpaceship3",
															            "pointsToUnlockSpaceship2",
															            "pointsToUnlockSpaceship1",
															            "pointsToUnlockSpaceship0Weapon0",
															            "pointsToUnlockSpaceship0Weapon1",
															            "pointsToUnlockSpaceship0Weapon2",
															            "pointsToUnlockSpaceship1Weapon0",
															            "pointsToUnlockSpaceship1Weapon1",
															            "pointsToUnlockSpaceship1Weapon2",
															            "pointsToUnlockSpaceship2Weapon0",
															            "pointsToUnlockSpaceship2Weapon1",
															            "pointsToUnlockSpaceship2Weapon2"
            };

	

	private static Globals g = new Globals();
	
	
	private String postUrl = "http://asteroidmaze-jwow.rhcloud.com/oortcloud.php";
	private String getUrl  = "http://asteroidmaze-jwow.rhcloud.com/oortcloud_get.php";
	private String voteUrl = "http://asteroidmaze-jwow.rhcloud.com/oortcloud_vote.php";
	
	
	private String postUserUrl = "http://asteroidmaze-jwow.rhcloud.com/useroortcloud.php";
	private String getUserUrl  = "http://asteroidmaze-jwow.rhcloud.com/useroortcloud_get.php";
	private String voteUserUrl = "http://asteroidmaze-jwow.rhcloud.com/useroortcloud_vote.php";
	
	private String getFileNameUrl = "http://asteroidmaze-jwow.rhcloud.com/useroortcloud_getfname.php";
	
	
	private static boolean debug = false;
	private static boolean isLogFPS = false;
	private static boolean isDebugDrawingEnabled = false;

	
	private static boolean isDebrisSmokeTrails = true;
	
	
	

	//Whether user is playing a game within 
	//the main game or the community section.
	private static boolean isBuiltIn = false;
	
	
	 enum Difficulty{
		 EASY,MEDIUM,HARD
	 }
	 
	public Difficulty difficulty;
	
	
	private static SharedPreferences prefs;
	
	

	
	public void initPrefs(Activity a){
		

		prefs = PreferenceManager.getDefaultSharedPreferences(a);
		//Globals.instance().getPrefs().edit().putInt("difficulty", 0);
		int whichDifficulty = prefs.getInt("difficulty", 0);
		switch(whichDifficulty){
			case 0:
				difficulty = Difficulty.EASY;
				break;
			case 1:
				difficulty = Difficulty.MEDIUM;
				break;
			case 2:
				difficulty = Difficulty.HARD;
				break;
				default:
					break;
		}
	

		prefs.edit().putInt("pointsToUnlockMazeBuilder", 10000000).commit();
		prefs.edit().putInt("pointsToUnlockMazeBuilderWarp", prefs.getInt("pointsToUnlockSpaceship1", 0)+20000).commit();
		
		
		prefs.edit().putInt("pointsToUnlockSpaceship1", 500000).commit();
		prefs.edit().putInt("pointsToUnlockSpaceship2", 4000000).commit();
		prefs.edit().putInt("pointsToUnlockSpaceship3", 10000000).commit();
		
		prefs.edit().putInt("pointsToUnlockSpaceship0Weapon0", 1000).commit();
		prefs.edit().putInt("pointsToUnlockSpaceship0Weapon1", 2000000).commit();
		prefs.edit().putInt("pointsToUnlockSpaceship0Weapon2", 40000000).commit();
		
		prefs.edit().putInt("pointsToUnlockSpaceship1Weapon0", prefs.getInt("pointsToUnlockSpaceship1", 0)+50000).commit();
		prefs.edit().putInt("pointsToUnlockSpaceship1Weapon1", prefs.getInt("pointsToUnlockSpaceship1", 0)+200000).commit();
		prefs.edit().putInt("pointsToUnlockSpaceship1Weapon2", prefs.getInt("pointsToUnlockSpaceship1", 0)+400000).commit();
		
		prefs.edit().putInt("pointsToUnlockSpaceship2Weapon0", prefs.getInt("pointsToUnlockSpaceship2", 0)+50000).commit();
		prefs.edit().putInt("pointsToUnlockSpaceship2Weapon1", prefs.getInt("pointsToUnlockSpaceship2", 0)+200000).commit();
		prefs.edit().putInt("pointsToUnlockSpaceship2Weapon2", prefs.getInt("pointsToUnlockSpaceship2", 0)+400000).commit();
	}
	
	public void savePref(String key, String value){
		if(prefs!=null){
			prefs.edit().putString(key, value).commit();
			
		}
	}
	
	public String getPref(String key){
		return prefs.getString(key, null);
	}
	
	public SharedPreferences getPrefs(){
		return prefs;
	}
	
	public static Globals instance(){
		return Globals.g;
	}
	
	public boolean isDebug(){
		return Globals.debug;
	}
	
	public void setIsDebug(boolean b){
		Globals.debug = b;
	}
	
	public boolean isLogFPS(){
		return Globals.isLogFPS;
	}

	public String getPostUrl() {
		return postUrl;
	}
	
	public String getUserPostUrl(){
		return postUserUrl;
	}

	public String getGetUrl() {
		return getUrl;
	}
	
	public String getUserGetUrl(){
		return getUserUrl;
	}

	public String getVoteUrl(){
		return this.voteUrl;
	}
	
	public String getUserVoteUrl(){
		return this.voteUserUrl;
	}
	
	public String getFileNameUrl(){
		return this.getFileNameUrl;
	}

	public boolean isDebugDrawingEnabled() {
		return isDebugDrawingEnabled;
	}
	
	public boolean isBuiltIn(){
		return isBuiltIn;
	}
	
	public void setIsBuiltIn(boolean isBuiltIn){
		Globals.isBuiltIn = isBuiltIn;
	}
	
	public boolean isDebrisSmokeTrails() {
		return isDebrisSmokeTrails;
	}

	
	
}
