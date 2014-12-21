import java.util.Collections;
import java.util.Stack;


public class Deck{
	
	/** Collection of cards */
	Stack<Card> cards = new Stack<Card>();
	
	public Deck(DeckSize deckSize){	
		// Fill deck with all possible cards 
		for(Suit suit : Suit.values()){
			for (CardValue value : CardValue.values()){	
				cards.add(new Card(suit, value));				
			}
		}
		
		this.shuffle();
	}
	
	public Stack<Card> getCards()
	{
		return this.cards;
	}
	
	/**
	 * Shuffle cards in a deck
	 */
	public void shuffle()
	{
		Collections.shuffle(this.cards);
	}
	
	/**
	 * @return Return current number of cards in a deck
	 */
	public int getCardsLeft()
	{
		return cards.size();
	}
	
	/**
	 * Get top card from a deck 
	 * @return Top card 
	 */
	public Card draw()
	{
		return this.cards.pop();
	}	
}
