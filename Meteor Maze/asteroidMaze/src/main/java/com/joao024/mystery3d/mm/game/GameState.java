package com.joao024.mystery3d.mm.game;

import org.andengine.entity.Entity;

import java.util.ArrayList;

public class GameState {
	
	
	private static GameState gs = new GameState();
	private ArrayList<Entity> sat = new ArrayList<Entity>();
	
	
	public void addSat(Entity s){
		sat.add(s);
	}
	public void remSat(Entity s){
		sat.remove(s);
	}
	public Entity getSatByName(String name){
		ObjectMeta om;
		for(int i=0;i<sat.size();i++){
			om = (ObjectMeta)sat.get(i).getUserData();
			if(om.getName().equalsIgnoreCase(name)){
				return sat.get(i);
			}
		}
		return null;
	}
	
	public static GameState instance(){
		return GameState.gs;
	}

}
