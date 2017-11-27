package controller;

import java.awt.GridBagConstraints;
import java.util.Iterator;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import player.HolsDerGeierSpieler;
import tableDisplay.AbstractBuzzardTableDisplay;
import vultureUtil.BuzzardGameModel;
import vultureUtil.TableFactory;


public class DealerStateFullBuzzard extends DealerStateTableBuzzard{
	
	//===========================================================================================
	//    	V A R I A B L E N
	//===========================================================================================
	
	
	
	//-PRIVATE-----------------------------------------------------------------------------------
	
	/** 
	 * Der Dialog, der fuer die Darstellung der Karten und des Spielablaufs zu-
	 * staendig ist.
	 */
	private AbstractBuzzardTableDisplay buzzardTable;
	
	
	
	//===========================================================================================
	//    	K O N S T R U K T O R ( E N )
	//===========================================================================================
	
	public DealerStateFullBuzzard(MainController controller){
		super(controller);
//		this.controller=controller;
//		analyser = new BuzzardAnalyser();
//		lock = new ReentrantLock();
//		simIns = lock.newCondition();
//		playedCardsOfPlayerX = new ArrayList<ArrayList<Integer>>();
//		series = new PriorityQueue<BuzzardSeriesInfo>();
//		firstSeries2BPrepared=true;
	}
	
	
	
	//===========================================================================================
	//    	M E T H O D E N 
	//===========================================================================================
	
	@Override
	public void setBuzzardTable(AbstractBuzzardTableDisplay buzzardTable){
		this.buzzardTable = buzzardTable;
	}
	
	
	
	@Override
	public void signalSimIns(){
		lock.lock();
		simIns.signal();
		lock.unlock();
	}
	
	
	
