package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.JTable;

import player.HolsDerGeierSpieler;
import vultureUtil.BuzzardAnalyser;
import vultureUtil.BuzzardGameModel;

public abstract class DealerStateCalc implements DealerState {
	public static final int DISQUALIFIED = -1000;
	public static final int NVY = -1001;
	public static final int PDE = -1002;
	protected BuzzardAnalyser analyser;
	protected ArrayList<HolsDerGeierSpieler> players;
	/** Hier werden die bereits gespielten Karten der Spieler verwaltet */
	protected ArrayList<ArrayList<Integer>> playedCardsOfPlayerX;
	/** Hier werden die Punktestaende der Spieler gespeichert */
	protected int[] playerScore;
	/**
	 * In diesem Feld wird vermerkt, ob ein Spieler disqualifiziert oder noch
	 * spielberechtigt ist
	 */
	protected boolean[] playerDisqualified;
	/** Der Gewinnkartenstapel */
	protected BuzzardDeck deck;
	protected BuzzardGameModel gameModel;
	/** Zaehler, welche die aktuelle Rundennummer vermerkt */
	protected int roundCtr;
	protected Queue<BuzzardSeriesInfo> series;
	protected BuzzardSeriesInfo currentSeries;
	/** Die Zeit, die zwischen zwei Runden verstreichen soll */
	protected int waitingTime;
	/** Array für die GameModels */
	protected ArrayList<BuzzardGameModel> gameModels;
	/**
	 * Zaehler, der die Nummer des Spieles der aktuellen Serie (beginend bei 0!)
	 * widergibt
	 */
	protected int gameCounter;
	/** Zaehler fuer die Spielernummern */
	protected int playerCounter;
	/** Anzahl der an der aktuellen Serie partizipierenden Spieler */
	protected int numOfPlayers;
	/**
	 * Nummer der Spalte des GameModels, in die die Punktesummen der Spieler
	 * geschrieben werden
	 */
	protected int sumColumn;
	/**
	 * Die aktuelle Gewinnkarte, um die gespielt wird. Eine Gewinnkarte kann
	 * einen positiven Wert (Mäusekarte) oder einen negativen Wert (Geierkarte)
	 * besitzen.
	 */
	protected int currentBuzzardCard;
	/**
	 * In diesem Array werden die Karten der Spieler, welche diese in der letzen
	 * Runde spielten gespeichert. Dieses Array wird bei den Methoden
	 * "letzeKarte()" und "letzteKarte(int spieler)" verwendet.
	 */
	protected int[] cardsOfLastRound;
	/**
	 * In diesem Array werden die Karten der Spieler, welche diese in der
	 * aktuellen Runde spielen, gespeichert.
	 */
	protected int[] cardsOfCurrentRound;
	/**
	 * Dieses Array dient der Methode "letzeKarte()" - Hierbei werden die Karten
	 * der letzten Runde mit Ausnahme der Karte des anfragenden Spielers in
	 * diesem Array gespeichert.
	 */
	protected int[] tmpCards;
	protected int currentPlayerCard;
	/** Anzahl der Mindestens darzustellenden GamePanels */
	protected int minNumPan;
	/**
	 * Hier werden die Werte der Karten, welche wegen eines Unentschiedens
	 * beiseite gelegt wurden gespeichert, bis es einen Gewinner gibt
	 */
	protected int drawDeck;

	// =====================================================================================================
	// K O N S T R U K T O R ( E N )
	// =====================================================================================================
	public DealerStateCalc() {
		analyser = new BuzzardAnalyser();
		playedCardsOfPlayerX = new ArrayList<ArrayList<Integer>>();
		series = new PriorityQueue<BuzzardSeriesInfo>();
	}

	// ======================================================================================================
	// M E T H O D E N
	// ======================================================================================================
	@Override
	public int gewinnrundenanzahl() {
		return currentSeries.getNoOfMatches();
	}

	/** Simuliert eine Runde */
	void simulateRound() {
		getAnimalCard();
		writeToModel();
		updateInfo();
		updateInfoForPlayers();
		askPlayersForCard();
		evaluateRound();
		concludeRound();
	}

	@Override
	public void setBuzzardSeriesInfo(BuzzardSeriesInfo info) {
		currentSeries = info;
	}

