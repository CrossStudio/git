package dnd.dungeon_master_helper;

import java.io.Serializable;

/**
 * Represents character's special moves/spells/attacks 
 * @author XAM
 *
 */
public class Power implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 896822509048731250L;
	
	private String title;
	private PowerType type;
	private int maxAmount;
	private int currentAmount;
	
	
	
	public Power (String title, PowerType type, int maxAmount){
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
	public PowerType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(PowerType type) {
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
	
	public String toString(){
		return title;
	}
}
