import java.util.ArrayList;

/**
 * Super class for a Player (bot or human)
 * Provides common features: hold cards, budget, name,...
 * @author Robert Braunschweig
 *
 */
public abstract class Player {
	
	protected Card[] hole;	//hold cards
	protected String name;	//Players name
	protected int budget;	//Players money
	protected boolean status; //true = player is in the game, false = out
	protected int ownBet;
	protected int wins;
	protected ArrayList<Card> allCards = Card.gen52Cards();
	
	/*
	 * Player constructor for "dummy" player for Hand-Strength-Calculations
	 */
	protected Player(Card[] hole){
		this.hole = hole;
	}
	
	/**
	 * Constructor for real players
	 * @param name
	 * @param budget
	 */
	protected Player(String name, int budget) {
		hole = new Card[2];
		this.name = name;
		this.budget = budget;
		status = true;
		wins = 0;
	}
	
	public void incWins(){ wins++;}
	
	public int getWins(){ return wins;}
	
	public void setHole(Card h1, Card h2) {
		hole[0] = h1;
		hole[1] = h2;
	}
	
	/*
	 * Performes a raise
	 * Decreases the difference to the old own bet from budget
	 * @param x	the new current bet
	 * @return	true if raise is possible and executed properly
	 * 			false otherwise
	 */
	protected boolean raise(int x){
		boolean res = false;
		int bet = Game.getInstance().getBet();
		int diff = x-ownBet;
		int newBet = x;
		do {
			//if x is not in the valid range it is set to the old bet (like a call)
			if ((newBet >= bet) && (newBet <= Game.getInstance().getMaxBet())){
				decBudget(diff);	//decrease with difference to ownBet
				Game.getInstance().incPot(diff);	//increase pot
				ownBet = newBet;
				Game.getInstance().setBet(newBet);
				res = true;
			}
			else {
				newBet = bet;
				diff = newBet-ownBet;
			}
		} while (!res);

		return res;
	}
	
	/*
	 * Performs a call.
	 * Bets the same as current bet and decreases the difference
	 * to the old own bet from budget
	 */
	protected void call(){
		int bet = Game.getInstance().getBet();
		int diff = bet-ownBet;
		decBudget(diff);	//decrease with difference to ownBet
		Game.getInstance().incPot(diff);	//increase pot
		ownBet = bet;
	}
	
	/*
	 * some useful functions
	 */
	public void initBet(){ ownBet = 0; }
	
	public void setStatus(boolean b){ status = true; }
	
	public void incBudget(int a){ budget += a;	}
	
	public void decBudget(int a){
		budget -= a;
		ownBet += a;
	}

	public int getBudget()
	{
		return budget;
	}
	
	public int getBet(){ return ownBet; }
	
	public boolean getStatus(){ return status; }
	
	public Card[] getHole(){ return hole; }
	
	/**
	 * Performs an Action with input of the current bet
	 * @param bet the common bet of the current betting round
	 * @return the performed Action
	 */
	public abstract Action performAction(int bet);
	
	public String toString(){ return name;	}
}
