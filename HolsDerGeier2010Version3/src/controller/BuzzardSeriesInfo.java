package controller;

import java.io.Serializable;
import java.util.*;
import player.*;
import vultureUtil.*;

public class BuzzardSeriesInfo implements Comparable<BuzzardSeriesInfo>, Serializable {
	
	//============================================================================================
	// 		V A R I A B L E N 
	//============================================================================================
	
	/** Datum, an dem die Info angelegt wurde*/
	protected Date date;
	
	/** Verrät, ob die Simulation der Serie abgeschlossen ist*/
	protected boolean finished;
	
	/** Der Spielmodus der Serie */
	protected DealerState dealerState;
	
	/** Das zu dieser Serie gehörende Kartendeck. */
	protected BuzzardDeck deck;
	
	/** Anzahl der Spiele, die für den Gewinn der Serie zu gewinnen sind. */
	protected int noOfMatches;
	
	/** Hier werden die Spieler der Serie gespeichert */
	protected ArrayList<HolsDerGeierSpieler> players;
	
	/** Gibt an, ob diese Serie bereits berechnet wurde. */
	protected boolean isSimulated;
	
	/** Gibt die Dauer der Visualisierungssequenzen an*/
	protected int waitingTime;
	
	/** Hier wird jedem Spieler die Anzahl der Siege zugeordnet. */
	protected Hashtable<HolsDerGeierSpieler,SeriesPlayerInfoFile> results;
	
	/** Die zu dieser Serie gehörenden GameModels. */
	protected ArrayList<BuzzardGameModel> data;
	
	/** Gibt an, ob es sich bei dieser Serie um ein Turnier 
	 * oder um eine einfache Serie handelt
	 */
	protected boolean isTournament;
	
	//===========================================================================================
	//    	K O N S T R U K T O R ( E N )
	//===========================================================================================
	public BuzzardSeriesInfo(){
		finished=false;
		players = new ArrayList<HolsDerGeierSpieler>();
		date = new Date();
		isTournament=false;
	}
	
	
	
	
	//===========================================================================================
	//    	M E T H O D E N 
	//===========================================================================================
	
	//- PUBLIC ----------------------------------------------------------------------------------
	
	/** Lege fest, wie viele Siege zum Gewinn der Serie erforderlich sind. */
	public void setNoOfMatches(int number){
		this.noOfMatches=number;
	}
	
	/** Verrät, wie viele Siege zum Gewinn der Serie erforderlich sind. */
	public int getNoOfMatches(){
		return noOfMatches;
	}
	
	/** Beantwortet die Frage, ob diese Serie bereits simuliert wurde. */
	public boolean isSimulated(){
		return isSimulated;
	}
	
	/** Markiere diese Serie als simuliert. */
	public void setSimulated(boolean simu){
		this.isSimulated=simu;
	}
	
	/** Gibt die Wartezeit zwischen den Visualisierungssequenzen zurueck*/
	public int getWaitingTime(){
		return waitingTime;
	}
	
	/** Setzt die Dauer der Wartezeit zwischen den Visualisierungssequenzen */
	public void setWaitingTime(int waitingTime){
		this.waitingTime = waitingTime;
	}
	
	/** Gibt zurueck, ob Serie ohne Anzeige simuliert werden soll - Gruppe 2*/
	public boolean isFastSim() {
		return this.waitingTime == 0;
	}
	
	/** Füge der Serie einen Spieler hinzu. */
	public void addPlayer(HolsDerGeierSpieler player){
		players.add(player);
	}

	
	/** Lege fest, welches Deck für die Austragung der Serie verwendet werden soll. */
	public void setDeck(BuzzardDeck deck){
		this.deck=deck;
	}
	
	/** Gibt die Anzahl der gespeicherten GameModels zurueck */
	public int getDataSize(){
		return data.size();
	}
	
	/** Gibt das für diese Serie verwendete Deck zurück. */
	public BuzzardDeck getDeck(){
		return deck;
	}
	
	/** Setzt die zu dieser Serie gehörenden GameModels. */
	public void addGameModel(BuzzardGameModel data){
		this.data.add(data);
	}
	
	/** Gibt das i-te GameModel dieser Serie zurück. */
	public BuzzardGameModel getData(int index){
		return data.get(index);
	}
	
	/** Verrät, ob diese Serie beendet ist */
	public boolean isFinished(){
		return finished;
	}
	
	/** Gibt als String das Erstellungsdatum aus*/
	@Override
	public String toString(){
		return date.toString();
	}
	
	public Date getDate(){
		return date;
	}
	
	@Override
	public int compareTo(BuzzardSeriesInfo in){
		return this.date.compareTo(in.getDate());
	}
	
	public boolean isTournament(){
		return isTournament;
	}
	
	//- PACKAGE ----------------------------------------------------------------------------------
	
	public ArrayList<HolsDerGeierSpieler> getPlayers(){
		return players;
	}
	
	void init(){
		results = new Hashtable<HolsDerGeierSpieler,SeriesPlayerInfoFile>();
	}
	
