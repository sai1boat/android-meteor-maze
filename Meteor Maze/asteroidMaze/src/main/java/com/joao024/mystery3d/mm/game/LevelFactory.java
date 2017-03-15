package com.joao024.mystery3d.mm.game;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.joao024.mystery3d.mm.leveleditor.Level;
import com.joao024.mystery3d.mm.leveleditor.LevelUtils;
import com.joao024.mystery3d.mm.leveleditor.S_Asteroid;
import com.joao024.mystery3d.mm.leveleditor.S_Powerup;
import com.joao024.mystery3d.mm.leveleditor.S_Satellite;
import com.joao024.mystery3d.mm.leveleditor.S_Trigger;
import com.joao024.mystery3d.mm.leveleditor.S_Wall;
import com.joao024.mystery3d.mm.leveleditor.S_Warp;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
//import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import java.io.IOException;
import java.util.ArrayList;



public class LevelFactory {
	


	private GameSupport spt;
	
	
	
	public LevelFactory(GameSupport spt){
		this.spt = spt;
		
	}
	
	
	public void loadLevel(String levelName) {

		
		if(Globals.instance().isDebug()){
			Log.w("LevelFactory","loading level "+levelName);
			
		}
		
		int i = 0;
		
		if(levelName.equalsIgnoreCase("Play")){
			i = -1;
		}
		else {
			i = Integer.parseInt(levelName);
		}
		
		switch(i){
			case -1:
			levelEditor();
			break;
			case 1:
				levelOne();
				break;
		}
		
	}
	
