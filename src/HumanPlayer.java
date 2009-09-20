import java.util.Scanner;

/**
 * Class for a human Player
 * @author Robert Braunschweig
 *
 */

public class HumanPlayer extends Player {
	
	public HumanPlayer(String name, int budget){
		super(name, budget);
	}
	
	/**
	 * performs action according to keyboard input
	 */
	public Action performAction(int currentBet){
		Scanner sc = new Scanner(System.in);
		Action act = Action.FOLD;
		String str;
		boolean b = false;
		do{
			System.out.println(name+", choose your action! (f-fold, c-call,r-raise): ");
			str = sc.next();
			if(!((str.equals("f")) || (str.equals("c")) || (str.equals("r")))){
				System.out.println("Wrong input, try again!");
				b = true;
			}else
				b = false;
		}while(b);
		switch(str.charAt(0)){
		case 'f':
			act = Action.FOLD; break;
		case 'c': 
			act = Action.CALL;
			call();
			break;
		case 'r':
			act = Action.RAISE;
			System.out.println("Type in your new bet ("+currentBet+"< new bet <"
					+Game.getInstance().getMaxBet()+"): ");
			raise(sc.nextInt());
			break;
		}
		
		return act;
	}
}
