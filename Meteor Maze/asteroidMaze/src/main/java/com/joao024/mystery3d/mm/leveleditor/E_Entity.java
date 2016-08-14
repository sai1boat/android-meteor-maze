package com.joao024.mystery3d.mm.leveleditor;

import java.util.UUID;

public class E_Entity {
	public String uniqueId = UUID.randomUUID().toString();
	public float x, y;
	
	public boolean equals(Object o){
		if(o == null) return false;
		if(!(o instanceof E_Entity)) return false;
		E_Entity e = (E_Entity) o;
		if(e.uniqueId.equals(this.uniqueId)) return true;
		else return false;
	}
}
