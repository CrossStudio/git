

import java.util.ArrayList;
import java.util.Arrays;

public class Game {
	
	private ArrayList<Player> players = new ArrayList<Player>();
	
	private CardGame game;
	
	private Deck newDeck;
	
	/*
	 * Козырь в данной игре
	 */
	private Suit trump;
	
	public Game(ArrayList<Player> players, CardGame game){
		this.players = players;
		this.game = game;
		this.trump = getTrump();
		
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
	
	private Suit getTrump(){
		int cardSuit = ((int)(Math.random() * 4));
		switch(cardSuit){
		case 0:
			return Suit.SPADES;
		case 1:
			return Suit.CLUBS;
		case 2:
			return Suit.DIAMONDS;
		case 3:
			return Suit.HEARTS;
		default:
			return getTrump();
		}
	}
	
	private void playGame() {
		
		newDeck.shuffleCardsInDeck();
		givePlayersTheirCards();
	}

	private void givePlayersTheirCards() {
		
		for (Player player : players){
			for (int i = 0; i < 6; ++i){
				player.addCardToHand(newDeck.drawCard());
			}
			sortCardsOnHands(player);
			
		}
		
	}

	private void sortCardsOnHands(Player player) {
		Card[] tempArray = new Card[player.cardsOnHand.size()];
		player.cardsOnHand.toArray(tempArray);
		Arrays.sort(tempArray, new CardComparator());
		player.cardsOnHand = new ArrayList<>(Arrays.asList(tempArray));
		System.out.println(player.cardsOnHand);
		
	}
	
	
	
	
}
