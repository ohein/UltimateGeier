package smartStratPlayerPackage;

import java.util.ArrayList;

public class InfoLevel {
	/** Die im vorherigen Zug von Spieler A gespielte Karte*/
	private Integer lastCardOfA;
	/** Die im vorherigen Zug von Spieler B gespielte Karte*/
	private Integer lastCardOfB;
	/** Der Stapel mit den noch zu gewinnenden Maesue-/Geierkarten*/
	private ArrayList<Integer> animalCardDeck;
	/** Die Bietkarten von Spieler A */
	private ArrayList<Integer> playerADeck;
	/** Die Bietekarten von Spieler B */
	private ArrayList<Integer> playerBDeck;
	/** Die Karte, auf die in der aktuellen Runde geboten wird*/
	private Integer currentAnimalCard;
	/** Das aktuelle Punktedelta. Ein positiver Wert bedeutet hierbei eine Fuehrung von Spieler A, ein negativer Wert
	 * bedeutet eine Führung von Spieler B	 */
	private Integer scoreDelta;
	/** Die Karten, die in vorangegangen Runden auf den Unentschieden-Stapel gelegt wurden*/
	private Integer drawPoints;
	public Integer getLastCardOfA() {
		return lastCardOfA;
	}
	public void setLastCardOfA(Integer lastCardOfA) {
		this.lastCardOfA = lastCardOfA;
	}
	public Integer getLastCardOfB() {
		return lastCardOfB;
	}
	public void setLastCardOfB(Integer lastCardOfB) {
		this.lastCardOfB = lastCardOfB;
	}
	public ArrayList<Integer> getAnimalCardDeck() {
		return animalCardDeck;
	}
	public void setAnimalCardDeck(ArrayList<Integer> animalCardDeck) {
		this.animalCardDeck = animalCardDeck;
	}
	public ArrayList<Integer> getPlayerADeck() {
		return playerADeck;
	}
	public void setPlayerADeck(ArrayList<Integer> playerADeck) {
		this.playerADeck = playerADeck;
	}
	public ArrayList<Integer> getPlayerBDeck() {
		return playerBDeck;
	}
	public void setPlayerBDeck(ArrayList<Integer> playerBDeck) {
		this.playerBDeck = playerBDeck;
	}
	public Integer getCurrentAnimalCard() {
		return currentAnimalCard;
	}
	public void setCurrentAnimalCard(Integer currentAnimalCard) {
		this.currentAnimalCard = currentAnimalCard;
	}
	public Integer getScoreDelta() {
		return scoreDelta;
	}
	public void setScoreDelta(Integer scoreDelta) {
		this.scoreDelta = scoreDelta;
	}
	public Integer getDrawPoints() {
		return drawPoints;
	}
	public void setDrawPoints(Integer drawPoints) {
		this.drawPoints = drawPoints;
	}
	
	
	
}
