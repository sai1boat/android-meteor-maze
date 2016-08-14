package com.joao024.mystery3d.mm.game;

import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
/*
 * Bottom left-hand coordinate rectangle. So sick of setting rectangle locations by their
 * center :(
 */
public class MyProgressBar extends MyRectangle{

	MyRectangle progress;
	float percent = 0;
	
	public MyProgressBar(float pX, float pY, float pWidth, float pHeight,
			VertexBufferObjectManager vertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, vertexBufferObjectManager);
		setColor(Color.WHITE);
		
	}
	
	public void setProgress(final float percent){
		this.percent = percent;
		
		
		
		MyGameActivity.spt.mg.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				
				if(progress==null){
					progress = new MyRectangle(0, 0, 1f, MyProgressBar.this.getHeightScaled(), MyProgressBar.this.getVertexBufferObjectManager());
					progress.setColor(Color.GREEN);
					MyProgressBar.this.attachChild(progress);
				}
				
				
				float oldWidth = progress.getWidthScaled();
				float newWidth = getWidth()*percent;
				progress.setWidth(newWidth);
				progress.setPosition(progress.getX()+(newWidth-oldWidth)/2, progress.getY());
					
					
			}
		});
		
		
	}

	
}
