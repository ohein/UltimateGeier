package tableDisplay;

import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import player.*;
import java.applet.*;

public class BuzzardTable extends AbstractBuzzardTableDisplay{

	/** Horizontaler Abstand zwischen den DisplayPlayerPanels*/
	private static final int H_GAP = 50;
	/** Vertikaler Abstand zwichen den DisplayPlayerPanels*/
	private static final int V_GAP = 110;
	
	/** Zusaetzlicher Abstand der obersten Reihe DisplayPlayerPanels zu den Insets*/
	private static final int TOP_INSET = 30;
	
	/** Zusaetzlicher Abstand der DisplayPlayerPanels zu den linken Insets*/
	private static final int LEFT_INSET = 40;
	
	/** Auf diesem Layer findet die Gewinn-/Verlustanimation statt*/
	private AnimationLayer animationLayer;
	
	/** Array mit den DisplayPlayer2Panels	 */
	private DisplayPlayerPanel2[] pPanels;
	
	/** Die LayeredPane dieses Frames (fuer HG und Ani sind versch. Ebenen noetig	 */
	private JLayeredPane layeredPane;
	
	/** Der Stack des Tisches	 */
	private DisplayStackPanel stackPanel;
	
	/** Die TischGrafik*/
	private JLabel tableLabel;
	
	/** Dieses Array dient zum Mappen der Reihenfolge-Nummer auf die Platznummer 
	 *  eines Spielers, es wird im Konstruktor durch die Methode initializeMappingArray()
	 *  initialisiert	 */
	private int[] mappingArray;
	
	/** In dieser Hashtable wird gespeichert, an welchem Platz welcher Spieler sitzt*/
	private Hashtable<HolsDerGeierSpieler, Integer> playerHash;
	
	/** In dieser Liste werden die BuzzardPlayer des Tisches verwaltet*/
	private ArrayList<HolsDerGeierSpieler> playerList;
	
	/** Speichert die Anzahl der am Tisch sitzenden Spieler*/
	private int playerCnt;
	
	/** SoundDatei, welche beim Legen einer Karte abgespielt werden soll */
	private AudioClip cardLay;
	
	/** SoundDatei, welche beim Aufdecken der von den Spielern gelegten Karten
	 *  abgespielt wird.
	 */
	private AudioClip cardsOpen;
	
	
	
	/** Konstruktor*/
	public BuzzardTable(){
		loadSoundFiles();
		playerCnt=0;
		setModal(false);
		initializeMappingArray();
		playerHash = new Hashtable<HolsDerGeierSpieler, Integer>();
		playerList = new ArrayList<HolsDerGeierSpieler>();
		layeredPane = getLayeredPane();
		layeredPane.setLayout(null);
		layeredPane.setVisible(true);
		//setContentPane(layeredPane);
		initializePanels();
		Insets insets = getInsets();
		Dimension d = new Dimension((insets.left+insets.right+3 * pPanels[1].getPreferredSize().width + 2* H_GAP + 2*LEFT_INSET+30),
				(insets.top+insets.bottom+3*pPanels[1].getPreferredSize().height + 2 * V_GAP + 2*TOP_INSET+40));
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		tableLabel = new JLabel();
		tableLabel.setIcon(new ImageIcon("src/graphics/tisch2.jpg"));
		tableLabel.setBounds(0+insets.left,0+insets.top,1080,691);
		setSize(d);
		initializeAnimationPanel(d);
		layeredPane.setLayer(tableLabel, JLayeredPane.DEFAULT_LAYER-1);
		layeredPane.add(tableLabel);
		
	}
	
	/** Entfernt alle Spieler vom Tisch und leert zusaetzlich die playerHash sowie die 
	 * playerList.
	 */
	@Override
	public void clearTable(){
		for(int i=0;i<playerCnt;i++){
			pPanels[mappingArray[i]].removePlayer();
		}
		// Die Anzahl der am Tisch sitzenden Spieler soll ja wieder auf 0 gesetzt werden
		playerCnt=0;
		// Die Spieler müssen sowohl aus der ArrayList als auch aus der Hashtable entfernt werden
		playerHash.clear();
		playerList.clear();
		
	}
	
