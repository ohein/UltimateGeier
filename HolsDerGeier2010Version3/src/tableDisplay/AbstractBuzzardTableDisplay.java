package tableDisplay;

import javax.swing.JDialog;
import player.HolsDerGeierSpieler;

public abstract class AbstractBuzzardTableDisplay extends JDialog{
	public abstract int seatPlayer(HolsDerGeierSpieler player);
	public abstract void removePlayer(HolsDerGeierSpieler player);
	public abstract void showAllCards();
	public abstract void showCardBack(HolsDerGeierSpieler player, int value);
	public abstract void clearTable();
	public abstract void deckShowNext(int value);
	public abstract void startScoreAnimation(HolsDerGeierSpieler player, int value);
	public abstract void clearCardOfPlayer(HolsDerGeierSpieler player);
	public abstract void startDrawAnimation();
}
