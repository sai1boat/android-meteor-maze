package com.joao024.mystery3d.mm.game;

import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.entity.Entity;

import java.util.ArrayList;

/* This class holds metadata information about existing objects.
 * This will be useful for collision detection (What type is coliding with what etc.)
 * Also, we may use this to store health, points, money, and any other information we
 * need to track about the object. 
 */
public class ObjectMeta {

	private String name;
	private int    health = 100;
	private int    gas    = 100;
	private int    shield = 0;
	private Object entity;
	private Body   body;
	private ArrayList<Entity> affiliatedObj = new ArrayList<Entity>();
	
	private Object editorData;
	
	
	public Object getEditorData() {
		return editorData;
	}

	public void setEditorData(Object editorData) {
		this.editorData = editorData;
	}

	public ObjectMeta(String name, Entity e, Body b){
		
		setHealth(100);
		setGas(100);
		
		setName(name);
		setEntity(e);
		setBody(b);
	}
	
	public ArrayList<Entity> getAffiliatedObj() {
		return affiliatedObj;
	}
	public void setAffiliatedObj(ArrayList<Entity> affiliatedObj) {
		this.affiliatedObj = affiliatedObj;
	}
	
	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
	private boolean selected = false;
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String newName){
		this.name = newName;
	}
	
	public Object getEntity(){
		return this.entity;
	}
	
	public void setEntity(Object entity){
		this.entity = entity;
	}

	public int getShield() {
		return shield;
	}

	public void setShield(int shield) {
		this.shield = shield;
	}
	

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public int getGas(){
		return this.gas;
	}
	
	public void setGas(int gas){
		this.gas = gas;
	}
	
	public boolean getSelected(){
		return this.selected;
	}
	public void setSelected(boolean selected){
		this.selected = selected;
	}
}
