import java.util.ArrayList;
import java.util.Collections;


public class TestClass {
	
	private static final SuitComparator suitComp = SuitComparator.getInstance();
	private static final ValueComparator valueComp = ValueComparator.getInstance();	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	/*	ArrayList<Card> list = new ArrayList<Card>();
		list.add(new Card(Suit.DIAMOND, Value.SEVEN));
		list.add(new Card(Suit.HEART, Value.KING));
		list.add(new Card(Suit.DIAMOND, Value.JACK));
		list.add(new Card(Suit.CLUB, Value.SIX));
		list.add(new Card(Suit.SPADE, Value.NINE));

		//Collections.reverse(list);
		//list = Card.gen52Cards();
		//Card.shuffleCards(list);
		
		/*for(int i: Card.calcCardsPower(list)){
			System.out.println(i);
		}
		System.out.println(list.size());*/
		new Game();
	}

}
