package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;

import player.HolsDerGeierSpieler;
import vultureUtil.BuzzardAnalyser;
import vultureUtil.BuzzardGameModel;
import vultureUtil.LeagueTableModel;
import vultureUtil.TableFactory;

public class SimulationGUI extends JPanel {
	private BuzzardAnalyser analyser;
	private JPanel panel;
	private BuzzardGameModel gameModel;
	private DealerStateManager dealer;
	private BuzzardSeriesInfo info;
	private MainController controller;
	private JFrame mainFrame;
	private JPanel mainPanel;
	private MouseListener mouseListener;
	
	/** Dieser Mouselistener reagiert, wenn ein Klick auf den Replaybutton erfolgt. */
	private MouseListener replayButtonListener; 
	
	/** Dies ist der Replaybutton */
	private JButton replayButton; 
	
	/** Auf dieses Panel wird der Replaybutton gelegt, 
	 * damit er nicht auf die breite des Fensters gezogen wird */
	private JPanel replayButtonPanel; 
	
	/** Dieser Timer ŸberprŸft in einem festgelegten Intervall, ob aktuell ein Spiel lŠuft und graut den 
	 * Replaybutton aus. Sobald das Spiel beendet ist wird der Replaybutton freigegeben.  */
	private Timer replayButtonTimer; 
	
	private LeagueTableModel ltm;
	/** Aktuelle Position des Spielers XYZ */
	private int playerPos;

	/** Array für die JTables */
	protected ArrayList<JTable> tables;

	/** Diese Variable speichert die Anzahl der geführten Tabellen */
	private int numOfTables;

	/** Das GridBagLayout, welches fuer das scoreTablePanel verwendet wird */
	private GridBagLayout tablePanelGBL;

	/**
	 * Das GridBagConstraints-Objekt, welches fuer das scoreTablePanel verwendet
	 * wird
	 */
	private GridBagConstraints tablePanelConstraints;

	/**
	 * Das GridBagLayout-Objekt, welches fuer das GesamtPanel (MainPanel)
	 * verwendet wird
	 */
	private GridBagLayout mainGBL;

	/**
	 * In diesem Panel werden die in JScrollPanes gehuellten JTables eingefuegt.
	 */
	private JPanel scoreTablePanel;

	/** In dieser ArrayList werden die StatPanels fuer die Spieler verwaltet */
	private ArrayList<PlayerStatPanel> statPanels;

	private JScrollPane scrPaneOne;
	private GridBagConstraints mainGBC;
	private JPanel statArea;
	private JScrollPane scrPaneTwo;
	private ArrayList<JScrollPane> scrollPaneList;