	/** 
	 * Weist das Deck an, als naechstes die Karte mit dem Wert <<value>> zu zeigen. Wird als 
	 * Wert die <<0>> uebergeben, so entspricht dies einem Aufruf der Methode <<deckClear()>>.
	 * @param value Wert der Karte, die als Naechste dargestellt wird.
	 */
	@Override
	public void deckShowNext(int value){
		cardLay.play();
		stackPanel.setCurrentValue(value);
	}
	
	/**
	 * Sorgt dafuer, dass nur die verdeckt liegenden Karten des Decks zu sehen sind.
	 */
	public void deckClear(){
		stackPanel.setCurrentValue(0);
	}
	
	private void loadSoundFiles(){
		try{
			File f = new File("src/sounds/1A_20009.wav");
			URL url = f.toURI().toURL();
			System.out.println(url);
			cardLay = Applet.newAudioClip(url);
			
			f = new File("src/sounds/1A_20010.wav");
			url = f.toURI().toURL();
			cardsOpen = Applet.newAudioClip(url);
			
			System.out.println("CardLay: " +cardLay);
		}catch(MalformedURLException mfe){
			mfe.printStackTrace();
		}
	}
	
	private void initializeMappingArray(){
		mappingArray = new int[8];
		mappingArray[0] = 0;
		mappingArray[1] = 7;
		mappingArray[2] = 5;
		mappingArray[3] = 2;
		mappingArray[4] = 3;
		mappingArray[5] = 4;
		mappingArray[6] = 6;
		mappingArray[7] = 1;
	}
	
	/** Erzeugt einen neuen AnimationLayer, auf dem die Gewinn-/Verlustanimation stattfindet und 
	 *  fuegt diesem der LayeredPane hinzu. 
	 * @param d Dimension des AnimationLayer
	 */
	private void initializeAnimationPanel(Dimension d){
		animationLayer = new AnimationLayer(pPanels,d,stackPanel);
		animationLayer.setBounds(0,0,d.width,d.height);
		layeredPane.setLayer(animationLayer, JLayeredPane.DEFAULT_LAYER+1);
		layeredPane.add(animationLayer);
	}
	
	/** Animation, die dafuer sorgt, dass der punktende Spieler auf seinem Panel
	 *  eine Animation des von ihm erspielten Wertes erhaelt
	 * @param panelNr Platz des Spielers, fuer den die Animation angezeigt 
	 *        werden soll.
	 * @param value Wert, den der Spieler erspielt hat. 
	 */
	private void startScoreAnimation(int panelNr, int value){
		animationLayer.startAnimation(value, pPanels[panelNr].getLocation());
		pPanels[panelNr].setPoints(pPanels[panelNr].getPoints()+value);
	}
	
	/** Startet die Animation, welche dafuer sorgt, dass die Karte auch zu 
	 *  ihrem entsprechenden Gewinner wandert.
	 * @param panelNr Platz des Spielers, der die Karte erhalten soll.
	 */
	private void startScoreAnimationCard(int panelNr){
		System.out.println("startScoreAnimation(2) ivoked");
		animationLayer.startAnimation1(pPanels[panelNr]);
	}
	
	@Override
	public void startDrawAnimation(){
		animationLayer.startDrawAnimation();
	}
	
	/** Sorgt dafuer, dass die <<Spieler-Erhaelt-Karte-Sequenz>> gestartet 
	 *  wird. 
	 *  @param player Spieler, fuer den diese Animationssequenz ausgefuehrt 
	 *         wird.
	 *  @param value Wert, fuer den diese Animationssequenz ausgefuehrt 
	 *         wird.
	 */
	@Override
	public void startScoreAnimation(HolsDerGeierSpieler player, int value){
		System.out.println("startScoreAnimation() invoked");
		startScoreAnimationCard(playerHash.get(player));
		startScoreAnimation(playerHash.get(player), value);
	}
	
