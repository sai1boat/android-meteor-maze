package com.joao024.mystery3d.mm.game;

import org.andengine.audio.sound.Sound;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class JWOW_Button extends ButtonSprite{
	
	/* This is used so we can ID what button was clicked,
	 * its just the text of the button itself
	 */
	private String id;
	private Text text;
	

	private IOnClickCallback onClick;
	private Sound clickSound;
	

	public IOnClickCallback getOnClick() {
		return onClick;
	}

	public void setOnClick(IOnClickCallback onClick) {
		this.onClick = onClick;
	}
	
	public void setOnClickSound(Sound s){
		this.clickSound = s;
	}

	public JWOW_Button(float x, float y, float width, float height, ITiledTextureRegion tiledTextureRegion, VertexBufferObjectManager vbo){
		super(x,y,tiledTextureRegion,vbo);
		this.setWidth(width);
		this.setHeight(height);
		this.setZIndex(64);
	}
	
	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return this.id;
	}
	
	
	@Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                    float pTouchAreaLocalX, float pTouchAreaLocalY){
		
		if(!this.isVisible()){
			return false;
		}
		
		if(pSceneTouchEvent.isActionUp()){
			
			onClick.onClick(this);
			
			if(this.clickSound!=null){
				this.clickSound.play();
			}
		}
		return true;
		//return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }
	
}