	public void genericLoad(){
		
		

		if(Globals.instance().isLogFPS()){
			spt.gameScene.registerUpdateHandler(new FPSLogger());
		}
		
		
		/*
		 * Set up background image. Background images are 4096w x 4096h, and
		 * lets randomize the location of the giant sprite so we get some
		 * variability. The camera is 800w x 480h
		 */
		

		createBackground();
		
		
		
		if(Level.earthOrigin!=null){
			final IEntity e = createEarth(Level.earthOrigin.x, Level.earthOrigin.y);
			final ObjectMeta om = (ObjectMeta) e.getUserData();
			
			om.setEditorData(Level.earthOrigin);
			
		
			
		}
		
		
		for(S_Satellite s_sat:Level.satellites){
			final IEntity e = spt.createSatellite(s_sat.x, s_sat.y);
			final ObjectMeta om = (ObjectMeta) e.getUserData();
			
			om.setEditorData(s_sat);
		}
		
		double random = Math.random();
		/*
		final AlphaModifier blinkIn = new AlphaModifier(1.2f, 0.0f, 1.0f);
		final AlphaModifier blinkOut = new AlphaModifier(2.2f, 1.0f, 0.0f);
		final SequenceEntityModifier sem = new SequenceEntityModifier(blinkIn, blinkOut);
		final LoopEntityModifier blinkLooper = new LoopEntityModifier(sem);
	*/
		
		final SpriteBatch sbCloud = new SpriteBatch(spt.bta_circleParticleTextureAtlas, 10, spt.mg.getVertexBufferObjectManager());
		
		
		
		for(S_Wall s_wall: Level.walls){
			final Wall e = createWall(s_wall.x,s_wall.y,s_wall.width,s_wall.height,(double)s_wall.rotation);
			
			
			
			if(random>=0.0f){
				
				
				double random2 = Math.random();
				if(random2<0.2f){
					
					final Rectangle r = new Rectangle(0.0f, 0.0f, 128f, 128f, spt.mg.getVertexBufferObjectManager());
					
					
					
					
					for(int i=0;i<r.getWidth();i+=r.getWidth()/8){
						final Line l = new Line(i,0,i,r.getHeight(), spt.mg.getVertexBufferObjectManager());
						l.setColor(Color.BLUE);
						r.attachChild(l);
						
					}
					
					for(int i=0;i<r.getHeight();i+=r.getHeight()/8){
						final Line l = new Line(0,i,r.getWidth(),i, spt.mg.getVertexBufferObjectManager());
						l.setColor(Color.BLUE);
						r.attachChild(l);
					}

					r.setAlpha(0.02f);
					r.setZIndex(16);
					e.setZIndex(1);
					e.attachChild(r);
					e.sortChildren();
				}
				
				//fake-o half-tone clouds. Cool effect, but performance sucks
				/*
				ArrayList<Sprite> obj = new ArrayList<Sprite>();
				for(int i = 0; i<10; i++){
					final Sprite s = new Sprite(0f, 0f, spt.tex_circleParticleTextureRegion, spt.mg.getVertexBufferObjectManager());
					
					s.setScale(16f);
					s.setZIndex(1);
					s.setColor(Color.BLUE);
					s.setAlpha(0.6f);
					s.setCullingEnabled(true);
					
					if(obj.size()==0){
						s.setX(e.getX());
						s.setY(e.getY());
						//r.attachChild(s);
						sbCloud.attachChild(s);
						obj.add(s);
					}
					else {
						//randomly grab a point
						int ran = (int)(Math.random() * (obj.size()-1));
						final Sprite t = obj.get(ran);
						
						s.setX((float)(t.getX()+(Math.cos(ran)*t.getWidthScaled())));
						s.setY((float)(t.getY()+(Math.sin(ran)*t.getHeightScaled())));
						s.setScale(t.getScaleX()*0.7f);
						//r.attachChild(s);
						sbCloud.attachChild(s);
						obj.add(s);
					}
				}
				e.setZIndex(16);
				e.sortChildren();*/
			}
			
			
			//Sprite s = new Sprite(s_wall.width/2, s_wall.height/2,spt.tex_wallJoint, spt.mg.getVertexBufferObjectManager());
			//e.attachChild(s);

			
			final ObjectMeta om = (ObjectMeta) e.getUserData();
			om.setEditorData(s_wall);
		}
		
		//spt.gameScene.attachChild(sbCloud);
		//spt.gameScene.sortChildren();
		
		for(S_Trigger s_trigger: Level.triggers){
			if(s_trigger.width<=0.0f)
				s_trigger.width=64;
			if(s_trigger.height<=0.0f)
				s_trigger.height=64;

			final Trigger t = createTrigger(s_trigger.x, s_trigger.y, s_trigger.width, s_trigger.height,(double)s_trigger.rotation);
			final ObjectMeta om = (ObjectMeta) t.getUserData();
			om.setEditorData(s_trigger);
		}
		
		SpriteBatch sbPowerup = new SpriteBatch(spt.bta_asteroid0Texture, Level.powerups.size(), spt.mg.getVertexBufferObjectManager());
		
		for(S_Powerup s_powerup: Level.powerups){
			final Powerup p = createPowerup(s_powerup.x, s_powerup.y);
			final ObjectMeta om = (ObjectMeta) p.getUserData();
			om.setEditorData(s_powerup);
			sbPowerup.attachChild(p);
		}
		
		spt.gameScene.attachChild(sbPowerup);
		
		
		for(S_Warp warp: Level.warps){
			final Warp h = createWarp(warp.x, warp.y);
			
			final ObjectMeta om = (ObjectMeta) h.getUserData();
			om.setEditorData(warp);
			h.showLabel();
			
			for(int i=0;i<Level.warps.size();i++){
				if(warp.connectsTo.equalsIgnoreCase(Level.warps.get(i).name)){
					warp.connectsToWarp = Level.warps.get(i);
					break;
				}
				
				
			}
			
		}
			

		
		
		for(S_Asteroid ast: Level.asteroids){
			/* TODO: implement asteroid functionality.
			 * We need vector for direction and linearVelocity
			 */
		}
		
		/*
		 * Follows missle will be handled at runtime of the missle instead
		if(l.isCameraFollowsMissle){
			spt.attachCameraToMissle();
		}*/
		
		
		
	}

	
	public Warp createWarp(float x, float y){
		
		
			
		
		final Warp w = new Warp(x, y){
			
			S_Warp sp;
			
			
			
			@Override
			public boolean onAreaTouched(
					TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				
				
				
				sp = (S_Warp) om.getEditorData();

				if(LevelFactory.mode == EditorMode.MOVE){
					//om = (ObjectMeta)this.getUserData();
					
					if(pSceneTouchEvent.isActionMove()){
						
						
						sp.x = pSceneTouchEvent.getX();
						sp.y = pSceneTouchEvent.getY();

						om.getBody().setTransform(pSceneTouchEvent.getX()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
								pSceneTouchEvent.getY()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, om.getBody().getAngle());

					}
				}
				else if(pSceneTouchEvent.isActionUp() && LevelFactory.mode == EditorMode.MODIFY){
					
					spt.mg.runOnUiThread(new Runnable() {
						
						String[] items = new String[]{"Connects to", "Properties"};
						
						@Override
						public void run() {
							new AlertDialog.Builder(spt.mg)
							.setTitle("Modify Warp")
							.setItems(items, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									switch(which){
										case 0:
											final String[] warps = new String[Level.warps.size()];

											for(int i = 0; i<Level.warps.size(); i++){
												warps[i] = Level.warps.get(i).name;
											}
											dialog.dismiss();
											
											
											spt.mg.runOnUiThread(new Runnable() {
												
												@Override
												public void run() {
													new AlertDialog.Builder(spt.mg)
													.setTitle("Set warp destination")
													.setItems(warps, new DialogInterface.OnClickListener() {
														
														@Override
														public void onClick(DialogInterface dialog, int which) {
															sp.connectsTo=warps[which];
															for(int i=0;i<Level.warps.size();i++){
																if(warps[which].equalsIgnoreCase(Level.warps.get(i).name)){
																	sp.connectsToWarp = Level.warps.get(i);
																	break;
																}
															}
															Toast.makeText(spt.mg, sp.name+" now warps to "+warps[which], Toast.LENGTH_LONG).show();
														}
													})
													.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
														
														@Override
														public void onClick(DialogInterface dialog, int arg1) {
															dialog.dismiss();
															
														}
													})
													.show();
												}
											});
											
											break;
											
										case 1:
											break;
											default:
												break;
									}
									
								}
							})
							.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									
									spt.mg.runOnUpdateThread(new Runnable() {
										
										@Override
										public void run() {
											detachSelf();
											spt.mg.sm.getCurrentSceneObject().unregisterTouchArea((Warp)om.getEntity());
											
											om.getBody().setActive(false);
											spt.mg.mPhysicsWorld.destroyBody(om.getBody());
											for(Entity e:om.getAffiliatedObj()){
												e.detachSelf();
											}
											int foundAtIndex = -1;
												for(int i = 0; i<Level.warps.size(); i++){
													final S_Warp w = Level.warps.get(i);
													if(w.uniqueId==null || w.uniqueId.equals(((S_Warp)om.getEditorData()).uniqueId)){
														foundAtIndex = i;
														break;
															
													}

												}
												if(foundAtIndex>=0){
													Level.warps.remove(foundAtIndex);
												}
											
										}
									});

									
								}
							})
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									
								}
							})
							.create()
							.show();
							
						}
					});
					
				}
				return true;
			}
		};
		
		
		
		
		final PointParticleEmitter particleEmitter = new PointParticleEmitter(
				0, 0);

		float rateMin = 2f;
		float rateMax = 4f;
		int maxParticles = 10;
		SpriteParticleSystem p = new SpriteParticleSystem(
				particleEmitter, rateMin, rateMax, maxParticles,
				spt.tex_circleTextureRegion, spt.mg
						.getVertexBufferObjectManager());
		
		
		p.addParticleInitializer(new ScaleParticleInitializer<Sprite>(0.0f));
		p.addParticleInitializer(new ColorParticleInitializer<Sprite>(Color.BLUE));
		//p.addParticleInitializer(new AlphaParticleInitializer<Sprite>(0.6f));
		p.addParticleInitializer(new ExpireParticleInitializer<Sprite>(4.0f));
		
		p.addParticleModifier(new ScaleParticleModifier<Sprite>(0.0f, 2.0f, 0.0f, 0.4f));
		p.addParticleModifier(new AlphaParticleModifier<Sprite>(1.0f, 2.0f, 1.0f, 0.0f));
		
		p.registerUpdateHandler(new IUpdateHandler() {
			public void onUpdate(float pSecondsElapsed) {
				particleEmitter.setCenterX(w.getX());
				particleEmitter.setCenterY(w.getY());
			}

			public void reset() {
			}
		});
		
		w.setCullingEnabled(true);
		
		w.setVisible(false);
		
		//spt.mg.sm.getCurrentSceneObject().attachChild(p);
		spt.gameScene.attachChild(p);
		ObjectMeta om = (ObjectMeta)w.getUserData();
		om.getAffiliatedObj().add(p);
		
		//w.registerEntityModifier(new LoopEntityModifier(new AlphaModifier(1.0f, 0.1f, 0.8f)));
		//w.registerEntityModifier(new LoopEntityModifier(new AlphaModifier(1.0f, 0.8f, 0.1f)));
		
		
		spt.gameScene.registerTouchArea(w);
		spt.gameScene.attachChild(w);
		
		
		return w;
	}
	

	
	
	public Powerup createPowerup(float x, float y){
		final Powerup p = new Powerup(x,y,spt.tex_asteroid0TextureRegion, 0.5f){
			
			
			
			//ObjectMeta om = null;
			S_Powerup  sp = null;
			
			@Override
			public boolean onAreaTouched(
					TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				
				if(LevelFactory.mode == EditorMode.MOVE){
					//om = (ObjectMeta)this.getUserData();
					
					if(pSceneTouchEvent.isActionMove()){
						
						sp = (S_Powerup) om.getEditorData();
						sp.x = pSceneTouchEvent.getX();
						sp.y = pSceneTouchEvent.getY();

						om.getBody().setTransform(pSceneTouchEvent.getX()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
								pSceneTouchEvent.getY()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, om.getBody().getAngle());

					}
				}
				else if(pSceneTouchEvent.isActionUp() && LevelFactory.mode == EditorMode.MODIFY){
					
					spt.mg.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							new AlertDialog.Builder(spt.mg)
							.setTitle("Modify Powerup")
							.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									
									spt.mg.runOnUpdateThread(new Runnable() {
										
										@Override
										public void run() {
											detachSelf();
											
											om.getBody().setActive(false);
											spt.mg.mPhysicsWorld.destroyBody(om.getBody());
											int foundAtIndex = -1;
												for(int i = 0; i<Level.powerups.size(); i++){
													final S_Powerup w = Level.powerups.get(i);
													if(w.uniqueId==null || w.uniqueId.equals(((S_Powerup)om.getEditorData()).uniqueId)){
														foundAtIndex = i;
														break;
															
													}

												}
												if(foundAtIndex>=0){
													Level.powerups.remove(foundAtIndex);
												}
											
										}
									});

									
								}
							})
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
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
		
		if(isCreator==true)
			spt.gameScene.registerTouchArea(p);
		
		
		
		p.registerEntityModifier(new LoopEntityModifier(
				new RotationModifier(spt.ROT_EAR_TIME, 0, 360)));
		p.setRotationCenter(0.5f, 0.5f);
		
		

		/*
		final FixtureDef earthObjectFixtureDef = PhysicsFactory
				.createFixtureDef(spt.DENSE_EAR, 0.0f, 0.5f, true, spt.CATEGORY_BIT_EARTH, spt.MASK_BIT_EARTH, (short)0);
		final Body bPowerup = PhysicsFactory.createBoxBody(spt.mg.mPhysicsWorld,
				p, BodyType.DynamicBody, earthObjectFixtureDef);

		boolean earth_allowPhysicsToInfluencePosition = true;
		boolean earth_updateRotationBaseOnPhysicalCollision = false;

		spt.mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				p, bPowerup, earth_allowPhysicsToInfluencePosition,
				earth_updateRotationBaseOnPhysicalCollision));

		ObjectMeta meta = new ObjectMeta("powerup", p, bPowerup);
		p.setUserData(meta);
		bPowerup.setUserData(meta);
		*/

		//spt.gameScene.attachChild(p);
		
		
		
		return p;
	}
	

	Sprite sEarth;
	
	public IEntity createEarth(float x, float y){
		sEarth = new Sprite(x,y, spt.tex_earthTextureRegion, spt.mg
						.getEngine().getVertexBufferObjectManager()){
			
			ObjectMeta om = null;
			
			@Override
			public boolean onAreaTouched(
					TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				
				if(LevelFactory.mode == EditorMode.MOVE){
					om = (ObjectMeta)this.getUserData();
					
					if(pSceneTouchEvent.isActionMove()){
						om.getBody().setTransform(pSceneTouchEvent.getX()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
								pSceneTouchEvent.getY()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, om.getBody().getAngle());
						
						((Vector2)om.getEditorData()).x = pSceneTouchEvent.getX();
						((Vector2)om.getEditorData()).y = pSceneTouchEvent.getY();
				
					}
				}
				
				return super
						.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		
		
		
		spt.gameScene.registerTouchArea(sEarth);
		
		
		sEarth.setScale(0.5f, 0.5f); 
		sEarth.registerEntityModifier(new LoopEntityModifier(
				new RotationModifier(spt.ROT_EAR_TIME, 0, 360)));
		sEarth.setRotationCenter(0.5f, 0.5f);

		final FixtureDef earthObjectFixtureDef = PhysicsFactory
				.createFixtureDef(spt.DENSE_EAR, 0.0f, 0.5f, true, spt.CATEGORY_BIT_EARTH, spt.MASK_BIT_EARTH, (short)0);
		final Body bEarth = PhysicsFactory.createBoxBody(spt.mg.mPhysicsWorld,
				sEarth, BodyType.DynamicBody, earthObjectFixtureDef);

		boolean earth_allowPhysicsToInfluencePosition = true;
		boolean earth_updateRotationBaseOnPhysicalCollision = false;

		spt.mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				sEarth, bEarth, earth_allowPhysicsToInfluencePosition,
				earth_updateRotationBaseOnPhysicalCollision));

		ObjectMeta earthMeta = new ObjectMeta("earth", sEarth, bEarth);
		sEarth.setUserData(earthMeta);
		bEarth.setUserData(earthMeta);

		spt.gameScene.attachChild(sEarth);
		
		return sEarth;
	}
	
	
	/*
	public IEntity createSun(float x, float y){
		

		spt.sSun = new Sprite(x, y, spt.sunTextureRegion, spt.mg
				.getEngine().getVertexBufferObjectManager());
		spt.sSun.registerEntityModifier(new LoopEntityModifier(
				new RotationModifier(spt.ROT_SUN_TIME, 0, -360)));
		spt.sSun.setZIndex(-1);

		Log.w("Sun", "placing sun at " + x + " ," + y);
		spt.gameScene.attachChild(spt.sSun);
		
		return spt.sSun;
	}*/
	

	public void createOldBackground(){

		
		Sprite bg = new Sprite(0, 0, spt.tex_backgroundTextureRepeatingTexture, spt.mg
				.getEngine().getVertexBufferObjectManager());
		bg.setScale(0.5f);
		bg.setAlpha(0.5f);

		float xRan = (float) (Math.random() * (bg.getWidthScaled() / 2));
		float yRan = (float) (Math.random() * (bg.getHeightScaled() / 2));
		
		
		if (xRan + bg.getWidthScaled() / 2 < spt.mg.mCameraWidth) {
			xRan = spt.mg.mCameraWidth - bg.getWidthScaled()/2;
		}


		bg.setPosition(xRan, yRan);

		if (Globals.instance().isDebug()) {
			
			if (xRan + bg.getWidthScaled() / 2 < spt.mg.mCameraWidth) {
				
				Log.w("gameSupport bg",
						"bg is likely off the screen in the x-axis direction");
			}

			if (yRan + bg.getHeightScaled() / 2 < spt.mg.mCameraHeight) {
				Log.w("gameSupport bg",
						"bg is likely off the screen in the y-axis direction");
			}
			Log.w("gameSupport bg", "bg sprite location [" + xRan + "," + yRan
					+ "]");
		}

		spt.gameScene.attachChild(bg);
	}
	
	
	class MissleOrigin extends Sprite {
		
		public MissleOrigin(float x, float y, float angle, ITextureRegion it, VertexBufferObjectManager vbo){
			super(x,y,it,vbo);
			

			this.setScale(0.5f);
			this.setAlpha(0.5f);
			

			final FixtureDef missleOriginObjectFixtureDef = PhysicsFactory
					.createFixtureDef(spt.DENSE_EAR, 0.0f, 0.5f, true, spt.CATEGORY_BIT_DEFAULT, spt.MASK_BIT_DEFAULT, (short)0);
			final Body bMissleOrigin = PhysicsFactory.createBoxBody(spt.mg.mPhysicsWorld,
					this, BodyType.DynamicBody, missleOriginObjectFixtureDef);

			boolean earth_allowPhysicsToInfluencePosition = true;
			boolean earth_updateRotationBaseOnPhysicalCollision = true;

			spt.mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
					this, bMissleOrigin, earth_allowPhysicsToInfluencePosition,
					earth_updateRotationBaseOnPhysicalCollision));
			
			bMissleOrigin.setTransform(bMissleOrigin.getPosition(), angle);
			
			ObjectMeta missleOriginMeta = new ObjectMeta("missleOrigin", this, bMissleOrigin);
			if(Level.missleOrigin!=null){
				missleOriginMeta.setEditorData(Level.missleOrigin);
			}
			this.setUserData(missleOriginMeta);
			bMissleOrigin.setUserData(missleOriginMeta);
		}
	}
	/*
	public MissleOrigin createMissleOrigin(float x, float y, float angle){
		
		MissleOrigin mo = new MissleOrigin(x,y,angle, spt.missleLaunchPoint, spt.mg.getEngine().getVertexBufferObjectManager());
		
		return mo;
	}*/
	
	
	public void createParallaxBackground(){
		ParallaxBackground back = new ParallaxBackground(0.3f,0.3f,0.9f){
			float cameraPreviousX = 0; 
			float cameraPreviousY = 0;
			float parallaxValueOffsetX = 0;
			float parallaxValueOffsetY = 0;


			@Override
			public void onUpdate(float pSecondsElapsed) {
				
				float cameraCurrentX = spt.mg.mCamera.getCenterX();
				float cameraCurrentY = spt.mg.mCamera.getCenterY();
				
				if(cameraCurrentX!=cameraPreviousX || 
						cameraCurrentY!=cameraPreviousY){
					parallaxValueOffsetX+= cameraCurrentX - cameraPreviousX;
					parallaxValueOffsetY+= cameraCurrentY - cameraPreviousY;
					this.setParallaxValue(parallaxValueOffsetX);
					cameraPreviousX = cameraCurrentX;
				}
				super.onUpdate(pSecondsElapsed);
			}
		};
		
	}
	
	public void createBackground(){
		
    	try{
    		
    		if(spt.backgroundTextureRepeating!=null)
    			spt.backgroundTextureRepeating.unload();
    		
    		if(spt.tex_backgroundTextureRepeatingTexture!=null)
    			spt.tex_backgroundTextureRepeatingTexture.getTexture().unload();
    		

    		
    		int backgroundIndex = (int)(Math.floor(Math.random()*4));
    		
    		if(backgroundIndex>3 || backgroundIndex<1){
    			backgroundIndex = 3;
    		}
    		
    		spt.backgroundTextureRepeating = new AssetBitmapTexture(spt.mg.getEngine().getTextureManager(), 
    				spt.mg.getAssets(), "gfx/background"+backgroundIndex+".jpg",BitmapTextureFormat.RGB_565, TextureOptions.REPEATING_BILINEAR);
    		
    		
    		
    		//fake-o half-tone clouds. Cool effect, but performance sucks
			/*
			ArrayList<Sprite> obj = new ArrayList<Sprite>();
			for(int i = 0; i<20; i++){
				double ranX = Math.random();
				double ranY = Math.random();
				
				final Sprite s = new Sprite(0f, 0f, spt.tex_circleParticleTextureRegion, spt.mg.getVertexBufferObjectManager());
				
				s.setScale(16f);
				s.setZIndex(1);
				s.setColor(Color.PINK);
				s.setAlpha(0.3f);
				//s.setCullingEnabled(true);
				
				if(obj.size()==0){
					s.setX((float)(ranX*spt.mg.mCameraWidth));
					s.setY((float)(ranY*spt.mg.mCameraHeight));
					//r.attachChild(s);
					spt.mg.mHud.attachChild(s);
					obj.add(s);
				}
				else {
					//randomly grab a point
					int ran = (int)(Math.random() * (obj.size()-1));
					final Sprite t = obj.get(ran);
					
					s.setX((float)(t.getX()+(Math.cos(ran)*t.getWidthScaled())));
					s.setY((float)(t.getY()+(Math.sin(ran)*t.getHeightScaled())));
					s.setScale(t.getScaleX()*0.7f);
					//r.attachChild(s);
					spt.mg.mHud.attachChild(s);
					obj.add(s);
				}
			}
			
			spt.mg.mHud.sortChildren();
    		*/
    		
    		
    	}
    	catch (IOException e){
    		Log.e("game support","error while loading repeating background: "+e.toString());
    	}
    	
    	//backgroundTextureRepeating.load();
    	
    	spt.tex_backgroundTextureRepeatingTexture = 
    			TextureRegionFactory.extractFromTexture(spt.backgroundTextureRepeating);

    	spt.background = new RepeatingSpriteBackground( spt.mg.mCameraWidth, spt.mg.mCameraHeight, spt.tex_backgroundTextureRepeatingTexture,0.8f, spt.mg.getEngine().getVertexBufferObjectManager());

    	spt.backgroundTextureRepeating.load();
		
		
		spt.gameScene.setBackground(spt.background);
		
		
		CircleParticleEmitter p = new CircleParticleEmitter(spt.mg.mCamera.getCenterX(), spt.mg.mCamera.getCenterY(), 256f);
		
		
		float rateMin = 0.1f;
		float rateMax = 3f;
		int maxParticles = 120;
		SpriteParticleSystem sp = new SpriteParticleSystem(
				p, rateMin, rateMax, maxParticles,
				spt.tex_circleStarTextureRegion, spt.mg
						.getVertexBufferObjectManager());
		
		
		float expireIn = 10f;
		float topAlpha = 0.4f;
		sp.addParticleInitializer(new ScaleParticleInitializer<Sprite>(0.0f));
		sp.addParticleInitializer(new AlphaParticleInitializer<Sprite>(topAlpha));
		sp.addParticleInitializer(new ExpireParticleInitializer<Sprite>(expireIn));
		
		float mod = 5;
		float minVelX = -14*mod;
		float maxVelX = 25f*mod;
		float minVelY = -15f*mod;
		float maxVelY = 14f*mod;
		sp.addParticleInitializer(new VelocityParticleInitializer<Sprite>(minVelX, maxVelX, minVelY, maxVelY));
		
		sp.addParticleModifier(new ScaleParticleModifier<Sprite>(0.0f, expireIn, 0.0f, 0.2f));
		sp.addParticleModifier(new AlphaParticleModifier<Sprite>(expireIn-10f, expireIn, topAlpha, 0.0f));
		sp.setZIndex(1);
		//spt.gameScene.attachChild(sp);
		spt.mg.mHud.attachChild(sp);
		
		
		/*
		PointParticleEmitter p = new PointParticleEmitter(0f, spt.mg.mCameraHeight);
		
		
		float rateMin = 0.1f;
		float rateMax = 3f;
		int maxParticles = 120;
		SpriteParticleSystem sp = new SpriteParticleSystem(
				p, rateMin, rateMax, maxParticles,
				spt.circleStarTextureRegion, spt.mg
						.getVertexBufferObjectManager());
		
		
		float expireIn = 10f;
		float topAlpha = 0.4f;
		sp.addParticleInitializer(new ScaleParticleInitializer<Sprite>(0.0f));
		sp.addParticleInitializer(new AlphaParticleInitializer<Sprite>(topAlpha));
		sp.addParticleInitializer(new ExpireParticleInitializer<Sprite>(expireIn));
		
		float mod = 5;
		float minVelX = 2*mod;
		float maxVelX = 25f*mod;
		float minVelY = -2f*mod;
		float maxVelY = -20f*mod;
		sp.addParticleInitializer(new VelocityParticleInitializer<Sprite>(minVelX, maxVelX, minVelY, maxVelY));
		
		sp.addParticleModifier(new ScaleParticleModifier<Sprite>(0.0f, expireIn, 0.0f, 0.2f));
		sp.addParticleModifier(new AlphaParticleModifier<Sprite>(expireIn-10f, expireIn, topAlpha, 0.0f));
		sp.setZIndex(1);
		//spt.gameScene.attachChild(sp);
		spt.mg.mHud.attachChild(sp);
		*/
		
		/*
		ParallaxLayer parallaxLayer = new ParallaxLayer(spt.mg.mCamera, true, 8000, false);
		
		parallaxLayer.setParallaxChangePerSecond(1.1f);
		parallaxLayer.setParallaxScrollFactor(1.0f);
		
		//parallaxLayer.attachChild(spt.createNaughtyLetter(256, 256, "X"));
		
		//parallaxLayer.attachChild(spt.createNaughtyLetter(400, 400, "X"));
		
		
		parallaxLayer.attachParallaxEntity(new ParallaxEntity(0.2f, new Sprite(0,0,
				spt.backgroundTextureRepeatingTexture,spt.mg.getVertexBufferObjectManager()),true));
		

		spt.gameScene.attachChild(parallaxLayer);*/

		
	}
	
	
	
	private void levelOne(){

		/*
		 * Set up background image. Background images are 4096w x 4096h, and
		 * lets randomize the location of the giant sprite so we get some
		 * variability. The camera is 800w x 480h
		 */

		createBackground();
		
		
		/*
		 * BEGIN EARTH
		 */
		createEarth(spt.mg.mCameraWidth *7/8,spt.mg.mCameraHeight / 2);
		
		
		
		
		
		spt.pause();
		
		spt.mg.getEngine().registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler arg0) {
				
				final float windowWidth = 420;
				final float windowHeight = 128;
				
				Sprite handPointerSprite = new Sprite(0, windowHeight/3, spt.tex_circleParticleTextureRegion, spt.mg
						.getEngine().getVertexBufferObjectManager());
				
				handPointerSprite.setScale(0.5f);
				handPointerSprite.setPosition(windowWidth - handPointerSprite.getWidthScaled(), windowHeight);
				
				ArrayList<IEntity> buttons = new ArrayList<IEntity>();
				buttons.add(handPointerSprite);
				
				final JWOW_Menu jm = new JWOW_Menu(spt.mg);
				Rectangle touchToFireDialog = jm.showDialog("Touch the screen to\nfire a missle at earth", spt.mg.mCameraWidth/2, spt.mg.mCameraHeight*2/3, windowWidth, windowHeight,null, new IOnClickCallback() {
					@Override
					public void onClick(Object objectClicked) {
						jm.disposeWindow((JWOW_Button)objectClicked);
						jm.showDialog("Tilt to steer", spt.mg.mCameraWidth/2, spt.mg.mCameraHeight*2/3, windowWidth, windowHeight, null,new IOnClickCallback() {
							@Override
							public void onClick(Object objectClicked) {
								spt.createSatellite(spt.mg.mCameraWidth/2, spt.mg.mCameraHeight/2);
								
								jm.disposeWindow((JWOW_Button)objectClicked);
								jm.showDialog("Watch out for defenses", spt.mg.mCameraWidth/2, spt.mg.mCameraHeight*2/3, windowWidth, windowHeight, null, new IOnClickCallback() {
									@Override
									public void onClick(Object objectClicked) {
										jm.disposeWindow((JWOW_Button)objectClicked);
										spt.pause();
										
									}
								});
								
							}
						});
						
					}
				});
				
				touchToFireDialog.attachChild(handPointerSprite);
				
			}
		}));
		
		
		
	
	}

	
	
	
	/*
	 * 
	 * Level Editor MODE!
	 * 
	 */
	
	/* 
	 * Level Editor
	 */
	
	Integer initX;
	Integer initY;
	Float initXSwipe;
	Float initYSwipe;
	
	boolean isDown = false;
	long timeDownStart = 0;
	
	//Rectangler user data contains Vector2
	//this is just for editor purposes only,
	//this doesn't get saved.
	boolean isWallModeStrip = false;
	ArrayList<Rectangle> wallstripPoints = new ArrayList<Rectangle>();
	
	public enum EditorMode {
		SATELLITE,WALL,MOVE,NAVIGATE,MODIFY,TRIGGER, CREATE_EARTH, CREATE_POWERUP, ROCKET, ENEMY, WARP
	}
	
	boolean showSatelliteOption     = false;
	boolean showWallOption          = true;
	boolean showMoveOption          = true;
	boolean showNavigateOption      = true;
	boolean showModifyOption        = true;
	boolean showTriggerOption       = false;
	boolean showCreateEarthOption   = true;
	boolean showCreatePowerupOption = true;
	boolean showCreateRocketOption  = true;
	boolean showCreateEnemyOption   = false;
	boolean showCreateWarpOption    = true;
	
	static EditorMode mode = EditorMode.ROCKET;
	//Rectangle selectedWall = null;
	
	
	
	/*
	 * Trigger - based off of the Wall class, except it is invisible and
	 * works to trigger various events. Currently I'm thinking we can
	 * trigger some text to the screen or trigger camera perspective changes.
	 */
	class Trigger extends Rectangle{
		
		 public Trigger(float x, float y, float width, float height, double angle){
	
			 
			 super(x, y, width, height, spt.mg.getVertexBufferObjectManager());
			
			 
			 this.setColor(1.0f,1.0f,0.0f);
			 

			S_Trigger s_trigger;
		
			this.setRotationCenter(0.5f, 0.5f);
			
			spt.gameScene.registerTouchArea(this);
			

			float density    = 100.0f;
	      	float elasticity = 0.0f;
	      	float friction   = 0.1f;
	      	boolean isSensor = true;
	        final FixtureDef sat_objectFixtureDef = PhysicsFactory.createFixtureDef( density, elasticity, friction, isSensor, 
	        		spt.CATEGORY_BIT_TRIGGER, spt.MASK_BIT_TRIGGER, (short)0);

	        final Body bBody = PhysicsFactory.createBoxBody(spt.mg.mPhysicsWorld,this,BodyType.DynamicBody,sat_objectFixtureDef);
	        spt.mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this,bBody,true, true)); //physics influences pos, rot?
	        
	        
	        
	        bBody.setTransform(x/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
	        		y/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, (float)angle);
	        
	        ObjectMeta om = new ObjectMeta("trigger",this,bBody);
	        s_trigger = new S_Trigger();
	        s_trigger.height = height;
	        s_trigger.width = width;
	        s_trigger.x = x;
	        s_trigger.y = y;
	        s_trigger.rotation = this.getRotation();
	        om.setEditorData(s_trigger);
	        this.setUserData(om);
	        bBody.setUserData(om);
	        
	        
	        spt.gameScene.attachChild(this);
	        /*
			 
			 super(x, y, width, height, spt.mg.getVertexBufferObjectManager());
				
				

				S_Wall s_wall;
		
			this.setRotationCenter(0.5f, 0.5f);
			this.setCullingEnabled(true);
			
			
			spt.gameScene.registerTouchArea(this);
			
	
			this.setColor(1f, 1f, 1f);
			
			
			float density    = 100.0f;
	      	 float elasticity = 0.0f;
	      	 float friction   = 0.1f;

	        final Body bWall = PhysicsFactory.createBoxBody(spt.mg.mPhysicsWorld,this,BodyType.DynamicBody,sat_objectFixtureDef);

	        spt.mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this,bWall,true, true)); //physics influences pos, rot?
	        
	        bWall.setTransform(x/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
	        		y/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, (float)angle);
	        
	        ObjectMeta om = new ObjectMeta("wall",this,bWall);
	        s_wall = new S_Wall();
	        s_wall.height = height;
	        s_wall.width = width;
	        s_wall.x = x;
	        s_wall.y = y;
	        s_wall.rotation = this.getRotation();
	        om.setEditorData(s_wall);
	        this.setUserData(om);
	        bWall.setUserData(om);
	        
	        spt.gameScene.attachChild(this);
	        */

		}
	}
	
	public Trigger createTrigger(float x, float y, float width, float height, double angle){
		final Trigger trigger = new Trigger(x,y,width,height,angle){
			
			public boolean onAreaTouched(
					TouchEvent t,
					float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				
			
				final ObjectMeta om = (ObjectMeta)getUserData();
				
				if(t.isActionUp() && mode == EditorMode.MODIFY){
					
	
					
					final String[] items = new String[]{"Trigger text","Trigger zoom"};
					
					if(t.isActionUp()){
						spt.mg.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								
								final S_Trigger trig = ((S_Trigger)om.getEditorData());
								
								boolean[] bools = new boolean[]{trig.text!=null,
										                        trig.zoom!=null};
								
								new AlertDialog.Builder(spt.mg)
								.setTitle("Edit Trigger")
								.setMultiChoiceItems(items, bools, new OnMultiChoiceClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which, boolean isChecked) {
										
										switch(which){
											case 0:
												if(!isChecked){
													trig.text = null;
													break;
												}
												
												final EditText et = new EditText(spt.mg);
												if(trig.text!=null){
													et.setText(trig.text);
												}
												new AlertDialog.Builder(spt.mg)
													.setTitle("Set Trigger Text")
													.setView(et)
													.setMessage("Input text to show when triggered")
													.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
														@Override
														public void onClick(DialogInterface dialog, int which) {
															trig.text = et.getText().toString();
															Toast.makeText(spt.mg,"Trigger text set to:"+et.getText().toString(),Toast.LENGTH_SHORT).show();
														}
													})
													.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
														
														@Override
														public void onClick(DialogInterface dialog, int which) {
															dialog.dismiss();
														}
													})
													.show();
												break;
												
												
											case 1:
												if(!isChecked){
													trig.zoom = null;
													break;
												}
												final EditText et1 = new EditText(spt.mg);
											
												if(trig.zoom!=null){
													et1.setText(trig.zoom+"");
												}
												new AlertDialog.Builder(spt.mg)
													.setTitle("Set Trigger Zoom")
													.setView(et1)
													.setMessage("Input camera zoom")
													.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
														@Override
														public void onClick(DialogInterface dialog, int which) {
															trig.zoom = Float.parseFloat(et1.getText().toString());
															Toast.makeText(spt.mg,"Trigger zoom set to:"+et1.getText().toString(),Toast.LENGTH_SHORT).show();
														}
													})
													.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
														
														@Override
														public void onClick(DialogInterface dialog, int which) {
															dialog.dismiss();
														}
													})
													.show();
												break;
											default:
												break;
										}
										
									}
								})
								.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								})
								.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
	
										spt.mg.runOnUpdateThread(new Runnable() {
												
											@Override
											public void run() {
												detachSelf();
													
												om.getBody().setActive(false);
												spt.mg.mPhysicsWorld.destroyBody(om.getBody());
												int foundAtIndex = -1;
													for(int i = 0; i<Level.triggers.size(); i++){
														final S_Trigger w = Level.triggers.get(i);
														if(w.uniqueId==null || w.uniqueId.equals(((S_Trigger)om.getEditorData()).uniqueId)){
															foundAtIndex = i;
															break;
																
														}
			
													}
													if(foundAtIndex>=0){
														Level.triggers.remove(foundAtIndex);
													}
													
														
													
											}
										});
										
									}
								})
								.show();
								
							}
						});
					}
					
				}
				
				return true;
			}
		};
		
		if(isCreator==true){
			trigger.setAlpha(0.4f);
		}
		else {
			trigger.setVisible(false);
		}
		
		return trigger;
	}
	
	
	class Warp extends Rectangle{
		
		ObjectMeta om;
		
		public Warp(float x, float y){
			super(x, y,128f, 128f, spt.mg.getVertexBufferObjectManager());
			
			this.setCullingEnabled(true);
			
			S_Warp s_warp;
			
			float density    = 100.0f;
	      	float elasticity = 0.0f;
	      	float friction   = 0.1f;
	      	boolean isSensor = true;
	        final FixtureDef sat_objectFixtureDef = PhysicsFactory.createFixtureDef( density, elasticity, friction, isSensor, 
	        		spt.CATEGORY_BIT_WARP, spt.MASK_BIT_WARP, (short)0);
	        
	        final Body bBody = PhysicsFactory.createBoxBody(spt.mg.mPhysicsWorld,this,BodyType.DynamicBody,sat_objectFixtureDef);
	        spt.mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this,bBody,true, true)); //physics influences pos, rot?
	        
	        bBody.setTransform(x/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
	        		y/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0.0f);
	        
	        om = new ObjectMeta("warp",this,bBody);
	        s_warp = new S_Warp();
	        s_warp.x = x;
	        s_warp.y = y;
	        om.setEditorData(s_warp);
	        this.setUserData(om);
	        bBody.setUserData(om);
		}
		
		public void showLabel(){
			if(isCreator){
				S_Warp wh = (S_Warp)((ObjectMeta)getUserData()).getEditorData();
				final Text nameLabel = new Text(0f,0f,spt.mg.mFont,wh.name, spt.mg.getVertexBufferObjectManager());
				nameLabel.setScale(0.7f);
				nameLabel.setCullingEnabled(true);
				//spt.mg.sm.getCurrentSceneObject().attachChild(nameLabel);
				spt.gameScene.attachChild(nameLabel);
				om.getAffiliatedObj().add(nameLabel);
				registerUpdateHandler(new IUpdateHandler() {
					
					@Override
					public void reset() {}
					
					@Override
					public void onUpdate(float pSecondsElapsed) {
						nameLabel.setPosition(getX(), getY()+getHeightScaled()/2);
						
					}
				});
				
			}
		}
	}
	
	class Powerup extends Sprite{
		
		ObjectMeta om;
		
		 public Powerup(float x, float y,ITextureRegion t, float scale){
			 
			super(x, y, t, spt.mg.getVertexBufferObjectManager());
			
			S_Powerup s_powerup;
		
			
			this.setScale(scale);

			float density    = 300.1f;
	      	float elasticity = 1.1f;
	      	float friction   = 0.1f;
	      	boolean isSensor = true;
	        final FixtureDef sat_objectFixtureDef = PhysicsFactory.createFixtureDef( density, elasticity, friction, isSensor, 
	        		spt.CATEGORY_BIT_POWERUP, spt.MASK_BIT_POWERUP, (short)0);

	        final Body bBody = PhysicsFactory.createBoxBody(spt.mg.mPhysicsWorld,this,BodyType.DynamicBody,sat_objectFixtureDef);
	        spt.mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this,bBody,true, false)); //physics influences pos, rot?
	        
	        

	        bBody.setTransform(x/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
	        		y/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0.0f);
	        
	        om = new ObjectMeta("powerup",this,bBody);
	        s_powerup = new S_Powerup();
	        s_powerup.x = x;
	        s_powerup.y = y;
	        om.setEditorData(s_powerup);
	        this.setUserData(om);
	        bBody.setUserData(om);
	        

		}
	}
	
	class Wall extends Sprite{
	
		 public Wall(float x, float y, float width, float height, double angle){
			 //for clip entity
			 //super(x, y, width, height);

			
			 //for rectangle
			//super(x, y, width, height, spt.mg.getVertexBufferObjectManager());
			 
			 
			 //for entity
			 super(x, y, width, height, spt.tex_rockWall1TextureRegion, spt.mg.getVertexBufferObjectManager());
			 
			 //for textured mesh 2 triangles in clock wise motion
			// float[] buffer = new float[]{x-width/2, y+height/2, x+width/2, y+width/2, x+width/2, y-width/2,
					 					 // x-width/2, y+height/2, x+width/2, y-width/2, x-width/2, y-width/2};
			 //super(x,y,buffer, buffer.length/2, DrawMode.TRIANGLES, spt.mg.getVertexBufferObjectManager());
			 


			S_Wall s_wall;
		
			this.setRotationCenter(0.5f, 0.5f);
			this.setCullingEnabled(true);
			
			
			spt.gameScene.registerTouchArea(this);
			
	
			

			//this.setColor((float)Math.random(),(float)Math.random(),(float)Math.random());
			
			
			this.setAlpha(0.8f);
			
			
			//this.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);
			//this.setBlendingEnabled(true);
			//this.setClippingEnabled(true);
			
			float density    = 100.0f;
	      	 float elasticity = 0.0f;
	      	 float friction   = 0.1f;
	        final FixtureDef sat_objectFixtureDef = PhysicsFactory.createFixtureDef( density, elasticity, friction, false, spt.CATEGORY_BIT_WALL, spt.MASK_BIT_WALL, (short)0);

	        final Body bWall = PhysicsFactory.createBoxBody(spt.mg.mPhysicsWorld,this,BodyType.KinematicBody,sat_objectFixtureDef);
	        //final Body bWall = PhysicsFactory.createBoxBody(spt.mg.mPhysicsWorld,this.getX(), this.getY(), width*2, height*2,BodyType.KinematicBody,sat_objectFixtureDef);

	        spt.mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this,bWall,true, true)); //physics influences pos, rot?
	        
	        bWall.setTransform(x/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
	        		y/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, (float)angle);
	        
	        ObjectMeta om = new ObjectMeta("wall",this,bWall);
	        s_wall = new S_Wall();
	        s_wall.height = height;
	        s_wall.width = width;
	        s_wall.x = x;
	        s_wall.y = y;
	        s_wall.rotation = this.getRotation();
	        om.setEditorData(s_wall);
	        this.setUserData(om);
	        bWall.setUserData(om);
	        
	        
	        this.registerEntityModifier(new ParallelEntityModifier(new AlphaModifier(0.5f,0.0f,1.0f), 
	        		             new ScaleModifier(0.5f, 3.0f, 1.0f)));
	        
	        

	        
	        spt.gameScene.attachChild(this);
	        
	/*
	        float wjDensity = 0.1f;
	        float wjElasticity = 0.1f;
	        float wjFriction   = 0.1f;
	        
	        final FixtureDef wjFixture0 = PhysicsFactory.createFixtureDef(wjDensity, wjElasticity, wjFriction);
	        final FixtureDef wjFixture1 = PhysicsFactory.createFixtureDef(wjDensity, wjElasticity, wjFriction);
	        
	        
	        
	        wjFixture0.isSensor = true;
	        wjFixture1.isSensor = true;
	        
	        
	        CircleShape s0 = new CircleShape();
	        CircleShape s1 = new CircleShape();
	        
	        s0.setRadius(0.4f);
	        s1.setRadius(0.4f);
	        
	        Vector2 leftPoint  = Vector2Pool.obtain(-0.5f*width/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0.0f);
	        Vector2 rightPoint = Vector2Pool.obtain(0.5f*width/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0.0f);
	        
	        s0.setPosition(leftPoint);
	        s1.setPosition(rightPoint);
	        
	        Vector2Pool.recycle(leftPoint);
	        Vector2Pool.recycle(rightPoint);
	        
	        Fixture fixture0 = bWall.createFixture(s0,wjDensity);
	        Fixture fixture1 = bWall.createFixture(s1,wjDensity);
	        
	        fixture0.setUserData("wallJoint");
	        fixture1.setUserData("wallJoint");
	        
	        float anchor_density    = 1.0f;
	        float anchor_elasticity = 1.0f;
	        float anchor_friction   = 0.0f;
	        
	        
	        
	        */
	        
	        
	        
	       
	        
		}
		 

	}
	
	private void renderWallStrip(){
		
		if(wallstripPoints.size()<2){
			spt.mg.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					new AlertDialog.Builder(spt.mg)
						.setTitle("Message")
						.setMessage("Please place at least 2 wall points!")
						.setPositiveButton("Ok", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								dialog.dismiss();
							}
						})
						.show();
				}
			});
		}
		else {
			spt.mg.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					for(int i = 0;i<wallstripPoints.size()-1; i++){
						
						final Vector2 a = (Vector2)wallstripPoints.get(i).getUserData();
						final Vector2 b = (Vector2)wallstripPoints.get(i+1).getUserData();
						
						
						final float height= 24f;
						final float width = a.dst(b.x, b.y)+height; //height is added in for some nice overlap
						final float xWall = (a.x+b.x)/2;
						final float yWall = (a.y+b.y)/2;
						
						float xPrime = b.x-a.x;
						float yPrime = b.y-a.y;
						Vector2 c = Vector2Pool.obtain(xPrime, yPrime);
						
						final  float angle = spt.getAngle(spt.zeroVector, c);
		
						Wall w = createWall(xWall, yWall, width, height, angle);
						S_Wall s_wall = (S_Wall) ((ObjectMeta)w.getUserData()).getEditorData();
						s_wall.rotation = angle;
						Level.walls.add(s_wall);
						Vector2Pool.recycle(c);
					}
					
					for(Rectangle r:wallstripPoints){
						r.setColor(Color.WHITE);
					}
					wallstripPoints.clear();
						
						
					
					
				}
			});
		}
	}
	
	
	public Wall createWall(float x, float y, float width, float height, double angle){
		final Wall wall = new Wall(x, y, width, height, angle){
    		@Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                            float pTouchAreaLocalX, float pTouchAreaLocalY){
				
				
				boolean consumeTouchEvent = false;
				final ObjectMeta om = (ObjectMeta)this.getUserData();
				final S_Wall s_wall = (S_Wall) om.getEditorData();
				
				if(pSceneTouchEvent.isActionMove()){
					
					if(mode == EditorMode.MOVE){
						
						s_wall.x = pSceneTouchEvent.getX();
						s_wall.y = pSceneTouchEvent.getY();
						s_wall.rotation = om.getBody().getAngle();
					
						om.getBody().setTransform((pSceneTouchEvent.getX())/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
								(pSceneTouchEvent.getY())/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, om.getBody().getAngle());
						
				
						Log.w("local touch area", pSceneTouchEvent.getX()+","+pSceneTouchEvent.getY());
						
						
						/* too verbose
						if(Globals.instance().isDebug()){
							Log.w("Wall touched", " dragging wall to ["+pSceneTouchEvent.getX()+","+pSceneTouchEvent.getY()+"]");
						}
						*/
						consumeTouchEvent = true;
					}
				}
				else if(pSceneTouchEvent.isActionUp() && LevelFactory.mode == EditorMode.MODIFY){

						
						

						consumeTouchEvent = true;
				
						spt.mg.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
									
									SeekBar rotateSeekBar = new SeekBar(spt.mg);
									rotateSeekBar.setMax(100);
									rotateSeekBar.setProgress(50);
									rotateSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
										
										int lastProgress = 50;
										
										@Override
										public void onStopTrackingTouch(SeekBar seekBar) {
											
										}
										
										@Override
										public void onStartTrackingTouch(SeekBar seekBar) {}
										
										@Override
										public void onProgressChanged(SeekBar seekBar, final int progress,
												boolean fromUser) {
										
												float diff = ((float)progress - lastProgress)/100;
												
												final float addToRotation = (float)(diff * 1/2*Math.PI);
												final float finalRot = om.getBody().getAngle()+addToRotation;
												final ObjectMeta om = (ObjectMeta)getUserData();
												
												spt.mg.runOnUpdateThread(new Runnable() {
													
													@Override
													public void run() {
														om.getBody().setTransform(om.getBody().getPosition().x ,
																om.getBody().getPosition().y, finalRot);
														S_Wall s_wall = ((S_Wall)om.getEditorData());
														s_wall.rotation = s_wall.rotation + addToRotation;
														
														Log.w("rotate","rotating wall to ["+getX()+","+getY()+"] rot="+finalRot);
														
														lastProgress = progress;
														
													}
												});
												
												
												
												
											
											
										}
									});
									
									
								
									new AlertDialog.Builder(spt.mg)
									 .setTitle("Wall Editor")
									 .setMessage("x = "+s_wall.x+", y = "+s_wall.y+" rot = "+(s_wall.rotation * Math.PI/180))
									 .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											dialog.dismiss();
										}
									 
									})
									.setView(rotateSeekBar)
									.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
						
												
												spt.mg.runOnUpdateThread(new Runnable() {
													
													@Override
													public void run() {
														detachSelf();
														
														om.getBody().setActive(false);
														int foundAtIndex = -1;
															for(int i = 0; i<Level.walls.size(); i++){
																final S_Wall w = Level.walls.get(i);
																if(w.uniqueId==null || w.uniqueId.equals(((S_Wall)om.getEditorData()).uniqueId)){
																	foundAtIndex = i;
																	break;
																	
																}
				
															}
															if(foundAtIndex>=0){
																Level.walls.remove(foundAtIndex);
															}
								
														
														
													}
												});
												
											
											dialog.dismiss();
										}
									})
									.show();
								
								
							}
								
							
						
					});
					 
					
				}
				
				
				
				
				
				
        		return consumeTouchEvent;
			};
    	};
    	/*
    	
    	Sprite r = new Sprite(0/8f*wall.getWidth(), 
				(float)(wall.getHeight()/2+Math.random()*wall.getHeight()/2),spt.wallJoint, spt.mg.getVertexBufferObjectManager());
		r.setScale(0.10f);
    	
    	
    	
    	int barnacles = (int)(wall.getWidth()/r.getWidthScaled());
    	
    	
    	
    	//int barnacles = (int)(wall.getWidth()/16f);
    	
    	
		for(int i = 0; i<barnacles; i++){

			
			r = new Sprite(i/8f*wall.getWidth(), 
					(float)(wall.getHeight()/2+Math.random()*wall.getHeight()/2),spt.wallJoint, spt.mg.getVertexBufferObjectManager());
			r.setScale(0.10f);
			
			
			wall.attachChild(r);
			((ObjectMeta)wall.getUserData()).getAffiliatedObj().add(r);
		}
    	
		*/
    	
    	

    	return wall; 
	}
	
	JWOW_Button buttonWallSwipe = null;
	JWOW_Button buttonWallStrip = null;
	Rectangle wallOptionsContainer = null;
	
	private void createWallHUDOptions(){
		
		spt.mg.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {

				if(wallOptionsContainer==null){
					wallOptionsContainer = new Rectangle(128.0f, 128.0f, 128f, 128f, spt.mg.getVertexBufferObjectManager());
				}
				
				if(buttonWallSwipe==null){
					buttonWallSwipe = new JWOW_Button(0.0f, 0.0f, 128f, 64f, spt.tex_buttonUpDownTextureRegion, spt.mg.getVertexBufferObjectManager()){
						@Override
						public boolean onAreaTouched(
								TouchEvent touchEvent,
								float touchX,
								float touchY) {
							if(touchEvent.isActionUp()){
								isWallModeStrip = false;
								GameSupport.soundDoot.play();
								Log.d("createWallHUDOptions", "isWallModeStrip = "+isWallModeStrip);
							}
							return true;
						}
					};
					final Text t = new Text(buttonWallSwipe.getWidthScaled()/2, buttonWallSwipe.getHeightScaled()/2, spt.mg.mFont, "Swipe", spt.mg.getVertexBufferObjectManager());
					t.setScale(0.5f);
					buttonWallSwipe.attachChild(t);
					buttonWallSwipe.setPosition(64f, 128-buttonWallSwipe.getHeight()/2f);
					wallOptionsContainer.attachChild(buttonWallSwipe);
				}
				if(buttonWallStrip==null){
					buttonWallStrip = new JWOW_Button(0.0f, 0.0f, 128f, 64f, spt.tex_buttonUpDownTextureRegion, spt.mg.getVertexBufferObjectManager()){
						@Override
						public boolean onAreaTouched(
								TouchEvent touchEvent,
								float touchX,
								float touchY) {
							if(touchEvent.isActionUp()){
								if(isWallModeStrip){
									Log.d("createWallHUDOptions", "rendering wall strip");
									renderWallStrip();
								}
								else {
									isWallModeStrip = true;
									GameSupport.soundDoot.play();
								}
								
								Log.d("createWallHUDOptions", "isWallModeStrip = "+isWallModeStrip);
							}
							return true;
						}
					};
					final Text t = new Text(buttonWallStrip.getWidthScaled()/2, buttonWallStrip.getHeightScaled()/2, spt.mg.mFont, "Strip", spt.mg.getVertexBufferObjectManager());
					t.setScale(0.5f);
					buttonWallStrip.attachChild(t);
					buttonWallStrip.setPosition(64f, 64-buttonWallStrip.getHeight()/2f);
					wallOptionsContainer.attachChild(buttonWallStrip);
				}
				
				spt.mg.mHud.attachChild(wallOptionsContainer);
				spt.mg.mHud.registerTouchArea(buttonWallSwipe);
				spt.mg.mHud.registerTouchArea(buttonWallStrip);
			}
		});
		
		 
	}
	
	private void destroyWallHUDOptions(){
		if(wallOptionsContainer!=null){
			spt.mg.runOnUpdateThread(new Runnable() {
				
				@Override
				public void run() {
					spt.mg.mHud.detachChild(wallOptionsContainer);
					spt.mg.mHud.unregisterTouchArea(buttonWallSwipe);
					spt.mg.mHud.unregisterTouchArea(buttonWallStrip);
				}
			});
			
		}
	}
	
	
	
	private class AsyncXMLOortCloudHttpPost extends AsyncTask<String, Void, String> {

	    /** The system calls this to perform work in a worker thread and
	      * delivers it the parameters given to AsyncTask.execute() */
	    protected String doInBackground(String... xml) {
	        return LevelUtils.saveToOortCloud(xml[0], true);
	    }
	    
	    /** The system calls this to perform work in the UI thread and delivers
	      * the result from doInBackground() */
	    protected void onPostExecute(String result) {

	    }
	}
	
	private class AsyncXMLUserOortCloudHttpPost extends AsyncTask<String, Void, String> {

	    /** The system calls this to perform work in a worker thread and
	      * delivers it the parameters given to AsyncTask.execute() */
	    protected String doInBackground(String... xml) {
	        return LevelUtils.saveToOortCloud(xml[0], false);
	    }
	    
	    /** The system calls this to perform work in the UI thread and delivers
	      * the result from doInBackground() */
	    protected void onPostExecute(String result) {

	    }
	}
	
	private class AsyncXMLRequestFileName extends AsyncTask<String, Void, String>{
		/** The system calls this to perform work in a worker thread and
	      * delivers it the parameters given to AsyncTask.execute() */
	    protected String doInBackground(String... xml) {
	        return LevelUtils.requestFileName();
	    }
	    
	    /** The system calls this to perform work in the UI thread and delivers
	      * the result from doInBackground() */
	    protected void onPostExecute(String result) {

	    }
	}
	
	
	
	
	
	public static boolean isCreator = false;
	final float ZOOM_INCREMENT = 0.3f;
	

	private String checkIntegrityOfUserLevel(){
		String error = null;
		
		if(Level.walls.size()<4){	
			error = "You must create more walls before you may publish to the Oort cloud";
		}
		
		if(Level.powerups.size()==0){
			error = "You must create more powerups before you may publish to the Oort cloud";
		}
		
		if(Level.fileName==null || Level.fileName.length()==0){
			error = "Please input a unique file name";
		}
		
		return error;
	}
	
	
	private void levelEditor(){
		
		
		final float scale = 0.3f;
		final float offset= 32f;
		spt.mg.mCamera.setZoomFactor(0.7f);
		

		if(Level.creatorId.equalsIgnoreCase(LevelUtils.getCreatorId()) || Globals.instance().isDebug()){
			isCreator = true;

		}
		else {
			isCreator = false;
		}
		
		Log.w("isCreator?","LevelCreatorUuid="+Level.creatorId+", creatorId="+LevelUtils.getCreatorId()+", isCreator="+isCreator+"");
		
		/*
		if(isCreator){
			mode = EditorMode.NAVIGATE;
		}
		else {
			mode = EditorMode.ROCKET;
		}*/

		/*
		if(Globals.instance().isDebugDrawingEnabled()){
			DebugRenderer debug = new DebugRenderer(spt.mg.mPhysicsWorld, spt.mg.getVertexBufferObjectManager());
			spt.gameScene.attachChild(debug);
		}*/
		
		
		
		

		
		
		
		
		final MissleOrigin missleOrigin = new MissleOrigin(Level.missleOrigin.x, 
				Level.missleOrigin.y, Level.missleOriginAngle,spt.tex_missleLaunchPoint, spt.mg.getVertexBufferObjectManager()){
			ObjectMeta om = null;
			

			
			@Override
			public boolean onAreaTouched(
					TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				
				om = (ObjectMeta)this.getUserData();
				
				if(LevelFactory.mode == EditorMode.MOVE){
					
					
					if(pSceneTouchEvent.isActionMove()){
						om.getBody().setTransform(pSceneTouchEvent.getX()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
								pSceneTouchEvent.getY()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, om.getBody().getAngle());
						
						((Vector2)om.getEditorData()).x = pSceneTouchEvent.getX();
						((Vector2)om.getEditorData()).y = pSceneTouchEvent.getY();
				
					}
				}
				else if(pSceneTouchEvent.isActionUp() && LevelFactory.mode == EditorMode.MODIFY){
					
					if(pSceneTouchEvent.isActionUp()){
						spt.mg.runOnUiThread(new Runnable() {
							public void run() {
								
								SeekBar rotateSeekBar = new SeekBar(spt.mg);
								rotateSeekBar.setMax(100);
								rotateSeekBar.setProgress(50);
								rotateSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
									
									int lastProgress = 50;
									
									@Override
									public void onStopTrackingTouch(SeekBar seekBar) {
										
									}
									
									@Override
									public void onStartTrackingTouch(SeekBar seekBar) {}
									
									@Override
									public void onProgressChanged(SeekBar seekBar, final int progress,
											boolean fromUser) {
										
											float diff = ((float)progress - lastProgress)/100;
											
											final float addToRotation = (float)(2*diff * 1/2*Math.PI);
											final float finalRot = om.getBody().getAngle()+addToRotation;
											final ObjectMeta om = (ObjectMeta)getUserData();
											
											spt.mg.runOnUpdateThread(new Runnable() {
												
												@Override
												public void run() {
													om.getBody().setTransform(om.getBody().getPosition().x ,
															om.getBody().getPosition().y, finalRot);
													//S_Wall s_wall = ((S_Wall)om.getEditorData());
													//s_wall.rotation = s_wall.rotation + addToRotation;
													Level.missleOriginAngle = finalRot;  
													
													Log.w("rotate","rotating missle origin to ["+getX()+","+getY()+"] rot="+finalRot);
													
													lastProgress = progress;
													
												}
											});
											
									}
								});
								
								
								new AlertDialog.Builder(spt.mg)
								 .setTitle("Missle Origin Editor")
								 .setMessage("x = "+Level.missleOrigin.x+", y = "+Level.missleOrigin.y+" rot = "+(Level.missleOriginAngle * Math.PI/180))
								 .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								 
								})
								.setView(rotateSeekBar)
								.show();
								
							}
						});
					}
				}
				
				
				return super
						.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		
		if(isCreator)
			spt.gameScene.registerTouchArea(missleOrigin);
		
		spt.gameScene.attachChild(missleOrigin);
		spt.mg.mCamera.setCenterDirect(Level.missleOrigin.x, Level.missleOrigin.y);
		
		/* Make the toolbar buttons 
		 * so user can switch between modes
		 * */
		
		JWOW_ButtonFactory bf = new JWOW_ButtonFactory(spt.mg);
		
		JWOW_Button modeButton = bf.init("Mode", 100, 32, new IOnClickCallback() {
			
			@Override
			public void onClick(Object clickedObject) {
				
				spt.mg.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						AlertDialog.Builder b = new AlertDialog.Builder(spt.mg);
						
						ArrayList<String> options = new ArrayList<String>();
						if(showNavigateOption) options.add("Navigate");
						if(showMoveOption)     options.add("Move");
						if(showModifyOption)   options.add("Modify/Delete/Rotate");
						if(showWallOption)     options.add("Swipe to create Wall");
						if(showTriggerOption)  options.add("Trigger");
						if(showSatelliteOption)options.add("Satellite");
						if(showCreateEarthOption) options.add("Touch to move Earth");
						if(showCreatePowerupOption) options.add("Create asteroid");
						if(showCreateRocketOption)  options.add("Spaceship");
						if(showCreateEnemyOption)   options.add("Enemy");
						if(showCreateWarpOption)    options.add("Touch to create warp");
						

						final Object[] tempItems = options.toArray();
						final String[] items = new String[tempItems.length];
						for(int i=0; i<items.length;i++){
							items[i] = tempItems[i].toString();
						}
						
						b.setItems(items, new DialogInterface.OnClickListener() {
							
							@Override
									public void onClick(DialogInterface dialog,int which) {
								
									
										if(LevelFactory.mode != EditorMode.WALL && 
												items[which].equalsIgnoreCase("Swipe to create Wall")){
											
											createWallHUDOptions();
										}
										else {
											destroyWallHUDOptions();
										}
										
										
										
										
								
										if(items[which].equalsIgnoreCase("Navigate")){
											LevelFactory.mode = EditorMode.NAVIGATE;
										}
										if(items[which].equalsIgnoreCase("Move")){
											LevelFactory.mode = EditorMode.MOVE;
										}
										else if(items[which].equalsIgnoreCase("Modify/Delete/Rotate")){
											LevelFactory.mode = EditorMode.MODIFY;
										}
										else if(items[which].equalsIgnoreCase("Swipe to create Wall")){
											LevelFactory.mode = EditorMode.WALL;
										}
										else if(items[which].equalsIgnoreCase("Trigger")){
											LevelFactory.mode = EditorMode.TRIGGER;
										}
										else if(items[which].equalsIgnoreCase("Satellite")){
											LevelFactory.mode = EditorMode.SATELLITE;
										}
										else if(items[which].equalsIgnoreCase("Touch to move Earth")){
											LevelFactory.mode = EditorMode.CREATE_EARTH;
										}
										else if(items[which].equalsIgnoreCase("Create asteroid")){
											LevelFactory.mode = EditorMode.CREATE_POWERUP;
										}
										else if(items[which].equalsIgnoreCase("Spaceship")){
											LevelFactory.mode = EditorMode.ROCKET;
										}
										else if(items[which].equalsIgnoreCase("Enemy")){
											LevelFactory.mode = EditorMode.ENEMY;
										}
										else if(items[which].equalsIgnoreCase("Touch to create warp")){
											LevelFactory.mode = EditorMode.WARP;
										}
										
										


										//reset any gesture variables when switching modes.
										initX = null;
										initY = null;
										initXSwipe = null;
										initYSwipe = null;
									}
						})
						.setTitle("Choose an editing mode")
						.show();
					}
				});
				
				
			}
			
			
			
		});
		
		JWOW_Button optionsButton = bf.init("File", 100, 32, new IOnClickCallback() {
			
			@Override
			public void onClick(Object clickedObject) {
				
				spt.mg.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						AlertDialog.Builder b = new AlertDialog.Builder(spt.mg);
						String[] items = new String[]{"Save to disk", "Delete level","Rename level","Quit"};
						
						
						if(Globals.instance().isDebug()){
							items = new String[]{"Save to disk", "Delete level","Rename level","Quit", "Save to oort cloud"};
						}

						
						//checkedItems[0] = Level.isCameraFollowsMissle;
						
						
						
						b.setTitle("File options");
						
						b.setItems(items, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, final int which) {
								switch(which){
									case 0:
										final EditText input = new EditText(spt.mg);
										
										input.setText(Level.fileName);
										if(Globals.instance().isDebug()==false){
											input.setEnabled(false);
											
											//Get an awesome unused name from the database!
											AsyncXMLRequestFileName p = new AsyncXMLRequestFileName(){
												
											    protected void onPostExecute(String responseText) {
											    	/*
											    	new AlertDialog.Builder(spt.mg)
													.setMessage(responseText)
													.show();*/
											    	input.setText(responseText);
											    }
											};
											p.execute(new String[]{});
											
										}
										
										new AlertDialog.Builder(spt.mg)
										 .setView(input)
										 .setMessage("Enter a file name")
										 .setTitle("Save")
										 .setPositiveButton("Save", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int choice) {

												Level.fileName = input.getText().toString();
												Level.creatorId = LevelUtils.getCreatorId();
												
												
												String response = LevelUtils.saveToLocalUserDb(spt.mg);
												if(!response.equalsIgnoreCase("SUCCESS")){
													new AlertDialog.Builder(spt.mg)
													.setTitle("Error")
													.setMessage(response)
													.show();
												}
												
												Log.d("SaveToLocalDb response:", response);
												
												if(which==0){
														
														//Do a few checks on the level before publishing
														String error = checkIntegrityOfUserLevel();
														
														if(error!=null){
															new AlertDialog.Builder(spt.mg)
															.setTitle("Integrity error")
															.setMessage(error)
															.setPositiveButton("Ok", new DialogInterface.OnClickListener(){

																@Override
																public void onClick(
																		DialogInterface dialog,
																		int which) {
																	dialog.dismiss();
																}
																
															})
															.show();
															
														}
														else{
														
														
															AsyncXMLUserOortCloudHttpPost p = new AsyncXMLUserOortCloudHttpPost(){
																	
																    protected void onPostExecute(String responseText) {
																    	new AlertDialog.Builder(spt.mg)
																		.setMessage(responseText)
																		.show();
																    }
																};
															p.execute(new String[]{LevelUtils.grabXmlFromLocalDb(spt.mg)});
														}

													}
												}
											}
										 ).show();
									

										
										break;
										
									case 1: 
										dialog.dismiss();
										 new AlertDialog.Builder(spt.mg)
										   .setMessage("Do you want to delete file "+Level.fileName+" permanently?")
										   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												//File deleteThis = spt.mg.getApplication().getFileStreamPath(Level.fileName);
												//deleteThis.delete();
												LevelUtils.deleteLevel(spt.mg);
												Log.w("Level editor","File "+Level.fileName+" was permanently deleted.");
												
											}
										})
										.setNegativeButton("No", new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												dialog.dismiss();
											}
										})
										.show();
										 
										break;
									case 2:
										dialog.dismiss();
										final EditText et = new EditText(spt.mg);
										et.setText(Level.fileName);
										
										new AlertDialog.Builder(spt.mg)
											.setView(et)
											.setTitle("Rename level")
											.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
												
												@Override
												public void onClick(DialogInterface dialog, int which) {
													//File deleteThis = spt.mg.getApplication().getFileStreamPath(Level.fileName);
													//deleteThis.delete();
													LevelUtils.deleteLevel(spt.mg);
													Level.fileName = et.getText().toString();
													String response = LevelUtils.saveToLocalUserDb(spt.mg);
													
												}
											})
											.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
												
												@Override
												public void onClick(DialogInterface dialog, int which) {
													dialog.dismiss();
													
												}
											})
											.show();
											
										
										
										break;
									case 3: //quit
										spt.fadeOutAndReturnToMenu(1.0f);
										break;
									case 4:
										final EditText input1 = new EditText(spt.mg);
										input1.setText(Level.fileName);
										
										new AlertDialog.Builder(spt.mg)
										 .setView(input1)
										 .setMessage("Enter a file name")
										 .setTitle("Save")
										 .setPositiveButton("Save", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int choice) {

												Level.fileName = input1.getText().toString();
												Level.creatorId = LevelUtils.getCreatorId();
												
												
												String response = LevelUtils.saveToLocalUserDb(spt.mg);
												if(!response.equalsIgnoreCase("SUCCESS")){
													new AlertDialog.Builder(spt.mg)
													.setTitle("Error")
													.setMessage(response)
													.show();
												}
												
												Log.d("SaveToLocalDb response:", response);
												
					
														
														//Do a few checks on the level before publishing
														String error = null;
														
														if(Level.walls.size()<4){	
															error = "You must create more walls before you may publish to the Oort cloud";
														}
														
														if(Level.powerups.size()==0){
															error = "You must create more powerups before you may publish to the Oort cloud";
														}
														
														if(Level.fileName==null || Level.fileName.length()==0){
															error = "Please input a unique file name";
														}
														
														if(error!=null){
															new AlertDialog.Builder(spt.mg)
															.setTitle("Integrity error")
															.setMessage(error)
															.setPositiveButton("Ok", new DialogInterface.OnClickListener(){

																@Override
																public void onClick(
																		DialogInterface dialog,
																		int which) {
																	dialog.dismiss();
																	
																}
																
															})
															.show();
															
														}
														
														
	
														AsyncXMLOortCloudHttpPost p = new AsyncXMLOortCloudHttpPost(){
														
															    protected void onPostExecute(String responseText) {
															    	new AlertDialog.Builder(spt.mg)
																	.setMessage(responseText)
																	.show();
															    }
															};
														p.execute(new String[]{LevelUtils.grabXmlFromLocalDb(spt.mg)});
														
													

													
												}
											}
										 ).show();
										break;
									default: break;
								}
								
							}
						})
						.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						})
						.show();
					}
				});
				
				
			}	
		});

		/*
		JWOW_Button saveButton = bf.init("Save", 76, 32, new IOnClickCallback() {
			
			@Override
			public void onClick(Object clickedObject) {
				
				spt.mg.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						
						String[] items = new String[]{"Save to Oort cloud","Save to disk"};
						
						
						new AlertDialog.Builder(spt.mg)
						.setItems(items, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, final int oortOrDiskWhich) {
								
								
								final EditText input = new EditText(spt.mg);
								input.setText(Level.fileName);
								
								
								new AlertDialog.Builder(spt.mg)
								 .setView(input)
								 .setMessage("Enter a file name")
								 .setTitle("Save")
								 .setPositiveButton("Save", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {

										Level.fileName = input.getText().toString();
										Level.creatorId = LevelUtils.getCreatorId();
										switch(oortOrDiskWhich){
											case 0:
												
												//Do a few checks on the level before publishing
												String error = null;
												
												if(Level.walls.size()<4){	
													error = "You must create more walls before you may publish to the Oort cloud";
												}
												
												if(Level.powerups.size()==0){
													error = "You must create more powerups before you may publish to the Oort cloud";
												}
												
												if(Level.fileName==null || Level.fileName.length()==0){
													error = "Please input a unique file name";
												}
												
												if(error!=null){
													new AlertDialog.Builder(spt.mg)
													.setTitle("Integrity error")
													.setMessage(error)
													.setPositiveButton("Ok", new DialogInterface.OnClickListener(){

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															dialog.dismiss();
															
														}
														
													})
													.show();
													return;
												}
												
												LevelUtils.save(spt.mg);
												AsyncXMLOortCloudHttpPost p = new AsyncXMLOortCloudHttpPost(){
											
												    protected void onPostExecute(String responseText) {
												    	new AlertDialog.Builder(spt.mg)
														.setMessage(responseText)
														.show();
												    }
												};
												p.execute(new String[]{LevelUtils.loadXmlFromDisk(Level.fileName, spt.mg)});

												break;
											case 1:
												LevelUtils.save(spt.mg);
												break;
												default:
													break;
										}
										
									}
								 })
								 .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
										return;
										
									}
								})
								.show();
								
								
								 
								
								
								
							}
						}).show();
						
						
						
						
					}
				});
				
			}
		});*/
		
		

		


		

		JWOW_Button plus = bf.init("+", 36, 36, new IOnClickCallback() {
			
			@Override
			public void onClick(Object clickedObject) {
				
				spt.mg.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						spt.mg.mCamera.setZoomFactor(spt.mg.mCamera.getZoomFactor()+ZOOM_INCREMENT);
					}
				});
				
			}
		});
		
		
		
		JWOW_Button minus = bf.init("-", 36, 36, new IOnClickCallback() {
			
			@Override
			public void onClick(Object clickedObject) {
				
				spt.mg.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						float cur = spt.mg.mCamera.getZoomFactor();
						if(cur - ZOOM_INCREMENT>=0){
							spt.mg.mCamera.setZoomFactor(spt.mg.mCamera.getZoomFactor()-ZOOM_INCREMENT);
						}
					}
				});
				
			}
		});
		
		
		

		if(isCreator){
			modeButton.setPosition(spt.mg.mCameraWidth/2, 32);
			optionsButton.setPosition(spt.mg.mCameraWidth/2+100, 32);
			//saveButton.setPosition(spt.mg.mCameraWidth/2+200,32);
			plus.setPosition(spt.mg.mCameraWidth-32, spt.mg.mCameraHeight-64);
			minus.setPosition(spt.mg.mCameraWidth-32, spt.mg.mCameraHeight-128);
			
			spt.mg.mHud.attachChild(modeButton);
			spt.mg.mHud.attachChild(optionsButton);
			//spt.mg.mHud.attachChild(saveButton);
			spt.mg.mHud.registerTouchArea(modeButton);
			spt.mg.mHud.registerTouchArea(optionsButton);
			//spt.mg.mHud.registerTouchArea(saveButton);
			
			spt.mg.mHud.attachChild(plus);
			spt.mg.mHud.attachChild(minus);
			spt.mg.mHud.registerTouchArea(plus);
			spt.mg.mHud.registerTouchArea(minus);
			
		
		}
		
		
		spt.mg.mCamera.setHUD(spt.mg.mHud);
		
		String modeDisplay = "";
		
		int maxCharacters = 3;
		
		/*
		 * deprecating state info because its broken
		 */
		final Text modeText = new Text(0, 0, spt.mg.mFont, modeDisplay,maxCharacters, spt.mg.getVertexBufferObjectManager());
		modeText.registerUpdateHandler(new IUpdateHandler() {
			
			String modeTextLabel;
			
			@Override
			public void reset() {}
			
			@Override
			public void onUpdate(float arg0) {
				
				switch (mode) {
				    case NAVIGATE:
					    modeTextLabel = "nav";
					    break;
					case MOVE:
						modeTextLabel = "mov";
						break;
					case WALL:
						modeTextLabel = "wal";
						break;
					case SATELLITE:
						modeTextLabel = "sat";
						break;
					case CREATE_EARTH:
						modeTextLabel = "ear";
						break;
					case MODIFY:
						modeTextLabel = "mod";
						break;
					case TRIGGER:
						modeTextLabel = "trg";
						break;
					case CREATE_POWERUP:
						modeTextLabel = "pow";
						break;
					case ENEMY:
						modeTextLabel = "emy";
						break;
					case WARP:
						modeTextLabel = "wrp";
						break;
					default:
						modeTextLabel = "nav";
						break;
				}
				
				modeText.setText(modeTextLabel);
			}
		});
		
		modeText.setPosition(spt.mg.mCameraWidth-38, spt.mg.mCameraHeight-16);
		
		if(isCreator)
			spt.mg.mHud.attachChild(modeText);
		
		
		
		spt.gameScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			public boolean onSceneTouchEvent(final Scene s, final TouchEvent t) {
				
				if(t.isActionDown()){
					isDown = true;
					timeDownStart = System.currentTimeMillis();
					initX = (int) t.getX();
					initY = (int) t.getY();
					
				}
				else if(t.isActionUp()){
					isDown = false;
				}

				if (mode == EditorMode.NAVIGATE) {

					if (t.isActionDown()
							&& (initXSwipe == null || initYSwipe == null)) {
						initXSwipe = t.getMotionEvent().getX();
						initYSwipe = t.getMotionEvent().getY();
					}
					else if (t.isActionMove()) {

						/*
						if (Globals.instance().isDebug()) {
							Log.w("Level factory.levelEditor",
									"moving camera to [" + t.getX() + ","
											+ t.getY() + "]");
						}*/

						float newX = t.getMotionEvent().getX();
						float newY = t.getMotionEvent().getY();

						float diffX = newX - initXSwipe;
						float diffY = newY - initYSwipe;

						float newScrollX = spt.mg.mCamera.getCenterX() - diffX;
						float newScrollY = spt.mg.mCamera.getCenterY() + diffY;


						spt.mg.mCamera.setCenterDirect(newScrollX, newScrollY);

						initXSwipe = newX;
						initYSwipe = newY;

					}
					else if(t.isActionUp()){
						initXSwipe = null;
						initYSwipe = null;
					}

				}
				else if((mode==EditorMode.WALL && !isWallModeStrip) || mode==EditorMode.TRIGGER){
					
					//create the wall or trigger
					if(t.isActionUp()){
	                    int x =  (int)t.getX();
	                    int y =  (int)t.getY();
	
	                    final Vector2 p1 = new Vector2(initX,initY);
	                    final Vector2 p2 = new Vector2(x,y);
	 
	                    int dst = (int)p1.dst(p2);
	                    
	                    final int midX = (x + initX)/2;
	                    final int midY = (y + initY)/2;
	                    
	                    
	                    
	                    if(dst>0){
	                    	
	
	                    	final double heading = Math.atan2((y-initY),(x-initX));
	                    	
	                  		if(Globals.instance().isDebug()){
	                  			
	                  			//spt.mg.printAtLoc("["+x+","+y+"]", x+16, y+16, spt.gameScene,scale);
		                    	//spt.mg.printAtLoc("["+midX+","+midY+"] length = "+dst, midX+offset, midY+offset, spt.gameScene,scale);
	                  			
	                  			Log.w("wall","creating wall or trigger with parameters");
	                  			Log.w("wall","initX,initY=["+initX+","+initY+"]");
	                        	Log.w("wall","x,y        =["+x+","+y+"]");
	                        	Log.w("wall","radians:"+heading);
	                  			Log.w("wall","degrees:"+(heading * 180/Math.PI));
	                  			//Log.w("wall", "Number of walls in level="+Level.walls.size()+" + 1");
	                  		}
	                  		
	                  		
	                  		/*
	                  		 * Actually create the wall, this is a huge part !
	                  		 * 
	                  		 * 
	                  		 * 
	                  		 */
	
	                  		spt.mg.runOnUpdateThread(new Runnable() {
								
								@Override
								public void run() {
								
									if(mode == EditorMode.WALL){
										LevelUtils.statsCreateWall();
				                    	final Wall wall = createWall(midX, midY, p1.dst(p2), 24f, heading);
				                    	final ObjectMeta om = (ObjectMeta) wall.getUserData();
				                    	final S_Wall s_wall = (S_Wall) om.getEditorData();
				                    	s_wall.rotation = (float)heading;
				                    	Log.w("Create wall","levels.walls.size="+Level.walls.size());
				                    	Level.walls.add(s_wall);
				                    	Log.w("Create wall","levels.walls.size="+Level.walls.size());
									}
									
									else if(mode == EditorMode.TRIGGER){
										
										
										final Trigger trigger = createTrigger(midX,midY,p1.dst(p2),24f,heading);
										
										final ObjectMeta om = (ObjectMeta) trigger.getUserData();
										final S_Trigger s_trigger = (S_Trigger)om.getEditorData();
										s_trigger.rotation = (float)heading;
										Level.triggers.add(s_trigger);
									}
							
								}
							});
	                    }
	                    
	                    initX = null;
	                    initY = null;
					}/*
					else if(t.isActionMove()){
						int x = (int)t.getX();
						int y = (int)t.getY();
						
						if(tmpLine==null){
							tmpLine = new Line(initX, initY, x, y, 10f, spt.mg.getVertexBufferObjectManager());
							tmpLine.registerUpdateHandler(new IUpdateHandler() {
								
								@Override
								public void reset() {}
								
								@Override
								public void onUpdate(float pSecondsElapsed) {
									tmpLine.
									
								}
							});
						}
						else{
							tmpLine.setVisible(true);
						}
					}
					else {
						if(tmpLine!=null)
							tmpLine.setVisible(false);
					}*/
                    
                }
				else if(mode == EditorMode.WALL && isWallModeStrip && t.isActionUp()){
					spt.mg.runOnUpdateThread(new Runnable() {
						
						@Override
						public void run() {
							final Vector2 point = Vector2Pool.obtain(initX, initY);
							final Rectangle rPoint = new Rectangle(initX, initY, 64f, 64f, spt.mg.getVertexBufferObjectManager()){
								public boolean onAreaTouched(
										TouchEvent t,
										float pTouchAreaLocalX,
										float pTouchAreaLocalY) {
									
									spt.mg.runOnUpdateThread(new Runnable() {
										
										@Override
										public void run() {
											
											setColor(Color.BLUE);
										}
									});
						
									wallstripPoints.add(this);
									
									return true;
								}
									
							};
							//rPoint.registerEntityModifier(new LoopEntityModifier(new RotationModifier(4.0f, 0.0f, 360f)));
							rPoint.setZIndex(128);
							rPoint.setColor(Color.BLUE);
							rPoint.setUserData(point);
							
							wallstripPoints.add(rPoint);
							spt.gameScene.attachChild(rPoint);
							spt.gameScene.registerTouchArea(rPoint);
						}
					});
				}	
				
				
				/*
				else if( mode==EditorMode.ROCKET && 
						spt.bMissle!=null && spt.bMissle.isActive() && isDown){
					

					
					spt.mg.runOnUpdateThread(new Runnable() {
						
						@Override
						public void run() {
							if(spt.particleSystemFire!=null){
								spt.particleSystemFire.setParticlesSpawnEnabled(true);
								if(spt.currentMissleSpeed<=spt.MAX_MISSLE_SPEED){
									//spt.currentMissleSpeed = spt.currentMissleSpeed*1.05f;
									//Log.w("speed",spt.currentMissleSpeed+"");
									spt.bMissle.applyLinearImpulse(spt.curHeading, spt.bMissle.getWorldCenter());
									
								}
								
		
								
							}
							
						}
					});
					
			
				}
				else if(!isDown && mode==EditorMode.ROCKET && 
						spt.bMissle!=null && spt.bMissle.isActive()){

					spt.mg.runOnUpdateThread(new Runnable() {
						
						@Override
						public void run() {
							if(spt.particleSystemFire!=null){
								spt.particleSystemFire.setParticlesSpawnEnabled(false);
								
								//if(spt.currentMissleSpeed>=spt.MIN_MISSLE_SPEED){
									//while(spt.currentMissleSpeed>= spt.MIN_MISSLE_SPEED){
										//spt.currentMissleSpeed = spt.currentMissleSpeed*0.9999f;
										//Log.w("speed",spt.currentMissleSpeed+"");
									//}
								//}
								
								//spt.bMissle.applyLinearImpulse(spt.bMissle.getLinearVelocity().mul(-.4f), spt.bMissle.getWorldCenter());
						
								
							}
							
						}
					});
					
			
				}*/
/*
				else if (t.isActionDown() && (initX == null || initY == null)) {
					initX = (int) t.getX();
					initY = (int) t.getY();

				
				
				}*/ else if(t.isActionUp() && initX!=null && initY!=null) {
        			
	
        			
        			if(mode==EditorMode.ROCKET){
        				
        				
        				//Either create a rocket or fire a projectile (depending on whether missle is active)
        				if(spt.bSpaceship!=null && spt.bSpaceship.isActive()){
        					/*
        					spt.mg.runOnUpdateThread(new Runnable() {
								
								@Override
								public void run() {
									spt.shotsFired+=1;
									spt.createProjectile(spt.missle.getX(), 
		        							spt.missle.getY(), spt.bMissle.getLinearVelocity().mul(3.0f));
		
									spt.soundPewPew.stop();
		        					spt.soundPewPew.play();
		        					
									
								}
							});
        					*/
        				}
        				else {
        					
        					

							spt.createSpaceship(Level.missleOrigin.x, Level.missleOrigin.y, Level.missleOriginAngle);
	
        					
        				}
        				
        				
        				
        				if(Level.isCameraFollowsMissle){
        					spt.attachCameraToMissle(); 
        				}
        			}
        			else if(mode==EditorMode.SATELLITE){
        				spt.createSatellite(initX, initY);
        			}
        			else if(mode==EditorMode.CREATE_EARTH){
        				Log.w("Touch event","creating or moving earth to "+initX+","+initY);
        				if(Level.earthOrigin== null){
        					createEarth(initX,  initY);
        				}
        				else {
        					final Body bEarth = ((ObjectMeta)sEarth.getUserData()).getBody();
        					bEarth.setTransform(initX/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
        							            initY/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,bEarth.getAngle());
        					Level.earthOrigin.x = initX;
        					Level.earthOrigin.y = initY;
        					//TODO
        				}
        			}
        			else if(mode==EditorMode.CREATE_POWERUP){
        				LevelUtils.statsCreatePowerup();
        				final Powerup p = createPowerup(initX,initY);
        				final ObjectMeta om = (ObjectMeta) p.getUserData();
        				final S_Powerup sp = (S_Powerup) om.getEditorData();
        				Level.powerups.add(sp);	
        				spt.gameScene.attachChild(p);
        				
        				
        			}
        			else if(mode==EditorMode.ENEMY){
        				
        				final Entity e = spt.createNaughtyLetter(initX,  initY, "M");
        				spt.mg.sm.getCurrentSceneObject().attachChild(e);
        				
        			}
        			else if(mode==EditorMode.WARP){
        				spt.mg.runOnUpdateThread(new Runnable() {
							
							@Override
							public void run() {
								final Warp h = createWarp(initX, initY);
		        				final ObjectMeta om = (ObjectMeta)h.getUserData();
		        				S_Warp sw = (S_Warp)om.getEditorData();
		        				h.showLabel();
		        				Level.warps.add(sw);
		        				//spt.mg.sm.getCurrentSceneObject().attachChild(h);
		        				//spt.mg.sm.getCurrentSceneObject().registerTouchArea(h);
							}
						});
        				
        			}
        			
        		}
                return true;
        	}
        });
		
		/*
		 * Set up background image. Background images are 4096w x 4096h, and
		 * lets randomize the location of the giant sprite so we get some
		 * variability. The camera is 800w x 480h
		 

		Sprite bg = new Sprite(0, 0, spt.backgroundTextureRepeatingTexture, spt.mg
				.getEngine().getVertexBufferObjectManager());
		bg.setScale(0.5f);
		bg.setAlpha(0.5f);

		float xRan = (float) (Math.random() * (bg.getWidthScaled() / 2));
		float yRan = (float) (Math.random() * (bg.getHeightScaled() / 2));
		
		if (xRan + bg.getWidthScaled() / 2 < spt.mg.mCameraWidth) {
			xRan = spt.mg.mCameraWidth - bg.getWidthScaled()/2;
		}

		bg.setPosition(xRan, yRan);

		if (Globals.instance().isDebug()) {
			if (xRan + bg.getWidthScaled() / 2 < spt.mg.mCameraWidth) {
				Log.w("gameSupport bg",
						"bg is likely off the screen in the x-axis direction");
			}

			if (yRan + bg.getHeightScaled() / 2 < spt.mg.mCameraHeight) {
				Log.w("gameSupport bg",
						"bg is likely off the screen in the y-axis direction");
			}
			Log.w("gameSupport bg", "bg sprite location [" + xRan + "," + yRan
					+ "]");
		}

		spt.gameScene.attachChild(bg);


		final Sprite sEarth = new Sprite(spt.mg.mCameraWidth *2/3,
				spt.mg.mCameraHeight / 6, spt.earthTextureRegion, spt.mg
						.getEngine().getVertexBufferObjectManager());
		sEarth.setScale(0.25f, 0.25f);
		sEarth.registerEntityModifier(new LoopEntityModifier(
				new RotationModifier(spt.ROT_EAR_TIME, 0, 360)));
		sEarth.setRotationCenter(0.5f, 0.5f);

		final FixtureDef earthObjectFixtureDef = PhysicsFactory
				.createFixtureDef(spt.DENSE_EAR, 0.0f, 0.5f);
		earthObjectFixtureDef.isSensor = true;
		final Body bEarth = PhysicsFactory.createBoxBody(spt.mg.mPhysicsWorld,
				sEarth, BodyType.DynamicBody, earthObjectFixtureDef);

		boolean earth_allowPhysicsToInfluencePosition = false;
		boolean earth_updateRotationBaseOnPhysicalCollision = false;

		spt.mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				sEarth, bEarth, earth_allowPhysicsToInfluencePosition,
				earth_updateRotationBaseOnPhysicalCollision));

		ObjectMeta earthMeta = new ObjectMeta("earth", sEarth, bEarth);
		sEarth.setUserData(earthMeta);
		bEarth.setUserData(earthMeta);
		

		spt.gameScene.attachChild(sEarth);
*/
		
		
	}

}
