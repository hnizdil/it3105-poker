import java.util.Comparator;

/**
 * Class with one instance (singleton)
 * provides a compare function concerning values
 * @author rb, jh
 *
 */
public class ValueComparator implements Comparator<Card>{
	
	private static ValueComparator instance;
	
	private ValueComparator(){
	}
	
	public static ValueComparator getInstance(){
		if(instance == null){
			instance = new ValueComparator();
		}
		return instance;
	}
	
	/**
	 * @return negative, zero, positive if the value of c1
	 * 		   is less than, equal, greater than the value of c2
	 */
	public int compare(Card c1, Card c2) {
		int i = 0;
		if(c1.getValue().ordinal() >= c2.getValue().ordinal()){
			if(c1.getValue().ordinal() == c2.getValue().ordinal())
				i = 0;
			else i = 1;	
		} else i = -1;
		return i;
	}	
}
