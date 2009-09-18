import java.util.ArrayList;
import java.util.Collections;

public class TestClass {
	
	private static final SuitComparator suitComp = SuitComparator.getInstance();
	private static final ValueComparator valueComp = ValueComparator.getInstance();	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<Card> list = new ArrayList<Card>();
		/*
		list.add(new Card(Suit.DIAMOND, Value.SEVEN));
		list.add(new Card(Suit.HEART, Value.KING));
		list.add(new Card(Suit.DIAMOND, Value.JACK));
		list.add(new Card(Suit.CLUB, Value.SIX));
		list.add(new Card(Suit.SPADE, Value.NINE));
		*/

		//Collections.reverse(list);
		list = Card.gen52Cards();

		ArrayList<Card>
			hand1 = new ArrayList<Card>(),
			hand2 = new ArrayList<Card>();

		// Hand 1
		Card.shuffleCards(list);
		for (int i = 0; i < 5; i++) hand1.add(list.get(i));
		Card.shuffleCards(list);
		for (int i = 0; i < 5; i++) hand2.add(list.get(i));

		System.out.println(hand1);
		System.out.println(hand2);

		System.out.println();

		int[] power1 = Card.calcCardsPower(hand1);
		int[] power2 = Card.calcCardsPower(hand2);

		HandComparator comparator = HandComparator.getInstance();

		System.out.println(comparator.compare(power1, power2));

		//for(int i: Card.calcCardsPower(list)){
		/*
		for (int i = 0; i < list.size(); i++){
			System.out.println(list.get(i));
		}
		*/
		//System.out.println(list.size());

		//new Game();
	}

}
