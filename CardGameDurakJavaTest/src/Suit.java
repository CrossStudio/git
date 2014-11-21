

public enum Suit {
	SPADES(0), CLUBS(1), DIAMONDS(2), HEARTS(3);
	
	private int numValue;
	
	Suit(int numValue){
		this.numValue = numValue;
	}
	
	public int getNumValue(){
		return numValue;
	}
	
	public String toString(){
		return "" + numValue;
	}
	
	
}
