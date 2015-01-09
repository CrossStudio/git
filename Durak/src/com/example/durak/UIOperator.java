package com.example.durak;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class UIOperator {
	
	LayoutInflater inflater;
	
	private static UIOperator operator;
	
	private static ArrayList<Integer> UIPairsOfCardsIDs = new ArrayList<Integer>();
	
	
	public UIOperator(){
		
	}
	
	public static synchronized UIOperator getInstance(){
		if (operator == null){
			operator = new UIOperator();
		}
		return operator;
	}
	
	/**
	 * Draws player's cards on the screen
	 */
	void UIShowPlayerCards() {
		inflater = GameActivity.getInstance().getLayoutInflater();
		ArrayList<Card> cardsOnHand = GameActivity.getInstance().getPlayers().get(0).cardsOnHand;
		int numOfCardsOnHand = cardsOnHand.size();
		for (int i = 0; i < numOfCardsOnHand; i++){
			View card = inflater.inflate(R.layout.card, GameActivity.getInstance().llCardsOnHand, false);
			ImageView cardValue = (ImageView) card.findViewById(R.id.ivCardValue);
			cardValue.setImageResource(cardsOnHand.get(i).getValueResID());
			
			ImageView cardSuit = (ImageView) card.findViewById(R.id.ivCardSuit);
			cardSuit.setImageResource(cardsOnHand.get(i).getSuit().getResourceID());
			GameActivity.getInstance().llCardsOnHand.addView(card);
			card.setOnTouchListener(new MyOnTouchListener(cardsOnHand.get(i), card));
		}
	}
	
	/**
	 * Draws new attacking card on the table passed as a parameter
	 * @param attackCard - card to draw on the table
	 */
	void UIDrawNewAttackCard(Card attackCard){
		RelativeLayout pairOfCards = (RelativeLayout) inflater.inflate(R.layout.pair_of_cards, GameActivity.getInstance().llTable, false);
		GameActivity.getInstance().llTable.addView(pairOfCards);
		
		UIPairsOfCardsIDs.add(pairOfCards.getId());
		View viewAttackCard = inflater.inflate(R.layout.card, pairOfCards, false);
		ImageView cardValue = (ImageView) viewAttackCard.findViewById(R.id.ivCardValue);
		cardValue.setImageResource(attackCard.getValueResID());
		
		ImageView cardSuit = (ImageView) viewAttackCard.findViewById(R.id.ivCardSuit);
		cardSuit.setImageResource(attackCard.getSuit().getResourceID());
		pairOfCards.addView(viewAttackCard);
	}
	
	void UIDrawNewDefendCard(Card attackCard, Card defendCard){
		
	}
}
