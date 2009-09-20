import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class TestClass {
	
	private static final SuitComparator suitComp = SuitComparator.getInstance();
	private static final ValueComparator valueComp = ValueComparator.getInstance();

	public static void main(String[] args) {
		/*ArrayList<Card> list = new ArrayList<Card>();
		list.add(new Card(Suit.DIAMOND, Value.TWO));
		list.add(new Card(Suit.HEART, Value.THREE));
		list.add(new Card(Suit.DIAMOND, Value.ACE));
		list.add(new Card(Suit.CLUB, Value.ACE));
		list.add(new Card(Suit.SPADE, Value.FOUR));
		
		System.out.println(new Card(Suit.CLUB, Value.ACE).equals(new Card(Suit.CLUB, Value.ACE)));
		//Collections.reverse(list);
		//list = Card.gen52Cards();
		
		//list.add(new Card(Suit.CLUB, Value.KING));
		for(Card i: list){
			System.out.println(i);
		}	
		/*
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
		//for(int i:Card.getHighestPower(list2))
		System.out.println(list.size());*/
		Game.getInstance().start();
		//System.out.println(list.size());
	}
}	
