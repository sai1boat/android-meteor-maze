package com.joao024.mystery3d.mm.game;


import android.os.Bundle;
import android.util.Log;

import com.realmayo.myDatabase;
import com.swarmconnect.Swarm;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.StrokeFont;
import org.andengine.ui.activity.BaseGameActivity;


public class MyGameActivity extends BaseGameActivity{

	final float mCameraWidth = 800;
	final float mCameraHeight = 480;

	public Scene mScene;
	public SmoothCamera mCamera;
	public HUD mHud;
	
	public static GameSupport spt;
	public PhysicsWorld mPhysicsWorld;
	public SceneManager sm;
	public myDatabase myDB;

	JWOW_Menu jm;
	Font mFont;
	//Font mFontBlack;
	//StrokeFont mFontStroke;
	StrokeFont mFontStrokeArial;
	



	
	

    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
	public static boolean connectedToGoogle;
	
	

	@Override
	public EngineOptions onCreateEngineOptions() {

		/* zoom/move relatively fast to the missle location */
		
		float maxVx = 160f;
		float maxVy = 160f;
		float maxZoom = 2f;

		mCamera = new SmoothCamera(0f,0f,mCameraWidth,mCameraHeight, maxVx, maxVy,maxZoom);

		/* Create HUD */
		mHud = new HUD();
		mCamera.setHUD(mHud);
		mHud.setCamera(mCamera);
		mHud.setTouchAreaBindingOnActionDownEnabled(true);

		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				mCamera);
		engineOptions.getAudioOptions().setNeedsSound(true);
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);
		

		return engineOptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {
		spt = new GameSupport(this);
		this.enableAccelerationSensor(spt);// new AccelerationSensorOptions(SensorDelay.NORMAL));
		myDB = new myDatabase(this);
		sm = new SceneManager(this, getEngine(), mCamera);
		sm.loadSplashResources();
		Globals.instance().initPrefs(this);
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	
	
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {

		pOnCreateSceneCallback.onCreateSceneFinished(sm.createSplashScene());
	}

	

	public void printAtLoc(String s, float x, float y, Entity e) {
		printAtLoc(s, x, y, e, 1.0f);
	}

	public void printAtLoc(String s, float x, float y, Entity e, float scale) {

		final TextOptions to = new TextOptions();
		final Text centerText = new Text(x, y, this.mFont, s, to,
				this.getVertexBufferObjectManager());
		centerText.setScale(scale);
		centerText.setZIndex(255); // always on top.
		e.attachChild(centerText);
		e.sortChildren();
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {


		mEngine.registerUpdateHandler(new TimerHandler(3f,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						sm.createMenuScene();
						sm.setCurrentScene(SceneManager.AllScenes.MENU);
					}
				}));

		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	@Override
	public void onBackPressed(){
		
		if(spt.bSpaceship!=null && spt.bSpaceship.isActive()){
			
			GameSupport.isMissleSpawnAllowed = false;
			if(Globals.instance().isDebug()){
				Log.w("Explode","isMissleSpawnAllowed="+GameSupport.isMissleSpawnAllowed);
			}
			
			spt.soundExplosionBitsy.setVolume(0.05f);
			spt.soundExplosionBitsy.play();
			
			spt.soundRocketSound.stop();
			this.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					spt.createDebris(spt.spaceship.getX(), spt.spaceship.getY(), null);
					spt.myDetachEntity(spt.spaceship);
				}
			});
			
			
			
		}
		
		
			spt.fadeOutAndReturnToMenu(1.0f);
	}


    public void initSwarm(){
        if(!Swarm.isInitialized()){
            Swarm.init(this, 9592, "08e50f97fef68c87eecc25bc228e243f");
        }
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Swarm.setActive(this);
    }

    public void onResume() {
        super.onResume();
        Swarm.setActive(this);
    }

    public void onPause() {
        super.onPause();
        Swarm.setInactive(this);
    }




}
