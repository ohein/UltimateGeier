package controller;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;

import vultureUtil.BuzzardAnalyser;

public class DealerStateTableCinderella extends DealerStateTable {
	int mouseDrawDeck;
	int buzzardDrawDeck;
	
	
	public DealerStateTableCinderella(MainController controller){
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
	
	
	@Override
	public void evaluateRound(){
		System.out.println("+++++ LOS GEHTS");
		int totalVal = drawDeck + currentBuzzardCard;
		if (currentBuzzardCard >= 0){
			if(gameModel.hasWinningBidder(roundCtr+1)){
				playerScore[gameModel.getMaxPlayerAtColumn(roundCtr+1)] += currentBuzzardCard;
				if(mouseDrawDeck!=0){
					playerScore[gameModel.getMaxPlayerAtColumn(roundCtr+1)]+=currentBuzzardCard;
					mouseDrawDeck=0;
				}
				if(buzzardDrawDeck!=0 && gameModel.hasLoosingBidder(roundCtr+1)){
					playerScore[gameModel.getMinPlayerAtColumn(roundCtr+1)]+= buzzardDrawDeck;
					buzzardDrawDeck=0;
				}
			}else{
				mouseDrawDeck+=currentBuzzardCard;
			}
		}
		else {
			if( gameModel.hasLoosingBidder(roundCtr+1)){
				playerScore[gameModel.getMinPlayerAtColumn(roundCtr+1)] += currentBuzzardCard;
				if(buzzardDrawDeck!=0){
					playerScore[gameModel.getMinPlayerAtColumn(roundCtr+1)] += buzzardDrawDeck;
					buzzardDrawDeck = 0;
				}
				if(mouseDrawDeck!=0 && gameModel.hasLoosingBidder(roundCtr+1)){
					playerScore[gameModel.getMaxPlayerAtColumn(roundCtr+1)] += mouseDrawDeck;
					mouseDrawDeck = 0;
				}
			}
			else{
				buzzardDrawDeck += currentBuzzardCard;
			}
		}

		/* Abschließend müssen die Punktestände der beiden Spieler noch in das Model geschrieben werden*/
		for(int i=0;i<playerScore.length;i++){
			System.out.println("++++PLAYERSCORE: " +playerScore[i]);
			gameModel.setValueAt(String.valueOf(playerScore[i]),(i+1),sumColumn);
		}
	}

}
