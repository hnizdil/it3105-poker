import java.util.Comparator;

/**
 * Class with one instance (singleton)
 * provides a compare function concerning suits
 * @author rb, jh
 *
 */
public class SuitComparator  implements Comparator<Card>{

	private static SuitComparator instance = null;
	
	private SuitComparator(){
	}

	public static SuitComparator getInstance(){
		if (instance == null) instance = new SuitComparator();
		return instance;
	}
	
	/**
	 * @return negative, zero, positive if the suit of c1
	 * 		   is less than, equal, greater than the suit of c2
	 * reference order: DIAMOND-HEART-SPADE-CLUB
	 */
	public int compare(Card c1, Card c2) {
		int i = 0;
		if(c1.getSuit().ordinal() >= c2.getSuit().ordinal()){
			if(c1.getSuit().ordinal() == c2.getSuit().ordinal())
				i = 0;
			else i = 1;	
		} else i = -1;
		return i;
	}	
}
