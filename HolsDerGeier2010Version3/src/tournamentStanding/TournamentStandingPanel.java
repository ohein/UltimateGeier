package tournamentStanding;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import player.HolsDerGeierSpieler;
import vultureUtil.LeagueTableModel;
import vultureUtil.TableFactory;
import controller.BuzzardSeriesInfo;
import controller.BuzzardTournamentInfo;
import controller.MainController;
import controller.TournamentTableDataClass;

public class TournamentStandingPanel extends JPanel {
	private final MainController mainController;
	private TournamentTableDataClass tournamentTable;
	private JTable leagueTable;
	private JScrollPane leagueTableScrPane;
	private JScrollPane leagueTableScrPane2;
	private JButton continueBtn;
	private JButton saveBtn;
	
	// group3
	private JButton matchdayBtn;
	// @author Gruppe 1
	// Zeiger auf benötigte Instanzen
	private BuzzardTournamentInfo bti;
	private ArrayList<BuzzardSeriesInfo> matchups;
	private ArrayList<HolsDerGeierSpieler> player;

	private final JLabel nextLabel = new JLabel(""); // neues Label

	private final GridBagLayout gbl;
	private GridBagConstraints gbc;

	public TournamentStandingPanel(MainController mainController) {
		this.mainController = mainController;
		tournamentTable = null;
		gbl = new GridBagLayout();
		setLayout(gbl);
	}

	public void updatePlayer() {
		String um = "Upcoming Match:       ";
		String vs = "    vs    ";
		// Wenn nur eine Serie oder FastSim
		if (matchups.size() > 0) {
			// Zeiger auf ArrayListPlayer
			player = matchups.get(0).getPlayers();
			// Ausgabe des nächsten Lineups
			nextLabel.setText(um + player.get(0) + vs + player.get(1));
		} else {
			nextLabel.setText(um + "Tournament finished!");
		}
	}

	public void recreateView(TournamentTableDataClass tournamentTable) {
		// Wird ein mal am Anfang aufgerufen, um den TournamentTable zu
		// erstellen
		if (tournamentTable != this.tournamentTable) {
			this.tournamentTable = tournamentTable;
			removeAll();
			leagueTable = TableFactory.getLeagueTable(tournamentTable);
			leagueTableScrPane = new JScrollPane(leagueTable);
			leagueTableScrPane2 = new JScrollPane(leagueTableScrPane);
			gbc = createGBC(0, 0, 1, 1, 100, 100);
			gbc.insets = new Insets(10, 10, 10, 10);
			gbc.fill = GridBagConstraints.BOTH;
			gbl.setConstraints(leagueTableScrPane2, gbc);
			add(leagueTableScrPane2);

			continueBtn = new JButton("Continue");
			continueBtn.addActionListener(new ContinueButtonActionListener());

			saveBtn = new JButton("Save");
			saveBtn.addActionListener(new SaveGameListener());

			// @author Gruppe 1
			// Zeiger
			bti = mainController.nextPlayer();
			matchups = bti.getMatchups();
			updatePlayer();

			// Schriftartenobjekte erstellen und adden
			Font nlFont = new Font("Dialog", 0, 14);
			nextLabel.setFont(nlFont);

			JPanel btnPane = new JPanel();
			JPanel feuerbach = new JPanel();
			btnPane.setLayout(new GridLayout(2, 2));
			// Label wird geaddet
			btnPane.add(nextLabel);
			btnPane.add(feuerbach);

			feuerbach.setLayout(new FlowLayout());
			feuerbach.add(continueBtn);
			feuerbach.add(saveBtn);
			
			// Group3
			matchdayBtn = new JButton("Spieltage anzeigen >>");
			matchdayBtn.addActionListener(new MatchdayButtonActionListener());
			feuerbach.add(matchdayBtn);
			//
			
			gbc = createGBC(0, 1, 1, 1, 0, 0);
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.CENTER;
			gbl.setConstraints(btnPane, gbc);
			add(btnPane);
			revalidate();
			// Wird ab der zweiten Runde jede Runde aufgerufen, bis das Tunier
			// beenendet ist
			// Aktualisiert den Table
		} else {

			((LeagueTableModel) leagueTable.getModel()).update();
			revalidate();
			updatePlayer();
		}
	}

	/**
	 * Wird aufgerufen, wenn letzte Runde gespielt wurde. Setzt den
	 * Continue-Button auf "Back to main"
	 * 
	 * Gruppe 2
	 */
	public void lastRound() {
		continueBtn.setText("Back to main");
		continueBtn.addActionListener(new ContinueToMainListener());
	}

	private GridBagConstraints createGBC(int gridx, int gridy, int gridwidth,
			int gridheight, int weightx, int weighty) {
		GridBagConstraints res = new GridBagConstraints();
		res.gridx = gridx;
		res.gridy = gridy;
		res.gridwidth = gridwidth;
		res.gridheight = gridheight;
		res.weightx = weightx;
		res.weighty = weighty;
		return res;
	}

	class ContinueButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			mainController.signalSimIns();
		}
	}

	/**
	 * Wird dem "Back to main"-Button hinzugefügt.
	 * 
	 * @author Gruppe 2
	 */
	class ContinueToMainListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (JOptionPane.showConfirmDialog(leagueTableScrPane2,
					"Zurückkehren zum Hauptmenü", "Achtung",
					JOptionPane.OK_CANCEL_OPTION) == 0) {
				mainController.resetToTitle();
			}
		}

	}

	// group3
	class MatchdayButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			mainController.displayMatchdayView(tournamentTable);
		}
	}
	/**
	 * Ruft den Dialog zum Speichern des Turniers auf
	 * 
	 * @author Gruppe 2
	 * 
	 */
	class SaveGameListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				mainController.saveTournament();
			} catch (IOException e1) {
				System.out.println("Fehler beim Speichern.");
				JOptionPane.showMessageDialog(leagueTableScrPane2,
						"Fehler beim Speichern!");
				e1.printStackTrace();
			}
		}

	}
}
