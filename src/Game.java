import java.util.ArrayList;
import java.util.Scanner;


public class Game {
	
	private ArrayList<Player> players;	//2-10 players
	private ArrayList<Card> deck;		//52 cards
	private int pot;					//The pot
	
	public Game(){
		pot = 0;
		initGame();
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
				new HumanPlayer(name,budget);
			else if(ch.equals("a"))
				new BotPlayer(name,budget);
		}
		deck = Card.gen52Cards();
		Card.shuffleCards(deck);
	}
	
	/**
	 * @param args
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	} */

}
