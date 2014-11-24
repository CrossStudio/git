

import java.util.ArrayList;

public class Game {
	
	/**
	 * Players participating in current game
	 */
	private static ArrayList<Player> players = new ArrayList<Player>();
	
	/**
	 * Turns completed in this game
	 */
	private ArrayList<Turn> turns = new ArrayList<Turn>();
	
	private CardGame game;
	
	private static Deck newDeck;
	
	/*
	 * Current trump card
	 */
	private static Card trumpCard;
	
	/**
	 * Create a new game
	 * @param players - players in current game
	 * @param game - type of card game
	 */
	public Game(ArrayList<Player> players, CardGame game){
		this.players = players;
		this.game = game;		
		
		switch(game){
		case DURAK:
			newDeck = new Deck(DeckSize.THIRTY_SIX);
			break;
		default: break;
		}
	}

	public static void main (String[] a){

		Player vasia = new Player();
		Player petia = new Player();
		Player kolia = new Player();
		Player gena = new Player();
		ArrayList<Player> playersInGame = new ArrayList<Player>();
		playersInGame.add(vasia);
		playersInGame.add(petia);
		playersInGame.add(kolia);
		playersInGame.add(gena);
		
		Game newGame = new Game(playersInGame,CardGame.DURAK);
		newGame.playGame();
		
	}	
	
	/**
	 * Start a new game 
	 */
	private void playGame() {	
		firstDraw();
		setMoveOrder();

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
	private void setMoveOrder(){
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
	private void rearrangePlayers(Player firstPlayer) {
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
	private boolean gameOver(){
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
