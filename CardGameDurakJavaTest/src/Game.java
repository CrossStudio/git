

import java.util.ArrayList;


public abstract class Game {
	
	/**
	 * Players participating in current game
	 */
	private static ArrayList<Player> players = new ArrayList<Player>();
	
	private static ArrayList<Player> attackingPlayers;
	
	private static Player defendingPlayer;
	
	/**
	 * Turns completed in this game
	 */
	//private ArrayList<Turn> turns = new ArrayList<Turn>();
	
	private CardGame game;
	
	private static Deck newDeck = new Deck(DeckSize.THIRTY_SIX);
	
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
	 * Sets trump card equal to the received parameter
	 * @param trumpCard
	 */
	public static void setTrumpCard(){
		trumpCard = newDeck.draw();
		System.out.println("Trump: " + trumpCard);
		// Put card at the beginning of the deck 
		newDeck.getCards().add(0, trumpCard);
	}
	
	public static void main (String[] a){

		Player vasia = new Player("Vasia");
		Player petia = new Player("Petia");
		Player kolia = new Player("Kolia");
		Player gena = new Player("Gena");

		players.add(vasia);
		players.add(petia);
		players.add(kolia);
		players.add(gena);
		
		playGame();
		
	}	
	
	/**
	 * Start a new game 
	 */
	private static void playGame() {
		for (Player player : players){
			player.drawCards(newDeck);
		}
		setTrumpCard();
		setMoveOrder();
		while (!gameOver()){
			playersMoves();
		}

	}
	
	/**
	 * Lets players make their moves
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
			finishTurn(attackingPlayers.get(1));
		}
		else{
			finishTurn(defendingPlayer);
		}
	}
	
	private static void finishTurn(Player firstPlayer) {
		Table.discard();
		for (Player attackingPlayer : players){
			attackingPlayer.drawCards(newDeck);
		}
		defendingPlayer.drawCards(newDeck);
		checkPlayersInPlay();
		rearrangePlayers(firstPlayer);
	}
	
	

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
}
