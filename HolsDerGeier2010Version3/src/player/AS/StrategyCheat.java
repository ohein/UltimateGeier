package player.AS;

public class StrategyCheat implements IStrategy {
	
//	private CardStack oCardStack;
	private int firstCard = 0;
	

	@Override
	public int nextCard(int card, int iLastOppCard) {
//		this.oCardStack.rem_GameCard(card);
//		this.oCardStack.rem_OppCard(iLastOppCard);
		
		
		
		int[] aStrat = new int[16];
		
		card = card + 5;
		aStrat[0] = 1;
		aStrat[1] = 2;
		aStrat[2] = 3;
		aStrat[3] = 5;
		aStrat[4] = 4;
		aStrat[6] = 6;
		aStrat[7] = 9;
		aStrat[8] = 7;
		aStrat[9] = 8;
		aStrat[10] = 13;
		aStrat[11] = 11;
		aStrat[12] = 14;
		aStrat[13] = 12;
		aStrat[14] = 15;
		aStrat[15] = 10;
		
		//remember first card to cheat
		if (this.firstCard == 0) {
			this.firstCard = aStrat[card];
		} else if (card < 0) { //cheat if gamecard is negative
			return this.firstCard;
		}
		
		return aStrat[card];
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}
	
	@Override
	public String toString() {
		return "Cheater Strategy";
	}

}
