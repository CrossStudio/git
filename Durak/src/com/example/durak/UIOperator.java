package com.example.durak;

import java.util.ArrayList;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class UIOperator {
	
	static UIOperator operator;
	
	private static GameActivity uiActivity = GameActivity.activity;
	
	private static ArrayList<Integer> UIPairsOfCardsIDs = new ArrayList<Integer>();
	
	UIOperator(){
		operator = this;
	}
	
	/**
	 * Draws player's cards on the screen
	 */
	void UIShowPlayerCards() {
		ArrayList<Card> cardsOnHand = uiActivity.getPlayers().get(0).cardsOnHand;
		int numOfCardsOnHand = cardsOnHand.size();
		for (int i = 0; i < numOfCardsOnHand; i++){
			View card = uiActivity.inflater.inflate(R.layout.card, uiActivity.llCardsOnHand, false);
			ImageView cardValue = (ImageView) card.findViewById(R.id.ivCardValue);
			cardValue.setImageResource(cardsOnHand.get(i).getValueResID());
			
			ImageView cardSuit = (ImageView) card.findViewById(R.id.ivCardSuit);
			cardSuit.setImageResource(cardsOnHand.get(i).getSuit().getResourceID());
			uiActivity.llCardsOnHand.addView(card);
			card.setOnTouchListener(new MyOnTouchListener(cardsOnHand.get(i), card));
		}
	}
	
	/**
	 * Draws new attacking card on the table passed as a parameter
	 * @param attackCard - card to draw on the table
	 */
	void UIDrawNewAttackCard(Card attackCard){
		RelativeLayout pairOfCards = (RelativeLayout) uiActivity.inflater.inflate(R.layout.pair_of_cards, uiActivity.llTable, false);
		uiActivity.llTable.addView(pairOfCards);
		
		UIPairsOfCardsIDs.add(pairOfCards.getId());
		View viewAttackCard = uiActivity.inflater.inflate(R.layout.card, pairOfCards, false);
		ImageView cardValue = (ImageView) viewAttackCard.findViewById(R.id.ivCardValue);
		cardValue.setImageResource(attackCard.getValueResID());
		
		ImageView cardSuit = (ImageView) viewAttackCard.findViewById(R.id.ivCardSuit);
		cardSuit.setImageResource(attackCard.getSuit().getResourceID());
		pairOfCards.addView(viewAttackCard);
	}
	
	void UIDrawNewDefendCard(Card attackCard, Card defendCard){
		
	}
}
