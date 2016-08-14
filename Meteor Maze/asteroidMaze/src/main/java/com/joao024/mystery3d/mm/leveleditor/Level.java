package com.joao024.mystery3d.mm.leveleditor;


import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Level {
	
	public static String xml = "";
	
	/* Not implemented yet */
	public static boolean isCameraFollowsMissle = true;
	public static boolean isBuiltIn = false;

	//Satellites, walls, asteroids
	public  static List<S_Satellite> satellites = new ArrayList<S_Satellite>();
	public  static List<S_Wall>      walls      = new ArrayList<S_Wall>();
	public  static List<S_Asteroid>  asteroids  = new ArrayList<S_Asteroid>();
	public  static List<S_Trigger>   triggers   = new ArrayList<S_Trigger>();
	public  static List<S_Powerup>   powerups   = new ArrayList<S_Powerup>();
	public  static List<S_Warp>  warps  = new ArrayList<S_Warp>();
	
	
	public static Vector2 missleOrigin =  new Vector2(0,0);
	public static float missleOriginAngle = 0.0f;
	
	
	public static Vector2 earthOrigin  = new Vector2(200,200);
	public static String fileName="";
	public static String creatorId = "";
	public static String uuid = UUID.randomUUID().toString();
	
	
	
	public static void reset(){
		satellites = new ArrayList<S_Satellite>();
		walls      = new ArrayList<S_Wall>();
		asteroids  = new ArrayList<S_Asteroid>();
		triggers   = new ArrayList<S_Trigger>();
		powerups   = new ArrayList<S_Powerup>();
		warps  = new ArrayList<S_Warp>();
		
		missleOrigin = new Vector2(0,0);
		missleOriginAngle = 0.0f;
		earthOrigin  = new Vector2(200,200);
		fileName="";
		creatorId = "";
		uuid = UUID.randomUUID().toString();
		isBuiltIn = false;
		isCameraFollowsMissle = true;
	}
	
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("summary: satellites:"+satellites.size()+" walls:"+walls.size()+" asteroids:"+asteroids.size());
		sb.append(" uuid="+uuid);
		sb.append(" creatorId="+creatorId);
		sb.append(" fileName="+fileName);
		sb.append(", missleOrigin="+missleOrigin.toString());
		sb.append(", earthOrigin="+earthOrigin.toString());
		sb.append(", isCameraFollowsMissle="+isCameraFollowsMissle);
		sb.append(", satellites: {");
		for(int i=0 ;i<satellites.size();i++){
			sb.append("[");
			sb.append(satellites.get(i).x);
			sb.append(",");
			sb.append(satellites.get(i).y);
			sb.append("] ");
		}
		sb.append("}");
		sb.append(", walls: {");
		for(int i=0 ;i<walls.size();i++){
			sb.append("[");
			sb.append(walls.get(i).x);
			sb.append(",");
			sb.append(walls.get(i).y);
			sb.append("] ");
			sb.append("rot=");
			sb.append(walls.get(i).rotation);
			sb.append(" ");
		}
		sb.append("}");
		
		sb.append(", asteroids: {");
		for(int i=0 ;i<asteroids.size();i++){
			sb.append("[");
			sb.append(asteroids.get(i).x);
			sb.append(",");
			sb.append(asteroids.get(i).y);
			sb.append("] ");
		}
		sb.append("}");
		
		
		
		return sb.toString();
	}
	
}
