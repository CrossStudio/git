package com.example.durak;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class GameActivity extends Activity {

	static final int FIRST_ATTACKING_PLAYER_INDEX = 0;
	
	static final int DEFENDING_PLAYER_INDEX = 1;

	static {
		players = new ArrayList<Player>();
		newDeck = new Deck(DeckSize.THIRTY_SIX);
	}
	
	private static GameActivity activity;
	
	public GameActivity(){
		activity = this;
	}
	
	public static synchronized GameActivity getInstance(){
		if (activity == null){
			activity = new GameActivity();
		}
		return activity;
	}
	
	
	LinearLayout llCardsOnHand;
	GridLayout glTable;
	Button btnPCDefenceMove;
	Button btnPCAttackMove;
	Button btnEndMove;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnPCDefenceMove = (Button) findViewById(R.id.btnPCDefenceMove);
		btnPCDefenceMove.setOnClickListener(new DefendClickListener());
		
		btnPCAttackMove = (Button) findViewById(R.id.btnPCAttackMove);
		btnPCAttackMove.setOnClickListener(new AttackClickListener());
		
		btnEndMove = (Button) findViewById(R.id.btnEndMove);
		btnEndMove.setOnClickListener(new EndMoveClickListener());
		
		llCardsOnHand = (LinearLayout) findViewById(R.id.llCardsOnHand);
		glTable = (GridLayout) findViewById(R.id.glTable);
		glTable.setOnDragListener(new MyDragListener());
		
		startNewGame();
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
	 * List of players in order of actions for current turn (defender is always the last)
	 */
	private static ArrayList<Player> orderedPlayers;
	
	/**
	 * Current active player
	 */
	private static Player currentPlayer;
	
	/**
	 * Curent player index
	 */
	static int currentPlayerIndex;
	
	/**
	 * Ordered list of players attacking this turn
	 */
	private static ArrayList<Player> attackingPlayers;
	
	/**
	 * Current attacking player's index
	 */
	private static int attackingPlayerIndex;
	
	/**
	 * Has anyone attacked after a card was defended this turn 
	 */
	private boolean didAnyoneAttack = false;
	
	/**
	 * Player defending this turn
	 */
	private static Player defendingPlayer;
	
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
	
	/**
	 * Start a new game of Durak
	 */
	public void startNewGame (){
		attackingPlayerIndex = 0;
		
		humanPlayer = new Player("XAM CROSS");
		Player petia = new Player("Petia");
		Player kolia = new Player("Kolia");
		Player gena = new Player("Gena");

		players.add(humanPlayer);
		humanPlayer.setHuman();
		players.add(petia);
		players.add(kolia);
		players.add(gena);
		
		initiateGame();
		
	}	
	
	/**
	 * 
	 * @return current Player (whose move it is)
	 */
	Player getCurrentPlayer(){
		return players.get(currentPlayerIndex);
	}
	
	void setCurrentPlayer(int indexOfCurrentPlayer){
		currentPlayerIndex = indexOfCurrentPlayer;
	}
	
	/**
	 * Prepare the setting of a new game (give six cards to each player, determine move order)
	 */
	private void initiateGame() {
		for (Player player : players){
			player.drawCards(newDeck);
		}
		setTrumpCard();
		setInitialMoveOrder();
		defendingPlayer = players.get(DEFENDING_PLAYER_INDEX);
		defendingPlayer.noAttackThisMove();
		showPlayersHand(humanPlayer);
		if (!players.get(0).amIHuman()){
			UIOperator.getInstance().UIDisablePlayerMove();
		}
		else {
			System.out.println("Its human's turn");
		}
		currentPlayerIndex = FIRST_ATTACKING_PLAYER_INDEX;
		currentPlayer = players.get(currentPlayerIndex);
		playersMove();
	}
	
	/**
	 * Show player's hand on the screen
	 */
	private void showPlayersHand(Player currentPlayer) {
		UIOperator.getInstance().UIShowPlayerCards(currentPlayer);
	}
	
	void playersMove(){
		//If defender is already overwhelmed (cannot beat all the attacking cards on the table)
		if (defendingPlayer.isOverwhelmed()){
			for (Player player : players) {
				currentPlayer = player;
				//If current active player is a defender
				if (currentPlayerIndex == DEFENDING_PLAYER_INDEX){
					continue;
				}
				//If current active player is an attacker
				else {
					currentPlayer.lastAttackChance();
				}
			}
		}
		//If defender is not yet overwhelmed
		else {
			//If current active player is a defender
			if (currentPlayerIndex == DEFENDING_PLAYER_INDEX){
				currentPlayer.defensiveAction();
			}
			//If current active player is an attacker
			else {
				currentPlayer.attackingAction();
			}
		}
		
	}
	
	/**
	 * Make PC players attack with one card (if one of them has a card to attack with and is willing to do so)
	 * starting with player who is the first attacker
	 */
	void PCMakesAttackMove(){
		Player curAttPlayer = getCurrentPlayer();
		didAnyoneAttack = curAttPlayer.randomAttack();
		/*
		 * After some attacker adds another card to the table everyone's ability to attack is rejuvenated
		 */
		if (didAnyoneAttack){
			UIOperator.getInstance().UIDrawNewAttackCard(Table.getUnbeatenCards().get(Table.getUnbeatenCards().size()-1));
			for (int i = 0; i < attackingPlayers.size(); i++){
				attackingPlayers.get(i).mayAttackThisMove();
			}
			setCurrentPlayer(DEFENDING_PLAYER_INDEX);
		}
		else {
			curAttPlayer.noAttackThisMove();
		}
		playersMove();
	}

	/**
	 * Make PC player defend one card on the table (if he has a card that he can defend with or is willing to do so)
	 * In case he cannot or is not willing to defend the remaining cards on the table he flushes the table
	 */
	void PCMakesDefenceMove(){
		Card defendCard = defendingPlayer.PCGetCardToDefend();
		//Check if there are any unbeaten (not defended) attacking cards on the table
		if (Table.getUnbeatenCards().size() > 0){
			//Check if defender is overwhelmed (cannot beat some of the attacking cards on the table)
			if (!defendingPlayer.isOverwhelmed()){
				Card firstAttackCard = Table.getUnbeatenCards().get(0);
				UIOperator.getInstance().UIDrawNewDefendCard(firstAttackCard, defendCard);
				defendingPlayer.defendWith(defendCard, firstAttackCard);
			}
			else {
				System.out.println(defendingPlayer + " is overwhelmed");
			}
			currentPlayerIndex = FIRST_ATTACKING_PLAYER_INDEX;
		}
		//There are no unbeaten cards on the table, turn ends with a discard and defending player may now attack
		else {
			endTurn();
		}
		playersMove();
	}
	
	/**
	 * End turn with a discard, if defender is successful, or with a flush, if otherwise
	 */
	void endTurn() {
		if (Table.getUnbeatenCards().size() == 0){
			finishTurnWithDiscard();
		}
		else {
			finishTurnWithFlush();
		}
		UIOperator.getInstance().UIClearTable();
		UIOperator.getInstance().UIRefreshPlayerCards(humanPlayer);
		if (getCurrentPlayer().amIHuman()){
			UIOperator.getInstance().UIEnablePlayerMove(getCurrentPlayer());
		}
		else {
			UIOperator.getInstance().UIDisablePlayerMove();
		}
	}

	/**
	 * End current turn, have each attacking player draw cards from deck until they have 6 on hand
	 * Change attackers and defender
	 */
	private static void finishTurnWithFlush() {
		defendingPlayer.flushTheTable();
		for (Player attackingPlayer : attackingPlayers){
			attackingPlayer.drawCards(newDeck);
		}
		checkPlayersInPlay();
		if (attackingPlayers.size() > 1){
			System.out.println("First attacker will be = " + attackingPlayers.get(1));
			rearrangePlayers(attackingPlayers.get(1));
			System.out.println("First attacker is = " + attackingPlayers.get(0));
		}
		else {
			rearrangePlayers(attackingPlayers.get(0));
		}
	}

	/**
	 * End current turn, have each player to draw cards from deck until they have 6 on hand
	 * Change attackers and defender
	 */
	private static void finishTurnWithDiscard() {
		Table.discard();
		for (Player attackingPlayer : attackingPlayers){
			attackingPlayer.drawCards(newDeck);
		}
		defendingPlayer.drawCards(newDeck);
		checkPlayersInPlay();
		System.out.println("First attacker will be = " + defendingPlayer);
		rearrangePlayers(defendingPlayer);
		System.out.println("First attacker will be = " + attackingPlayers.get(0));
	}
	
	
	/**
	 * Check players still in play and remove ones with no cards left
	 * The game is over if number of players still with cards on hand is 1 or 0
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
		if (gameOver()){
			System.out.println("GAME OVER!!111111");
		}
	}

	/**
	 * 
	 * @return ordered list of attacking players for this turn
	 */
	public static ArrayList<Player> getAttackingPlayers() {
		attackingPlayers = new ArrayList<Player>();
		attackingPlayers.addAll(orderedPlayers.subList(0, orderedPlayers.size()-1));
		System.out.println("Attacking players: " + attackingPlayers);
		return attackingPlayers;
	}

	/**
	 * Returns players participating in game
	 * @return ArrayList of players
	 */
	public ArrayList<Player> getPlayers(){
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
	private static void setInitialMoveOrder(){
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
		orderedPlayers = new ArrayList<Player>();
		orderedPlayers.addAll(players.subList(players.indexOf(firstPlayer), players.size()));
		
		for (Player player : players){
			if (player.equals(firstPlayer)){
				break;
			}
			else {
				orderedPlayers.add(player);
			}
		}
		
		defendingPlayer = orderedPlayers.get(1);
		orderedPlayers.remove(1);
		orderedPlayers.add(defendingPlayer);
		
		attackingPlayers = getAttackingPlayers();
		
		System.out.println("Order of movement: " + orderedPlayers);
		System.out.println("Defending player is: " + defendingPlayer);
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
	 * @return true if the attack was successful, false otherwise
	 */
	public boolean humanPlayerAttack(Card attackCard){
		ArrayList<Card> cardsAvailable = humanPlayer.getCardsToAttack();
		if (cardsAvailable.contains(attackCard)){
			humanPlayer.attackWith(attackCard);
			UIOperator.getInstance().UIRefreshPlayerCards(humanPlayer);
			return true;
		}
		else {
			System.out.println("Cannot attack with this card: " + attackCard);
			return false;
		}
	}
	
	public boolean humanPlayerDefend(Card defendCard, Card attackCard){
		if (!defendingPlayer.isOverwhelmed()){
			if (defendCard.beats(attackCard, getTrump().getSuit())){
				defendingPlayer.defendWith(defendCard, attackCard);
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}
	
}
