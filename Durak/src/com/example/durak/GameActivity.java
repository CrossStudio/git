package com.example.durak;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity {

	static {
		players = new ArrayList<Player>();
		newDeck = new Deck(DeckSize.THIRTY_SIX);
	}
	
	LayoutInflater inflater;
	LinearLayout llCardsOnHand;
	LinearLayout llTable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		inflater = getLayoutInflater();
		llCardsOnHand = (LinearLayout) findViewById(R.id.llCardsOnHand);
		llTable = (LinearLayout) findViewById(R.id.llTable);
		llTable.setOnDragListener(new MyDragListener());
		
		main();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Human controlled player
	 */
	private Player humanPlayer;
	
	/**
	 * @return human controlled player
	 */
	public Player getHumanPlayer(){
		return humanPlayer;
	}
	
	/**
	 * Players participating in current game
	 */
	private static ArrayList<Player> players;
	
	/**
	 * Ordered list of players attacking this turn
	 */
	private static ArrayList<Player> attackingPlayers;
	
	/**
	 * Player defending this turn
	 */
	private static Player defendingPlayer;
	
	/**
	 * Turns completed in this game
	 */
	//private ArrayList<Turn> turns = new ArrayList<Turn>();
	
	/**
	 * Deck used in Durak game (36 cards)
	 */
	private static Deck newDeck;
	
	/**
	 * Current trump card
	 */
	private static Card trumpCard;
	
	/**
	 * 
	 * @return current trump card
	 */
	public static Card getTrumpCard(){
		return trumpCard;
	}

	/**
	 * Sets trump card
	 */
	public static void setTrumpCard(){
		trumpCard = newDeck.draw();
		System.out.println("Trump: " + trumpCard);
		// Put card at the beginning of the deck 
		newDeck.getCards().add(0, trumpCard);
	}
	
	public void main (){

		humanPlayer = new Player("XAM CROSS");
		Player petia = new Player("Petia");
		Player kolia = new Player("Kolia");
		Player gena = new Player("Gena");

		players.add(petia);
		players.add(kolia);
		players.add(gena);
		
		playGame();
		
	}	
	
	/**
	 * Start a new game 
	 */
	private void playGame() {
		for (Player player : players){
			player.drawCards(newDeck);
		}
		setTrumpCard();
		setMoveOrder();
		UIShowPlayerCards();
		
		//Play until the game is over
		while (!gameOver()){
			
			playersMoves();
		}

	}
	
	/**
	 * Draws player's cards on the screen
	 */
	private void UIShowPlayerCards() {
		ArrayList<Card> cardsOnHand = players.get(0).cardsOnHand;
		int numOfCardsOnHand = cardsOnHand.size();
		for (int i = 0; i < numOfCardsOnHand; i++){
			View card = inflater.inflate(R.layout.card, llCardsOnHand, false);
			ImageView cardValue = (ImageView) card.findViewById(R.id.ivCardValue);
			cardValue.setImageResource(cardsOnHand.get(i).getValueResID());
			
			ImageView cardSuit = (ImageView) card.findViewById(R.id.ivCardSuit);
			cardSuit.setImageResource(cardsOnHand.get(i).getSuit().getResourceID());
			llCardsOnHand.addView(card);
			card.setOnTouchListener(new MyOnTouchListener(cardsOnHand.get(i), card));
		}
	}

	/**
	 * AI players make their moves on this turn
	 */
	private static void playersMoves() {
		attackingPlayers = getAttackingPlayers();
		defendingPlayer = players.get(1);
		Player activeAttackingPlayer;
		for (int i = 0; i < attackingPlayers.size(); i++){
			activeAttackingPlayer = attackingPlayers.get(i);
			while(!activeAttackingPlayer.getCardsToAttack().isEmpty()){
				activeAttackingPlayer.randomAttack();
				Card defendCard = defendingPlayer.getCardToDefend();
				if (!defendingPlayer.isOverwhelmed()){
					defendingPlayer.defendWith(defendCard, Table.getUnbeatenCards().get(0));
					i = -1;
				}
			}
		}
		if (defendingPlayer.isOverwhelmed()){
			defendingPlayer.flushTheTable();
			if (players.size() >= 3){
				finishTurn(attackingPlayers.get(1));
			}
			else {
				finishTurn(attackingPlayers.get(0));
			}
		}
		else{
			finishTurn(defendingPlayer);
		}
	}
	
	/**
	 * End current turn, have each player to draw cards from deck until they have 6 on hand
	 * Change attacking and defending players
	 * @param firstPlayer - player that will be first attacking player next turn
	 */
	private static void finishTurn(Player firstPlayer) {
		Table.discard();
		for (Player attackingPlayer : players){
			attackingPlayer.drawCards(newDeck);
		}
		defendingPlayer.drawCards(newDeck);
		checkPlayersInPlay();
		rearrangePlayers(firstPlayer);
	}
	
	
	/**
	 * Check players still in play and remove ones with no cards left
	 */
	private static void checkPlayersInPlay() {
		ArrayList<Player> playersToRemove = new ArrayList<Player>();
		for (Player playerInPlay : players){
			if (!playerInPlay.isPlaying()){
				playersToRemove.add(playerInPlay);
			}
		}
		for (Player player : playersToRemove){
			players.remove(player);
		}
		
	}

	/**
	 * 
	 * @return ordered list of attacking players for this turn
	 */
	private static ArrayList<Player> getAttackingPlayers() {
		ArrayList<Player> attackingPlayers = new ArrayList<Player>();
		attackingPlayers.add(players.get(0));
		if (players.size() > 2){
			attackingPlayers.add(players.get(2));
		}
		System.out.println("Attacking players: " + attackingPlayers);
		return attackingPlayers;
	}

	/**
	 * Returns players participating in game
	 * @return ArrayList of players
	 */
	public static ArrayList<Player> getPlayers(){
		return players;
	}
	
	/**
	 * 
	 * @return trump card for this game
	 */
	public static Card getTrump(){
		return trumpCard;
	}
	
	/**
	 * Arranges move order of players based on the smallest trump card they have
	 * on their hands
	 */
	private static void setMoveOrder(){
		Card smallestTrump = trumpCard;
		Player firstPlayer = null;
		for (Player player : players){
			for (Card card : player.cardsOnHand){
				if (card.getSuit().equals(trumpCard.getSuit())){
					if (smallestTrump.equals(trumpCard)){
						smallestTrump = card;
						firstPlayer = player;
					}
					else if (card.getValue().getNumValue() < smallestTrump.getValue().getNumValue()){
						smallestTrump = card;
						firstPlayer = player;
					}
				}
			}
		}
		if (smallestTrump.equals(trumpCard)){
			firstPlayer = players.get((int)(Math.random()*players.size()));
		}
		rearrangePlayers(firstPlayer);
	}

	/**
	 * Arrange players' move order starting from the indicated player
	 * @param firstPlayer - player to move first
	 */
	private static void rearrangePlayers(Player firstPlayer) {
		ArrayList<Player> dummyPlayersList = new ArrayList<Player>();
		if (players.indexOf(firstPlayer) >= 0){
			dummyPlayersList.addAll(players.subList(players.indexOf(firstPlayer), players.size()));
			
			for (Player player : players){
				if (player.equals(firstPlayer)){
					break;
				}
				else {
					dummyPlayersList.add(player);
				}
			}
			players = dummyPlayersList;
		}
		System.out.println(players);
	}
	
	/**
	 * Check if the game is over
	 * @return - true if the game is over, false - otherwise
	 */
	private static boolean gameOver(){
		int playersWithCards = players.size();
		for (Player player : players){
			if (player.cardsOnHand.size() == 0){
				playersWithCards--;
			}
		}
		if (playersWithCards < 2){
			return true;
		}
		return false;
	}
	
	/**
	 * Human controlled player attacks with chosen card
	 * @param attackCard - card that human controlled player has chosen to attack
	 */
	public void humanPlayerAttack(Card attackCard){
		humanPlayer.attackWith(attackCard);	
	}
	
	/**
	 * UI method that adds one chosen card to the table (if the conditions are met)
	 * @param draggedCard - card to be put onto table
	 */
	public static void putCardOntoTable(View view, Card draggedCard, View draggedView) {
		
		LinearLayout llTable = (LinearLayout) view;
		GameActivity activity = (GameActivity)llTable.getContext();
		LinearLayout llCardsOnHand = (LinearLayout) activity.findViewById(R.id.llCardsOnHand);
		
		LayoutInflater inflater = activity.getLayoutInflater();
		View card = inflater.inflate(R.layout.card, llTable, false);
		
		ImageView cardValue = (ImageView) card.findViewById(R.id.ivCardValue);
		System.out.println(cardValue.getClass());
		int cardValueId = draggedCard.getValueResID();
		cardValue.setImageResource(cardValueId);
		
		ImageView cardSuit = (ImageView) card.findViewById(R.id.ivCardSuit);
		cardSuit.setImageResource(draggedCard.getSuit().getResourceID());
		llTable.addView(card);
		llCardsOnHand.removeView(draggedView);
		
		activity.humanPlayerAttack(draggedCard);
	}
	
}
