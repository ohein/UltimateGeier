package controller;

import java.io.Serializable;

import player.*;

public class SeriesPlayerInfoFile implements Serializable {
	private HolsDerGeierSpieler player;
	private int wins;
	private int losses;
	private int pointsMade;
	private int pointsAllowed;
	private int setsWon;
	
	public SeriesPlayerInfoFile(HolsDerGeierSpieler player){
		this.player = player;
		wins = 0;
		losses = 0;
		pointsMade = 0;
		pointsAllowed = 0;
		setsWon = 0;
	}
	
	public int addWin(){
		wins++;
		return wins;
	}
	
	public int addLoss(){
		losses++;
		return losses;
	}
	
	public int addPointsMade(int points){
		pointsMade += points;
		return pointsMade;
	}
	
	public int addPointsAllowed(int points){
		pointsAllowed += points;
		return pointsAllowed;
	}
	
	public int getWins(){
		return wins;
	}
	
	public int getPointsMade(){
		return pointsMade;
	}

	public int getLosses(){
		return losses;
	}
	
	public int getPointsAllowed(){
		return pointsAllowed;
	}
	
	public void addSetWon(){
		setsWon++;
	}
	
	public int getSetsWon(){
		return setsWon;
	}
}
