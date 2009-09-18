import java.util.ArrayList;

@SuppressWarnings("unchecked")

public class Combination
{
	private ArrayList set;
	private CombinationGenerator cg;

	public boolean hasMore()
	{
		return this.cg.hasMore();
	}

	public ArrayList getNext()
	{
		ArrayList combo = new ArrayList();

		for (int i : cg.getNext()) combo.add(this.set.get(i));

		return combo;
	}

	public Combination(ArrayList set, int k)
	{
		this.set = set;
		this.cg = new CombinationGenerator(set.size(), k);
	}

	/*
	 * This method is pretty cool, but due to the recursion also very memory
	 * demanding. Took me like two hours to write this, but for generating
	 * combinations from card deck with 52 cards it consumes too much memory.
	 */
	/*
	public static ArrayList<ArrayList> compute(ArrayList set, int k)
	{
		int i, j;
		Object removed;
		ArrayList combination, subSet;
		ArrayList<ArrayList>
			subCombinations,
			combinations = new ArrayList<ArrayList>();

		if (k > set.size()) {
			throw new IllegalArgumentException();
		}
		else if (set.size() < 1) {
			throw new IllegalArgumentException();
		}
		else if (k == 2) {
			for (i = 1; i < set.size(); i++) {
				for (j = 0; j < i; j++) {
					combination = new ArrayList();
					combination.add(set.get(i));
					combination.add(set.get(j));
					combinations.add(combination);
				}
			}
		}
		else {
			subSet = (ArrayList)set.clone();

			while (subSet.size() >= k) {
				removed = subSet.remove(0);
				subCombinations = compute(subSet, k - 1);
				for (ArrayList sc : subCombinations) sc.add(removed);
				combinations.addAll(subCombinations);
			}
		}

		return combinations;
	}
	*/
}
