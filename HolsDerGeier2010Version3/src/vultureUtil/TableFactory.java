package vultureUtil;

import javax.swing.*;
import javax.swing.table.*;
import controller.*;

public class TableFactory {
	public static final int VULTURE_RENDERER=0;
	
	public static JTable getLeagueTable(TournamentTableDataClass tournamentTable){
		DefaultTableColumnModel cm = new DefaultTableColumnModel();
		TableColumn c = new TableColumn(0,5);
		c.setHeaderValue("Pl.");
		cm.addColumn(c);
		c = new TableColumn(1,100);
		c.setHeaderValue("Name");
		cm.addColumn(c);
		c = new TableColumn(2, 10);
		c.setHeaderValue("MP");
		cm.addColumn(c);
		c = new TableColumn(3,10);
		c.setHeaderValue("W");
		cm.addColumn(c);
		c = new TableColumn(4,10);
		c.setHeaderValue("D");
		cm.addColumn(c);
		c = new TableColumn(5,10);
		c.setHeaderValue("L");
		cm.addColumn(c);
		c = new TableColumn(6,10);
		c.setHeaderValue("SW");
		cm.addColumn(c);
		c = new TableColumn(7,10);
		c.setHeaderValue("PM");
		cm.addColumn(c);
		
		LeagueTableModel ltm = new LeagueTableModel(tournamentTable);
		JTable table = new JTable(ltm,cm);
		return table;
	}
	
	public static JTable getJTable(BuzzardGameModel data){
		int rows = data.getRowCount(); 
		int cols = data.getColumnCount(); 
		
		
		/* Erzeugen des Tabellenspaltenmodells*/
		DefaultTableColumnModel cm = new DefaultTableColumnModel();
		/* Erzeugen der Namensspalte der Tabelle, welche die breiteste der Spalten sein wird*/
		TableColumn c = new TableColumn(0,90); 
		c.setHeaderValue("Runde");
		cm.addColumn(c);
		/* Erzeugen der einzelnen Rundenspalten, in denen Informationen über die Gewinnkarte sowie
		 * die gebotenen Karten zu finden sein werden		 */
		for(int i=1;i<(cols-1);i++){
			c = new TableColumn(i,5);
			c.setHeaderValue(i);
			cm.addColumn(c);
		}
		/* Erzeugen der Gesamtpunktestandspalte*/
		c=new TableColumn((cols-1),20);
		c.setHeaderValue("Gesamt");
		cm.addColumn(c);
		
		/* Erzeugen eines Table-Models mit unseren Daten*/
		BuzzardTableModel btm = new BuzzardTableModel(data);
	
		/* Erzeugen eines Renderers, welcher mit Hilfe eines Analyser Informationen über die Spieltabelle
		 * gewiint.*/
		BuzzardRenderer renderer = new BuzzardRenderer(data);
		
		JTable res = new JTable(btm,cm);
		res.setDefaultRenderer(Object.class,renderer);
		return res;
		
	}
	
	
	
	
//	public static JTable getJTable(int numberOfPlayers, int  numberOfRounds, 
//	BuzzardAnalyser analyser){
//		String[] playerNames = new String[numberOfPlayers];
//		for(int i=0;i<numberOfPlayers;i++){
//			playerNames[i]="Spieler "+i;
//		}
//		return getJTable(playerNames, numberOfRounds,analyser);
//	}
	
//	public static JTable getJTable(String names[], int numberOfRounds, BuzzardAnalyser analyser){
//		int rows = names.length+1; // Anzahl der Spieler + (Tier)Kartenwertzeile
//		int cols = numberOfRounds+2; // Anzahl der Runden + Spieler- und Gesamtpunktespalte
//		
//		
//		/* Erzeugen des Tabellenspaltenmodells*/
//		DefaultTableColumnModel cm = new DefaultTableColumnModel();
//		/* Erzeugen der Namesspalte der Tablle, welche die breiteste der Spalten sein wird*/
//		TableColumn c = new TableColumn(0,90); 
//		c.setHeaderValue("Runde");
//		cm.addColumn(c);
//		/* Erzeugen der einzelnen Rundenspalten, in denen Informationen über die Gewinnkarte sowie
//		 * die gebotenen Karten zu finden sein werden		 */
//		for(int i=1;i<(cols-1);i++){
//			c = new TableColumn(i,5);
//			c.setHeaderValue(i);
//			cm.addColumn(c);
//		}
//		/* Erzeugen der Gesamtpunktestandspalte*/
//		c=new TableColumn((cols-1),20);
//		c.setHeaderValue("Gesamt");
//		cm.addColumn(c);
//		
//		/* Spaltenüberschriften generieren*/
//		String[] colHeaders = new String[cols];
//		colHeaders[0] = "Runde";
//		for(int i=1;i<(cols-1);i++){
//			colHeaders[i]=String.valueOf(i);
//		}
//		colHeaders[cols-1]="Gesamt";
//		
//		
//		
//		/* Datentabelle erzeugen und mit Werten belegen */
//		/* ============================================ */
//		/* Als erstes links oben: Die (Tier)Kartenwertzeile, für die wir nur den Inhalt der ersten
//		 * Spalte ("Karte") von Beginn an Wissen - Die Werte der Tierkarten ergeben sich im Spiel
//		 * verlauf		 */
//		String[][] data = new String[rows][cols];
//		
//		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//		// !    I M P O R T A N T   N O T I C E      ||           I M P O R T A N T   N O T I C E   !
//		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//		// Das mehrdimensionale String-Array "data" wird vor Erzeugung der eigentlichen Tabelle der !
//		// Klasse "BuzzardTableModel" uebergeben, welche eine Spezialiserung der Klasse             !
//		// "AbstractTableModel" ist. Dies ist notwendig, da der JTable-Konstruktor neben dem        !
//		// Column-Model auch nach einem TableModel verlangt.                                        !
//		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//		
//		data[0][0] = "Karte";
//		
//		/* Initialisieren der Spalte mit den Spielernamen*/
//		for(int i=1;i<(rows);i++){
//			data[i][0] = names[i-1];
//			data[i][cols-1]=String.valueOf(0);
//		}
//		
//		/* Initialisieren der Werte der Gesamtwertspalte*/
//		for(int j=0;j<rows;j++){
//			data[j][cols-1]=String.valueOf(0);
//		}
//		
//		/* Initialisieren der restlichen Tabellenzellen mit Leerstrings*/
//		for(int i=0;i<rows;i++){
//			for(int j=1;j<cols-1;j++){
//				data[i][j]="";
//			}
//		}
//		
//		/* Erzeugen eines Table-Models mit unseren Daten*/
//		BuzzardTableModel vtm = new BuzzardTableModel(data);
//	
//		/* Erzeugen eines Renderers, welcher mit Hilfe eines Analyser Informationen über die Spieltabelle
//		 * gewiint.*/
//		BuzzardRenderer renderer = new BuzzardRenderer(analyser);
//		
//		JTable res = new JTable(vtm,cm);
//		res.setDefaultRenderer(Object.class,renderer);
//		return res;
//	}
	
}