	private void initializePanels(){
		pPanels = new DisplayPlayerPanel2[8];
		Insets insets = getInsets();
		int cnt = 0;
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				if((i==1)&&(j==1)){
					continue;
				}
				pPanels[cnt] = new DisplayPlayerPanel2(cnt);
				pPanels[cnt].setBounds(insets.left + LEFT_INSET + i*(pPanels[cnt].getPreferredSize().width+H_GAP),
						insets.top + TOP_INSET + j*(pPanels[cnt].getPreferredSize().height+V_GAP),
						pPanels[cnt].getPreferredSize().width,
						pPanels[cnt].getPreferredSize().height);
				System.out.println(pPanels[cnt].getBounds());
				layeredPane.setLayer(pPanels[cnt], JLayeredPane.DEFAULT_LAYER);
				layeredPane.add(pPanels[cnt]);
				cnt++;
			}
		}
		
		stackPanel = new DisplayStackPanel(25);
		stackPanel.setBounds(insets.left+LEFT_INSET+ ((int)(1.45*pPanels[0].getPreferredSize().width)) + (H_GAP/2),
				        insets.top+TOP_INSET+ pPanels[0].getPreferredSize().height + (V_GAP/2),
				        stackPanel.getPreferredSize().width,
				        stackPanel.getPreferredSize().height
				        );
		layeredPane.setLayer(stackPanel,JLayeredPane.DEFAULT_LAYER);
		layeredPane.add(stackPanel);
	}
	
	/** Platziert einen Spieler am Tisch und gibt die Platznummer des Spielers zurueck*/
	@Override
	public int seatPlayer(HolsDerGeierSpieler player){
		playerCnt++;
		playerList.add(player);
		int orderNumber = playerList.size() -1;
		pPanels[mappingArray[orderNumber]].seatPlayer(player);
		playerHash.put(player, mappingArray[orderNumber]);
		return mappingArray[orderNumber];
	}
	
	/** Zeigt die Rueckansicht einer Karte und ermoeglicht dem Panel, sich den uebergebenen 
	 *  Wert zu merken. 
	 */
	@Override
	public void showCardBack(HolsDerGeierSpieler player, int value){
		System.out.println(player);
		int panelNumber = playerHash.get(player);
		cardLay.play();
		pPanels[panelNumber].showCardBack(value);
	}
	
	/** Entfernt die Karte eines Spielers am Tisch, so dass dessen Bietkartenfeld leer ist*/
	@Override
	public void clearCardOfPlayer(HolsDerGeierSpieler player){
		int panelNumber = playerHash.get(player);
		pPanels[panelNumber].clearCard();
	}
	
	/** Dreht die Rueckansichten aller Karten um*/
	@Override
	public void showAllCards(){
		try{
			Thread.sleep(200);
		}catch(InterruptedException ie){
			ie.printStackTrace();
		}
		cardsOpen.play();
		for(int i=0;i<playerCnt;i++){
			pPanels[mappingArray[i]].showCurrentCard();
		}
	}
	
	@Override
	public void removePlayer(HolsDerGeierSpieler player){
		playerList.remove(player);
		playerHash.remove(player);
		playerCnt--;
	}
	
	
//	public static void main(String[] args){
//		BuzzardTable d = new BuzzardTable();
//		BuzzardPlayer ford = new IntelligentBuzzardPlayer("Ford");
//		BuzzardPlayer fulkerson = new IntelligentBuzzardPlayer("Fulkerson");
//		d.seatPlayer(ford);
//		d.seatPlayer(fulkerson);
//		try{
//			Thread.sleep(2000);
//		}catch(InterruptedException ie){
//			ie.printStackTrace();
//		}
//		d.showCardBack(ford, 5);
//		try{
//			Thread.sleep(1500);
//		}catch(InterruptedException ie){
//			ie.printStackTrace();
//		}
//		d.showCardBack(fulkerson, 7);
//		try{
//			Thread.sleep(1500);
//		}catch(InterruptedException ie){
//			ie.printStackTrace();
//		}
//		d.showAllCards();
//		for(int i=0;i<8;i++){
//			d.startScoreAnimation(i, 5);
//			try{
//				Thread.sleep(1500);
//			}
//			catch(InterruptedException ie){
//				ie.printStackTrace();
//			}
//		}
//	}
	
	
	
}
