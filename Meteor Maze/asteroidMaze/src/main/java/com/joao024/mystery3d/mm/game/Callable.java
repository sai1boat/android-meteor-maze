package com.joao024.mystery3d.mm.game;


/*
 * This is an interface used by the refresh oort cloud function in LevelUtils.
 * We just want a way to call back into the code after communications with
 * the cloud have finished and an action needs to be taken.
 */
public interface Callable {
	public void call();
}
