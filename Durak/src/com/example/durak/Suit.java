package com.example.durak;

public enum Suit {
	SPADES(0, "Spades"), CLUBS(1, "Clubs"), DIAMONDS(2, "Diamonds"), HEARTS(3, "Hearts");
	
	private int numValue;
	private String stringValue;
	
	public static final int size = Suit.values().length;
	
	Suit(int numValue, String stringValue){
		this.numValue = numValue;
		this.stringValue = stringValue;
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
	
	
}
