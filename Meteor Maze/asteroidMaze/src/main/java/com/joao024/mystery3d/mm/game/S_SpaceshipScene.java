package com.joao024.mystery3d.mm.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;

import com.joao024.mystery3d.mm.game.Globals.Difficulty;
import com.joao024.mystery3d.mm.game.SceneManager.AllScenes;
import com.joao024.mystery3d.mm.leveleditor.LevelUtils;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.modifier.ease.EaseCircularIn;
import org.andengine.util.modifier.ease.EaseCubicInOut;

public class S_SpaceshipScene extends Scene{
	
	private MyGameActivity mg;
	
	public S_SpaceshipScene(final MyGameActivity mg){

		this.mg = mg;
		

		
		
		
		final boolean unlockedSpaceship1 = Globals.instance().getPrefs().getBoolean("unlocked_pointsToUnlockSpaceship1", false);
		final boolean unlockedSpaceship2 = Globals.instance().getPrefs().getBoolean("unlocked_pointsToUnlockSpaceship2", false);
		final boolean unlockedSpaceship3 = Globals.instance().getPrefs().getBoolean("unlocked_pointsToUnlockSpaceship3", false);

		final float curPoints = LevelUtils.statsGetPoints();
		
		
		
		this.setBackground(new Background(0,0,0));
		
		for(int i=0;i<mg.mCameraWidth;i+=mg.mCameraWidth/40){
			final Line l = new Line(i,0,i,mg.mCameraHeight, mg.getVertexBufferObjectManager());
			l.setAlpha(0.6f);
			l.setColor(Color.BLUE);
			this.attachChild(l);
		}
		
		for(int i=0;i<mg.mCameraHeight;i+=mg.mCameraHeight/30){
			final Line l = new Line(0,i,mg.mCameraWidth,i, mg.getVertexBufferObjectManager());
			l.setAlpha(0.6f);
			l.setColor(Color.BLUE);
			this.attachChild(l);
		}


		
		
	
		final Sprite missle0 = new Sprite(0, 0, mg.spt.tex_missleTextureRegion, mg.getVertexBufferObjectManager()){
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
										float pTouchAreaLocalX, 
										float pTouchAreaLocalY){
				if(pSceneTouchEvent.isActionUp()){
					Globals.instance().difficulty = Difficulty.EASY;
					Globals.instance().getPrefs().edit().putInt("selectedSpaceship", 0).commit();
						
						GameSupport.soundDoot.play();
						mg.sm.createMenuScene();
						mg.sm.setCurrentScene(AllScenes.MENU);
				}

				return true;
			}
		};
		
		
		final Sprite missle1 = new Sprite(0, 0, mg.spt.tex_missleTextureRegion1, mg.getVertexBufferObjectManager()){
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, 
					float pTouchAreaLocalY){
				
				if(pSceneTouchEvent.isActionUp()){
					if(unlockedSpaceship1){
						Globals.instance().difficulty = Difficulty.MEDIUM;
						Globals.instance().getPrefs().edit().putInt("selectedSpaceship", 1).commit();
						GameSupport.soundDoot.play();
						mg.sm.createMenuScene();
						mg.sm.setCurrentScene(AllScenes.MENU);
					}
					else {
						final int points = Globals.instance().getPrefs().getInt("pointsToUnlockSpaceship1", Integer.MAX_VALUE);
						mg.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								new AlertDialog.Builder(mg)
								.setTitle("Message")
								.setMessage("You need "+(points-curPoints)+" more points \nto unlock this spaceship.")
								.setPositiveButton("Ok", new OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
							}).show();
							}
						});
						
					}
				}
				return true;
			}
		};
		final Sprite missle2 = new Sprite(0, 0, mg.spt.tex_missleTextureRegion2, mg.getVertexBufferObjectManager()){
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, 
					float pTouchAreaLocalY){
				if(pSceneTouchEvent.isActionUp()){
					if(unlockedSpaceship2){
						Globals.instance().difficulty = Difficulty.HARD;
						Globals.instance().getPrefs().edit().putInt("selectedSpaceship", 2).commit();
	
						GameSupport.soundDoot.play();
						mg.sm.createMenuScene();
						mg.sm.setCurrentScene(AllScenes.MENU);
					}
					else {
						final int points = Globals.instance().getPrefs().getInt("pointsToUnlockSpaceship2", Integer.MAX_VALUE);
						mg.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								new AlertDialog.Builder(mg)
								.setTitle("Message")
								.setMessage("You need "+(points-curPoints)+" more points \nto unlock this spaceship.")
								.setPositiveButton("Ok", new OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								}).show();
							}
						});
						
					}
				}
				return true;
			}
		};
		
		final Sprite missle3 = new Sprite(0, 0, mg.spt.tex_missleTextureRegion3, mg.getVertexBufferObjectManager()){
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, 
					float pTouchAreaLocalY){
				
				if(pSceneTouchEvent.isActionUp()){
					if(unlockedSpaceship3){
						Globals.instance().difficulty = Difficulty.HARD;
						Globals.instance().getPrefs().edit().putInt("selectedSpaceship", 3).commit();
	
						GameSupport.soundDoot.play();
						mg.sm.createMenuScene();
						mg.sm.setCurrentScene(AllScenes.MENU);
					}
					else {
						final int points = Globals.instance().getPrefs().getInt("pointsToUnlockSpaceship3", Integer.MAX_VALUE);
						mg.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								new AlertDialog.Builder(mg)
								.setTitle("Message")
								.setMessage("Wow. You need "+(points-curPoints)+" more points to unlock this spaceship. \nSo disappoint.")
								.setPositiveButton("Ok", new OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								}).show();
							}
						});
						
					}
				}
				return true;
			}
		};
		
		
		
		
		missle0.setScale(0.25f);
		missle1.setScale(0.25f);
		missle2.setScale(0.25f);
		missle3.setScale(0.25f);
		
		missle0.setRotation(-90f);
		missle1.setRotation(-90f);
		missle2.setRotation(-90f);
		missle3.setRotation(0f);
		

		
		
		missle0.setPosition(100, 160);
		missle1.setPosition(100+missle0.getWidthScaled(), 160);
		missle2.setPosition(100+missle0.getWidthScaled()+missle1.getWidthScaled(), 160);
		missle3.setPosition(100+missle0.getWidthScaled()+missle1.getWidthScaled()+missle2.getWidthScaled(), 160);
		
		this.attachChild(missle0);
		this.attachChild(missle1);
		this.attachChild(missle2);
		this.attachChild(missle3);
		
		float missle0X = 100;
		float missle0Y = 160;
		
		float missle1X = 100+128;
		float missle1Y = mg.mCameraHeight-178f;
		
		float missle2X = 100+2*128;
		float missle2Y = mg.mCameraHeight-178f;
		
		float missle3X = 100+3*128;
		float missle3Y = mg.mCameraHeight-178f;
		
		this.registerTouchArea(missle0);
		final JWOW_Button customize0 = new JWOW_Button(0f, 0f, 88f, 32f, mg.spt.tex_buttonUpDownTextureRegion, mg.getVertexBufferObjectManager());
		Text customize0Text = new Text(customize0.getWidthScaled()/2, customize0.getHeight()/2, mg.mFont, "Customize", mg.getVertexBufferObjectManager());
		customize0Text.setScale(0.5f);
		customize0.setOnClick(new IOnClickCallback() {
			
			@Override
			public void onClick(Object clickedObject) {
				mg.spt.mg.mCamera.setCenter(missle0.getX(), missle0.getY());
				mg.spt.mg.mCamera.setZoomFactor(4.7f);
				customize0.setVisible(false);
				unregisterTouchArea(customize0);
				unregisterTouchArea(missle0);
				unregisterTouchArea(missle1);
				unregisterTouchArea(missle2);
				
				createCustomizationUI(0);
			}
		});
		
		customize0.setPosition(missle0X,78f);
		customize0.attachChild(customize0Text);
		
		this.attachChild(customize0);
		this.registerTouchArea(customize0);
		
		
		this.registerTouchArea(missle1);
		if(unlockedSpaceship1){
			
			final JWOW_Button customize1 = new JWOW_Button(0f, 0f, 88f, 32f, mg.spt.tex_buttonUpDownTextureRegion, mg.getVertexBufferObjectManager());
			Text customize1Text = new Text(customize1.getWidthScaled()/2, customize1.getHeight()/2, mg.mFont, "Customize", mg.getVertexBufferObjectManager());
			customize1Text.setScale(0.5f);
			customize1.setOnClick(new IOnClickCallback() {
				
				@Override
				public void onClick(Object clickedObject) {
					mg.spt.mg.mCamera.setCenter(missle1.getX(), missle1.getY());
					mg.spt.mg.mCamera.setZoomFactor(4.7f);
					customize1.setVisible(false);
					unregisterTouchArea(customize1);
					unregisterTouchArea(missle0);
					unregisterTouchArea(missle1);
					unregisterTouchArea(missle2);
					
					createCustomizationUI(1);
				}
			});
			
			customize1.setPosition(missle1X,78f);
			customize1.attachChild(customize1Text);
			
			this.attachChild(customize1);
			this.registerTouchArea(customize1);
		
		}
		
		this.registerTouchArea(missle2);
		if(unlockedSpaceship2){
			
			final JWOW_Button customize2 = new JWOW_Button(0f, 0f, 88f, 32f, mg.spt.tex_buttonUpDownTextureRegion, mg.getVertexBufferObjectManager());
			Text customize2Text = new Text(customize2.getWidthScaled()/2, customize2.getHeight()/2, mg.mFont, "Customize", mg.getVertexBufferObjectManager());
			customize2Text.setScale(0.5f);
			customize2.setOnClick(new IOnClickCallback() {
				
				@Override
				public void onClick(Object clickedObject) {
					mg.spt.mg.mCamera.setCenter(missle2.getX(), missle2.getY());
					mg.spt.mg.mCamera.setZoomFactor(4.7f);
					customize2.setVisible(false);
					unregisterTouchArea(customize2);
					unregisterTouchArea(missle0);
					unregisterTouchArea(missle1);
					unregisterTouchArea(missle2);
					
					createCustomizationUI(2);
				}
			});
			
			customize2.setPosition(missle2X,78f);
			customize2.attachChild(customize2Text);
			
			this.attachChild(customize2);
			this.registerTouchArea(customize2);
		}
		
		this.registerTouchArea(missle3);
		if(unlockedSpaceship3){
			
			final JWOW_Button customize3 = new JWOW_Button(0f, 0f, 88f, 32f, mg.spt.tex_buttonUpDownTextureRegion, mg.getVertexBufferObjectManager());
			Text customize3Text = new Text(customize3.getWidthScaled()/2, customize3.getHeight()/2, mg.mFont, "Customize", mg.getVertexBufferObjectManager());
			customize3Text.setScale(0.5f);
			customize3.setOnClick(new IOnClickCallback() {
				
				@Override
				public void onClick(Object clickedObject) {
					mg.spt.mg.mCamera.setCenter(missle3.getX(), missle3.getY());
					mg.spt.mg.mCamera.setZoomFactor(4.7f);
					customize3.setVisible(false);
					unregisterTouchArea(customize3);
					unregisterTouchArea(missle0);
					unregisterTouchArea(missle1);
					unregisterTouchArea(missle2);
					unregisterTouchArea(missle3);
					
					createCustomizationUI(3);
				}
			});
			
			customize3.setPosition(missle2X,78f);
			customize3.attachChild(customize3Text);
			
			this.attachChild(customize3);
			this.registerTouchArea(customize3);
		}
		
		final Sprite checkmark = new Sprite(0.0f, 0.0f, mg.spt.tex_checkmarkTextureRegion, mg.getVertexBufferObjectManager());
		checkmark.setColor(Color.GREEN);
		//checkmark.setScale(0.5f);
		
		//Add checkmark for selected spaceship
		int whichSpaceship = Globals.instance().getPrefs().getInt("selectedSpaceship", 0);
		switch(whichSpaceship){
			case 0:
				LevelUtils.badge(missle0, checkmark, LevelUtils.BadgeLocation.BOTTOM);
				break;
			case 1:
				LevelUtils.badge(missle1, checkmark, LevelUtils.BadgeLocation.BOTTOM);
				break;
			case 2:
				LevelUtils.badge(missle2, checkmark, LevelUtils.BadgeLocation.BOTTOM);
				break;
			case 3:
				LevelUtils.badge(missle3, checkmark, LevelUtils.BadgeLocation.BOTTOM);
				break;
				default:
					LevelUtils.badge(missle0, checkmark, LevelUtils.BadgeLocation.BOTTOM);
					break;
		}
		
		Path p0 = new Path(2).to(100,       -256).to(100,mg.mCameraHeight-178f);
		Path p1 = new Path(2).to(100+128,   -256).to(missle1X, missle1Y);
		Path p2 = new Path(2).to(100+2*128, -256f).to(missle2X, missle2Y);
		Path p3 = new Path(2).to(100+3*128, -256f).to(missle3X, missle3Y);
		//Path p3 = new Path(2).to(-512f, missle3Y).to(100+3*128, missle3Y);
		
		missle0.registerEntityModifier(new PathModifier(1.0f, p0, EaseCubicInOut.getInstance()));
		missle1.registerEntityModifier(new PathModifier(1.0f, p1, EaseCubicInOut.getInstance()));
		missle2.registerEntityModifier(new PathModifier(1.0f, p2, EaseCubicInOut.getInstance()));
		missle3.registerEntityModifier(new PathModifier(1.0f, p3, EaseCubicInOut.getInstance()));
		
		JWOW_Button cancel = new JWOW_ButtonFactory(mg).init("Cancel", 92, 32, new IOnClickCallback() {
			
			@Override
			public void onClick(Object clickedObject) {

					mg.spt.fadeOutAndReturnToMenu(1.0f);
					
				
			}
		});
		
		this.registerTouchArea(cancel);
		cancel.setPosition(mg.mCameraWidth-128f, 48f);
		this.attachChild(cancel);
		
		Text t = new Text(0,0,mg.mFontStrokeArial,"Choose a spaceship", mg.getVertexBufferObjectManager());
		t.setPosition(mg.mCameraWidth/2, mg.mCameraHeight-64f);
		this.attachChild(t);
		
		Rectangle r0 = new Rectangle(0,0,96f,96f,mg.getVertexBufferObjectManager());
		Rectangle r1 = new Rectangle(0,0,96f,96f,mg.getVertexBufferObjectManager());
		Rectangle r2 = new Rectangle(0,0,96f,96f,mg.getVertexBufferObjectManager());
		
		r0.setColor(Color.BLACK);
		r1.setColor(Color.BLACK);
		r2.setColor(Color.BLACK);
		
		Text tSpeed0 = new Text(0,0,mg.mFont,"speed",mg.getVertexBufferObjectManager());
		Text tSpeed1 = new Text(0,0,mg.mFont,"speed",mg.getVertexBufferObjectManager());
		Text tSpeed2 = new Text(0,0,mg.mFont,"speed",mg.getVertexBufferObjectManager());
		
		tSpeed0.setScale(0.5f);
		tSpeed1.setScale(0.5f);
		tSpeed2.setScale(0.5f);
		
		Text tHits0 = new Text(0,0,mg.mFont,"hits",mg.getVertexBufferObjectManager());
		Text tHits1 = new Text(0,0,mg.mFont,"hits",mg.getVertexBufferObjectManager());
		Text tHits2 = new Text(0,0,mg.mFont,"hits",mg.getVertexBufferObjectManager());
		
		tHits0.setScale(0.5f);
		tHits1.setScale(0.5f);
		tHits2.setScale(0.5f);
		
		

		MyProgressBar rSpeed0 = new MyProgressBar(0,0,40, 8f,mg.getVertexBufferObjectManager());
		MyProgressBar rSpeed1 = new MyProgressBar(0,0,40, 8f,mg.getVertexBufferObjectManager());
		MyProgressBar rSpeed2 = new MyProgressBar(0,0,40, 8f,mg.getVertexBufferObjectManager());
		
		rSpeed0.setProgress(Spaceship.max_missle_speeds[0]/Spaceship.max_missle_speeds[Spaceship.max_missle_speeds.length-2]);
		rSpeed1.setProgress(Spaceship.max_missle_speeds[1]/Spaceship.max_missle_speeds[Spaceship.max_missle_speeds.length-2]);
		rSpeed2.setProgress(Spaceship.max_missle_speeds[2]/Spaceship.max_missle_speeds[Spaceship.max_missle_speeds.length-2]);
	
		
		
		MyProgressBar rHits0 = new MyProgressBar(0f,0f,40, 8f,mg.getVertexBufferObjectManager());
		MyProgressBar rHits1 = new MyProgressBar(0f,0f,40, 8f,mg.getVertexBufferObjectManager());
		MyProgressBar rHits2 = new MyProgressBar(0f,0f,40, 8f,mg.getVertexBufferObjectManager());
		
		rHits0.setProgress(Spaceship.max_missle_hits[0]/Spaceship.max_missle_hits[0]);
		rHits1.setProgress(Spaceship.max_missle_hits[1]/Spaceship.max_missle_hits[0]);
		rHits2.setProgress(Spaceship.max_missle_hits[2]/Spaceship.max_missle_hits[0]);
		
		
		
		
		tSpeed0.setPosition(12f, 80f);
		tSpeed1.setPosition(12f, 80f);
		tSpeed2.setPosition(12f, 80f);
		rSpeed0.setPosition(tSpeed0.getWidthScaled(), 76f);
		rSpeed1.setPosition(tSpeed1.getWidthScaled(), 76f);
		rSpeed2.setPosition(tSpeed2.getWidthScaled(), 76f);
		
		
		tHits0.setPosition(12f, 48f);
		tHits1.setPosition(12f, 48f);
		tHits2.setPosition(12f, 48f);
		rHits0.setPosition(tHits0.getWidthScaled(), 44f);
		rHits1.setPosition(tHits1.getWidthScaled(), 44f);
		rHits2.setPosition(tHits2.getWidthScaled(), 44f);
		
		Sprite sIcon0 = new Sprite(0,0, mg.spt.tex_lockIconTextureRegion, mg.getVertexBufferObjectManager());
		sIcon0.setScale(0.4f);
		Sprite sIcon1 = new Sprite(0,0, mg.spt.tex_lockIconTextureRegion, mg.getVertexBufferObjectManager());
		sIcon1.setScale(0.4f);
		
		
		//progress indicators
		if(!unlockedSpaceship1){
			Text tUnlocked1 = new Text(0,0,mg.mFont,"prog",mg.getVertexBufferObjectManager());
			tUnlocked1.setScale(0.5f);
			float pointsToUnlock1 = curPoints/Globals.instance().getPrefs().getInt("pointsToUnlockSpaceship1", Integer.MAX_VALUE);

			MyProgressBar rProgress1 = new MyProgressBar(0f, 0f,40f, 8f,mg.getVertexBufferObjectManager());
			rProgress1.setProgress(pointsToUnlock1);
			tUnlocked1.setPosition(12f, 16f);
			rProgress1.setPosition(tUnlocked1.getWidthScaled(), 12f);
			r1.attachChild(tUnlocked1);
			r1.attachChild(rProgress1);
		
		}
		if(!unlockedSpaceship2){
			Text tUnlocked2 = new Text(0,0,mg.mFont,"prog",mg.getVertexBufferObjectManager());
			tUnlocked2.setScale(0.5f);
			float pointsToUnlock2 = curPoints/Globals.instance().getPrefs().getInt("pointsToUnlockSpaceship2", Integer.MAX_VALUE);
			MyProgressBar rProgress2 = new MyProgressBar(0f, 0f,40f, 8f,mg.getVertexBufferObjectManager());
			rProgress2.setProgress(pointsToUnlock2);
			tUnlocked2.setPosition(12f, 16f);
			rProgress2.setPosition(tUnlocked2.getWidthScaled(), 12f);
			r2.attachChild(tUnlocked2);
			r2.attachChild(rProgress2);
		}
		
		
		
		if(!unlockedSpaceship1)
			LevelUtils.badge(r1, sIcon0, LevelUtils.BadgeLocation.TOP);
		if(!unlockedSpaceship2)
			LevelUtils.badge(r2, sIcon1, LevelUtils.BadgeLocation.TOP);
		
		r0.attachChild(tSpeed0);
		r0.attachChild(rSpeed0);
		r0.attachChild(tHits0);
		r0.attachChild(rHits0);
		
		r1.attachChild(tSpeed1);
		r1.attachChild(rSpeed1);
		r1.attachChild(tHits1);
		r1.attachChild(rHits1);
		
		r2.attachChild(tSpeed2);
		r2.attachChild(rSpeed2);
		r2.attachChild(tHits2);
		r2.attachChild(rHits2);
		
		
		r0.setPosition(missle0.getX(), missle0.getY());
		r1.setPosition(missle1.getX(), missle1.getY());
		r2.setPosition(missle2.getX(), missle2.getY());
		
		this.attachChild(r0);
		this.attachChild(r1);
		this.attachChild(r2);
		
    	

	
	}
	
	
	
	
	
	
	
	
	

	
	private void setSelectedWeapon(int weapon){
		Globals.instance().getPrefs().edit().putInt("selectedWeapon", weapon).commit();
		
	}
	
	private void setSelectedGadget(int gadget){
		Globals.instance().getPrefs().edit().putInt("selectedGadget", gadget).commit();
	}

	public void createCustomizationUI(final int missle){
		
		
		final boolean wep0Unlocked = Globals.instance().getPrefs().getBoolean("unlocked_pointsToUnlockSpaceship"+missle+"Weapon0", Globals.instance().isDebug()? true:false);
		final boolean wep1Unlocked = Globals.instance().getPrefs().getBoolean("unlocked_pointsToUnlockSpaceship"+missle+"Weapon1", Globals.instance().isDebug()? true:false);
		final boolean wep2Unlocked = Globals.instance().getPrefs().getBoolean("unlocked_pointsToUnlockSpaceship"+missle+"Weapon2", Globals.instance().isDebug()? true:false);
		
		final boolean gad0Unlocked = Globals.instance().getPrefs().getBoolean("unlocked_pointsToUnlockSpaceship"+missle+"Gadget0", Globals.instance().isDebug()? true:false);
		final boolean gad1Unlocked = Globals.instance().getPrefs().getBoolean("unlocked_pointsToUnlockSpaceship"+missle+"Gadget1", Globals.instance().isDebug()? true:false);
		final boolean gad2Unlocked = Globals.instance().getPrefs().getBoolean("unlocked_pointsToUnlockSpaceship"+missle+"Gadget2", Globals.instance().isDebug()? true:false);
		
		
		final int wep0Points = Globals.instance().getPrefs().getInt("pointsToUnlockSpaceship"+missle+"Weapon0", Integer.MAX_VALUE);
		final int wep1Points = Globals.instance().getPrefs().getInt("pointsToUnlockSpaceship"+missle+"Weapon1", Integer.MAX_VALUE);
		final int wep2Points = Globals.instance().getPrefs().getInt("pointsToUnlockSpaceship"+missle+"Weapon2", Integer.MAX_VALUE);
		
		final int gad0Points = Globals.instance().getPrefs().getInt("pointsToUnlockSpaceship"+missle+"Gadget0", Integer.MAX_VALUE);
		final int gad1Points = Globals.instance().getPrefs().getInt("pointsToUnlockSpaceship"+missle+"Gadget1", Integer.MAX_VALUE);
		final int gad2Points = Globals.instance().getPrefs().getInt("pointsToUnlockSpaceship"+missle+"Gadget2", Integer.MAX_VALUE);
		
		
		final Text tWeapon = new Text(128f, mg.mCameraHeight-128f, mg.mFont, "Weapon", mg.getVertexBufferObjectManager());
		//final Text tShield = new Text(mg.mCameraWidth-128f, mg.mCameraHeight-128f, mg.mFont,"Gadget", mg.getVertexBufferObjectManager());
		tWeapon.setAlpha(0.0f);
		//tShield.setAlpha(0.0f);
		
		final Sprite[] weps = new Sprite[3];
		final Sprite[] gadgets = new Sprite[3];
		
		final Sprite wep0 = new Sprite(tWeapon.getX(), tWeapon.getY()-64*1, mg.spt.tex_circleParticleTextureRegion, mg.getVertexBufferObjectManager()){
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, 
					float pTouchAreaLocalY){
				if(wep0Unlocked==true){
					setSelectedWeapon(0);
					for(int i = 0; i<weps.length;i++){
						weps[i].setColor(Color.WHITE);
					}
					setColor(Color.GREEN);
				}
				else if(pSceneTouchEvent.isActionUp()){
					mg.runOnUiThread(new Runnable() {
						public void run() {
							new AlertDialog.Builder(mg)
							.setTitle("Message")
							.setMessage("You must get "+wep0Points+" points to unlock this weapon")
							.setPositiveButton("Ok", new OnClickListener(){
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
								
							})
							.show();
						}
					});
					
				}
				
				return true;
			}
		};
		final Sprite wep1 = new Sprite(tWeapon.getX(), tWeapon.getY()-64f*2, mg.spt.tex_circleParticleTextureRegion, mg.getVertexBufferObjectManager()){
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, 
					float pTouchAreaLocalY){
				
				if(wep1Unlocked==true){
					setSelectedWeapon(1);
					for(int i = 0; i<weps.length;i++){
						weps[i].setColor(Color.WHITE);
					}
					setColor(Color.GREEN);
				}
				else if(pSceneTouchEvent.isActionUp()){
					mg.runOnUiThread(new Runnable() {
						public void run() {
							new AlertDialog.Builder(mg)
							.setTitle("Message")
							.setMessage("You must get "+wep1Points+" points to unlock this weapon")
							.setPositiveButton("Ok", new OnClickListener(){
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
								
							})
							.show();
						}
					});
						
					}
				return true;
			}
		};
		final Sprite wep2 = new Sprite(tWeapon.getX(), tWeapon.getY()-64f*3, mg.spt.tex_circleParticleTextureRegion, mg.getVertexBufferObjectManager()){
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, 
					float pTouchAreaLocalY){
				
				if(wep2Unlocked==true){
					setSelectedWeapon(2);
					for(int i = 0; i<weps.length;i++){
						weps[i].setColor(Color.WHITE);
					}
					setColor(Color.GREEN);
				}
				else if(pSceneTouchEvent.isActionUp()){
					mg.runOnUiThread(new Runnable() {
						public void run() {
							new AlertDialog.Builder(mg)
							.setTitle("Message")
							.setMessage("You must get "+wep2Points+" points to unlock this weapon")
							.setPositiveButton("Ok", new OnClickListener(){
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
								
							})
							.show();
						}
					});
					
				}
				return true;
			}
		};

		/*
		final Sprite shi0 = new Sprite(tShield.getX(), tShield.getY()-64*1, mg.spt.tex_circleParticleTextureRegion, mg.getVertexBufferObjectManager()){
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, 
					float pTouchAreaLocalY){
				
				if(gad0Unlocked==true){
					setSelectedGadget(0);
					for(int i = 0; i<gadgets.length;i++){
						gadgets[i].setColor(Color.WHITE);
					}
					setColor(Color.GREEN);
				}
				else if(pSceneTouchEvent.isActionUp()){
					mg.runOnUiThread(new Runnable() {
						public void run() {
							new AlertDialog.Builder(mg)
								.setTitle("Message")
								.setMessage("You must get "+gad0Points+" points to unlock this gadget")
								.setPositiveButton("Ok", new OnClickListener(){
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
									
								})
								.show();
						}
					});
				}
				return true;
			}
		};
		final Sprite shi1 = new Sprite(tShield.getX(), tShield.getY()-64f*2, mg.spt.tex_circleParticleTextureRegion, mg.getVertexBufferObjectManager()){
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, 
					float pTouchAreaLocalY){
				if(gad1Unlocked==true){
					setSelectedGadget(1);
					for(int i = 0; i<gadgets.length;i++){
						gadgets[i].setColor(Color.WHITE);
					}
					setColor(Color.GREEN);
				}
				else if(pSceneTouchEvent.isActionUp()){
					mg.runOnUiThread(new Runnable() {
						public void run() {
							new AlertDialog.Builder(mg)
								.setTitle("Message")
								.setMessage("You must get "+gad1Points+" points to unlock this gadget")
								.setPositiveButton("Ok", new OnClickListener(){
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
									
								})
								.show();
						}
					});
				}
				return true;
			}
		};
		final Sprite shi2 = new Sprite(tShield.getX(), tShield.getY()-64f*3, mg.spt.tex_circleParticleTextureRegion, mg.getVertexBufferObjectManager()){
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, 
					float pTouchAreaLocalY){
				if(gad2Unlocked==true){
					setSelectedGadget(2);
					for(int i = 0; i<gadgets.length;i++){
						gadgets[i].setColor(Color.WHITE);
					}
					setColor(Color.GREEN);	
				}
				else if(pSceneTouchEvent.isActionUp()){
					
					mg.runOnUiThread(new Runnable() {
						public void run() {
							new AlertDialog.Builder(mg)
								.setTitle("Message")
								.setMessage("You must get "+gad2Points+" points to unlock this gadget")
								.setPositiveButton("Ok", new OnClickListener(){
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
									
								})
								.show();
						}
					});
				}
				return true;
			}
		};*/

		
		weps[0] = wep0;
		weps[1] = wep1;
		weps[2] = wep2;

		/*
		gadgets[0] = shi0;
		gadgets[1] = shi1;
		gadgets[2] = shi2;
*/
		

		
		int selectedWeapon = Globals.instance().getPrefs().getInt("selectedWeapon", 0);
		
		//int selectedGadget    = Globals.instance().getPrefs().getInt("selectedGadget", 0);
                 
		weps[selectedWeapon].setColor(Color.GREEN);
		//gadgets[selectedGadget].setColor(Color.GREEN);
		
		for(int i=0;i<weps.length;i++){
			mg.mHud.attachChild(weps[i]);
			weps[i].registerEntityModifier(new AlphaModifier(2.0f, 0.0f, 1.0f,EaseCircularIn.getInstance()));
			mg.mHud.registerTouchArea(weps[i]);
			
			
		}
		/*
		for(int i=0;i<gadgets.length;i++){
			mg.mHud.attachChild(gadgets[i]);
			gadgets[i].registerEntityModifier(new AlphaModifier(2.0f, 0.0f, 1.0f,EaseCircularIn.getInstance()));
			mg.mHud.registerTouchArea(gadgets[i]);
			
		}*/

		//I guess text items are different than entites, OK :)
		tWeapon.registerEntityModifier(new AlphaModifier(2.0f, 0.0f, 1.0f,EaseCircularIn.getInstance()));
		//tShield.registerEntityModifier(new AlphaModifier(2.0f, 0.0f, 1.0f,EaseCircularIn.getInstance()));
		
		mg.mHud.attachChild(tWeapon);
		//mg.mHud.attachChild(tShield);
		
		mg.mHud.setVisible(true);
	}
	
	
}
