package com.example.durak;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class UIOperator {
	
	LayoutInflater inflater;
	
	private static GameActivity activity = GameActivity.getInstance();
	
	private static UIOperator operator;

	@SuppressLint("UseSparseArrays")
	private static ArrayList<Card> UIPairsOfCards = new ArrayList<Card>();
	
	public UIOperator(){
		inflater = activity.getLayoutInflater();
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
	void UIShowPlayerCards(Player currentPlayer) {
		
		ArrayList<Card> cardsOnHand = currentPlayer.cardsOnHand;
		int numOfCardsOnHand = cardsOnHand.size();
		if (numOfCardsOnHand >= 12){
			UIShowPlayerGroupOfCards(currentPlayer);
		}
		else {
			for (int i = 0; i < numOfCardsOnHand; i++){
				View card = inflater.inflate(R.layout.card, activity.llCardsOnHand, false);
				ImageView cardValue = (ImageView) card.findViewById(R.id.ivCardValue);
				cardValue.setImageResource(cardsOnHand.get(i).getValueResID());
				
				ImageView cardSuit = (ImageView) card.findViewById(R.id.ivCardSuit);
				cardSuit.setImageResource(cardsOnHand.get(i).getSuit().getResourceID());
				
				LinearLayout.LayoutParams cardParams = (LinearLayout.LayoutParams) card.getLayoutParams();
				if (i != 0){
					cardParams.leftMargin = -45;
				}				
				activity.llCardsOnHand.addView(card);
				card.setOnTouchListener(new MyOnTouchListener(cardsOnHand.get(i), card));
			}
		}
	}
	
	/**
	 * Draws group of player's cards (grouped by suits) if their number is more than 11
	 * @param currentPlayer - player, whose cards will be drawn
	 */
	private void UIShowPlayerGroupOfCards(Player currentPlayer) {
		ArrayList<Card> cardsOnHand = currentPlayer.cardsOnHand;
		int numOfCardsOnHand = cardsOnHand.size();
		Suit suitAlreadyUsed = null;
		for (int i = 0; i < numOfCardsOnHand; i++){
			
			View card = inflater.inflate(R.layout.card, activity.llCardsOnHand, false);
			
			if (suitAlreadyUsed != cardsOnHand.get(i).getSuit()){
				ImageView cardValue = (ImageView) card.findViewById(R.id.ivCardValue);
				cardValue.setVisibility(ImageView.INVISIBLE);
				
				ImageView cardSuit = (ImageView) card.findViewById(R.id.ivCardSuit);
				cardSuit.setImageResource(cardsOnHand.get(i).getSuit().getResourceID());
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cardSuit.getLayoutParams();
				params.addRule(RelativeLayout.CENTER_IN_PARENT);
				params.removeRule(RelativeLayout.RIGHT_OF);
				cardSuit.setLayoutParams(params);
				suitAlreadyUsed = cardsOnHand.get(i).getSuit();
				
				activity.llCardsOnHand.addView(card);
			}
		}
		
	}

	/**
	 * Redraws player's hand
	 * @param currentPlayer - player whose hand should be redrawn
	 */
	void UIRefreshPlayerCards(Player currentPlayer){
		UIClearPlayerHand();
		UIShowPlayerCards(currentPlayer);
	}
	
	/**
	 * Removes all the cards from player's hand
	 */
	private void UIClearPlayerHand() {
		activity.llCardsOnHand.removeAllViews();
	}

	/**
	 * Enable player's hand for movement
	 */
	void UIEnablePlayerMove(Player currentPlayer){
		UIRefreshPlayerCards(currentPlayer);
	}
	
	/**
	 * Disable player's hand
	 */
	void UIDisablePlayerMove(){
		System.out.println("Disabling player's hand");
		for (int i = 0; i < activity.llCardsOnHand.getChildCount(); i++){
			activity.llCardsOnHand.getChildAt(i).setOnTouchListener(null);
		}
	}
	
	/**
	 * Draws new attacking card on the table passed as a parameter
	 * @param attackCard - card to draw on the table
	 */
	void UIDrawNewAttackCard(Card attackCard){
		RelativeLayout pairOfCards = (RelativeLayout) inflater.inflate(R.layout.pair_of_cards, activity.glTable, false);
		
		System.out.println("Attacking card: " + attackCard + ". Its id = " + pairOfCards.getId());
		
		UIPairsOfCards.add(attackCard);
		
		activity.glTable.addView(pairOfCards);
		
		View viewAttackCard = inflater.inflate(R.layout.card, pairOfCards, false);
		ImageView cardValue = (ImageView) viewAttackCard.findViewById(R.id.ivCardValue);
		cardValue.setImageResource(attackCard.getValueResID());
		
		ImageView cardSuit = (ImageView) viewAttackCard.findViewById(R.id.ivCardSuit);
		cardSuit.setImageResource(attackCard.getSuit().getResourceID());
		pairOfCards.addView(viewAttackCard);
		viewAttackCard.setOnDragListener(new MyDragListener());
	}
	
	/**
	 * Draws new defending card on the table passed as a parameter on top of the attacking card passed as a parameter
	 * @param attackCard - to be defended
	 * @param defendCard - to be placed on top of the appropriate attacking card
	 */
	void UIDrawNewDefendCard(Card attackCard, Card defendCard){
		if (UIPairsOfCards.indexOf(attackCard) != -1){
			RelativeLayout pairOfCards = (RelativeLayout) activity.glTable.getChildAt(UIPairsOfCards.indexOf(attackCard));
			View viewDefendCard = inflater.inflate(R.layout.card, pairOfCards, false);
			
			ImageView cardValue = (ImageView) viewDefendCard.findViewById(R.id.ivCardValue);
			cardValue.setImageResource(defendCard.getValueResID());
			
			ImageView cardSuit = (ImageView) viewDefendCard.findViewById(R.id.ivCardSuit);
			cardSuit.setImageResource(defendCard.getSuit().getResourceID());
			
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewDefendCard.getLayoutParams();
			
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			
			pairOfCards.addView(viewDefendCard);
		}
	}
	
	/**
	 * Remove all card views from the table
	 */
	void UIClearTable(){
		activity.glTable.removeAllViews();
		UIPairsOfCards.clear();
	}
	
	void UIDisableDefendButton(){
		Button btnDefendButton = (Button) activity.findViewById(R.id.btnPCDefenceMove);
		btnDefendButton.setEnabled(false);
	}
	
	void UIEnableDefendButton(){
		Button btnDefendButton = (Button) activity.findViewById(R.id.btnPCDefenceMove);
		btnDefendButton.setEnabled(true);
	}
}
