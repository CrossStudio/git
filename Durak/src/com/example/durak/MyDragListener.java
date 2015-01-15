package com.example.durak;

import java.util.ArrayList;

import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;


public class MyDragListener implements OnDragListener {

	@Override
	public boolean onDrag(View view, DragEvent event) {
		
		int action = event.getAction();
		
		ArrayList<Object> dataReceived = (ArrayList<Object>)event.getLocalState();
		
		Card draggedCard = (Card) dataReceived.get(0);
		View draggedView = (View) dataReceived.get(1);
		
		switch (action){
		case DragEvent.ACTION_DRAG_STARTED:
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			break;
		case DragEvent.ACTION_DROP:
			GameActivity.getInstance().humanPlayerAttack(draggedCard);
			UIOperator.getInstance().UIDrawNewAttackCard(draggedCard);
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

	
}