	/**
	 * Diese Methode wird zu Beginn einer neuen Serie einmalig aufgerufen,
	 * anschließend wird für jedes neue Spiel dieser Serie jedes Mal die Methode
	 * "prepareNewGame()" aufgerufen.
	 */
	@Override
	public void prepareNewSeries() {
		drawDeck = 0;
		roundCtr = 0;
		playerCounter = 0;

		gameCounter = 0; // Zurücksetzen des Game-Counters, der Angibt, die wie
							// vielte Partie gespielt wird
		// currentSeries = series.poll();
		/*
		 * Erzeugt eine Hashtable innerhalb der Serie, in der die Ergebnisse
		 * abgespeichert werden
		 */
		currentSeries.init();
		this.players = currentSeries.getPlayers();
		/*
		 * Der Dealer muss sich nun bei jedem Spieler registrieren, damit die
		 * Kommunikation gelingen kann
		 */
		for (int i = 0; i < players.size(); i++) {
			players.get(i).register(this, playerCounter++);
		}
		numOfPlayers = players.size();
		deck = currentSeries.getDeck();
		deck.reset();

		Iterator<HolsDerGeierSpieler> it = players.iterator();
		while (it.hasNext()) {
			HolsDerGeierSpieler spieler = it.next();
			spieler.reset();
			spieler.reset2();
		}

		/*
		 * Erstellen der Kartenfelder, welche für die Versorgung der Spieler mit
		 * Informationen über die gespielten Karten der Gegner verantwortlich
		 * sind.
		 */
		cardsOfLastRound = new int[numOfPlayers]; // Die Karten, die in der
													// Letzten Runde gespielt
													// wurden
		cardsOfCurrentRound = new int[numOfPlayers]; // Die Karten, die in der
														// aktuellen Runde
														// gespielt wurden
		tmpCards = new int[numOfPlayers - 1];
		/* Erstellen der Felder zur Spielverwaltung */
		playedCardsOfPlayerX = new ArrayList<ArrayList<Integer>>();
		playerScore = new int[numOfPlayers];
		playerDisqualified = new boolean[numOfPlayers];

		/*
		 * Initialisieren aller Slots, so dass klar ist, dass hier noch keine
		 * Werte vorliegen
		 */
		for (int i = 0; i < tmpCards.length; i++) {
			tmpCards[i] = NVY; // NVY = (N)o (V)alue (Y)et
		}
		for (int i = 0; i < numOfPlayers; i++) {
			cardsOfLastRound[i] = NVY;
			cardsOfCurrentRound[i] = NVY;
			playedCardsOfPlayerX.add(new ArrayList<Integer>());
			playerScore[i] = 0;
			playerDisqualified[i] = false;
		}

		/*
		 * Erstellen der ArrayList, in der die Gamemodels der Partien verwaltet
		 * werden.
		 */
		gameModels = new ArrayList<BuzzardGameModel>();
		/*
		 * Erstellen des (ersten) GameModels, für das erste Spiel der aktuellen
		 * Serie
		 */
		for (int i = 0; i < currentSeries.getNoOfMatches(); i++) {
			gameModels.add(new BuzzardGameModel(players
					.toArray(new HolsDerGeierSpieler[players.size()]), deck
					.size(), analyser));
		}

		gameModel = gameModels.get(gameCounter);

		/*
		 * Die letze Spalte des GameModels ist gleichzeitig die Spalte, in die
		 * die Gesamtergebnisse geschrieben werden sollen
		 */
		sumColumn = gameModel.getColumnCount() - 1;

	}

	/**
	 * Diese Methode wird ab dem zweiten Spiel einer Serie aufgerufen, da dann
	 * lediglich die entsprechenden Felder zurückzusetzen sind
	 */

