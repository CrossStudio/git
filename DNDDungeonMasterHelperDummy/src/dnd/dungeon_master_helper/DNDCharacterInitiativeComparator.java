package dnd.dungeon_master_helper;

import java.util.Comparator;

public class DNDCharacterInitiativeComparator implements
		Comparator<DNDCharacter> {

	@Override
	public int compare(DNDCharacter lhs, DNDCharacter rhs) {
		
		return rhs.getCharInitiativeEncounter() - lhs.getCharInitiativeEncounter();
	}

}
