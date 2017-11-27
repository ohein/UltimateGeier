package player.AS;

public class StrategyLearning extends StrategySimple implements IStrategy {
	
	/**
	 * @var stack of cards (opponent / own / game value) 
	 */
	private CardStack oCardStack = new CardStack();
	
	/**
	 * @var flag for indication of first round
	 */
	private boolean bFirstRound = true;
	
	/**
	 * @var iLastGameCard member for game (value) card of last round
	 */
	private int iLastGameCard;
	
	/**
	 * construct
	 */
	public StrategyLearning() {
		for (int i = 1; i <= 5; i++) {		//remove lowest cards from standard stack
			this.oCardStack.rem_MyCard(i);	//(reserved for low gamecards)
		}
	}
	
	/**
	 * setter for iLastGameCard member
	 * @param iLastGameCard
	 * @return StrategyLearning this
	 */
	private StrategyLearning setLastGameCard(int iLastGameCard) {
		this.iLastGameCard = iLastGameCard;
		return this;
	}

	/**
	 * toggle bFirstRound member
	 * @return StrategyLearning this
	 */
	private StrategyLearning toggleFirstRoundFlag() {
		this.bFirstRound = !this.bFirstRound;
		return this;
	}

	/**
	 * return next card for game
	 * @param int card the game card
	 * @param int iLastOppCard last card played by opponent
	 * @return int next card to play
	 */
	@Override
	public int nextCard(int card, int iLastOppCard) {
		
		this.setLastGameCard(card); //store last gamecard for next round
		
		this.oCardStack
						.rem_GameCard(card)				//remove GameCard from stack
						.rem_OppCard(iLastOppCard);		//remove opponent's last card from stack 
		
		/**
		 * for first round just take the value of superclass StrategySimple (static array)
		 */
		if(this.bFirstRound) {
			this.toggleFirstRoundFlag();
			int iRet = super.nextCard(card, iLastOppCard);
			this.oCardStack.rem_MyCard(iRet);			//remove chosen card from stack
			return iRet;
		}
		
		if(card < 0) {
			//negative gamecard
			int iRet = super.nextCard(card, iLastOppCard);
			this.oCardStack.rem_MyCard(iRet);			//remove chosen card from stack
			return iRet;

		} else {
			//positive gamecard
			int iDiff = this.getLastOppDiff(iLastOppCard)+ 1;			//get difference of opponents last move
			int iRet = 0;
			if ((iDiff) < 4) {
				iRet = this.oCardStack.getNearest(card + 5 + iDiff);	//use card with opponents difference + 1
			} else {
				iRet = this.oCardStack.getNearest(card + 6);			//use card with value + 1 of actual game card
			}
			this.oCardStack.rem_MyCard(iRet);			//remove chosen card from stack
			return iRet;
		}
	}
	
	
	/**
	 * returns the difference of opponent's last move (oppcard - game value card)
	 * @return
	 */
	private int getLastOppDiff(int iOppLastCard) {
		return this.iLastGameCard - iOppLastCard;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * test values
		 */
		
//		StrategyLearning a = new StrategyLearning();
//		System.out.println(
//		a.nextCard(7, 0)+" _ 7 _ 13\n"+
//		a.nextCard(3, 13)+" _ 3 _ 7\n"+
//		a.nextCard(1, 7)+" _ 1 _ 6\n"+
//		a.nextCard(10, 6)+" _ 10 _ 14\n"+
//		a.nextCard(-4, 14)+" _ -4 _ 3\n"+
//		a.nextCard(5, 3)+" _ 5 _ 11\n"+
//		a.nextCard(-1, 11)+" _ -1 _ 2\n"+
//		a.nextCard(7, 2)+" _ 7 _ 13\n"+
//		a.nextCard(2, 13)+" _ 2 _ 8\n"+
//		a.nextCard(-3, 8)+" _ -3 _ 4\n"+
//		a.nextCard(4, 4)+" _ 4 _ 9\n"+
//		a.nextCard(-2, 9)+" _ -2 _ 5\n"+
//		a.nextCard(6, 5)+" _ 6 _ 10\n"+
//		a.nextCard(-5, 10)+" _ -5 _ 1\n"+
//		a.nextCard(8, 1)+" _ 8 _ 12\n"+
//		a.nextCard(9, 12)+"_ 9 _ 15");
	}
	
	@Override
	public String toString() {
		return "Learning Strategy";
	}

}