	/** Diese Methode wird zu Beginn einer neuen Serie einmalig aufgerufen, anschließend wird für jedes neue
	 *  Spiel dieser Serie jedes Mal die Methode "prepareNewGame()" aufgerufen. */
	@Override
	public void prepareNewSeries(){
		super.prepareNewSeries();
		{

			/*
			 * Die Spieler muessen nun noch an die Tische gesetzt werden
			 */
			Iterator<HolsDerGeierSpieler> i = players.iterator();
			while(i.hasNext()){
				buzzardTable.seatPlayer(i.next());
			}
			// Auch die Kartenausgabe soll sichtbar gemacht werden
			buzzardTable.setVisible(true);
			
		}	// Wie gesagt, nur wenn die Tabellen auf dem Monitor dargestellt werden sollen.	
		
		
		gameModel=gameModels.get(gameCounter);
		
		/* Die letze Spalte des GameModels ist gleichzeitig die Spalte, in die die 
		 * Gesamtergebnisse geschrieben werden sollen*/
		sumColumn = gameModel.getColumnCount()-1;
		firstSeries2BPrepared=false;


	}
	

	
	/** Diese Methode wird ab dem zweiten Spiel einer Serie aufgerufen, da dann lediglich die 
	 *  entsprechenden Felder zurückzusetzen sind	 */
	@Override
	public void prepareNewGame(){
		gameCounter++;
		roundCtr=0;
		deck.reset();
		/* Wenn der gameCounter die Zahl der zum Sieg benötigten Partien übersteigt, sind die ursprünglich 
		 * angelegten GameModel und Tabellen nicht ausreichend und es muss mindestens ein zusätzliches GameModel
		 * und ggf. auch eine zusätzliche Tabelle angelegt werden. (Erreicht der GameCounter einmal die Höhe
		 * der Best-Of-X Serie, geschieht das Anlegen eines neuen GameModels und ggf. einer neuen Tabelle fortan
		 * mit jedem Aufruf dieser Methode. */
		if(gameCounter>=currentSeries.getNoOfMatches()){
			gameModel = new BuzzardGameModel(players.toArray(new HolsDerGeierSpieler[players.size()]),deck.size(),analyser);
			gameModels.add(gameModel);
			if(scoreTablePanel!=null){
				
				/* Die Zahl der mindestens benötigten GameModels und Tabellen erhöhrt sich (diese wird für 
				 * die Berechnung der Zeilenhöhe einer Tabelle benötigt*/
				minNumPan++; 
				/* Die Zeilenhöhe der Tabellen muss neu berechnet und gesetzt werden*/
				calculateRowHeight();
				
				System.err.println("JTable created");
				JTable table = TableFactory.getJTable(gameModel);
				System.err.println("JTable added");
				tables.add(table);
				
				Iterator<JTable> iterator = tables.iterator();
				while(iterator.hasNext()){
					iterator.next().setRowHeight(rowHeight);
				}
				
				gameModel.setOwner(table);
				JScrollPane sp = new JScrollPane(table);
				scrollPaneList.add(sp);
//				int width;
//				int height;
//				width=table.getPreferredSize().width;
//				height=table.getMinimumSize().height;
//				Dimension pDim = new Dimension(width,height);
//				sp.setPreferredSize(pDim);
//				sp.setMinimumSize(pDim);
//				sp.setMaximumSize(pDim);
				arrangeScrollPanels();
			
				gbc = new GridBagConstraints();
				gbc.gridx=0;
				gbc.gridy=gameCounter;
				gbc.weightx=50;
				gbc.weighty=50;
				gbc.fill=GridBagConstraints.BOTH;
				gbl.setConstraints(sp, gbc);
				scoreTablePanel.add(sp);
			}
			arrangeScrollPanels();
			arrangeStatPanels();
			bigPanel.validate();
			bigPanel.repaint();
			bigPanel.repaintMainFrame();
			
		}
		else{
			/* Das neue "aktuelle" GameModel aus der ArrayList beziehen und damit weiterarbeiten.*/
			gameModel = gameModels.get(gameCounter);
		}
		for(int i=0;i<numOfPlayers;i++){
			playedCardsOfPlayerX.get(i).clear(); // Kein Spieler hat am Anfang eine Karte gespielt
			if(i!=(numOfPlayers-1)){
				tmpCards[i]=NVY;
			}
			cardsOfLastRound[i]=NVY;
			cardsOfCurrentRound[i]=NVY;
			playerScore[i]=0;
			playerDisqualified[i]=false;
			players.get(i).reset();
		}
		
	}
	

	
	@Override
	public void addSeries(BuzzardSeriesInfo series){
		this.series.add(series);
	}
	
	@Override
	public boolean isGameOver(){
		return deck.isEmpty();
	}
	

	
	@Override
	public boolean isDisplayed(){
		return !(display==null);
	}
	
	
	/** Methode, um die im letzen Zug gespielte Karte eines Spielers abzufragen.
	 *  @param player Nummer des Spielers, dessen Karte in Erfahrung gebracht werden soll.
	 * */
	@Override
	public int getLastCardOf(int player){
		if(player>=0&&player<numOfPlayers){
			return cardsOfLastRound[player];
		}else{
			return PDE;
		}
	}
	
	/** Methode, mit der die Karten des letzen Zuges von sämtlichen Gegnern abgefragt werden können
	 *  @param player Nummer des Spielers, der die Anfrage stellt, da seine letze Karte nicht im Ergebnis vorkommen soll*/ 
	@Override
	public int[] getSetOfLastCards(int player){
		int cnt=0;
		for(int i=0;i<numOfPlayers;i++){
			if(i!=player){
				tmpCards[cnt++]=cardsOfLastRound[i];
			}
		}
		return tmpCards;
	}
	
