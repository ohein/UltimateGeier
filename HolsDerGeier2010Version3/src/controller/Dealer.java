package controller;

import tableDisplay.AbstractBuzzardTableDisplay;
import vultureUtil.*;
import player.*;

import java.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.locks.*;
@Deprecated 
public class Dealer {
	
	//===========================================================================================
	//    	V A R I A B L E N
	//===========================================================================================
	
	//-PUBLIC------------------------------------------------------------------------------------
	public static final int DISQUALIFIED = -1000;
	public static final int NVY = -1001; // NVY == "No Value Yet"
	public static final int PDE = -1002; // PDE == "Player does not exist"
	
	
	//-PRIVATE-----------------------------------------------------------------------------------
	private BuzzardAnalyser analyser;
	
	private ArrayList<HolsDerGeierSpieler> players;
	
	/** Hier werden die bereits gespielten Karten der Spieler verwaltet*/
	private ArrayList<ArrayList<Integer>> playedCardsOfPlayerX;
	
	/** Hier werden die Punktestaende der Spieler gespeichert */
	private int[] playerScore;
	
	/** In diesem Feld wird vermerkt, ob ein Spieler disqualifiziert oder noch spielberechtigt ist */
	private boolean[] playerDisqualified;
	
	/** Der Gewinnkartenstapel*/
	private BuzzardDeck deck;
	
	private BuzzardGameModel gameModel;
	
	/** Zaehler, welche die aktuelle Rundennummer vermerkt */
	private int roundCtr;
	
	private Queue<BuzzardSeriesInfo> series;
	
	private BuzzardSeriesInfo currentSeries;
	
	/** Liste in der die ScrollPanes der Tabellen verwaltet werden, diese Liste wird in der 
	 *  Methode "setPanel()" initialisiert.	 */
	private ArrayList<JScrollPane> scrollPaneList;
	
	private JScrollPane paneTmp;
	
	private JTable tableTmp2;
	
	//=========
	
	/** Die Zeit, die zwischen zwei Runden verstreichen soll*/
	private int waitingTime;
	
	/** Array für die JTables */
	private ArrayList<JTable> tables;
	
	/** Array für die GameModels*/
	private ArrayList<BuzzardGameModel> gameModels;
	
	/** Zaehler, der die Nummer des Spieles der aktuellen Serie (beginend bei 0!) widergibt*/
	private int gameCounter;
	
	/** Höhe einer Zeile in der GameModel-Tabelle*/
	private int rowHeight;
	
	//=========
	
	/** Zaehler fuer die Spielernummern */
	private int playerCounter;
	
	/** Layout für das Panel (wenn gesetzt)*/
	private GridBagLayout gbl;
	
	/** Die Constraints für das GridBagLayout (wenn gesetzt)*/
	private GridBagConstraints gbc;
	
	/** Anzahl der an der aktuellen Serie partizipierenden Spieler */
	private int numOfPlayers;
	
	/** Nummer der Spalte des GameModels, in die die Punktesummen der Spieler geschrieben werden */
	private int sumColumn;
	
	/** Die aktuelle Gewinnkarte, um die gespielt wird. Eine Gewinnkarte kann einen positiven Wert (Mäusekarte) oder einen 
	 *  negativen Wert (Geierkarte) besitzen.	 */
	private int currentBuzzardCard;
	
	
	/** In diesem Array werden die Karten der Spieler, welche diese in der letzen Runde spielten 
	 *  gespeichert. Dieses Array wird bei den Methoden "letzeKarte()" und "letzteKarte(int spieler)"
	 *  verwendet.
	 */
	private int[] cardsOfLastRound;
	
	/** In diesem Array werden die Karten der Spieler, welche diese in der aktuellen Runde spielen,
	 *  gespeichert. 	 */
	private int[] cardsOfCurrentRound;
	
	   
	/** Dieses Array dient der Methode "letzeKarte()" - Hierbei werden die Karten der letzten Runde
	 *  mit Ausnahme der Karte des anfragenden Spielers in diesem Array gespeichert. */
	private int[] tmpCards;
	
	private int currentPlayerCard;
	
	private JTable display;
	
	/** Frame, der die aktuelle Partie darstellt*/
	private SimulationGUI bigPanel;
	
	
	/** Das mainPanel */
	private JPanel mainPanel;
	
