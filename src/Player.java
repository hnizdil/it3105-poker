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
	
	public Player(String name, int budget) {
		this.name = name;
		this.budget = budget;
		status = true;
	}
	
	
}
