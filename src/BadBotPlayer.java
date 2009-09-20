import java.util.Arrays;
import java.util.ArrayList;

/**
 * Class for a artificial Player
 * @author Robert Braunschweig
 *
 */
public class BadBotPlayer extends Player {
	public BadBotPlayer(Card[] hole){
		super(hole);
	}

	public BadBotPlayer(String name, int budget)
	{
		super(name,budget);
	}
	
	/*
	 * contains the decision of the bot on information of current game state
	 * @see Player#performAction(int)
	 */
	public Action performAction(int currentBet)
	{
		int
			pot = game.getPot(),
			maxBet = game.getMaxBet();

		int[] power;

		ArrayList<Card>
			hand = new ArrayList<Card>(Arrays.asList(hole)),
			comCards = game.getComCards();

		Action act = Action.FOLD;

		switch (comCards.size()) {
			// Preflop
			case 0:
				act = Action.CALL;
				break;

			// Postflop
			case 3:
				System.out.println(hand);
				break;

			// Turn or turn and river
			default:
		}

		return act;
	}
}