	private GridBagLayout mainGBL;
	
	private GridBagConstraints mainGBC;
	
	
	/** Das Panel, in dem die Tabellen dargestellt werden*/
	private JPanel scoreTablePanel;
	
	/** Anzahl der Mindestens darzustellenden GamePanels*/
	private int minNumPan;
	
	
	/** Stauchfaktor, der bei der Skalierung der Tabellen verwendet wird. */
	private double sf = 0.60;
	
	/** In dieser ScrollPane sind saemtliche PlayerStatPanels verpackt*/
	private JScrollPane scrPaneOne;
	
	/** In dieser ScrollPane werden saemtliche PlayerTable verpackt*/
	private JScrollPane scrPaneTwo;
	
	/** 
	 * Der Dialog, der fuer die Darstellung der Karten und des Spielablaufs zu-
	 * staendig ist.
	 */
	private AbstractBuzzardTableDisplay buzzardTable;
	
	//--------------------------------------------------------------------------------------------
	// V a r i a b l e n  p l a y e r P a n e l
	//--------------------------------------------------------------------------------------------
	private JPanel statArea;
	private PlayerStatPanel[] statPanels;
	
	/** Dieses Flag gibt an, ob bereits eine Serie vorbereitet wurde oder 
	 * nicht.	 */
	private boolean firstSeries2BPrepared;
	
	private MainController controller;
	
	private ReentrantLock lock;
	private Condition simIns;
	
	
	//===========================================================================================
	//    	K O N S T R U K T O R ( E N )
	//===========================================================================================
	
	public Dealer(MainController controller){
		this.controller=controller;
		analyser = new BuzzardAnalyser();
		lock = new ReentrantLock();
		simIns = lock.newCondition();
		playedCardsOfPlayerX = new ArrayList<ArrayList<Integer>>();
		series = new PriorityQueue<BuzzardSeriesInfo>();
		firstSeries2BPrepared=true;
	}
	
	
	
	//===========================================================================================
	//    	M E T H O D E N 
	//===========================================================================================
	
	public void setBuzzardTable(AbstractBuzzardTableDisplay buzzardTable){
		this.buzzardTable = buzzardTable;
	}
	
	public void setPanel(SimulationGUI bigPanel){
		this.buzzardTable=buzzardTable;
		this.bigPanel=bigPanel;
		this.bigPanel.setLayout(new BorderLayout());
//		bigPanel.addComponentListener(new PanelResizeListener2());
		mainPanel = new JPanel();
		mainGBL = new GridBagLayout();
		mainPanel.setLayout(mainGBL);
		mainGBC = new GridBagConstraints();
		
		scoreTablePanel = new JPanel();
		scrPaneOne = new JScrollPane(scoreTablePanel);
		mainGBC.gridx=0;
		mainGBC.gridy=1;
		mainGBC.gridwidth=1;
		mainGBC.gridheight=1;
		mainGBC.fill=GridBagConstraints.BOTH;
		mainGBC.weightx=100;
		mainGBC.weighty=75;

		mainGBL.setConstraints(scrPaneOne,mainGBC);
		mainPanel.add(scrPaneOne);
		
		statArea = new JPanel(new GridLayout(3,3));
		
		mainGBC = new GridBagConstraints();
		mainGBC.gridx=0;
		mainGBC.gridy=0;
		mainGBC.gridwidth=1;
		mainGBC.gridheight=1;
		mainGBC.fill=GridBagConstraints.BOTH;
		mainGBC.weightx=100;
		mainGBC.weighty=25;
		scrPaneTwo = new JScrollPane(statArea);
		mainGBL.setConstraints(scrPaneTwo,mainGBC);
		mainPanel.add(scrPaneTwo);
		
		
		bigPanel.add(BorderLayout.CENTER,mainPanel);
		scrollPaneList = new ArrayList<JScrollPane>();
	}
	
	public void signalSimIns(){
		lock.lock();
		simIns.signal();
		lock.unlock();
	}
	
