

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Player {

	private PlayerNumber playerNumber;
	
	public ArrayList<Card> cardsOnHand = new ArrayList<Card>();
	
	public PlayerNumber getPlayerNumber(){
		return playerNumber;
	}
	
	public void addCardToHand(Card newCard){
		cardsOnHand.add(newCard);
	}
	
	public Card playCard(Card playedCard){
		return playedCard;
	}
	
	public void sortCardsOnHands() {
		Collections.sort(this.cardsOnHand, new CardComparator());		
	}
}
