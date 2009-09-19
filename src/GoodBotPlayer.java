import java.util.ArrayList;
import java.util.Random;

/**
 * Class for a better artificial Player with pre-flop simulation
 * and hand-strength calculation
 * @author Robert Braunschweig
 *
 */
public class GoodBotPlayer extends Player{
	
	public GoodBotPlayer(Card[] hole){
		super(hole);
	}
	
	public GoodBotPlayer(String name, int budget){
		super(name,budget);
	}
	
	/*
	 * contains the decision of the bot on information of current game state
	 * @see Player#performAction(int)
	 */
	public Action performAction(int currentBet){
		//collecting information on the game
		int pot = Game.getInstance().getPot();
		int numberOfActive = Game.getInstance().getNumberOfActive();
		int maxBet = Game.getInstance().getMaxBet();
		float winProbability = PreFlopTable.getWinningProbability(hole, numberOfActive);
		ArrayList<Card> comCards = Game.getInstance().getComCards();
		Action act = Action.FOLD;	//default value
		
		switch(comCards.size()){
		case 0:	//pre-flop
			//TODO values in here determine if player is aggressive in the beginning
			if(winProbability < 0.5)
				act = Action.FOLD;
			else if(winProbability < 0.65){
				act = Action.CALL;
				call();
			}else{
				act = Action.RAISE;
				//place randomly a bet between ownBet and maxBet
				raise(new Random().nextInt(maxBet-ownBet)+1+ownBet);
			}
			break;
		default:	//after Flop
			ArrayList<Card> cards = new ArrayList<Card>(comCards);
			cards.add(hole[0]);
			cards.add(hole[1]);
			float handStrength = calcHandStrength(cards);
			if(handStrength < 0.5)
				act = Action.FOLD;
			else if(handStrength < 0.65){
				act = Action.CALL;
				call();
			}else{
				act = Action.RAISE;
				//place randomly a bet between ownBet and maxBet
				raise(new Random().nextInt(maxBet-ownBet)+1+ownBet);
			}
		}	
		return act;
	}
	
	private float calcHandStrength(ArrayList<Card> cards){
		float res;
		return res;
	}

}
