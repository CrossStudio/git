import java.util.ArrayList;


public abstract class Table {
	
	/**
	 * Cards currently in-play on the table
	 */
	private static ArrayList<Card> cards = new ArrayList<Card>();
	
	/**
	 * Cards currently in discard
	 */
	private static ArrayList<Card> discardPile = new ArrayList<Card>();
	
	/**
	 * Place new card onto table
	 * @param card - card to be put onto table
	 */
	public void addCard(Card card){
		cards.add(card);
	}
	
	/**
	 * Gets cards currently on the table
	 * @return cards currently on the table
	 */
	public static ArrayList<Card> getCardsOnTable(){
		return cards;
	}
	
	/**
	 * Move all cards currently in-play to discard
	 * and clear the table
	 */
	public void discard(){
		moveCardsToDiscardPile();
		clear();
	}
	
	/**
	 * Add cards to discard
	 */
	private void moveCardsToDiscardPile() {
		discardPile.addAll(cards);
	}

	/**
	 * Clear the table
	 */
	private void clear(){
		cards.clear();
	}
	
	

}
