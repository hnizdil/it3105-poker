import java.util.ArrayList;

/**
 * Class for a artificial Player
 * @author Robert Braunschweig
 *
 */
public class GoodBotPlayer extends Player{
	
	public GoodBotPlayer(String name, int budget){
		super(name,budget);
	}
	
	/*
	 * contains the decision of the bot on information of current game state
	 * @see Player#performAction(int)
	 */
	public Action performAction(int currentBet){
		int pot = Game.getInstance().getPot();
		ArrayList<Card> comCards = Game.getInstance().getComCards();
		Action act = Action.FOLD;
		int maxBet = Game.getInstance().getMaxBet(); 
		
		
		
		return act;
	}

}
