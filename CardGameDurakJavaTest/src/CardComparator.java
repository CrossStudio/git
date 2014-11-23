import java.util.Comparator;


public class CardComparator implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		
		Card card1 = (Card) o1;
		Card card2 = (Card) o2;
		
		if (card1.getSuit().getNumValue() > card2.getSuit().getNumValue()){
			return 1;
		}
		else if (card1.getSuit().getNumValue() < card2.getSuit().getNumValue()){
			return -1;
		}
		else {
			if (card1.getValue().getNumValue() > card2.getValue().getNumValue()){
				return 1;
			}
			else if (card1.getValue().getNumValue() < card2.getValue().getNumValue()){
				return -1;
			}
			else{
				return 0;
			}
		}		
	}	
}