	@Override
	public void prepareNewGame() {
		drawDeck = 0;
		gameCounter++;
		roundCtr = 0;
		deck.reset();
		/*
		 * Wenn der gameCounter die Zahl der zum Sieg benötigten Partien
		 * übersteigt, sind die ursprünglich angelegten GameModel und Tabellen
		 * nicht ausreichend und es muss mindestens ein zusätzliches GameModel
		 * und ggf. auch eine zusätzliche Tabelle angelegt werden. (Erreicht der
		 * GameCounter einmal die Höhe der Best-Of-X Serie, geschieht das
		 * Anlegen eines neuen GameModels und ggf. einer neuen Tabelle fortan
		 * mit jedem Aufruf dieser Methode.
		 */
		if (gameCounter >= currentSeries.getNoOfMatches()) {
			gameModel = new BuzzardGameModel(
					players.toArray(new HolsDerGeierSpieler[players.size()]),
					deck.size(), analyser);
			gameModels.add(gameModel);

		} else {
			/*
			 * Das neue "aktuelle" GameModel aus der ArrayList beziehen und
			 * damit weiterarbeiten.
			 */
			gameModel = gameModels.get(gameCounter);
		}
		for (int i = 0; i < numOfPlayers; i++) {
			playedCardsOfPlayerX.get(i).clear(); // Kein Spieler hat am Anfang
													// eine Karte gespielt
			/*
			 * Auch das Array, welches fuer die getSetOfLastCards(int
			 * player)-Methode benoetigt wird soll an dieser Stelle
			 * zurueckgesetzt werden.
			 */
			if (i != (numOfPlayers - 1)) {
				tmpCards[i] = NVY;
			}
			cardsOfLastRound[i] = NVY; // Im neuen Spiel gab es noch keine
										// letzte Runde -> Zuruecksezten
			cardsOfCurrentRound[i] = NVY; // Auch hier: Zuruecksetzen
			playerScore[i] = 0; // Jeder Spieler startet mit 0 Punkten
			playerDisqualified[i] = false;// Hier gilt die Unschuldsvermutung ;)
			players.get(i).reset(); // Den Spielern die Chance geben, sich neu
									// zu sortieren
		}

	}

	public void addSeries(BuzzardSeriesInfo series) {
		this.series.add(series);
	}

	public boolean isGameOver() {
		return deck.isEmpty();
	}

	public void setDisplay(JTable display) {

	}

	public boolean isDisplayed() {
		return false;
	}

	protected void updateInfoForPlayers() {
		for (int i = 0; i < cardsOfLastRound.length; i++) {
			cardsOfLastRound[i] = cardsOfCurrentRound[i];
		}
	}

	/**
	 * Methode, um die im letzen Zug gespielte Karte eines Spielers abzufragen.
	 * 
	 * @param player
	 *            Nummer des Spielers, dessen Karte in Erfahrung gebracht werden
	 *            soll.
	 * */
	@Override
	public int getLastCardOf(int player) {
		if (player >= 0 && player < numOfPlayers) {
			return cardsOfLastRound[player];
		} else {
			return PDE;
		}
	}

	/**
	 * Methode, mit der die Karten des letzen Zuges von sämtlichen Gegnern
	 * abgefragt werden können
	 * 
	 * @param player
	 *            Nummer des Spielers, der die Anfrage stellt, da seine letze
	 *            Karte nicht im Ergebnis vorkommen soll
	 */
	@Override
	public int[] getSetOfLastCards(int player) {
		int cnt = 0;
		for (int i = 0; i < numOfPlayers; i++) {
			if (i != player) {
				tmpCards[cnt++] = cardsOfLastRound[i];
			}
		}
		return tmpCards;
	}

	/**
	 * Simuliert eine komplette Serie, bis ein Spieler die geforderte Anzahl von
	 * Siegen erzielt hat.
	 */
	@Override
	public void simulateSeries() {
		prepareNewSeries();
		simulateMatch(0);
		while (!currentSeries.isFinished()) {
			prepareNewGame();
			simulateMatch(0);
		}
	}

