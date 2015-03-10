package cross.xam.lostinthewoods;

/**
 * enum type that specifies all possible objects in game.
 * Associated with GameField class.
 * Determines specific activity of owning Field
 * @author XAM
 *
 */
public enum GameObject {

	SHELLS(0, "Shells"),
	TRAP(1, "Trap");
	
	private int numValue;
	
	private String title;
	
	GameObject(int numValue, String title){
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
