import java.util.ArrayList;


public class Turn {
	
	private Player mainPlayer;
	
	private Player defendingPlayer;
	
	private ArrayList<Player> attackingPlayers = new ArrayList<Player>();
	
	private int cardsPlayed;
	
	private int cardsDefended;
	
	public Turn (ArrayList<Player> players){
		this.mainPlayer = mainPlayer;
		this.defendingPlayer = defendingPlayer;
		for (Player player : players){
			if (!player.amIDefending()){
				this.attackingPlayers.add(player);
			}
		}
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