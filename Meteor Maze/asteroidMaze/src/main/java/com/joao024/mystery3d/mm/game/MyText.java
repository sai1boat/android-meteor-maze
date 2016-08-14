package com.joao024.mystery3d.mm.game;

import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/*
 * Bottom left-hand coordinate rectangle. So sick of setting rectangle locations by their
 * center :(
 */
public class MyText extends Text{

	public MyText(float pX, float pY, Font font, String text, VertexBufferObjectManager vertexBufferObjectManager) {
		super(pX, pY, font, text, vertexBufferObjectManager);
		setPosition(pX, pY);

	}

	public void setPosition(float x, float y){
		
		super.setPosition(x+getWidthScaled()/2, y+getHeightScaled()/2);
	}
	
	public float getX(){
		return super.getX()-super.getWidthScaled()/2;
	}
	
	public float getY(){
		return super.getY()-super.getHeightScaled()/2;
	}
	
	public void setScale(float scale){
		float curX = getX();
		float curY = getY();
		super.setScale(scale);
		setPosition(curX, curY);
	}
}