	public SimulationGUI(MainController controller, DealerStateManager dealer) {
		this.controller = controller;
		numOfTables = 0;
		this.dealer = dealer;
		mouseListener = new MyMouseListener();
		addMouseListener(mouseListener);
		replayButtonListener = new ReplayMouseListener();
		addMouseListener(replayButtonListener);
		controller.registerSimulationGUI(this);

		// info = new BuzzardSeriesInfo();
		// info.setNoOfMatches(5);
		// int[] cardValues = {-3,4,7,8,5,10};
		// info.setDeck(new BuzzardDeck(cardValues,BuzzardDeck.SHUFFLED));
		// info.addPlayer(new IntelligentBuzzardPlayer("a"));
		// info.addPlayer(new TestBot2("b"));
		// info.addPlayer(new StupidBot("c"));

		// dealer.addSeries(info);

		setLayout(new BorderLayout());
		// bigPanel.addComponentListener(new PanelResizeListener2());
		mainPanel = new JPanel();
		mainPanel.addMouseListener(mouseListener);
		mainGBL = new GridBagLayout();
		mainPanel.setLayout(mainGBL);
		mainGBC = new GridBagConstraints();

		scoreTablePanel = new JPanel();
		scoreTablePanel.addMouseListener(mouseListener);
		tablePanelGBL = new GridBagLayout();
		scoreTablePanel.setLayout(tablePanelGBL);

		scrPaneOne = new JScrollPane(scoreTablePanel);
		scrPaneOne.addMouseListener(mouseListener);
		mainGBC.gridx = 0;
		mainGBC.gridy = 1;
		mainGBC.gridwidth = 1;
		mainGBC.gridheight = 1;
		mainGBC.fill = GridBagConstraints.BOTH;
		mainGBC.weightx = 100;
		mainGBC.weighty = 75;

		mainGBL.setConstraints(scrPaneOne, mainGBC);
		mainPanel.add(scrPaneOne);

		System.out.println("statArea Created");
		this.statArea = new JPanel(new GridLayout(3, 3));

		mainGBC = new GridBagConstraints();
		mainGBC.gridx = 0;
		mainGBC.gridy = 0;
		mainGBC.gridwidth = 1;
		mainGBC.gridheight = 1;
		mainGBC.fill = GridBagConstraints.BOTH;
		mainGBC.weightx = 100;
		mainGBC.weighty = 25;
		scrPaneTwo = new JScrollPane(statArea);
		scrPaneTwo.addMouseListener(mouseListener);
		mainGBL.setConstraints(scrPaneTwo, mainGBC);
		mainPanel.add(scrPaneTwo);
		
		//Initialisierung von replayButton, replayButtonTimer und replayButtonPanel
		replayButton = new JButton ("replay");
		replayButton.addMouseListener(replayButtonListener);
		replayButtonTimer = new Timer(100, onTimerFired);
		replayButtonTimer.start();
		replayButton.setEnabled(false);
		
		replayButtonPanel = new JPanel();
		replayButtonPanel.add(replayButton);
		mainGBL.setConstraints(replayButtonPanel, mainGBC);

		add(BorderLayout.CENTER, mainPanel);
		//FŸgt das replayButtonPanel dem aktuellen Fenster hinzu.
		add(BorderLayout.PAGE_END, replayButtonPanel);
		scrollPaneList = new ArrayList<JScrollPane>();
		statPanels = new ArrayList<PlayerStatPanel>();
		tables = new ArrayList<JTable>();
		dealer.setPanel(this);
		setSize(800, 700);
		setLocation(100, 100);
		setVisible(true);
	}

	public void repaintMainFrame() {
		mainFrame.repaint();
	}
	/** Diese Methode erzeugt ein LeaguetableModel anhand des Ÿbergebenen TournamentTableDataClass files.
	 *  Die Position des Spielers kann dann durch eine Methode in dem LeagueTableModel ermittelt werden */
	public void setPlayerPos(TournamentTableDataClass table) {
		ltm = new LeagueTableModel(table);
	}
	/** Diese Methode liefert die aktuelle Position des Ÿbergebenen Spielernames */
	public int getPlayerPos(String name) {
		int pos;
		if (ltm == null)
			return 0;
		else {
			pos = ltm.getPlayerPos(name);
			return pos;
		}
	}
	public void registerMainFrame(JFrame mainFrame) {
		mainFrame.addMouseListener(mouseListener);
		this.mainFrame = mainFrame;
	}

	void start() {
		dealer.simulateNext();
	}

	protected JPanel getMainPanel() {
		return mainPanel;
	}

	protected GridBagLayout getMainGBL() {
		return mainGBL;
	}

	protected JPanel getScoreTablePanel() {
		return scoreTablePanel;
	}

	protected JScrollPane getScrPaneOne() {
		return scrPaneOne;
	}

	protected GridBagConstraints getMainGBC() {
		return mainGBC;
	}

	protected JPanel getStatArea() {
		return statArea;
	}

	protected JScrollPane getScrPaneTwo() {
		return scrPaneTwo;
	}

	protected ArrayList<JScrollPane> getScrollPaneList() {
		return scrollPaneList;
	}