	void simulateNext(){
		Thread t1 = new Thread(){
			@Override
			public void run(){
				currentSeries = series.poll();
				System.out.println(currentSeries.isTournament());
				System.out.println(currentSeries instanceof BuzzardTournamentInfo);
				if(currentSeries instanceof BuzzardTournamentInfo){
					System.out.println("Ich simuliere ein Turnier!");
					ArrayList<HolsDerGeierSpieler> tmpPlayerList;
					BuzzardTournamentInfo tournament =(BuzzardTournamentInfo)currentSeries;
					tournament.generateTournament();
					while(tournament.hasNextMatchUp()){
						controller.displaySimulationGUI();
						System.err.println("Requesting next MatchUp");
						currentSeries = tournament.getNextMatchUp();
						System.err.println("Next MatchUp received");
						prepareNewSeries();
						System.err.println("New Series Prepared");
						System.err.println("Requesting simulation");
						simulateMatch(1000);
						System.err.println("Match Simulated");
						while(!currentSeries.isFinished()){
							prepareNewGame();
							System.err.println("New Game Prepared");
							simulateMatch(1000);
							System.err.println("Match Simulated");
						}
						System.err.println("CurrentSeries Finished");
						tmpPlayerList = currentSeries.getWinnerOfSeries();
						System.err.println("Winner of Series received");
						if(tmpPlayerList.size()==1){
							System.err.println("Adding single winner");
							tournament.getTournamentTableDataClass().addWin(tmpPlayerList.get(0));
						}else if(tmpPlayerList.size()>1){
							System.err.println("Adding draw players");
							Iterator<HolsDerGeierSpieler> it = tmpPlayerList.iterator();
							while(it.hasNext()){
								tournament.getTournamentTableDataClass().addDraw(it.next());
							}
						}
						lock.lock();
						try{
							
							controller.displayTournamentStanding(tournament.getTournamentTableDataClass());
							simIns.await();
						}
						catch(InterruptedException ie){
							ie.printStackTrace();
						}finally{
							lock.unlock();
						}
					}
				}
				else{
					System.out.println("Ich simuliere kein Turnier");
					prepareNewSeries();
					simulateMatch(1000);
					System.out.println(currentSeries.isFinished());
					while(!currentSeries.isFinished()){
						prepareNewGame();
						simulateMatch(1000);
					}
				}

			}
		};
		t1.start();
	}
	
	/** Diese Methode wird zu Beginn einer neuen Serie einmalig aufgerufen, anschließend wird für jedes neue
	 *  Spiel dieser Serie jedes Mal die Methode "prepareNewGame()" aufgerufen. */
	public void prepareNewSeries(){
		roundCtr=0;
		playerCounter=0;

		gameCounter=0;  // Zurücksetzen des Game-Counters, der Angibt, die wie vielte Partie gespielt wird
//		currentSeries = series.poll();
		/* Erzeugt eine Hashtable innerhalb der Serie, in der die Ergebnisse abgespeichert werden*/
		currentSeries.init();
		this.players = currentSeries.getPlayers();
		/* Der Dealer muss sich nun bei jedem Spieler registrieren, damit die Kommunikation gelingen kann*/
		for(int i=0;i<players.size();i++){
		//	players.get(i).register(this, playerCounter++);
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
				System.err.println("JTable created");
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
				
//				height = tableTmp2.getPreferredSize().height+20;
//				width = tableTmp2.getPreferredSize().width;
//				pDim = new Dimension(width,height);
//				pane.setPreferredSize(pDim);
//				pane.setMinimumSize(pDim);
//				pane.setMaximumSize(pDim);
				
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
			
			// Auch die Kartenausgabe soll sichtbar gemacht werden
			buzzardTable.setVisible(true);
			/*
			 * Die Spieler muessen nun noch an die Tische gesetzt werden
			 */
			Iterator<HolsDerGeierSpieler> i = players.iterator();
			while(i.hasNext()){
				buzzardTable.seatPlayer(i.next());
			}
			
		}	// Wie gesagt, nur wenn die Tabellen auf dem Monitor dargestellt werden sollen.	
		
		
		gameModel=gameModels.get(gameCounter);
		
		/* Die letze Spalte des GameModels ist gleichzeitig die Spalte, in die die 
		 * Gesamtergebnisse geschrieben werden sollen*/
		sumColumn = gameModel.getColumnCount()-1;
		firstSeries2BPrepared=false;


	}
	

	
	/** Diese Methode wird ab dem zweiten Spiel einer Serie aufgerufen, da dann lediglich die 
	 *  entsprechenden Felder zurückzusetzen sind	 */
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
	
