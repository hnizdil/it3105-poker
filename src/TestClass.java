import java.util.ArrayList;


public class TestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(0, 1);
		list.add(3);
		System.out.println(list.get(0).toString()+list.get(1).toString());
	}

}
