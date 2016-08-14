package com.joao024.mystery3d.mm.leveleditor;

public class S_Warp extends E_Entity{
	
	
	
	
	
	public float rotation = 0f;
	public String name="WARP-"+this.uniqueId.substring(1,7);
	public String connectsTo = name; //warps to itself for default to prevent null pointer
	public S_Warp connectsToWarp = this;

}
