package player.AS;

import java.util.*;

public class StrategySimple implements IStrategy {
	
//	private CardStack oCardStack;

	@Override
	public int nextCard(int card, int iLastOppCard) {
//		this.oCardStack.rem_GameCard(card);
//		this.oCardStack.rem_OppCard(iLastOppCard);
		

		/**
		 * @var CardTable
		 * Table of cards (allocation of game cards to own cards)
		 */
		Map<Integer, Integer> CardTable = new HashMap<Integer, Integer>();
		CardTable.put(	-5,	1);
		CardTable.put(	-4,	2);
		CardTable.put(	-3,	3);
		CardTable.put(	-2,	4);
		CardTable.put(	-1,	5);
		CardTable.put(	1,	6);
		CardTable.put(	2,	7);
		CardTable.put(	3,	8);
		CardTable.put(	4,	9);
		CardTable.put(	5,	10);
		CardTable.put(	6,	11);
		CardTable.put(	7,	12);
		CardTable.put(	8,	13);
		CardTable.put(	9, 	14);
		CardTable.put(	10,	15);

		
//		this.oCardStack.rem_MyCard(aStrat[card]);
		return CardTable.get(card);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}
	
	@Override
	public String toString() {
		return "Simple Strategy";
	}

}
