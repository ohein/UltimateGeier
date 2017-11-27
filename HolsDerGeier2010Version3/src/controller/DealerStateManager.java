package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import player.HolsDerGeierSpieler;
import tableDisplay.AbstractBuzzardTableDisplay;
import vultureUtil.BuzzardAnalyser;
import vultureUtil.BuzzardGameModel;
import vultureUtil.TableFactory;

public class DealerStateManager {
	// ===========================================================================================
	// V A R I A B L E N
	// ===========================================================================================

	// -PUBLIC------------------------------------------------------------------------------------
	public static final int DISQUALIFIED = -1000;
	public static final int NVY = -1001; // NVY == "No Value Yet"
	public static final int PDE = -1002; // PDE == "Player does not exist"
	

	// ===========================================================================================
	// Z U S T A E N D E
	// ===========================================================================================
	/** Wird verwendet, wenn eine Serie lediglich berechnet werden soll */
	private DealerStateCalc stateCalculating;

	/**
	 * Wird verwendet, wenn in einer Simulation sowohl die Tabellen als auch der
	 * Tisch animiert zu sehen sein sollen.
	 */
	private DealerStateFullBuzzard stateFull;

	/**
	 * Wird verwendet, wenn in einer Simulation lediglich die Tabellen zu sehen
	 * sein sollen
	 */
	private DealerStateTable stateTable;

	/** Allgemeine Referenz auf den aktuellen Zustand */
	private DealerState currentState;
	
	/** Gibt Auskunft, ob ein Spiel beendet ist. */
	private boolean isFinished;
	
	/** Gibt Auskunft, ob es sich um ein Tunier handelt, falls false, handelt es sich um eine Serie */
	private boolean isTournament;
	
	

	// -PRIVATE-----------------------------------------------------------------------------------

	private Queue<BuzzardSeriesInfo> series;

	private BuzzardSeriesInfo currentSeries;
	
	private BuzzardTournamentInfo tournament;
	
	/** Speichert das erhaltene BuzzardSeriesInfo Objekt ab, damit die Informationen bei einer 
	 * Wiederholung des Spiels gespeichert sind */
	private BuzzardSeriesInfo savedBuzzardSeries;

	// =========

	/** Die Zeit, die zwischen zwei Runden verstreichen soll */
	private int waitingTime;

	/**
	 * 
	 */
	private int playerCounter;

	// =========

	/** Anzahl der an der aktuellen Serie partizipierenden Spieler */
	private int numOfPlayers;

	/**
	 * Die aktuelle Gewinnkarte, um die gespielt wird. Eine Gewinnkarte kann
	 * einen positiven Wert (Mäusekarte) oder einen negativen Wert (Geierkarte)
	 * besitzen.
	 */
	private int currentBuzzardCard;

	/**
	 * Frame, der die aktuelle Partie darstellt, muss an die Zustaende
	 * <<stateFull>> und <<stateTable>> weitergereicht werden
	 */
	protected SimulationGUI bigPanel;

	/**
	 * Der Dialog, der fuer die Darstellung der Karten und des Spielablaufs zu-
	 * staendig ist.
	 */
	private AbstractBuzzardTableDisplay buzzardTable;

	// --------------------------------------------------------------------------------------------
	// V a r i a b l e n p l a y e r P a n e l
	// --------------------------------------------------------------------------------------------

	private MainController controller;

	private ReentrantLock lock;
	private Condition simIns;
	private Condition goToTournamentTable;

	// ===========================================================================================
	// K O N S T R U K T O R ( E N )
	// ===========================================================================================

	public DealerStateManager(MainController controller) {
		this.controller = controller;
		lock = new ReentrantLock();
		simIns = lock.newCondition();
		goToTournamentTable = lock.newCondition();
		series = new PriorityQueue<BuzzardSeriesInfo>();
		savedBuzzardSeries = new BuzzardSeriesInfo();
		stateCalculating = new DealerStateCalcBuzzard(controller);
		stateTable = new DealerStateTableBuzzard(controller);
		stateFull = new DealerStateFullBuzzard(controller);
		isFinished=false;
	}

	// ===========================================================================================
	// M E T H O D E N
	// ===========================================================================================
	
	/**Gibt als RŸckgabewert true or false ob ein Spiel beendet ist*/
	public boolean isFinished(){
		return isFinished;
	}
	
