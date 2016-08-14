package com.joao024.mystery3d.mm.game;


import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.joao024.mystery3d.mm.game.LevelFactory.EditorMode;
import com.joao024.mystery3d.mm.game.SceneManager.AllScenes;
import com.joao024.mystery3d.mm.leveleditor.Level;
import com.joao024.mystery3d.mm.leveleditor.LevelUtils;
import com.joao024.mystery3d.mm.leveleditor.S_Trigger;
import com.joao024.mystery3d.mm.leveleditor.S_Warp;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleAtModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.RotationParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.StrokeFont;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.GenericPool;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;
import org.andengine.util.modifier.ease.EaseBackIn;
import org.andengine.util.modifier.ease.EaseBackOut;
import org.andengine.util.modifier.ease.EaseCubicInOut;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;


public class GameSupport implements IAccelerationListener{
	
	MyGameActivity mg;
	JWOW_Menu jm;
	

	Scene gameScene;
	
	/* Game optimization stuff here */
	GenericPool<Projectile> poolProjectile;
	GenericPool<Projectile> poolEnemyProjectile;
	GenericPool<SwirlExplosion> poolSwirlExplosion;
	GenericPool<SpriteParticleSystem> poolExplosionSmokeTrails;
	GenericPool<Rectangle> poolDebris;
	
	
	SpriteBatch sbSmokeTrail;
	
	/* Global game state attributes here */
	public int misslesUsed = 0;
	public int powerupsHit = 0;
	public int score = 0;
	public static int multiplier = 1;
	public int selectedWeapon = 0;
	public int selectedGadget = 0;
	public int coins = 0;
	public int coinsMax = 0;
	
	
	public long startTime;
	DecimalFormat myTimeFormatter = new DecimalFormat("###,##0.00");
	Vector2 noMansLand = new Vector2(Float.MIN_VALUE/2, Float.MIN_VALUE/2);
	
	public static boolean boost = false;
	
	int ROT_SUN_TIME = 180;
    int ROT_EAR_TIME = 3;
    int ROT_SAT_TIME = 180;
    
    /* densities for all our objects here just for tweaking */
    //float DENSE_SUN = 1000f;
    float DENSE_EAR = 100f;
    float DENSE_SAT = 10f;
    float DENSE_AST = 0.1f;
    float DENSE_MISSLE = 100f;
    
    
    float accelX = 0.0f;
    float accelY = 0.0f;
    float accelOffsetX = 0.0f;
    float accelOffsetY = 0.0f;
    
    /* category bits go by power of 2 */
    
    public short CATEGORY_BIT_DEFAULT = 1;
	public short CATEGORY_BIT_WALL = 2;
	public short CATEGORY_BIT_MISSLE = 4;
	public short CATEGORY_BIT_EARTH = 8;
	public short CATEGORY_BIT_CONEOFDOOM = 16;
	public short CATEGORY_BIT_TRIGGER = 32;
	public short CATEGORY_BIT_PROJECTILE = 64;
	public short CATEGORY_BIT_POWERUP    = 128;
	public short CATEGORY_BIT_ENEMY_PROJECTILE = 256;
	public short CATEGORY_BIT_ENEMY = 512;
	public short CATEGORY_BIT_WARP = 1024;
	public short CATEGORY_BIT_DEBRIS = 2048;
	

	public short MASK_BIT_DEFAULT = (short)(CATEGORY_BIT_WALL + CATEGORY_BIT_MISSLE + CATEGORY_BIT_EARTH + CATEGORY_BIT_CONEOFDOOM + CATEGORY_BIT_PROJECTILE);
	public short MASK_BIT_WALL = (short)(CATEGORY_BIT_MISSLE + CATEGORY_BIT_PROJECTILE + CATEGORY_BIT_DEBRIS);
	public short MASK_BIT_CONEOFDOOM = CATEGORY_BIT_MISSLE;
	public short MASK_BIT_EARTH = (short)(CATEGORY_BIT_MISSLE + CATEGORY_BIT_PROJECTILE);
	public short MASK_BIT_TRIGGER = CATEGORY_BIT_MISSLE;
	public short MASK_BIT_PROJECTILE = (short)(CATEGORY_BIT_WALL + CATEGORY_BIT_EARTH + CATEGORY_BIT_POWERUP + CATEGORY_BIT_ENEMY); //might need some tweaking to collide with powerup
	public short MASK_BIT_ENEMY_PROJECTILE = (short)(CATEGORY_BIT_WALL + CATEGORY_BIT_MISSLE); 
	public short MASK_BIT_ENEMY = (short)(CATEGORY_BIT_MISSLE + CATEGORY_BIT_PROJECTILE);
	public short MASK_BIT_WARP = (short)(CATEGORY_BIT_MISSLE);
	public short MASK_BIT_DEBRIS = (short)(CATEGORY_BIT_WALL  + CATEGORY_BIT_POWERUP);
	public short MASK_BIT_POWERUP = (short)(CATEGORY_BIT_MISSLE + CATEGORY_BIT_PROJECTILE + CATEGORY_BIT_DEBRIS);
	public short MASK_BIT_MISSLE = (short)(CATEGORY_BIT_WALL + CATEGORY_BIT_EARTH + CATEGORY_BIT_CONEOFDOOM + CATEGORY_BIT_POWERUP + CATEGORY_BIT_WARP + CATEGORY_BIT_ENEMY_PROJECTILE + CATEGORY_BIT_TRIGGER);

	public short GROUP_ALLWAYS_COLLIDE_DEBRIS = 1;
	
	
	
	
    /* satellites get attached to the sun so we need to pull it out of the 
     * onPopulateScene below.
     */
    Sprite sSun;
	
    
	BitmapTextureAtlas bta_splashTA;
	ITextureRegion tex_splashTR;
	
	BitmapTextureAtlas bta_atlas0;
	BitmapTextureAtlas bta_atlas1;
	
	BuildableBitmapTextureAtlas bta_atlas3;
	BuildableBitmapTextureAtlas bta_atlas4;
	BuildableBitmapTextureAtlas bta_atlas5;
	
	BitmapTextureAtlas bta_coolTextureAtlas;
	BitmapTextureAtlas bta_mStrokeFontTexture;
	BitmapTextureAtlas bta_mStrokeArialFontTexture;
	

	ITextureRegion tex_powerupTextureRegion;
	ITextureRegion tex_earthTextureRegion;
	ITextureRegion tex_smokeTrailTextureRegion;
	ITextureRegion tex_satelliteTextureRegion;
	ITextureRegion tex_asteroid0TextureRegion;
	ITextureRegion tex_explosionTexture1Region;
	ITextureRegion tex_medalTextureRegion;
    
	//BitmapTextureAtlas blueCirclesTexture;
	//ITiledTextureRegion blueCirclesTextureRegion;
    BitmapTextureAtlas bta_asteroid0Texture;
	BitmapTextureAtlas bta_purpleConeOfDoomTexture;
	ITextureRegion tex_purpleConeOfDoomTextureRegion;
    
	BitmapTextureAtlas bta_explosionTexture;
	ITiledTextureRegion tex_explosionTextureRegion;

	BitmapTextureAtlas bta_whiteCircleSpinTexture;
	ITiledTextureRegion tex_whiteCircleSpinTextureRegion;
    
	BitmapTextureAtlas bta_missleTexture;
	public static ITextureRegion tex_missleTextureRegion, tex_missleTextureRegion1, tex_missleTextureRegion2,tex_missleTextureRegion3;
	ITextureRegion tex_lockIconTextureRegion,tex_buttonRefreshTextureRegion;
    
   // BitmapTextureAtlas backgroundTexture;
	ITextureRegion tex_coolXTextureRegion;
	
	BitmapTextureAtlas bta_repeatingTextureAtlas;
	ITextureRegion tex_rockWall1TextureRegion;
	
	//BitmapTextureAtlas wormHoleTextureAtlas;
	//ITextureRegion wormHoleTextureRegion;
	
	BitmapTextureAtlas bta_circleTextureAtlas;
	ITextureRegion tex_circleTextureRegion;
	
	BitmapTextureAtlas bta_circleParticleTextureAtlas;

	
	BitmapTextureAtlas bta_circleStarTextureAtlas;
	ITextureRegion tex_circleStarTextureRegion;
	
	
     
    
    /* UNUSED */
	/*
	ITextureRegion handPointerTextureRegion, handRockOnTextureRegion, gasGauge0, gasGauge2,gasGauge3, 
	    gasSpout,smokeTrailFireTexture;
	ITiledTextureRegion gasGauge1;
	*/
	
	
	ITextureRegion smokeTrailFireTexture;
	
	ITextureRegion tex_wallJoint, tex_missleLaunchPoint,tex_shockWaveTexture,tex_projectileExplosion,
		tex_circleParticleTextureRegion,tex_missleProjectileTextureRegion, tex_checkmarkTextureRegion, tex_handThumbsUpTextureRegion, tex_coinTextureRegion;
	
	ITiledTextureRegion tex_buttonUpDownTextureRegion;
    
    
    
	ITextureRegion tex_backgroundTextureRepeatingTexture;
	RepeatingSpriteBackground background;
	AssetBitmapTexture backgroundTextureRepeating;

    
	static Sound soundDoot;
	static Sound soundMissleLaunch;
	static Sound soundRocketSound;
	static Sound soundExplosionBitsy;
	static Sound soundExplosion0;
	static Sound soundUpvote;
	static Sound soundDownvote;
	static Sound soundOw;
	static Sound soundMedal;
	static Sound soundPewPew;
	static Sound soundSpinDown;
	static Sound soundSpinUp;
	static Sound soundCoin;
	//static Sound soundSongAliensHaveLanded;
    

	
	
