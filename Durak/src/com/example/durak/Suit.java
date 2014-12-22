package com.example.durak;

public enum Suit {
	SPADES(0, "Spades", R.drawable.spades),
	CLUBS(1, "Clubs", R.drawable.clubs),
	DIAMONDS(2, "Diamonds", R.drawable.diamonds),
	HEARTS(3, "Hearts", R.drawable.hearts);
	
	private int resourceID;
	private int numValue;
	private String stringValue;
	
	public static final int size = Suit.values().length;
	
	Suit(int numValue, String stringValue, int resID){
		this.numValue = numValue;
		this.stringValue = stringValue;
		this.resourceID = resID;
	}
	
	public int getNumValue(){
		return numValue;
	}
	
	public String getStringValue(){
		return stringValue;
	}
	
	public String toString(){
		return stringValue;
	}
	
	public int getResourceID(){
		return resourceID;
	}
	
	
}
