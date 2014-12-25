package com.example.durak;

import java.util.ArrayList;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;

public class MyOnTouchListener implements OnTouchListener {

	private ArrayList<Object> dataToSend = new ArrayList<Object>();
	
	public MyOnTouchListener(Card cardPlayed, View cardView) {
		dataToSend.add(cardPlayed);
		dataToSend.add(cardView);
	}
	
	public boolean onTouch(View view, MotionEvent motionEvent) {
		ClipData data = ClipData.newPlainText(" ", " ");
	    DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
	    view.startDrag(data, shadowBuilder, dataToSend, 0);
		return false;
	}
}
