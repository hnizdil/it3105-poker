/**
 * Class for a artificial Player
 * @author Robert Braunschweig
 *
 */
public class BotPlayer extends Player{
	
	public BotPlayer(String name, int budget){
		super(name,budget);
	}
	
	public Action performAction(int currentBet){
		Action act = Action.FOLD;
		return act;
	}

}
