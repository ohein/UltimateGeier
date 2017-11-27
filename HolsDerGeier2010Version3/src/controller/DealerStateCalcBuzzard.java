package controller;
public class DealerStateCalcBuzzard extends DealerStateCalc{
	//===========================================================================================
	//    	V A R I A B L E N
	//===========================================================================================
	
	public DealerStateCalcBuzzard(MainController controller){
		super();
	}
	
	
	
	//===========================================================================================
	//    	M E T H O D E N 
	//===========================================================================================
	



	@Override
	public void evaluateRound(){
		System.out.println("+++++ LOS GEHTS");
		int totalVal = drawDeck + currentBuzzardCard;
		
		/* ZWEITER TEIL: AUSWERTERN DER GESPIELTEN KARTEN ZUR PUNKTEVERGABE */
		// Wenn hier um eine Karte mit positivem Wert gespielt wird...
		if(totalVal >=0){
			/* Wenn es tatsächlich einen alleinigen Höchstbieter gibt*/
			if(gameModel.hasWinningBidder(roundCtr+1)){
				playerScore[gameModel.getMaxPlayerAtColumn((roundCtr+1))] += totalVal;
				drawDeck=0;
			}
			/* In diesem Fall konnte keiner der Spieler die Karte für sich allein gewinnen*/
			// HIER MUSS NOCH EINE ART "DrawDeck" implementiert werden
			else{
				System.out.println("Leider hat diese Karte niemand gewinnen können.");
				drawDeck = totalVal;
			}
			
		}
		// Wenn hier um eine Karte mit negativem Wert gespielt wird...
		else{
			if(gameModel.hasLoosingBidder((roundCtr+1))){
				playerScore[gameModel.getMinPlayerAtColumn(roundCtr+1)]+=totalVal;
				drawDeck=0;
			}
			else{
				// AUCH HIER MUSS WIEDER EIN DRAWSTACK IMPLEMENTIERT WERDEN!!! 
				System.out.println("Glück gehabt: In dieser Runde muss niemand eine Geierkarte schlucken");
				drawDeck = totalVal;
			}
		}		/* Abschließend müssen die Punktestände der beiden Spieler noch in das Model geschrieben werden*/
		for(int i=0;i<playerScore.length;i++){
			System.out.println("++++PLAYERSCORE: " +playerScore[i]);
			gameModel.setValueAt(String.valueOf(playerScore[i]),(i+1),sumColumn);
		}
	}

}
