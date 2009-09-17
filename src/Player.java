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
		if((x > bet) && (x <= Game.getInstance().getMaxBet())){
			decBudget(x-ownBet);	//decrease with difference to ownBet
			Game.getInstance().incPot(x-ownBet);	//increase pot
			ownBet = x;
			Game.getInstance().setBet(x);
			res = true;
		}
		return res;
	}
	
	protected void call(){
		int bet = Game.getInstance().getBet();
		decBudget(bet-ownBet);	//decrease with difference to ownBet
		Game.getInstance().incPot(bet-ownBet);	//increase pot
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
	}
	public int getBet(){ return ownBet; }
	
	public boolean getStatus(){ return status; }
	
	public Card[] getHold(){ return hold; }
	
	public abstract Action performAction(int bet);
}
