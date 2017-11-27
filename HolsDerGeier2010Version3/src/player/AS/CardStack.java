package player.AS;

import java.util.ArrayList;

public class CardStack {
	
	/**
	 * opponent's cards
	 */
	private ArrayList<Integer> OppCards=new ArrayList<Integer>();
	
	/**
	 * own cards
	 */
	private ArrayList<Integer> MyCards=new ArrayList<Integer>();
	
	/**
	 * game cards (values -5 to 10)
	 */
	private ArrayList<Integer> GameCards=new ArrayList<Integer>();
	
	
	public CardStack() {
		this.reset();
	}
	
	
	public static void main(String[] args) {
		CardStack c = new CardStack();
		System.out.println(c);
		c.rem_MyCard(7);
		System.out.println(c);
	}
	
	/**
	 * resets object
	 */
	public void reset() {
		// prepare card lists (1-15)
		this.MyCards.clear();
		this.OppCards.clear();
		this.GameCards.clear();
    	
		int i;
    	for (i = 1; i <= 15; i++) {
    		this.add_MyCard(i);
    		this.add_OppCard(i);
    	}
    	//prepare list of game cards
    	for (i = -5; i <= 10; i++) {
    		if (i == 0) continue;
    		this.add_GameCard(i);
    	}
	}
	
	private boolean ElementInList(int element, ArrayList<Integer> list) {
		return list.contains(element);
	}
	
	private CardStack add_MyCard(int card) {
		this.MyCards.add(card);
		return this;
	}
	
	public CardStack rem_MyCard(int card) {
		if (this.ElementInList(card, this.MyCards))	this.MyCards.remove(this.MyCards.indexOf(card));
		return this;
	}
	
	public ArrayList<Integer> getMyCards() {
		return this.MyCards;
	}
	
	private CardStack add_OppCard(int card) {
		this.OppCards.add(card);
		return this;
	}
	
	public int getNearest(int card) {
//		if(this.ElementInList(card, MyCards)) return card; //if exact card is available, return it
		int i = 0;
		while(i < 30) {
			if (this.ElementInList(card + i, MyCards)) return card + i;
			if (this.ElementInList(card - i, MyCards)) return card - i;
			i++;
		}
//		if(this.ElementInList(card + i, MyCards)) return card;
//		else if (this.ElementInList(card - i, MyCards)) return card;
		return 0;
	}
	
	public CardStack rem_OppCard(int card) {
		if (this.ElementInList(card, this.OppCards)) this.OppCards.remove(this.OppCards.indexOf(card));
		return this;
	}
	
	public ArrayList<Integer> getOppCards() {
		return this.OppCards;
	}
	
	private CardStack add_GameCard(int card) {
		this.GameCards.add(card);
		return this;
	}
	
	public CardStack rem_GameCard(int card) {
		if (this.ElementInList(card, this.GameCards)) this.GameCards.remove(this.GameCards.indexOf(card));
		return this;
	}
	
	public ArrayList<Integer> getGameCards() {
		return this.GameCards;
	}
	
	@Override
	public String toString() {
		return "GameCards: "+this.GameCards+"\nMyCards: "+this.MyCards+"\nOppCards: "+this.OppCards;
	}
	
}