package controller;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;

import vultureUtil.BuzzardAnalyser;

public class DealerStateTableDrop extends DealerStateTable {
	public DealerStateTableDrop(MainController controller){
		super(controller);
		this.controller=controller;
		analyser = new BuzzardAnalyser();
		lock = new ReentrantLock();
		simIns = lock.newCondition();
		playedCardsOfPlayerX = new ArrayList<ArrayList<Integer>>();
		series = new PriorityQueue<BuzzardSeriesInfo>();
		firstSeries2BPrepared=true;
		simLock = new ReentrantLock();
		startSimulation = simLock.newCondition();
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
