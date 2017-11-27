package controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import tableDisplay.AbstractBuzzardTableDisplay;
import tableDisplay.BuzzardTable;
import tournamentStanding.MatchdayView;
import tournamentStanding.TournamentStandingPanel;
import dealerStackGUI.DealerStackPanel;
import deckConfiguration.DeckDialog;
import enrollGUI.GameEnrollDialog;
import enrollGUI.NetworkEnrollDialog;
import enrollGUI.NetworkEnrollDialogClient;
import enrollGUI.TournamentEnrollDialog;
import gui.BuzzardTournamentFileChooser;

public class MainController {
	/** Verantwortlich fuer die Logik */
	private final DealerStateManager dealer;
	/** Fenster, in dem eine Serie visualisiert wird */
	private SimulationGUI simulationGUI;
	/** Dialog zur Erstellung einer Series */
	private final GameEnrollDialog enDialog;
	/** Der Dialog zur Erstellung eines Turniers */
	private final TournamentEnrollDialog toDialog;
	/** Der Dialog zur Erstellung eines Netzwerk*/
	/**
	 * Variablen zum einbinden der GUI-Fenster für die Netzwerk geschichte
	 * @author Johannes Hohmann
	 * @datum 25.06.12
	 */ 
	private NetworkEnrollDialog neDialog;
	private NetworkEnrollDialogClient clDialog;
	
	
	/** In diesem Panel wird der Inhalt des Dealer-Stacks visualisiert*/
	private final DealerStackPanel dealerStackPanel;
	/** Das Titelbild*/
	private final TitlePanel titlePanel;
	/** Der Frame, welcher in Turnieren zwischen zwei Serien gezeigt wird*/
	private final TournamentStandingPanel tournamentStandingPanel;
	/** Der Dialog, in dem das Deck konfiguriert werden kann */
	private final DeckDialog deckDialog;
	
	// group3
	/** Der Frame, welcher die Spieltagesübersicht anzeigt */
	private MatchdayView matchdayView;
	//
	/**
	 * Das Buzzard-Table-Fenster, welches die Kartenausspielung grafisch
	 * darstellt
	 */
	private final AbstractBuzzardTableDisplay buzzardTable;

	/** Der HauptFrame */
	JFrame mainFrame;

	public MainController() {
		dealer = new DealerStateManager(this);
		deckDialog = new DeckDialog(mainFrame, "Deck Configurator");
		buzzardTable = new BuzzardTable();
		dealer.setBuzzardTable(buzzardTable);
		simulationGUI = new SimulationGUI(this, dealer);
		initMainFrame();
		dealerStackPanel = new DealerStackPanel(dealer, this);
		tournamentStandingPanel = new TournamentStandingPanel(this);
		titlePanel = new TitlePanel();
		enDialog = new GameEnrollDialog(mainFrame, this);
		toDialog = new TournamentEnrollDialog(mainFrame, this);
		mainFrame.getContentPane().add(titlePanel, BorderLayout.CENTER);

		/**
		 * Erstellen der GUI-Fenster für das Netzwerkspiel
		 * @author Johannes Hohmann
		 * @datum 25.06.12
		 */ 
		neDialog = new NetworkEnrollDialog(mainFrame,this);
		clDialog = new NetworkEnrollDialogClient(mainFrame,this);
		mainFrame.getContentPane().add(titlePanel,BorderLayout.CENTER);
		mainFrame.setJMenuBar(generateMenuBar());
		mainFrame.setVisible(true);
		simulationGUI.registerMainFrame(mainFrame);
		buzzardTable.setLocation(
				mainFrame.getLocation().x + mainFrame.getSize().width,
				mainFrame.getLocation().y);
	}

	// @author Gruppe 1
	public BuzzardTournamentInfo nextPlayer() {
		return dealer.getTournament();
	}

	public void signalSimIns() {
		dealer.signalSimIns();
	}

	public void displayTournamentStanding(TournamentTableDataClass table) {
		simulationGUI.setPlayerPos(table);
		tournamentStandingPanel.recreateView(table);
		if (!dealer.getTournament().hasNextMatchUp()) {
			tournamentStandingPanel.lastRound();
		}
		mainFrame.getContentPane().removeAll();
		mainFrame.getContentPane().add(tournamentStandingPanel,
				BorderLayout.CENTER);
		tournamentStandingPanel.revalidate();
		mainFrame.repaint();
	}

	// group3
	public void displayMatchdayView(TournamentTableDataClass tournamentTable) {
		System.out.println("displayMatchdayView()");
		mainFrame.getContentPane().removeAll();
		matchdayView = new MatchdayView(this, tournamentTable);
		mainFrame.add(matchdayView, BorderLayout.CENTER);
		matchdayView.revalidate();
		mainFrame.repaint();
	}

	public MatchdayView getMatchdayView() {
		return this.matchdayView;
	}

	//


	public void displaySimulationGUI() {
		mainFrame.getContentPane().removeAll();
		mainFrame.getContentPane().add(simulationGUI);
		// System.out.println(simulationGUI);
	}