	/**Gibt als RŸckgabewert true or false ob es sich um ein Tunier handelt. 
	 * (bei false handelt es sich um eine Serie)*/
	public boolean isTournament(){
		return isTournament;
	}
	
	/**Legt den Zustand fest, ob ein Spiel beendet ist*/
	public void setIsFinished(boolean zustand){
		isFinished=zustand;
	}
	
	public void setBuzzardTable(AbstractBuzzardTableDisplay buzzardTable) {
		this.buzzardTable = buzzardTable;
		/*
		 * Das stateFull-Objekt benötigt einen
		 * AbstractBuzzardTableDisplay-Objekt, weshalb dieses an dieser Stelle
		 * an das stateFullObjekt weitergeleitet wird
		 */
		stateFull.setBuzzardTable(buzzardTable);
	}

	public void setPanel(SimulationGUI bigPanel) {
		/*
		 * Sowohl das stateFull- als auch das stateTable-Objekt benoetigen eine
		 * Referenz auf ein SimulationGUI-Objekt, deshalb wird dieses an diese
		 * beiden Objekete weitergeleitet.
		 */
		stateTable.setPanel(bigPanel);
		stateFull.setPanel(bigPanel);

	}

	public void signalSimIns() {
		lock.lock();
		simIns.signal();
		lock.unlock();
	}

