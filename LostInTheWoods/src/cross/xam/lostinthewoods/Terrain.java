package cross.xam.lostinthewoods;

/**
 * enum type that specifies all possible terrain types in game.
 * Assosciated with GameField class.
 * Determines movesCost of Characters that stand on a field with such terrain and ability of the owning Field to hold game objects
 * @author XAM
 *
 */
public enum Terrain {

	MEADOW(0, "Meadow"),
	THICKET(1, "Thicket"),
	LAKE(2, "Lake"),
	RANGERS_HUT(3, "Ranger's hut");
	
	private int numValue;
	
	private String title;
	
	Terrain (int numValue, String title){
		this.numValue = numValue;
		this.title = title;
	}
	
	public int getNumValue(){
		return this.numValue;
	}
	
	public String toString(){
		return this.title;
	}
	
}