	/** Diese Methode ist fuer das richtige Skalieren der StatPanels verantwortlich, welche die
	 *  Anzahl der gewonnenen Spiele eines Spielers visualisieren
	 */
	private void arrangeStatPanels(){
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
	private void arrangeScrollPanels(){
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
	
	public void addSeries(BuzzardSeriesInfo series){
		this.series.add(series);
	}
	
	public boolean isGameOver(){
		return deck.isEmpty();
	}
	
	public void setDisplay(JTable display){
		this.display=display;
		if(gameModel!=null){
			gameModel.setOwner(display);
		}
	}
	
	public boolean isDisplayed(){
		return !(display==null);
	}
	
	private void updateInfoForPlayers(){
		for(int i=0;i<cardsOfLastRound.length;i++){
			cardsOfLastRound[i]=cardsOfCurrentRound[i];
		}
	}
	
	/** Methode, um die im letzen Zug gespielte Karte eines Spielers abzufragen.
	 *  @param player Nummer des Spielers, dessen Karte in Erfahrung gebracht werden soll.
	 * */
	public int getLastCardOf(int player){
		if(player>=0&&player<numOfPlayers){
			return cardsOfLastRound[player];
		}else{
			return PDE;
		}
	}
	
	/** Methode, mit der die Karten des letzen Zuges von sämtlichen Gegnern abgefragt werden können
	 *  @param player Nummer des Spielers, der die Anfrage stellt, da seine letze Karte nicht im Ergebnis vorkommen soll*/ 
	public int[] getSetOfLastCards(int player){
		int cnt=0;
		for(int i=0;i<numOfPlayers;i++){
			if(i!=player){
				tmpCards[cnt++]=cardsOfLastRound[i];
			}
		}
		return tmpCards;
	}
	

	
	/** Simuliert ein Match*/
	void simulateMatch(int waitingTime){
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
	
	
	/** Simuliert eine Runde */
	void simulateRound(){
		/* ERSTER TEIL: ENTGEGENNEHMEN UND EINTRAGEN DER GESPIELTEN (UND GEZOGENEN) KARTEN*/
		currentBuzzardCard = deck.getNextCard();  // Ziehen der nächsten Gewinnkarte
		gameModel.setValueAt(String.valueOf(currentBuzzardCard), 0, (roundCtr+1)); // Eintragen der gezogenen Gewinnkarte ins GameModel
		updateInfoForPlayers();
		
		buzzardTable.deckShowNext(currentBuzzardCard); // MISSING IF_CONDITION
		
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
				}
				/* Sollte er doch versuchen zu mogeln, dann... */
				else{
					System.err.println("Player " + i + " played invalid card (" +currentPlayerCard + ")");
					playerDisqualified[i]=true;
					cardsOfCurrentRound[i]=DISQUALIFIED;
					gameModel.setValueAt("X",(i+1),(roundCtr+1));
				}
			}else{ // Sollte der Spieler nicht mehr spielberechtigt, also disqualifiziert sein...
				cardsOfCurrentRound[i]=DISQUALIFIED;
				gameModel.setValueAt("X",(i+1),(roundCtr+1));
			}
		}
		buzzardTable.showAllCards();
		/* ZWEITER TEIL: AUSWERTERN DER GESPIELTEN KARTEN ZUR PUNKTEVERGABE */
		// Wenn hier um eine Karte mit positivem Wert gespielt wird...
		if(currentBuzzardCard>=0){
			/* Wenn es tatsächlich einen alleinigen Höchstbieter gibt*/
			if(gameModel.hasWinningBidder(roundCtr+1)){
				playerScore[gameModel.getMaxPlayerAtColumn((roundCtr+1))] += currentBuzzardCard;
				buzzardTable.startScoreAnimation(players.get(gameModel.getMaxPlayerAtColumn(roundCtr+1)), currentBuzzardCard);
			}
			/* In diesem Fall konnte keiner der Spieler die Karte für sich allein gewinnen*/
			// HIER MUSS NOCH EINE ART "DrawDeck" implementiert werden
			else{
				System.out.println("Leider hat diese Karte niemand gewinnen können.");
			}
			
		}
		// Wenn hier um eine Karte mit negativem Wert gespielt wird...
		else{
			if(gameModel.hasLoosingBidder((roundCtr+1))){
				playerScore[gameModel.getMinPlayerAtColumn(roundCtr+1)]+=currentBuzzardCard;
			}
			else{
				// AUCH HIER MUSS WIEDER EIN DRAWSTACK IMPLEMENTIERT WERDEN!!! 
				System.out.println("Glück gehabt: In dieser Runde muss niemand eine Geierkarte schlucken");
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
					statPanels[winners[i]].showNextBuzzard();
				}
			}else{
				System.err.println("simulateRound(): Adding Single Win!");
				currentSeries.addWin(players.get(winner));
				statPanels[winner].showNextBuzzard();
			}
			roundCtr++;
		}
		else{
			roundCtr++;
		}
	}
	

	
	private boolean isCardValid(int playerCard){
		return(playerCard<=15&&playerCard>=1);
	}
	
	private String[] generatePlayerStrings(){
		String[] res = new String[players.size()];
		Iterator<HolsDerGeierSpieler> i = players.iterator();
		int ctr =0;
		while(i.hasNext()){
			res[ctr++]=i.next().getName();
		}
		return res;
	}
	
	BuzzardGameModel getGameModel(){
		return gameModel;
	}
	
	public Queue<BuzzardSeriesInfo> getSeriesQueue(){
		return series;
	}
	

	
	private void calculateRowHeight(){
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
	
	
	
	//===========================================================================================
	//    	S C H R O T T P L A T Z
	//===========================================================================================

	
	//	public void prepareNewGame(){
//	roundCtr=0;
//	cardsOfLastRound=new int[numberOfPlayers];
//	cardsOfCurrentRound=new int[numberOfPlayers];
//	tmpCards = new int[numberOfPlayers-1];
//	for(int i=0;i<tmpCards.length;i++){
//		tmpCards[i]=NVY;
//	}
//	gameModel=new BuzzardGameModel(generatePlayerStrings(),deck.size(),analyser);
//	/* Die letze Spalte des GameModels ist gleichzeitig die Spalte, in die die 
//	 * Gesamtergebnisse geschrieben werden sollen.*/
//	sumColumn = gameModel.getColumnCount()-1;
//	if(isDisplayed()){
//		gameModel.setOwner(display);
//	}
//	if(!firstPreparationDone){
//		int[] cardValues = {5,6,7,8,9,10};
//		deck = new BuzzardDeck(cardValues,BuzzardDeck.SHUFFLED);
//		playerScore = new int[numberOfPlayers];
//		playerDisqualified = new boolean[numberOfPlayers];
//		for(int i=0;i<numberOfPlayers;i++){
//			playedCards.add(new ArrayList<Integer>());
//			playerScore[i]=0;
//			cardsOfLastRound[i]=NVY;
//			cardsOfCurrentRound[i]=NVY;
//			playerDisqualified[i]=false;
//		}
//		firstPreparationDone=true;
//	}else{
//		deck.reset();
//		for(int i=0;i<numberOfPlayers;i++){
//			playedCards.get(i).clear();
//			playerScore[i]=0;
//			playerDisqualified[i]=false;
//			cardsOfLastRound[i]=NVY;
//			cardsOfCurrentRound[i]=NVY;
//		}
//	}
//}
	
	
//	panel.addComponentListener(
//			new ComponentAdapter(){
//				public void componentResized(ComponentEvent e){
////					int width, height;
////					Dimension pDim;
////					Iterator<JScrollPane> it = scrollPaneList.iterator();
////					int i =0;
////					while(it.hasNext()){
////						tableTmp2 = tables.get(i++);
////						width = tableTmp2.getPreferredSize().width;
////						height = tableTmp2.getPreferredSize().height+20;
////						pDim = new Dimension(width,height);
////						paneTmp=it.next();
////						paneTmp.setPreferredSize(pDim);
////						paneTmp.setMaximumSize(pDim);
////						paneTmp.setMinimumSize(pDim);
////					}
//					arrangeScrollPanels();
//				}
//			});
//	class PanelResizeListener2 extends ComponentAdapter{
//	public void componentResized(ComponentEvent ce){
//		arrangeScrollPanels();
//	}
//}
}
