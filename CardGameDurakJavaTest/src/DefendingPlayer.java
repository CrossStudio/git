import java.util.ArrayList;


public class DefendingPlayer extends Player {

	public DefendingPlayer (Player player){
		this.cardsOnHand = player.cardsOnHand;
		this.cardsToDefendWith = getCardsToPlayWith();
	}
	
	private ArrayList<Card> cardsToDefendWith = new ArrayList<Card>();
	
	public ArrayList<Card> getCardsToPlayWith() {
		for (Card attackCard : Table.getUnbeatenCards()){
			for (Card playersCard : cardsOnHand){
				if (playersCard.getSuit().equals(attackCard.getSuit())){
					if (playersCard.getValue().getNumValue() >
						attackCard.getValue().getNumValue()){
						cardsToDefendWith.add(playersCard);
					}
				}
				else {
					if (playersCard.getSuit().equals(Game.getTrump().getSuit())){
						cardsToDefendWith.add(playersCard);
					}
				}
			}
		}
		return cardsToDefendWith;
	}

}
