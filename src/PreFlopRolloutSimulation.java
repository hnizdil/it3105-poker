import mpi.*;
import java.io.*;
import java.util.Date;
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
		MPI.Init(args);

		int
			size = MPI.COMM_WORLD.Size(),
			rank = MPI.COMM_WORLD.Rank(),
			step = (int)Math.ceil(classes.size() / (float)size);

		// Don't have to do anything fancy, just process rank will do the trick
		roll(step, rank, 100);

		MPI.Finalize();

		//fillECTable();
	}

	/**
	 * Prints SQL to fill preflop_ec table to the standard output
	 */
	private static void fillECTable()
	{
		ArrayList<ArrayList<Card>> hs = new ArrayList<ArrayList<Card>>(classes);

		System.out.println("INSERT INTO `preflop_ec` (`id`, `card1`, `card2`, `suited`) VALUES");

		for (ArrayList<Card> ec : hs) {
			System.out.println("(" +
				hs.indexOf(ec) + ", \"" +
				ec.get(0).getValue() + "\", \"" +
				ec.get(1).getValue() + "\", " +
				(ec.get(0).getSuit() == ec.get(1).getSuit() ? "TRUE" : "FALSE") +
			"),");
		}
	}

	/**
	 * Just empty constructor.
	 */
	private PreFlopRolloutSimulation()
	{
	}

	/**
	 * Rolls preflop rollout simulation
	 *
	 * @param length Length of chunk to get from equivalence classes.
	 * @param offset Offset to start the chunk. It's in multiples of chunk.
	 */
	private static void roll(int length, int offset, int rounds)
	{
		ArrayList<int[]> results = new ArrayList<int[]>();
		ArrayList<Card> remainder, community, cards;
		ArrayList<ArrayList<Card>> classesSet = new ArrayList<ArrayList<Card>>();
		HandComparator hc = HandComparator.getInstance();

		int[] myPower, power;

		int
			lower = offset*length, upper,
			result, subResult,
			runCount, runWins, runTies, runLosses,
			plCount,
			eclassIndex;

		// Get chunk of this process
		upper = (upper = lower + length) > classes.size() ? classes.size() : upper;
		classesSet.addAll(classes.subList(lower, upper));

		// Main neverending loop
		while(true) {

		// Reset results ArrayList
		results.clear();

		// Loop through all player counts
		for (plCount = 1; plCount < 10; plCount++) {
			// Loop through equivalence classes
			for (ArrayList<Card> ec : classesSet) {
				// ID of this equivalence class
				eclassIndex = classesSet.indexOf(ec) + lower;

				// Reset counters
				runCount = runWins = runTies = runLosses = 0;

				// Run through all equivalence classes
				remainder = new ArrayList<Card>();
				remainder.addAll(allCards);
				remainder.removeAll(ec);

				// Do rounds rounds for each equivalence class
				for (int cr = 0; cr < rounds; cr++) {
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

					runCount++;
					if (result > 1) {
						runWins++;
					}
					else if (result == 0) {
						runTies++;
					}
					else {
						runLosses++;
					}
				} // Rounds loop

				// Save round results
				results.add(new int[]{eclassIndex, plCount, runCount, runWins, runTies, runLosses});
			} // Equivalence classes loop
		} // Player counts loop

		conn.saveResults(results);

		System.out.println(new Date().getTime() + " - rank: " + MPI.COMM_WORLD.Rank() + ", saved: " + results.size());

		} // Main neverending loop
	}

	/**
	 * Instance getter
	 */
	public static PreFlopRolloutSimulation getInstance()
	{
		if (instance == null) instance = new PreFlopRolloutSimulation();
		return instance;
	}

	/**
	 * Generates equivalence classes. Just one pair from each class.
	 */
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
