

public class Card{

	private Suit suit;
	
	private CardValue value;
	
	private boolean beaten = false;
	
	public Suit getSuit(){
		return suit;		
	}
	
	public CardValue getValue(){
		return value;
	}
	
	public boolean getBeaten(){
		return beaten;
	}
	
	/**
	 * This card gets beaten
	 */
	public void isBeaten(){
		this.beaten = true;
	}
	
	public Card (Suit suit, CardValue value){
		this.suit = suit;
		this.value = value;
	}
	
	public String toString(){
		return suit + " " + value;
	}
	
	/**
	 * Check whether this cards beats the other card
	 * @param cardToBeat - card to be beaten (or not)
	 * @param trump - current trump suit
	 * @return - true if this card beats the other card, false if otherwise
	 */
	public boolean beats(Card cardToBeat, Suit trump){
		if (this.getSuit().equals(cardToBeat.getSuit())){
			if (this.getValue().getNumValue() > cardToBeat.getValue().getNumValue()){
				return true;	
			}
		}
		else if (this.getSuit().equals(trump)){
			return true;
		}
		System.out.println(this + " cannot beat " + cardToBeat);
		return false;		
	}

}