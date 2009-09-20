import java.io.File;
import java.util.Scanner;

class PreFlopTable
{
	private static PreFlopTable instance = null;

	// Probabilities
	private static double[] probs = new double[1521];

	// Equivalence classes (same indexing as probs[])
	private static int[][] classes = new int[1521][];

	/**
	 * Constructor.
	 * Loads probability file and fills probs[] and classes[].
	 *
	 * @param file Filename of the probability file
	 */
	private PreFlopTable(String file)
	{
		int c1val, c2val, suited, players, i = 0;

		try {
			Scanner sc = new Scanner(new File(file));

			// Start rading tokens in file (every row has exactly 5 tokens)
			while (sc.hasNext()) {
				// We don't care about suit, because we need just value
				c1val = Value.valueOf(sc.next()).ordinal();
				c2val = Value.valueOf(sc.next()).ordinal();

				// Suited and number of players
				suited = sc.nextInt();
				players = sc.nextInt();

				// Save the probability
				probs[i] = Double.parseDouble(sc.next());

				// Save values identifying probability
				classes[i++] = new int[]{c1val, c2val, suited, players};
			}

			sc.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

	public static PreFlopTable getInstance()
	{
		if (instance == null) {
			instance = new PreFlopTable("PreFlopTable.dat");
		}

		return instance;
	}

	public static double getWinningProbability(Card[] hole, int players)
	{
		int
			i,
			suited = hole[0].getSuit() == hole[1].getSuit() ? 1 : 0,
			c1val  = hole[0].getValue().ordinal(),
			c2val  = hole[1].getValue().ordinal();

		// Loop through all equivalence classes
		for (i = 0; i < classes.length; i++) {
			// Compare suited and number of players
			if (classes[i][2] != suited)  continue;
			if (classes[i][3] != players) continue;

			// Compare cards
			if (
				(classes[i][0] == c1val && classes[i][1] == c2val) ||
				(classes[i][1] == c1val && classes[i][0] == c2val)
			) {
				break;
			}
		}

		return probs[i];
	}

	/**
	 * Just for testing
	 */
	/*
	public static void main (String[] args)
	{
		System.out.println(PreFlopTable.getInstance().getWinningProbability(
			new Card[]{new Card(Suit.SPADE, Value.KING), new Card(Suit.HEART, Value.KING)}, 5
		));
	}
	*/
}
