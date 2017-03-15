package com.joao024.mystery3d.mm.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.joao024.andenginebook.ArrayAdapterWithIcon;
import com.joao024.mystery3d.mm.R;
import com.joao024.mystery3d.mm.game.SceneManager.AllScenes;
import com.joao024.mystery3d.mm.leveleditor.LevelUtils;
import com.realmayo.SupportEmailActivity;
import com.swarmconnect.Swarm;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.RotationParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.modifier.ease.EaseCubicInOut;


public class S_MenuScene extends Scene{
	
	MyGameActivity mg;

	
public  S_MenuScene(final MyGameActivity mg) {


		
		/* Download mazes if user has not done so */
		/*
		if(Globals.instance().getPref("firstTime")==null){
			mg.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					LevelUtils.refreshOortCloud(mg, true, null);
					Globals.instance().savePref("firstTime", "0");
				}
			});
		}*/
		
		mg.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				final long start = System.currentTimeMillis();
				Log.d("Parsing mazes...", start+"");
				LevelUtils.refreshOortCloud(mg, true, null);
				Globals.instance().savePref("firstTime", "0");
				final long end = System.currentTimeMillis();
				Log.d("Finished parsing mazes.", end+"");
				Log.d("Parsing mazes took", (end-start)+" milliseconds.");
			}
		});

		
		this.mg = mg;
		this.setBackground(new Background(0.0f, 0.0f, 0.0f));
		
		//Sprite lines = new Sprite(mg.mCamera.getCenterX(), mg.mCamera.getCenterY(), mg.spt.circleParticleTextureRegion, mg.getVertexBufferObjectManager());
		
		Rectangle lines = new Rectangle(mg.mCamera.getCenterX(), mg.mCamera.getCenterY(),mg.mCameraWidth, mg.mCameraHeight, mg.getVertexBufferObjectManager());
		lines.setColor(Color.TRANSPARENT);
		//lines.setScale(0.0f);
		
		for(int i=0;i<mg.mCameraWidth;i+=mg.mCameraWidth/40){
			final Line l = new Line(i,0,i,mg.mCameraHeight, mg.getVertexBufferObjectManager());
			l.setAlpha(0.6f);
			l.setColor(Color.BLUE);
			lines.attachChild(l);
		}
		
		for(int i=0;i<mg.mCameraHeight;i+=mg.mCameraHeight/30){
			final Line l = new Line(0,i,mg.mCameraWidth,i, mg.getVertexBufferObjectManager());
			l.setAlpha(0.6f);
			l.setColor(Color.BLUE);
			lines.attachChild(l);
		}
		
		
		this.attachChild(lines);
		
		
		
		
		
		mg.mCamera.clearUpdateHandlers();
		
		mg.mCamera.setCenter(mg.mCameraWidth/2, mg.mCameraHeight/2);

		final JWOW_ButtonFactory bf = new JWOW_ButtonFactory(mg);
		float playButtonHeight = 64f;
		float playButtonWidth  = 128f;


					
		final JWOW_Button play = bf.init("Play", playButtonWidth,playButtonHeight, new IOnClickCallback() {
			
			@Override
			public void onClick(Object clickedObject) {

				
				mg.runOnUiThread(new Runnable() {
					@Override
					public void run() {
							Globals.instance().setIsBuiltIn(true);
							showLevelSelectionDialog();

						}
					});
					
				
	
				
				
				
			}
		});
		
		
		final JWOW_Button communityMazes = bf.init("Community", playButtonWidth+48,playButtonHeight, new IOnClickCallback() {
			
			
			@Override
			public void onClick(Object clickedObject) {
				
				Globals.instance().setIsBuiltIn(false);
				mg.sm.createCommunityScene();

			}
		});
		
		
		
		final JWOW_Button stats = bf.init("Stats", playButtonWidth,playButtonHeight, new IOnClickCallback() {
			
			@Override
			public void onClick(Object clickedObject) {

				
				
				mg.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//showMazeListView();
	
						
						
						TextView t = new TextView(mg);
						t.setText(LevelUtils.getStatsText());
						
						
						final AlertDialog.Builder b = new AlertDialog.Builder(mg);
						b.setTitle("Stats");
						b.setView(t);

						b.setNeutralButton("Achievements", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mg.runOnUiThread(new Runnable() {
									
									@Override
									public void run() {


											//Toast.makeText(mg, "Not implemented yet.", Toast.LENGTH_LONG).show();
                                        if(!Swarm.isInitialized()){
                                            mg.initSwarm();
                                        }
                                        Swarm.showAchievements();

									}
								});
								
								
							}
						});
						
						b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								
							}
						});

							
							b.show();
	
						}
					});

			}
		});
		
		final JWOW_Button difficulty = bf.init("Spaceship", playButtonWidth+48,playButtonHeight, new IOnClickCallback() {
			@Override
			public void onClick(Object clickedObject) {
				
				mg.sm.createSpaceshipScene();
				mg.sm.setCurrentScene(AllScenes.SPACESHIP_SCENE);

			}
		
		});
						
		
		
		float quitButtonHeight = playButtonHeight;
		float quitButtonWidth  = playButtonWidth;
		JWOW_Button quit = bf.init("Quit", quitButtonWidth, quitButtonHeight, new IOnClickCallback() {	
			@Override
			public void onClick(Object objectClicked) {
				mg.finish();
			}
		});
		
		MyText emailUs = new MyText(0,0, mg.mFont,"Problem or Suggestion?\n Touch here to Email us.", mg.getVertexBufferObjectManager()){
			@Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                            float pTouchAreaLocalX, float pTouchAreaLocalY){
				if(pSceneTouchEvent.isActionUp()){
					final SupportEmailActivity emailActivity = new SupportEmailActivity(mg);
					emailActivity.showHelpPopup();
				}
				
				return true;
			}
		};
		
		emailUs.setScale(0.5f);
		
		play.setAlpha(0.0f);
		difficulty.setAlpha(0.0f);
		quit.setAlpha(0.0f);
		stats.setAlpha(0.0f);
		communityMazes.setAlpha(0.0f);
		emailUs.setAlpha(0.0f);
		
		play.setPosition(mg.mCameraWidth/2, mg.mCameraHeight-128f);
		//createMaze.setPosition(mg.mCameraWidth/2, mg.mCameraHeight-128f-playButtonHeight);
		communityMazes.setPosition(mg.mCameraWidth/2, mg.mCameraHeight-128f-playButtonHeight);
		difficulty.setPosition(mg.mCameraWidth/2,mg.mCameraHeight-128f-playButtonHeight*2);
		stats.setPosition(mg.mCameraWidth/2, mg.mCameraHeight-128f-playButtonHeight*3);
		quit.setPosition(mg.mCameraWidth/2, mg.mCameraHeight-128f-playButtonHeight*4);
		emailUs.setPosition(mg.mCameraWidth/2+128, mg.mCameraHeight-128f-playButtonHeight*2);
		
		//all buttons use width of the widest button
		play.setWidth(communityMazes.getWidthScaled());
		difficulty.setWidth(communityMazes.getWidthScaled());
		stats.setWidth(communityMazes.getWidthScaled());
		quit.setWidth(communityMazes.getWidthScaled());
		
	
		play.registerEntityModifier(new AlphaModifier(0.8f, 0.0f, 1.0f));
		quit.registerEntityModifier(      new AlphaModifier(0.8f, 0.0f, 1.0f));
		difficulty.registerEntityModifier(new AlphaModifier(0.8f, 0.0f, 1.0f));
		stats.registerEntityModifier(     new AlphaModifier(0.8f, 0.0f, 1.0f));
		communityMazes.registerEntityModifier(new AlphaModifier(0.8f,0f, 1.0f));
		emailUs.registerEntityModifier(new AlphaModifier(0.8f,0f, 1.0f));

		this.attachChild(play);
		this.attachChild(quit);
		this.attachChild(difficulty);
		this.attachChild(stats);
		this.attachChild(communityMazes);
		this.attachChild(emailUs);

		this.registerTouchArea(quit);
		this.registerTouchArea(play);
		this.registerTouchArea(difficulty);
		this.registerTouchArea(stats);
		this.registerTouchArea(communityMazes);
		this.registerTouchArea(emailUs);
		
		
		

				
		spawnCoolAsteroid();

				
		Text t = new Text(mg.mCameraWidth/2+200f, mg.mCameraHeight-64f, mg.mFontStrokeArial, mg.getString(R.string.app_name), mg.getVertexBufferObjectManager());
		t.setPosition(t.getX()-t.getWidth()/2, t.getY());
		t.setZIndex(64);
		t.setScale(2.0f);
		this.attachChild(t);
		
		//this.setCullingEnabled(true);
		this.sortChildren();
		


	}


	private void showLevelSelectionDialog(){
		final AlertDialog.Builder b = new AlertDialog.Builder(mg);
		b.setTitle("Select a level");
	
		
		final String[] items = mg.myDB.getLevelList();
		
		

		
        final Integer[] icons = new Integer[items.length];
        for(int i = 0; i<icons.length; i++){
        	
        	int rating = mg.myDB.getScoreRating(items[i]);
        	switch(rating){
        	
	        	case 0:
	        		icons[i] = android.R.drawable.btn_star_big_off;
	        		break;
	        	case 1:
	        		icons[i] = android.R.drawable.btn_star_big_on;
	        		break;
	        	case 2:
	        		icons[i] = android.R.drawable.star_big_on;
	        		break;
        	}
        	Log.d("level selection dialog", "rating for "+items[i]+" is "+rating);
        	
        }
        
        
        ListAdapter adapter = new ArrayAdapterWithIcon(mg, items, icons);
       
		b.setAdapter(adapter, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				LevelUtils.loadLevelIntoMemory(mg.myDB.getLevelXMLByFileName(items[which]), mg);
	
				mg.sm.createGameScene("Play"); //dont think this text does anything
				mg.sm.setCurrentScene(SceneManager.AllScenes.GAME);
				mg.spt.showCalibrationScene();
			}
		});
		
		
		
		/*
		b.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				LevelUtils.loadLevelIntoMemory(mg.myDB.getLevelXMLByFileName(items[which]), mg);
	
				mg.sm.createGameScene("Play"); //dont think this text does anything
				mg.sm.setCurrentScene(SceneManager.AllScenes.GAME);
				mg.spt.showCalibrationScene();
			}
		});*/
		
		if(Globals.instance().isDebug()){
			b.setNeutralButton("Refresh", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					LevelUtils.refreshOortCloud(mg, true, null);
				}
			});
		}
		
		b.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
		/*
		b.setPositiveButton("Create New Maze", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				Level.reset();
				Level.creatorId = LevelUtils.getCreatorId();
				createGameScene(levelName);
				setCurrentScene(SceneManager.AllScenes.GAME);
				
			}
		});*/







		
		b.show();
	}
	
	

	SpriteParticleSystem particleSystemCool;


	public void spawnCoolX(final Scene m){
	
	//spt.mg.runOnUpdateThread(new Runnable() {
		
		//@Override
		//public void run() {
			CircleParticleEmitter pEmitter = new CircleParticleEmitter(mg.mCameraWidth/2, mg.mCameraHeight/2,256f);
			float rateMin = 1f;
			float rateMax = 14f;
			int maxParticles = 20;
			float expireTime = 4;
			particleSystemCool = new SpriteParticleSystem(
					pEmitter, rateMin, rateMax, maxParticles,
					mg.spt.tex_coolXTextureRegion, mg
							.getVertexBufferObjectManager());
			
			particleSystemCool
					.addParticleInitializer(new ExpireParticleInitializer<Sprite>(
							expireTime));
			
			
			
			//alphas in pretty fast
			
			particleSystemCool
					.addParticleModifier(new AlphaParticleModifier<Sprite>(0.0f,
							0.8f, 0.0f, 0.2f));

			//initialzes black
			/*
			particleSystemCool
					.addParticleInitializer(new ColorParticleInitializer<Sprite>(
							1.0f, 1.0f, 1.0f));*/

			//gets 20x bigger in 14 seconds
			particleSystemCool
					.addParticleModifier(new ScaleParticleModifier<Sprite>(0.0f,
							1.2f, 0.0f, 0.6f));
			
			float rR = (float)Math.random();
			float rG = (float)Math.random();
			float rB = (float)Math.random();
			
			float rRMid = (float)Math.random();
			float rGMid = (float)Math.random();
			float rBMid = (float)Math.random();
			
			
			
			//the first second its black, then at second 1 to 2 it changes color.
			particleSystemCool
					.addParticleModifier(new ColorParticleModifier<Sprite>(0.0f,
							2.0f, 
							rR, rG, rB, 
							rRMid, rGMid, rBMid));
			
			/*particleSystemCool
			.addParticleModifier(new ColorParticleModifier<Sprite>(2.0f,
					3.0f, 
					0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f));*/
			
			float rREnd = (float)Math.random();
			float rGEnd = (float)Math.random();
			float rBEnd = (float)Math.random();
			
			particleSystemCool
			.addParticleModifier(new ColorParticleModifier<Sprite>(2.0f,
					3.0f, 
					rRMid, rGMid, rBMid, 
					rREnd, rGEnd, rBEnd));
			

			particleSystemCool
					.addParticleModifier(new RotationParticleModifier<Sprite>(0.0f,
							4.2f, 0.0f, (float) 360));
			
			
			
			//particleSystemSmoke.setAlpha(0.4f);
			particleSystemCool.setZIndex(1);
			
			m.attachChild(particleSystemCool);
			
			
		//}
	//});
	
	
	}

	public void spawnCoolAsteroid(){
		//loop around the screen
		final Path path = new Path(5).to(58, (int)mg.mCameraHeight-58)
				.to((int)mg.mCameraWidth-58, (int)mg.mCameraHeight-58)
				.to((int)mg.mCameraWidth-58, 58)
				.to(58,58)
				.to(58, (int)mg.mCameraHeight-58);
		Sprite s = new Sprite(0f, 0f, mg.spt.tex_asteroid0TextureRegion, mg.getVertexBufferObjectManager());
		s.setScale(0.7f);
		float duration = 10f;
		
		s.registerEntityModifier(new LoopEntityModifier(new PathModifier(duration, path, EaseCubicInOut.getInstance())));
		s.registerEntityModifier(new LoopEntityModifier(new RotationModifier(0.5f,0,360)));
		
		
		
		
		this.attachChild(s);
	
	}
}
