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
	
	public int getBet(){ return ownBet; }
	
	public boolean getStatus(){ return status; }
	
	public abstract Action performAction(int bet);
}
