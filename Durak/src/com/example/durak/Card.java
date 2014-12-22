package com.example.durak;


public class Card{

	private Suit suit;
	
	private CardValue value;
	
	private int valueResID;
	
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
	public void isBeaten(boolean beaten){
		this.beaten = beaten;
	}
	
	public Card (Suit suit, CardValue value){
		this.suit = suit;
		this.value = value;
		if (this.suit == Suit.DIAMONDS || this.suit == Suit.HEARTS){
			switch (value){
				case SIX: 
					this.valueResID = R.drawable.six_red;
					break;
				case SEVEN: 
					this.valueResID = R.drawable.seven_red;
					break;
				case EIGHT:
					this.valueResID = R.drawable.eight_red;
					break;
				case NINE:
					this.valueResID = R.drawable.nine_red;
					break;
				case TEN:
					this.valueResID = R.drawable.ten_red;
					break;
				case JACK:
					this.valueResID = R.drawable.jack_red;
					break;
				case QUEEN:
					this.valueResID = R.drawable.queen_red;
					break;
				case KING:
					this.valueResID = R.drawable.king_red;
					break;
				case ACE:
					this.valueResID = R.drawable.ace_red;
					break;
			}
		}
		else {
			switch (value){
			case SIX: 
				this.valueResID = R.drawable.six_black;
				break;
			case SEVEN: 
				this.valueResID = R.drawable.seven_black;
				break;
			case EIGHT:
				this.valueResID = R.drawable.eight_black;
				break;
			case NINE:
				this.valueResID = R.drawable.nine_black;
				break;
			case TEN:
				this.valueResID = R.drawable.ten_black;
				break;
			case JACK:
				this.valueResID = R.drawable.jack_black;
				break;
			case QUEEN:
				this.valueResID = R.drawable.queen_black;
				break;
			case KING:
				this.valueResID = R.drawable.king_black;
				break;
			case ACE:
				this.valueResID = R.drawable.ace_black;
				break;
		}
		}
	}
	
	/**
	 * 
	 * @return id of card value resource image
	 */
	public int getValueResID(){
		return valueResID;
	}
	
	public String toString(){
		return value + " of " + suit;
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
		return false;		
	}

}