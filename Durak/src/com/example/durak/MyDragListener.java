package com.example.durak;

import java.util.ArrayList;

import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.GridLayout;
import android.widget.ImageView;


public class MyDragListener implements OnDragListener {

	@Override
	public boolean onDrag(View view, DragEvent event) {
		
		if (view.getClass() == GridLayout.class){
			System.out.println("You dragged onto GridLayout");
		}
		else if (view.getClass() == ImageView.class){
			System.out.println("You dragged onto ImageView");
		}
		else {
			System.out.println("You dragged onto " + view.getClass());
		}
		
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
			if (GameActivity.getInstance().getHumanPlayer().getAbilityToAttackThisMove()){
				if (GameActivity.getInstance().humanPlayerAttack(draggedCard)){
					UIOperator.getInstance().UIDrawNewAttackCard(draggedCard);
					GameActivity.getInstance().setCurrentPlayer(GameActivity.DEFENDING_PLAYER_INDEX);
					GameActivity.getInstance().playersMove();
				}
			}
			else {
				//view.getParent().
				GameActivity.getInstance().humanPlayerDefend(draggedCard, Table.getUnbeatenCards().get(0));
				GameActivity.getInstance().setCurrentPlayer(GameActivity.FIRST_ATTACKING_PLAYER_INDEX);
			}
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