	/** Simuliert ein Match */
	@Override
	public void simulateMatch(int waitingTime) {
		while (!isGameOver()) {
			simulateRound();
			try {
				Thread.sleep(waitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.err.println("simulateMatch(): Game is over");
	}

	/**
	 * Wertet die Daten der aktuellen Runde aus und vergibt entsprechend die
	 * Punkte
	 */
	protected abstract void evaluateRound();

	protected boolean isCardValid(int playerCard) {
		return (playerCard <= 15 && playerCard >= 1);
	}

	protected String[] generatePlayerStrings() {
		String[] res = new String[players.size()];
		Iterator<HolsDerGeierSpieler> i = players.iterator();
		int ctr = 0;
		while (i.hasNext()) {
			res[ctr++] = i.next().getName();
		}
		return res;
	}

	BuzzardGameModel getGameModel() {
		return gameModel;
	}

	public Queue<BuzzardSeriesInfo> getSeriesQueue() {
		return series;
	};

	protected void concludeRound() {
		if (gameIsOver()) {
			addWinnerToSeries();
			incrementRndCtr();
		} else {
			incrementRndCtr();
		}
	}

	protected boolean gameIsOver() {
		return deck.isEmpty();
	}

	protected void incrementRndCtr() {
		roundCtr++;
	}

	protected void addWinnerToSeries() {
		System.err.println("simulateRound(): Deck is Empty!");
		int winner = gameModel.getWinner();
		/*
		 * Wenn es keinen eindeutigen/alleinigen Sieger gibt, gibt es eben
		 * mehrere.
		 */
		if (winner == -1) {
			Integer[] winners = gameModel.getWinners();
			System.err.println("simulateRound(): Adding multiple wins!");
			for (int i = 0; i < winners.length; i++) {
				currentSeries.addWin(players.get(winners[i]));
			}
		} else {
			System.err.println("simulateRound(): Adding Single Win!");
			currentSeries.addWin(players.get(winner));
		}
	}

	protected void askPlayersForCard() {
		/*
		 * Abarbeiten der einzelnen Spieler, das heißt, entgegennehmen ihrer
		 * gespielten Karten unter vorheriger Prüfung der Nicht-
		 * disqualifikationsbedingung sowie Prüfung der gespielten Karte auf
		 * Gültigkeit
		 */
		for (int i = 0; i < numOfPlayers; i++) {
			try {
				if (!playerDisqualified[i]) { // Ist der Spieler noch
												// spielberechtigt? Wenn ja,
												// dann...

					currentPlayerCard = players.get(i).gibKarte(
							currentBuzzardCard);
					/* Wenn der Spieler nicht versucht zu schummeln, dann... */
					if (!playedCardsOfPlayerX.get(i)
							.contains(currentPlayerCard)
							&& isCardValid(currentPlayerCard)) {
						cardsOfCurrentRound[i] = currentPlayerCard;
						playedCardsOfPlayerX.get(i).add(currentPlayerCard);
						gameModel.setValueAt(String.valueOf(currentPlayerCard),
								(i + 1), (roundCtr + 1));
					}
					/* Sollte er doch versuchen zu mogeln, dann... */
					else {
						System.err.println("Player " + i
								+ " played invalid card (" + currentPlayerCard
								+ ")");
						playerDisqualified[i] = true;
						cardsOfCurrentRound[i] = DISQUALIFIED;
						gameModel.setValueAt("X", (i + 1), (roundCtr + 1));
					}
				} else { // Sollte der Spieler nicht mehr spielberechtigt, also
							// disqualifiziert sein...
					cardsOfCurrentRound[i] = DISQUALIFIED;
					gameModel.setValueAt("X", (i + 1), (roundCtr + 1));
				}
			} catch (Exception e) {
				playerDisqualified[i] = true;
				cardsOfCurrentRound[i] = DISQUALIFIED;
				gameModel.setValueAt("X", (i + 1), (roundCtr + 1));
			}
		}
	}

	protected void updateInfo() {
		gameModel.setValueAt(String.valueOf(currentBuzzardCard), 0,
				(roundCtr + 1)); // Eintragen der gezogenen Gewinnkarte ins
									// GameModel
	}

	protected void writeToModel() {
		gameModel.setValueAt(String.valueOf(currentBuzzardCard), 0,
				(roundCtr + 1)); // Eintragen der gezogenen Gewinnkarte ins
									// GameModel
	}

	protected void getAnimalCard() {
		/*
		 * ERSTER TEIL: ENTGEGENNEHMEN UND EINTRAGEN DER GESPIELTEN (UND
		 * GEZOGENEN) KARTEN
		 */
		currentBuzzardCard = deck.getNextCard(); // Ziehen der nächsten
													// Gewinnkarte
	}
}
