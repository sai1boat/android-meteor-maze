package com.joao024.mystery3d.mm.game;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;



public class SceneManager {
	private AllScenes currentScene;
	private MyGameActivity mg;
	private Engine engine;
	private Camera camera;
	private Scene splashScene, gameScene, menuScene,spaceshipScene, missionCompleteScene, communityScene;
	
	
	GameSupport spt;

	public enum AllScenes {
		SPLASH, MENU, GAME, SPACESHIP_SCENE, MISSION_COMPLETE, COMMUNITY
	}

	public SceneManager(MyGameActivity act, Engine eng, Camera cam) {
		// TODO Auto-generated constructor stub
		this.mg = act;
		spt = mg.spt;
		this.engine = eng;
		this.camera = cam;
	}

	public void loadSplashResources() {
		spt.loadSpashGfx();
		spt.loadSfx();
	}




	


	public Scene createSplashScene() {
		
		splashScene = new Scene();
		splashScene.setBackground(new Background(0, 0, 0));

		Sprite icon = new Sprite(0, 0, spt.tex_splashTR,
				engine.getVertexBufferObjectManager());
		
		icon.setScale(0.7f);
		icon.setPosition((camera.getWidth()) / 2, (camera.getHeight() ) / 2);
		//icon.registerEntityModifier(new ScaleModifier(2.2f, 1.0f, 1.4f,  EaseBounceInOut.getInstance()));
		

		splashScene.attachChild(icon);
		
		//Text t = new Text(0,0,mg.mFontStrokeArial, "JBowker Games", mg.getVertexBufferObjectManager());
		//t.setPosition(mg.mCameraWidth/2, 100f);
		//splashScene.attachChild(t);
	

		final AnimatedSprite progressBar = spt.createProgressBar(camera.getWidth()-128f, 128f);
		progressBar.setScale(0.1f);
		splashScene.attachChild(progressBar);
		
		return splashScene;
	}
	
	

	
	
	
	


	
	
	public void createMenuScene(){
		this.menuScene = new S_MenuScene(mg);
		this.setCurrentScene(AllScenes.MENU);
	}
	
	
	
	public void createSpaceshipScene(){
		this.spaceshipScene = new S_SpaceshipScene(mg);
		this.setCurrentScene(AllScenes.SPACESHIP_SCENE);
	}


	public void createMissionCompleteScene() {
		this.missionCompleteScene = new S_MissionCompleteScene(mg);
		this.setCurrentScene(AllScenes.MISSION_COMPLETE);
	}
	
	public void createCommunityScene(){
		this.communityScene = new S_CommunityScene(mg);
		this.setCurrentScene(AllScenes.COMMUNITY);
	}



	public void createGameScene(String levelName) {
		
		gameScene = new Scene();                   
		gameScene.setBackground(new Background(0,0,0));

		spt.setGameScene(gameScene);
		spt.initPhysics();

		
		/* passes in levelName so that if the mode is 
		 * editor it will be handled slightly differently.
		 */
    	spt.populateSceneCommon(levelName);
		
		
		final LevelFactory lf = new LevelFactory(mg.spt);

		lf.genericLoad();
		lf.loadLevel(levelName);

    	
		mg.spt.initSpritePools();
		
    
	}


	

	public AllScenes getCurrentScene() {
		return currentScene;
	}

	public void setCurrentScene(AllScenes currentScene) {
		this.currentScene = currentScene;
		switch (currentScene) {
		case SPLASH:
			break;
		case MENU:
			engine.setScene(menuScene);
			break;
		case GAME:
			engine.setScene(gameScene);
			break;
		case SPACESHIP_SCENE:
			engine.setScene(spaceshipScene);
			break;
		case MISSION_COMPLETE:
			engine.setScene(missionCompleteScene);
			break;
		case COMMUNITY:
			engine.setScene(communityScene);
		default:
			break;
		}
	}
	
	public Scene getCurrentSceneObject() {

		switch (this.currentScene) {
			case SPLASH:
				return this.splashScene;
			case MENU:
				return this.menuScene;
				
			case GAME:
				return this.gameScene;
			case SPACESHIP_SCENE:
				return this.spaceshipScene;
			case MISSION_COMPLETE:
				return this.missionCompleteScene;
			case COMMUNITY:
				return this.communityScene;
			default:
				return null;
		}
	}

}