	public void simNext() {
		displaySimulationGUI();
		dealer.simulateNext();
	}

	public void pushBuzzardSeriesInfo(BuzzardSeriesInfo info) {
		dealer.addSeries(info);
		mainFrame.getContentPane().removeAll();
		// System.out.println("Gleich sollte das BuzzardStackPanel zu sehen sein");
		dealerStackPanel.init();
		mainFrame.getContentPane().add(dealerStackPanel);
		dealerStackPanel.revalidate();
		mainFrame.repaint();
	}

	private JMenuBar generateMenuBar() {
		JMenuBar res = new JMenuBar();
		JMenu newGame = new JMenu("New");
		res.add(newGame);

		JMenuItem generateSeries = new JMenuItem("Erzeuge Serie");
		generateSeries.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enDialog.setLocationRelativeTo(mainFrame);
				enDialog.setVisible(true);
			}
		});
		JMenuItem generateTournament = new JMenuItem("Erzeuge Turnier");

		generateTournament.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				toDialog.setLocationRelativeTo(mainFrame);
				toDialog.setVisible(true);
			}
		});

		/*
		 * Gruppe 2 Laden in das Menue eingefuegt, Datei wird ausgewaehlt und
		 * nach Ueberpruefung ob lesbar und verfuegbar an die Methode
		 * loadTournament uebergeben
		 */
		JMenuItem loadTournament = new JMenuItem("Lade Turnier");
		loadTournament.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					loadTournament();
				} catch(IOException ex) {
					JOptionPane.showMessageDialog(mainFrame,
						"Die Datei konnte nicht geladen werden!",
						"Fehler beim Laden der Datei",
						JOptionPane.ERROR_MESSAGE);
				} catch(ClassNotFoundException ex) {
					JOptionPane.showMessageDialog(mainFrame,
						"Falsches Dateiformat!",
						"Fehler beim Laden der Datei",
						JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
			
		/**
		 * Erstellen des Menues für das Netzwerkspiel
		 * @author Johannes Hohmann
		 * @version 1
		 * @datum 12.03.2012
		 */
		JMenuItem generateNetworkServer = new JMenuItem("Erzeuge Server");
		generateNetworkServer.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				neDialog.setLocationRelativeTo(mainFrame);
				neDialog.startThread();
				neDialog.setVisible(true);
			}
		});		

		JMenuItem generateNetworkClient = new JMenuItem("Erzeuge Client");
		generateNetworkClient.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				clDialog.setLocationRelativeTo(mainFrame);
				clDialog.setVisible(true);
			}
		});		

		
		newGame.add(generateSeries);
		newGame.add(generateTournament);
		newGame.add(loadTournament);

		// Erstellen eines Untermenu für das Netzwerkspiel
		JMenu newNetwork = new JMenu("Erzeuge Netwerk");
		newGame.add(newNetwork);
		newNetwork.add(generateNetworkServer);
		newNetwork.add(generateNetworkClient);
		
		
		
		JMenu settings = new JMenu("Settings");
		res.add(settings);
		JMenuItem configureDeck = new JMenuItem("Configure Deck");

		configureDeck.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				deckDialog.setVisible(true);
			}
		});
		settings.add(configureDeck);

		return res;
	}

	private void initMainFrame() {
		mainFrame = new JFrame();

		mainFrame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent ev){
				ev.getWindow().setVisible(false);
				ev.getWindow().dispose();
				System.exit(0);
			}
		});
		mainFrame.setBounds(100, 100, 730, 700);
	}

	public DealerStateManager getDealer() {
		return dealer;
	}

	public void registerSimulationGUI(SimulationGUI testGUI) {
		this.simulationGUI = testGUI;
	}

	public static void main(String[] args) {
		MainController main = new MainController();
	}

	/**
	 * Setzt das Fenster wieder zurueck auf den Title-Screen.
	 * 
	 * Gruppe 2
	 */
	public void resetToTitle() {
		mainFrame.getContentPane().removeAll();
		mainFrame.getContentPane().add(titlePanel);
		mainFrame.repaint();
	}

	/**
	 * Gruppe 2:
	 * Speichert das aktuelle im DealerStateManager vorhandene Turnier mit
	 * Dateiauswahl auf die Festplatte
	 * @throws IOException
	 */
	public void saveTournament() throws IOException{
		BuzzardTournamentFileChooser fileChooser = new BuzzardTournamentFileChooser();
		fileChooser.saveTournament(dealer.getTournament());
	}
	
	/**
	 * Gruppe 2:
	 * Laedt ein Turnier mit Dateiauswahl von der Festplatte
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void loadTournament() throws IOException, ClassNotFoundException {
		BuzzardTournamentFileChooser fileChooser = new BuzzardTournamentFileChooser();
		BuzzardTournamentInfo info = fileChooser.loadTournament();
		if (info != null) {
			dealer.addSeries(info);
			this.simNext();
		}
	}
}
