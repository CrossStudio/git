package com.example.durak;

import java.util.ArrayList;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;

public class MyOnTouchListener implements OnTouchListener {

	private ArrayList<Object> dataToSend = new ArrayList<Object>();
	
	Card cardPlayed;
	View cardView;
	
	public MyOnTouchListener(Card cardPlayed, View cardView) {
		this.cardPlayed = cardPlayed;
		this.cardView = cardView;
		dataToSend.add(cardPlayed);
		dataToSend.add(cardView);
	}
	
	public boolean onTouch(View view, MotionEvent motionEvent) {
		//If this is a move as an attacking player
		if (!GameActivity.getInstance().getHumanPlayer().isDefender()){
			System.out.println("Human player attacks");
			//If this move is the last chance to attack the defender who is overwhelmed
			if (GameActivity.getDefendingPlayer().isOverwhelmed()){
				if (GameActivity.getInstance().humanPlayerAttack(cardPlayed)){
					UIOperator.getInstance().UIDrawNewAttackCard(cardPlayed);
				}
			}
			//If this is a general attacking move
			else {
				if (GameActivity.getInstance().humanPlayerAttack(cardPlayed)){
					UIOperator.getInstance().UIDrawNewAttackCard(cardPlayed);
					GameActivity.getInstance().letDefenderMove();
				}
			}
			GameActivity.getInstance().btnEndMove.setEnabled(true);
		}
		//If this is a move as a defending player
		else {
			System.out.println("Human player defends");
			if (GameActivity.getInstance().humanPlayerDefend(cardPlayed, Table.getUnbeatenCards().get(0))){
				if (GameActivity.getInstance().getHumanPlayer().getCardsOnHand().isEmpty()){
					GameActivity.getInstance().endTurn();
					return true;
				}
				GameActivity.currentPlayerIndex = -1;
				GameActivity.getInstance().letNextAttackerMove();
			}
			else {
				System.out.println("You cannot defend " + Table.getUnbeatenCards().get(0) + " with "+ cardPlayed);
			}
		}
		/*ClipData data = ClipData.newPlainText(" ", " ");
	    DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
	    view.startDrag(data, shadowBuilder, dataToSend, 0);*/
		return false;
	}
}