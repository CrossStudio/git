package com.example.durak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class CardView extends RelativeLayout {
	
	public CardView(Context context, Card card) {
		super(context);
		this.card = card;
		
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.card,this);
	}
	
	public CardView(Context context, Suit suit){
		super(context);
		this.suit = suit;
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.blank_card, this);
	}
	
	private Suit suit;
	
	public Suit getSuit(){
		return suit;
	}
	
	private View view;
	
	public View getView(){
		return view;
	}
	
	private Card card;

	public Card getCard(){
		return card;
	}
}
