import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


public class Game {
	
	private ArrayList<Player> players;	//2-10 players
	private ArrayList<Player> activePlayers; // all player who did not fold
	private ArrayList<Card> deck;		//52 cards
	private ArrayList<Card> comCards;	//community cards: flop-turn-river
	private int pot;					//The pot
	private int bet;					//current bet
	private int maxBet;
	private int blind;
	private static Game instance;
	
	/*
	 * private constructor. singleton pattern
	 */
	private Game(){
		players = new ArrayList<Player>();
		activePlayers = new ArrayList<Player>();
		initGame();
	}
	
	/*
	 * get-set methods
	 */
	public static Game getInstance(){
		if(instance == null)
			instance = new Game();
		return instance;
	}
	public int getMaxBet(){
		return maxBet;
	}
	public int getBet(){
		return bet;
	}
	public void setBet(int x){
		bet = x;
	}
	
	/*
	 * scans parameters from keyboard: number of players, initial budget,
	 * generation of deck
	 */
	private void initGame(){
		Scanner sc = new Scanner(System.in);
		int budget, playNum;
		System.out.print("Number of all players (2-10): ");
		playNum = sc.nextInt();
		System.out.print("Initial budget of every player: ");
		budget = sc.nextInt();
		System.out.print("Maximum bet each betting round: ");
		maxBet = sc.nextInt();
		System.out.print("Blind (only one Blind for the Player who goes first): ");
		blind = sc.nextInt();
		//Player instantiations
		for(int i=0; i<playNum; i++){
			String ch, name;
			boolean b;
			do{
				b = false;
				System.out.print("Is player "+(i+1)+" human(h) or artificial(a)? ");
				ch = sc.next();
				if(!(ch.equals("h") || ch.equals("a"))){
					System.out.println("Wrong input! Try again!");
					b = true;
				}	
			} while(b);	
			System.out.print("Name of the new player: ");
			name = sc.next();
			if(ch.equals("h"))
				players.add(new HumanPlayer(name,budget));
			else if(ch.equals("a"))
				players.add(new BotPlayer(name,budget));
		}
		deck = Card.gen52Cards();
		Card.shuffleCards(deck);
	}
	
	/*
	 * takes the first player and put him to the end
	 */
	private void newOrder(){
		Player p = players.get(0);
		players.remove(0);
		players.add(p);
	}
	
	private void dealCards(){
		for(Player p: players){
			//deal two cards
			p.setHold(deck.get(0), deck.get(1));
			//remove that two cards from deck
			deck.remove(0);
			deck.remove(0);
			if(p instanceof HumanPlayer){
				System.out.println("The Hold cards for "+p.toString()+" (other humans must look away)." +
						" Press return for showing the cards!");
				System.in.read();
				Card[] hold = p.getHold();
				System.out.println(hold[0].toString()+hold[1].toString());
			}
		}
	}
	
	/*
	 * deals i community cards on the table
	 * for example, i=3 for flop
	 */
	private void dealComCards(int i){
		for(int k=0; k<i; k++){
			comCards.add(deck.get(0));
			deck.remove(0);
		}
	}
	/*
	 * resets pot, community cards, shuffles deck again
	 */
	private void newGame(){
		pot = 0;
		comCards.clear();
		deck = Card.gen52Cards();
		Card.shuffleCards(deck);
		activePlayers.addAll(players);
	}
	
	/*
	 * TODO implement the betting round, check for winner, ...
	 */
	private void runGame() {
		int round = 1;	//number of current betting round
		Action a;
		Player p;
		activePlayers.get(0).decBudget(blind);
		do{
			Iterator<Player> it = activePlayers.listIterator();
			while(it.hasNext()){
				p = it.next();
				a = p.performAction(bet);
				printAction(p.toString(), a);
				System.out.print("Current pot: "+pot);
				switch(a){
				case FOLD:
					it.remove();
					break;
				case CALL:
					System.out.print(", current bet: "+bet);
					break;
				case RAISE:
					System.out.print(", new bet: "+bet);
				}	
					
				}
			}
		}while();
	}
	
	private void start(){
		Scanner sc = new Scanner(System.in);
		String ch;
		do{
			newGame();
			runGame();
			do{
				System.out.println("Next Game? (y/n): ");
				ch = sc.next();
			}while(!(ch.equals("y") || ch.equals("n")));
		}while(ch.equals("y"));
	}
	
	private void printAction(String name, Action act){
		System.out.println(name+" did: "+act.toString());
	}
	
	public void incPot(int a){
		pot += a;
	}
	
	/**
	 * @param args
	
	public static void main(String[] args) {
		instance = new Game();
		instance.start();		
	} */

}