	/** Simuliert eine komplette Serie, bis ein Spieler die geforderte Anzahl von Siegen erzielt 
	 *  hat.
	 */
	@Override
	public void simulateSeries(){
		int waitingTime = currentSeries.getWaitingTime();
		prepareNewSeries();
		simulateMatch(waitingTime);
		while(!currentSeries.isFinished()){
			prepareNewGame();
			simulateMatch(waitingTime);
		}
		buzzardTable.clearTable();
	}

	
	/** Simuliert ein Match*/
	@Override
	public void simulateMatch(int waitingTime){
		while(!isGameOver()){
			simulateRound();
			try{
				Thread.sleep(waitingTime);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		System.err.println("simulateMatch(): Game is over");
	}
	
	/** Convenience-Methode, die den Thread fuer eine bestimmte Zeit 
	 *  schalfen legt.
	 * @param millis Zeitdauer, die der Thread schlafen soll.
	 */
	private void goToSleep(int millis){
		try{
			Thread.sleep(millis);
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	
	/** Simuliert eine Runde */
	@Override
	void simulateRound(){
		/* ERSTER TEIL: ENTGEGENNEHMEN UND EINTRAGEN DER GESPIELTEN (UND GEZOGENEN) KARTEN*/
		currentBuzzardCard = deck.getNextCard();  // Ziehen der nächsten Gewinnkarte
		gameModel.setValueAt(String.valueOf(currentBuzzardCard), 0, (roundCtr+1)); // Eintragen der gezogenen Gewinnkarte ins GameModel
		updateInfoForPlayers();
		
		buzzardTable.deckShowNext(currentBuzzardCard);
		goToSleep(800);
		
		/* Abarbeiten der einzelnen Spieler, das heißt, entgegennehmen ihrer gespielten Karten unter vorheriger Prüfung der Nicht-
		 * disqualifikationsbedingung sowie Prüfung der gespielten Karte auf Gültigkeit */
		for(int i=0;i<numOfPlayers;i++){
			if(!playerDisqualified[i]){  // Ist der Spieler noch spielberechtigt? Wenn ja, dann...
				currentPlayerCard=players.get(i).gibKarte(currentBuzzardCard);
				/* Wenn der Spieler nicht versucht zu schummeln, dann...*/
				if(!playedCardsOfPlayerX.get(i).contains(currentPlayerCard)&&isCardValid(currentPlayerCard)){
					cardsOfCurrentRound[i]=currentPlayerCard;
					playedCardsOfPlayerX.get(i).add(currentPlayerCard);
					gameModel.setValueAt(String.valueOf(currentPlayerCard),(i+1),(roundCtr+1));
					buzzardTable.showCardBack(players.get(i), currentPlayerCard);
					goToSleep(800);
				}
				/* Sollte er doch versuchen zu mogeln, dann... */
				else{
					System.err.println("Player " + i + " played invalid card (" +currentPlayerCard + ")");
					playerDisqualified[i]=true;
					cardsOfCurrentRound[i]=DISQUALIFIED;
					gameModel.setValueAt("X",(i+1),(roundCtr+1));
					goToSleep(800);
				}
			}else{ // Sollte der Spieler nicht mehr spielberechtigt, also disqualifiziert sein...
				cardsOfCurrentRound[i]=DISQUALIFIED;
				gameModel.setValueAt("X",(i+1),(roundCtr+1));
				goToSleep(800);
			}
		}
		buzzardTable.showAllCards();
		goToSleep(1500);
		/* ZWEITER TEIL: AUSWERTERN DER GESPIELTEN KARTEN ZUR PUNKTEVERGABE */
		// Wenn hier um eine Karte mit positivem Wert gespielt wird...
		if(currentBuzzardCard>=0){
			/* Wenn es tatsächlich einen alleinigen Höchstbieter gibt*/
			if(gameModel.hasWinningBidder(roundCtr+1)){
				playerScore[gameModel.getMaxPlayerAtColumn((roundCtr+1))] += currentBuzzardCard;
				HolsDerGeierSpieler winningPlayer = players.get(gameModel.getMaxPlayerAtColumn(roundCtr+1));
				Iterator<HolsDerGeierSpieler> it = players.iterator();
				HolsDerGeierSpieler playerTmp;
				while(it.hasNext()){
					playerTmp = it.next();
					if(!playerTmp.equals(winningPlayer)){
						buzzardTable.clearCardOfPlayer(playerTmp);
					}
				}
				goToSleep(500);
				buzzardTable.startScoreAnimation(winningPlayer, currentBuzzardCard);
				buzzardTable.clearCardOfPlayer(winningPlayer);
				goToSleep(500);
			}
			/* In diesem Fall konnte keiner der Spieler die Karte für sich allein gewinnen*/
			// HIER MUSS NOCH EINE ART "DrawDeck" implementiert werden
			else{
				System.out.println("Leider hat diese Karte niemand gewinnen können.");
				buzzardTable.startDrawAnimation();
				Iterator<HolsDerGeierSpieler> it = players.iterator();
				HolsDerGeierSpieler playerTmp;
				while(it.hasNext()){
					playerTmp = it.next();
					buzzardTable.clearCardOfPlayer(playerTmp);
				}
				goToSleep(500);
			}
			
		}
		// Wenn hier um eine Karte mit negativem Wert gespielt wird...
		else{
			if(gameModel.hasLoosingBidder((roundCtr+1))){
				HolsDerGeierSpieler loosingPlayer = players.get(gameModel.getMinPlayerAtColumn(roundCtr+1));
				playerScore[gameModel.getMinPlayerAtColumn(roundCtr+1)]+=currentBuzzardCard;
				
				Iterator<HolsDerGeierSpieler> it = players.iterator();
				HolsDerGeierSpieler playerTmp;
				while(it.hasNext()){
					playerTmp = it.next();
					if(!playerTmp.equals(loosingPlayer)){
						buzzardTable.clearCardOfPlayer(playerTmp);
					}
				}
				goToSleep(500);
				buzzardTable.startScoreAnimation(loosingPlayer, currentBuzzardCard);
				buzzardTable.clearCardOfPlayer(loosingPlayer);
				goToSleep(500);
			}
			else{
				// AUCH HIER MUSS WIEDER EIN DRAWSTACK IMPLEMENTIERT WERDEN!!! 
				System.out.println("Glück gehabt: In dieser Runde muss niemand eine Geierkarte schlucken");
				buzzardTable.startDrawAnimation();
				Iterator<HolsDerGeierSpieler> it = players.iterator();
				HolsDerGeierSpieler playerTmp;
				while(it.hasNext()){
					playerTmp = it.next();
					buzzardTable.clearCardOfPlayer(playerTmp);
				}
				goToSleep(500);
			}
		}
		/* Abschließend müssen die Punktestände der beiden Spieler noch in das Model geschrieben werden*/
		for(int i=0;i<numOfPlayers;i++){
			gameModel.setValueAt(String.valueOf(playerScore[i]),(i+1),sumColumn);
		}
		
		/* Überprüfen, ob das Spiel beendet ist, und ggf. den oder die Sieger in die SeriesInfo
		 * zurückschreiben
		 */
		if(deck.isEmpty()){
			System.err.println("simulateRound(): Deck is Empty!");
			int winner = gameModel.getWinner();
			/* Wenn es keinen eindeutigen/alleinigen Sieger gibt, gibt es eben
			 * mehrere.			 */
			if(winner == -1){
				Integer[] winners = gameModel.getWinners();
				System.err.println("simulateRound(): Adding multiple wins!");
				for(int i=0;i<winners.length;i++){
					currentSeries.addWin(players.get(winners[i]));
					bigPanel.showNextTrophy(winners[i]);
				}
			}else{
				System.err.println("simulateRound(): Adding Single Win!");
				currentSeries.addWin(players.get(winner));
				bigPanel.showNextTrophy(winner);
			}
			roundCtr++;
		}
		else{
			roundCtr++;
		}
	}
	

	
	@Override
	protected void calculateRowHeight(){
		int tHeight = bigPanel.getHeight();
		rowHeight = (20);
	}
	
	

}
