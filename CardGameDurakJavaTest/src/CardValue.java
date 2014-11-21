

public enum CardValue {
	SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13), ACE(14);
	
	private int numVal;
	
	CardValue(int numVal){
		this.numVal = numVal;
	}
	
	public int getNumVal(){
		return numVal;
	}
	
	public String toString(){
		return "" + numVal;
	}
}
