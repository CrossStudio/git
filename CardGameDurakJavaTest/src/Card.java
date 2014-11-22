

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
	
	public boolean beats(Card cardToBeat, Suit trump){
		if (this.getSuit().getNumValue() == cardToBeat.getSuit().getNumValue()){
			if (this.getValue().getNumValue() > cardToBeat.getValue().getNumValue()){
				return true;
			}
			else {
				return false;
			}
		}
		else if (this.getSuit().equals(trump)){
			return true;
		}
		else {
			return false;
		}
	}

	
}