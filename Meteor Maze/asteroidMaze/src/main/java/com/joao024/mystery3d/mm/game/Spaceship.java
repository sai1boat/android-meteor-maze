package com.joao024.mystery3d.mm.game;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

public class Spaceship extends Sprite{

	
	public static Float[] max_missle_speeds = new Float[]{3.0f, 4.0f,5.0f,4.0f};
	public static Float[] max_missle_hits = new Float[]{6f,3f,1f,0f};
	public static String[] missleNames    = new String[]{"code.slowmo", "code.pipes", "code.sleektown", "code.doge0"};
	public static Float[] densities = new Float[]{1f, 0.5f, 0.25f, 0.1f};
	//public static Float[] densities = new Float[]{0.25f, 0.14f, 0.07f, 0.1f}; experimental densities
	public static ITextureRegion[] textures = new ITextureRegion[]{GameSupport.tex_missleTextureRegion, 
																   GameSupport.tex_missleTextureRegion1, 
																   GameSupport.tex_missleTextureRegion2, 
																   GameSupport.tex_missleTextureRegion3};
	

	
	public int index = 0;
	MyGameActivity mg;

	
	
	public Spaceship(float x, float y, int which,  MyGameActivity mg){
		super(0, 0, textures[which], mg
				.getEngine().getVertexBufferObjectManager());
		this.mg = mg;
		index = which;
		//index = Globals.instance().getPrefs().getInt("selectedSpaceship", 0);
		
		

	}
	
	public void reset(){
		mg.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				MyGameActivity.spt.myDetachEntity(Spaceship.this);
				MyGameActivity.spt.createDebris(Spaceship.this.getX(), Spaceship.this.getY(), null);
				
			}
		});
		
	}
	
	
}
