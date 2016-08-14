package com.joao024.mystery3d.mm.game;

import org.andengine.entity.Entity;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;

public class JWOW_ButtonFactory {
	
	
	private static MyGameActivity mg;

	
	
	public JWOW_ButtonFactory(MyGameActivity mg){
		
		JWOW_ButtonFactory.mg = mg;
		
		
	}
	

	
	public JWOW_Button init(String text,float buttonWidth, float buttonHeight, final IOnClickCallback onClick) {
		
		
		final JWOW_Button ok = new JWOW_Button(buttonWidth/2,buttonHeight/2, buttonWidth, buttonHeight, mg.spt.tex_buttonUpDownTextureRegion, mg.getVertexBufferObjectManager());
		
	
		
		ok.setOnClick(onClick);
		ok.setOnClickSound(mg.spt.soundDoot);
		
		ok.setColor(0.6f, 0.6f,0.6f);
		ok.setId(text);
		/*
		final Line top = new Line(0,buttonHeight,buttonWidth,buttonHeight,2.0f,mg.getVertexBufferObjectManager());
		final Line bot = new Line(0,0,buttonWidth,0,2.0f,mg.getVertexBufferObjectManager());
		final Line lef = new Line(0,0,0,buttonHeight,2.0f,mg.getVertexBufferObjectManager());
		final Line rig = new Line(buttonWidth,0,buttonWidth,buttonHeight,2.0f,mg.getVertexBufferObjectManager());
		
		top.setColor(1.0f,0,0);
		bot.setColor(1.0f,0,0);
		lef.setColor(1.0f,0,0);
		rig.setColor(1.0f,0,0);
		
		ok.attachChild(top);
		ok.attachChild(bot);
		ok.attachChild(lef);
		ok.attachChild(rig);*/
		
		printAtLoc(text,buttonWidth/2, buttonHeight/2, ok);
		return ok;
	}
	

	
	public void printAtLoc(String s, float x, float y, Entity e){
		
	 	   final Text t = new Text(x,y, mg.mFont, 
	 			                            s, 
	 			                            new TextOptions(), mg.getVertexBufferObjectManager());
	 	   t.setPosition(x, y);

	 	   t.setZIndex(255); //always on top.
	 	   e.attachChild(t);
	 	   e.sortChildren();
	    }
}
