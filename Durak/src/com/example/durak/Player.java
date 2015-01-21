package com.example.durak;
import java.util.ArrayList;
import java.util.Collections;

public class Player {

	/**
	 * This player's name
	 */
	private String name;

	/**
	 * If player is a human player
	 */
	private boolean isHuman = false;
	
	/**
	 * If this player is a defending player
	 */
	private boolean defender = false;
	
	/**
	 * If player was unable to beat some card on the table on his turn
	 */
	public boolean overwhelmed = false;
	
	/**
	 * If player has an ability and willingness to attack with his next move
	 */
	private boolean mayAttackThisMove = true;
	
	/**
	 * If player has already finished playing (has no cards left)
	 */
	private boolean playing = true;
	
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

	/**
	 * 
	 * @return true if this player is human, false otherwise
	 */
	public boolean amIHuman(){
		return isHuman;
	}
	
	/**
	 * This player becomes human player
	 */
	public void setHuman(){
		isHuman = true;
	}
	
	public boolean isDefender(){
		return defender;
	}
	
	public void setDefender(boolean isDef){
		defender = isDef;
	}
	
	/**
	 * 
	 * @return true if player could not beat all the cards he was attacked with on this turn
	 */
	public boolean isOverwhelmed(){
		return overwhelmed;
	}
	
	public void notOverwhelmed(){
		overwhelmed = false;
	}
	
	/**
	 * 
	 * @return true if player is willing and able to attack with his next move
	 */
	public boolean getAbilityToAttackThisMove(){
		return mayAttackThisMove;
	}
	
	/**
	 * Player is not willing or able to attack with his move
	 */
	public void noAttackThisMove(){
		mayAttackThisMove = false;
	}
	
	/**
	 * Player is now able to attack with his next move
	 */
	public void mayAttackThisMove(){
		mayAttackThisMove = true;
	}
	
	/**
	 * 
	 * @return true if player is still playing (has cards on hand), false - finished playing
	 */
	public boolean isPlaying(){
		return playing;
	}
	
	/**
	 * Sets this player's status to "finished playing" (playing == false)
	 */
	public void finishedPlaying(){
		playing = false;
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
		UIOperator.getInstance().UIDrawNewDefendCard(attackCard, defenceCard);
		System.out.println(name + " defends: " + attackCard + " with: " + defenceCard);
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
		if (cardsOnTable.isEmpty()){
			return cardsOnHand;
		}
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

	/**
	 * Randomizer of attack (in case it is not this player's move and he is not defending)
	 */
	public boolean randomAttack(){
		int randomAttackCardNum;
		Card attackCard;
		ArrayList<Card> dummyList = getCardsToAttack();
		if (!dummyList.isEmpty()){
			randomAttackCardNum = (int)(Math.random() * dummyList.size());
			attackCard = dummyList.get(randomAttackCardNum);
			attackWith(attackCard);
			//Draw the current attacking card
			UIOperator.getInstance().UIDrawNewAttackCard(Table.getUnbeatenCards().get(Table.getUnbeatenCards().size()-1));
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
	public boolean PCDefend(){
		UIOperator.getInstance().UIDisablePlayerMove();
		
		Card defendCard = getCardToDefend();
		Card attackCard = Table.getUnbeatenCards().get(0);
		if (defendCard != null){
			defendWith(defendCard, attackCard);
			return true;
		}
		else {
			System.out.println("Defender is overwhelmed");
			return false;
		}
	}

	/**
	 * Player takes all the cards currently on the table to his hand
	 */
	public void flushTheTable() {
		cardsOnHand.addAll(Table.getAllCardsOnTable());
		Table.clear();
		for (Card cardOnHand : cardsOnHand){
			cardOnHand.isBeaten(false);
		}
		sortCardsOnHands();
		System.out.println(name + " cards: " + cardsOnHand);
	}

	public String toString(){
		return getName();
	}

	/**
	 * 
	 * @return card which will be used to defend against the attacking card currently on the table
	 */
	public Card getCardToDefend() {
		if (!Table.getUnbeatenCards().isEmpty()){
			Card attackCard = Table.getUnbeatenCards().get(0);
			for (Card cardOnHand : cardsOnHand){
				if (cardOnHand.beats(attackCard, GameActivity.getTrump().getSuit())){
					return cardOnHand;
				}
			}
		}
		else {
			return null;
		}
		overwhelmed = true;
		return null;
	}
	
	/**
	 * Give each player 6 cards and assign a trump suit (if it is the first turn)
	 */
	public void drawCards(Deck newDeck) {
		System.out.println(name + " draws cards");
			for (int i = 0; this.getCardsOnHand().size() < 6; ++i){
				if (newDeck.getCardsLeft() != 0){
					this.addCardToHand(newDeck.draw());
					continue;
				}
				else if (this.getCardsOnHand().size() == 0){
					this.finishedPlaying();
					return;
				}
				break;
			}
			this.sortCardsOnHands();
			System.out.println(this.getName() + ": " + this.getCardsOnHand());

		System.out.println("Cards left in deck = " + newDeck.getCardsLeft());
	}
	
	public void attackingAction() {
		//If this player is PC controlled
		if (!amIHuman()){
			System.out.println("It's PC's turn");
			UIOperator.getInstance().UIDisablePlayerMove();
			//If this PC player has successfully attacked
			if (randomAttack()){
				GameActivity.getInstance().letDefenderMove();
				GameActivity.getInstance().btnEndMove.setEnabled(true);
			}
			else {
				//If this player is NOT the last attacking player
				if (GameActivity.getInstance().currentPlayerIndex + 1 < GameActivity.getAttackingPlayers().size()){
					GameActivity.getInstance().letNextAttackerMove();
				}
				//If this player is the last attacking player
				else {
					GameActivity.getInstance().endTurn();
				}
			}
		}
		//If this player is human controlled
		else {
			System.out.println("It's human's turn");
			GameActivity.getInstance().letHumanMove();
		}
		
	}

	public void defensiveAction() {
		if (this.getCardToDefend() != null){
			//If this player is PC controlled
			if (!amIHuman()){
				System.out.println("It's PC's turn");
				PCDefend();
				if (getCardsOnHand().isEmpty()){
					GameActivity.getInstance().endTurn();
					return;
				}
				GameActivity.currentPlayerIndex = -1;
				GameActivity.getInstance().letNextAttackerMove();
			}
			//If this player is human controlled
			else {
				System.out.println("It's human's turn");
				UIOperator.getInstance().UIEnablePlayerMove(this);
			}
		}
		else {
			System.out.println("Defender is overwhelmed");
			GameActivity.currentPlayerIndex = -1;
			GameActivity.getInstance().letNextAttackerMove();
		}
	}
	
	public void lastAttackChance() {
		System.out.println("Entering lastAttackChance method");
		UIOperator.getInstance().UIDisablePlayerMove();
		randomAttack();
		GameActivity.getInstance().letNextAttackerMove();
			
	}

}
