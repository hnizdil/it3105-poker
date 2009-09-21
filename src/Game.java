import java.io.File;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;

/**
 * Game is the basic frame in which the Poker game runs.
 * It organizes the Players and active Players, holds all relevant game
 * information like pot, current bet, maximum bet, number of played games.
 * It deals the cards to the players and holds the community card.
 * It finds out who is the winner and assigns the money.
 * 
 * Game has the Singleton pattern. Only one instance may be accessed via
 * the static method getInstance().
 * 
 *@author rb, jh
 *@version 20.09.2009
 */
public class Game
{
	private ArrayList<Player> players;			//2-10 players
	private ArrayList<Player> activePlayers; 	// all player who did not fold
	private ArrayList<Card> deck;				//52 cards
	private ArrayList<Card> comCards;			//community cards: flop-turn-river
	private int pot;							//The pot
	private int bet;							//current bet
	private int maxBet;							//maximum bet amount per round
	private int blind;							//bet of first player in first round
	private int numberOfGames;
	private static Game instance;

	private static int noOfGames = 1;
	private static File commandFile = null;

	//Comparator for hand power
	private static final HandComparator powerComp = HandComparator.getInstance();

	/**
	 * private constructor. singleton pattern
	 */
	private Game()
	{
		players       = new ArrayList<Player>();
		activePlayers = new ArrayList<Player>();
		comCards      = new ArrayList<Card>();
		numberOfGames = 0;
		initGame();
	}

	/**
	 * get-set methods
	 */
	public static Game getInstance(){
		return instance = instance == null ? new Game() : instance;
	}

	public ArrayList<Card> getComCards(){ return comCards;}

	public int getNumberOfActive(){ return activePlayers.size(); }

	public int getPot(){ return pot; }

	public ArrayList<Card> getDeck(){ return deck; }

	public int getMaxBet(){	return maxBet; }

	public int getBet(){ return bet; }

	public void setBet(int x){ bet = x;	}

	/**
	 * scans parameters from keyboard: number of players, initial budget,
	 * generation of deck
	 */
	private void initGame()
	{
		Scanner sc = null;

		// Command file not specified, we'll read stdin
		if (commandFile == null) {
			sc = new Scanner(System.in);
		}
		// Read the file, baby
		else {
			try {
				sc = new Scanner(commandFile);
			}
			catch (Exception e) {
				System.out.println(e);
			}
		}

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
		for (int i = 0; i < playNum; i++){
			boolean b;
			String name;
			char ch;

			do {
				System.out.print(
						"Is player " + (i+1) +
						" human (h) or good artificial (g) or bad artificial (b)? "
						);
				ch = sc.next().charAt(0);

				if(b = ch != 'h' && ch != 'g' && ch != 'b') {
					System.out.println("Wrong input. Try again, please.");
				}
			} while(b);

			System.out.print("Name of the new player: ");
			name = sc.next();

			// 3 different Player types
			switch (ch) {
				case 'h': players.add(new HumanPlayer(name,budget)); break;
				case 'g': players.add(new GoodBotPlayer(name,budget)); break;
				case 'b': players.add(new BadBotPlayer(name,budget)); break;
				default:
			}
		}
		deck = Card.gen52Cards();
		Card.shuffleCards(deck);
	}

	/**
	 * Takes the first player and put him to the end
	 */
	private void newOrder(){
		Player p = players.get(0);
		players.remove(0);
		players.add(p);
	}

	private void dealCards(){
		for(Player p: players){
			//deal two cards to every player
			p.setHole(deck.get(0), deck.get(1));

			//remove that two cards from deck
			deck.remove(0);
			deck.remove(0);

			//in case of a human player the cards are shown on the screen
			if(p instanceof HumanPlayer){
				System.out.println("The Hold cards for "+p.toString()+" (other humans must look away)." +
						" Press any key and return for showing the cards!");
				Scanner sc = new Scanner(System.in);
				sc.next();
				sc.close();
				Card[] hold = p.getHole();
				System.out.println(hold[0].toString()+hold[1].toString());
			}
		}
	}

	/**
	 * deals i community cards on the table
	 * for example, i=3 for flop
	 */
	private void dealComCards(int i){
		for(int k=0; k<i; k++){
			comCards.add(deck.get(0));
			deck.remove(0);
		}
	}

