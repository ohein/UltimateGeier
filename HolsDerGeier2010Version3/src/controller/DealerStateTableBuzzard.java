package controller;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;


import vultureUtil.BuzzardAnalyser;

public class DealerStateTableBuzzard extends DealerStateTable  {

	//===========================================================================================
	//    	V A R I A B L E N
	//===========================================================================================
	
	public DealerStateTableBuzzard(MainController controller){
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
	
	/** Wertet die Daten der aktuellen Runde aus und vergibt entsprechend die Punkte */
	@Override
	protected void evaluateRound() {
		System.out.println("+++++ LOS GEHTS");
		int totalVal = drawDeck + currentBuzzardCard;
		
		/* ZWEITER TEIL: AUSWERTERN DER GESPIELTEN KARTEN ZUR PUNKTEVERGABE */
		// Wenn hier um eine Karte mit positivem Wert gespielt wird...
		if(totalVal >=0){
			/* Wenn es tats�chlich einen alleinigen H�chstbieter gibt*/
			if(gameModel.hasWinningBidder(roundCtr+1)){
				playerScore[gameModel.getMaxPlayerAtColumn((roundCtr+1))] += totalVal;
				drawDeck=0;
			}
			/* In diesem Fall konnte keiner der Spieler die Karte f�r sich allein gewinnen*/
			// HIER MUSS NOCH EINE ART "DrawDeck" implementiert werden
			else{
				System.out.println("Leider hat diese Karte niemand gewinnen k�nnen.");
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
				System.out.println("Gl�ck gehabt: In dieser Runde muss niemand eine Geierkarte schlucken");
				drawDeck = totalVal;
			}
		}
		/* Abschlie�end m�ssen die Punktest�nde der beiden Spieler noch in das Model geschrieben werden*/
		for(int i=0;i<playerScore.length;i++){
			System.out.println("++++PLAYERSCORE: " +playerScore[i]);
			gameModel.setValueAt(String.valueOf(playerScore[i]),(i+1),sumColumn);
		}
	}


	

}
