package com.example.durak;


public enum DeckSize {

	THIRTY_SIX(36), FIFTY_TWO(52), THIRTY_TWO(32);
	
	private int numValue;
	
	DeckSize(int numValue){
		this.numValue = numValue;
	}
	
	public int getNumValue(){
		return numValue;
	}
}