	/**
	 * Diese Methode erwartet ein BuzzardGameModel als Parameter. Dieses
	 * BuzzardGameModel wird daraufhin als Datenbasis fuer eine JTable
	 * verwendet, die wiederum in das scoreTablePanel eingefuegt wird
	 * 
	 * @param model
	 *            Dieses Model stellt die Datenbasis fuer die erzeugte und
	 *            dargestellte Tabelle dar.
	 */
	public void addNewTable(BuzzardGameModel model) {
		numOfTables++;
		JTable tableTmp = TableFactory.getJTable(model);
		tableTmp.setRowHeight(20);
		tables.add(tableTmp);
		model.setOwner(tableTmp);
		JScrollPane scrollPane = new JScrollPane(tableTmp);
		scrollPaneList.add(scrollPane);
		GridBagConstraints myGBC = getGBC(0, numOfTables);
		tablePanelGBL.setConstraints(scrollPane, myGBC);
		scoreTablePanel.add(scrollPane);
		arrangeScrollPanels();
	}

	/**
	 * Erstellt aus einer simulierten BuzzardSeriesInfo eine GUI, die die
	 * Ergebnisse der BuzzardSeriesInfo darstellt.
	 * 
	 * @param info
	 *            Simulierte BuzzardSeriesInfo, die dargestellt werden soll.
	 */
	public void generateTablesFromBuzzardInfo(BuzzardSeriesInfo info) {
		int dataSize = info.getDataSize();
		for (int i = 0; i < dataSize; i++) {
			addNewTable(info.getData(i));
		}
	}

	/**
	 * Erstellt fuer jeden teilnehmenden Spieler der uebergebenen
	 * BuzzardSeriesInfo ein PlayerStatPanel und stellt es in diesem JPanel dar.
	 * 
	 * @param info
	 *            BuzzardSeriesInfo, die die Spieler enthaelt fuer welche die
	 *            StatPanels erstellt werden.
	 */
	protected void createStatPanel(BuzzardSeriesInfo info) {
		int numberOfPlayers = info.getPlayers().size();
		int numberOfMatches = info.getNoOfMatches();
		statArea.setLayout(new GridLayout(1, numberOfPlayers));
		ArrayList<HolsDerGeierSpieler> players = info.getPlayers();
		for (int i = 0; i < numberOfPlayers; i++) {
			//Hier wird nach der Position des Spielers i gesucht.
			//Dazu wird der Name des Spielers i an die Methode getPlayerPos Ÿbergeben.
			playerPos= getPlayerPos(players.get(i).getName());
		
			statPanels.add(new PlayerStatPanel(players.get(i).getName(),
					numberOfMatches,playerPos));
			statPanels.get(i).setBorder(
					BorderFactory.createLineBorder(Color.BLACK, 2));
			statArea.add(statPanels.get(i));
		}
		arrangeStatPanels();
	}

	protected void showNextBuzzard(int i) {
		statPanels.get(i).showNextBuzzard();
	}

	/**
	 * Entfernt alle Punktetabellen (JTables) und alle Spieler-StatistikPanels
	 * (StatPanels) vom Bildschirm. Sollte vom DealerStateTable und dessen
	 * Subklassen aufgerufen werden, um bei wiederholtem Aufbau (bei Simulation
	 * mehrerer Spiele) stets mit einer frischen SimulationGUI zu beginnen.
	 */
	protected void clearView() {
		statArea.removeAll();
		scoreTablePanel.removeAll();
		statPanels.clear();
		tables.clear();
	}

