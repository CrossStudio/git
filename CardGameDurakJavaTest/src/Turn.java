import java.util.ArrayList;


public class Turn {
	
	private Player mainPlayer;
	
	private Player defendingPlayer;
	
	private ArrayList<Player> attackingPlayers = new ArrayList<Player>();
	
	private int cardsPlayed;
	
	private int cardsDefended;
	
	public Turn (Player attackPlayer){
		ArrayList<Player> players = Game.getPlayers();
		this.mainPlayer = attackPlayer;
		if (players.indexOf(attackPlayer) < players.size() - 1){
			this.defendingPlayer = players.get(players.indexOf(attackPlayer)+1);
		}
		else {
			this.defendingPlayer = players.get(0);
		}
		
		mainPlayer.attackWith(mainPlayer.cardsOnHand.get((int)(Math.random()*mainPlayer.cardsOnHand.size())));
	}
	
	public int getCardsPlayed(){
		return cardsPlayed;
	}
	
	public int getCardsDefended(){
		return cardsDefended;
	}
	
	public Turn getTurn(){
		return this;
	}
	
}