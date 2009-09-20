import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


/**
 * Models a playing card out of an 52-card Poker-deck.
 * Provides the basic methods for finding the power of a card set 
 * and comparing them.
 *
 */
public class Card{
	private Suit suit;
	private Value value;
	//Comparators for comparing according to suit or value
	private static final SuitComparator suitComp = SuitComparator.getInstance();
	private static final ValueComparator valueComp = ValueComparator.getInstance();
	//Comparator for hand power
	private static final HandComparator powerComp = HandComparator.getInstance();
	//Indices of combinations for 7-card input or 6-card-input
	private static final HashSet<HashSet<Integer>> indexSet_7 = genIndexSet(7,5);
	private static final HashSet<HashSet<Integer>> indexSet_6 = genIndexSet(6,5);

	public Card(Suit s, Value v) {
		suit = s;
		value = v;
	}

	//instanciating a an empty card
	public Card() {}

	public Suit getSuit() {	return suit; }

	public Value getValue() { return value;	}

	/**
	 * Compares two Cards
	 * @param c - A Card
	 * @return true-equal, false-not equal
	 */
	@Override public boolean equals(Object o) {
		if (o instanceof Card) {
			Card c = (Card)o;
			return c.suit == this.suit && c.value == this.value;
		}
		else {
			return false;
		}
	}

