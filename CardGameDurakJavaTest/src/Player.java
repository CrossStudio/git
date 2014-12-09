

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
	 * Cards currently available for attack
	 */
	public ArrayList<Card> cardsAvailableForAttack = new ArrayList<Card>();
	
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
		currentCardsAttackedWith.add(attackCard);
		removeCardFromHand(attackCard);
	}

	private void removeCardFromHand(Card cardToRemove) {
		cardsOnHand.remove(cardToRemove);
	}

	public void defendWith(Card defenceCard, Card attackCard){
		Table.addDefenceCard(defenceCard, attackCard);
		currentCardsDefendedWith.add(defenceCard);
		removeCardFromHand(defenceCard);
	}
	
	public void setCardsAvailableForAttack(){
		
	}
	
	public class ArtificialIntelligence{
		
		/**
		 * Randomizer of attack (in case it is this player's move)
		 */
		public void randomFirstAttack(){
			int randomAttackCardNum = (int)(Math.random() * cardsOnHand.size());
			Card attackCard = cardsOnHand.get(randomAttackCardNum);
			attackWith(attackCard);
		}
		/**
		 * Randomizer of attack (in case it is not this player's move and he is not defending)
		 */
		public void randomSecondaryAttack(){
			
			attackWith(attackCard);
		}
	}

}
