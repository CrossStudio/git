

public class Card{

	private Suit suit;
	
	private CardValue value;
	
	public Suit getSuit(){
		return suit;
		
	}
	
	public CardValue getValue(){
		return value;
	}
	
	public Card (Suit suit, CardValue value){
		this.suit = suit;
		this.value = value;
	}
	
	public String toString(){
		return "This card is " + suit + " " + value;
	}
	
}