import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class for a better artificial Player with pre-flop simulation
 * and hand-strength calculation
 * TODO testing
 * @author rb,jh
 * @version 20.09.2009
 */
public class GoodBotPlayer extends Player
{
	//Thresholds before the Flop
	private final double preFlopFoldLimit = 0.085;
	private final double preFlopCallLimit = 0.20;

	//Thresholds after Flop (with com. Cards)
	private final double foldLimit = 0.2;
	private final double callLimit = 0.5;

	public GoodBotPlayer(Card[] hole)
	{
		super(hole);
	}

	public GoodBotPlayer(String name, int budget)
	{
		super(name,budget);
	}

	/**
	 * contains the decision of the bot on information of current game state
	 * @see Player#performAction(int)
	 */
	public Action performAction(int currentBet)
	{
		//collecting information on the game
		int pot = Game.getInstance().getPot();
		int numberOfActive = Game.getInstance().getNumberOfActive();
		int maxBet = Game.getInstance().getMaxBet();
		ArrayList<Card> comCards = Game.getInstance().getComCards();
		double winProbability = 0;

		// Default action is to fold
		Action act = Action.FOLD;

		// Last man, just call for the pot
		if (numberOfActive == 1) {
			call();
			return Action.CALL;
		}

		// Preflop
		if (comCards.size() == 0) {
			winProbability = PreFlopTable.getWinningProbability(hole, numberOfActive);

			//TODO values in here determine if player is aggressive in the beginning
			if (winProbability < preFlopFoldLimit) {
				act = Action.FOLD;
			}
			else if ((winProbability < preFlopCallLimit) || (ownBet == maxBet) || (raises > 1)) {
				act = Action.CALL;
				call();
			}
			else {
				//place randomly a bet between ownBet and maxBet
				act = Action.RAISE;
				raise(ownBet + new Random().nextInt(maxBet-ownBet)+1);
			}
		}
		// Postflop
		else {
			double handStrength = calcHandStrength(comCards, numberOfActive);

			//System.err.println(handStrength);

			if (handStrength < foldLimit) {
				act = Action.FOLD;
			}
			else if ((handStrength < callLimit) || (ownBet == maxBet)) {
				act = Action.CALL;
				call();
			}
			else {
				//place randomly a bet between ownBet and maxBet
				act = Action.RAISE;
				raise(ownBet+1 + (int)Math.floor((maxBet-ownBet-1)*handStrength));
			}
		}

		return act;
	}

	/*
	 * calculates probability of win of the current Hand
	 */
	private double calcHandStrength(ArrayList<Card> comCards, int numberOfActive)
	{
		ArrayList<Card> deck = new ArrayList<Card>(allCards);
		HandComparator hc = HandComparator.getInstance();
		int[] stat = new int[]{0, 0, 0}; // loss, tie, win

		ArrayList<Card>
			nmHand,
			myHand = new ArrayList<Card>(comCards);

		int[]
			myPower,
			nmPower;

		// Cards I have together with community cards
		myHand.addAll(Arrays.asList(this.hole));

		// Power of my hand
		myPower = Card.getHighestPower(myHand);

		// Without the own hole cards and the comCards
		deck.removeAll(myHand);

		// Loop thgrough all remaining combinations and compare powers
		for (ArrayList<Card> pair : new Combination(deck, 2)) {
			nmHand = new ArrayList<Card>(comCards);
			nmHand.addAll(pair);
			nmPower = Card.getHighestPower(nmHand);
			stat[Integer.signum(hc.compare(myPower, nmPower))+1]++;
		}

		// Equation from the paper
		double s = Math.pow(
			(stat[2] + stat[1]/2.0) / (double)(stat[0]+stat[1]+stat[2]),
			numberOfActive
		);

		/*
		System.err.print(s + "\t" + stat[0] + "\t" + stat[1] + "\t" + stat[2] + "\t===\t");
		for (int z = 0; z < myPower.length; z++) System.err.print(myPower[z] + "\t");
		System.err.println();
		*/

		return s;
	}
}