	/* spawn locations for asteroids */
	final static int TOP_RIGHT = 0;
    final static int BOTTOM_LEFT = 1;
    final static int BOTTOM_RIGHT = 2;
    final static int TOP_LEFT = 3;
    
    
    /* NOT USED shader program */
    public static final String FRAGMENTSHADER =
    	      "precision lowp float;\n" +
    	      "uniform sampler2D " + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ";\n" +
    	      "varying mediump vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
    	      "void main() {\n" +
    	      " vec4 myColor = texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ");\n" +
    	      " gl_FragColor.r = dot(myColor.rgb, vec3(.3, .59, .11));\n" +
    	      " gl_FragColor.g = dot(myColor.rgb, vec3(.3, .59, .11));\n" +
    	      " gl_FragColor.b = dot(myColor.rgb, vec3(.3, .59, .11));\n" +   
    	      " gl_FragColor.a = myColor.a;\n" +
    	      "}";
	
	
	public GameSupport(MyGameActivity mg){
		this.mg = mg;
		//max_missle_speeds = new Float[]{3.0f, 4.0f,5.0f};
		 //max_missle_hits = new Float[]{6f,3f,1f};
	}
	
	public void setGameScene(Scene gameScene){
		this.gameScene = gameScene;
		
	}
	
	
	

	
	public void initSpritePools(){
		
		//Init our sprite batches
		sbSmokeTrail = new SpriteBatch(this.tex_circleTextureRegion.getTexture(), 200, mg.getVertexBufferObjectManager());
		gameScene.attachChild(sbSmokeTrail);
		
		//init our pools

		this.poolProjectile = new GenericPool<Projectile>() {
			
			@Override
			protected Projectile onAllocatePoolItem() {
				Projectile.Behavior behavior = null;
				switch(selectedWeapon){
					case 0:
						behavior = Projectile.Behavior.WEP0;
						break;
					case 1:
						behavior = Projectile.Behavior.WEP1;
						break;
					case 2:
						behavior = Projectile.Behavior.WEP2;
						break;
					case 3:
						behavior = Projectile.Behavior.WEP3;
						break;
					default:
						behavior = Projectile.Behavior.WEP0;
						break;
				}
				final Projectile p = new Projectile(0.0f, 0.0f,Projectile.Type.FRIENDLY_PROJECTILE, behavior,mg);
				mg.sm.getCurrentSceneObject().attachChild(p);
				return p;
			}
			
			@Override
			protected void onHandleRecycleItem(Projectile pItem) {
				final ObjectMeta om = (ObjectMeta)pItem.getUserData();
				om.getBody().setActive(false);
				pItem.setIgnoreUpdate(true);
				pItem.setVisible(false);
				
				switch(selectedWeapon){
				case 0:
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				default:
					break;
				}
			}
			
			@Override
			protected void onHandleObtainItem(final Projectile pItem) {
				final ObjectMeta om = (ObjectMeta)pItem.getUserData();
				om.getBody().setActive(true);
				pItem.setIgnoreUpdate(false);
				pItem.setVisible(true);
				Projectile.Behavior behavior = null;
				switch(selectedWeapon){
					case 0:
						behavior = Projectile.Behavior.WEP0;
						break;
					case 1:
						behavior = Projectile.Behavior.WEP1;
						break;
					case 2:
						behavior = Projectile.Behavior.WEP2;
						om.getBody().setAngularVelocity(0.0f);//missle projectile does NOT spin.
						
						break;
					case 3:
						behavior = Projectile.Behavior.WEP3;
						break;
					default:
						behavior = Projectile.Behavior.WEP0;
						break;
				}
				
			}
		};
		
		this.poolEnemyProjectile = new GenericPool<Projectile>() {
			
			@Override
			protected Projectile onAllocatePoolItem() {
				final Projectile p = new Projectile(0.0f, 0.0f, Projectile.Type.ENEMY_PROJECTILE, Projectile.Behavior.WEP0,mg);
				mg.sm.getCurrentSceneObject().attachChild(p);
				return p;
			}
			
			@Override
			protected void onHandleRecycleItem(Projectile pItem) {
				final ObjectMeta om = (ObjectMeta)pItem.getUserData();
				om.getBody().setActive(false);
				pItem.setIgnoreUpdate(true);
				pItem.setVisible(false);
			}
			
			@Override
			protected void onHandleObtainItem(Projectile pItem) {
				final ObjectMeta om = (ObjectMeta)pItem.getUserData();
				om.getBody().setActive(true);
				pItem.setIgnoreUpdate(false);
				pItem.setVisible(true);
				
			}
		};
		
		this.poolSwirlExplosion = new GenericPool<SwirlExplosion>() {
			
			@Override
			protected SwirlExplosion onAllocatePoolItem() {
				final SwirlExplosion p = new SwirlExplosion(0f,0f, tex_explosionTextureRegion, mg.getVertexBufferObjectManager());
				mg.sm.getCurrentSceneObject().attachChild(p);
				return p;
			}
			
			@Override
			protected void onHandleRecycleItem(SwirlExplosion pItem) {
				/*
				final ObjectMeta om = (ObjectMeta)pItem.getUserData();
				om.getBody().setActive(false);*/
				pItem.setIgnoreUpdate(true);
				pItem.setVisible(false);
			}
			
			@Override
			protected void onHandleObtainItem(SwirlExplosion pItem) {
				/*
				final ObjectMeta om = (ObjectMeta)pItem.getUserData();
				om.getBody().setActive(true);*/
				pItem.setIgnoreUpdate(false);
				pItem.setVisible(true);
				
			}
		};
		
		this.poolExplosionSmokeTrails = new GenericPool<SpriteParticleSystem>() {
			
			@Override
			protected SpriteParticleSystem onAllocatePoolItem() {
				final PointParticleEmitter particleEmitter = new PointParticleEmitter(0,0);
		 		
				float expireTime = 1.0f;
		 		float rateMin = 9f;
		 		float rateMax = 19f;
		 		int maxParticles = 20;
		 		
		 		/*
		 		final SpriteParticleSystem particleSystem = new SpriteParticleSystem(
		 		particleEmitter, rateMin, rateMax, maxParticles,circleParticleTextureRegion, mg.getVertexBufferObjectManager());*/
		 		
		 		SpriteParticleSystem particleSystem = new SpriteParticleSystem(particleEmitter, rateMin, rateMax, 
		 				maxParticles,tex_circleParticleTextureRegion, mg.getVertexBufferObjectManager());
		 		
		 		
		 		
		 		
		 		//float expireTime = 2.0f;
		 		
		 		particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(expireTime));
		 		particleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(1.0f,1.0f,0.0f));
		 		
		 		particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(0.0f, expireTime, 0.5f, 0.0f));
		 		particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(0.0f,0.2f,
		 				1.0f,1.0f,
	                    0.0f,0.8f,
	                    0.8f,0.8f)); //purple!!!
		 				
		 				
		 				/* yellow--yuck
		 				1.0f,0.8f,
		 				1.0f,0.8f,
		 				0.0f,0.8f)); */
		 		

		 		
		 		ObjectMeta st_om = new ObjectMeta("smokeTrail",particleSystem,null);
		 		particleSystem.setUserData(st_om);
		 		
		 		particleSystem.setCullingEnabled(true);
				
				
		 		sbSmokeTrail.attachChild(particleSystem);
				
				return particleSystem;
			}
			
			@Override
			protected void onHandleRecycleItem(SpriteParticleSystem pItem) {

				//pItem.setIgnoreUpdate(true);
				pItem.setVisible(false);
				pItem.setParticlesSpawnEnabled(false);
				pItem.setPosition(-100000000f, -100000000f);
				pItem.reset();
				pItem.getParticleEmitter().reset();
				
			}
			
			@Override
			protected void onHandleObtainItem(SpriteParticleSystem pItem) {

				pItem.setParticlesSpawnEnabled(true);
				
				pItem.setVisible(true);
				
				
			}
		};
		
		this.poolDebris = new GenericPool<Rectangle>() {
			
			@Override
			protected Rectangle onAllocatePoolItem() {
		      	
		      	final Rectangle debris = new Rectangle(0f,0f,16f,16f,mg.getVertexBufferObjectManager());
		      	
		      	

		      	
		      	
		      	
		      	debris.setColor(Color.GRAY);

		          
		      	float density    = DENSE_AST;
		      	float elasticity = 0.8f;
		      	float friction   = 0.2f;
		      	
		          final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(density, elasticity, friction,  false, CATEGORY_BIT_DEBRIS, MASK_BIT_DEBRIS, (short)0);
		          
		          final Body bAsteroid = PhysicsFactory.createBoxBody(mg.mPhysicsWorld, 
		          										debris, 
		          		                                 BodyType.DynamicBody, 
		          		                                 objectFixtureDef); 
		          bAsteroid.setBullet(true);
		          boolean updateRotationBaseOnPhysicalCollision = true;
		          boolean allowPhysicsToInfluencePosition = true;
		          mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(debris, 
		          		                                                         bAsteroid, 
		          		                                                         allowPhysicsToInfluencePosition, 
		          		                                                         updateRotationBaseOnPhysicalCollision));
		          
		          
		          
		          ObjectMeta asteroidMetaData = new ObjectMeta("debris",debris,bAsteroid);
		          //asteroidMetaData.getAffiliatedObj().add(particleSystem);
		          asteroidMetaData.setBody(bAsteroid);
		          debris.setUserData(asteroidMetaData); 
		          bAsteroid.setUserData(asteroidMetaData);
		          
		          /* Make the smoke trail follow the asteroid but 
		         	 * stays within the mScene, it is not a child of asteroid 
		         	 */
		          /*
		         	debris.registerUpdateHandler(new IUpdateHandler() {
		    			
		    			@Override
		    			public void reset() {
		    				// TODO Auto-generated method stub
		    				
		    			}
		    			
		    			@Override
		    			public void onUpdate(float arg0) {

		    				particleEmitter.setCenter(debris.getX(), debris.getY());

		    			}
		    		});*/
		          

		         	
		        debris.setCullingEnabled(true);
		        
				
				//mg.sm.getCurrentSceneObject().attachChild(debris);
		        gameScene.attachChild(debris);
				
				return debris;
			}
			
			@Override
			protected void onHandleRecycleItem(Rectangle pItem) {

				final ObjectMeta dMeta = (ObjectMeta)pItem.getUserData();
				dMeta.getBody().setLinearVelocity(0.0f, 0.0f);
				dMeta.getBody().setActive(false);
				pItem.setIgnoreUpdate(true);
				pItem.setVisible(false);
				pItem.clearUpdateHandlers();
				
			}
			
			@Override
			protected void onHandleObtainItem(Rectangle pItem) {

				final ObjectMeta dMeta = (ObjectMeta)pItem.getUserData();
				dMeta.getBody().setActive(true);
				pItem.setIgnoreUpdate(false);
				pItem.setVisible(true);
				pItem.setAlpha(1.0f);
				
			}
		};
		
		for(int i = 0; i<30; i++){
			final Rectangle d = poolDebris.obtainPoolItem();
			poolDebris.recyclePoolItem(d);
		}
		
		
		
	}
	
	
	public void loadSpashGfx(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		bta_splashTA = new BitmapTextureAtlas(this.mg.getTextureManager(),
				1024, 512);

		//tex_splashTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_splashTA, this.mg, "splashscreen_just_cat.png", 0, 0);
        tex_splashTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_splashTA, this.mg, "jbowkerGamesLogo.png", 0, 0);
		
		bta_whiteCircleSpinTexture = new BitmapTextureAtlas(mg.getTextureManager(), 1024, 256);
		tex_whiteCircleSpinTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(bta_whiteCircleSpinTexture, 
    			mg, "white_chicklets_spin.png", 0,0, 4, 1); //x,y,columns,rows
		
		bta_splashTA.load();
		bta_whiteCircleSpinTexture.load();
		

    	mg.mFont = FontFactory.create(mg.getFontManager(), mg.getTextureManager(), 
    			512, 512, Typeface.create(Typeface.MONOSPACE, Typeface.BOLD), 24f,Color.WHITE);
    	mg.mFont.load();
    	/*
    	mg.mFontBlack = FontFactory.create(mg.getFontManager(), mg.getTextureManager(), 
    			256, Typeface.create(Typeface.MONOSPACE, Typeface.BOLD), 24f,Color.BLACK);256, 
    	mg.mFontBlack.load();
    	*/
    	
    	
    	bta_mStrokeFontTexture = new BitmapTextureAtlas(mg.getTextureManager(),256, 256, TextureOptions.BILINEAR);
    	bta_mStrokeArialFontTexture = new BitmapTextureAtlas(mg.getTextureManager(),256, 256, TextureOptions.BILINEAR);
    	
    	//mg.mFontStroke = new StrokeFont(mg.getFontManager(),mStrokeFontTexture, 
    		//	Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 32, true, Color.CYAN, 2, Color.WHITE, true);
    	
    	//String strokeFontTtf = "font/Abduction.ttf";
    	String strokeArialFontTtf = "font/arial_rounded_bold.ttf";
  /*
    	mg.mFontStroke = new StrokeFont(mg.getFontManager(),bta_mStrokeFontTexture, 
    			Typeface.createFromAsset(mg.getAssets(), strokeFontTtf), 32, true, Color.BLACK, 2, Color.WHITE, true);
    	mg.mFontStroke.load();
    */	
    	mg.mFontStrokeArial = new StrokeFont(mg.getFontManager(),bta_mStrokeArialFontTexture, 
    			Typeface.createFromAsset(mg.getAssets(), strokeArialFontTtf), 36, true, Color.BLACK, 2, Color.WHITE, true);
    	mg.mFontStrokeArial.load();
    	
    	
    	/*
    	mg.mFontStroke = FontFactory.createStrokeFromAsset(mg.getFontManager(),mStrokeFontTexture , mg.getAssets(), 
    			"font/arial_rounded_bold.ttf", (float)35, true, Color.rgb(255, 162, 53), 2, Color.rgb(170, 113, 14));
    	mg.getEngine().getTextureManager().loadTexture(mStrokeFontTexture);
    	mg.mFontStroke.load();
*/
    	
    	
    	

    	




    	//coolTextureAtlas = new BitmapTextureAtlas(mg.getTextureManager(),256, 256);
    	//coolXTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(coolTextureAtlas, mg, "cool_x.png",0,0);
    	
    	//coolTextureAtlas.load();
		

    	
    	//Atlas 0
    	bta_atlas0 = new BitmapTextureAtlas(mg.getTextureManager(), 1024,1024);
    	//powerupTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas0,mg,"powerupZ.png",0,0);
    	tex_medalTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas0, mg, "medal.png",512,0);
    	tex_satelliteTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas0, mg, "satellite.png",0,512);
    	//explosionTexture1Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas0, mg, "explosion_1.png",512,512);
    	//tex_explosionTexture1Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas0, mg, "dot.png",32,32);
    	
    	//Atlas 1
    	bta_atlas1 = new BitmapTextureAtlas(mg.getTextureManager(),512,256);
    	tex_earthTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas1,mg,"earth_small.png",0,0);
    	//smokeTrailTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas1,mg,"smoke_trail.png",256,0);
    	
    	
    	

    	//blueCirclesTexture = new BitmapTextureAtlas(mg.getTextureManager(), 512, 256);
    	//blueCirclesTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(blueCirclesTexture, 
    			//mg, "blue_circles_pulse.png", 0, 0, 2, 1); //x,y,columns,rows
    	bta_asteroid0Texture = new BitmapTextureAtlas(mg.getTextureManager(),512,512);
    	tex_asteroid0TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_asteroid0Texture, mg, "asteroid_0.png",0,0);
    	
    	bta_purpleConeOfDoomTexture = new BitmapTextureAtlas(mg.getTextureManager(),128,128);
    	tex_purpleConeOfDoomTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_purpleConeOfDoomTexture, mg, "purple_cone_of_doom_small.png",0,0);
    	
    	
    	bta_explosionTexture = new BitmapTextureAtlas(mg.getTextureManager(), 1024, 512);
    	tex_explosionTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(bta_explosionTexture, 
    			mg, "explosion_big.png", 0, 0, 4, 2); //x,y,columns,rows
    	
    	
    	//backgroundTexture = new BitmapTextureAtlas(mg.getTextureManager(), 2048, 2048);
    	//backgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundTexture, mg, "background4.jpg",0,0);
    	
    	bta_repeatingTextureAtlas = new BitmapTextureAtlas(mg.getTextureManager(), 512, 64, TextureOptions.REPEATING_BILINEAR);
    	tex_rockWall1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_repeatingTextureAtlas, mg, "rockWall2.jpg",0,0);
    	//ResourceManager.getInstance(). mSquareTextureRegion.setTextureSize( 800, 480);
    	tex_rockWall1TextureRegion.setTextureSize(512, 32);


    	
    	//wormHoleTextureAtlas = new BitmapTextureAtlas(mg.getTextureManager(), 512, 512);
    	//wormHoleTextureRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(wormHoleTextureAtlas, mg, "worm_hole.png", 0,0);
    	
    	bta_circleTextureAtlas = new BitmapTextureAtlas(mg.getTextureManager(), 512, 512);
    	tex_circleTextureRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_circleTextureAtlas, mg, "circle.png", 0,0);
    	
    	//bta_circleParticleTextureAtlas
    	bta_circleParticleTextureAtlas = new BitmapTextureAtlas(mg.getTextureManager(), 32, 32);
    	tex_explosionTexture1Region= BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_circleParticleTextureAtlas, mg, "dot.png", 0,0);
    	
    	
    	bta_circleStarTextureAtlas = new BitmapTextureAtlas(mg.getTextureManager(), 512, 512);
    	tex_circleStarTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_circleStarTextureAtlas, mg, "dot_star.png", 0,0);
    	
    	
	    	try{
	    		
	    		int backgroundIndex = (int)(Math.floor(Math.random()*4));
	    		
	    		if(backgroundIndex>3 || backgroundIndex<1){
	    			backgroundIndex = 3;
	    		}
	    		
	    		backgroundTextureRepeating = new AssetBitmapTexture(mg.getEngine().getTextureManager(), 
	    				mg.getAssets(), "gfx/background"+backgroundIndex+".jpg",BitmapTextureFormat.RGB_565, TextureOptions.REPEATING_BILINEAR);
	    	}
	    	catch (IOException e){
	    		Log.e("game support","error while loading repeating background: "+e.toString());
	    	}
	    	
	    	//backgroundTextureRepeating.load();
	    	
	    	tex_backgroundTextureRepeatingTexture = 
	    			TextureRegionFactory.extractFromTexture(backgroundTextureRepeating);
	
	    	background = new RepeatingSpriteBackground( mg.mCameraWidth, mg.mCameraHeight, tex_backgroundTextureRepeatingTexture,0.8f, mg.getEngine().getVertexBufferObjectManager());

    	
    	
    	
    	//atlas3
    	

    	bta_atlas3 = new BuildableBitmapTextureAtlas( mg.getTextureManager(), 1024, 1024);
    	tex_lockIconTextureRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas3, mg, "lock.png");
    	tex_buttonRefreshTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas3, mg, "button_refresh.png");
    	tex_missleProjectileTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas3, mg, "projectile_missle.png");
    	tex_checkmarkTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas3, mg, "checkmark.png");
      	tex_wallJoint= BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas3,  mg,  "wall_joint.png");
    	tex_missleLaunchPoint = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas3,  mg,  "missle_launch_point.png");
    	tex_shockWaveTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas3,  mg,  "shock_wave.png");
    	tex_projectileExplosion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas3,  mg,  "projectile_explosion.png");//128x128
    	tex_circleParticleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas3,  mg,  "dot.png");//128x128
    	//smokeTrailFireTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas3,  mg,  "smoketrail_fire.png"); //256x256
    	
    	
    	bta_atlas4 = new BuildableBitmapTextureAtlas( mg.getTextureManager(), 1024, 1024);
    	tex_buttonUpDownTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(bta_atlas4, 
    			mg, "button_up_down.png", 2, 1); //x,y,columns,rows
    	
    	
    	//tex_coolXTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas4, mg, "cool_x.png");
        tex_coolXTextureRegion = tex_asteroid0TextureRegion;
    	tex_handThumbsUpTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas4, mg, "hands_thumbs_up.png");//256x256
    	tex_coinTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas4, mg, "coin.png");//16x16
    	
    	bta_atlas5 = new BuildableBitmapTextureAtlas(mg.getTextureManager(), 1024, 1024);
    	tex_missleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas5, mg, "missle_new.png");//512x512
    	tex_missleTextureRegion1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas5, mg, "missle_new1.png");//512x512
    	tex_missleTextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas5, mg, "missle_new2.png");//512x512
    	tex_missleTextureRegion3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta_atlas5, mg, "missle_new3.png");//512x512
    	
    	/* UNUSED
    	handPointerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas3, mg, "hand_pointer.png");//128 x 128
    	handRockOnTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas3, mg, "hand_rock_on.png");//128 x 128
    	gasGauge0 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas3, mg, "gas_gauge_0.png");
    	
    	gasGauge1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(atlas3, 
    			mg, "gas_gauge_1.png", 2, 1); //x,y,columns,rows
    	gasGauge2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas3, mg, "gas_gauge_2.png");
    	gasGauge3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas3, mg, "gas_gauge_3.png");
    	gasSpout = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas3, mg, "gas_spout.png");
    	catTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas3,  mg,  "catIcon.png");
    	*/
    	
  
    	
    	
    	
    	try{
    		bta_atlas3.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
    		bta_atlas4.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
    		bta_atlas5.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
    	}
    	catch(TextureAtlasBuilderException e){
    		Log.e("Buildable Bitmap Texture Atlas", e.toString());
    	}

    	bta_atlas0.load();
    	bta_atlas1.load();
    	bta_atlas3.load();
    	bta_atlas4.load();
    	bta_atlas5.load();
    	//blueCirclesTexture.load();
    	bta_asteroid0Texture.load();
    	bta_purpleConeOfDoomTexture.load();
    	bta_explosionTexture.load();
    	backgroundTextureRepeating.load();
    	bta_repeatingTextureAtlas.load();
    	//wormHoleTextureAtlas.load();
    	bta_circleTextureAtlas.load();
    	bta_circleStarTextureAtlas.load();

    	//rockWall1TextureRegion.setTextureSize(800, 480);
    	
    	
    	/* init font */
    	if(mg.mFont==null){
	    	mg.mFont = FontFactory.create(mg.getFontManager(), mg.getTextureManager(), 
	    			256, 256, Typeface.create(Typeface.MONOSPACE, Typeface.BOLD), 24,Color.WHITE);
	    	mg.mFont.load();
    	}
		
		
	}
	


	
	public void loadSfx(){
    	SoundFactory.setAssetBasePath("sfx/");
    	
	    	 try {
	    		 if(soundDoot==null)
	    			 soundDoot = SoundFactory.createSoundFromAsset(mg.getEngine().getSoundManager(), mg, "bong_xfade.ogg");
	    		 if(soundMissleLaunch==null)
	    			 soundMissleLaunch = SoundFactory.createSoundFromAsset(mg.getEngine().getSoundManager(), mg, "missle_launch.ogg");
	    		 if(soundRocketSound==null)
	    			 soundRocketSound = SoundFactory.createSoundFromAsset(mg.getEngine().getSoundManager(), mg, "rocket_sound.ogg");
	    		 if(soundExplosionBitsy==null)
	    			 soundExplosionBitsy = SoundFactory.createSoundFromAsset(mg.getEngine().getSoundManager(), mg, "exp_bitsy_long.ogg");
	    		 if(soundExplosion0==null)
	    			 soundExplosion0 = SoundFactory.createSoundFromAsset(mg.getEngine().getSoundManager(), mg, "explosion0.ogg");
	    		 if(soundUpvote==null)
	    			 soundUpvote = SoundFactory.createSoundFromAsset(mg.getEngine().getSoundManager(), mg, "upvote.ogg");
	    		 if(soundDownvote==null)
	    			 soundDownvote = SoundFactory.createSoundFromAsset(mg.getEngine().getSoundManager(), mg, "downvote.ogg");
	    		 if(soundOw==null)
	    			 soundOw = SoundFactory.createSoundFromAsset(mg.getEngine().getSoundManager(), mg, "ow.ogg");
	    		 if(soundMedal==null)
	    			 soundMedal = SoundFactory.createSoundFromAsset(mg.getEngine().getSoundManager(), mg, "medalSound.ogg");
	    		 if(soundPewPew==null)
	    			 soundPewPew = SoundFactory.createSoundFromAsset(mg.getEngine().getSoundManager(), mg, "pewpew.ogg");
	    		 if(soundSpinDown==null)
	    			 soundSpinDown = SoundFactory.createSoundFromAsset(mg.getEngine().getSoundManager(), mg, "spinDown.ogg");
	    		 if(soundSpinUp==null)
	    			 soundSpinUp = SoundFactory.createSoundFromAsset(mg.getEngine().getSoundManager(), mg, "spinUp.ogg");
	    		 if(soundCoin==null)
	    			 soundCoin = SoundFactory.createSoundFromAsset(mg.getEngine().getSoundManager(), mg, "coin.ogg");
	    		 //if(soundSongAliensHaveLanded==null)
	    			 //soundSongAliensHaveLanded = SoundFactory.createSoundFromAsset(mg.getEngine().getSoundManager(), mg, "song_aliens_have_landed.ogg");
	    	 } catch (final IOException e) {
	             Log.e("Error loading sound",e.toString());
	    	 }
    	

    }
	
	
	public void printText(String s, float x, float y, Scene scene){
		final Text text = new Text(x, y, mg.mFont, s, 128, mg.getVertexBufferObjectManager());
		text.setScale(text.getScaleX()/mg.mCamera.getZoomFactor());
        
        scene.attachChild(text);
	}
	
	
	
	/*
	 * TODO:Earmark this for pushing into a generic utilites class.
	 */
	public void printTextThatDisappears(String s, float x, float y, float decayTime, float scaleTime, Scene scene, Font f){
		final Text text = new Text(x, y, f, s, 128, mg.getVertexBufferObjectManager());
		text.setScale(text.getScaleX()/mg.mCamera.getZoomFactor());
        text.registerEntityModifier(new ParallelEntityModifier(
                                                       // new AlphaModifier(0.2f, 0.0f, 1.0f),
                                                        new ScaleModifier(scaleTime, 0.0f, 2.0f),
                                                        new FadeOutModifier(decayTime)){
        	@Override
        	protected void onModifierFinished(IEntity pItem) {
        		mg.runOnUpdateThread(new Runnable() {
					
					@Override
					public void run() {
						text.clearUpdateHandlers();
	            		text.detachSelf();
						text.dispose();
					}
				});
        		
        		super.onModifierFinished(pItem);
        	}
        });  	
        scene.attachChild(text);
	}
	
	
	/*
	 * Prints "points scoring text"-like text that disappears in one second
	 * and attaches it to the game scene. Just a convienience function for
	 * getting something on the game screen fast.
	 */
	public void printTextThatDisappears(String s, Font f){
		printTextThatDisappears(s, mg.mCamera.getCenterX(), mg.mCamera.getCenterY()+128f, 2.0f, 0.3f, gameScene,f);	
	}
	
	
	
	public void executeTrigger(ObjectMeta trigger){
		
		if(trigger.getEditorData() instanceof S_Trigger){
			final S_Trigger trig = (S_Trigger)trigger.getEditorData();
			
			Log.w("spt.executeTrigger","Executing trigger");
			
			if(trig.text!=null && trig.text.length()>0){
				Log.w("spt.executeTrigger", "Printing "+trig.text+" to screen");

				
				final Text text = new Text(mg.mCamera.getCenterX()-32, mg.mCamera.getCenterY()+32, mg.mFont, trig.text, 128, mg.getVertexBufferObjectManager());
				text.setScale(text.getScaleX()/mg.mCamera.getZoomFactor());
	            text.registerEntityModifier(new ParallelEntityModifier(
	                                                           // new AlphaModifier(0.2f, 0.0f, 1.0f),
	                                                            new ScaleModifier(0.3f, 0.0f, 2.0f),
	                                                            new FadeOutModifier(2.0f)){
	            	@Override
	            	protected void onModifierFinished(IEntity pItem) {
	            		mg.runOnUpdateThread(new Runnable() {
							
							@Override
							public void run() {
								text.clearUpdateHandlers();
			            		text.detachSelf();
								
							}
						});
	            		
	            		super.onModifierFinished(pItem);
	            	}
	            });
	            
	            
	            //text.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	            
	            	
	            gameScene.attachChild(text);
	            
			}
			
			if(trig.zoom!=null){
				Log.w("spt.executeTrigger", "Zooming to "+trig.zoom);
				mg.mCamera.setZoomFactor(trig.zoom);
			}
		
			
		}
	}
	
	private void collide(final Contact pContact){
		/*
		 * I use Strings for fixtures just to keep it simple for now.
		 * Since they are parts of bodies you can always get the ObjectMeta
		 * from the body it is attached to if you need more information.
		 */
		
		
		final Fixture fixA = pContact.getFixtureA();
		final Fixture fixB = pContact.getFixtureB();
		
		//final String fixAName = fixA.getUserData()==null? "":(String)fixA.getUserData();
		//final String fixBName = fixB.getUserData()==null? "":(String)fixB.getUserData();
	
		/*
		 * The body user data has all the Object Meta information.	
		 */
		final Body bodyA = fixA.getBody();
		final Body bodyB = fixB.getBody();

		final ObjectMeta metaA = (ObjectMeta) bodyA.getUserData();
		final ObjectMeta metaB = (ObjectMeta) bodyB.getUserData();

		
		boolean aIsProjectile = false;
		boolean bIsProjectile = false;
		
		
		

		/*
		if (metaA == null) {
			Log.e("Collision:", "metaA is null");
			return;
		} else if (metaA.getName() == null) {
			Log.e("Collision", "metaA.getName is null");
			return;
		} else if (metaA.getEntity() == null) {
			Log.e("Collision", "metaA.getEntity is null");
			return;
		}

		if (metaB == null) {
			Log.e("Collision:", "metaB is null");
			return;
		} else if (metaB.getName() == null) {
			Log.e("Collision", "metaB.getName is null");
			return;
		} else if (metaB.getEntity() == null) {
			Log.e("Collision", "metaB.getEntity is null");
			return;
		}*/
		
		

		if(metaA.getName().equalsIgnoreCase("projectile")){

			explode(metaA, pContact);


			aIsProjectile = true;
		}
		else if(metaB.getName().equalsIgnoreCase("projectile")){

			explode(metaB, pContact);
			bIsProjectile = true;
		}
		
		
		
		if(aIsProjectile && metaB.getName().equalsIgnoreCase("powerup")){

				explodeSpin((Entity)metaB.getEntity(), metaA.getBody().getLinearVelocity());
				powerupsHit+=1;
				shotsLanded+=1;
				soundUpvote.stop();
				soundUpvote.setVolume(0.1f);
				soundUpvote.play();
	
				printTextThatDisappears(""+multiplier, mg.mFontStrokeArial);
	
				score+=125*multiplier;
				multiplier+=1; 
				LevelUtils.statsPowerup();
				
				explodeCoin(metaB, metaA, pContact);
				coins+=1;
			
		}
		else if(bIsProjectile && metaA.getName().equalsIgnoreCase("powerup")){
			explodeSpin((Entity)metaA.getEntity(), metaB.getBody().getLinearVelocity());
			powerupsHit+=1;
			shotsLanded+=1;
	
			soundUpvote.stop();
			soundUpvote.setVolume(0.1f);
			soundUpvote.play();

			printTextThatDisappears(""+multiplier, mg.mFontStrokeArial);

			score+=125*multiplier;
			multiplier+=1;
			LevelUtils.statsPowerup();
			
			explodeCoin(metaA, metaB, pContact);
			coins+=1;
			
		}
		else if(metaA.getName().equalsIgnoreCase("debris") && metaB.getName().equalsIgnoreCase("powerup")){
			explodeSpin((Entity)metaB.getEntity(), null);
			printTextThatDisappears(""+multiplier, mg.mFontStrokeArial);
			score+=125 * multiplier;
			powerupsHit+=1;
			shotsLanded+=1;
			multiplier+=1;
			soundUpvote.setVolume(0.1f);
			soundUpvote.play();
			LevelUtils.statsPowerup();
			
			explodeCoin(metaB, metaA, pContact);
			coins+=1;
			
			mg.runOnUpdateThread(new Runnable() {
				
				@Override
				public void run() {
					poolDebris.recyclePoolItem((Rectangle)metaA.getEntity());
					if(metaA.getAffiliatedObj().size()>0)
						poolExplosionSmokeTrails.recyclePoolItem((SpriteParticleSystem)metaA.getAffiliatedObj().get(0));
					
				}
			});
			
		}
		else if(metaA.getName().equalsIgnoreCase("powerup") && metaB.getName().equalsIgnoreCase("debris")){
			explodeSpin((Entity)metaA.getEntity(), null);
			printTextThatDisappears(""+multiplier, mg.mFontStrokeArial);
			score+=125 * multiplier;
			powerupsHit+=1;
			shotsLanded+=1;
			multiplier+=1;
			soundUpvote.stop();
			soundUpvote.setVolume(0.1f);
			soundUpvote.play();
			LevelUtils.statsPowerup();
			
			explodeCoin(metaA, metaB, pContact);
			coins+=1;
			
			mg.runOnUpdateThread(new Runnable() {
				
				@Override
				public void run() {
					poolDebris.recyclePoolItem((Rectangle)metaB.getEntity());
					if(metaB.getAffiliatedObj().size()>0)
						poolExplosionSmokeTrails.recyclePoolItem((SpriteParticleSystem)metaB.getAffiliatedObj().get(0));
					
				}
			});
			
		}
		else if(aIsProjectile && metaB.getName().equalsIgnoreCase("naughtyLetter")){
			explode(metaB, pContact);
			Log.w("collision","naughtyLetter was B");
		}
		else if(bIsProjectile&& metaA.getName().equalsIgnoreCase("naughtyLetter")){
			explode(metaA, pContact);
			Log.w("collision", "naughtyLetter was A");
		}
		else if(aIsProjectile && metaB.getName().equalsIgnoreCase("earth")){
			
			shotsLanded+=1;
			//if(!LevelFactory.isCreator || Globals.instance().isDebug())
				pause();
			explode(metaB, pContact);
			
			
			
		}
		else if(bIsProjectile && metaA.getName().equalsIgnoreCase("earth")){
			shotsLanded+=1;

			//if(!LevelFactory.isCreator || Globals.instance().isDebug())
				pause();
			explode(metaA, pContact);

		}
		else if(aIsProjectile || bIsProjectile){ //projectile has struck something other than a target
			
		}
		else if(metaA.getName().equalsIgnoreCase("missle")
				&& metaB.getName().equalsIgnoreCase("powerup")){
			explodeSpin((Entity)metaB.getEntity(), metaA.getBody().getLinearVelocity());
			powerupsHit+=1;
			soundUpvote.setVolume(0.1f);
			soundUpvote.play();

			score+=100;
			LevelUtils.statsPowerup();
			damageSpaceship();
			
		} 
		else if(metaA.getName().equalsIgnoreCase("powerup")
				&& metaB.getName().equalsIgnoreCase("missle")){
			explodeSpin((Entity)metaA.getEntity(), metaB.getBody().getLinearVelocity());
			powerupsHit+=1;
			soundUpvote.setVolume(0.1f);
			soundUpvote.play();

			score+=100;
			LevelUtils.statsPowerup();
			damageSpaceship();
		}
		else if(metaA.getName().equalsIgnoreCase("missle") 
				&& metaB.getName().equalsIgnoreCase("warp")){
			
			final S_Warp from = (S_Warp)metaB.getEditorData();
			final S_Warp to = from.connectsToWarp;
			if(from!=to){
				mg.runOnUpdateThread(new Runnable() {
					
					@Override
					public void run() {
						Log.d("Missle collided warp", "from =["+from.x+","+from.y+"] to =["+to.x+","+to.y+"]");
						spinTheMissleAndWarp(to);
						
					}
				});
			}
			
		} 
		else if(metaA.getName().equalsIgnoreCase("warp") 
				&& metaB.getName().equalsIgnoreCase("missle")){
			
			final S_Warp from = (S_Warp)metaA.getEditorData();
			final S_Warp to = from.connectsToWarp;
			if(from!=to){
				mg.runOnUpdateThread(new Runnable() {
					
					@Override
					public void run() {
						Log.d("Warp collided missle", "from =["+from.x+","+from.y+"] to =["+to.x+","+to.y+"]");
						spinTheMissleAndWarp(to);
					}
				});
			}
			
			
		} 
		else if (metaA.getName().equalsIgnoreCase("earth")
				&& metaB.getName().equalsIgnoreCase("missle")) {
			
			pause();
			explode(metaA, pContact);
			explode(metaB, pContact);


			
		} else if (metaA.getName().equalsIgnoreCase("missle")
				&& metaB.getName().equalsIgnoreCase("earth")) {
			
			pause();
			explode(metaA, pContact);
			explode(metaB, pContact);

			
		} 
		
		
		//This is not being used
		else if (metaA.getName().equalsIgnoreCase("asteroid")
				&& metaB.getName()
						.equalsIgnoreCase("satellite")) {
			// EXPLODE metaB
			explode(metaB, pContact);

		} else if (metaA.getName()
				.equalsIgnoreCase("satellite")
				&& metaB.getName().equalsIgnoreCase("asteroid")) {
			// EXPLODE metaA
			explode(metaA, pContact);
		} else if (metaA.getName().equalsIgnoreCase(
				"coneOfDoom")
				&& metaB.getName().equalsIgnoreCase("asteroid")) {
			explode(metaB, pContact);
		} else if (metaA.getName().equalsIgnoreCase("asteroid")
				&& metaB.getName().equalsIgnoreCase(
						"coneOfDoom")) {
			explode(metaA, pContact);
		} 
		
		else if (metaA.getName().equalsIgnoreCase("missle")
				&& (metaB.getName().equalsIgnoreCase(
						"coneOfDoom") || metaB.getName()
						.equalsIgnoreCase("wall"))) {

			if(missleHits>=Spaceship.max_missle_hits[spaceship.index]){
				explode(metaA, pContact);
				LevelUtils.statsDeath();
				mg.getEngine().registerUpdateHandler(new TimerHandler(1f,
					new ITimerCallback() {
						@Override
						public void onTimePassed(TimerHandler pTimerHandler) {
							showRefreshButton(mg.mCamera.getCenterX(), mg.mCamera.getCenterY());
						}
					}));
				missleHits = 0;
			}
			else{
				missleHits+=1;

				damageSpaceship();
			}
			


		} else if ((metaA.getName().equalsIgnoreCase(
				"coneOfDoom") || metaA.getName()
				.equalsIgnoreCase("wall"))
				&& metaB.getName().equalsIgnoreCase("missle")) {
			

			
			if(missleHits>=Spaceship.max_missle_hits[spaceship.index]){
				explode(metaB, pContact);
	
				LevelUtils.statsDeath();
				mg.getEngine().registerUpdateHandler(new TimerHandler(1f,
					new ITimerCallback() {
						@Override
						public void onTimePassed(TimerHandler pTimerHandler) {
							showRefreshButton(mg.mCamera.getCenterX(), mg.mCamera.getCenterY());
						}
					}));
				missleHits = 0;
			}
			else {
				missleHits+=1;
				damageSpaceship();
			}


		}
		else if(metaA.getName().equalsIgnoreCase("missle") && metaB.getName().equalsIgnoreCase("trigger")){
			executeTrigger(metaB);
		}
		else if(metaB.getName().equalsIgnoreCase("missle") && metaA.getName().equalsIgnoreCase("trigger")){
			executeTrigger(metaA);
		}
		
	}
	
	int missleHits = 0;
	//float missleHitsMax = 3;
	float shotsFired = 0;
	float shotsLanded = 0;

    public void initPhysics(){

    	
 	   mg.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_DEATH_STAR_I), true);

 	  /* Set up collision action listeners */
       mg.mPhysicsWorld.setContactListener( new ContactListener() {
           
    	   
       		@Override
           public void beginContact(final Contact pContact) {
       		
       			
				collide(pContact);
				
       			
           }

           @Override
           public void preSolve(Contact contact, Manifold oldManifold) {
           	
           }

           @Override
           public void postSolve(final Contact contact, ContactImpulse impulse) {
           	
        	   
           }
              
           @Override
           public void endContact(final Contact pContact) {
           	
        	   
           }
       });
 	   gameScene.registerUpdateHandler(mg.mPhysicsWorld); 
    }
    

    Sprite    refreshButton = null;
    
    
    public void showRefreshButton(float x, float y){
    	mg.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				refreshButton = new Sprite(mg.mCameraWidth/2, mg.mCameraHeight/2, tex_buttonRefreshTextureRegion, mg.getVertexBufferObjectManager());
		    	refreshButton.setAlpha(0.0f);
		    	refreshButton.setScale(0.8f);
		    	refreshButton.registerEntityModifier(new AlphaModifier(0.5f, 0.0f, 0.9f));
		    	mg.mHud.attachChild(refreshButton);
		    	mg.mHud.sortChildren();
			}
		});
	    
    
    }
    
    /*
     * Already runs on update thread. as it is called from createMissle.
     */
    public void removeRefreshButton(){
    	
    	if(refreshButton!=null){

			mg.mHud.detachChild(refreshButton);
		    refreshButton = null;

    		
    		
    	}
    }
    
    
    
    
    boolean isCalibrationShowing = false;
 
    public void showCalibrationScene(){
    	final float curCamX = mg.mCamera.getCenterX();
    	final float curCamY = mg.mCamera.getCenterY();
    	
    	/* Set the camera way offscene so we don't hit any UI touch areas */
    	final float offsetX = -120000f;
    	final float offsetY = -120000f;
    	mg.mCamera.setCenterDirect(offsetX, offsetY);
    	
    	
    	isCalibrationShowing = true;
    	final Scene cScene = new Scene();
    	cScene.setColor(1.0f, 1.0f, 1.0f);
    	cScene.setAlpha(0.8f);
    	


    	
    	Rectangle bigR = new Rectangle(offsetX+0, offsetY+0, 64, 64, mg.getVertexBufferObjectManager());
    	final Rectangle lilR = new Rectangle(0, 0, 48, 48, mg.getVertexBufferObjectManager());
    	
    	lilR.setAlpha(0.6f);

    
    	lilR.registerUpdateHandler(new IUpdateHandler() {
			
			@Override
			public void reset() {}
			
			@Override
			public void onUpdate(float pSecondsElapsed) {

				lilR.setX(offsetX+(accelX-accelOffsetX)*32f);
				lilR.setY(offsetY+(accelY-accelOffsetY)*32f);

				
			}
		});
    	lilR.setColor(Color.BLUE);

    	
    	
    	
    	
    	JWOW_ButtonFactory bf = new JWOW_ButtonFactory(mg);
    	JWOW_Button calibrate = bf.init("Calibrate", 128f, 64f, new IOnClickCallback() {
			@Override
			public void onClick(Object clickedObject) {
				Log.w("calibrate", "accelOffsetX="+accelX+", accelOffsetY="+accelY);
				accelOffsetX = accelX;
				accelOffsetY = accelY;
				
			}
		});
    	JWOW_Button ok = bf.init("Ok", 128f, 64f, new IOnClickCallback() {
			@Override
			public void onClick(Object clickedObject) {

				
				isCalibrationShowing = false;
				
				
				mg.runOnUpdateThread(new Runnable() {
					
					@Override
					public void run() {
						gameScene.getChildScene().detachSelf();
						gameScene.clearChildScene();
						mg.mHud.setVisible(true);
						mg.mCamera.setCenterDirect(curCamX, curCamY);
						final Text text = new Text(curCamX+mg.mCameraWidth, curCamY, mg.mFontStrokeArial, Level.fileName, mg.getVertexBufferObjectManager());
						text.setScale(2.0f);
						text.setAlpha(0.0f);
						
						Path p = new Path(2).to(text.getX(), text.getY()).to(curCamX, curCamY);
						text.registerEntityModifier(new PathModifier(1.0f, p));
						
						
						AlphaModifier alpha = new AlphaModifier(2.0f, 0.0f, 1.0f);
						alpha.addModifierListener(new IModifierListener<IEntity>() {
							@Override
							public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
							
							@Override
							public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
								mg.runOnUpdateThread(new Runnable() {	
									@Override
									public void run() {
										text.detachSelf();
									}
								});
								
							}
						});
						text.registerEntityModifier(alpha);
						gameScene.attachChild(text);
					}
				});
				
				
				mg.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(LevelFactory.mode == EditorMode.ROCKET){
							Toast.makeText(mg, "Touch anywhere to launch spaceship", Toast.LENGTH_LONG).show();
						}
					}
				});
				
				
			}
		});
    	
    	calibrate.setPosition(offsetX+mg.mCameraWidth/2-128f, offsetY+-mg.mCameraHeight/2+128f);
    	ok.setPosition(offsetX+-mg.mCameraWidth/2+128f,offsetY+-mg.mCameraHeight/2+128f);
    	cScene.registerTouchArea(calibrate);
    	cScene.registerTouchArea(ok);
    	
    	//lilR.attachChild(lilR2);

    	
    	cScene.attachChild(calibrate);
    	cScene.attachChild(ok);
    	cScene.attachChild(bigR);
    	//cScene.attachChild(bigR2);
    	cScene.attachChild(lilR);
    	
    	//mg.sm.spawnCoolX(cScene);
    	mg.mHud.setVisible(false);
    	
    	
    	/*
    	printTextThatDisappears("Hold your device comfortably and touch calibrate button", 
    			mg.mCamera.getCenterX(), mg.mCamera.getCenterY()+128, 6.0f,12.0f, cScene);
    	printTextThatDisappears("Now touch Ok.", 
    			mg.mCamera.getCenterX(), mg.mCamera.getCenterY()+64, 6.0f, 8.0f, cScene);
    	*/
    	printText("Hold your device comfortably and touch calibrate button", 
    			mg.mCamera.getCenterX(), mg.mCamera.getCenterY()+128, cScene);
    	printText("Now touch Ok.", 
    			mg.mCamera.getCenterX(), mg.mCamera.getCenterY()+64, cScene);
    	
    	
    	printText("Statistics for "+Level.fileName+" "+mg.myDB.getSynopsisForLevel(Level.uuid), 
    			mg.mCamera.getCenterX(), mg.mCamera.getCenterY()-256, cScene);
    	
    	gameScene.setChildScene(cScene, true, true, true);
    	
    }
    
    
    /*
     * 2 second fade out. All it does is paint a black regtangle
     * and then vary the alpha from 0 to 1 in 3 seconds.
     */
    public void fadeOutAndReturnToMenu(float time){
    	final Rectangle crossfade = new Rectangle(mg.mCamera.getCenterX(),
    			                                  mg.mCamera.getCenterY(),
    			                                  mg.mCameraWidth/mg.mCamera.getZoomFactor(),
    			                                  mg.mCameraHeight/mg.mCamera.getZoomFactor(), 
    			                                  mg.getVertexBufferObjectManager()){
    		protected void onManagedUpdate(float pSecondsElapsed) {
    			this.setPosition(mg.mCamera.getCenterX(), mg.mCamera.getCenterY());
    			super.onManagedUpdate(pSecondsElapsed);
    		};
    	};
    	crossfade.setAlpha(0.0f);
    	
    	AlphaModifier alphaMod = new AlphaModifier(time,0.0f,1.0f);
    	alphaMod.addModifierListener(new IModifierListener<IEntity>() {
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
			
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				
				isMissleSpawnAllowed = true;
				
				mg.mHud.clearUpdateHandlers();
				mg.mHud.detachChildren();
				mg.mHud.setVisible(false);
				mg.mHud.clearTouchAreas();
				mg.mCamera.clearUpdateHandlers();
				detachCameraFromMissle();
				//mg.mCamera.setBounds(0, 0, mg.mCameraWidth, mg.mCameraHeight);
				mg.mCamera.setZoomFactor(1.0f);
				mg.mCamera.setCenterDirect(mg.mCameraWidth/2, mg.mCameraHeight/2);
				
				mg.sm.createMenuScene();
				mg.sm.setCurrentScene(SceneManager.AllScenes.MENU);
		
				soundRocketSound.stop();
				
				
				
			}
		});
    	crossfade.registerEntityModifier(alphaMod);
    	crossfade.setColor(0,0,0);
    	//gameScene.attachChild(crossfade);
    	mg.sm.getCurrentSceneObject().attachChild(crossfade);
    	
    }
    
    /* I don't think this really works yet */
    public void fadeIn(){
    	final Rectangle ok = new Rectangle(mg.mCameraWidth/2,mg.mCameraHeight/2,mg.mCameraWidth,mg.mCameraHeight, mg.getVertexBufferObjectManager());
    	ok.setAlpha(1.0f);
    	ok.registerEntityModifier(new AlphaModifier(6.0f,1.0f,0.0f));
    	ok.setColor(0,0,0);
    	gameScene.attachChild(ok);
    }
    
    private boolean paused = false;
    
    
    /*
     * Pause function iterates over the bodies and
     * makes them active or inactive, depending on whether
     * the game is paused or not paused.
     */

    
    
    public void pause(){
    	mg.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
		    	Iterator<Body> it = mg.mPhysicsWorld.getBodies();
		    	while(it.hasNext()){
		    		final Body b = it.next();

		    		
		    			b.setActive(paused);

		   
		    	
		    	
		    	
		    	}
		    	
		    	paused = !paused;
		    	
		    	//Log.w("Pause","missle is active="+bSpaceship.isActive());
			}
    	});
    	
    	
    }
    
    
    public void myDetachChild(final IEntity e, final Scene s){
    	mg.runOnUpdateThread(new Runnable()
     	{
     	    @Override
     	    public void run()
     	    {
     	    	s.detachChild(e);
     	    }
     	});
    }
    

    
    /* Show a summary of the mission complete with stats.
     * When user hits Ok, fade out and register a callback to change
     * the Scene after 3 seconds, same amount of time as the fade out.
     */
    
    
    enum MedalType{
    	ACCURACY_OVER_200,PERFECTION, POWERUPS_MASTER, MAZE_BUILDER,UNLOCKED_SPACESHIP_1,UNLOCKED_SPACESHIP_2, UNLOCKED_SPACESHIP_3;
    }
    
    private int medalIndex = 1;
    
    public void printMedal(String txt, Scene scene){
    	printTextThatDisappears(txt,mg.mCameraWidth-128f,256f, 4.0f, 1.0f, gameScene, mg.mFont);
    	
    }
    
    public void printMedal(MedalType medalType, Scene scene){
    	
    	ITextureRegion textureRegion = null;
    	Sound snd = null;
    	
    	float destinationScale = 0.2f;
    	
    	switch(medalType){
    		case UNLOCKED_SPACESHIP_1:
    			textureRegion = tex_missleTextureRegion1;
    			destinationScale = 0.3f;
    			break;
    		case UNLOCKED_SPACESHIP_2:
    			textureRegion = tex_missleTextureRegion2;
    			destinationScale = 0.3f;
    			break;
    		case UNLOCKED_SPACESHIP_3:
    			textureRegion = tex_missleTextureRegion3;
    			destinationScale = 0.3f;
    			break;
    		case ACCURACY_OVER_200:
    			textureRegion = tex_medalTextureRegion;
    			break;
    		case PERFECTION:
    			textureRegion = tex_medalTextureRegion;
    			break;
    		case POWERUPS_MASTER:
    			textureRegion = tex_medalTextureRegion;
    			break;
    		default:
    			textureRegion = tex_medalTextureRegion;
    			break;
    	}
    	/*
    	final Sprite sprite = new Sprite(mg.mCamera.getCenterX()-200, 
    									mg.mCamera.getCenterY(), 
    									textureRegion, 
    									mg.getVertexBufferObjectManager());*/
    	
    	final Sprite sprite = new Sprite(128*medalIndex, 
				128f, 
				textureRegion, 
				mg.getVertexBufferObjectManager());
    	
    	sprite.setScale(0.0f);
    	sprite.setZIndex(2);
    	
    	switch(medalType){
    	case UNLOCKED_SPACESHIP_1:
    		final Text text0 = new Text(textureRegion.getWidth()/2, 40, mg.mFont, "Unlocked spaceship", mg.getVertexBufferObjectManager());
			text0.setScale(1.2f);
			sprite.attachChild(text0);
			sprite.setColor(Color.GREEN);
			snd = soundMedal;
			snd.setVolume(0.5f);
    		break;
    	case UNLOCKED_SPACESHIP_2:
    		final Text text1 = new Text(textureRegion.getWidth()/2, 40, mg.mFont, "Unlocked spaceship", mg.getVertexBufferObjectManager());
			text1.setScale(1.2f);
			sprite.attachChild(text1);
			sprite.setColor(Color.GREEN);
			snd = soundMedal;
			snd.setVolume(0.5f);
    		break;
    	case UNLOCKED_SPACESHIP_3:
    		final Text text2 = new Text(textureRegion.getWidth()/2, 40, mg.mFont, "Unlocked spaceship", mg.getVertexBufferObjectManager());
			text2.setScale(1.2f);
			sprite.attachChild(text2);
			sprite.setColor(Color.GREEN);
			snd = soundMedal;
			snd.setVolume(0.5f);
    		break;
    	case ACCURACY_OVER_200:
    		final Text text3 = new Text(textureRegion.getWidth()/2, 40, mg.mFont, "Accuracy>200%", mg.getVertexBufferObjectManager());
    		text3.setScale(2.2f);
    		sprite.attachChild(text3);
    		sprite.setColor(Color.YELLOW);
    		snd = soundMedal;
    		snd.setVolume(0.3f);
    		break;
		case PERFECTION:
			final Text text4 = new Text(textureRegion.getWidth()/2, 40, mg.mFont, "No Wall Hits", mg.getVertexBufferObjectManager());
			text4.setScale(2.2f);
			sprite.attachChild(text4);
			sprite.setColor(Color.YELLOW);
			snd = soundMedal;
			snd.setVolume(0.3f);
			break;
		case POWERUPS_MASTER:
			final Text text5 = new Text(textureRegion.getWidth()/2, 40, mg.mFont, "Powerups Clear", mg.getVertexBufferObjectManager());
			text5.setScale(2.2f);
			sprite.attachChild(text5);
			sprite.setColor(Color.YELLOW);
			sprite.registerEntityModifier(new RotationModifier(1.0f, 0.0f, 360.0f));
			snd = soundMedal;
			snd.setVolume(0.3f);
			break;
		default:
			textureRegion = tex_medalTextureRegion;
			snd = soundMedal;
			snd.setVolume(0.3f);
			break;
    	}
    	
    	sprite.registerEntityModifier(new ScaleModifier(1.0f, 0.0f, destinationScale));
    	
    	/*
    	sprite.registerEntityModifier( new SequenceEntityModifier(

                 new ScaleModifier(1.0f, 0.0f, destinationScale),
                 new FadeOutModifier(2.0f)){
		@Override
			protected void onModifierFinished(IEntity pItem) {
				mg.runOnUpdateThread(new Runnable() {
			
					@Override
					public void run() {
						sprite.clearUpdateHandlers();
						sprite.detachSelf();
			
					}
			});
			
			super.onModifierFinished(pItem);
			}
		});  	*/              
    	
    	
		scene.attachChild(sprite);
		snd.play();
		
		medalIndex+=1;
    } 
	
	private void showMissionCompleteDialog(){
		

		
		isMissionComplete = true;
		isMissleSpawnAllowed = false;
		
		if(soundRocketSound!=null){
			soundRocketSound.stop();
		}
		
		//mg.speak("Mission complete.");

		detachCameraFromMissle();
		mg.mCamera.getHUD().setVisible(false);
		mg.mCamera.setZoomFactorDirect(1.0f);
		mg.mCamera.setCenterDirect(mg.mCameraWidth/2, mg.mCameraHeight/2);
		mg.sm.createMissionCompleteScene();
		mg.sm.setCurrentScene(AllScenes.MISSION_COMPLETE);

	}
	
	public void loadNextLevel(boolean isBuiltIn){
		
		
		
		isMissleSpawnAllowed = true;
		mg.mHud.clearUpdateHandlers();
		mg.mHud.detachChildren();
		mg.mHud.setVisible(false);
		mg.mHud.clearTouchAreas();
		mg.mCamera.clearUpdateHandlers();
		detachCameraFromMissle();
		mg.mCamera.setZoomFactor(1.0f);
		mg.mCamera.setCenterDirect(mg.mCameraWidth/2, mg.mCameraHeight/2);
		soundRocketSound.stop();
		
		
		final String[] items;
		if(isBuiltIn){
			items = mg.myDB.getLevelList();
		}
		else {
			items = mg.myDB.getUserLevelList(null);
		}
		int which = 0;
		
		//dont load the same level twice please!
		do {
			which = (int)Math.round(Math.random()*(items.length-1));
		}
		while(items[which].equals(Level.fileName));
		
		if(isBuiltIn){
			LevelUtils.loadLevelIntoMemory(mg.myDB.getLevelXMLByFileName(items[which]), mg);
		}
		else {
			LevelUtils.loadLevelIntoMemory(mg.myDB.getUserLevelXMLByFileName(items[which]), mg);
		}
		mg.sm.createGameScene("Play"); //dont think this text does anything
		mg.sm.setCurrentScene(SceneManager.AllScenes.GAME);
		showCalibrationScene();
	}
	
	
	
	enum GaugeState{
    	FULL, TWO_THIRDS, ONE_THIRD, EMPTY
    }
	
	
	Text scoreText;
	Text timeText;
	Text multiplierText;
	
	
	
	public void populateSceneCommon(String level){

		/* If user is transitioning from another level,
		 * make sure to remember reset this to false here.
		 */
		paused = false;
		
		this.misslesUsed = 0;
		this.shotsFired = 0;
		this.shotsLanded = 0;
		this.powerupsHit = 0;
		this.score = 0;
		//this.multiplier = 1; multiplier does not reset!!!
		this.medalIndex = 1;
		
		this.selectedWeapon = Globals.instance().getPrefs().getInt("selectedWeapon", 0);
		this.selectedGadget = Globals.instance().getPrefs().getInt("selectedGadget", 0);
		Log.d("populateSceneCommon", "selectedWeapon="+this.selectedWeapon);
		Log.d("populateSceneCommon", "selectedGadget="+this.selectedGadget);
		
		isMissionComplete = false;
		boolean isCreator = false;
		
		
		if(Level.creatorId.equalsIgnoreCase(LevelUtils.getCreatorId())){
			isCreator = true;
		}
		
		
		
		
		/* Every level has a missle */
		
		//populateMissle();
		
		/*
		 * The level editor has more complex behavior for touches,
		 * so that is handled within populate levelEditor function.
		 * Everybody else has the default "tap for missle".
		 */
		if(!level.equalsIgnoreCase("Level Editor")){
			gameScene.setOnSceneTouchListener(new IOnSceneTouchListener(){
	        	public boolean onSceneTouchEvent(Scene s, TouchEvent t){
	                if(t.isActionUp()) {
	                    float x =  t.getX();
	                    float y =  t.getY();
	                    mg.mCamera.setZoomFactor(1.0f);
	                    createSpaceship(x,y, (float)Math.PI/4);
	                }
	                return false;
	        	}
	        });
		}
		
		gameScene.setTouchAreaBindingOnActionDownEnabled(true);
		
		
		
            
            this.startTime = System.currentTimeMillis();
            
            /* Here is the code for the HUD.
             * the HUD has to be completely redrawn each update. why ?
             * 
             */
            
            
            
            
            
            
            //Gas gauge
            //0,1,2,3 (full) and pump icon
            
            /*
            final Sprite gauge3 = new Sprite(16, 64, gasGauge3, mg
					.getEngine().getVertexBufferObjectManager());
            final Sprite gauge2 = new Sprite(16, 64, gasGauge2, mg
					.getEngine().getVertexBufferObjectManager());
            final AnimatedSprite gauge1 = new AnimatedSprite(16, 64, gasGauge1, mg
					.getEngine().getVertexBufferObjectManager());
            gauge1.animate(new long[]{500,500});
            final Sprite gauge0 = new Sprite(16, 64, gasGauge0, mg
					.getEngine().getVertexBufferObjectManager());
            
            float gaugeScale = 0.3f;
            gauge3.setScale(gaugeScale);
            gauge2.setScale(gaugeScale);
            gauge1.setScale(gaugeScale);
            gauge0.setScale(gaugeScale);
            
            gameScene.attachChild(gauge3);
            
            final Sprite sGasSpout = new Sprite(64,64, gasSpout, mg.getEngine().getVertexBufferObjectManager());
            sGasSpout.setScale(0.5f);
            gameScene.attachChild(sGasSpout);
            
            
            
            
            IUpdateHandler elapsedTimeUpdateHandler = new IUpdateHandler() {
            	
            	GaugeState gaugeState = GaugeState.FULL;
				
				@Override
				public void reset() {}
				
				@Override
				public void onUpdate(float arg0) {
					
					if(missle==null) return;
					
					final Object missO = missle.getUserData();
					if(!paused && missO!=null){
						final ObjectMeta missOm = (ObjectMeta)missO;
						
						
						
						if(missOm.getGas()<=66 && gaugeState==GaugeState.FULL){
				
							myDetachChild(gauge3,gameScene);
							gameScene.attachChild(gauge2);
							gaugeState = GaugeState.TWO_THIRDS;
						}
						else if(missOm.getGas()<=33 && gaugeState==GaugeState.TWO_THIRDS){
							myDetachChild(gauge2, gameScene);
							gameScene.attachChild(gauge1);
							gaugeState = GaugeState.ONE_THIRD;
						}
						else if(missOm.getGas()<=0){
							myDetachChild(gauge1, gameScene);
							gameScene.attachChild(gauge0);
							gaugeState = GaugeState.EMPTY;
							mg.mHud.unregisterUpdateHandler(this);
						}
					}
				}
			};
			
            mg.mHud.registerUpdateHandler(elapsedTimeUpdateHandler);
            */
            //Time display
            mg.mFont.prepareLetters("1234567890".toCharArray());	
			final TextOptions to = new TextOptions();
			int maxCharacters = 16;
			
			Log.w("populate scene common", "bMissle==null? "+(bSpaceship==null)
					+", !bMissle.getActive()? "+(bSpaceship!=null && !bSpaceship.isActive())
					+", isMissionComplete? "+isMissionComplete
					+", !isMissleSpawnAllowed? "+!isMissleSpawnAllowed
					+", Level.powerups.size()==0? "+(Level.powerups.size()==0));
			
			if(!level.equalsIgnoreCase("Level Editor")){
				
				Rectangle container = new Rectangle(78f/2, 128/2f, 78f, 128f, mg.getVertexBufferObjectManager());
				container.setColor(Color.BLACK);
				container.setAlpha(0.0f);
				
				scoreText = new Text(78/2f, 128f-32f, mg.mFont, "", maxCharacters, to, 
						mg.getVertexBufferObjectManager()){
					protected void onManagedUpdate(float secondsElapsed){
						
						if(bSpaceship==null || !bSpaceship.isActive() || isMissionComplete || !isMissleSpawnAllowed || Level.powerups.size()==0){
							scoreText.setText("");
							super.onManagedUpdate(secondsElapsed);
							return;
						}
							this.setText(""+((int)(Math.floor(score - (System.currentTimeMillis()-startTime)/10000f))));
							super.onManagedUpdate(secondsElapsed);
					}
				};
				
				
				
				multiplierText = new Text(78/2f , 128f-64f, mg.mFont, "",5, to,
						mg.getVertexBufferObjectManager()){
					
					
					protected void onManagedUpdate(float secondsElapsed){

								this.setText("x"+multiplier);
								super.onManagedUpdate(secondsElapsed);
					}
				};
				
				multiplierText.setColor(Color.GREEN);
				
				timeText = new Text(78/2f , 128f-96f, mg.mFont, "",maxCharacters, to,
						mg.getVertexBufferObjectManager()){
					
					float time;
					//float prevTime;
					
					protected void onManagedUpdate(float secondsElapsed){
						
							if(bSpaceship==null || !bSpaceship.isActive() || isMissionComplete || !isMissleSpawnAllowed){
								timeText.setText("");
								super.onManagedUpdate(secondsElapsed);
								return;
							}
						
							time = (System.currentTimeMillis()-startTime)/1000f;
								this.setText(myTimeFormatter.format(time));
								super.onManagedUpdate(secondsElapsed);
						
					}
				};
				
				
				timeText.setScale(0.5f);
				scoreText.setScale(0.5f);
				multiplierText.setScale(0.5f);
				
				timeText.setZIndex(255); // always on top.
				scoreText.setZIndex(255);
				multiplierText.setZIndex(255);

				
				container.attachChild(timeText);
				container.attachChild(scoreText);
				container.attachChild(multiplierText);
				
				
				//labels
				Text scoreLabel = new Text(78/2f , 128f-32+14f, mg.mFont, "score",maxCharacters, to,
						mg.getVertexBufferObjectManager());
				Text multiplierLabel = new Text(78/2f , 128f-64+14f, mg.mFont, "multiplier",maxCharacters, to,
						mg.getVertexBufferObjectManager());
				Text timeLabel = new Text(78/2f , 128f-96+14f, mg.mFont, "time",maxCharacters, to,
						mg.getVertexBufferObjectManager());
				scoreLabel.setScale(0.5f);
				multiplierLabel.setScale(0.5f);
				timeLabel.setScale(0.5f);
				
				container.attachChild(scoreLabel);
				container.attachChild(multiplierLabel);
				container.attachChild(timeLabel);
				
				container.setScale(1.3f);
				container.setPosition(container.getWidthScaled()/2, container.getHeightScaled()/2+64);
			
				Text boostText = new Text(39f, 39f, mg.mFont, "boost", maxCharacters, to, mg.getVertexBufferObjectManager());
				MyRectangle boostButton = new MyRectangle(0.0f, 0.0f, 78f, 78f, mg.getVertexBufferObjectManager()){
					@Override
		            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
		                            float pTouchAreaLocalX, float pTouchAreaLocalY){

						if(pSceneTouchEvent.isActionDown()){
							boost = true;
							setColor(Color.LTGRAY);
							if(bSpaceship!=null && bSpaceship.isActive())
								soundRocketSound.play();
						}
						else if(pSceneTouchEvent.isActionUp() || pSceneTouchEvent.isActionOutside() ){
							boost = false;
							setColor(Color.BLUE);
							soundRocketSound.stop();
						}
						return true;
		    		}
				};
				boostButton.setPosition(0.0f, 0.0f);
				boostButton.attachChild(boostText);
				boostButton.setColor(Color.BLUE);
				
				mg.mHud.attachChild(boostButton);
				mg.mHud.registerTouchArea(boostButton);
				
				Text fireText = new Text(39f, 39f, mg.mFont, "fire", maxCharacters, to, mg.getVertexBufferObjectManager());
				MyRectangle fireButton = new MyRectangle(0.0f, 0.0f, 78f, 78f, mg.getVertexBufferObjectManager()){
					@Override
		            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
		                            float pTouchAreaLocalX, float pTouchAreaLocalY){

						if(pSceneTouchEvent.isActionDown()){
							
							setColor(Color.LTGRAY);
							
							//Either create a rocket or fire a projectile (depending on whether missle is active)
	        				if(bSpaceship!=null && bSpaceship.isActive()){
	        					
	        					mg.runOnUpdateThread(new Runnable() {
									
									@Override
									public void run() {
										shotsFired+=1;
										createProjectile(spaceship.getX(), 
			        							spaceship.getY(), bSpaceship.getLinearVelocity().mul(3.0f));
			
										soundPewPew.stop();
			        					soundPewPew.play();
			        					
										
									}
								});
	        					
	        				}
	        				
						}
						else if(pSceneTouchEvent.isActionUp() || pSceneTouchEvent.isActionOutside()){
							setColor(Color.BLUE);
						}

						return true;
		    		}
				};
				fireButton.setPosition(mg.mCameraWidth-fireButton.getWidthScaled(), 0.0f);
				fireButton.attachChild(fireText);
				fireButton.setColor(Color.BLUE);
				
				mg.mHud.attachChild(fireButton);
				mg.mHud.registerTouchArea(fireButton);
				
				
				mg.mHud.attachChild(container);
				
				
			}
			
			mg.mHud.setVisible(true);
			mg.mHud.sortChildren();
            
            
	}
	
	
	public AnimatedSprite createProgressBar(float x, float y){
		final AnimatedSprite progressBar = new AnimatedSprite(x, 
				y, 
				tex_whiteCircleSpinTextureRegion,
				mg.getVertexBufferObjectManager());
		

		progressBar.setScale(0.0f);
		progressBar.animate(new long[]{64,64,64,64});
		progressBar.registerEntityModifier(new ScaleAtModifier(0.8f, 0.0f, 0.3f, 0.5f, 0.5f));
		
		return progressBar;
	}
	
	private IUpdateHandler followMissle = new IUpdateHandler() {
		
		@Override
		public void reset() {}
		
		@Override
		public void onUpdate(float arg0) {
			if(spaceship!=null){
				mg.mCamera.setCenterDirect(spaceship.getX(), spaceship.getY());
			}
			
		}
	};
	
	
	public void attachCameraToMissle(){
		mg.mCamera.registerUpdateHandler(followMissle);
	}
	
	public void detachCameraFromMissle(){
		mg.mCamera.unregisterUpdateHandler(followMissle);
		mg.mCamera.clearUpdateHandlers();

	}
	
	
	
	/*
	 * BEGIN MISSLE SECTION
	 */
	SpriteParticleSystem particleSystemSmoke;
	SpriteParticleSystem particleSystemFire;
	Spaceship spaceship;
	Body bSpaceship;
	float missleScale = 0.5f/4;

	
	static boolean isMissleSpawnAllowed = true;
	static boolean isMissionComplete = false;

	
	
	
	public void damageSpaceship(){

		
		if(spaceship!=null){
			
			mg.runOnUpdateThread(new Runnable() {
				
				@Override
				public void run() {
					
					LevelUtils.statsMissleHit();
					
					multiplier = 1;						//so reset the multiplier and award no points.
					printTextThatDisappears("Multiplier Reset", mg.mFontStrokeArial);
						
						soundOw.stop();
						soundOw.play();
						spaceship.registerEntityModifier(new ParallelEntityModifier(new ColorModifier(0.4f,0.0f,1.0f,0.0f,0.0f,0.0f,0.0f),
									                                             new ScaleModifier(0.4f,missleScale, missleScale*1.2f)){
							@Override
							protected void onModifierFinished(final IEntity pItem) {
								mg.runOnUpdateThread(new Runnable() {
										
									@Override
									public void run() {
										spaceship.setColor(1.0f,1.0f,1.0f);
										spaceship.setScale(missleScale);
										//pItem.clearEntityModifiers(); if spaceship is damaged and it goes
										//into a warp zone, this clearing here was preventing
										//warp zone from spinning up the spaceship
											
									}
								});
								super.onModifierFinished(pItem);
							}
							
					});
				}
			});
		


			
		}
	}
	
	public Spaceship createSpaceshipSprite(){
		int which = Globals.instance().getPrefs().getInt("selectedSpaceship", 0);
		
		Spaceship s = new Spaceship(0, 0, which, mg);
		s.setScale(missleScale);
		
		return s;
	}

	
	public IEntity createSpaceship(final float x, final float y, final float angle) {

		mg.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				
				boolean missleIsGoingToSpawn = true;

				if (paused){
					missleIsGoingToSpawn = false;
					Log.w("GameSupport.createMissle","Missle WILL NOT spawn because [paused]");
				}
				else if(bSpaceship!=null && bSpaceship.isActive()){
					missleIsGoingToSpawn = false;
					Log.w("GameSupport.createMissle","Missle WILL NOT spawn because [missle already active]");
				}
				else if(isMissleSpawnAllowed==false){
					missleIsGoingToSpawn = false;
					Log.w("GameSupport.createMissle","Missle WILL NOT spawn because [missle spawn is not allowed]");
				}
				
				
				if(!missleIsGoingToSpawn){
					return;
				}
				
				
				//if applicable
				removeRefreshButton();
				
				
				
				
				missleHits = 0;
				misslesUsed += 1;
				startTime = System.currentTimeMillis();
				
				//if (missle == null) {
				
				
				
					

					spaceship = createSpaceshipSprite();
					spaceship.setPosition(x,y);
					
					/*
					float mis_density = 0.3f;
					float mis_elasticity = 0.1f;
					float mis_friction = 1.0f;
					*/
					float mis_density = Spaceship.densities[spaceship.index];
					float mis_elasticity = 0.1f;
					float mis_friction = 0.1f;
					final FixtureDef mis_objectFixtureDef = PhysicsFactory
							.createFixtureDef(mis_density, mis_elasticity,
									mis_friction, false, CATEGORY_BIT_MISSLE, MASK_BIT_MISSLE, (short)0);

					bSpaceship = PhysicsFactory.createBoxBody(
							mg.mPhysicsWorld, spaceship.getWidthScaled() / 2,
							spaceship.getHeightScaled() / 2,
							spaceship.getWidthScaled()/2, spaceship.getHeightScaled()/2,
							BodyType.DynamicBody, mis_objectFixtureDef);
					


					mg.mPhysicsWorld
							.registerPhysicsConnector(new PhysicsConnector(
									spaceship, bSpaceship, true, true));

					bSpaceship.setTransform(new Vector2(x/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
							                         y/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT), angle);
					
					bSpaceship.setLinearVelocity(0f,0f);
					bSpaceship.setAngularDamping(0.0f);
					
					
					bSpaceship.applyLinearImpulse((float)Math.cos(angle)*Spaceship.max_missle_speeds[spaceship.index],
							(float)Math.sin(angle)*Spaceship.max_missle_speeds[spaceship.index], 
							bSpaceship.getWorldCenter().x, bSpaceship.getWorldCenter().y);
					
					//float linX = (float)Math.cos(angle);
					//float linY = (float)Math.sin(angle);
					//Vector2 linearVelocity = new Vector2(linX,linY);
					//bMissle.setLinearVelocity(linearVelocity);
					//Log.w("Create Missle","Linear velocity=["+linearVelocity.x+","+linearVelocity.y+"]");


					final ObjectMeta om = new ObjectMeta("missle", spaceship,
							bSpaceship);
					spaceship.setUserData(om);
					bSpaceship.setUserData(om);
					
					bSpaceship.setActive(false);
					bSpaceship.setBullet(true);
					
					
					spaceship.registerUpdateHandler(new IUpdateHandler() {
						
						@Override
						public void reset() {}
						
						@Override
						public void onUpdate(float pSecondsElapsed) {
							mg.runOnUpdateThread(new Runnable() {
								
								@Override 
								public void run() {
									
									if(paused)
										return;

						     		final Vector2 tot = Vector2Pool.obtain(bSpaceship.getLinearVelocity());
						     		
						     		/*
						     		 * These magic numbers here when applying force to the spaceship reflect
						     		 * the fact that balance in handling was obtained using 10, -10 bounded tilt inputs
						     		 * with a spaceship density of 0.3. I had to increase the density of the ship
						     		 * By a few orders of magnitude because debris need to be able to bounce off the
						     		 * ship without messing with its trajectory.
						     		 */
						     		//curHeading = Vector2Pool.obtain((float)(accelX*DENSE_MISSLE/120f),(float)(accelY*DENSE_MISSLE/120f));
						     		headingPrime = Vector2Pool.obtain((float)(accelX),(float)(accelY));
					     		
						     		/*
						     		if(bMissle.getLinearVelocity().len()>=Spaceship.max_missle_speeds[missle.index]){
						     			if(particleSystemFire!=null && !particleSystemFire.isParticlesSpawnEnabled()){
											particleSystemFire.setParticlesSpawnEnabled(true);
						     			}
						     		}
						     		else {
						     			if(particleSystemFire!=null && particleSystemFire.isParticlesSpawnEnabled()){
											particleSystemFire.setParticlesSpawnEnabled(false);
						     			}
						     		}*/
						     		
						     		if(boost){
						     			particleSystemSmoke.setParticlesSpawnEnabled(true);
						     		
						     		}
						     		else {
						     			particleSystemSmoke.setParticlesSpawnEnabled(false);
						     		}
						     		
						     		headingCur = getAngle(zeroVector, tot.nor());
						     		
						     		   
						     		//float diff = Math.abs(headingCur - getAngle(zeroVector, headingPrime));
						     		
						     		//sorta twitchy
						     		//bSpaceship.applyLinearImpulse(headingPrime.div(((float)Math.cos(diff))), bSpaceship.getWorldCenter());
						     		
						     		//not bad but at high speed its hard to turn
						     		bSpaceship.applyLinearImpulse(headingPrime, bSpaceship.getWorldCenter());
						     		
						     		
						     		//cant set linear velocity to the tilt input because its too twitchy as well
						     		//bSpaceship.setLinearVelocity(headingPrime.nor().mul(bSpaceship.getLinearVelocity().len()));
						     		
						     		
						     		bSpaceship.setTransform(bSpaceship.getPosition().x, bSpaceship.getPosition().y, headingCur, false);
						     		
						     		
						     		if(!boost && bSpaceship.getLinearVelocity().len()>=Spaceship.max_missle_speeds[spaceship.index]){
						     			bSpaceship.setLinearVelocity(bSpaceship.getLinearVelocity().nor().mul(Spaceship.max_missle_speeds[spaceship.index]));
						     		}
						     		

						     		Vector2Pool.recycle(headingPrime);
						     		Vector2Pool.recycle(tot);
									
								}
							});
							
						}
					});
					
/*
					final Sprite smokeTrail = new Sprite(0, 0,
							smokeTrailTextureRegion, mg.getEngine()
									.getVertexBufferObjectManager());
*/
					// smokeTrail.setAlpha(0.2f);
					//smokeTrail.setZIndex(-1);
					//float scale = 0.8f;
					float scale = 1.8f;
					//smokeTrail.setScale(scale, scale);

					// Create the emitter at 0,0
					final PointParticleEmitter particleEmitter = new PointParticleEmitter(
							0, 0);

					float rateMin = 12f;
					float rateMax = 24f;
					int maxParticles = 240;
					particleSystemSmoke = new SpriteParticleSystem(
							particleEmitter, rateMin, rateMax, maxParticles,
							tex_circleParticleTextureRegion, mg
									.getVertexBufferObjectManager());

					
					//float expireTime = 6.0f;
					
					float expireTime = 3.0f/Spaceship.max_missle_speeds[spaceship.index];


					particleSystemSmoke
							.addParticleInitializer(new ExpireParticleInitializer<Sprite>(
									expireTime));
					particleSystemSmoke.addParticleInitializer(new ColorParticleInitializer<Sprite>(1.0f,0.3f,0.0f));
					particleSystemSmoke.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE));
					
					
					/*
					particleSystemSmoke.addParticleModifier(new ColorParticleModifier<Sprite>(1.5f, 2.0f, 1.0f, 0.8f, 
							0.3f, 0.8f, 0f, 0.8f));*/
					
					//ALPHA IN AND OUT
					particleSystemSmoke
					.addParticleModifier(new AlphaParticleModifier<Sprite>(
							0.0f, 0.5f, 0.0f, 0.7f));
					
					particleSystemSmoke
							.addParticleModifier(new AlphaParticleModifier<Sprite>(
									0.5f, expireTime, .7f, 0.0f));
					
					
					//SCALE IN AND  OUT
					particleSystemSmoke
							.addParticleModifier(new ScaleParticleModifier<Sprite>(
									0.0f, 2.5f, 0.5f, scale * 3.0f));
					/*
					particleSystemSmoke
					.addParticleModifier(new ScaleParticleModifier<Sprite>(
							1.5f, 3.0f, scale*3.0f, 0.5f));
					*/
		
					 //particleSystemSmoke.addParticleModifier(new ColorParticleModifier<Sprite>(0.0f,1.0f,1.0f,0.8f,0.9f,0.8f,0.0f,0.8f));
					 
					 particleSystemSmoke.addParticleModifier(new RotationParticleModifier<Sprite>(0.0f,expireTime,0.0f,(float)Math.PI*2));



					ObjectMeta st_om = new ObjectMeta("smokeTrail",
							particleSystemSmoke, null);
					particleSystemSmoke.setUserData(st_om);

					om.getAffiliatedObj().add(particleSystemSmoke);
					
					
					//FIRE
					
					/*
					
					final CircleParticleEmitter particleEmitterFire = new CircleParticleEmitter(0,0,5);
					
					
					particleSystemFire = new SpriteParticleSystem(
							particleEmitterFire, rateMin, rateMax, maxParticles,
							smokeTrailFireTexture, mg
									.getVertexBufferObjectManager());

					particleSystemFire.setAlpha(0.0f);
					expireTime = 0.8f;

					particleSystemFire
							.addParticleInitializer(new ExpireParticleInitializer<Sprite>(
									expireTime));
					particleSystemFire
					.addParticleModifier(new AlphaParticleModifier<Sprite>(
							0.0f, 0.5f, 0.0f, 0.7f));
					
					particleSystemFire
							.addParticleModifier(new AlphaParticleModifier<Sprite>(
									0.4f, expireTime, 1.0f, 0.0f));
					particleSystemFire
							.addParticleModifier(new ScaleParticleModifier<Sprite>(
									0.0f, 1.0f, scale/8, scale/8 * 2));
					
					particleSystemFire.setParticlesSpawnEnabled(false);

					ObjectMeta stf_om = new ObjectMeta("smokeTrail",
							particleSystemFire, null);
					particleSystemFire.setUserData(stf_om);
					

					

					om.getAffiliatedObj().add(particleSystemFire);
					*/

					/*
					 * Keep the smoke trail behind missle 
					 */
					particleSystemSmoke.registerUpdateHandler(new IUpdateHandler() {
						public void onUpdate(float pSecondsElapsed) {
							
							 //final Vector2 linearVelocity = Vector2Pool.obtain(bMissle.getLinearVelocity());


							particleEmitter.setCenterX(spaceship.getX());
							particleEmitter.setCenterY(spaceship.getY());
							
							//particleEmitterFire.setCenterX(missle.getX());
							//particleEmitterFire.setCenterY(missle.getY());
							 
		
							 //Vector2Pool.recycle(linearVelocity);
							 
							 

						}

						public void reset() {
						}
					});

				//}
					



				gameScene.attachChild(spaceship);
				gameScene.attachChild(particleSystemSmoke);
				//gameScene.attachChild(particleSystemFire);
				bSpaceship.setActive(true);

				
				soundMissleLaunch.setVolume(0.1f);
				soundMissleLaunch.play();
				
				//soundRocketSound.setVolume(0.3f);
				//soundRocketSound.setLooping(true);
				//soundRocketSound.play();

			}
		});
		
		return this.spaceship;

	}
	
	
	
	public IEntity createSatellite(float x, float y){
		return createSatellite(x,y, 1f,1f);
		
		
	}
	
	 public IEntity createSatellite(float x, float y, float motorSpeed, float scale){
     	
     	/* find the center of the sprites image so we can place the center
     	 * directly where the user touched the screen.
     	 */
     	float x_norm_touch = x;
     	float y_norm_touch = y;
     	
     	
     	
         final Sprite satellite = new Sprite(x_norm_touch,
					 y_norm_touch,
					 tex_satelliteTextureRegion,
					 mg.getEngine().getVertexBufferObjectManager()){
         	
         	@Override
             public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                             float pTouchAreaLocalX, float pTouchAreaLocalY){
         		 
         		ObjectMeta m = (ObjectMeta)this.getUserData(); 
         		
         		if(pSceneTouchEvent.isActionUp()){ 
         			
         			
         			if(jm!=null && jm.isAlreadyOpen()){
         				return true;
         			}
         		
         			
	         			
         			/*
	         		final AnimatedSprite whiteCirleSpin = new AnimatedSprite(
	         				 this.getX()-mg.whiteCircleSpinTextureRegion.getWidth()/2,
							 this.getY(), 
	                         mg.whiteCircleSpinTextureRegion, 
	                         mg.getEngine().getVertexBufferObjectManager());
	         		 
	         		 ObjectMeta om = new ObjectMeta();
	         		 om.setName("selection");
	         		 whiteCirleSpin.setUserData(om);
	         		 	
	         		whiteCirleSpin.setScale(4f);
	         		whiteCirleSpin.setAlpha(0.1f);
	         		whiteCirleSpin.animate(new long[] { 80,80,80,80}, 0, 3, true);
	         		this.attachChild(whiteCirleSpin);
	         			*/
	         		if(jm==null){
		         		jm = new JWOW_Menu(mg){
		                    	@Override
		                    	public void onOk(){
		                    		/* We want to get the satellite and then unselect it*/
		                    		/*
		                    		if(!whiteCirleSpin.isDisposed()){
		                    			whiteCirleSpin.dispose();
		                    		}
		                    		whiteCirleSpin.detachSelf();
		                    		*/
		                    	}
		                 };
	         		}
	                 jm.showDialog("Name:"+m.getName()+"\nhealth:"+m.getHealth()+"\nshield:"+m.getShield(), mg.mCameraWidth/2, mg.mCameraHeight/2, 260, 200, null,null);
	                 
         		}/*else if(pSceneTouchEvent.isActionMove()) {
         			m.getBody().setTransform(pTouchAreaLocalX, pTouchAreaLocalY, m.getBody().getAngle());
         		}*/
         		return true;
             }
         	
         	
         };
         gameScene.registerTouchArea(satellite);
         GameState.instance().addSat(satellite);
         
         
         satellite.setScale(0.14f*scale);
         satellite.setZIndex(1); //in the middle
         
         
         
         final Sprite coneOfDoom = new Sprite(satellite.getX(),
				 satellite.getY(),
				 tex_purpleConeOfDoomTextureRegion,
				 mg.getEngine().getVertexBufferObjectManager());

         //coneOfDoom.setRotationCenter(1.0f, 0.5f);
         //coneOfDoom.setScaleCenter(1.0f, 0.5f);
         coneOfDoom.setScaleX(3.0f*scale);
         coneOfDoom.setScaleY(0.3f*scale);
         //coneOfDoom.setRotation(-45f);
         
         coneOfDoom.setAlpha(0.7f);
           //bCod.setTransform(coneOfDoom.getX(), coneOfDoom.getY(), coneOfDoom.getRotation());
         
         
         //coneOfDoom.registerEntityModifier(new LoopEntityModifier(new AlphaModifier(0.6f,0.6f,0.8f)));
         
         
         gameScene.attachChild(coneOfDoom);


    
         if(Globals.instance().isDebug()){
        	 Log.w("satellite","creating satellite at "+x_norm_touch+", "+y_norm_touch);
        	 mg.printAtLoc("Satellite",0,0,satellite);
         }

         gameScene.attachChild(satellite);
         
         
         
         
         float density    = DENSE_SAT;
       	 float elasticity = 1.0f;
       	 float friction   = 0.5f;
         final FixtureDef sat_objectFixtureDef = PhysicsFactory.createFixtureDef(density, elasticity, friction, true, CATEGORY_BIT_CONEOFDOOM, MASK_BIT_CONEOFDOOM, (short)0);
         sat_objectFixtureDef.isSensor = true;
         final Body bSat = PhysicsFactory.createBoxBody(mg.mPhysicsWorld,satellite,BodyType.DynamicBody,sat_objectFixtureDef);
         
         mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(satellite,bSat,true, false)); //physics influences pos, rot?
          
         float cod_density    = 1.0f;
         float cod_elasticity = 1.0f;
         float cod_friction   = 0.0f;
         final FixtureDef cod_objectFixtureDef = PhysicsFactory.createFixtureDef(cod_density, cod_elasticity, cod_friction);  
         cod_objectFixtureDef.isSensor = true;
         final Body bConeOfDoom = PhysicsFactory.createBoxBody(mg.mPhysicsWorld, 150, 10, 350*scale, 20*scale, BodyType.DynamicBody, cod_objectFixtureDef);
         //final Body bConeOfDoom = PhysicsFactory.createBoxBody(mg.mPhysicsWorld,coneOfDoom,BodyType.DynamicBody,cod_objectFixtureDef);
         mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(coneOfDoom,bConeOfDoom,true,true));
         
         final ObjectMeta om = new ObjectMeta("coneOfDoom",coneOfDoom,bConeOfDoom);
         coneOfDoom.setUserData(om);
         bConeOfDoom.setUserData(om);
         
         ObjectMeta satelliteMetaData = new ObjectMeta("satellite",satellite,bSat);  
         satelliteMetaData.getAffiliatedObj().add(coneOfDoom);
         satellite.setUserData(satelliteMetaData); 
         bSat.setUserData(satelliteMetaData);
         
         
         
         
         

         RevoluteJointDef rj1Def = new RevoluteJointDef();
         rj1Def.initialize(bSat, bConeOfDoom, bSat.getWorldCenter());
         rj1Def.localAnchorA.set(0,0);
         rj1Def.localAnchorB.set(6*scale,0);
         rj1Def.enableMotor = true;
         rj1Def.motorSpeed = motorSpeed;
         rj1Def.maxMotorTorque = 12f;
         rj1Def.collideConnected = false;
         
         mg.mPhysicsWorld.createJoint(rj1Def);
         
         boolean moves = false;
         
         if(!moves){
        	 
        	 float anchor_density    = 1.0f;
             float anchor_elasticity = 1.0f;
             float anchor_friction   = 0.0f;
        	 Rectangle anchor = new Rectangle(satellite.getX(), satellite.getY(), 16, 16, mg.getVertexBufferObjectManager());
        	 final FixtureDef anchor_objectFixtureDef = PhysicsFactory.createFixtureDef(anchor_density, anchor_elasticity, anchor_friction);  
             final Body bAnchor = PhysicsFactory.createBoxBody(mg.mPhysicsWorld,anchor,BodyType.StaticBody,anchor_objectFixtureDef);
             
             
             mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(coneOfDoom,bConeOfDoom,false,false));
             RevoluteJointDef rj2Def = new RevoluteJointDef();
             rj2Def.initialize(bAnchor, bSat, bSat.getWorldCenter());
        	 rj2Def.localAnchorA.set(0,0);
        	 rj2Def.localAnchorB.set(0,0);
        	 rj2Def.collideConnected = false;
        	 mg.mPhysicsWorld.createJoint(rj2Def);
        	 
         }
       
         
         
         //Apply force to get them going in the same direction.
         

         
         
         /* Keep the satellites on the screen, 
          * this actually works great, and update the cone of doom
          * */
         satellite.registerUpdateHandler(new IUpdateHandler() {
				public void onUpdate(float pSecondsElapsed) {
					Vector2 curVel = bSat.getLinearVelocity();
					if(satellite.getX()>mg.mCameraWidth || satellite.getX()<0){
						bSat.setLinearVelocity(-curVel.x,curVel.y);
					}
					if(satellite.getY()>mg.mCameraHeight || satellite.getY()<0){
						bSat.setLinearVelocity(curVel.x, -curVel.y);
					}	
				}
				
				public void reset(){}
         });

         return satellite;
     }
	 

	 
	 
	public void myDetachEntity(final Entity sprite){

		ObjectMeta om = (ObjectMeta)sprite.getUserData();
		sprite.detachSelf();
		
		sprite.clearUpdateHandlers();
		sprite.clearEntityModifiers();

		if (om!=null && om.getBody() != null) {
			om.getBody().setActive(false);
		}
		else{
			Log.w("myDetachEntity", "om was null or om.getBody() was null!");
		}

		ObjectMeta omm;
		if(om!=null && om.getAffiliatedObj()!=null){
			for (Entity e : om.getAffiliatedObj()) {
				omm = (ObjectMeta) e.getUserData();
	
				if(e instanceof ParticleSystem<?>){
					final ParticleSystem<?> p = (ParticleSystem<?>)e;
					p.setParticlesSpawnEnabled(false);
	
					
				}
				else if(e instanceof SpriteParticleSystem){
					final SpriteParticleSystem p = (SpriteParticleSystem)e;
					p.setParticlesSpawnEnabled(false);
				}
				else {
	
					e.detachSelf();
				}
	
				if (omm != null && omm.getBody() != null) {
					omm.getBody().setActive(false);
				}
			}
		}
		
		
	}
	
	public class SwirlExplosion extends AnimatedSprite{
		
		public SwirlExplosion(float x, float y, ITiledTextureRegion tr, VertexBufferObjectManager vbo){
			super(x,y,tr, vbo);
			this.setScale(0.5f);
			this.setAlpha(0.6f);
			this.setCullingEnabled(true);

		}
		
		/*
		 * Impact direction will tell the explosion which way approximately
		 * to fire the debris. This way, the user controls somewhat the spray
		 * of debris and can anticipate where to fly to collect the coins.
		 * if null, it will explode in random direction.
		 */
		public void animateNow(final Vector2 impactDirection){
			this.animate(new long[] { 50, 75, 75, 75, 75, 75, 75,
					75 }, 0, 7, false, new IAnimationListener() {
						
						@Override
						public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
								int pInitialLoopCount) {}
						
						@Override
						public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
								int pRemainingLoopCount, int pInitialLoopCount) {}
						
						@Override
						public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,
								int pOldFrameIndex, int pNewFrameIndex) {
								if(pNewFrameIndex==3){
									
									mg.runOnUpdateThread(new Runnable() {
										
										@Override
										public void run() {
											createDebris(SwirlExplosion.this.getX(), SwirlExplosion.this.getY(), impactDirection);
										}
									});
									
								}
						}
						
						@Override
						public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
							mg.runOnUpdateThread(new Runnable() {
								
								@Override
								public void run() {
									poolSwirlExplosion.recyclePoolItem(SwirlExplosion.this);
								}
							});
							
							
						}
					});
		}
		
		
	}
	
	/*
	 * Whats special about this pause function is that it does not
	 * mess with powerups. Those things get inactivated as you shoot
	 * and if you reactivate wholesale those shot powerups do also,
	 * so it made sense to keep them the way they were, no matter
	 * whether the game is paused or not.
	 */
	private void warpPause(){
		mg.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
		    	Iterator<Body> it = mg.mPhysicsWorld.getBodies();
		    	while(it.hasNext()){
		    		final Body b = it.next();
		    		
		    		ObjectMeta om = (ObjectMeta)b.getUserData();
		    		boolean isPowerup = om.getName().equalsIgnoreCase("powerup");
		    		boolean isProjectile = om.getName().equalsIgnoreCase("projectile");

		    			if(b!=bSpaceship && !isPowerup && !isProjectile)
		    				b.setActive(paused);
	
		    	}
		    	
		    	paused = !paused;
			}
    	});
	}
	
	public void spinTheMissleAndWarp(final S_Warp to){
		
		
		final Vector2 oldSpeed = Vector2Pool.obtain(bSpaceship.getLinearVelocity());
		float oldScale = spaceship.getScaleX();
		final float oldRotation = spaceship.getRotation();
		//bMissle.setAngularVelocity(10f);
		//bMissle.setLinearVelocity(0.0f,0.0f);
		
		final ParallelEntityModifier spinDown = new ParallelEntityModifier(new ScaleModifier(2.0f,oldScale, 0.0f, EaseBackIn.getInstance()));
		final ParallelEntityModifier spinUp   = new ParallelEntityModifier(new ScaleModifier(2.0f, 0.0f, oldScale, EaseBackOut.getInstance()));
		
		spinDown.addModifierListener(new IModifierListener<IEntity>() {
			
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				soundSpinDown.setVolume(0.2f);
				soundSpinDown.play();
				warpPause();
				bSpaceship.setLinearVelocity(0.0f, 0.0f);
				bSpaceship.applyAngularImpulse(200f);
				
				
			}
			
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				spaceship.registerEntityModifier(spinUp);
				bSpaceship.setTransform(to.x/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
						to.y/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, oldRotation);
				spinDown.removeModifierListener(this);
			}
		});
		
		spinUp.addModifierListener(new IModifierListener<IEntity>() {
			
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				soundSpinUp.setVolume(0.2f);
				soundSpinUp.play();
			}
			
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				warpPause();
				bSpaceship.setTransform(to.x/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
						to.y/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, oldRotation);
				bSpaceship.setAngularVelocity(0.0f);
				bSpaceship.setLinearVelocity(oldSpeed);
				Vector2Pool.recycle(oldSpeed);
				spinUp.removeModifierListener(this);
			}
		});
		
		spaceship.registerEntityModifier(spinDown);
		
		
		
		
		
		
		
	}
	
	public void explodeCoin(final ObjectMeta missle, final ObjectMeta debris, Contact pContact){
		
		mg.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {	
				final Entity e = (Entity)missle.getEntity();
				


				final Sprite coin = new Sprite(e.getX(), e.getY(), tex_coinTextureRegion, mg.getVertexBufferObjectManager());
				coin.setScale(5.0f);
				
				Path coinPath = new Path(2).to(e.getX(), e.getY()).to(e.getX(), e.getY()+128f);
				coin.registerEntityModifier(new PathModifier(1.0f, coinPath));
				AlphaModifier alphaMod = new AlphaModifier(1.0f, 0.0f, 1.0f);
				alphaMod.addModifierListener(new IModifierListener<IEntity>() {
					@Override
					public void onModifierStarted(IModifier<IEntity> arg0,IEntity arg1) {}
	
					@Override
					public void onModifierFinished(IModifier<IEntity> arg0,IEntity arg1) {
						mg.runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
								coin.detachSelf();
							}
						});
					}
				});
				
				
				coin.registerEntityModifier(alphaMod);

				
				
				
				soundCoin.stop();
				soundCoin.play();
				gameScene.attachChild(coin);
			
			}
		});
	}
	
	
	public void explodeSpin(final Entity sprite, final Vector2 impactDirection){

		mg.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				final SwirlExplosion se = poolSwirlExplosion.obtainPoolItem();
				se.animateNow(impactDirection);
				se.setPosition(sprite);
				myDetachEntity(sprite);
			}
		});
	}

	public void shatter(final Sprite s, int num){
		
		final ITextureRegion t = s.getTextureRegion();
		
		final ITexture texture = t.getTexture();
		
		float incX = s.getWidthScaled()/num;
		float incY = s.getHeightScaled()/num;
		
		for(int i = 0; i<num; i++){
			for(int j=0; j<num; j++){
				
				final ITextureRegion square = t.deepCopy();
				square.setTextureSize(incX, incY);
				square.setTexturePosition(incX*i, incY*j);
				
				Sprite slice = new Sprite(s.getX()*i, s.getY()*j,square, mg.getVertexBufferObjectManager());
				gameScene.attachChild(slice);
			}
		}
	}
     
     
	public void explode(final ObjectMeta om, Contact pContact) {
		
		mg.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {	
				Entity sprite = (Entity)om.getEntity();
				//final ObjectMeta om = (ObjectMeta) sprite.getUserData();
				float contactPointX = sprite.getX();
				float contactPointY = sprite.getY();
				
				final boolean isMissle = om.getName().equalsIgnoreCase("missle");
				final boolean isEarth  = om.getName().equalsIgnoreCase("earth");
				final boolean isProjectile = om.getName().equalsIgnoreCase("projectile");
				final boolean isNaughtyLetter = om.getName().equalsIgnoreCase("naughtyLetter");
				
				
				
				/*
				 * Missle spawn is not allowed until the
				 * explosion completes.
				 */
				
				
				if(isMissle){
					isMissleSpawnAllowed = false;
					
					myDetachEntity(sprite);
					
					//TODO: 07-12-2014 troubleshoot warp problem
					if(bSpaceship!=null){
						mg.mPhysicsWorld.destroyBody(bSpaceship);
						bSpaceship = null;
					}
					detachCameraFromMissle();
					
					if(Globals.instance().isDebug()){
						Log.w("Explode","isMissleSpawnAllowed="+isMissleSpawnAllowed);
					}
					
					soundExplosionBitsy.setVolume(0.05f);
					soundExplosionBitsy.play();
					
					soundRocketSound.stop();
					createDebris(contactPointX, contactPointY, null);
					
				} 
				else if (isEarth) {

					
					soundExplosionBitsy.setVolume(0.05f);
					soundExplosionBitsy.play();
					
					
					createDebris(contactPointX, contactPointY, null);
					
					
					final CircleParticleEmitter emitter = new CircleParticleEmitter(contactPointX, contactPointY, 64f);
					
			      
			        final SpriteParticleSystem particleSystem = new SpriteParticleSystem(
			         		emitter, 10f, 34f, 240,tex_explosionTextureRegion, mg.getVertexBufferObjectManager());
			         		
					


			        particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-7, 7, -8, 6));
			        particleSystem.addParticleInitializer(new ScaleParticleInitializer<Sprite>(0.3f));
			       // particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(1.6f));
			        particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(0.0f, 0.9f, 0f, 0.6f));
			        /*
			        particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(0.5f, 2.0f, 
			        		1f,   0.5f, 	//R
			        		1f,   0.2f, //G
			        		0.0f, 0.5f)); //B*/
			        particleSystem.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback() {
						
						@Override
						public void onTimePassed(TimerHandler pTimerHandler) {
							particleSystem.setParticlesSpawnEnabled(false);
								showMissionCompleteDialog();
						}
					}));
			        
			
			        
			        emitter.setCenter(sprite.getX(),  sprite.getY());
			        gameScene.attachChild(particleSystem);
					
					
					float delay = 0.5f;
					createShockWave(delay, contactPointX, contactPointY);
			     	
					myDetachEntity(sprite);
					
					soundRocketSound.stop();
		
				} 
				else if(isProjectile){
					final Sprite projectileFlash = new Sprite(contactPointX,
							contactPointY, tex_projectileExplosion, mg.getEngine()
									.getVertexBufferObjectManager());
					
					projectileFlash.setScale(0.25f);
					projectileFlash.setBlendingEnabled(true);

					
					
					//double ran0 = Math.random();
					double ran1 = Math.random();
					//if(ran0>0.5d)
						projectileFlash.setColor(Color.BLUE);
					//else
						//projectileFlash.setColor(Color.RED);
					
					projectileFlash.setRotation((float)(ran1*360f));
					projectileFlash.setAlpha(0.3f);
					
					final AlphaModifier projectileFlashAlphaModifier = new AlphaModifier(1.0f, 0.8f, 0.0f);
					projectileFlashAlphaModifier.addModifierListener(new IModifierListener<IEntity>() {
						
						@Override
						public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
						
						@Override
						public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
							mg.runOnUpdateThread(new Runnable() {
								
								@Override
								public void run() {
									gameScene.detachChild(projectileFlash);
									
								}
							});
							
							
						}
					});
					
					projectileFlash.registerEntityModifier(projectileFlashAlphaModifier);
		
					gameScene.attachChild(projectileFlash);
					
					final Projectile projectile = (Projectile)om.getEntity();
		
					
					
					
					if(projectile.getType()==Projectile.Type.ENEMY_PROJECTILE){
						poolEnemyProjectile.recyclePoolItem(projectile);
					}
					else if(projectile.getType()==Projectile.Type.FRIENDLY_PROJECTILE){
						poolProjectile.recyclePoolItem(projectile);
					}
					
			  
				}
				else if(isNaughtyLetter){
					
					Log.w("explode","was naughty letter");
		
					final CircleParticleEmitter emitter = new CircleParticleEmitter(contactPointX, contactPointY, 10);
					
			        
			        final SpriteParticleSystem particleSystem = new SpriteParticleSystem(
			         		emitter, 10f, 20f, 100,tex_projectileExplosion, mg.getVertexBufferObjectManager());
			        
			        //particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
			        
			        
		
			        particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-75, 75, -80, -60));
			        
			        
			        particleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(1f, 1f, 0.8f));
			        //particleSystem.addParticleInitializer(new AlphaParticleInitializer<Sprite>(0.5f));
			        particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(1.6f));
			        
			        particleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0f, 1.6f, 0f, 1.4f));
			        particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(0.0f, 1f, 0f, 0.4f));
			        particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(0.5f, 1.0f, 
			        		1f,   0.5f, 	//R
			        		1f,   0.2f, //G
			        		0.8f, 0.5f)); //B
			        particleSystem.registerUpdateHandler(new TimerHandler(2.0f, new ITimerCallback() {
						
						@Override
						public void onTimePassed(TimerHandler pTimerHandler) {
							mg.runOnUpdateThread(new Runnable() {
								
								@Override
								public void run() {
									particleSystem.setParticlesSpawnEnabled(false);
									myDetachEntity(particleSystem);
								}
							});
							
						}
					}));
			        
			        emitter.setCenter(sprite.getX(),  sprite.getY());
			        gameScene.attachChild(particleSystem);
			        
			        myDetachEntity(sprite);
				}
		
				
				
				if(isMissle || (LevelFactory.isCreator && isEarth)){
					isMissleSpawnAllowed = true;

				}
			}
		});
	
		
	} //end explode
	
	
	
	
     
     
     public float getAngle(Vector2 v1, Vector2 v2){
    	 float heading = (float)Math.acos(v1.nor().dot(v2.nor()));
  		
  		if(v2.y<0){
  			heading = (float)(2*Math.PI - heading);
  		}	
  		
  		return heading;
     }
     
     
     public void createShockWave(float delay, float x, float y){
    	 final Sprite s = new Sprite(x,y,tex_shockWaveTexture, mg.getVertexBufferObjectManager());
    	 s.registerEntityModifier(new DelayModifier(delay, new IEntityModifierListener() {
			
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
			
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				
				s.registerEntityModifier(new ParallelEntityModifier(
						new ScaleModifier(4f,0.0f,6f), 
						new AlphaModifier(4f, 0.2f, 0.0f))
				);
				
			}
		}));
    	 
    	 s.setAlpha(0.0f);
    	 s.setPosition(x,y);
    	 gameScene.attachChild(s);
     }
     
     
     
     
     int MOD_VELOCITY = 4;
     
     public void createDebris(final float x, final float y, Vector2 impactDirection){
    	 
    	 //Log.w("createDebris","YES");
    	 

				for(int i=0; i<4;i++){
					
		    		 final Vector2 linearVelocity = Vector2Pool.obtain();

		    		 if(impactDirection==null){
			    		 final double d = Math.random();
			    		 final double xIsNeg = Math.random()>=0.5? 1d:-1d;
			    		 final double yIsNeg = Math.random()>=0.5? 1d:-1d;
			    		 linearVelocity.x = (float)(d*Math.PI*2*xIsNeg + MOD_VELOCITY*xIsNeg);
			    		 linearVelocity.y = (float)(d*Math.PI*2*yIsNeg + MOD_VELOCITY*yIsNeg);
		    		 }
		    		 else {
		    			 
		    			 //direct the debris in a 45 degree spread from the impact direction.
		    			 final double d = Math.random();
		    			 linearVelocity.x = (float)(impactDirection.x + Math.cos(d*20f/Math.PI));
		    			 linearVelocity.y = (float)(impactDirection.y + Math.sin(d*20f/Math.PI));
		    			 
		    			 
		    		 }

		    		 
		    		 createSingleDebris(x,y,linearVelocity);
		    		 
		    		 Vector2Pool.recycle(linearVelocity);
		    	 }
    	 
     }
     /*
     public Debris createSingleDebris(float x, float y, final Vector2 linearVelocity){
    	 Log.w("createSingleDebris","x,y,linearVelocity["+x+","+y+","+linearVelocity.toString());
    	 final Debris deb = debrisPool.obtainPoolItem();
    	 deb.body.setTransform(x, y, 0f);
    	 deb.body.applyLinearImpulse(linearVelocity, deb.body.getWorldCenter());
    	 return deb;
     }*/
     
     public void createSingleDebris(float x, float y, final Vector2 linearVelocity){
      	
    	final Rectangle debris = poolDebris.obtainPoolItem();
    	final Body bBody = ((ObjectMeta)debris.getUserData()).getBody();
     	
     	bBody.setTransform(x/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
     			y/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0.0f);
     	bBody.setLinearVelocity(linearVelocity);

     	bBody.setAngularVelocity(10f * (float)Math.random());
    	 
    	 
    	 
    	boolean smoke = Globals.instance().isDebrisSmokeTrails();
    	 
    	 
    	if(smoke){
	    	final SpriteParticleSystem particleSystem = poolExplosionSmokeTrails.obtainPoolItem();
	    	final PointParticleEmitter particleEmitter = (PointParticleEmitter)particleSystem.getParticleEmitter();
	    	
	    	final ObjectMeta dMeta = (ObjectMeta) debris.getUserData();
	    	dMeta.getAffiliatedObj().add(particleSystem);
	
	    	debris.registerUpdateHandler(new IUpdateHandler() {
				
				@Override
				public void reset() {}
				
				@Override
				public void onUpdate(float arg0) {
	
					particleEmitter.setCenter(debris.getX(), debris.getY());
					particleSystem.setPosition(debris);
					
					if(debris.isCulled(mg.mCamera)){
	
						mg.runOnUpdateThread(new Runnable() {
							
							@Override
							public void run() {
								poolDebris.recyclePoolItem(debris);
								poolExplosionSmokeTrails.recyclePoolItem((SpriteParticleSystem)dMeta.getAffiliatedObj().get(0));
							}
						});
						
						
					}
				}
			});
    	}
		
		
       	/*
       	debris.registerUpdateHandler(new TimerHandler(0.8f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				final AlphaModifier alpha = new AlphaModifier(0.4f, debris.getAlpha(), 0.0f, EaseBounceOut.getInstance());
				alpha.addModifierListener(new IModifierListener<IEntity>() {
					
					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
					
					@Override
					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
						debris.unregisterUpdateHandler(pTimerHandler);
						poolExplosionSmokeTrails.recyclePoolItem(particleSystem);
						poolDebris.recyclePoolItem(debris);
						
					}
				});
						
				debris.registerEntityModifier(alpha);
				
			}
		}));
       	*/
       	

      }
     
     
     
     public Entity createNaughtyLetter(float x, float y, String txt){
    	 
 		
 		final NaughtyLetter n = new NaughtyLetter(x,y,mg.mFont,txt, mg);

 		//every two seconds, a letter gets to attack.
 	   	 n.registerUpdateHandler(new IUpdateHandler() {
 				float secondsSinceLastFire = 0f;
 				@Override
 				public void reset() {}
 				
 				@Override
 				public void onUpdate(float pSecondsElapsed) {
 					if(secondsSinceLastFire>=2){
 						
 						
 						secondsSinceLastFire = 0;
 						if(bSpaceship==null){
 							return;
 						}

 						final Vector2 pos = Vector2Pool.obtain(spaceship.getX(), spaceship.getY());

 						float xDist = (pos.x - n.getX());
 						float yDist = (pos.y - n.getY());

 						final Vector2 pVec = Vector2Pool.obtain(xDist, yDist);
	
 						createEnemyProjectile(n.getX(), n.getY(), pVec.nor().mul(5.0f));

 						Vector2Pool.recycle(pVec);
 						Vector2Pool.recycle(pos);
 			
 						
 					}
 					else {
 						secondsSinceLastFire += pSecondsElapsed;
 					}
 					
 				}
 			});
 	   	 
 	   	 
 	   	 
 	   	 final Path path = new Path(5)
 	   	 		.to(x-32f, y+32f)
 					.to(x+32f, y+32f)
 					.to(x+32f, y-32f)
 					.to(x-32f, y-32f)
 					.to(x-32f, y+32f);
 	   	 
 			float duration = 3f;
 			
 
 			
 			
 		n.registerEntityModifier(new LoopEntityModifier(new PathModifier(duration, path, EaseCubicInOut.getInstance())));
 		
 			
 		return n;
     }
     
     
     
     /*
      * Does NOT attach to a scene, it returns the entity which may be attached
      * to any scene of your choice.
      */
     public void createEnemyProjectile(float x, float y, final Vector2 linearVelocity){
       	
    	 final Projectile p = poolEnemyProjectile.obtainPoolItem();
    	 

    	 ObjectMeta pm = (ObjectMeta)p.getUserData();
    	 pm.getBody().setLinearVelocity(linearVelocity);
    	 pm.getBody().setTransform(x/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
    			 y/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0.0f);
    	 //p.setY(y);
    	 
    	 p.registerUpdateHandler(new TimerHandler(3.4f, new ITimerCallback() {
  			
  			@Override
  			public void onTimePassed(TimerHandler pTimerHandler) {
  				mg.runOnUpdateThread(new Runnable() {
  					
  					@Override
  					public void run() {
  	
  						poolEnemyProjectile.recyclePoolItem(p);
  						
  					}
  				});
  				
  			}
  		}));
    	 

       }
     
     
     
     /*
      * Does NOT attach to a scene, it returns the entity which may be attached
      * to any scene of your choice.
      */
     public void createProjectile(float x, float y, final Vector2 linearVelocity){
       	
    	 final Projectile p = poolProjectile.obtainPoolItem();

    	 ObjectMeta pm = (ObjectMeta)p.getUserData();
    	 pm.getBody().setLinearVelocity(linearVelocity);
    	 pm.getBody().setTransform(x/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
    			 y/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, getAngle(zeroVector, linearVelocity.nor()));
    	
       }
     
     
     
     
	 
	 public void createAsteroid(int spawnLocation){
     	
     	final Vector2 linearVelocity = new Vector2(1.0f,1.0f);
     	float astX = 0f;
     	float astY = 0f;
     	
     	
     	
     	StringBuilder sb = new StringBuilder();
     	sb.append("Creating Asteroid ");
     	
     	/* Spawn starting from each corner for now */
     	
     	switch(spawnLocation){
         	case TOP_RIGHT: 
         		linearVelocity.set(-1.0f,-1.0f);
         		astX = mg.mCamera.getXMax();
         		astY = mg.mCamera.getYMax();
         		sb.append("TOP_RIGHT");
         			break;
         	case BOTTOM_LEFT: 
         		linearVelocity.set(1.0f,1.0f);
         		astX = mg.mCamera.getXMin();
         		astY = mg.mCamera.getYMin();
         		sb.append("BOTTOM_LEFT");
         			break;
         	case BOTTOM_RIGHT: 
         		linearVelocity.set(-1.0f,1.0f);
         		astX = mg.mCamera.getXMax();
         		astY = mg.mCamera.getYMin();
         		sb.append("BOTTOM_RIGHT");
         			break;
         	case TOP_LEFT:
         		linearVelocity.set(1.0f,-1.0f);
         		astX = mg.mCamera.getXMin();
         		astY = mg.mCamera.getYMax();
         		sb.append("TOP_LEFT");
         			break;
         	default: 
         		linearVelocity.set(1.0f,0f); //left to right
         		astX = mg.mCamera.getXMin();
         		astY = mg.mCamera.getYMax()/2;
         		sb.append("DEFAULT");
     		
     	}
     	
     	sb.append(" at position "+astX+","+astY);
     	
     	Log.w("creating asteroid:",sb.toString());
     	
     	
     	
     	final Sprite smokeTrail = new Sprite(astX,
				 astY,
				 tex_smokeTrailTextureRegion,
				 mg.getEngine().getVertexBufferObjectManager());
     	
     	
     	
     	final Sprite asteroid = new Sprite(astX, 
         		astY, 
         		tex_asteroid0TextureRegion, 
         		mg.getEngine().getVertexBufferObjectManager());
     	
     	
     	
     	asteroid.setScale(0.2f, 0.2f);
     	
     	 
		
		//smokeTrail.setAlpha(0.2f);
		smokeTrail.setZIndex(-1);
		float scale = 1f;
		smokeTrail.setScale(scale,scale);
		
		//Create the emitter at 0,0
		final PointParticleEmitter particleEmitter = new PointParticleEmitter(0,0);
		
		float rateMin = 1f;
		float rateMax = 6f;
		int maxParticles = 120;
		final SpriteParticleSystem particleSystem = new SpriteParticleSystem(
		particleEmitter, rateMin, rateMax, maxParticles,tex_smokeTrailTextureRegion, mg.getVertexBufferObjectManager());
		
		float expireTime = 8.0f;
		
		//Makes like a smoke trail. this is great too.
		particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-15*linearVelocity.x, -22*linearVelocity.x, -60*linearVelocity.y, -90*linearVelocity.y));
		particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(expireTime));
		particleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(1.0f,1.0f,0.0f));
		
		particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(0.0f, expireTime, 1.0f, 0.0f));
		particleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0.0f, 15.0f,scale,scale*4));
		particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(0.0f,0.8f,1.0f,1.0f,0.0f,0.8f,0.8f,0.8f));

		//particleSystem.addParticleInitializer(new RotationParticleInitializer<Sprite>(0f, 360f));
		//particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
		//particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(0, 1, 0, 1));
		
		ObjectMeta st_om = new ObjectMeta("smokeTrail",particleSystem,null);
		particleSystem.setUserData(st_om);
		
		//smokeTrail.attachChild(particleSystem);
     	gameScene.attachChild(particleSystem);
     	

         
     	float density    = DENSE_AST;
     	float elasticity = 0.5f;
     	float friction   = 0.5f;
     	
         final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(density, elasticity, friction);
         
         
         final Body bAsteroid = PhysicsFactory.createBoxBody(mg.mPhysicsWorld, 
         										asteroid, 
         		                                 BodyType.DynamicBody, 
         		                                 objectFixtureDef);
         
         /*
         final Body bAsteroid = PhysicsFactory.createBoxBody(mg.mPhysicsWorld, asteroid.getWidthScaled()/2, asteroid.getHeightScaled()/2,
        		       asteroid.getWidthScaled()-30, asteroid.getHeightScaled(), BodyType.DynamicBody, objectFixtureDef);
         */
         //bAsteroid.setBullet(true);
         bAsteroid.setLinearVelocity(linearVelocity);
         bAsteroid.applyAngularImpulse((float)(60.0f * Math.random()));
         
         boolean updateRotationBaseOnPhysicalCollision = true;
         boolean allowPhysicsToInfluencePosition = true;
         mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(asteroid, 
         		                                                         bAsteroid, 
         		                                                         allowPhysicsToInfluencePosition, 
         		                                                         updateRotationBaseOnPhysicalCollision));
         ObjectMeta asteroidMetaData = new ObjectMeta("asteroid",asteroid,bAsteroid);
         asteroidMetaData.getAffiliatedObj().add(particleSystem);
         asteroidMetaData.setBody(bAsteroid);
         asteroid.setUserData(asteroidMetaData); 
         bAsteroid.setUserData(asteroidMetaData);
         
         mg.printAtLoc("Asteroid X5"+spawnLocation,0,0,asteroid);
         
         /* Make the smoke trail follow the asteroid but 
      	 * stays within the mScene, it is not a child of asteroid 
      	 */
      	asteroid.registerUpdateHandler(new IUpdateHandler() {
 			
 			@Override
 			public void reset() {
 				// TODO Auto-generated method stub
 				
 			}
 			
 			@Override
 			public void onUpdate(float arg0) {

 				//prefect :)
 				particleEmitter.setCenterX(asteroid.getX() + -linearVelocity.x*asteroid.getWidthScaled()/2);
 				particleEmitter.setCenterY(asteroid.getY() + -linearVelocity.y*asteroid.getHeightScaled()/2);

 				
 				if(asteroid.getX()>mg.mCameraWidth || asteroid.getX()<0 
 						|| asteroid.getY()>mg.mCameraHeight || asteroid.getY()<0){
 					
 					if(!asteroid.isDisposed()){
 						asteroid.detachSelf();
 						asteroid.dispose();
 					}
 				}
 				
 			}
 		});
      	
         
         gameScene.attachChild(asteroid);
     }
	 
	 

	 
	 
	 //Float[] max_missle_speeds;
	 //Float[] max_missle_hits;
	 


	 

     //float MAX_MISSLE_ANGULAR_V = 0.4f;
     //float MIN_MISSLE_SPEED = 3.0f;
    // float MAX_MISSLE_SPEED = 6.0f;
     //float currentMissleSpeed = 3.0f;
     Vector2 zeroVector = new Vector2(1.0f,0.0f);
     float headingCur;
     
     boolean decel = false;
     boolean accel = false;
     
     Vector2 headingPrime;
     
     static Long lastUpdateStep = 0l;
     static Long curUpdateStep = 0l;
     
     
     
     public void onAccelerationChanged(final AccelerationData d){
    	
    	if(isCalibrationShowing){
    		accelX = d.getX();
    		accelY = d.getY();
    	}
    	
    	
    	 
     	if(spaceship!=null && bSpaceship!=null && bSpaceship.isActive()==true){
     		
     		accelX = (d.getX()-accelOffsetX)/20;
     		accelY = (d.getY()-accelOffsetY)/20;
     	}
     	
     }
     public void onAccelerationAccuracyChanged(AccelerationData d){}
     
     
}
