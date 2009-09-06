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
		boolean keyFlag = false;//true=Value, false=Suit
		
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
	
	/*
	private static ArrayList<ArrayList<Card>> sortList(ArrayList<ArrayList<Card>> cl){
		//TODO maybe a short sort algorithm for sorting the sublists
	}
	*/
	
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
		ArrayList<ArrayList<Card>> suitGroups = genSuitGroups(cards);
		//temporary List to build the tuple
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		int[] powerList;
		
		//single Cards from high to low
		int[] tieBreaker = findTieBreaker(valGroups); 
		//Flush and Straight Flush
		if(findFlush(suitGroups)){
			if(findStraight(valGroups)){ //Straight Flush
				tempList.add(0,9);
				tempList.add(tieBreaker[0]); //14 (Ace) for Royal Flush
			}	
			else {
				tempList.add(0, 6); //normal Flush
				//adds tie-breaker to end of list
				for(int i=0; i<tieBreaker.length; i++){
					tempList.add(tieBreaker[i]); 
				}
			}	
		} else{
			//normal Straight
			if(findStraight(valGroups)){
				tempList.add(0,5);
				tempList.add(tieBreaker[0]);
			}	
		}
		
		
		//findEqualVal(valGroups);
		powerList = new int[tempList.size()];
		for(int j=0; j<tempList.size(); j++){
			powerList[j] = tempList.get(j);
		}
		return powerList;
	}
	
	/**
	 * @param vg Value Groups
	 * @return Array of sorted values of single Cards
	 */
	private static int[] findTieBreaker(ArrayList<ArrayList<Card>> vg){
		ArrayList<Integer> temp = new ArrayList<Integer>();
		int[] i;
		for(ArrayList<Card> cards: vg){
			if(cards.size() == 1)
				temp.add(cards.get(0).getValue().ordinal()+2); //Card-deck starts at 2!
		}
		Collections.sort(temp);
		Collections.reverse(temp);
		i = new int[temp.size()];
		for(int j=0; j<temp.size(); j++){
			i[j] = temp.get(j);
		}
		return i;
	}
	
	private static boolean findFlush(ArrayList<ArrayList<Card>> sg){
		if( (sg.size()==1) && (sg.get(0).size()==5))
			return true;
		else return false;
	}
	
	private static boolean findStraight(ArrayList<ArrayList<Card>> vg){
		boolean x = false;
		ArrayList<Value> vals = new ArrayList<Value>();
		
		if(vg.size() == 5){
			for(ArrayList<Card> c: vg){
				vals.add(c.get(0).getValue());
			}
			Collections.sort(vals);
			for(int j=0; j<vals.size()-1; j++){
				//looks if cards are in a row
				if(vals.get(j+1).ordinal() - vals.get(j).ordinal() == 1)
					x = true;
				else {
					x = false;
					break; //not all in a row; routine can stop
				}
			}
		} else x = false;
		return x;
	}
	
	/**
	 * @param vg - List of value grouped sublists
	 * @return max Value
	 * 
	private static Value getMaxVal(ArrayList<ArrayList<Card>> vg) {
		Value maxVal = vg.get(0).get(0).getValue(); //Value of first sublist
		for(int i=1; i<vg.size(); i++) { //go through all sublists
			Value compVal = vg.get(i).get(0).getValue();
			if(compVal.ordinal() > maxVal.ordinal())
				maxVal = compVal;
		}
		return maxVal;
	}
	*/
	
	/**
	 * Finds groups of equal Values (pairs, 3/4 of an kind) 
	 * @param vg
	 * @return 
	 *
	private static int[] findEqualVal(ArrayList<ArrayList<Card>> vg){
		
	}
	*/
}
