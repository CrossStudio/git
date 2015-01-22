package com.example.durak;

import java.util.ArrayList;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

public class CollapseOnTouchListener implements OnTouchListener {

	private ArrayList<Object> dataToSend = new ArrayList<Object>();
	
	public CollapseOnTouchListener(Card cardPlayed, View cardView) {
		dataToSend.add(cardPlayed);
		dataToSend.add(cardView);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		System.out.println("Collapsing a group view");
		System.out.println("Card touched: " + dataToSend.get(0));
		GameActivity.getInstance().llGroupOfCards.setVisibility(LinearLayout.INVISIBLE);
		ClipData data = ClipData.newPlainText(" ", " ");
	    DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
	    view.startDrag(data, shadowBuilder, dataToSend, 0);
		return false;
	}

}
