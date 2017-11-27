package vultureUtil;

/**
 * A TimePlanner to Store played games and create a tournament day-by-day plan   
 * Attention: a singleton pattern is implemented but with synchronization / locking!
 * @Author Team Altenhof, Jablonski, Pfeffer
 */

import java.util.ArrayList;

import player.HolsDerGeierSpieler;
import controller.BuzzardSeriesInfo;

public class TimePlanner {

	ArrayList<BuzzardSeriesInfo> matches = new ArrayList<BuzzardSeriesInfo>();
	ArrayList<HolsDerGeierSpieler> blackList = new ArrayList<HolsDerGeierSpieler>();

	// Part of the singelton pattern
	private static TimePlanner instance = null;

	private TimePlanner() {
	};

	public static TimePlanner getInstance() {
		if (instance == null) {
			instance = new TimePlanner();
		}
		return instance;
	}

	/**
	 * Adds a match to the list of matches
	 * 
	 * @param BuzzardSeriesInfo
	 *            for one match
	 */
	public void addMatch(BuzzardSeriesInfo match) {
		matches.add(match);
	}

	/**
	 * Returns the plain list of matches in the order generated
	 * 
	 * @return
	 */
	public ArrayList<BuzzardSeriesInfo> getMatchList() {
		return matches;
	}

	/**
	 * Calculates the timetable for all games played until now The matches are
	 * played one after the other in the order added No team can play twice a
	 * day so that as soon as a team which already played that day appears in a
	 * match the tournament continues on the next day
	 * 
	 * @return ArrayList<ArrayList<BuzzardSeriesInfo>> The BuzardSeriesInfo
	 *         contains the players in and informations about one game. The
	 *         outer ArrayList contains the matches for one day. The outer
	 *         ArrayList contains all days
	 */
	public ArrayList<ArrayList<BuzzardSeriesInfo>> getTimeTable() {
		ArrayList<ArrayList<BuzzardSeriesInfo>> timeTable = new ArrayList<ArrayList<BuzzardSeriesInfo>>();
		timeTable.clear();
		ArrayList<BuzzardSeriesInfo> tmpMatches = new ArrayList<BuzzardSeriesInfo>(
				matches);
		while (!tmpMatches.isEmpty()) {
			ArrayList<BuzzardSeriesInfo> matchDay = new ArrayList<BuzzardSeriesInfo>();
			while (!checkForDoubles(tmpMatches.get(0).getPlayers())) {
				matchDay.add(tmpMatches.get(0));
				for (int i = 0; i < tmpMatches.get(0).getPlayers().size(); i++) {
					blackList.add(tmpMatches.get(0).getPlayers().get(i));
				}
				tmpMatches.remove(0);
				if (tmpMatches.isEmpty())
					break;
			}
			timeTable.add(matchDay);
			blackList.clear();
		}
		return timeTable;
	}

	/**
	 * Returns the number of days for the tournament
	 * 
	 * @return
	 */
	public int getDayCount() {
		return getTimeTable().size();
	}

	/**
	 * Return the number of matches for a given day
	 * 
	 * @param day
	 * @return
	 */
	public int getMatchCount(int day) {
		if (getTimeTable().get(day) != null) {
			return getTimeTable().get(day).size();
		} else {
			return 0;
		}
	}

	/**
	 * Returns an Array of matches for a given day
	 * 
	 * @param day
	 * @return
	 */
	public ArrayList<BuzzardSeriesInfo> getMatches(int day) {
		return getTimeTable().get(day);
	}

	/**
	 * Checks if one or more of the players of the outstanding match have been
	 * playing on the actual day already and therefore are blocked to play again
	 * this day
	 * 
	 * @param match
	 * @return
	 */
	private boolean checkForDoubles(ArrayList<HolsDerGeierSpieler> match) {
		for (int i = 0; i < match.size(); i++) {
			if (blackList.contains(match.get(i)))
				return true;
		}
		return false;
	}

}