	void addPointsMade(HolsDerGeierSpieler player, int points){
		SeriesPlayerInfoFile res = results.get(player);
		
		if(res==null){
			SeriesPlayerInfoFile infoFile = new SeriesPlayerInfoFile(player);
			infoFile.addPointsMade(points);
			results.put(player,infoFile);
		}else{
			res.addPointsMade(points);
		}
	}
	
	void addSetWonForPlayer(HolsDerGeierSpieler player){
		SeriesPlayerInfoFile res = results.get(player);
		// Wenn der gesuchte Spieler bislang noch keinen Eintrag hatte 
		// (Warum auch immer das so sein sollte - hier muss eventuell noch einmal 
		// nachgebessert werden), dann wird eben einer erzeugt und dem Spieler 
		// ein gewonnener Satz zugewiesen. 
		if(res==null){
			SeriesPlayerInfoFile infoFile = new SeriesPlayerInfoFile(player);
			infoFile.addSetWon();
			results.put(player,infoFile);
		}else{
			res.addSetWon();
		}
	}
	
	int getWinsForPlayer(HolsDerGeierSpieler spieler){
		SeriesPlayerInfoFile res = results.get(spieler);
		if(res == null){
			return 0;
		}else{
			return res.getWins();
		}
	}
	
	void addWin(HolsDerGeierSpieler player){
		SeriesPlayerInfoFile res = results.get(player);
		/* Sollte dieser Spieler in der Liste, die die Spieler und die Anzahl ihrer
		 * Siege in der aktuellen Serie fuehrt noch nicht eingetragen sein, so hat 
		 * dieser Spieler gerade sein erstes Spiel gewonnen, was wir ihm jetzt auch 
		 * gutschreiben wollen. 
		 */
		if(res==null){
			SeriesPlayerInfoFile infoFile = new SeriesPlayerInfoFile(player);
			infoFile.addWin();
			results.put(player, infoFile);
			/* Wenn ein Spiel zum Sieg der Serie genuegt, dann ist die Serie nun
			 * vorbei			 */
			if(noOfMatches==1){
				finished=true;
				System.out.println("addWin(): Finished=true");
			}
		}
		else{
			int numberOfWins = res.addWin();
			/* Wenn der Spieler die Anzahl der erforderlichen Siege nun erreicht hat
			 * dann ist die Serie abgeschlossen. 			 */
			if(numberOfWins==noOfMatches){
				finished=true;
				System.err.println("addWin(): Finished=true");
			}
		}
	}
	
	public ArrayList<HolsDerGeierSpieler> getWinnerOfSeries(){
		ArrayList<HolsDerGeierSpieler> res = new ArrayList<HolsDerGeierSpieler>();
		Enumeration<HolsDerGeierSpieler> en = results.keys();
		int tmpMax = -1;
		int tmpVal;
		HolsDerGeierSpieler tmpPlayer;
		while(en.hasMoreElements()){
			tmpPlayer = en.nextElement();
			tmpVal = results.get(tmpPlayer).getWins();
			if(tmpVal>tmpMax){
				tmpMax=tmpVal;
				res.clear();
				res.add(tmpPlayer);
			}else if(tmpVal==tmpMax){
				res.add(tmpPlayer);
			}
		}
		return res;
	}
	
	public int getPointsMadeForPlayer(HolsDerGeierSpieler player){
		SeriesPlayerInfoFile infoFile = results.get(player);
		if(infoFile==null){
			return 0;
		}else{
			return infoFile.getPointsMade();
		}
	}
	
	static class Builder{
		
		
		//*** REQUIRED PARAMETERS *** 
		private DealerState dealerState;
		/** Datum, an dem die Info angelegt wurde*/
		private ArrayList<HolsDerGeierSpieler> players;
		/** Das zu dieser Serie gehörende Kartendeck. */
		private BuzzardDeck deck;
		
		//*** Optional Parameters ***
		/** Anzahl der Spiele, die für den Gewinn der Serie zu gewinnen sind. */
		private int noOfMatches = 1;
		/** Gibt die Dauer der Visualisierungssequenzen an*/
		private int waitingTime = 1000;
		/** Gibt an, ob diese Serie visualisiert werden soll. */
		private boolean isSimulated = false;
		/** Gibt an, ob es sich um ein Turnier handelt */
		private boolean isTournament = false;
		
		//*** Constructor ***
		public Builder(DealerState dealerState, ArrayList<HolsDerGeierSpieler> players, BuzzardDeck deck){
			this.dealerState = dealerState;
			this.players = players;
			this.deck = deck;
		}
		
		//*** Setter methods ***
		public Builder noOfMatches(int val)
		{ this.noOfMatches = val; return this; }
		
		public Builder waitingTime(int val)
		{ this.waitingTime = val; return this; }
		
		public Builder isSimulated(boolean val)
		{ this.isSimulated = val; return this; }
		
		public Builder isTournament(boolean val){
			this.isTournament = val; return this;
		}
	

	}

}
