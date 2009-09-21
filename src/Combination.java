import java.math.BigInteger;
import java.util.Iterator;
import java.util.Arrays;
import java.util.ArrayList;

@SuppressWarnings("unchecked")

/*
 * Uses Knuth's lexicographic combinations algorithm
 * http://www-cs-faculty.stanford.edu/~knuth/fasc3a.ps.gz
 *
 * Can be looped through with foreach when instantiated.
 *
 */
class Combination implements Iterable<ArrayList>, Iterator
{
	private int[] c;
	private int n, k, i, x;
	private BigInteger left;
	private ArrayList set = new ArrayList();

	public Combination(ArrayList set, int k)
	{
		this.set.addAll(set);
		this.n = set.size();
		this.k = k;
		this.left = factorial(n).divide(factorial(k).multiply(factorial(n-k)));

		// Initialization
		c = new int[k+2];

		for (i = 0; i < k; i++) c[i] = i;

		c[i] = n;
		c[i+1] = 0;

		i = k;
	}

	/*
	 * Returns next combination
	 */
	public int[] comboNext()
	{
		boolean gotoT4;
		int[] combo = Arrays.copyOf(c, k);

		left = left.subtract(BigInteger.ONE);

		if (i > 0) {
			c[--i] = x = i + 1;
		}
		else {
			if (c[0]+1 < c[1]) {
				c[0]++;
			}
			else {
				i = 2;

				do {
					c[i-2] = i - 2;
					x = c[i-1] + 1;
					if (gotoT4 = x == c[i]) i++;
				} while (gotoT4);

				c[--i] = x;
			}
		}

		return combo;
	}

	/*
	 * Calculates factorial
	 */
	private BigInteger factorial(int x)
	{
		BigInteger f = BigInteger.ONE;
		for (int i = x; i > 1; i--) {
			f = f.multiply(new BigInteger(Integer.toString(i)));
		}
		return f;
	}

	/*
	 * Iterable interface
	 */
	public Iterator<ArrayList> iterator()
	{
		return this;
	}

	/*
	 * Iterator interface
	 */
	public boolean hasNext()
	{
		return left.compareTo(BigInteger.ZERO) == 1;
	}

	/*
	 * Iterator interface
	 */
	public Object next()
	{
		ArrayList result = new ArrayList();
		int[] combo = comboNext();
		for (int i : combo) result.add(set.get(i));
		return result;
	}

	/*
	 * Iterator interface
	 */
	public void remove()
	{
	}

	/*
	 * Test method
	 */
	/*
	public static void main(String[] args)
	{
		ArrayList<Integer> cards = new ArrayList<Integer>();

		for (int q = 0; q < 50; q++) cards.add(q);

		Combination c = new Combination(cards, 5);

		for (ArrayList<Integer> combo : c) {
			System.out.println(combo);
		}
	}
	*/
}
