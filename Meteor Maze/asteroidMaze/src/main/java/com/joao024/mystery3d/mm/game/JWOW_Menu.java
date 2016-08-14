package com.joao024.mystery3d.mm.game;

import android.util.Log;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;

import java.util.List;

public class JWOW_Menu {
	
	private static int MARGIN_PADDING_LEFT = 8;
	
	Scene mScene;
	Font mFont;
	static MyGameActivity mg;
	
	static Rectangle win;
	
    static BitmapTextureAtlas boxTexture;
    static ITextureRegion boxTextureRegion;
	
    
    boolean alreadyOpen = false;
	

	public JWOW_Menu(MyGameActivity mg){
		mScene = mg.sm.getCurrentSceneObject();
		mFont = mg.mFont;
		JWOW_Menu.mg = mg;
		loadResources();
	}

	public JWOW_Menu(Font f, Scene s, MyGameActivity bg){
		mScene = s;
		mFont = f;
		JWOW_Menu.mg = bg;
		loadResources();
	}
	
	public void loadResources(){
		/*
		if(boxTextureRegion!=null) return;
		else{
			boxTexture = new BitmapTextureAtlas(mBaseGameActivity.getTextureManager(),64,64);
    		boxTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(boxTexture, mBaseGameActivity, "box_outline.png",0,0);
		}*/
		
	}
	
	
	/* Override this function to do something when ok is pressed
	 * 
	 */
	public void onOk(){}
	

	public void disposeWindow(JWOW_Button okButton){
		win.detachSelf();
		if(!win.isDisposed()){
			
			win.dispose();
		}
		alreadyOpen = false;
		
		
		if(okButton!=null){
			mScene.unregisterTouchArea(okButton);
		}
	}
	
	
	
	public Rectangle createBlankDialog(float x, float y, float width, float height){
		if(alreadyOpen){
			return null;
		}

		
		alreadyOpen = true;
		
		int actualHeightNeeded = 0;

		
		
		if(height<actualHeightNeeded){
			height = actualHeightNeeded;
		}

		
		float okButtonHeight = height/3;
		height+=okButtonHeight;
		

		win = new Rectangle(x,y,width,height, mg.getVertexBufferObjectManager());
		win.setColor(0.0f,0.0f,0.0f);
		
		//red borders :)
		final Line wtop = new Line(0,height,width,height,2.0f,mg.getVertexBufferObjectManager());
		final Line wbot = new Line(0,0,width,0,2.0f,mg.getVertexBufferObjectManager());
		final Line wlef = new Line(0,0,0,height,2.0f,mg.getVertexBufferObjectManager());
		final Line wrig = new Line(width,0,width,height,2.0f,mg.getVertexBufferObjectManager());
		
		wtop.setColor(1.0f,0,0);
		wbot.setColor(1.0f,0,0);
		wlef.setColor(1.0f,0,0);
		wrig.setColor(1.0f,0,0);
		
		win.attachChild(wtop);
		win.attachChild(wbot);
		win.attachChild(wlef);
		win.attachChild(wrig);
		

		
		
		return win;
	}

	
	public Rectangle showDialog(String text, float x, float y, float width, float height,  List<IEntity> buttons,  IOnClickCallback callback){
		
		if(alreadyOpen){
			return null;
		}
		
		
		if(Globals.instance().isDebug()){
			Log.w("JWOW_Menu","showing dialog with text:"+text);
		}
		
		alreadyOpen = true;
		
		int actualHeightNeeded = 0;

		
		/* Split text on \n to make new lines */
		String[] tokens = text.split("\n");
		if(Globals.instance().isDebug()){
			for(int i=0;i<tokens.length;i++){
				if(tokens.length==0){
					Log.e("JWOW_Menu","tokens length was zero");
				}
				if(mFont==null){
					Log.e("JWOW_Menu","mfont was null");
				}
				
			}
		}
		
		if(height<actualHeightNeeded){
			height = actualHeightNeeded;
		}

		
		float okButtonHeight = height/3;
		height+=okButtonHeight;
		

		win = new Rectangle(x,y,width,height, mg.getVertexBufferObjectManager());
		win.setColor(0.0f,0.0f,0.0f);
		
		//red borders :)
		final Line wtop = new Line(0,height,width,height,2.0f,mg.getVertexBufferObjectManager());
		final Line wbot = new Line(0,0,width,0,2.0f,mg.getVertexBufferObjectManager());
		final Line wlef = new Line(0,0,0,height,2.0f,mg.getVertexBufferObjectManager());
		final Line wrig = new Line(width,0,width,height,2.0f,mg.getVertexBufferObjectManager());
		
		wtop.setColor(1.0f,0,0);
		wbot.setColor(1.0f,0,0);
		wlef.setColor(1.0f,0,0);
		wrig.setColor(1.0f,0,0);
		
		win.attachChild(wtop);
		win.attachChild(wbot);
		win.attachChild(wlef);
		win.attachChild(wrig);
		
		for(int i=0;i<tokens.length;i++){
			printAtLoc(tokens[i],MARGIN_PADDING_LEFT,(height-((mFont.getLineHeight()*i) + mFont.getLineHeight())),win);
		}
		
		/* IF callback is null, use the 
		 * default OK behavior
		 */
		if(callback == null){

			callback = new IOnClickCallback() {
				
				@Override
				public void onClick(Object clickedObject) {
					final JWOW_Button daButton = (JWOW_Button) clickedObject;
					daButton.detachSelf();
					if(!daButton.isDisposed()){
						
						daButton.dispose();
					}
					
					
					onOk();
					
					disposeWindow((JWOW_Button)clickedObject);

					mScene.unregisterTouchArea(daButton);
					
				}
			};
			
		}
		
		
		
		
		if(buttons!=null){
			for(IEntity e:buttons){
				win.attachChild(e);
				if(e instanceof JWOW_Button){
					mScene.registerTouchArea((JWOW_Button)e);
				}
				
			}
		}
		else {
			JWOW_ButtonFactory bf = new JWOW_ButtonFactory(mg);
			JWOW_Button ok = bf.init("OK", 128f, 64f, callback);
			ok.setPosition(win.getWidth()/2, 64f/2);
			win.attachChild(ok);
			mScene.registerTouchArea(ok);
		}
		
		mScene.attachChild(win);
		
		return win;
	}
	
	
	
	
	
	
	public void printAtLoc(String s, float x, float y, Entity e){
		
 	   final Text t = new Text(x,y, this.mFont, 
 			                            s, 
 			                            new TextOptions(), mg.getVertexBufferObjectManager());
 	   t.setPosition(x+t.getWidth()/2, y);

 	  // t.setZIndex(); //always on top.
 	   e.attachChild(t);
 	   e.sortChildren();
    }
	
	public boolean isAlreadyOpen(){
		return this.alreadyOpen;
	}

}
