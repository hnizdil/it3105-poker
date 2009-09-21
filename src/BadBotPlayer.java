import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Class for a simple artificial Player who makes his decisions only on the current game state
 * @author rb, jh
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
			pot = Game.getInstance().getPot(),
			maxBet = Game.getInstance().getMaxBet(),
			active = Game.getInstance().getNumberOfActive();

		int[] power;

		ArrayList<Card>
			hand = new ArrayList<Card>(Arrays.asList(hole)),
			comCards = Game.getInstance().getComCards();

		if (active == 1) {
			call();
			return Action.CALL;
		}

		// Default action is to fold
		Action act = Action.FOLD;

		// We're after the flop
		if (comCards.size() > 0) {
			hand.addAll(comCards);
			power = Card.getHighestPower(hand);

			if (power[0] < 2) {
				act = Action.FOLD;
			}
			else if ((power[0] > 4) && (ownBet < maxBet) && (raises < 1)) {
				act = Action.RAISE;
				raise(ownBet + new Random().nextInt(maxBet-ownBet)+1);
			}
			else {
				act = Action.CALL;
				call();
			}
		}
		// It's preflop, we will just call
		else {
			act = Action.CALL;
			call();
		}

		return act;
	}
}
