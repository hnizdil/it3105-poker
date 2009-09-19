import java.io.*;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("unchecked")

public class PreFlopRolloutSimulation
{
	private static PreFlopRolloutSimulation instance = null;
	private static ArrayList<ArrayList<Card>> classes = generateHoleCardsEquivalenceClasses();
	private static ArrayList<Card> allCards = Card.gen52Cards();
	private static MysqlConnection conn = MysqlConnection.getInstance();

	public static void main(String[] args)
	{
		int upper;
		int size = 4;
		int step = (int)Math.ceil(classes.size() / (float)size);
		ArrayList<ArrayList<Card>> classesSet;

		for (int i = 0; i < size; i++) {
			upper = (i+1) * step;
			upper = upper > classes.size() ? classes.size() : upper;

			classesSet = new ArrayList<ArrayList<Card>>();
			classesSet.addAll(classes.subList(i*step, upper));

			roll(classesSet);
		}

		//PreFlopRolloutSimulation();
		//fillECTable();
	}

	private static void fillECTable()
	{
		HashSet<ArrayList<Card>> hs = new HashSet<ArrayList<Card>>(classes);

		System.out.println("INSERT INTO `preflop_ec` (`id`, `card1`, `card2`, `suited`) VALUES");

		for (ArrayList<Card> ec : hs) {
			System.out.println("(" +
				ec.hashCode() + ", \"" +
				ec.get(0).getValue() + "\", \"" +
				ec.get(1).getValue() + "\", " +
				(ec.get(0).getSuit() == ec.get(1).getSuit() ? "TRUE" : "FALSE") +
			"),");
		}
	}

	private PreFlopRolloutSimulation()
	{
	}

	private static void roll(ArrayList<ArrayList<Card>> classesSet)
	{
		ArrayList<Card> remainder, community, cards;
		HandComparator hc = HandComparator.getInstance();
		int[] myPower, power;
		int result, subResult;
		int run_count, run_wins, run_ties, run_losses;
		int classRounds = 10;
		int plCount;
		int[] subResults;
		ArrayList<Hashtable<Integer,int[]>> results = new ArrayList();
		Hashtable<Integer,int[]> ht;
		int[] ra;

		for (plCount = 1; plCount < 10; plCount++) {
			results.add(plCount-1, new Hashtable<Integer,int[]>());
			ht = results.get(plCount-1);

			for (ArrayList<Card> ec : classesSet) {
				// Reset counters
				run_count = run_wins = run_ties = run_losses = 0;

				// Run through all equivalence classes
				remainder = new ArrayList<Card>();
				remainder.addAll(allCards);
				remainder.removeAll(ec);

				for (int cr = 0; cr < classRounds; cr++) {
					Collections.shuffle(remainder);

					community = new ArrayList<Card>();
					community.addAll(remainder.subList(0, 5));

					// All my cards
					cards = new ArrayList<Card>();
					cards.addAll(community);
					cards.addAll(ec);
					myPower = Card.getHighestPower(cards);

					result = subResult = -1;

					for (int i = 0; i < plCount; i++) {
						cards = new ArrayList<Card>();
						cards.addAll(community);
						cards.addAll(remainder.subList(5+i*2, 7+i*2));
						power = Card.getHighestPower(cards);

						subResult = hc.compare(myPower, power);

						if (subResult < 0) {
							result = subResult;
							break;
						}
						else if (subResult > -1 && result != 0) {
							result = subResult;
						}
					}

					run_count++;
					if (result > 1) {
						run_wins++;
					}
					else if (result == 0) {
						run_ties++;
					}
					else {
						run_losses++;
					}
				}

				ra = new int[]{run_count, run_wins, run_ties, run_losses};
				ht.put(ec.hashCode(), ra);
			}
		}

		conn.saveResults(results);
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
