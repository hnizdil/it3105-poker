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
	
	/**
	 * 
	 * @param cards 
	 * @param key - Value or Suit
	 * @return partitioned List of Cards (2-dimensional List)
	 */
	private static ArrayList<ArrayList<Card>> partition(Collection<Card> cards, Object key) {
		ArrayList<ArrayList<Card>> cl = new ArrayList<ArrayList<Card>>(); //partitioned list
		Card[] ca = (Card[])cards.toArray(); //input-"set"
		ArrayList<Card> temp; //temporary List
		boolean fitting = false; //Flag if a Card is fitting into a sublist
		boolean keyFlag;	//true=Value, false=Suit
		
		if(key instanceof Value) keyFlag=true;
		else if(key instanceof Suit) keyFlag=false;
		else ; //TODO place for an exception
		
		for(int x=0;x<ca.length;x++){
			for(ArrayList<Card> sublist: cl){ //search in all sublists
				if(keyFlag){
					if(sublist.get(0).getValue()==ca[x].getValue()){
						sublist.add(ca[x]);
						fitting=true;
					}
				}else{
					if(sublist.get(0).getSuit()==ca[x].getSuit()){
						sublist.add(ca[x]);
						fitting=true;
					}
				}	
			}
			//if no fitting list, create a new one
			if(!fitting){
				temp = new ArrayList<Card>();
				temp.add(ca[x]);
				cl.add(temp);
			}
		}
		return cl;
	}	
	
	private static ArrayList<ArrayList<Card>> sortList(ArrayList<ArrayList<Card>> cl){
		//TODO maybe a short sort algorithm for sorting the sublists
	}
	
	private static ArrayList<ArrayList<Card>> genValueGroups (Collection<Card> cards) {
		return partition(cards,Value.ACE); //which value doesn't matter
	}
	
	private static ArrayList<ArrayList<Card>> genSuitGroups (Collection<Card> cards) {
		return partition(cards,Suit.CLUB); //which suit doesn't matter
	}
	
	/**
	 * calculates Card's Power for 5 Cards
	 * @param cards (Collection of size 5)
	 * @return a tuple representing the Card's power
	 */
	public static int[] calcCardsPower(Collection<Card> cards) {
		ArrayList<ArrayList<Card>> valGroups = genValueGroups(cards);
		ArrayList<Integer> tempList; //temporary List to build the tuple
		
		//Flush and Straight Flush
		if(findFlush(valGroups)){
			if(findStraight(valGroups))
				tempList.add(0,9); //TODO proof if this is legal
			else tempList.add(0,6);
			//tie breaking number (+2 because ordinal() starts at 0)
			tempList.add(getMaxVal(valGroups).ordinal()+2);
		}
		
		findEqualVal(valGroups); //TODO specify return value
	}
	
	private static boolean findFlush(ArrayList<ArrayList<Card>> vg){
		boolean x = false;
		Suit s;
		s = vg.get(0).get(0).getSuit(); //Suit of first Card in first sublist
		if(vg.size()==5){
			for(ArrayList<Card> sublist: vg){
				if(sublist.get(0).getSuit() == s) x = true;
				else {
					x = false;
					break;
				}
			}
		}
		return x;
	}
	
	/**
	 * @param vg - List of value grouped sublists
	 * @return max Value
	 */
	private static Value getMaxVal(ArrayList<ArrayList<Card>> vg) {
		Value maxVal = vg.get(0).get(0).getValue(); //Value of first sublist
		for(int i=1; i<vg.size(); i++) { //go through all sublists
			Value compVal = vg.get(i).get(0).getValue();
			if(compVal.ordinal() > maxVal.ordinal())
				maxVal = compVal;
		}
		return maxVal;
	}
	
	/**
	 * Finds groups of equal Values (pairs, 3/4 of an kind) 
	 * @param vg
	 */
	private static findEqualVal(ArrayList<ArrayList<Card>> vg){
		
	}
}
