
public class Deck{
	
	private int totalNumberOfCardsInDeck;
	
	Card[] cardsInDeck;
	
	private int currentNumOfCards;
	
	public Deck(DeckSize deckSize){
		totalNumberOfCardsInDeck = deckSize.getNumValue();
		currentNumOfCards = totalNumberOfCardsInDeck;
		cardsInDeck = new Card[totalNumberOfCardsInDeck];
	}
	
	/**
	 * Shuffles cards in deck instance
	 */
	public void shuffleCardsInDeck(){
		
		int counter = totalNumberOfCardsInDeck / 4;
		while (counter > 0){
			for(Suit suit : Suit.values()){
				for (CardValue value : CardValue.values()){			
					Card newCard = new Card(suit, value);
					int randomIndex = getRandomIndex();
					cardsInDeck[randomIndex] = newCard;
					counter--;
				}
			}
		}
	}
	
	/**
	 * 
	 * @return random integer between 0 and totalNumberOfCardsInDeck - 1
	 * for this instance
	 */
	private int getRandomIndex(){
		int randomIndex = (int)(Math.random()*totalNumberOfCardsInDeck);
		if (cardsInDeck[randomIndex] == null){
			return randomIndex;
		}
		return getRandomIndex();
	}
	
	public Card drawCard(){
		int lastCardIndex = currentNumOfCards-1;
		currentNumOfCards--;
		return cardsInDeck[lastCardIndex];
	}
	
}
