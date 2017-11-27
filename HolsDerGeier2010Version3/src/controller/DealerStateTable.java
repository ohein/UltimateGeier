package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import player.HolsDerGeierSpieler;
import tableDisplay.AbstractBuzzardTableDisplay;
import vultureUtil.BuzzardGameModel;
import vultureUtil.TableFactory;

public abstract class DealerStateTable extends DealerStateCalc{
	protected ReentrantLock simLock;
	protected Condition startSimulation;
	/** Liste, in der die ScrollPanes der Tabellen verwaltet werden, diese Liste wird in der 
	 *  Methode "setPanel()" initialisiert.	 */
	protected ArrayList<JScrollPane> scrollPaneList;
	protected JScrollPane paneTmp;
	protected JTable tableTmp2;
	/** Array für die JTables */
	protected ArrayList<JTable> tables;
	/** Höhe einer Zeile in der GameModel-Tabelle*/
	protected int rowHeight;
	/** Layout für das Panel (wenn gesetzt)*/
	protected GridBagLayout gbl;
	/** Die Constraints für das GridBagLayout (wenn gesetzt)*/
	protected GridBagConstraints gbc;
	protected JTable display;
	/** Frame, der die aktuelle Partie darstellt*/
	protected SimulationGUI bigPanel;
	/** Das Panel, in dem die Tabellen dargestellt werden*/
	protected JPanel scoreTablePanel;
	/** Stauchfaktor, der bei der Skalierung der Tabellen verwendet wird. */
	protected double sf = 0.60;
	/** In dieser ScrollPane werden saemtliche PlayerTable verpackt*/
	protected JScrollPane scrPaneTwo;
	protected JPanel statArea;
	protected PlayerStatPanel[] statPanels;
	/** Dieses Flag gibt an, ob bereits eine Serie vorbereitet wurde oder 
	 * nicht.	 */
	protected boolean firstSeries2BPrepared;
	protected MainController controller;
	protected ReentrantLock lock;
	protected Condition simIns;

	DealerStateTable(MainController controller) {
		super();
	}
	
	/** Simuliert eine Runde */
	@Override
	void simulateRound(){
		super.simulateRound();
	}
	
	@Override
	protected void addWinnerToSeries() {
		System.err.println("simulateRound(): Deck is Empty!");
		int winner = gameModel.getWinner();
		/* Wenn es keinen eindeutigen/alleinigen Sieger gibt, gibt es eben
		 * mehrere.			 */
		if(winner == -1){
			Integer[] winners = gameModel.getWinners();
			System.err.println("simulateRound(): Adding multiple wins!");
			for(int i=0;i<winners.length;i++){
				currentSeries.addWin(players.get(winners[i])); // Hier herrscht Redundanz 
				currentSeries.addSetWonForPlayer(players.get(winners[i])); // Hier herrscht Redundanz
				bigPanel.showNextTrophy(winners[i]);
			}
		}else{
			System.err.println("simulateRound(): Adding Single Win!");
			currentSeries.addWin(players.get(winner));
			bigPanel.showNextTrophy(winner);
		}
		// An dieser Stelle sollen für alle Spieler die errungenen Punkte in die BuzzardSeriesInfo
		// geschrieben werden.
		for(HolsDerGeierSpieler p : currentSeries.players){
			currentSeries.addPointsMade(p, gameModel.getPointsMadeForPlayer(p));
		}
	}
	
	public void setBuzzardTable(AbstractBuzzardTableDisplay buzzardTable) {
		
	}

	public void setPanel(SimulationGUI bigPanel) {
			this.bigPanel=bigPanel;
	//		bigPanel.addComponentListener(new PanelResizeListener2());
	//		mainPanel = bigPanel.getMainPanel();
	//		mainGBL = bigPanel.getMainGBL();
	//		mainGBC = bigPanel.getMainGBC();
			scoreTablePanel = bigPanel.getScoreTablePanel();
	//		scrPaneOne = bigPanel.getScrPaneOne();
			statArea = bigPanel.getStatArea();
			System.out.println("statArea uebergeben");
			System.out.println(statArea);
			scrPaneTwo = bigPanel.getScrPaneTwo();
			scrollPaneList = bigPanel.getScrollPaneList();
		}

	public void signalSimIns() {
		lock.lock();
		simIns.signal();
		lock.unlock();
	}

	public void startSimulationSignal() {
		simLock.lock();
		startSimulation.signal();
		simLock.unlock();
	}

