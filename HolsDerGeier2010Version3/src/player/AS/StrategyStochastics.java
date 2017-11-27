package player.AS;

public class StrategyStochastics implements IStrategy {

	@Override
	public String toString() {
		return "Stochastics Strategy";
	}
	
	
	private int negamax(int player, int depth, int alpha, int beta) throws Exception	{
		int score = Integer.MIN_VALUE;
		
		if(depth == 0) {
			throw new Exception("keine moeglichen Zuege");
		}
		
		return score;
	}


	@Override
	public int nextCard(int card, int iLastOppCard) {
		// TODO Auto-generated method stub
		return 0;
	}

}
