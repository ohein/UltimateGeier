package vultureUtil;

import javax.swing.table.*;
import controller.*;
import java.util.*;

public class LeagueTableModel extends AbstractTableModel{
	private TournamentTableDataClass tournamentTable;
	private ArrayList<TournamentPlayerInfoFile> tournamentData;
	private TournamentPlayerInfoFileComparator tpifComp;
	/** Aktuelle Position des Spielers XYZ*/
	private int playerPos;
	
	/** Konstruktor des LeagueTableModels*/
	public LeagueTableModel(TournamentTableDataClass tournamentTable){
		this.tournamentTable=tournamentTable;
		tpifComp = new TournamentPlayerInfoFileComparator();
		prepareTournamentData();
	}
	
	private void prepareTournamentData(){
		if(tournamentData==null){
			tournamentData = new ArrayList<TournamentPlayerInfoFile>();
			
		}
		else{
			tournamentData.clear();
		}
		ArrayList<TournamentPlayerInfoFile> tmpQueue = tournamentTable.getTournamentTable();
		Collections.sort(tmpQueue,tpifComp);
		Iterator<TournamentPlayerInfoFile> it = tmpQueue.iterator();
		while(it.hasNext()){
			tournamentData.add(it.next());
		}
	}
	/** Ermittelt anhand des übergebenen Spielernamens die aktuelle Position des Spielers und
	 *  gibt diese als Integer zurück */
	public int getPlayerPos(String name){
		for (int i =0; i<tournamentData.size();i++){
		if(tournamentData.get(i).getPlayer().getName()==name)
			playerPos=i+1;
		}
		return playerPos;
	}
	
	public void update(){
		prepareTournamentData();
	}
	
	@Override
	public int getRowCount(){
		return tournamentData.size();
	}
	
	@Override
	public int getColumnCount(){
		return 4;
	}
	
	@Override
	public Class getColumnClass(int columnIndex){
		return String.class;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex){
		return (rowIndex<tournamentData.size()&&columnIndex<5);
	}
	
	
	@Override
	public Object getValueAt(int row, int column){
		switch(column){
			case 0:
				return String.valueOf((row+1));
			case 1:
				return tournamentData.get(row).getPlayer().getName();
			case 2:
				return tournamentData.get(row).getMatchesPlayed();
			case 3:
				return tournamentData.get(row).getWins();
			case 4:
				return tournamentData.get(row).getDraws();
			case 5:
				return tournamentData.get(row).getLosses();
			case 6:
				return tournamentData.get(row).getSetsWon();
			default:
				return tournamentData.get(row).getPointsMade();
		}
	}
	
//	public Object getValueAt(int row, int column){
//		switch(column){
//			case 0:
//				return String.valueOf((row+1));
//			case 1:
//				return tournamentData.get(row).getPlayer().getName();
//			case 2:
//				return tournamentData.get(row).getWins();
//			case 3:
//				return tournamentData.get(row).getDraws();
//			case 4:
//				return tournamentData.get(row).getLosses();
//			default:
//				return tournamentData.get(row).getPointsMade();
//		}
//	}
	
	class TournamentPlayerInfoFileComparator implements Comparator<TournamentPlayerInfoFile>{
		@Override
		public int compare(TournamentPlayerInfoFile f1, TournamentPlayerInfoFile f2){
			return (-1)*f1.compareTo(f2);
		}
	}
}
