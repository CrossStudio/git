package com.example.durak;

public enum CardValue {
	SIX(6, "6"),
	SEVEN(7, "7"),
	EIGHT(8, "8"),
	NINE(9, "9"),
	TEN(10, "10"),
	JACK(11, "Jack"),
	QUEEN(12, "Queen"),
	KING(13, "King"),
	ACE(14, "Ace");
	
	private int numVal;
	private String stringVal;
	
	CardValue(int numVal, String stringVal){
		this.numVal = numVal;
		this.stringVal = stringVal;
	}
	
	public int getNumValue(){
		return numVal;
	}
	
	public String getStringValue(){
		return stringVal;
	}
	
	public String toString(){
		return stringVal;
	}

}
