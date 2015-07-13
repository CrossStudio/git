package dnd.dungeon_master_helper;

import java.io.Serializable;

public enum PowerType implements Serializable{
	ATWILL, ENCOUNTER, DAILY;

	public String toString(){
		return this.name();
	}
	
}
