import java.util.ArrayList;
import java.util.Random;

/**
 * Class for a better artificial Player with pre-flop simulation
 * and hand-strength calculation
 * TODO testing
 * @author Robert Braunschweig
 *
 */
public class GoodBotPlayer extends Player{
	private final double foldLimit = 0.01;
	private final double callLimit = 0.15;
	
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
		double winProbability = PreFlopTable.getWinningProbability(hole, numberOfActive);
		ArrayList<Card> comCards = Game.getInstance().getComCards();
		Action act = Action.FOLD;	//default value
		
		switch(comCards.size()){
		case 0:	//pre-flop
			//TODO values in here determine if player is aggressive in the beginning
			if(winProbability < foldLimit)
				act = Action.FOLD;
			else if(winProbability < callLimit){
				act = Action.CALL;
				call();
			}else{
				act = Action.RAISE;
				//place randomly a bet between ownBet and maxBet
				raise(new Random().nextInt(maxBet-ownBet+1)+ownBet);
			}
			break;
		default:	//after Flop
			ArrayList<Card> cards = new ArrayList<Card>(comCards);
			cards.add(hole[0]);
			cards.add(hole[1]);
			double handStrength = calcHandStrength(cards,numberOfActive);
			if(handStrength < foldLimit)
				act = Action.FOLD;
			else if(handStrength < callLimit){
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
	
	/*generates the index tuples for all two hole cards out of the remaining deck
	 * of size N
	 *  
	 */
	private ArrayList<int[]> genIndexSet(int N){
		ArrayList<int[]> res = new ArrayList<int[]>();
		int[] tuple;
		for(int i=0; i<N; i++)
			for(int j=0; j<i; j++){
				tuple = new int[2];
				tuple[0] = i;
				tuple[1] = j;
				res.add(tuple);
			}
		return res;
	}
	
	/*
	 * calculates probability of win of the current Hand
	 */
	private double calcHandStrength(ArrayList<Card> comCards, int numberOfActive){
		double res;
		ArrayList<int[]> indexSet = genIndexSet(50-comCards.size());
		ArrayList<Card> deck = Card.gen52Cards();	//full deck
		//without the own hole cards and the comCards
		deck.remove(this.hole[0]);
		deck.remove(this.hole[1]);
		deck.remove(comCards);
		Player p;
		Card[] hole = new Card[2];	//hole for comparison
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Player> winner;
		int[] statistic = new int[]{0,0,0};	//win - loss - tie
		players.add(this);	//the calling player is always part of the List
		for(int[] pair: indexSet){
			hole[0] = deck.get(pair[0]);
			hole[1] = deck.get(pair[1]);
			p = new GoodBotPlayer(hole);
			players.add(p);
			winner = Game.getInstance().getWinner(players);
			if(winner.size()> 1)	//tie
				statistic[2]++;
			else
				if(winner.get(0).equals(this))	//win
					statistic[0]++;
				else						//loss
					statistic[1]++;	
		}
		//equation from script (paper)
		res = (statistic[0]+(statistic[2]/2))/(statistic[0]+statistic[1]+statistic[2]);
		res = Math.pow(res, numberOfActive);
		return res;
	}

}