	protected void arrangeScrollPanels() {
		System.out.println("+++++++++++++ ARRANGE_SCROLL_PANELS_()");
		int width = tables.get(0).getPreferredSize().width;
		int height = tables.get(0).getPreferredSize().height + 20;
		Dimension pDim = new Dimension(width, height);
		JScrollPane tp;
		for (Iterator<JScrollPane> it = scrollPaneList.iterator(); it.hasNext();) {
			tp = it.next();
			tp.setPreferredSize(pDim);
			tp.setMaximumSize(pDim);
			tp.setMinimumSize(pDim);
		}
		revalidate();
		repaint();
		repaintMainFrame();
	}

	/**
	 * Diese Methode ist fuer das richtige Skalieren der StatPanels
	 * verantwortlich, welche die Anzahl der gewonnenen Spiele eines Spielers
	 * visualisieren
	 */
	protected void arrangeStatPanels() {
		System.out.println("+++++++++++++ ARRANGE_STAT_PANELS_()");
		int width = (int) getWidth() / 3;
		int height = (int) getHeight() / 6;

		Dimension dDim = new Dimension(width, height);
		Iterator<PlayerStatPanel> it = statPanels.iterator();
		PlayerStatPanel tmpStatPanel;
		while (it.hasNext()) {
			tmpStatPanel = it.next();
			tmpStatPanel.setPreferredSize(dDim);
			tmpStatPanel.setMinimumSize(dDim);
			tmpStatPanel.setMaximumSize(dDim);
		}
		scrPaneTwo.setPreferredSize(new Dimension(3 * width, height));
		revalidate();
		repaint();
		repaintMainFrame();
	}

	/**
	 * Zeigt auf dem StatPanel von Spieler mit der Nummer index den Gewinn einer
	 * Runde durch aufleuchten eines Pokals an
	 */
	public void showNextTrophy(int index) {
		statPanels.get(index).showNextBuzzard();
	}

	/** Convenience-Methode, die ein GridBagConstraints erzeugt */
	protected GridBagConstraints getGBC(int gridx, int gridy) {
		GridBagConstraints res = new GridBagConstraints();
		res.gridheight = 1;
		res.gridwidth = 1;
		res.anchor = GridBagConstraints.NORTH;
		res.gridx = gridx;
		res.gridy = gridy;
		res.ipadx = 0;
		res.ipady = 0;
		res.weightx = 50;
		res.weighty = 100;
		res.fill = GridBagConstraints.BOTH;
		return res;
	}

	/*
	 * Das Feuern dieses Listeners soll bewirken, dass nach einer simulierten
	 * Serie nach einem Klick die TournamentTable-Ansicht aufgerufen wird. Bis
	 * dieser Klick erfolgt bleibt jedoch die SimulaitonGUI sichtbar (Hierdurch
	 * wird dem User ermoeglicht, sich das Tableau beliebig lange anzusehen,
	 * bevor die TournamentTableView aufgerufen wird).
	 */
	

	/* Das Feuern dieses Listeners soll bewirken, dass nach einer simulierten Serie
	 * nach einem Klick die TournamentTable-Ansicht aufgerufen wird. Bis dieser Klick
	 * erfolgt bleibt jedoch die SimulaitonGUI sichtbar (Hierdurch wird dem User ermoeglicht, 
	 * sich das Tableau beliebig lange anzusehen, bevor die TournamentTableView aufgerufen wird).
	 */
	class MyMouseListener extends MouseAdapter{
		@Override
		public void mouseReleased(MouseEvent e){
			dealer.signalGoToTournamentTable();
		}
	}
	
	class ReplayMouseListener extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			if(replayButton.isEnabled()==true){
			System.out.println("replay pressed");
			if(dealer.isFinished())
				dealer.setIsFinished(false);
			controller.simNext();
			dealer.signalGoToTournamentTable();
			}
		}
	}
ActionListener onTimerFired = new ActionListener(){
		
		public void actionPerformed(ActionEvent evt){
			replayButton.setEnabled(dealer.isFinished());
			if(dealer.isTournament()==true)
				replayButtonPanel.setVisible(false);
				else replayButtonPanel.setVisible(true);
		}
		};

	
}
