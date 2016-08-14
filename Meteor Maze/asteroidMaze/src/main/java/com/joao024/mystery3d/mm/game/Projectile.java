package com.joao024.mystery3d.mm.game;


import android.graphics.Color;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;




public class Projectile extends Rectangle{
	
	enum Type{
		ENEMY_PROJECTILE, FRIENDLY_PROJECTILE
	}
	
	enum Behavior{
		WEP0, WEP1, WEP2, WEP3
	}
	
	float astX = 0f;
   	float astY = 0f;
   	Vector2 linearVelocity;
   	MyGameActivity mg;
   	Type type;
   	Behavior behavior;
   	
   	
   	/*
   	 * Makes a friendly projectile (i.e. one that the spaceship shoots).
   	 */
   	public Projectile(float x, float y, MyGameActivity mg){
   		super(x,y,8f,8f,mg.getVertexBufferObjectManager());
   		this.mg = mg;
   		this.type = Type.FRIENDLY_PROJECTILE;
   		this.behavior = Behavior.WEP0;
   		init();
   		
   	}
   	
   	public Projectile(float x, float y, Type enemyOrFriendly, Behavior behavior, MyGameActivity mg){
   		super(x,y,8f,8f, mg.getVertexBufferObjectManager());
   		this.mg = mg;
   		this.type = enemyOrFriendly;
   		this.behavior = behavior;
   		init();
   	}
   	
   	private void init(){
   		

        
       	float density    = 0.1f;
       	float elasticity = 1.5f;
       	float friction   = 0.1f;
       	
           FixtureDef objectFixtureDef = null;
           
           switch(this.type){
          
	           case FRIENDLY_PROJECTILE:
	        	   setColor(Color.CYAN);
	        	   objectFixtureDef = PhysicsFactory.createFixtureDef(density, elasticity, friction, false, 
	        			   mg.spt.CATEGORY_BIT_PROJECTILE, mg.spt.MASK_BIT_PROJECTILE, (short)0);
	        	   break;
	           case ENEMY_PROJECTILE:
	        	   setColor(Color.RED);
	        	   objectFixtureDef = PhysicsFactory.createFixtureDef(density, elasticity, friction, false, 
	        			   mg.spt.CATEGORY_BIT_ENEMY_PROJECTILE, mg.spt.MASK_BIT_ENEMY_PROJECTILE, (short)0);
	        	   break;
	           default:
	        	   break;
           
           
           }
           
           
           switch(behavior){
           case WEP0: 
        	   
        	   break;
           case WEP1:
        	   this.setWidth(getWidth()*2);
        	   this.setHeight(getHeight()*2);
        	   break;
           case WEP2:
        	   final Sprite missle = new Sprite(0.5f,0.5f,mg.spt.tex_missleProjectileTextureRegion, mg.getVertexBufferObjectManager());
        	   this.setColor(Color.TRANSPARENT);
        	   missle.setScaleX(0.2f);
        	   missle.setScaleY(0.4f);
        	   this.attachChild(missle);
        	   
        	   break;
           case WEP3:
        	   break;
           default:
        	   break;
           }
        		   
           
           final Body body = PhysicsFactory.createBoxBody(mg.mPhysicsWorld, 
           										this, 
           		                                 BodyType.DynamicBody, 
           		                                 objectFixtureDef);
           body.setBullet(true);

           switch(behavior){
               case WEP0: 
            	   //body.applyTorque((float)(12.0f * Math.random()));
            	   body.setAngularVelocity((float)(12*Math.random()));
            	   break;
               case WEP1:
            	   //body.applyTorque((float)(12.0f * Math.random()));
            	   body.setAngularVelocity((float)(12*Math.random()));
            	   break;
               case WEP2:
            	   break;
               case WEP3:
            	   break;
               default:
            	   break;
           }
           

           boolean updateRotationBaseOnPhysicalCollision = true;
           boolean allowPhysicsToInfluencePosition = true;
           mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this, 
           		                                                         body, 
           		                                                         allowPhysicsToInfluencePosition, 
           		                                                         updateRotationBaseOnPhysicalCollision));
           ObjectMeta asteroidMetaData = new ObjectMeta("projectile", this,body);

           asteroidMetaData.setBody(body);
           this.setUserData(asteroidMetaData); 
           body.setUserData(asteroidMetaData);
           
        	
        	
        	
        	this.setCullingEnabled(true);
        
        	
        	
   	}
   	
   	public Type getType(){
   		return this.type;
   	}
   	
   	
   	
   	
   	
   	
}
