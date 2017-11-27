package controller;

import java.util.ArrayList;

import vultureUtil.ScheduleGenerators;
import vultureUtil.TimePlanner;
import vultureUtil.TournamentScheduleGenerator;

public class BuzzardTournamentInfo extends BuzzardSeriesInfo {
	// Team Altenhof, Jablonski, Pfeffer->
	// private final Queue<BuzzardSeriesInfo> matchups;
	private final ArrayList<BuzzardSeriesInfo> matchups;
	// <-
	private int playersPerMatch;
	private boolean tournamentGenerated;
	private TournamentTableDataClass tournamentTable;
	private TournamentScheduleGenerator gen; // @author Gruppe1

	// Gruppe 2: Flag, ob das Turnier gerade deserialisiert (z.B. von Festplatte
	// geladen) wurde
	private boolean deserialized = false;

	// Team Altenhof, Jablonski, Pfeffer->
	private final TimePlanner timePlanner = TimePlanner.getInstance();

	// <-

	public BuzzardTournamentInfo() {
		isTournament = true;
		// Team Altenhof, Jablonski, Pfeffer->
		// matchups = new PriorityQueue<BuzzardSeriesInfo>();
		matchups = new ArrayList<BuzzardSeriesInfo>();
		// <-
		tournamentGenerated = false;
	}

	// @author Gruppe1
	public TournamentScheduleGenerator getGen() {
		return gen;
	}

	// @author Gruppe1
	public ArrayList<BuzzardSeriesInfo> getMatchups() {
		return matchups;
	}

	public boolean hasNextMatchUp() {
		return !(matchups.isEmpty());
	}

	public BuzzardSeriesInfo getNextMatchUp() {
		// Team Altenhof, Jablonski, Pfeffer->
		// return matchups.poll();
		// return matchups.get(0);
		return matchups.remove(0);
		// <-
	}

	public void addMatchUp(BuzzardSeriesInfo info) {
		matchups.add(info);
	}

	public void setPlayersPerMatch(int ppm) {
		this.playersPerMatch = ppm;
	}

	public int getPlayersPerMatch() {
		return playersPerMatch;
	}

	// Team Altenhof, Jablonski, Pfeffer->
	/**
	 * Returns the TimePlanner for this tournament
	 * 
	 * @return
	 */
	public TimePlanner getTimePlanner() {
		return timePlanner;
	}

	// <-

	public void generateTournament() {
		tournamentTable = new TournamentTableDataClass(players);
		TournamentScheduleGenerator gen = ScheduleGenerators.getGenerator(
				players.size(), playersPerMatch);
		int[] indices;
		BuzzardSeriesInfo tmpBSI;
		while (gen.hasMore()) {
			indices = gen.getNext();
			tmpBSI = new BuzzardSeriesInfo();
			tmpBSI.setDeck(getDeck());
			tmpBSI.setNoOfMatches(getNoOfMatches());
			tmpBSI.setSimulated(isSimulated());
			tmpBSI.setWaitingTime(waitingTime);
			for (int i = 0; i < indices.length; i++) {
				tmpBSI.addPlayer(players.get(indices[i]));
			}
			// Team Altenhof, Jablonski, Pfeffer->
			timePlanner.addMatch(tmpBSI);
			// <-
			matchups.add(tmpBSI);
		}
		tournamentGenerated = true;
	}

	public boolean isTournamentGenerated() {
		return tournamentGenerated;
	}

	public TournamentTableDataClass getTournamentTableDataClass() {
		return tournamentTable;
	}

	/**
	 * Gruppe 2: Gibt an, ob das Turnier gerade deserialisiert (z.B. von
	 * Festplatte geladen) wurde
	 * 
	 * @return true, wenn deserialisiert, sonst false
	 */
	public boolean isDeserialized() {
		return deserialized;
	}

	/**
	 * Guppe 2: Setzt, ob das Turnier gerade deserialisiert wurde
	 * 
	 * @param deserialized
	 *            true oder false
	 */
	public void setDeserialized(boolean deserialized) {
		this.deserialized = deserialized;
	}
}
