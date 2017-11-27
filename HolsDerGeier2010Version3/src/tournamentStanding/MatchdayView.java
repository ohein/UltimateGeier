package tournamentStanding;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import player.HolsDerGeierSpieler;
import vultureUtil.TimePlanner;
import controller.BuzzardSeriesInfo;
import controller.MainController;
import controller.TournamentTableDataClass;

public class MatchdayView extends JPanel {

	private final MainController mainController;
	private final TournamentTableDataClass tournamentTable;
	private final JScrollPane mainScrollPane;
	private final GridBagLayout gbl = new GridBagLayout();
	private GridBagConstraints gbc;
	private final JPanel mainPanel = new JPanel();

	public MatchdayView(MainController mainController,
			TournamentTableDataClass tournamentTable) {
		this.mainController = mainController;
		this.tournamentTable = tournamentTable;
		this.setLayout(gbl);
		gbc = createGBC(0, 0, 1, 1, 100, 100);
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.BOTH;

		mainPanel.setLayout(new GridLayout());

		this.mainScrollPane = new JScrollPane(mainPanel);
		gbl.setConstraints(this.mainScrollPane, gbc);

		TimePlanner timePlanner = TimePlanner.getInstance();

		JTabbedPane matchdayTabbedPane = new JTabbedPane();

		String[] columnNames = null;
		String[][] data;

		int playerCount = timePlanner.getMatchList().get(0).getPlayers().size();

		// Inhalt d. Tabs
		for (int i = 0; i < timePlanner.getDayCount(); i++) {
			JPanel matchdayTabbedPaneContent = new JPanel();
			matchdayTabbedPaneContent.setLayout(new BorderLayout());
			// GRöße des Data Objects festlegen
			data = new String[timePlanner.getMatchCount(i)][playerCount * 2];
			// Inhalt d. Tabhellen bzw. des Data Objekt
			ArrayList<BuzzardSeriesInfo> matchList = timePlanner.getMatches(i);
			for (int j = 0; j < timePlanner.getMatchCount(i); j++) {
				// Header
				columnNames = new String[playerCount * 2];
				for (int j2 = 0; j2 < playerCount * 2; j2++) {
					if (j2 < playerCount) {// Player Nummerieren
						columnNames[j2] = ("Player " + (j2 + 1));
					} else {
						columnNames[j2] = ("P. " + (j2 + 1 - playerCount) + " score");
					}

				}
				// Matches
				for (int j2 = 0; j2 < timePlanner.getMatches(i).size(); j2++) {
					ArrayList<HolsDerGeierSpieler> players = matchList.get(j2)
							.getPlayers();
					for (int k = 0; k < playerCount * 2; k++) {
						if (k < playerCount) {
							data[j2][k] = players.get(k).getName();

						} else {
							if (matchList.get(j2).isFinished()) {
								data[j2][k] = ""
										+ matchList.get(j2)
												.getPointsMadeForPlayer(
														players.get(k
																- playerCount));
							} else {
								data[j2][k] = "outstanding";
							}
						}
					}
				}
			}
			JTable table = new JTable(data, columnNames);
			table.setEnabled(false);
			matchdayTabbedPaneContent.add(new JScrollPane(table));
			matchdayTabbedPane.add("Spieltag " + (i + 1),
					matchdayTabbedPaneContent);
		}

		this.mainPanel.add(matchdayTabbedPane);
		this.add(mainScrollPane);

		JButton continueBtn = new JButton("Continue");
		continueBtn.addActionListener(new ContinueButtonActionListener());
		gbc = createGBC(0, 1, 1, 1, 0, 0);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbl.setConstraints(continueBtn, gbc);
		this.add(continueBtn);

		JButton backBtn = new JButton("<< zurück");
		backBtn.addActionListener(new backButtonActionListener());
		gbc = createGBC(0, 1, 1, 1, 0, 0);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.SOUTHWEST;
		gbl.setConstraints(backBtn, gbc);
		this.add(backBtn);

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

	public TournamentTableDataClass getTournamentTable() {
		return this.tournamentTable;
	}

	class ContinueButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			mainController.signalSimIns();
		}
	}

	class backButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			mainController.displayTournamentStanding(mainController
					.getMatchdayView().getTournamentTable());
		}
	}

}
