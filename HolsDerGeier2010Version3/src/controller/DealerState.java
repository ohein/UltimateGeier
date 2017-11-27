package controller;

import java.io.Serializable;

public interface DealerState extends Serializable {
	public void simulateMatch(int waitingTime);

	public void prepareNewSeries();

	public void prepareNewGame();

	public void setBuzzardSeriesInfo(BuzzardSeriesInfo info);

	public int getLastCardOf(int playerNumber);

	public int[] getSetOfLastCards(int playerNumber);

	public void simulateSeries();

	public int gewinnrundenanzahl(); // neue Methode

}