	/**
	 * resets pot, community cards, shuffles deck again, moves order one seat further
	 * and resets all players to active players
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

	/**
	 * main frame of the game
	 * realizes the betting, the dealing, money handling,...
	 */
	private void runGame() {
		Action a;
		Player p;
		boolean betReady = false;
		boolean gameRunning = true;

		// Announce beggining of the game
		System.out.println("\n\n\n=== GAME " + numberOfGames + " BEGINS ===");

		//before the first betting round
		dealCards();
		activePlayers.get(0).decBudget(blind);
		pot = bet = blind;

		// beginning of betting round
		do {
			// prints the current game state
			System.out.println("\n= Another round begins =");
			System.out.println("Current pot: "+pot+", current bet: "+bet);
			System.out.println(comCards);
			ListIterator<Player> it = activePlayers.listIterator();

			// asks all active players (who didn't fold) for their next Action
			while (it.hasNext()){
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

			// all players performed an Action

			if(activePlayers.size() > 1){
				//check all active players if they are "in the game"
				for (Player pl: activePlayers) {
					if (pl.getBet() == bet) {
						betReady = true;
					}
					else {
						betReady = false;
						break;
					}
				}

				//betting round is over -> next community Card(s)
				if (betReady) {
					bet = 0;

					//set ownBet to 0
					for(Player ap: activePlayers) ap.initBet();

					switch (comCards.size()) {
						// The flop
						case 0: dealComCards(3); break;

						// The turn
						case 3: dealComCards(1); break;

						// The river
						case 4: dealComCards(1); break;

						// Showdown
						case 5:
							assignPot(getWinner(activePlayers));
							gameRunning = false;
					}
				}
				//only one player left -> gets the pot
			}
			else {
				assignPot(activePlayers);
				gameRunning = false;
			}
		} while (gameRunning);
	}

	/**
	 * The overall starting method.
	 * Provides a loop to play more than 1 Game.
	 * Prints the result of all games in the end.
	 */
	public void start()
	{
		while (numberOfGames++ < noOfGames) {
			newGame();
			runGame();
		}

		printGameResult();
	}

	private void printGameResult(){
		System.out.println("Played games: "+numberOfGames);
		for(Player p: players) {
			System.out.println(
				p + " won " + p.getWins() + " times " +
				"and has " + p.getBudget() + " money"
			);
		}
	}

	private void printAction(String name, Action act){
		System.out.println(name+" did: "+act.toString());
	}

	/**
	 * Looks for the winner.
	 * For several players with same power a list of them is returned
	 *
	 * @param pl list of players after one Game (after showdown, last remaining)
	 * @return list of winning players
	 */
	public ArrayList<Player> getWinner(ArrayList<Player> pl){
		ArrayList<Player> winner = null;
		int[] winPower = null;
		ArrayList<Card> cards = new ArrayList<Card>();

		//compare all with current best player
		for (Player p: pl) {
			cards.clear();

			if (winner == null) {
				winner = new ArrayList<Player>();
				winner.add(p);
				cards.addAll(comCards);

				//add two hold cards to comm. cards
				cards.add(p.getHole()[0]);
				cards.add(p.getHole()[1]);
				winPower = Card.getHighestPower(cards); 
			}
			else {
				int[] power;	//power to be compared
				int compareResult;	//result of comparing
				cards.addAll(comCards);

				//add two hold cards to comm. cards
				cards.add(p.getHole()[0]);
				cards.add(p.getHole()[1]);
				power = Card.getHighestPower(cards);	
				compareResult = powerComp.compare(winPower, power);

				//Player p is better or equal than current "winner"
				if (compareResult <= 0) {
					if (compareResult < 0) winner.clear(); //clear winner list
					winner.add(p);
				}

				//else no changes needed
			}
		}

		// if only one winner, his win-statistics is increased
		if (winner.size() == 1) winner.get(0).incWins();

		//convert list to array and return
		return winner;
	}

	/**
	 * splits the pot to several winners or even to one winner
	 * resets the pot to 0
	 */
	private void assignPot(ArrayList<Player> pa){
		int amount;
		amount = pot / pa.size();
		for(Player p: pa){
			p.incBudget(amount);
			System.out.println(p.toString()+" wins " + amount);
		}	
		pot = 0;
	}

	/**
	 * Increments the pot with a
	 * @param a
	 */
	public void incPot(int a){
		pot += a;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		// Number of games
		if (args.length > 0) noOfGames = Integer.parseInt(args[0]);

		// Command file
		if (args.length > 1) commandFile = new File(args[1]);

		Game.getInstance().start();
	}
}