	/**
	 * Generates a full card deck
	 * @return card deck
	 */
	public static ArrayList<Card> gen52Cards() {
		ArrayList<Card> deck = new ArrayList<Card>();
		//goes through all suits and values
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
	 */
	public static void shuffleCards(List<Card> cl) {
		Collections.shuffle(cl);
	}

	/*
	 * Partitions a list of cards by Value or Suit
	 * @param cards 
	 * @param key - Value or Suit
	 * @return partitioned List of Cards (2-dimensional List)
	 */
	private static ArrayList<ArrayList<Card>> partition(ArrayList<Card> cards, Object key) {
		ArrayList<ArrayList<Card>> partList = new ArrayList<ArrayList<Card>>(); //partitioned list
		ArrayList<Card> tempList = cards; //temporary List
		ArrayList<Card> tempSubList;
		Card tempCard;
		boolean keyFlag = false;//true=Value, false=Suit

		//specifies which partitioning shall be executed
		//and sorts according to this decision
		if(key instanceof Value){
			keyFlag = true;
			Collections.sort(tempList, valueComp);
		}else{
			keyFlag = false;
			Collections.sort(tempList, suitComp);
		}
		//Value partitions
		if(keyFlag){
			tempCard=tempList.get(0);
			tempSubList = new ArrayList<Card>();
			tempSubList.add(tempCard);
			partList.add(tempSubList);
			for(int j=1; j<tempList.size(); j++){
				//check if next cards fits in last group
				if(tempList.get(j).getValue() == tempCard.getValue()){
					tempSubList.add(tempList.get(j));
				//if not create a new sublist, because tempList is sorted	
				}else {
					tempSubList = new ArrayList<Card>();
					tempCard = tempList.get(j);
					tempSubList.add(tempCard);
					partList.add(0, tempSubList);
				}
			}
		//Suit partitions	
		}else {
			tempCard=tempList.get(0);
			tempSubList = new ArrayList<Card>();
			tempSubList.add(tempCard);
			partList.add(tempSubList);
			for(int j=1; j<tempList.size(); j++){
				if(tempList.get(j).getSuit() == tempCard.getSuit()){
					tempSubList.add(tempList.get(j));
				}else {
					tempSubList = new ArrayList<Card>();
					tempCard = tempList.get(j);
					tempSubList.add(tempCard);
					partList.add(0, tempSubList);
				}
			}
		}
		return partList;
	}	

	private static ArrayList<ArrayList<Card>> genValueGroups (ArrayList<Card> cards) {
		return partition(cards,Value.ACE); //which value doesn't matter
	}

	private static ArrayList<ArrayList<Card>> genSuitGroups (ArrayList<Card> cards) {
		return partition(cards,Suit.CLUB); //which suit doesn't matter
	}

	/*
	 * ATTENTION: only for 5 cards
	 * calculates Card's Power for 5 Cards
	 * @param cards (Collection of size 5)
	 * @return a tuple representing the Card's power
	 */
	private static int[] calcCardsPower(ArrayList<Card> cards) {
		ArrayList<ArrayList<Card>> valGroups = genValueGroups(cards);
		ArrayList<ArrayList<Card>> suitGroups = genSuitGroups(cards);
		//temporary List to build the tuple
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		int[] powerList;

		//single Cards of the input set from high to low
		int[] tieBreaker = findTieBreaker(valGroups);
		
		//Checks all possible kinds of hands (big if-else-if-else... construction 
		//Flush and Straight Flush
		if(findFlush(suitGroups)){
			if(findStraight(valGroups)){ //Straight Flush
				tempList.add(0,9);
				tempList.add(1,tieBreaker[0]); //14 (Ace) for Royal Flush
			}	
			else {
				tempList.add(0, 6); //normal Flush
				//adds tie-breaker to end of list
				for(int i=0; i<tieBreaker.length; i++){
					tempList.add(tieBreaker[i]); 
				}
			}	
		}//normal Straight
		else	if(findStraight(valGroups)){
			tempList.add(0,5);
			//check special case: ACE-2-3-4-5 (lowest Straight)
			Collections.sort(cards,valueComp);
			if((cards.get(0).getValue() == Value.TWO) &&
					(cards.get(4).getValue() == Value.ACE)){
				tempList.add(5);
			}else
				tempList.add(tieBreaker[0]);
		}
		//2 value groups -> Full House or 4 of a kind
		else	if(valGroups.size() == 2){
			//first sublist, order doesn't matter (only size)
			ArrayList<Card> cardlist = valGroups.get(0);
			//4 of a kind (power: [8] - [value of 4 cards])
			if((cardlist.size()==1) || (cardlist.size()==4)){
				tempList.add(0, 8);
				for(ArrayList<Card> cl: valGroups){
					if(cl.size() == 4)
						//add the value of the "4 of a kind"
						tempList.add(cl.get(0).getValue().ordinal()+2);
				}
				//Full House (power: [7] - [3 card-value] - [pair-value])
			} else {	
				tempList.add(0, 7);
				for(ArrayList<Card> cl: valGroups){
					if(cl.size() == 3)
						//add the value of the 3 cards
						tempList.add(1,cl.get(0).getValue().ordinal()+2);
					else
						//add the value of the pair
						tempList.add(cl.get(0).getValue().ordinal()+2);
				}	
			}
		}
		//3 value groups -> two pairs or 3 of a kind
		else	if(valGroups.size() == 3){
			int j;
			ArrayList<Card> cl = valGroups.get(0); //first sublist
			for(j=1; j<3; j++)
				if(cl.size() == 1)	//single card -> take next sublist
					cl = valGroups.get(j);
				else break;			//pair or triple -> break and go on below

			//3 of a kind (power: [4] - [high single card] - [low single card])
			if(cl.size() == 3){
				tempList.add(0,4);
				//value of triple
				tempList.add(cl.get(0).getValue().ordinal()+2);
				//values of the remaining single cards
				tempList.add(tieBreaker[0]);
				tempList.add(tieBreaker[1]);
			//two pairs (power: [3] - [value high pair] - [value low pair] - [single card])
			}else	if(cl.size() == 2){
				tempList.add(0,3);
				for(ArrayList<Card> c: valGroups){
					if(c.size() > 1){
						Integer x = new Integer(c.get(0).getValue().ordinal()+2);
						if((tempList.size()>1) && (x>tempList.get(1)))
							tempList.add(1, x);		//high pair
						else
							tempList.add(x);		//low pair
					}	
				}
				tempList.add(tieBreaker[0]);
			}	
		}	
		//4 value groups -> one pair (power: [2] - [single cards]
		else	if(valGroups.size() == 4){
			tempList.add(0, 2);
			for(ArrayList<Card> c: valGroups){
				if(c.size() == 2){
					tempList.add(c.get(0).getValue().ordinal()+2);
					tempList.add(tieBreaker[0]);
					tempList.add(tieBreaker[1]);
					tempList.add(tieBreaker[2]);
				}
			}
		}
		//highest card
		else {
			ArrayList<Card> l = new ArrayList<Card>();
			tempList.add(0, 1);
			for(ArrayList<Card> c: valGroups){
				l.add(c.get(0));
			}
			Collections.sort(l,valueComp);
			Collections.reverse(l);
			for(int j=0; j<l.size(); j++){
				tempList.add(new Integer(l.get(j).getValue().ordinal()+2));
			}
		}

		//convert temporary list into array
		powerList = new int[tempList.size()];
		for(int j=0; j<tempList.size(); j++){
			powerList[j] = tempList.get(j);
		}
		return powerList;
	}

	/* 
	 * Finds the single cards in a value group list and sorts them high to low
	 * @param vg Value Groups
	 * @return Array of sorted values of single Cards (high to low)
	 */
	private static int[] findTieBreaker(ArrayList<ArrayList<Card>> vg){
		ArrayList<Integer> temp = new ArrayList<Integer>();
		int[] res;
		for(ArrayList<Card> cards: vg){
			//only single cards
			if(cards.size() == 1)
				temp.add(cards.get(0).getValue().ordinal()+2); //Card-deck starts at 2!
		}
		Collections.sort(temp);
		//from highest value to lowest
		Collections.reverse(temp);
		//convert to array
		res = new int[temp.size()];
		for(int j=0; j<temp.size(); j++){
			res[j] = temp.get(j);
		}
		return res;
	}
	
	/*
	 * @return true - Flush found, false - no Flush
	 */
	private static boolean findFlush(ArrayList<ArrayList<Card>> sg){
		if( (sg.size()==1) && (sg.get(0).size()==5))
			return true;
		else return false;
	}

	/*
	 * finds a Straight in a List of value groups of Card (size 5)
	 */
	private static boolean findStraight(ArrayList<ArrayList<Card>> vg){
		boolean res = false;
		ArrayList<Card> cards = new ArrayList<Card>();

		if(vg.size() == 5){
			for(ArrayList<Card> c: vg){
				cards.add(c.get(0));
			}
			//sort up to values
			Collections.sort(cards,valueComp);
			for(int j=0; j<cards.size()-1; j++){
				int v1,v2;
				v1 = cards.get(j).getValue().ordinal()+2;
				v2 = cards.get(j+1).getValue().ordinal()+2;
				//implicit detection of special case: ACE-2-3-4-5
				if((j==cards.size()-2) && (v1==5) && (v2==14)){
					res = true;
					break;
				}
				//looks if cards are in a row
				if((v2 - v1) == 1)
					res = true;
				else {
					res = false;
					break; //not all in a row; routine can stop
				}
			}
		} else res = false;
		return res;
	}


	/* 
	 * generates combinations set for power calculation of 6 or 7 cards
	 * @ param k - the size of the set out of which 5 have to be combined
	 * 			   k: 6 or 7 for Texas Hold'em
	 */
	private static HashSet<HashSet<Integer>>
		generate(HashSet<HashSet<Integer>> s,String prefix, String elements, int k){
			if (k == 0) {
				HashSet<Integer> set = new HashSet<Integer>();
				for(int j=0; j<prefix.length(); j++){
					set.add(Integer.parseInt(prefix.substring(j, j+1)));
				}
				s.add(set);
				return s;
			}
			for (int i = 0; i < elements.length(); i++)
				generate(s, prefix + elements.charAt(i), elements.substring(i + 1), k - 1);
			return s;
		}

	/* 
	 * calculates all subsets of size k from N elements
	 * with method generate()
	 */
	private static HashSet<HashSet<Integer>> genIndexSet(int N, int k) {
		String elements = "0123456789";
		HashSet<HashSet<Integer>> s = new HashSet<HashSet<Integer>>();
		generate(s, "", elements.substring(0, N), k);
		return s;
	}

	/**
	 * Finds the highest power in a set of 5, 6 or 7 cards
	 * @param List of 5 - 7 cards
	 * @return card power (int[])
	 * @see calcCardsPower
	 */
	public static int[] getHighestPower(ArrayList<Card> cl){
		int[] res = null;
		//switches between 5, 6 or 7 cards
		switch(cl.size()){
			case 5:
				res = calcCardsPower(cl);
				break;
			case 6:
				//go through all index sets for 6 cards
				for(HashSet<Integer> s: indexSet_6){
					ArrayList<Card> cards = new ArrayList<Card>(cl);
					int[] tempRes;
					//put together a 5-card-list
					for(int i=0; i<6; i++){
						if(!(s.contains(i)))
							cards.remove(i);
					}
					//calculate power of that 5 cards
					tempRes = calcCardsPower(cards);
					//test if the power is better than the last one
					if(res != null){
						if(powerComp.compare(res, tempRes) < 0)
							res = tempRes;
					}else res = tempRes;
				}
				break;
			//similar to 6 cards	
			case 7:	
				for(HashSet<Integer> s: indexSet_7){
					ArrayList<Card> cards = new ArrayList<Card>(cl);
					int[] tempRes;
					ArrayList<Card> c = new ArrayList<Card>();
					for(int i=0; i<7; i++){	
						if(!s.contains(i))
							c.add(cards.get(i));
					}
					cards.removeAll(c);
					tempRes = calcCardsPower(cards);
					if(res != null){
						if(powerComp.compare(res, tempRes) < 0)
							res = tempRes;
					}else res = tempRes;
				}
				break;
		}
		return res;
	}
	
	/**
	 * @return a representation of the card: [ Value, Suit ]
	 */
	@Override public String toString() {
		String str;
		if(value.ordinal() <= 8)
			str = "[" + (value.ordinal()+2) + " of " + suit + "S]";
		else
			str = "[" + value + " of " + suit + "S]";
		return str;
	}

}
