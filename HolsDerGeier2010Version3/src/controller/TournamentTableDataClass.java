package controller;

import java.io.Serializable;
import java.util.*;

import player.*;

public class TournamentTableDataClass implements Serializable{
	private Hashtable<HolsDerGeierSpieler, TournamentPlayerInfoFile> hashTable;
	
	public TournamentTableDataClass(ArrayList<HolsDerGeierSpieler> players){
		Iterator<HolsDerGeierSpieler> it = players.iterator();
		HolsDerGeierSpieler tmpPlayer;
		hashTable = new Hashtable<HolsDerGeierSpieler, TournamentPlayerInfoFile>();
		while(it.hasNext()){
			tmpPlayer = it.next();
			hashTable.put(tmpPlayer, new TournamentPlayerInfoFile(tmpPlayer));
		}
	}
	
	public ArrayList<TournamentPlayerInfoFile> getTournamentTable(){
		ArrayList<TournamentPlayerInfoFile> res = new ArrayList<TournamentPlayerInfoFile>();
		Enumeration<TournamentPlayerInfoFile> e = hashTable.elements();
		while(e.hasMoreElements()){
			res.add(e.nextElement());
		}
		return res;
	}
	
	public void addWin(HolsDerGeierSpieler player){
		TournamentPlayerInfoFile tmp = hashTable.get(player);
		tmp.addWin();
	}
	
	public void addDraw(HolsDerGeierSpieler player){
		TournamentPlayerInfoFile tmp=hashTable.get(player);
		tmp.addDraw();
	}
	
	public void addLoss(HolsDerGeierSpieler player){
		hashTable.get(player).addLoss();
	}
	
	public void addMatchPlayedForPlayer(HolsDerGeierSpieler player){
		hashTable.get(player).addMatchPlayed();
	}
	
	public int getMatchesPlayedForPlayer(HolsDerGeierSpieler player){
		return hashTable.get(player).getMatchesPlayed();
	}
	
	public void addSetsWonForPlayer(HolsDerGeierSpieler player, int setsWon){
		hashTable.get(player).addSetsWon(setsWon);
	}
	
	public int getSetsWonForPlayer(HolsDerGeierSpieler player){
		return hashTable.get(player).getSetsWon();
	}
	
	public void addPointsMadeForPlayer(HolsDerGeierSpieler player, int points){
		hashTable.get(player).addPointsMade(points);
	}
	
	public void addPointsAllowedForPlayer(HolsDerGeierSpieler player, int points){
		hashTable.get(player).addPointsAllowed(points);
	}
}
