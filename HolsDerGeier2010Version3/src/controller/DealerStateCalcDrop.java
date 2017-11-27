package controller;

public class DealerStateCalcDrop extends DealerStateCalc {

	//===========================================================================================
	//    	K O N S T R U K T O R ( E N )
	//===========================================================================================
	
	public DealerStateCalcDrop(MainController controller){
		super();
	}
	
	
	
	//===========================================================================================
	//    	M E T H O D E N 
	//===========================================================================================
	@Override
	public void evaluateRound(){
		System.out.println("+++++ LOS GEHTS");
		if (currentBuzzardCard >= 0){
			if(gameModel.hasWinningBidder(roundCtr+1)){
				playerScore[gameModel.getMaxPlayerAtColumn(roundCtr+1)] += currentBuzzardCard;
			}
		}
		else {
			if( gameModel.hasLoosingBidder(roundCtr+1)){
				playerScore[gameModel.getMinPlayerAtColumn(roundCtr+1)] += currentBuzzardCard;	
			}	
		}
		/* ZWEITER TEIL: AUSWERTERN DER GESPIELTEN KARTEN ZUR PUNKTEVERGABE */
		// Wenn hier um eine Karte mit positivem Wert gespielt wird...
		/* Abschließend müssen die Punktestände der beiden Spieler noch in das Model geschrieben werden*/
		for(int i=0;i<playerScore.length;i++){
			System.out.println("++++PLAYERSCORE: " +playerScore[i]);
			gameModel.setValueAt(String.valueOf(playerScore[i]),(i+1),sumColumn);
		}
	}
}
