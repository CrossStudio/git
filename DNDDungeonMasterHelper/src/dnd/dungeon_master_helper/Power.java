package dnd.dungeon_master_helper;

/**
 * Represents character's special moves/spells/attacks 
 * @author XAM
 *
 */
public class Power {
	
	private String title;
	private String type;
	private int maxAmount;
	private int currentAmount;
	
	public Power (String title, String type, int maxAmount){
		this.setTitle(title);
		this.setType(type);
		this.setMaxAmount(maxAmount);
		this.setCurrentAmount(maxAmount);
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the maxAmount
	 */
	public int getMaxAmount() {
		return maxAmount;
	}

	/**
	 * @param maxAmount the maxAmount to set
	 */
	public void setMaxAmount(int maxAmount) {
		this.maxAmount = maxAmount;
	}

	/**
	 * @return the currentAmount
	 */
	public int getCurrentAmount() {
		return currentAmount;
	}

	/**
	 * @param currentAmount the currentAmount to set
	 */
	public void setCurrentAmount(int currentAmount) {
		this.currentAmount = currentAmount;
	}
}
