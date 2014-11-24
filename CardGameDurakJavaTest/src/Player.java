

import java.util.ArrayList;
import java.util.Collections;

public class Player {

	/**
	 * Player's number (order in move queue)
	 */
	private PlayerNumber playerNumber;
	
	/**
	 * Cards currently in player's hand
	 */
	public ArrayList<Card> cardsOnHand = new ArrayList<Card>();
	
	private boolean myTurn;
	
	private boolean iAmDefending;
	
	/**
	 * Get player's number (order in move queue)
	 * @return - player's number (order in move queue)
	 */
	public PlayerNumber getPlayerNumber(){
		return playerNumber;
	}
	
	/**
	 * Checks whether it is this player's turn
	 * @return
	 */
	public boolean isMyTurn(){
		return myTurn;
	}
	
	/**
	 * Check if this player is defending this turn
	 * @return
	 */
	public boolean amIDefending(){
		return iAmDefending;
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
}
