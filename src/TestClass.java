import java.util.ArrayList;
import java.util.Collections;


public class TestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<Value> list = new ArrayList<Value>();
		list.add(0, Value.TWO);
		list.add(Value.FIFE);
		list.add(Value.ACE);
		list.add(Value.KING);
		list.add(Value.NINE);
		Collections.sort(list);
		Collections.reverse(list);
		for(Value i: list){
		System.out.println(i.toString());
		}
	}

}
