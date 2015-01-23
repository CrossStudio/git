package com.example.durak;

import java.util.ArrayList;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

public class MyOnClickListener implements OnClickListener {

	private ArrayList<Object> dataToSend = new ArrayList<Object>();
	
	Card cardPlayed;
	CardView cardView;
	UIOperator operator = UIOperator.getInstance();
	
	public MyOnClickListener(Card cardPlayed, CardView cardView) {
		this.cardPlayed = cardPlayed;
		this.cardView = cardView;
		dataToSend.add(cardPlayed);
		dataToSend.add(cardView);
	}
	
	public void onClick(View view) {
		//If current display of players cards shows all the cards at once or cards of a certain suit
		if (UIOperator.isViewExpanded() || UIOperator.isViewForOneSuit()){
			//If this is a move as an attacking player
			if (!GameActivity.getInstance().getHumanPlayer().isDefender()){
				System.out.println("Human player attacks");
				//If this move is the last chance to attack the defender who is overwhelmed
				if (GameActivity.getDefendingPlayer().isOverwhelmed()){
					if (GameActivity.getInstance().humanPlayerAttack(cardPlayed)){
						UIOperator.getInstance().UIDrawNewAttackCard(cardPlayed);
					}
					else{
						operator.UIShowPlayerCards(GameActivity.getInstance().getHumanPlayer());
					}
				}
				//If this is a general attacking move
				else {
					if (GameActivity.getInstance().humanPlayerAttack(cardPlayed)){
						UIOperator.getInstance().UIDrawNewAttackCard(cardPlayed);
						GameActivity.getInstance().letDefenderMove();
					}
					else{
						operator.UIShowPlayerCards(GameActivity.getInstance().getHumanPlayer());
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
						return;
					}
					
					GameActivity.currentPlayerIndex = -1;
					GameActivity.getInstance().letNextAttackerMove();
				}
				else {
					System.out.println("You cannot defend " + Table.getUnbeatenCards().get(0) + " with "+ cardPlayed);
					operator.UIShowPlayerCards(GameActivity.getInstance().getHumanPlayer());
				}
			}
			UIOperator.setOneSuitView(false);
		}
		//If current display of player's cards shows only Suits to choose 
		else {
			System.out.println(cardView);
			UIOperator.setOneSuitView(true);
			operator.expandSuit(cardView.getSuit());
		}

	}
}