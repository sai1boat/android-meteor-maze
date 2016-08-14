package com.joao024.mystery3d.mm.game;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
/*
 * Bottom left-hand coordinate rectangle. So sick of setting rectangle locations by their
 * center :(
 */
public class MyRectangle extends Rectangle{

	public MyRectangle(float pX, float pY, float pWidth, float pHeight,
			VertexBufferObjectManager vertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, vertexBufferObjectManager);
		setPosition(pX, pY);
		setColor(Color.WHITE);
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
}
