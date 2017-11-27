package controller;

import java.io.Serializable;
import java.util.*;
import player.*;
import player.HolsDerGeierSpieler;

public class TournamentPlayerInfoFile implements Comparable<TournamentPlayerInfoFile>, Serializable{
	private HolsDerGeierSpieler player;
	private int wins;
	private int draws;
	private int losses;
	private int pointsMade;
	private int pointsAllowed;
	private int setsWon;
	private int matchesPlayed;
	
	public TournamentPlayerInfoFile(HolsDerGeierSpieler player){
		this.player=player;
		wins=0;
		draws=0;
		losses=0;
		pointsMade=0;
		pointsAllowed=0;
		setsWon=0;
	}
	


	@Override
	public int compareTo(TournamentPlayerInfoFile tpi){
		int res;
		res = wins - tpi.getWins();
		if(res!=0){
			return res;
		}else{
			res = draws - tpi.getDraws();
			if(draws!=0){
				return res;
			}else{
				res = setsWon - tpi.getSetsWon();
				if(res != 0){
					return res;
				}else{
					res = pointsMade-tpi.getPointsMade();
					return res;
				}
			}
		}
	}


	public HolsDerGeierSpieler getPlayer() {
		return player;
	}

	public int getWins() {
		return wins;
	}

	public int getDraws() {
		return draws;
	}

	public int getLosses() {
		return losses;
	}

	public int getPointsMade() {
		return pointsMade;
	}

	public int getPointsAllowed() {
		return pointsAllowed;
	}
	
	public void addWin(){
		wins++;
	}
	
	public void addDraw(){
		draws++;
	}
	
	public void addLoss(){
		losses++;
	}
	
	public void addMatchPlayed(){
		matchesPlayed++;
	}
	
	public int getMatchesPlayed(){
		return matchesPlayed;
	}
	
	public void addSetsWon(int setsWon){
		this.setsWon += setsWon;
	}
	
	public int getSetsWon(){
		return setsWon;
	}
	
	public void addPointsMade(int points){
		pointsMade+=points;
	}
	
	public void addPointsAllowed(int points){
		pointsAllowed+=points;
	}
	
	@Override
	public String toString(){
		return "Tournament: " + super.toString();
	}
}