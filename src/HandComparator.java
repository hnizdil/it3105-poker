import java.util.ArrayList;
import java.util.Comparator;

public class HandComparator implements Comparator<int[]>
{
	private static HandComparator instance = null;

	private HandComparator()
	{
	}

	public static HandComparator getInstance()
	{
		if (instance == null) instance = new HandComparator();
		return instance;
	}

	public int compare(int[] p1, int[] p2)
	{
		int result = 0;

		for (int i = 0; i < Math.min(p1.length, p2.length); i++) {
			if ((result = p1[i] - p2[i]) != 0) return result;
		}
		
		return result;
	}
}
