package com.example.durak;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;


public class GameActivity extends Activity {

	static final int FIRST_ATTACKING_PLAYER_INDEX = 0;
	
	static int defendingPlayerIndex = 1;

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
	Button btnEndMove;
	LinearLayout llGroupOfCards;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnEndMove = (Button) findViewById(R.id.btnEndMove);
		btnEndMove.setOnClickListener(new EndMoveClickListener());
		
		llCardsOnHand = (LinearLayout) findViewById(R.id.llCardsOnHand);
		llGroupOfCards = (LinearLayout) findViewById(R.id.llGroupOfCards);
		glTable = (GridLayout) findViewById(R.id.glTable);

		
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
	 * Player defending this turn
	 */
	private static Player defendingPlayer;
	
	public static Player getDefendingPlayer(){
		return defendingPlayer;
	}
	
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
		return orderedPlayers.get(currentPlayerIndex);
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
		defendingPlayer = orderedPlayers.get(orderedPlayers.size()-1);
		defendingPlayer.noAttackThisMove();
		defendingPlayer.setDefender(true);
		showPlayersHand(humanPlayer);
		btnEndMove.setEnabled(false);
		if (!orderedPlayers.get(0).amIHuman()){
			UIOperator.getInstance().UIDisablePlayerMove();
		}
		else {
			System.out.println("Its human's turn");
		}
		currentPlayerIndex = FIRST_ATTACKING_PLAYER_INDEX;
		currentPlayer = orderedPlayers.get(currentPlayerIndex);
		playersMove();
	}
	
	/**
	 * Show player's hand on the screen
	 */
	private void showPlayersHand(Player currentPlayer) {
		UIOperator.getInstance().UIShowPlayerCards(currentPlayer);
	}
	
	void playersMove(){
		System.out.println("Entering playersMove method");
		//If defender is already overwhelmed (cannot beat all the attacking cards on the table)
		if (defendingPlayer.isOverwhelmed()){
			System.out.println("Defender is overwhelmed");
			if (!currentPlayer.amIHuman()){
				System.out.println("It's PC's turn");
				currentPlayer.lastAttackChance();
			}
			else {
				System.out.println("It's human's turn");
				System.out.println("currentPlayer = " + currentPlayer);
				UIOperator.getInstance().UIEnablePlayerMove(currentPlayer);
			}

		}
		//If defender is not yet overwhelmed
		else {
			System.out.println("Defender is NOT overwhelmed");
			//If current active player is a defender
			if (currentPlayer == defendingPlayer){
				currentPlayer.defensiveAction();
			}
			//If current active player is an attacker
			else {
				currentPlayer.attackingAction();
			}
		}
		
	}
	
	public void letDefenderMove() {
		System.out.println("Entering letDefenderMove method");
		if (orderedPlayers.size() > 1){
			currentPlayerIndex = orderedPlayers.size()-1;
			System.out.println("currentPlayerIndex = " + currentPlayerIndex);
			currentPlayer = orderedPlayers.get(currentPlayerIndex);
			playersMove();
		}
		else {
			System.out.println("There is only one player left in the game");
		}
	}
	

	public void letNextAttackerMove(){
		System.out.println("Entering letNextAttackerMove method");
		if (currentPlayerIndex + 1 < attackingPlayers.size()){
			currentPlayerIndex++;
			System.out.println("currentPlayerIndex = " + currentPlayerIndex);
			currentPlayer = orderedPlayers.get(currentPlayerIndex);
			playersMove();
		}
		else {
			System.out.println("It was the last attacking player");
			endTurn();
		}
		
	}
	
	public void letHumanMove() {
		System.out.println("currentPlayer = " + currentPlayer);
		UIOperator.getInstance().UIEnablePlayerMove(currentPlayer);
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
		if (defendCard.beats(attackCard, getTrump().getSuit())){
			defendingPlayer.defendWith(defendCard, attackCard);
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * End turn with a discard, if defender is successful, or with a flush, if otherwise
	 */
	void endTurn() {
		GameActivity.getInstance().btnEndMove.setEnabled(false);
		System.out.println("Entering endTurn method");
		if (defendingPlayer.isOverwhelmed()){
			defendingPlayer.notOverwhelmed();
			defendingPlayer.setDefender(false);
			System.out.println("Finishing with discard");
			Table.discard();
			drawCardsForAllPlayers();
			removePlayersWithNoCards();
			if (checkGameOver()){
				UIOperator.getInstance().UIDisablePlayerMove();
				btnEndMove.setEnabled(false);
				return;
			}
			else {
				finishTurnWithDiscard();
			}
		}
		else {
			defendingPlayer.notOverwhelmed();
			defendingPlayer.setDefender(false);
			System.out.println("Finishing with flush");
			defendingPlayer.flushTheTable();
			drawCardsForAllPlayers();
			removePlayersWithNoCards();
			if (checkGameOver()){
				UIOperator.getInstance().UIDisablePlayerMove();
				btnEndMove.setEnabled(false);
				return;
			}
			else {
				finishTurnWithFlush();
			}
		}
		UIOperator.getInstance().UIClearTable();
		UIOperator.getInstance().UIRefreshPlayerCards(humanPlayer);
		if (getCurrentPlayer().amIHuman()){
			UIOperator.getInstance().UIEnablePlayerMove(getCurrentPlayer());
		}
		else {
			UIOperator.getInstance().UIDisablePlayerMove();
			
		}
		currentPlayerIndex = FIRST_ATTACKING_PLAYER_INDEX;
		currentPlayer = orderedPlayers.get(currentPlayerIndex);
		playersMove();
	}

	private void drawCardsForAllPlayers() {
		for (Player player : orderedPlayers){
			player.drawCards(newDeck);
		}
	}

	/**
	 * End current turn, have each attacking player draw cards from deck until they have 6 on hand
	 * Change attackers and defender
	 */
	private static void finishTurnWithFlush() {
		if (attackingPlayers.size() > 1){
			rearrangePlayers(attackingPlayers.get(1));
			System.out.println("First attacker is = " + attackingPlayers.get(0));
		}
		else {
			rearrangePlayers(attackingPlayers.get(0));
			System.out.println("First attacker is = " + attackingPlayers.get(0));
		}
	}

	/**
	 * End current turn, have each player to draw cards from deck until they have 6 on hand
	 * Change attackers and defender
	 */
	private static void finishTurnWithDiscard() {
		if (defendingPlayer != null){
			rearrangePlayers(defendingPlayer);
			System.out.println("First attacker will be = " + attackingPlayers.get(0));
		}
		else if (attackingPlayers.size() > 1){
			rearrangePlayers(attackingPlayers.get(1));
			System.out.println("First attacker is = " + attackingPlayers.get(0));
		}
	}
	
	
	private static boolean checkGameOver() {
		if (gameOver()){
			System.out.println("Game Over!!!");
			if (!players.isEmpty()){
				System.out.println(players.get(0) + " is DURAK!");
			}
			else {
				System.out.println("It's a DRAW!");
			}
			return true;
		}		
		return false;
	}

	/**
	 * Check players still in play and remove ones with no cards left
	 * The game is over if number of players still with cards on hand is 1 or 0
	 */
	private static void removePlayersWithNoCards() {
		ArrayList<Player> playersToRemove = new ArrayList<Player>();
		//Two loops are used to avoid ConcurrentModificationException
		for (Player playerInPlay : players){
			if (!playerInPlay.isPlaying()){
				playersToRemove.add(playerInPlay);
			}
		}
		for (Player player : playersToRemove){
			players.remove(player);
			orderedPlayers.remove(player);
			attackingPlayers.remove(player);
			if (defendingPlayer == player){
				defendingPlayer = null;
			}
			System.out.println(player + " has finished playing");
			System.out.println("players list: " + players);
			System.out.println("orderedPlayers list: " + orderedPlayers);
			System.out.println("defendingPlayer is: " + defendingPlayer);
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
		return orderedPlayers;
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
		if (players.size() > 1){
			orderedPlayers.addAll(players.subList(players.indexOf(firstPlayer), players.size()));
		}
		else {
			orderedPlayers.add(players.get(0));
		}
		for (Player player : players){
			if (player.equals(firstPlayer)){
				break;
			}
			else {
				orderedPlayers.add(player);
			}
		}
		
		defendingPlayer = orderedPlayers.get(1);
		defendingPlayer.noAttackThisMove();
		defendingPlayer.setDefender(true);
		orderedPlayers.remove(1);
		orderedPlayers.add(defendingPlayer);
		currentPlayerIndex = FIRST_ATTACKING_PLAYER_INDEX;
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
	


	
}
