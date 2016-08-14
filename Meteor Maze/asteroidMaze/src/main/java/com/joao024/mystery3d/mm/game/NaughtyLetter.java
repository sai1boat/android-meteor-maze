package com.joao024.mystery3d.mm.game;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.font.Font;

public class NaughtyLetter extends Text{
	
	private MyGameActivity mg;
	private Font mFont;
	private String letter;
	
	public NaughtyLetter(float x, float y, Font mFont, String txt,  MyGameActivity mg){
		super(x, y, mFont,txt, mg.getVertexBufferObjectManager());
		this.mFont = mFont;
		this.mg = mg;
		this.letter = txt;
		init(x,y);
	}
	private void init(float x, float y){

   	 
   	 this.setScale(2.0f);
   	 
		FixtureDef def = PhysicsFactory.createFixtureDef(0.1f, 0.1f, 0.1f, true, mg.spt.CATEGORY_BIT_ENEMY, mg.spt.MASK_BIT_ENEMY, (short)0);
		Body body = PhysicsFactory.createBoxBody(mg.mPhysicsWorld, this, BodyType.DynamicBody, def);
		ObjectMeta om = new ObjectMeta("naughtyLetter", this, body);
		body.setUserData(om);
		this.setUserData(om);
		

	}
	
	public String getLetter(){
		return this.letter;
	}
}
