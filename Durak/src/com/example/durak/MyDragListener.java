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
			//If this is a move as an attacking player
			if (!GameActivity.getInstance().getHumanPlayer().isDefender()){
				System.out.println("Human player attacks");
				//If this move is the last chance to attack the defender who is overwhelmed
				if (GameActivity.getDefendingPlayer().isOverwhelmed()){
					if (GameActivity.getInstance().humanPlayerAttack(draggedCard)){
						UIOperator.getInstance().UIDrawNewAttackCard(draggedCard);
					}
				}
				//If this is a general attacking move
				else {
					if (GameActivity.getInstance().humanPlayerAttack(draggedCard)){
						UIOperator.getInstance().UIDrawNewAttackCard(draggedCard);
						GameActivity.getInstance().letDefenderMove();
					}
				}
			}
			//If this is a move as a defending player
			else {
				System.out.println("Human player defends");
				if (GameActivity.getInstance().humanPlayerDefend(draggedCard, Table.getUnbeatenCards().get(0))){
					if (GameActivity.getInstance().getHumanPlayer().getCardsOnHand().isEmpty()){
						GameActivity.getInstance().endTurn();
						return true;
					}
					GameActivity.currentPlayerIndex = -1;
					GameActivity.getInstance().letNextAttackerMove();
				}
				else {
					System.out.println("You cannot defend " + Table.getUnbeatenCards().get(0) + " with "+ draggedCard);
				}
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
