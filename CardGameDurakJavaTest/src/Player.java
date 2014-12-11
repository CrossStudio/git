

import java.util.ArrayList;
import java.util.Collections;

public class Player {

	/**
	 * This player's name
	 */
	private String name;
	
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
	 * Cards that the player used for defense this turn
	 */
	public ArrayList<Card> currentCardsDefendedWith = new ArrayList<Card>();
	
	/**
	 * 
	 * @return this player's name
	 */
	public String getName(){
		return name;
	}
	
	public Player(String name){
		this.name = name;
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
	}
	
	/**
	 * Attack on this player's turn with a chosen card
	 * @param attackCard - card to attack with
	 */
	public void attackWith(Card attackCard){
		Table.addAttackCard(attackCard);
		currentCardsAttackedWith.add(attackCard);
		removeCardFromHand(attackCard);
		System.out.println(name + " attacks with: " + attackCard);
		System.out.println(name + "'s hand: " + getCardsOnHand());
		System.out.println("Unbeaten cards: " + Table.getUnbeatenCards());
	}

	/**
	 * Remove the card from player's hand
	 * @param cardToRemove - card to be removed from this player's hand
	 */
	private void removeCardFromHand(Card cardToRemove) {
		cardsOnHand.remove(cardToRemove);
	}

	/**
	 * Defend one attacking card with chosen card from this player's hand
	 * @param defenceCard - card from this player's hand to defend with
	 * @param attackCard - card on a table that this player chooses to defend with this move
	 */
	public void defendWith(Card defenceCard, Card attackCard){
		Table.addDefenceCard(defenceCard, attackCard);
		currentCardsDefendedWith.add(defenceCard);
		removeCardFromHand(defenceCard);
		System.out.println(name + " defends: " + attackCard + " with: " + defenceCard);
		System.out.println(name + "'s hand: " + getCardsOnHand());
	}
	
	/**
	 * @return cards that are currently on this player's hand
	 */
	public ArrayList<Card> getCardsOnHand(){
		return cardsOnHand;
	}
	
	/**
	 * @return cards on this player's hand that he can attack with
	 */
	public ArrayList<Card> getCardsToAttack(){
		ArrayList<Card> cardsOnTable = Table.getAllCardsOnTable();
		ArrayList<Card> dummyList = new ArrayList<Card>();
		for (Card cardInHand : cardsOnHand){
			for (Card cardOnTable : cardsOnTable){
				if (cardInHand.getValue() == cardOnTable.getValue()){
					dummyList.add(cardInHand);
				}
			}
		}
		return dummyList;
	}
	
	public class ArtificialIntelligence{
		
		private String name = getName();
		
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
		public boolean randomSecondaryAttack(){
			int randomAttackCardNum;
			Card attackCard;
			ArrayList<Card> dummyList = getCardsToAttack();
			if (!dummyList.isEmpty()){
				randomAttackCardNum = (int)(Math.random() * dummyList.size());
				attackCard = dummyList.get(randomAttackCardNum);
				attackWith(attackCard);
				return true;
			}
			else {
				System.out.println(name + " has no cards to attack with");
				return false;
			}
		}
		
		/**
		 * Defend against the first card that this player can beat
		 * @return true if some card was defended, false if this player cannot beat some card
		 * on the table or if he has beaten all the cards
		 */
		public boolean defend(){
			for (Card attackCard : Table.getUnbeatenCards()){
				for (Card cardInHand : cardsOnHand){
					if (cardInHand.beats(attackCard, Game.getTrump().getSuit())){
						defendWith(cardInHand, attackCard);
						return true;
					}
				}
			}
			System.out.println(name + " cannot beat all cards on the table");
			return false;
		}

	}
	
	public void flushTheTable() {
		cardsOnHand.addAll(Table.getAllCardsOnTable());
		sortCardsOnHands();
		System.out.println(name + " cards: " + cardsOnHand);
	}
	
	public String toString(){
		return getName();
	}

}
