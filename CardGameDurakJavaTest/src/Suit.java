

public enum Suit {
	SPADES(0), CLUBS(1), DIAMONDS(2), HEARTS(3);
	
	private int numValue;
	
	public static final int size = Suit.values().length;
	
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
