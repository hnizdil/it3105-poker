/**
 * Super class for a Player (bot or human)
 * Provides common features: hold cards
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
	protected Game game;
	
	/*
	 * Player constructor for "dummy" player for Hand-Strength-Calculations
	 */
	protected Player(Card[] hole){
		this.hole = hole;
		this.game = Game.getInstance();
	}
	
	protected Player(String name, int budget) {
		this.game = Game.getInstance();
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
	 * @param x	the new current bet
	 * @return	true if raise is possible and executed properly
	 * 			false otherwise
	 */
	protected boolean raise(int x){
		boolean res = false;
		int bet = Game.getInstance().getBet();
		int diff = x-ownBet;
		int newBet = x;
		do{
			if((newBet >= bet) && (newBet <= Game.getInstance().getMaxBet())){
				decBudget(diff);	//decrease with difference to ownBet
				Game.getInstance().incPot(diff);	//increase pot
				ownBet = newBet;
				Game.getInstance().setBet(newBet);
				res = true;
			}else{
				newBet = bet;
				diff = newBet-ownBet;
			}
		}while(!res);
		return res;
	}
	
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
	public void initBet(){
		ownBet = 0;
	}
	public void setStatus(boolean b){
		status = true;
	}
	public void incBudget(int a){
		budget += a;
	}
	
	public void decBudget(int a){
		budget -= a;
		ownBet += a;
	}
	public int getBet(){ return ownBet; }
	
	public boolean getStatus(){ return status; }
	
	public Card[] getHole(){ return hole; }
	
	public abstract Action performAction(int bet);
	
	public String toString(){
		return name;
	}
}
