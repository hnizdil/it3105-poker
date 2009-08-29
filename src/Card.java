import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Models a playing card out of an 52-card Poker-deck
 * 
 * @author Robert Braunschweig
 * @version 26.08.09
 *
 */
public class Card {
	private Suit suit;
	private Value value;
	
	public Card(Suit s, Value v) {
		suit = s;
		value = v;
	}
	
	public Suit getSuit() {	return suit; }
	
	public Value getValue() { return value;	}
	
	/**
	 * @return the name of the card (String with suit and value)
	 */
	public String getCardName() {
		return suit.toString()+value.toString();
	}
	
	/**
	 * 
	 * @param cards - A Collection of Cards
	 * @return Names of the cards
	 */
	public ArrayList<String> getCardsNames(Collection<Card> cards) {
		ArrayList<String> strArray = new ArrayList<String>();
		for (Card c: cards) {
			//stores cardname in local String-Collection
			strArray.add(c.getCardName());
		}
		return strArray;
	}
	
	/**
	 * Compares two Cards
	 * @param c - A Card
	 * @return true-equal, false-not equal
	 */
	public boolean equals(Card c) {
		if ((c.suit==this.suit)&&(c.value==this.value)) 
			return true;
		else 
			return false;	
	}
	
	/**
	 * Genereates a full card deck
	 * @return card deck
	 * TODO Parameter yes or no?
	 */
	public static Collection<Card> gen52Cards() {
		Collection<Card> deck = new ArrayList<Card>();
		for (Suit s: Suit.values()) {
			for (Value v: Value.values()) {
				deck.add(new Card(s,v));
			}
		}
		return deck;
	}
	
	/**
	 * Shuffles the given List
	 * @param cl - List of Cards
	 * 
	 * TODO possibly built in Random parameter 
	 */
	public static void shuffleCards(List<Card> cl) {
		Collections.shuffle(cl);
	}
	
	private static ArrayList<Card> partition(Collection<Card> cards, Object key) {
		
	}
	
	private static ArrayList<Value[]> genValueGroups (Collection<Card> cards) {
		
	}
	public static int[] calcCardsPower(Collection<Card> cards) {
		
	}
	
}
