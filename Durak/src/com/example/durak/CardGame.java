package com.example.durak;

public enum CardGame {
	
	DURAK(0), PREFERANS(1), BRIDGE(2);
	
	private int numValue;
	
	CardGame(int numValue){
		this.numValue = numValue;
	}
	
	public int getNumValue(){
		return numValue;
	}
	
}
