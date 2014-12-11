

import java.util.ArrayList;


public abstract class Game {
	
	/**
	 * Players participating in current game
	 */
	private static ArrayList<Player> players = new ArrayList<Player>();
	
	/**
	 * Turns completed in this game
	 */
	//private ArrayList<Turn> turns = new ArrayList<Turn>();
	
	private CardGame game;
	
	private static Deck newDeck = new Deck(DeckSize.THIRTY_SIX);
	
	/*
	 * Current trump card
	 */
	private static Card trumpCard;

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
		firstDraw();
		setMoveOrder();
		playersMove();

	}
	
	/**
	 * Lets players take their moves
	 */
	private static void playersMove() {
		
		ArrayList<Player> attackingPlayers = new ArrayList<Player>();
		ArrayList<Player.ArtificialIntelligence> attackingAIs = new ArrayList<Player.ArtificialIntelligence>();
		
		attackingPlayers.add(players.get(0));
		
		Player defendingPlayer = players.get(1);
		//Create a defending AI for this turn
		Player.ArtificialIntelligence defendingAI = defendingPlayer.new ArtificialIntelligence();
		
		//Create a first attack AI for this turn
		attackingAIs.add(attackingPlayers.get(0).new ArtificialIntelligence());
		
		for (int i = 2; i < players.size(); ++i){
			attackingPlayers.add(players.get(i));
			attackingAIs.add(players.get(i).new ArtificialIntelligence());
		}
		
		attackingAIs.get(0).randomFirstAttack();
		boolean allCardsBeaten = true;
		
		for (Player.ArtificialIntelligence ai : attackingAIs){
			allCardsBeaten = defencePhase(defendingAI);
			while(ai.randomSecondaryAttack());
		}
		if (!allCardsBeaten){
			defendingPlayer.flushTheTable();
		}
		else {
			System.out.println(defendingPlayer + " has defended successfully");
		}
		
	}
	
	/**
	 * Defend against all the unbeaten cards currently on the table
	 * @param ai - AI that shall defend
	 * @return true if all cards have been defended, false - otherwise
	 */
	private static boolean defencePhase(Player.ArtificialIntelligence ai){
		for (Card attackCard : Table.getUnbeatenCards()){
			ai.defend();
		}
		if (Table.getUnbeatenCards().isEmpty()){
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Returns players participating in game
	 * @return ArrayList of players
	 */
	public static ArrayList<Player> getPlayers(){
		return players;
	}
	
	public static Card getTrump(){
		return trumpCard;
	}

	/**
	 * Give each player 6 cards and assign a trump suit
	 */
	private static void firstDraw() {
		
		for (Player player : players){
			for (int i = 0; i < 6; ++i){
				player.addCardToHand(newDeck.draw());
			}
			player.sortCardsOnHands();			
		}
		
		// Get next card from deck
		trumpCard = newDeck.draw();
		System.out.println("Trump: " + trumpCard);
		// Put card at the beginning of the deck 
		newDeck.getCards().add(0, trumpCard);		
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
		System.out.println(players);

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