	/** Diese Methode wird zu Beginn einer neuen Serie einmalig aufgerufen, anschließend wird für jedes neue
	 *  Spiel dieser Serie jedes Mal die Methode "prepareNewGame()" aufgerufen. */
	@Override
	public void prepareNewSeries() {
			drawDeck=0;
			roundCtr=0;
			playerCounter=0;
	
			gameCounter=0;  // Zurücksetzen des Game-Counters, der Angibt, die wie vielte Partie gespielt wird
	//		currentSeries = series.poll();
			/* Erzeugt eine Hashtable innerhalb der Serie, in der die Ergebnisse abgespeichert werden*/
			currentSeries.init();
			this.players = currentSeries.getPlayers();
			/* Der Dealer muss sich nun bei jedem Spieler registrieren, damit die Kommunikation gelingen kann*/
			for(int i=0;i<players.size();i++){
				players.get(i).register(this, playerCounter++);
			}
			numOfPlayers = players.size();
			deck = currentSeries.getDeck();
			deck.reset();
				
			Iterator<HolsDerGeierSpieler> it = players.iterator();
			while(it.hasNext()){
				HolsDerGeierSpieler spieler = it.next();
				spieler.reset();
				spieler.reset2();
			}
			
			/* Erstellen der Kartenfelder, welche für die Versorgung der Spieler mit Informationen über
			 * die gespielten Karten der Gegner verantwortlich sind.		 */
			cardsOfLastRound = new int[numOfPlayers];  // Die Karten, die in der Letzten Runde gespielt wurden
			cardsOfCurrentRound = new int[numOfPlayers]; // Die Karten, die in der aktuellen Runde gespielt wurden
			tmpCards = new int[numOfPlayers-1]; 
			/* Erstellen der Felder zur Spielverwaltung*/
			playedCardsOfPlayerX = new ArrayList<ArrayList<Integer>>();
			playerScore = new int[numOfPlayers];
			playerDisqualified = new boolean[numOfPlayers];
			
		
			/* Initialisieren aller Slots, so dass klar ist, dass hier noch keine Werte vorliegen*/
			for(int i=0;i<tmpCards.length;i++){
				tmpCards[i]=NVY; // NVY = (N)o (V)alue (Y)et
			}
			for(int i=0;i<numOfPlayers;i++){
				cardsOfLastRound[i]=NVY;
				cardsOfCurrentRound[i]=NVY;
				playedCardsOfPlayerX.add(new ArrayList<Integer>());
				playerScore[i]=0;
				playerDisqualified[i]=false;
			}
			
			/* Erstellen der ArrayList, in der die Gamemodels der Partien verwaltet werden. */
			gameModels = new ArrayList<BuzzardGameModel>();
			/* Erstellen des (ersten) GameModels, für das erste Spiel der aktuellen Serie*/
			for(int i=0;i<currentSeries.getNoOfMatches();i++){
				gameModels.add(new BuzzardGameModel(players.toArray(new HolsDerGeierSpieler[players.size()]),deck.size(),analyser));
			}
			
			
			
			
			/* Wenn die Serie auf dem Monitor dargestellt werden soll, müssen die Tables erzeugt 
			 * und auf einem Panel angeordnet werden.		 */
			if(bigPanel!=null){
				
				/* Vorbereiten der PlayerStatPanels*/
				
				/*Falls von einer vorherigen Serie Panels vorhanden sein sollten, muessen diese
				 * geloescht werden			 */
				statArea.removeAll(); 
				statArea.setLayout(new GridLayout(1,numOfPlayers));
				statPanels = new PlayerStatPanel[numOfPlayers];
				for(int i=0;i<numOfPlayers;i++){
					statPanels[i] = new PlayerStatPanel(players.get(i).getName(),currentSeries.getNoOfMatches());
					statPanels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
					statArea.add(statPanels[i]);
				}
				arrangeStatPanels();
				bigPanel.clearView();
				bigPanel.createStatPanel(currentSeries);
				
				
				/* Speichern der Anzahl der mindestens benötigten Panels */
				minNumPan=currentSeries.getNoOfMatches();
				if(!firstSeries2BPrepared){
					bigPanel.addComponentListener(new PanelResizeListener());
				}
				tables = new ArrayList<JTable>(); 
				JTable tableTmp;
				
				/* Diese Methode berechnet die Höhe der Tabellenzeilen und speichert diese in der
				 * Instanzvariable "rowHeight".			 */
				calculateRowHeight();
				
				for(int i=0;i<currentSeries.getNoOfMatches();i++){
					bigPanel.addNewTable(gameModels.get(i));
					System.out.println("JTable created");
					tableTmp = TableFactory.getJTable(gameModels.get(i));
					tableTmp.setRowHeight(rowHeight);
					tables.add(tableTmp);
					gameModels.get(i).setOwner(tables.get(i));
				}
				scoreTablePanel.removeAll();
				scrollPaneList.clear();
				gbl = new GridBagLayout();
				scoreTablePanel.setLayout(gbl);
				JScrollPane pane;
				int height, width;
				Dimension pDim;
				
				for(int i=0;i<currentSeries.getNoOfMatches();i++){
					gbc = new GridBagConstraints();
					gbc.gridheight=1;
					gbc.gridwidth=1;
					gbc.anchor=GridBagConstraints.NORTH;
					gbc.gridx=0;
					gbc.gridy=i;
					gbc.ipadx=0;
					gbc.ipady=0;
					gbc.weightx=50;
					gbc.weighty=100;
					gbc.fill=GridBagConstraints.BOTH;
				
					tableTmp2 = tables.get(i);
					pane=new JScrollPane(tableTmp2);
					scrollPaneList.add(pane);
				
					height = tableTmp2.getPreferredSize().height+20;
					width = tableTmp2.getPreferredSize().width;
					pDim = new Dimension(width,height);
					pane.setPreferredSize(pDim);
					pane.setMinimumSize(pDim);
					pane.setMaximumSize(pDim);
					
					gbl.setConstraints(pane, gbc);
					scoreTablePanel.add(pane);
					System.err.println("Table added");
	
				}
				arrangeScrollPanels();
				arrangeStatPanels();
				scoreTablePanel.revalidate();
				scoreTablePanel.repaint();
				statArea.revalidate();
				statArea.repaint();
				bigPanel.revalidate();
				bigPanel.repaint();
				bigPanel.repaintMainFrame();
				
			
				
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
	public void prepareNewGame() {
			drawDeck=0;
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

	/** Diese Methode ist fuer das richtige Skalieren der StatPanels verantwortlich, welche die
	 *  Anzahl der gewonnenen Spiele eines Spielers visualisieren
	 */
	protected void arrangeStatPanels() {
		System.out.println("+++++++++++++ ARRANGE_STAT_PANELS_()");
		int width = bigPanel.getWidth()/3;
		int height = bigPanel.getHeight()/6;
		
		Dimension dDim = new Dimension(width,height);
		for(int i=0;i<statPanels.length;i++){
			statPanels[i].setPreferredSize(dDim);
			statPanels[i].setMinimumSize(dDim);
			statPanels[i].setMaximumSize(dDim);
		}
		scrPaneTwo.setPreferredSize(new Dimension(3*width,height));
		bigPanel.revalidate();
		bigPanel.repaint();
		bigPanel.repaintMainFrame();
	}

	/** Diese Methode ist fuer das richtige Skalieren der JScrollPanes, die die JTables umschliessen, 
	 * zustaendig. Ohne diese Methode waeren die umschliessenden JScrollPanes vor allem in vertikaler 
	 * Hinsicht viel zu gross. Die umschliessenden JScrollPanes sind in der ArrayList "scrollPaneList" 
	 * gespeichert.	 */
	protected void arrangeScrollPanels() {
		System.out.println("+++++++++++++ ARRANGE_SCROLL_PANELS_()");
		int width = tables.get(0).getPreferredSize().width;
		int height = tables.get(0).getPreferredSize().height+20;
		Dimension pDim = new Dimension(width,height);
		JScrollPane tp;
		for(Iterator<JScrollPane> it = scrollPaneList.iterator();it.hasNext();){
			tp = it.next();
			tp.setPreferredSize(pDim);
			tp.setMaximumSize(pDim);
			tp.setMinimumSize(pDim);
		}
		bigPanel.revalidate();
		bigPanel.repaint();
		bigPanel.repaintMainFrame();
	}

	@Override
	public void setDisplay(JTable display) {
		this.display=display;
		if(gameModel!=null){
			gameModel.setOwner(display);
		}
	}

	@Override
	public boolean isDisplayed() {
		return !(display==null);
	}

	/** Simuliert eine komplette Serie, bis ein Spieler die geforderte Anzahl von Siegen erzielt 
	 *  hat.
	 */
	@Override
	public void simulateSeries() {
		int waitingTime = currentSeries.getWaitingTime();
		prepareNewSeries();
		simLock.lock();
		try{
			startSimulation.await();
		}catch(InterruptedException ie){
			ie.printStackTrace();
		}finally{
			simLock.unlock();
		}
		
		simulateMatch(waitingTime);
		while(!currentSeries.isFinished()){
			prepareNewGame();
			simulateMatch(waitingTime);
		}
	}

	/** Simuliert ein Match*/
	@Override
	public void simulateMatch(int waitingTime) {
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

	protected void calculateRowHeight() {
		int tHeight = bigPanel.getHeight();
		rowHeight = (20);
	}



	//===========================================================================================
	//    	I N N E R E    K L A S S E N
	//===========================================================================================
	/** Sollte das Fenster vergrößert oder verkleinert werden, so müssen auch die Tabellendarstellungen
	 *  entsprechend angepasst werden. 	 */
	class PanelResizeListener extends ComponentAdapter{
		@Override
		public void componentResized(ComponentEvent e){

			for(int i=0;i<5;i++){
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++"+scoreTablePanel.getHeight());
			}
			if(players!=null&&tables!=null){
				if(players.size()!=0&&tables.size()!=0){
					JTable curTab;
					calculateRowHeight();

			
					Iterator<JTable> iterator = tables.iterator();
					while(iterator.hasNext()){
						curTab=iterator.next();
						curTab.setRowHeight(rowHeight);
						curTab.validate();
						curTab.repaint();
					}
					/* Diese Methode sorgt dafuer, dass die die JTables umgebenden JScrollPanes
					 * korrekt dimensioniert sind.
					 */
					arrangeScrollPanels();
					arrangeStatPanels();
				}
			}
		}
	}
}
