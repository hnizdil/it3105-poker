import java.util.ArrayList;
import java.util.ListIterator;
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
		comCards = new ArrayList<Card>();
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
	
	public ArrayList<Card> getComCards(){ return comCards;}
	
	public int getNumberOfActive(){ return activePlayers.size(); }
	
	public int getPot(){ return pot; }
	
	public int getMaxBet(){	return maxBet; }
	
	public int getBet(){ return bet; }
	
	public void setBet(int x){ bet = x;	}
	
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
				System.out.print("Is player "+(i+1)+" human (h) or good artificial (a) or bad artificial (b)? ");
				ch = sc.next();
				if(!(ch.equals("h") || ch.equals("g") || ch.equals("b"))){
					System.out.println("Wrong input! Try again!");
					b = true;
				}	
			} while(b);	
			System.out.print("Name of the new player: ");
			name = sc.next();
			if(ch.equals("h"))
				players.add(new HumanPlayer(name,budget));
			else if(ch.equals("g"))
				players.add(new GoodBotPlayer(name,budget));
			else if(ch.equals("b"))
				players.add(new BadBotPlayer(name,budget));
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
			p.setHole(deck.get(0), deck.get(1));
			//remove that two cards from deck
			deck.remove(0);
			deck.remove(0);
			if(p instanceof HumanPlayer){
				System.out.println("The Hold cards for "+p.toString()+" (other humans must look away)." +
						" Press any key and return for showing the cards!");
				Scanner sc = new Scanner(System.in);
				sc.next();
				Card[] hold = p.getHole();
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
	 * resets pot, community cards, shuffles deck again, moves order one seat further
	 */
	private void newGame(){
		pot = 0;
		bet = 0;
		comCards.clear();
		deck = Card.gen52Cards();
		Card.shuffleCards(deck);
		newOrder();
		activePlayers.clear();
		activePlayers.addAll(players);
	}
	
	/*
	 * main frame of the game
	 * realizes the betting, the dealing, money handling,...
	 */
	private void runGame() {
		Action a;
		Player p;
		boolean betReady = false;
		boolean gameRunning = true;
		
		dealCards();
		activePlayers.get(0).decBudget(blind);
		pot = blind;
		bet = blind;
		do{		//beginning of betting round
			System.out.println("Current pot: "+pot+", current bet: "+bet);
			printComCards();
			ListIterator<Player> it = activePlayers.listIterator();
			while(it.hasNext()){
				p = it.next();
				a = p.performAction(bet);
				printAction(p.toString(), a);
				switch(a){
				case FOLD:
					it.remove();
					break;
				case CALL:
					break;
				case RAISE:
					System.out.println("New bet: "+bet);
				}	
			}
			if(activePlayers.size() > 1){
				//check all active Players are in
				for(Player pl: activePlayers){	
					if(pl.getBet() == bet)
						betReady = true;
					else{
						betReady = false;
						break;
					}
				}
				if(betReady){	//betting round is over -> next community Card
					bet = 0;
					for(Player ap: activePlayers)	//set ownBet to 0
						ap.initBet();
					switch(comCards.size()){
					case 0:		//the Flop
						dealComCards(3);
						break;
					case 3:		//the Turn
						dealComCards(1);
						break;
					case 4:		//the River
						dealComCards(1);
						break;
					case 5:		//Showdown
						assignPot(getWinner(activePlayers));
						gameRunning = false;
					}
				}
			}else{
				//only one player left -> gets the pot
				assignPot(activePlayers.toArray(new Player[1]));
				gameRunning = false;
			}
		}while(gameRunning);
	}
	
	public void start(){
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
	
	private void printComCards(){
		for(Card c: comCards){
			System.out.print(c.toString());
		}
		System.out.println();
	}
	
	public Player[] getWinner(ArrayList<Player> pl){
		ArrayList<Player> winner = null;
		int[] winPower = null;
		ArrayList<Card> cards = new ArrayList<Card>();
		//compare all with current best player
		for(Player p: pl){
			cards.clear();
			if(winner == null) {
				winner = new ArrayList<Player>();
				winner.add(p);
				cards.addAll(comCards);
				//add two hold cards to comm. cards
				cards.add(p.getHole()[0]);
				cards.add(p.getHole()[1]);
				winPower = Card.getHighestPower(cards); 
			}else {
				int[] power;	//power to be compared
				int compareResult;	//result of comparing
				cards.addAll(comCards);
				//add two hold cards to comm. cards
				cards.add(p.getHole()[0]);
				cards.add(p.getHole()[1]);
				power = Card.getHighestPower(cards);	
				compareResult = Card.comparePower(winPower, power);
				if(compareResult <= 0){	//Player p is better or equal than current "winner"
					if(compareResult < 0)
						winner.clear(); //clear winner list
					winner.add(p);
				}	//else no changes needed
			}
		}
		return winner.toArray(new Player[1]);	//convert list to array and return
	}
	
	/*
	 * splits the pot to several winners or even to one winner
	 * resets the pot to 0
	 */
	private void assignPot(Player[] pa){
		int amount = pot/pa.length;
		for(Player p: pa){
			p.incBudget(amount);
			System.out.println(p.toString()+" wins "+amount);
		}
		pot = 0;
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
