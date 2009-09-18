import java.util.ArrayList;

public class PreFlopRolloutSimulation
{
	private static PreFlopRolloutSimulation instance = null;
	private static ArrayList<ArrayList<Card>> classes = new ArrayList<ArrayList<Card>>();
	private static ArrayList<Card> allCards = Card.gen52Cards();

	public static void main(String[] args)
	{
		PreFlopRolloutSimulation();
	}

	private static void PreFlopRolloutSimulation()
	{
		classes = generateHoleCardsEquivalenceClasses();

		ArrayList<Card> cards = Card.gen52Cards();
		/*
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.SPADE, Value.TWO));
		cards.add(new Card(Suit.SPADE, Value.THREE));
		cards.add(new Card(Suit.SPADE, Value.FOUR));
		cards.add(new Card(Suit.SPADE, Value.FIVE));
		cards.add(new Card(Suit.SPADE, Value.SIX));
		cards.add(new Card(Suit.SPADE, Value.SEVEN));
		cards.add(new Card(Suit.SPADE, Value.EIGHT));
		*/

		Combination cg = new Combination(cards, 2);
		while (cg.hasMore()) {
			System.out.println(cg.getNext());
		}

		//System.out.println(classes.get(0));
	}

	public static PreFlopRolloutSimulation getInstance()
	{
		if (instance == null) instance = new PreFlopRolloutSimulation();
		return instance;
	}

	private static ArrayList<ArrayList<Card>> generateHoleCardsEquivalenceClasses()
	{
		int i, j;
		Value values[] = Value.values();

		ArrayList<Card> pair;
		ArrayList<ArrayList<Card>> classes = new ArrayList<ArrayList<Card>>();

		// Different value, different suit
		for (i = 1; i < values.length; i++) {
			for (j = 0; j < i; j++) {
				pair = new ArrayList<Card>();
				pair.add(new Card(Suit.SPADE, values[i]));
				pair.add(new Card(Suit.HEART, values[j]));
				classes.add(pair);
			}
		}

		// Different value, same suit
		for (i = 1; i < values.length; i++) {
			for (j = 0; j < i; j++) {
				pair = new ArrayList<Card>();
				pair.add(new Card(Suit.SPADE, values[i]));
				pair.add(new Card(Suit.SPADE, values[j]));
				classes.add(pair);
			}
		}

		// Same value, different suit
		for (Value v : Value.values()) {
			pair = new ArrayList<Card>();
			pair.add(new Card(Suit.SPADE, v));
			pair.add(new Card(Suit.HEART, v));
			classes.add(pair);
		}

		return classes;
	}
}
