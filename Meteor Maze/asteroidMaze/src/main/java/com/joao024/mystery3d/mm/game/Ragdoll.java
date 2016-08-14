package com.joao024.mystery3d.mm.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

public class Ragdoll extends Rectangle{

	private MyGameActivity mg;
	
	public Ragdoll(MyGameActivity mg){
		super(0.0f, 0.0f, 78f, 78f, mg.getVertexBufferObjectManager());
		setAlpha(0.01f);
		this.mg = mg;
		init();
	}
	
	public void init(){
		
		float density    = 0.1f;
      	float elasticity = 0.1f;
      	float friction   = 0.1f;
      	/*
        final FixtureDef objectFixtureDef0 = PhysicsFactory.createFixtureDef(density, elasticity, friction, false, mg.spt.CATEGORY_BIT_DEFAULT, mg.spt.MASK_BIT_DEFAULT,(short) 0);
        final FixtureDef objectFixtureDef1 = PhysicsFactory.createFixtureDef(density, elasticity, friction, false, mg.spt.CATEGORY_BIT_DEFAULT, mg.spt.MASK_BIT_DEFAULT,(short) 0);
        final FixtureDef objectFixtureDef2 = PhysicsFactory.createFixtureDef(density, elasticity, friction, false, mg.spt.CATEGORY_BIT_DEFAULT, mg.spt.MASK_BIT_DEFAULT,(short) 0);
        final FixtureDef objectFixtureDef3 = PhysicsFactory.createFixtureDef(density, elasticity, friction, false, mg.spt.CATEGORY_BIT_DEFAULT, mg.spt.MASK_BIT_DEFAULT,(short) 0);
        final FixtureDef objectFixtureDef4 = PhysicsFactory.createFixtureDef(density, elasticity, friction, false, mg.spt.CATEGORY_BIT_DEFAULT, mg.spt.MASK_BIT_DEFAULT,(short) 0);
        final FixtureDef objectFixtureDef5 = PhysicsFactory.createFixtureDef(density, elasticity, friction, false, mg.spt.CATEGORY_BIT_DEFAULT, mg.spt.MASK_BIT_DEFAULT,(short) 0);
        */
      	
      	final FixtureDef objectFixtureDef0 = PhysicsFactory.createFixtureDef(density, elasticity, friction, false, mg.spt.CATEGORY_BIT_EARTH, mg.spt.MASK_BIT_EARTH, (short)0);
        final FixtureDef objectFixtureDef1 = PhysicsFactory.createFixtureDef(density, elasticity, friction, false, mg.spt.CATEGORY_BIT_EARTH, mg.spt.MASK_BIT_EARTH, (short)0);
        final FixtureDef objectFixtureDef2 = PhysicsFactory.createFixtureDef(density, elasticity, friction, false, mg.spt.CATEGORY_BIT_EARTH, mg.spt.MASK_BIT_EARTH, (short)0);
        final FixtureDef objectFixtureDef3 = PhysicsFactory.createFixtureDef(density, elasticity, friction, false, mg.spt.CATEGORY_BIT_EARTH, mg.spt.MASK_BIT_EARTH, (short)0);
        final FixtureDef objectFixtureDef4 = PhysicsFactory.createFixtureDef(density, elasticity, friction, false, mg.spt.CATEGORY_BIT_EARTH, mg.spt.MASK_BIT_EARTH, (short)0);
        final FixtureDef objectFixtureDef5 = PhysicsFactory.createFixtureDef(density, elasticity, friction, false, mg.spt.CATEGORY_BIT_EARTH, mg.spt.MASK_BIT_EARTH, (short)0);
        final FixtureDef objectFixtureDef6 = PhysicsFactory.createFixtureDef(density, elasticity, friction, false, mg.spt.CATEGORY_BIT_EARTH, mg.spt.MASK_BIT_EARTH, (short)0);
        final FixtureDef objectFixtureDef7 = PhysicsFactory.createFixtureDef(density, elasticity, friction, false, mg.spt.CATEGORY_BIT_EARTH, mg.spt.MASK_BIT_EARTH, (short)0);

		Rectangle head = new Rectangle(0.0f, 0.0f,  8f,8f, mg.getVertexBufferObjectManager());
		Rectangle chest = new Rectangle(0.0f, 0.0f, 16f, 6f, mg.getVertexBufferObjectManager());
		Rectangle torso = new Rectangle(0.0f, 0.0f, 16f,16f, mg.getVertexBufferObjectManager());
		Rectangle abdomen = new Rectangle(0.0f, 0.0f, 16f, 6f, mg.getVertexBufferObjectManager());
		Rectangle lLeg = new Rectangle(0.0f, 0.0f,  3f,16f, mg.getVertexBufferObjectManager());
		Rectangle rLeg = new Rectangle(0.0f, 0.0f,  3f,16f, mg.getVertexBufferObjectManager());
		Rectangle lArm = new Rectangle(0.0f, 0.0f,  3f,16f, mg.getVertexBufferObjectManager());
		Rectangle rArm = new Rectangle(0.0f, 0.0f,  3f,16f, mg.getVertexBufferObjectManager());
		
		head.setCullingEnabled(true);
		chest.setCullingEnabled(true);
		torso.setCullingEnabled(true);
		abdomen.setCullingEnabled(true);
		lLeg.setCullingEnabled(true);
		rLeg.setCullingEnabled(true);
		lArm.setCullingEnabled(true);
		rArm.setCullingEnabled(true);
		
		
		
		
		final Body bHead = PhysicsFactory.createBoxBody(mg.mPhysicsWorld,head, BodyType.DynamicBody, objectFixtureDef0); 
		final Body bChest = PhysicsFactory.createBoxBody(mg.mPhysicsWorld,chest, BodyType.DynamicBody, objectFixtureDef6); 
		final Body bTorso = PhysicsFactory.createBoxBody(mg.mPhysicsWorld,torso, BodyType.DynamicBody, objectFixtureDef1); 
		final Body bAbdomen = PhysicsFactory.createBoxBody(mg.mPhysicsWorld,abdomen, BodyType.DynamicBody, objectFixtureDef7); 
		final Body bLLeg = PhysicsFactory.createBoxBody(mg.mPhysicsWorld,lLeg, BodyType.DynamicBody, objectFixtureDef2); 
		final Body bRLeg = PhysicsFactory.createBoxBody(mg.mPhysicsWorld,rLeg, BodyType.DynamicBody, objectFixtureDef3); 
		final Body bLArm = PhysicsFactory.createBoxBody(mg.mPhysicsWorld,lArm, BodyType.DynamicBody, objectFixtureDef4); 
		final Body bRArm = PhysicsFactory.createBoxBody(mg.mPhysicsWorld,rArm, BodyType.DynamicBody, objectFixtureDef5); 
		
		
		
		ObjectMeta om0 = new ObjectMeta("head", head, bHead);
		ObjectMeta om6 = new ObjectMeta("chest", chest, bChest);
		ObjectMeta om1 = new ObjectMeta("torso", torso, bTorso);
		ObjectMeta om7 = new ObjectMeta("abdomen", abdomen, bAbdomen);
		ObjectMeta om2 = new ObjectMeta("lArm", lArm, bLArm);
		ObjectMeta om3 = new ObjectMeta("rArm", rArm, bRArm);
		ObjectMeta om4 = new ObjectMeta("lLeg", lLeg, bLLeg);
		ObjectMeta om5 = new ObjectMeta("rLeg", rLeg, bRLeg);
		
		head.setUserData(om0);
		chest.setUserData(om6);
		torso.setUserData(om1);
		abdomen.setUserData(om7);
		lArm.setUserData(om2);
		rArm.setUserData(om3);
		lLeg.setUserData(om4);
		rLeg.setUserData(om5);
		
		
		
		mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(head,bHead,true,true));
		mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(chest,bChest,true,true));
        mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(torso,bTorso,true,true));
        mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(abdomen,bAbdomen,true,true));
        mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(lArm,bLArm,true,true));
        mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rArm,bRArm,true,true));
        mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(lLeg,bLLeg,true,true));
        mg.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rLeg,bRLeg,true,true));
        
        
		
		
		//connect head to chest
		RevoluteJointDef r0 = new RevoluteJointDef();
        r0.initialize(bChest, bHead, bHead.getWorldCenter());
        r0.localAnchorA.set(0.0f,0.20f);
        r0.localAnchorB.set(0.0f,-0.20f);
        r0.enableLimit = true;
		r0.upperAngle = (float)Math.PI/180f *15f;
		r0.lowerAngle = (float)Math.PI/180f *-15f;
		r0.collideConnected = true;
		mg.mPhysicsWorld.createJoint(r0);
        
		//connect left arm to chest
        RevoluteJointDef r1 = new RevoluteJointDef();
        r1.initialize(bChest, bLArm, bChest.getWorldCenter());
        r1.localAnchorA.set(-0.20f,0.20f);
        r1.localAnchorB.set(0.0f,0.20f);
        r1.referenceAngle = (float)Math.PI/180f * -30f;
        r1.enableLimit = true;
        r1.upperAngle = (float)0.0f;
        r1.lowerAngle = (float)-Math.PI;
        r1.collideConnected = true;
        mg.mPhysicsWorld.createJoint(r1);
        
        //connect right arm to chest
        RevoluteJointDef r2 = new RevoluteJointDef();
        r2.initialize(bChest, bRArm, bChest.getWorldCenter());
        r2.localAnchorA.set(0.20f,0.20f);
        r2.localAnchorB.set(0.0f,0.20f);
        r2.referenceAngle = (float)Math.PI/180 * 30f;
        r2.enableLimit = true;
        r2.upperAngle = (float)Math.PI;
        r2.lowerAngle = (float)0.0f;
        r2.collideConnected = true;
        mg.mPhysicsWorld.createJoint(r2);
        
        //connect left leg to abdomen
        RevoluteJointDef r3 = new RevoluteJointDef();
        r3.initialize(bAbdomen, bLLeg, bAbdomen.getWorldCenter());
        r3.localAnchorA.set(-0.20f,-0.20f);
        r3.localAnchorB.set(0.0f,0.20f);
        r3.enableLimit = true;
        r3.lowerAngle = (float)Math.PI/180f * -15f;
        r3.upperAngle = (float)Math.PI/180f * 10f;
        r3.collideConnected = true;
        mg.mPhysicsWorld.createJoint(r3);
        
        //connect right leg to abdomen
        RevoluteJointDef r4 = new RevoluteJointDef();
        r4.initialize(bAbdomen, bRLeg, bAbdomen.getWorldCenter());
        r4.localAnchorA.set(0.20f,-0.20f);
        r4.localAnchorB.set(0.0f,0.20f);
        r4.enableLimit = true;
        r4.lowerAngle = (float)Math.PI/180f * -15f;
        r4.upperAngle = (float)Math.PI/180f * 15;
        r4.collideConnected = true;
        mg.mPhysicsWorld.createJoint(r4);
        
        //connect chest to torso
        RevoluteJointDef r5 = new RevoluteJointDef();
        r5.initialize(bTorso, bChest, bTorso.getWorldCenter());
        r5.localAnchorA.set(0.0f,0.08f);
        r5.localAnchorB.set(0.0f,-0.08f);
        r5.enableLimit = true;
        r5.lowerAngle = (float)Math.PI/180f * -4f;
        r5.upperAngle = (float)Math.PI/180f * 4;
        r5.collideConnected = true;
        mg.mPhysicsWorld.createJoint(r5);
        
        //connect torso to abdomen
        RevoluteJointDef r6 = new RevoluteJointDef();
        r6.initialize(bAbdomen, bTorso, bAbdomen.getWorldCenter());
        r6.localAnchorA.set(0.0f,0.08f);
        r6.localAnchorB.set(0.0f,-0.08f);
        r6.enableLimit = true;
        r6.lowerAngle = (float)Math.PI/180f * -4f;
        r6.upperAngle = (float)Math.PI/180f * 4;
        r6.collideConnected = true;
        mg.mPhysicsWorld.createJoint(r6);
        
        this.attachChild(head);
        this.attachChild(chest);
		this.attachChild(torso);
		this.attachChild(abdomen);
		this.attachChild(lLeg);
		this.attachChild(rLeg);
		this.attachChild(lArm);
		this.attachChild(rArm);
        
        
        
        
       
        
	}
}
