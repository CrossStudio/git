

import java.util.ArrayList;
import java.util.Collections;

public class Player {

	/**
	 * Player's number (order in move queue)
	 */
	private int playerNumber;
	
	/**
	 * Cards currently in player's hand
	 */
	public ArrayList<Card> cardsOnHand = new ArrayList<Card>();
	
	/**
	 * Cards that the player used for attack this turn
	 */
	public ArrayList<Card> currentCardsAttackedWith = new ArrayList<Card>();
	
	
	/**
	 * Cards that the player used for defence this turn
	 */
	public ArrayList<Card> currentCardsDefendedWith = new ArrayList<Card>();
	
	/**
	 * Get player's number (order in move queue)
	 * @return - player's number (order in move queue)
	 */
	public int getPlayerNumber(){
		return playerNumber;
	}
	
	/**
	 * Add a card to player's hand
	 * @param newCard - card to player's hand
	 */
	public void addCardToHand(Card newCard){
		cardsOnHand.add(newCard);
	}
	
	/**
	 * Sorts cards in each players' hands by suit then by value
	 */
	public void sortCardsOnHands() {
		Collections.sort(this.cardsOnHand, new CardComparator());	
		System.out.println(this + ": " + cardsOnHand);
	}
	
	/**
	 * Attack on this player's turn with a chosen card
	 * @param attackCard - card to attack with
	 */
	public void attackWith(Card attackCard){
		Table.addAttackCard(attackCard);
	}


}
