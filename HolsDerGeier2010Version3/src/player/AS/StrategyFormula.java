/**
 * 
 */
package player.AS;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author doc
 *
 */
public class StrategyFormula implements IStrategy {
	
	private CardStack oCardStack = new CardStack();
	
	@Override
	public String toString() {
		return "Formula Strategy";
	}
	
	private Random oRand = new Random();
	
	public static void main(String[] args) {
		StrategyFormula formula = new StrategyFormula();
		formula.nextCard(10, 3);
	}
	
	/* (non-Javadoc)
	 * @see player.AS.IStrategy#nextCard(int, int)
	 */
	@Override
	public int nextCard(int card, int iLastOppCard) {
		
		this.oCardStack.rem_GameCard(card);			//remove game card from deck
		this.oCardStack.rem_OppCard(iLastOppCard);	//remove opponents last card from his stack
		
		ArrayList<Integer> MyCards = this.oCardStack.getMyCards();
		ArrayList<Integer> OppCards = this.oCardStack.getOppCards();
		
		System.out.println("MyCards: "+MyCards);
		
		/**
		 * @var ArrayList of float; storage for calculated results
		 */
		ArrayList<Float> aCalcResult=new ArrayList<Float>();
		//TODO store results and choose best one.
		
		
		
		//loop trough every possible combination
		for (int i = 0; i < MyCards.size(); i++) {
			int iMyCard = MyCards.get(i);
			
			for (int j = 0; j < OppCards.size(); j++) {
				int iOppCard = OppCards.get(j);
				float fCalc = 0;
				
				try {
					aCalcResult.add(fCalc = this.calculate(card, iMyCard, iOppCard)); //calculate ratio
					
					System.out.println("Ergebnis: "+fCalc+ " Karte: "+iMyCard+" Gegner: "+iOppCard);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if ((fCalc < (float) 0.2) && (fCalc > (float) 0.1)) { //use this ratio
					System.out.println("-------------------------- "+iMyCard);
					this.oCardStack.rem_MyCard(iMyCard);
					return iMyCard;
				}
			}
		}
//		System.out.println(aCalcResult);
		int iCard = this.pickRand(MyCards); //else just pick any card. good luck.
		this.oCardStack.rem_MyCard(iCard);
		return iCard;
		
		
	}
		public int pickRand(ArrayList<Integer> array) {
	        int rnd = this.oRand.nextInt(array.size());
	        return array.get(rnd);
	}
	
	private float calculate(int iValueCard, int iMyCard, int iOppCard) throws Exception {
		
		/**
		 * valuecard cannot be 0;
		 */
		if (iValueCard == 0) {
			throw new Exception("Wertekarte darf nicht 0 sein!");
		}
		
		float fResult = 0;
		
		if (iMyCard == iOppCard) {
//			System.out.println("DRAW! -- me: "+iMyCard+" opp: "+iOppCard+" Game: "+iValueCard);
		} else {
			if (iValueCard < 0) {
				//Geier
				if (iMyCard < iOppCard) {
					//won
					fResult = (iOppCard - iMyCard);
//					System.out.println("WON! -- me: "+iMyCard+" opp: "+iOppCard+" Game: "+iValueCard);
				} else {
					//lost
					fResult = (iOppCard - iMyCard);
//					System.out.println("LOST! -- me: "+iMyCard+" opp: "+iOppCard+" Game: "+iValueCard);
				}
			} else {
				//Maus
				if (iMyCard > iOppCard) {
					//won
					fResult = (iMyCard - iOppCard);
//					System.out.println("WON! -- me: "+iMyCard+" opp: "+iOppCard+" Game: "+iValueCard);
				} else {
					//lost
					fResult = (iMyCard - iOppCard);
//					System.out.println("LOST! -- me: "+iMyCard+" opp: "+iOppCard+" Game: "+iValueCard);
				}
			}
		}
		
		return fResult / iValueCard;
	}

}