	void simulateNext() {
		Thread t1 = new Thread() {
			public void run() {
				series.clear();
				//Hier wird das gespeicherte Objekt savedBuzzardSeries der PriorityQueue hinzugefŸgt
				series.add(savedBuzzardSeries);

				currentSeries = series.poll();
				// Die currentSeries wird als finished=true bekennzeichnet, dies muss geŠndert werden,
				//da sonst das Spiel nach einem Klick auf den Replaybutton nicht weiterlŠuft
				currentSeries.finished=false;
				// System.out.println(currentSeries.isTournament());
				// System.out.println(currentSeries instanceof
				// BuzzardTournamentInfo);

				/*
				 * Je nach Simualtionsart wird ein anderer Zustand angenommen
				 * ANMERKUNG: Da der Zustand an dieser Stelle festgelegt wird,
				 * wird in einem Turnier jedes Spiel auf die gleiche Art und
				 * Weise simuliert - dies sollte in späteren Versionen
				 * vielleicht noch geaendert werden, so dass bestimmte Matchups
				 * eines Turniers visualisert und andere lediglich
				 * durchgerechnet werden
				 */
				if (currentSeries.isSimulated()) {
					currentState = stateFull;
					System.out.println("Full");
				}
				else if(!currentSeries.isFastSim()){
					currentState = stateTable;
					System.out.println("Table");
				} else {
					currentState = stateCalculating;
					System.out.println("Calculating");
				}
				if (currentSeries instanceof BuzzardTournamentInfo) {
					// System.out.println("Ich simuliere ein Turnier!");
					isTournament=true;
					ArrayList<HolsDerGeierSpieler> tmpPlayerList;
					tournament = (BuzzardTournamentInfo) currentSeries;
					
					//Initialisierungsroutine nur fuer neue Turniere aufrufen
					if (!tournament.isDeserialized()) {
						tournament.generateTournament();
					}

					while (tournament.hasNextMatchUp() || tournament.isDeserialized()) {
						//Gruppe 2:
						//Naechste Runde nur berechnen, wenn Turnier nicht gerade deserialisiert wurde,
						//da zuerst der aktuelle Zwischenstand angezeigt werden soll
						if (!tournament.isDeserialized()) {
							controller.displaySimulationGUI();
							// System.out.println("Requesting next MatchUp");
							currentSeries = tournament.getNextMatchUp();
							currentState.setBuzzardSeriesInfo(currentSeries);
							// System.out.println("Next MatchUp received");
							currentState.simulateSeries();
							// System.out.println("New Series Prepared");
							// System.err.println("Requesting simulation");
							// System.err.println("Match Simulated");
							System.err.println("CurrentSeries Finished");

							// ======================================================================
							// Zurueckschreiben / Speichern der Daten der gerade
							// simulierten Serie
							// ======================================================================
							tmpPlayerList = currentSeries.getWinnerOfSeries();
							System.err.println("Winner of Series received");
							if (tmpPlayerList.size() == 1) {
								System.err.println("Adding single winner");
								tournament.getTournamentTableDataClass().addWin(tmpPlayerList.get(0));
							} else if (tmpPlayerList.size() > 1) {
								System.err.println("Adding draw players");
								Iterator<HolsDerGeierSpieler> it = tmpPlayerList.iterator();
								while (it.hasNext()) {
									tournament.getTournamentTableDataClass().addDraw(it.next());
								}
							}
							// Hinzufuegen der Punkte, die jeder Spieler fuer sich
							// erzielen konnte
							for (HolsDerGeierSpieler p : currentSeries.players) {
								tournament.getTournamentTableDataClass().addPointsMadeForPlayer(p, currentSeries.getPointsMadeForPlayer(p));
								tournament.getTournamentTableDataClass().addMatchPlayedForPlayer(p);
								tournament.getTournamentTableDataClass().addSetsWonForPlayer(p, currentSeries.getWinsForPlayer(p));
								if (!(tmpPlayerList.contains(p))) {
									tournament.getTournamentTableDataClass().addLoss(p);
								}
							}
						}
							
						//Wenn FastSim, dann nicht auf Verlassen der Zwischenstandsanzeige warten.
						//Wenn Turnier gerade deserialisiert worden ist, dann ebenfalls nicht auf
						//Verlassen warten, da SimulationGUI noch nicht angezeigt wurde.
						if (!currentSeries.isFastSim() && !tournament.isDeserialized()) {
							lock.lock();
							try{
								goToTournamentTable.await();
							}catch(InterruptedException ie){
								ie.printStackTrace();
							}finally{
								lock.unlock();
							}
						}
						
						//Zwischenstand nur Anzeigen, wenn kein FastSim oder keine offenen Matches mehr
						if (!currentSeries.isFastSim() || !tournament.hasNextMatchUp()) {
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
						//Gruppe 2:
						//Turnier ist mindestens einmal durch die Zwischenstandsanzeige gelaufen,
						//kann also nicht mehr als gerade deserialisiert angesehen werden
						tournament.setDeserialized(false);
					}
				} else {
					System.out.println("Ich simuliere kein Turnier");
					isTournament=false;
					currentState.setBuzzardSeriesInfo(currentSeries);
					currentState.simulateSeries();
				}
				isFinished=true;
			}
		};
		t1.start();
	}

	/**
	 * Diese Methode wird zu Beginn einer neuen Serie einmalig aufgerufen,
	 * anschließend wird für jedes neue Spiel dieser Serie jedes Mal die Methode
	 * "prepareNewGame()" aufgerufen.
	 */
	public void prepareNewSeries() {

	}

	/**
	 * Diese Methode wird ab dem zweiten Spiel einer Serie aufgerufen, da dann
	 * lediglich die entsprechenden Felder zurückzusetzen sind
	 */
	public void prepareNewGame() {

	}
	/** Hier wird das BuzzardSeriesInfo Objekt gespeichert, da es durch currentSeries = series.poll(); 
	 * in der methode simulateNext() aus "series" herausgelšscht wird.*/
	public void addSeries(BuzzardSeriesInfo series) {
		//this.series.add(series);
		savedBuzzardSeries = series; 
	}

	// public boolean isGameOver(){
	//
	// }
	//

	//
	// public boolean isDisplayed(){
	// return !(display==null);
	// }
	//

	/** Simuliert ein Match */
	void simulateMatch(int waitingTime) {

	}

	/** Simuliert eine Runde */
	void simulateRound() {
		// Deligieren an State Objekt
	}

	public Queue<BuzzardSeriesInfo> getSeriesQueue() {
		return series;
	}

	public void signalGoToTournamentTable() {
		lock.lock();
		goToTournamentTable.signal();
		lock.unlock();
		if (currentState instanceof DealerStateTableBuzzard) {
			((DealerStateTable) currentState).startSimulationSignal();
		}
	}

	/**
	 * Gibt die aktuelle Serie zurück, falls sie ein Turnier ist.
	 * Gruppe 2
	 * 
	 * @return Gibt das Turnier zurueck. Falls es sich um eine Serie handelt,
	 *         wird null zurück gegeben
	 */
	public BuzzardTournamentInfo getTournament() {
		return tournament;
	}

}
