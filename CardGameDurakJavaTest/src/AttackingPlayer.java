import java.util.ArrayList;


public class AttackingPlayer extends Player {
	
	public AttackingPlayer (Player player){
		this.cardsOnHand = player.cardsOnHand;
		this.cardsToAttackWith = getCardsToPlayWith();
	}
	
	private ArrayList<Card> cardsToAttackWith = new ArrayList<Card>();
	
	/**
	 * Returns cards of this player that are available for attack at the moment
	 * @return
	 */
	public ArrayList<Card> getCardsToPlayWith(){
		if (Table.getAllCardsOnTable().size() != 0){
			for (Card playersCard : cardsOnHand){
				for (Card cardOnTable : Table.getAllCardsOnTable()){
					if (playersCard.getSuit().equals(cardOnTable.getSuit()) ||
							playersCard.getValue().equals(cardOnTable.getValue())){
						cardsToAttackWith.add(playersCard);
					}
				}
			}
		}
		else {
			return cardsOnHand;
		}
		return cardsToAttackWith;
	}
	
}
