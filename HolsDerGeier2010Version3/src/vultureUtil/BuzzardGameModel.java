package vultureUtil;

import javax.swing.*;

import java.io.Serializable;
import java.util.*;
import player.*;

public class BuzzardGameModel implements Serializable {
	public static final int DRAW = -1;
	public static final int NO_SUCH_PLAYER = -100000;
	
	private String[][] data;
	private int rows;
	private int cols;
	private BuzzardAnalyser analyser;
	private JTable owner;
	private HolsDerGeierSpieler[] players;
	
	public BuzzardGameModel(HolsDerGeierSpieler[] players, int rounds, BuzzardAnalyser analyser){
		
		this.players = players;
		rows = players.length + 1; // Anzahl Spielernamen + Tierkartenzeile
		cols = rounds+2;			// Anzahl Runden + Spielernamensplate + Gesamtpunktestandspalte
		data = new String[rows][cols];
		this.analyser = analyser;
		
		/* Initialiseren der Spielernamens- sowie der Gesamtpunktespalte*/
		data[0][0] = "Karte";
		data[0][cols-1]=String.valueOf(0);
		for(int i=1;i<rows;i++){
			data[i][0] = players[i-1].getName();
			data[i][cols-1]=String.valueOf(0); // Summe der bisher erspielten Punkte
		}
		
		/* Initialisieren der restlichen Tabellenzellen mit Leerstrings*/
		for(int i=0;i<rows;i++){
			for(int j=1;j<cols-1;j++){
				data[i][j]="";
			}
		}
		
	}
	
	public int getPointsMadeForPlayer(HolsDerGeierSpieler spieler){
		int res = -99;
		for(int i=0;i<players.length;i++){
			if(players[i].equals(spieler)){
				res=i;
			}
		}
		if(res == -99){
			return -99;
		}
		return Integer.parseInt(data[(res+1)][cols-1]);
	}
	
	/** Diese Methode gibt den Punktestand eines Spielers zum Anfragezeitpunkt zurück. 
	 *  @param player Spieler, dessen Punktestand ermittelt werden soll. Ist der angefragte 
	 *  Spieler nicht in diesem GameModel enthalten, gibt die Methode null zurück	 */
	public Integer getPlayerScore(HolsDerGeierSpieler player){
		for(int i=0;i<players.length;i++){
			if(players[i].equals(player)){
				return new Integer(data[i][cols-1]);
			}
		}
		return null; 
	}
	
	/** Diese Methode gibt den Index des Spielers mit der höchsten Gesamtpunktezahl zurück. VORSICHT:
	 *  Diese Methode überprüft NICHT, ob das aktuelle Spiel bereits zu Ende gespielt wurde oder nicht, 
	 *  sondern gibt den Index des führenden Spielers zum Zeitpunkt der Anfrage zurück. Bei 
	 *  Gleichstand zwischen zwei oder mehreren Spielern ist der Rückgabewert "-1". 
	 * @return Index des Spielers mit der höchsten Gesamtpunktezahl zum Zeitpunkt der Anfrage. Bei 
	 *         Gleichstand zwischen zwei oder mehr Spielern ist der Rückgabewert "-1". */
	public int getWinner(){
		int max = -99999999;
		int maxPlayer = -1;
		boolean draw = true;
		int currentValue;
		for(int i=1;i<rows;i++){
			currentValue = Integer.parseInt(data[i][cols-1]);
			if(currentValue>max){
				max=currentValue;
				maxPlayer=i-1;
				draw=false;
			}
			else if(currentValue==max){
				draw=true;
			}
		}
		if(draw){
			return -1;
		}
		else{
			return maxPlayer;
		}
	}
	
	/** Diese Methode gibt ein Array mit dem Index/den Indizes der Spieler mit der höchsten 
	 *  Gesamtpunktezahl zum Zeitpunkt der Anfrage zurück. VORSICHT: Diese Methode überprüft NICHT,
	 *  ob das Spiel bereits beendet ist oder nicht.
	 *  @param Array mit dem Index / den Indizes der Spieler mit der höchsten Gesamtpunktzahl zum
	 *         Zeitpunkt der Anfrage. 	 */
	public Integer[] getWinners(){
		ArrayList<Integer> maxPlayers = new ArrayList<Integer>();
		int currentValue;
		int max = -999999;
		for(int i=1;i<rows;i++){
			currentValue = Integer.parseInt(data[i][cols-1]);
			if(currentValue>max){
				max=currentValue;
				maxPlayers.clear();
				maxPlayers.add(i-1);
			}
			else if(currentValue==max){
				maxPlayers.add((i-1)); // <--- Nette Sache, dieses Auto-Boxing ;)
			}
		}
		
		return maxPlayers.toArray(new Integer[maxPlayers.size()]);
	}
	
	public void setOwner(JTable owner){
		this.owner=owner;
	}
	
	public boolean hasWinningBidder(int column){
		analyser.analyseColumnForMax(this,column);
		return analyser.isNewMax();
	}
	
	public boolean hasLoosingBidder(int column){
		analyser.analyseColumnForMin(this, column);
		return analyser.isNewMin();
	}
	
	public int getMaxValueAtColumn(int column){
		analyser.analyseColumnForMax(this,column);
		return analyser.getMax();
	}
	
	public int getMaxPlayerAtColumn(int column){
		analyser.analyseColumnForMax(this, column);
		return (analyser.getMaxPlayer()-1);
	}
	
	public int getMinValueAtColumn(int column){
		analyser.analyseColumnForMin(this, column);
		return analyser.getMin();
	}
	
	public int getMinPlayerAtColumn(int column){
		analyser.analyseColumnForMin(this, column);
		return (analyser.getMinPlayer()-1);
	}
	
	public int getRowCount(){
		return rows;
	}
	
	public int getColumnCount(){
		return cols;
	}
	
	public String getValueAt(int row, int col){
		return data[row][col];
	}
	
	public void setValueAt(String value, int row, int col){
		data[row][col]=value;
		if(!(owner==null)){
			owner.validate();
			owner.repaint();
		}
	}
}
