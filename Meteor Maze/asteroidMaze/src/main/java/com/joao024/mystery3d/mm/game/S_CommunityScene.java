package com.joao024.mystery3d.mm.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;

import com.joao024.mystery3d.mm.leveleditor.Level;
import com.joao024.mystery3d.mm.leveleditor.LevelUtils;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import java.util.ArrayList;

public class S_CommunityScene extends Scene{
	
	private MyGameActivity mg;
	
	private ArrayList<MyRectangle> rectangles = new ArrayList<MyRectangle>();
	
	int index = 0;
	int maxPages = 4;
	
	
	
	public void moveLeft(){
		if(index == 0) return;
		mg.mCamera.setCenterDirect(mg.mCamera.getCenterX() - mg.mCameraWidth, mg.mCamera.getCenterY());
		index-=1;
	}
	
	public void moveRight(){
		if(index==maxPages) return;
		mg.mCamera.setCenterDirect(mg.mCamera.getCenterX() + mg.mCameraWidth, mg.mCamera.getCenterY());
		index+=1;
	}
	
	
	
	
	
	public S_CommunityScene(final MyGameActivity mg){

		this.mg = mg;
		

		
		
		mg.mCamera.setCenterDirect(mg.mCameraWidth/2, mg.mCameraHeight/2);
		mg.mHud.setVisible(true);

		
		
		
		this.setBackground(new Background(0,0,0));
		
		for(int i=0;i<mg.mCameraWidth;i+=mg.mCameraWidth/40){
			final Line l = new Line(i,0,i,mg.mCameraHeight, mg.getVertexBufferObjectManager());
			l.setAlpha(0.6f);
			l.setColor(Color.BLUE);
			mg.mHud.attachChild(l);
		}
		
		for(int i=0;i<mg.mCameraHeight;i+=mg.mCameraHeight/30){
			final Line l = new Line(0,i,mg.mCameraWidth,i, mg.getVertexBufferObjectManager());
			l.setAlpha(0.6f);
			l.setColor(Color.BLUE);
			mg.mHud.attachChild(l);
		}
		
		
		
		
		JWOW_ButtonFactory bf = new JWOW_ButtonFactory(mg);
		
		
		
		final JWOW_Button createMaze = bf.init("Maze Builder", 164f,48f, new IOnClickCallback() {
			
			
			@Override
			public void onClick(Object clickedObject) {
				
				if(!Globals.instance().isDebug() && Globals.instance().getPrefs().getBoolean("unlocked_pointsToUnlockMazeBuilder", false)==false){
					mg.runOnUiThread(new Runnable(){
						public void run(){
							new AlertDialog.Builder(mg)
								.setTitle("Action Locked")
								.setMessage("You must attain "+Globals.instance().getPrefs().getInt("pointsToUnlockMazeBuilder", Integer.MAX_VALUE)
										+" points to unlock this feature.")
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
				else {
				
					mg.runOnUiThread(new Runnable(){
						public void run(){
							showUserLevelSelectionDialog();
						}
					});
					
					
				}
				
				
				
			}
		
		});

		if(Globals.instance().getPrefs().getBoolean("unlocked_pointsToUnlockMazeBuilder", false)==false){
			
			Sprite lockIcon = new Sprite(0f, 0f, mg.spt.tex_lockIconTextureRegion, mg.getVertexBufferObjectManager());
			lockIcon.setScale(0.5f);
			LevelUtils.badge(createMaze, lockIcon, LevelUtils.BadgeLocation.TOP_LEFT);
		}
		
		JWOW_Button refresh = new JWOW_ButtonFactory(mg).init("Refresh", 123, 48f, new IOnClickCallback() {
			
			@Override
			public void onClick(Object clickedObject) {
				LevelUtils.refreshOortCloud(mg, false, new Callable(){
					@Override
					public void call() {
						
						mg.runOnUpdateThread(new Runnable() {
							
							@Override
							public void run() {
								populateMazeButtons();
							}
						});
						
						
						
					}});
			}
		});
		
		JWOW_Button cancel = new JWOW_ButtonFactory(mg).init("Back", 92, 48f, new IOnClickCallback() {
			
			@Override
			public void onClick(Object clickedObject) {

					mg.spt.fadeOutAndReturnToMenu(1.0f);
					
				
			}
		});
		
	JWOW_Button left = new JWOW_ButtonFactory(mg).init("<", 32f, 32f, new IOnClickCallback() {
				
				@Override
				public void onClick(Object clickedObject) {
	
						moveLeft();
						
					
				}
			});
	JWOW_Button right = new JWOW_ButtonFactory(mg).init(">", 32f, 32f, new IOnClickCallback() {
		
		@Override
		public void onClick(Object clickedObject) {
	
				moveRight();
				
			
		}
	});
		
		

		
		
		
		createMaze.setAlpha(0.0f);
		refresh.setAlpha(0.0f);
		cancel.setAlpha(0.0f);
		left.setAlpha(0.0f);
		right.setAlpha(0.0f);
		
		createMaze.registerEntityModifier(new AlphaModifier(0.8f, 0.0f, 1.0f));
		refresh.registerEntityModifier(new AlphaModifier(0.8f, 0.0f, 1.0f));
		cancel.registerEntityModifier(new AlphaModifier(0.8f, 0.0f, 1.0f));
		left.registerEntityModifier(new AlphaModifier(0.8f, 0.0f, 1.0f));
		right.registerEntityModifier(new AlphaModifier(0.8f, 0.0f, 1.0f));
		
		createMaze.setPosition(128f, 88f);
		refresh.setPosition(312f, 88f);
		cancel.setPosition(444f, 88f);
		left.setPosition(32f, mg.mCameraHeight/2);
		right.setPosition(mg.mCameraWidth-32f, mg.mCameraHeight/2);
		
		
		mg.mHud.registerTouchArea(createMaze);
		mg.mHud.registerTouchArea(refresh);
		mg.mHud.registerTouchArea(cancel);
		
		mg.mHud.attachChild(createMaze);
		mg.mHud.attachChild(refresh);
		mg.mHud.attachChild(cancel);
		
		mg.mHud.attachChild(left);
		mg.mHud.attachChild(right);
		mg.mHud.registerTouchArea(left);
		mg.mHud.registerTouchArea(right);
		
		
		
		
		populateMazeButtons();


	}
	
	static float boxesSize = 32f;
	static float paddingVertical = 32f;
	static int columnsPerPage = 3;
	static int rowsPerColumn = 5;
	
	private void populateMazeButtons(){
		
		for(MyRectangle rec : rectangles){
			this.detachChild(rec);
		}
		
		rectangles.clear();
		
		String[] mazes = mg.myDB.getUserLevelList(null);
		int column = 0;
		int page   = 0;
		float pageOffset = 0;
		for(int i = 0; i<mazes.length; i++){
			
			final String mazeName = mazes[i];
			
			if(i%rowsPerColumn==0 && i!=0){
				column+=1;
			}
			if(column>columnsPerPage){
				page+=1;
				column=0;
				pageOffset+=mg.mCameraWidth;
			}
			
			
			float x = mg.mCamera.getCenterX()-mg.mCameraWidth/2 + (mg.mCameraWidth/columnsPerPage)*column +boxesSize +38f; //the left < button
			float y = mg.mCamera.getCenterY()+mg.mCameraHeight/2-((i%rowsPerColumn)*boxesSize)-boxesSize-(i%rowsPerColumn*paddingVertical);

			

			final MyRectangle mr = new MyRectangle(x, y, boxesSize, boxesSize, mg.getVertexBufferObjectManager()){
				@Override
	            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
	                            float pTouchAreaLocalX, float pTouchAreaLocalY){
					mg.mHud.clearTouchAreas();
					mg.mHud.detachChildren();
					LevelUtils.loadLevelIntoMemory(mg.myDB.getUserLevelXMLByFileName(mazeName), mg);
					mg.sm.createGameScene("Play"); //dont think this text does anything
					mg.sm.setCurrentScene(SceneManager.AllScenes.GAME);
					mg.spt.showCalibrationScene();
					
					return false;
					
	    		}
			};
			mr.setColor(Color.GRAY);
			mr.setCullingEnabled(true);
			mr.registerEntityModifier(new ParallelEntityModifier(new RotationModifier(0.5f, 0f, 360f), new ScaleModifier(0.5f, 0.0f, 1.0f)));
			
			final MyText t = new MyText(boxesSize+4, boxesSize/2, mg.mFontStrokeArial, mazes[i], mg.getVertexBufferObjectManager());
			t.setScale(0.4f);
			t.setCullingEnabled(true);
			
			
			mr.attachChild(t);
			this.attachChild(mr);
			this.registerTouchArea(mr);
			
			rectangles.add(mr);
		}
	}
	
	
	private void showUserLevelSelectionDialog(){
		final AlertDialog.Builder b = new AlertDialog.Builder(mg);
		b.setTitle("Select a level");
	
		
		final String[] itemsTmp = mg.myDB.getUserLevelList(LevelUtils.getCreatorId());
		
		Log.i("showUserLevelSelectionDialog", itemsTmp.length+"");
		
		final String[] items = new String[itemsTmp.length+1];
		items[0] = "New";
		for(int i=0;i<itemsTmp.length;i++){
			items[i+1] = itemsTmp[i];
		}
		
	
		b.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, final int which) {
				
				if(which==0){
					
					mg.runOnUpdateThread(new Runnable(){
						public void run(){
							mg.mHud.clearTouchAreas();
							mg.mHud.detachChildren();
							final String levelName = "Play";
							Level.reset();   
							Level.creatorId = LevelUtils.getCreatorId();
							mg.sm.createGameScene(levelName);
							mg.sm.setCurrentScene(SceneManager.AllScenes.GAME);
						}
					});
					

				}
				else {
					
					mg.runOnUpdateThread(new Runnable(){
						public void run(){
							mg.mHud.clearTouchAreas();
							mg.mHud.detachChildren();
							LevelUtils.loadLevelIntoMemory(mg.myDB.getUserLevelXMLByFileName(items[which]), mg);
							mg.sm.createGameScene("Play"); //dont think this text does anything
							mg.sm.setCurrentScene(SceneManager.AllScenes.GAME);
							mg.spt.showCalibrationScene();
						}
					});
				}
			}
		});
		b.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
		b.show();
	}

	
}
