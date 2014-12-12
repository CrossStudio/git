import java.util.ArrayList;


public abstract class Table {
	
	/**
	 * Attacking cards currently on the table
	 */
	private static ArrayList<Card> attackCards = new ArrayList<Card>();
	
	/**
	 * Defending cards currently on the table
	 */
	private static ArrayList<Card> defenceCards = new ArrayList<Card>();
	
	/**
	 * Cards currently in discard
	 */
	private static ArrayList<Card> discardPile = new ArrayList<Card>();
	
	/**
	 * Place new attacking card onto table
	 * @param card - card to be put onto table
	 */
	public static void addAttackCard(Card card){
		attackCards.add(card);
	}
	
	/**
	 * Place new defending card onto table, beat the appropriate attacking card
	 * @param card card to be put onto table
	 */
	public static void addDefenceCard(Card defenceCard, Card attackCard){
		defenceCards.add(defenceCard);
		attackCards.get(attackCards.indexOf(attackCard)).isBeaten(true);
	}
	
	/**
	 * Move all cards currently in-play to discard
	 * and clear the table
	 */
	public static void discard(){
		moveCardsToDiscardPile();
		clear();
	}
	
	/**
	 * Add cards to discard
	 */
	private static void moveCardsToDiscardPile() {
		discardPile.addAll(attackCards);
		discardPile.addAll(defenceCards);
	}

	/**
	 * Clear the table
	 */
	private static void clear(){
		attackCards.clear();
		defenceCards.clear();
	}
	
	/**
	 * Returns all cards that are currently on table
	 * @return ArrayList of cards on table
	 */
	public static ArrayList<Card> getAllCardsOnTable(){
		ArrayList<Card> allCards = new ArrayList<Card>();
		allCards.addAll(attackCards);
		allCards.addAll(defenceCards);
		return allCards;
	}
	
	/**
	 * Returns all unbeaten cards that are currently on table
	 * @return ArrayList of unbeaten attacking cards on table
	 */
	public static ArrayList<Card> getUnbeatenCards(){
		ArrayList<Card> dummyAttackCards = new ArrayList<Card>();
		for (Card card : attackCards){
			if (!card.getBeaten()){
				dummyAttackCards.add(card);
			}
		}
		return dummyAttackCards;
	}

}
