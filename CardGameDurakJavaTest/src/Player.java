

import java.util.ArrayList;

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
}
