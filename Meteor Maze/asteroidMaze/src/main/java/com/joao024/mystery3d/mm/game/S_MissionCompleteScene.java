package com.joao024.mystery3d.mm.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.joao024.mystery3d.mm.game.GameSupport.MedalType;
import com.joao024.mystery3d.mm.leveleditor.Level;
import com.joao024.mystery3d.mm.leveleditor.LevelUtils;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;
import org.andengine.util.modifier.ease.EaseCubicInOut;

import java.util.Set;

public class S_MissionCompleteScene extends Scene{

	
	private MyGameActivity mg;
	private boolean isBuiltIn = false;
	
	public S_MissionCompleteScene(final MyGameActivity mg){

		isBuiltIn = Globals.instance().isBuiltIn();
		
		
		
		
		
		this.mg = mg;
		this.setBackground(new Background(0,0,0));
		
		

		

		final Sprite missleSprite = mg.spt.createSpaceshipSprite();
		
		
		
		missleSprite.setRotation(-90f);
		missleSprite.setPosition(mg.mCameraWidth-128f, -200f);
		missleSprite.setScale(0.5f);
		
		
		this.attachChild(missleSprite);
		
		
		
		
		Path p0 = new Path(2).to(mg.mCameraWidth-128f,-256).to(mg.mCameraWidth-128f,mg.mCameraHeight-178f);
		missleSprite.registerEntityModifier(new PathModifier(1.0f, p0, EaseCubicInOut.getInstance()));
		
		
		JWOW_Button okButton = new JWOW_ButtonFactory(mg).init(">>>", 68, 32, new IOnClickCallback() {
			
			@Override
			public void onClick(Object clickedObject) {

					
					mg.spt.loadNextLevel(isBuiltIn);
					
				
			}
		});
		
		
		this.registerTouchArea(okButton);
		okButton.setPosition(mg.mCameraWidth-128f, 48f);
		this.attachChild(okButton);
		
		JWOW_Button menu = new JWOW_ButtonFactory(mg).init("Menu", 68, 32, new IOnClickCallback() {
			
			@Override
			public void onClick(Object clickedObject) {

					mg.spt.fadeOutAndReturnToMenu(1.0f);
					
				
			}
		});
		
		this.registerTouchArea(menu);
		menu.setPosition(128f, 48f);
		this.attachChild(menu);

		long finishTimeInMilliSeconds = (System.currentTimeMillis()-mg.spt.startTime);
		
		int points = ((int)(Math.floor(mg.spt.score - finishTimeInMilliSeconds/10000f)));
		int accuracy = (int)(Math.ceil(mg.spt.shotsLanded/mg.spt.shotsFired*100));
		
		SharedPreferences p = Globals.instance().getPrefs();
		
		
		

		

			LevelUtils.statsWon();
			LevelUtils.statsPoints(points);
			
			
			if(Globals.instance().isDebug()){
				Log.d("S_MissionComplete", "coins="+mg.spt.coins+", coinsMax="+mg.spt.coinsMax+", asteroids="+mg.spt.powerupsHit+", asteroidsMax="+Level.powerups.size());
			}
			
			mg.myDB.insertScore(points, mg.spt.coins, mg.spt.coinsMax, mg.spt.multiplier, accuracy,  mg.spt.powerupsHit,
					(int)finishTimeInMilliSeconds, mg.spt.spaceship.missleNames[mg.spt.spaceship.index]);
			
			int stars = mg.myDB.getScoreRating(Level.fileName);
			
			
			
			
			if(mg.spt.powerupsHit>Level.powerups.size()){
				mg.spt.powerupsHit = Level.powerups.size();
			}
			

			for(int i=0; i<Globals.pointsToUnlockAchievements.length;i++){
				
				if(LevelUtils.statsGetPoints()>= p.getInt(Globals.pointsToUnlockAchievements[i], Integer.MAX_VALUE) 
						&& p.getBoolean("unlocked_"+Globals.pointsToUnlockAchievements[i], false)==false){
					
				
					p.edit().putBoolean("unlocked_"+Globals.pointsToUnlockAchievements[i], true).commit();
				
					switch(i){
						case 0:
							mg.spt.printMedal("Unlocked Maze Builder!", this);
							break;
						case 1:
							mg.spt.printMedal("Unlocked Warp in Maze Builder!", this);
							break;
						case 2:
							mg.spt.printMedal(MedalType.UNLOCKED_SPACESHIP_3, this);
							break;
						case 3:
							mg.spt.printMedal(MedalType.UNLOCKED_SPACESHIP_2, this);
							break;
						case 4:
							mg.spt.printMedal(MedalType.UNLOCKED_SPACESHIP_1, this);
							break;
						case 5:
							mg.spt.printMedal("Unlocked Spaceship 1 weapon type[phazerlo]", this);
							break;
						case 6:
							mg.spt.printMedal("Unlocked Spaceship 1 weapon type[phazerhi]", this);
							break;
						case 7:
							mg.spt.printMedal("Unlocked Spaceship 1 weapon type[missle]", this);
							break;
						case 8:
							mg.spt.printMedal("Unlocked Spaceship 2 weapon type[phazerlo]", this);
							break;
						case 9:
							mg.spt.printMedal("Unlocked Spaceship 2 weapon type[phazerhi]", this);
							break;
						case 10:
							mg.spt.printMedal("Unlocked Spaceship 2 weapon type[missle]", this);
							break;
						case 11:
							mg.spt.printMedal("Unlocked Spaceship 3 weapon type[phazerlo]", this);
							break;
						case 12:
							mg.spt.printMedal("Unlocked Spaceship 3 weapon type[phazerhi]", this);
							break;
						case 13:
							mg.spt.printMedal("Unlocked Spaceship 3 weapon type[missle]", this);
							break;
						default:
							break;
					}
				}
			}
			
			
			if(mg.spt.powerupsHit==Level.powerups.size() && Level.powerups.size()>0){
				mg.spt.printMedal(MedalType.POWERUPS_MASTER, this);
				
			}
			
			
			if(mg.spt.powerupsHit==Level.powerups.size() && Level.powerups.size()>0 && mg.spt.missleHits ==0){
				LevelUtils.statsPerfection();
				mg.spt.printMedal(MedalType.PERFECTION, this);

			}

			
			if(accuracy>200.0f){
				mg.spt.printMedal(MedalType.ACCURACY_OVER_200, this);
			}
			
			
			if(mg.spt.misslesUsed==1){
				LevelUtils.statsConsecutiveNoDeaths();
			}
			else {
				LevelUtils.statsClearConsecutiveNoDeaths();
			}

			
		int winsNoDeaths = LevelUtils.statsGetConsecutiveNoDeaths();
		
		StringBuilder sb = new StringBuilder();
		sb.append("Mission Complete\n\nTime:"+mg.spt.myTimeFormatter.format((System.currentTimeMillis()-mg.spt.startTime)/1000f)+" seconds\n");
		sb.append("# Missles:");
		sb.append(mg.spt.misslesUsed);
		sb.append("\npowerups: "+mg.spt.powerupsHit+"/"+Level.powerups.size());
		sb.append("\npoints: "+points);
		sb.append("\naccuracy: "+accuracy+"%");
		sb.append("\nwins no deaths:"+winsNoDeaths);
		
		final Text t = new Text(mg.mCameraWidth/2, mg.mCameraHeight-200f, mg.mFont, sb.toString(), mg.getVertexBufferObjectManager());
		t.setScale(0.5f);
		this.attachChild(t);
		
		if(GameSupport.multiplier>1){
			Text t2 = new Text(mg.mCameraWidth/2, t.getY()-t.getHeightScaled()/2,mg.mFontStrokeArial, 
					"Multiplier: "+GameSupport.multiplier, mg.getVertexBufferObjectManager());
			t2.setScale(0.8f);
			t2.setPosition(t2.getX(), t2.getY()-t2.getHeightScaled()/2);
			t2.registerEntityModifier(new LoopEntityModifier(new AlphaModifier(1.0f,0.0f, 1.0f)));
			this.attachChild(t2);
		}

		
		Sprite thumbsUp = new Sprite(0f,0f,mg.spt.tex_handThumbsUpTextureRegion, mg.spt.mg.getVertexBufferObjectManager()){
			public boolean onAreaTouched(
					TouchEvent t,
					float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				
				
				
				if(!t.isActionUp())
					return true;
				
				mg.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						
						
						boolean votingSuccess = LevelUtils.vote(true, null, mg.spt.soundUpvote);
						if(!votingSuccess){
							Toast.makeText(mg, "Thanks,  but you have already voted.", Toast.LENGTH_LONG).show();
						}
					}
				});			
				return true;
			}
		};
		thumbsUp.setScale(0.4f);
		
		
		

		
		Sprite thumbsDown = new Sprite(0f,0f,mg.spt.tex_handThumbsUpTextureRegion, mg.spt.mg.getVertexBufferObjectManager()){
			public boolean onAreaTouched(
					TouchEvent t,
					float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				
				if(!t.isActionUp())
					return true;
				
				mg.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						
						String[] items = new String[]{"Too difficult","Poorly designed","Too easy","Performance problem","Impossible to succeed"};
						new AlertDialog.Builder(mg)
						.setItems(items, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String reason = "code.none";
								switch(which){
									case 0:
										reason = "code.tooDifficult";
										break;
									case 1:
										reason = "code.poorlyDesigned";
										break;
									case 2:
										reason = "code.tooEasy";
										break;
									case 3:
										reason = "code.performanceProblem";
										break;
									case 4:
										reason = "code.impossibleToSucceed";
										break;
								}
								
								boolean votingSuccess = LevelUtils.vote(false,reason,mg.spt.soundDownvote);
								if(!votingSuccess){
									Toast.makeText(mg, "Thanks,  but you have already voted.", Toast.LENGTH_LONG).show();
								}
								
							}
						})
						.show();
					}
				});			
				return true;
			}
		};
		thumbsDown.setScale(0.6f);
		thumbsDown.setRotation(180f);
		
		thumbsUp.setColor(Color.GREEN);
		thumbsDown.setColor(Color.RED);

		if(!LevelFactory.isCreator || Globals.instance().isDebug()){
			Set<String> votes = LevelUtils.getVotes();
			boolean alreadyVoted = votes.contains(Level.uuid);
			
			if(!alreadyVoted){
				
				thumbsUp.setPosition(mg.mCameraWidth - mg.mCameraWidth/12,mg.mCameraHeight/2);
				thumbsDown.setPosition(mg.mCameraWidth/12,mg.mCameraHeight/2);
				attachChild(thumbsUp);
				attachChild(thumbsDown);
				registerTouchArea(thumbsUp);
				registerTouchArea(thumbsDown);
			}
			
		}


		mg.mCamera.setCenterDirect(mg.mCameraWidth/2, mg.mCameraHeight/2);
    	

	}
}
