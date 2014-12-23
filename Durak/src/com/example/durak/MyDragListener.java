package com.example.durak;

import android.app.Activity;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MyDragListener implements OnDragListener {

	@Override
	public boolean onDrag(View view, DragEvent event) {
		
		int action = event.getAction();
		
		Card draggedCard = (Card)event.getLocalState();
		
		switch (action){
		case DragEvent.ACTION_DRAG_STARTED:
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			break;
		case DragEvent.ACTION_DROP:
			putCardOntoTable(view, draggedCard);
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			break;
		default:
			break;
		}
		
		return true;
		
		
	}
	/**
	 * UI method that adds one chosen card to the table (if the conditions are met)
	 * @param draggedCard - card to be put onto table
	 */
	private void putCardOntoTable(View view, Card draggedCard) {
		LinearLayout llTable = (LinearLayout) view;
		
		Activity activity = (Activity)llTable.getContext();
		LayoutInflater inflater = activity.getLayoutInflater();
		View card = inflater.inflate(R.layout.card, llTable, false);
		
		ImageView cardValue = (ImageView) card.findViewById(R.id.ivCardValue);
		System.out.println(cardValue.getClass());
		int cardValueId = draggedCard.getValueResID();
		cardValue.setImageResource(cardValueId);
		
		ImageView cardSuit = (ImageView) card.findViewById(R.id.ivCardSuit);
		cardSuit.setImageResource(draggedCard.getSuit().getResourceID());
		llTable.addView(card);
		
	}
	
}
