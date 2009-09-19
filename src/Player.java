/**
 * Super class for a Player (bot or human)
 * Provides common features: hold cards
 * @author Robert Braunschweig
 *
 */
public abstract class Player {
	
	protected Card[] hold;	//hold cards
	protected String name;	//Players name
	protected int budget;	//Players money
	protected boolean status; //true = player is in the game, false = out
	protected int ownBet;
	
	public Player(String name, int budget) {
		hold = new Card[2];
		this.name = name;
		this.budget = budget;
		status = true;
	}
	
	public void setHold(Card h1, Card h2) {
		hold[0] = h1;
		hold[1] = h2;
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
	
	public Card[] getHold(){ return hold; }
	
	public abstract Action performAction(int bet);
	
	public String toString(){
		return name;
	}
}
