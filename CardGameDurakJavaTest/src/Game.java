

import java.util.ArrayList;
import java.util.Arrays;

public class Game {
	
	private ArrayList<Player> players = new ArrayList<Player>();
	
	private CardGame game;
	
	private Deck newDeck;
	
	/*
	 * Козырь в данной игре
	 */
	private Suit trumpSuit;
	
	/*
	 * Last trump cards
	 */
	private Card trumpCard;
	
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
		ArrayList<Player> playersInGame = new ArrayList<Player>();
		playersInGame.add(vasia);
		playersInGame.add(petia);
		
		Game newGame = new Game(playersInGame,CardGame.DURAK);
		newGame.playGame();
		
	}	
	
	private void playGame() {	
		givePlayersTheirCards();
	}

	private void givePlayersTheirCards() {
		
		for (Player player : players){
			for (int i = 0; i < 6; ++i){
				player.addCardToHand(newDeck.draw());
			}
			player.sortCardsOnHands();			
		}
		
		// Get next card from deck
		this.trumpCard = newDeck.draw();
		// Save trump card suit
		this.trumpSuit = this.trumpCard.getSuit();
		// Put card at the beginning of the deck 
		this.newDeck.getCards().add(0, this.trumpCard);		
	}		
}
