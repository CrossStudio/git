package com.example.durak;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ExpandOnTouchListener implements OnTouchListener {

	CardView card;
	
	public ExpandOnTouchListener(CardView card){
		this.card = card;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		System.out.println("Expanding card group");
		UIOperator.expandSuit(card.getSuit());
		return false;
	}


}
