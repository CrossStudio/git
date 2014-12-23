package com.example.durak;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;

public class MyOnTouchListener implements OnTouchListener {

	Card cardPlayed;
	
	public MyOnTouchListener(Card cardPlayed) {
		this.cardPlayed = cardPlayed;
	}
	
	public boolean onTouch(View view, MotionEvent motionEvent) {
		ClipData data = ClipData.newPlainText(" ", " ");
	    DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
	    view.startDrag(data, shadowBuilder, cardPlayed, 0);
		return false;
	}
}
