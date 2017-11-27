package vultureUtil;

import java.io.Serializable;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class BuzzardAnalyser implements Serializable {
	/** Hier wird der akutelle Wert, welcher den Kartenzuschlag erhalten soll, gespeichert*/
	private int max;
	
	/** Hier wird der aktueller Wert der Karte, welche die Geierkarte erhalten wird, gespeichert*/
	private int min;
	
	/** Hier wird der Wert gespeichert, der den Kartenzuschlag erhalten hätte, wenn nicht zwei
	 *  oder mehr Spieler diesen Wert geboten hätten	 */
	private int upperLimit;
	
	/** Hier wird der Wert der Karte gespeichert, die eine Geierkarte erhaltne hätte, hätten nicht
	 * zwei oder mehr Spieler eine Karte mit gleichem Wert gesetzt	 */
	private int lowerLimit;
	
	/** Hier wird der Wert der aktuell Untersuchten Zelle gespeichert*/
	private int cellVal;
	
	/** Aus dieser Variable kann abgelesen werden, ob es eine alleinige Hoechstbieter Karte gab*/
	private boolean newMax;
	
	/** Aus dieser Karte kann abgelesen werden, ob es eine alleinige Mindestbieter Karte gab*/
	private boolean newMin;
	
	/** Enthält die Zeilennummer(!!!!!) des Spielers, der die höchste, nur von einem Spieler gebotene Karte
	 *  gespielt hat	 */
	private int maxPlayer;
	
	
	/** Enthält die Zeilennummer(!!!!) des Spielers, der die niedrigste, nur von einem Spieler gebnotene Karte
	 *  gespielt hat	 */
	private int minPlayer;
	

	public BuzzardAnalyser(){
		max=0;
		min=9999999;
		lowerLimit=0;
		upperLimit=9999999;
		
	}
	
	/** Diese Methode analysiert eine Tabellenspalte und speichert den Wert der Karte, welche den
	 *  Gewinnkartenzuschlag erhält, in der Variable "max".
	 * @param table Die Tabelle, welche die zu analysierende Spalte enthält
	 * @param column Der Index der zu analysierenden Spalte
	 */
	public void analyseColumnForMax(BuzzardGameModel model, int column){
		/* Gibt an, ob es bei der Suche nach dem alleinigen Maximum überhaupt ein solches gibt,
		 * bleibt sein Wert nach einem Schleifendurchlauf auf "false", gab es kein neues Maximum
		 * und somit gibt es auch kein alleiniges Maximum		 */
		newMax=false;
		
		/* Gibt an, ob das derzeitige Maximum von einem einzigen Spieler alleine (="true") oder
		 * von mehreren Spielern gleichzeitig geboten (="false") wurde.		 */
		boolean alone = false;
		
		int rows = model.getRowCount();

		/* Alles "zuruecksetzen", damit auch nichts uebersehen wird*/
		max=0;
		upperLimit=9999999;
		
		/* Solange kein alleiniges Maximum gefunden wurde (und auch eine Chance ein Solches zu
		 * finden besteht, wird brav gesucht		 */
		while(!alone){
			//System.out.println("Analysing");
			alone=true;
			newMax=false;
			max=0;
			for(int i=1;i<rows;i++){
				String cellContent =model.getValueAt(i,column);
				if(cellContent.equals("")||cellContent.equals("X")){
					continue;
				}
				cellVal = Integer.parseInt(cellContent);
				if(cellVal>max&&cellVal<upperLimit){
					max = cellVal;
					maxPlayer=i;
					alone=true;
					newMax=true;
				}
				else if(cellVal==max){
					alone=false;
				}
			}
			/* Wird das gefundene Maximum von mehreren Spielern geboten,  wird geschaut, ob es sich
			 * bei dem derzeitigen Maximum überhaupt um ein  neues Maximum handelt (denn wenn nicht 
			 * gibt es gar kein Maximum, das nur von einem einzigen Spieler geboten wird). Besteht
			 * weiterhin die Aussicht auf ein Unique-Maximum, dann wird das upperlimit auf den Wert
			 * des derzeitigen Max gesetzt, wodurch bewirkt wird, dass Schritt für Schritt der nächst
			 * niedrigere gebotene Wert untersucht und ggf. zum Maximum wird.
			 */
			if(!alone){
				if(!newMax){
					break;
				}
				upperLimit=max;
			}
		}
	}
	
	/** Diese Methode analysiert eine Tabellenspalte und speichert den Wert der Karte, welche die
	 *  Geierkarte erhält, in der Variable "min". Gibt es keine alleige Verliererkarte, so ist nach 
	 *  Ausführung dieser Methode die boolsche Variable "newMin" mit dem Wert "false" belegt.
	 */
	public void analyseColumnForMin(BuzzardGameModel model, int column){
		//================================================================================
		//						           W I C H T I G
		//================================================================================
		// Diese Methode arbeitet analog zur Methode "analyseColumnForMax()", weshalb 
		// die Funktionsweiese dieser Methode nicht weiter kommentiert wird. 
		//================================================================================
		newMin = false;
		boolean alone = false;
		int rows = model.getRowCount();
		min = 9999999;
		lowerLimit  = 0;
		
		while(!alone){
		//	System.out.println("Analysing");
			alone=false;
			newMin=false;
			min = 9999999;
			for(int i=1;i<rows;i++){
				String cellContent =model.getValueAt(i,column);
				if(cellContent.equals("")||cellContent.equals("X")){
					continue;
				}
				cellVal = Integer.parseInt(cellContent);
				if(cellVal<min&&cellVal>lowerLimit){
					min=cellVal;
					minPlayer=i;
					alone=true;
					newMin=true;
				}
				else if (cellVal==min){
					alone=false;
				}
			}
			if(!alone){
				if(!newMin){
					break;
				}
				lowerLimit=min;
			}
		}
	}	
	
	/** Diese Methode analysiert eine Tabellenspalte und speichert den Wert der Karte, welche den
	 *  Gewinnkartenzuschlag erhält, in der Variable "max".
	 * @param table Die Tabelle, welche die zu analysierende Spalte enthält
	 * @param column Der Index der zu analysierenden Spalte
	 */
	public void analyseColumnForMax(TableModel table, int column){
		/* Gibt an, ob es bei der Suche nach dem Alleinigen Maximum überhaupt ein solches gibt,
		 * bleibt sein Wert nach einem Schleifendurchlauf auf "false", gab es kein neues Maximum
		 * und somit gibt es auch kein alleiniges Maximum		 */
		newMax=false;
		
		/* Gibt an, ob das derzeitige Maximum von einem einzigen Spieler alleine (="true") oder
		 * von mehreren Spielern gleichzeitig geboten (="false") wurde.		 */
		boolean alone = false;
		
		int rows = table.getRowCount();

		/* Alles "zuruecksetzen", damit auch nichts uebersehen wird*/
		max=0;
		upperLimit=9999999;
		
		/* Solange kein alleiniges Maximum gefunden wurde (und auch eine Chance ein Solches zu
		 * finden besteht, wird brav gesucht		 */
		while(!alone){
	//		System.out.println("Analysing");
			alone=true;
			newMax=false;
			max=0;
			for(int i=1;i<rows;i++){
				String cellContent =(String) table.getValueAt(i,column);
				if(cellContent.equals("")||cellContent.equals("X")){
					continue;
				}
				cellVal = Integer.parseInt(cellContent);
				if(cellVal>max&&cellVal<upperLimit){
					max = cellVal;
					maxPlayer=i;
					alone=true;
					newMax=true;
				}
				else if(cellVal==max){
					alone=false;
				}
			}
			/* Wird das gefundene Maximum von mehreren Spielern geboten,  wird geschaut, ob es sich
			 * bei dem derzeitigen Maximum überhaupt um ein  neues Maximum handelt (denn wenn nicht 
			 * gibt es gar kein Maximum, das nur von einem einzigen Spieler geboten wird). Besteht
			 * weiterhin die Aussicht auf ein Unique-Maximum, dann wird das upperlimit auf den Wert
			 * des derzeitigen Max gesetzt, wodurch bewirkt wird, dass Schritt für Schritt der nächst
			 * niedrigere gebotene Wert untersucht und ggf. zum Maximum wird.
			 */
			if(!alone){
				if(!newMax){
					break;
				}
				upperLimit=max;
			}
		}
	}
	
	/** Diese Methode analysiert eine Tabellenspalte und speichert den Wert der Karte, welche den
	 *  Gewinnkartenzuschlag erhält, in der Variable "max".
	 * @param table Die Tabelle, welche die zu analysierende Spalte enthält
	 * @param column Der Index der zu analysierenden Spalte
	 */
	public void analyseColumnForMax(JTable table, int column){
		analyseColumnForMax(table.getModel(),column);
	}
	
	
	/** Diese Methode analysiert eine Tabellenspalte und speichert den Wert der Karte, welche die
	 *  Geierkarte erhält, in der Variable "min". Gibt es keine alleige Verliererkarte, so ist nach 
	 *  Ausführung dieser Methode die boolsche Variable "newMin" mit dem Wert "false" belegt.
	 */
	public void analyseColumnForMin(TableModel table, int column){
		//================================================================================
		//						           W I C H T I G
		//================================================================================
		// Diese Methode arbeitet analog zur Methode "analyseColumnForMax()", weshalb 
		// die Funktionsweiese dieser Methode nicht weiter kommentiert wird. 
		//================================================================================
		newMin = false;
		boolean alone = false;
		int rows = table.getRowCount();
		min = 9999999;
		lowerLimit  = 0;
		
		while(!alone){
			//System.out.println("Analysing");
			alone=false;
			newMin=false;
			min = 9999999;
			for(int i=1;i<rows;i++){
				String cellContent =(String) table.getValueAt(i,column);
				if(cellContent.equals("")||cellContent.equals("X")){
					continue;
				}
				cellVal = Integer.parseInt(cellContent);
				if(cellVal<min&&cellVal>lowerLimit){
					min=cellVal;
					minPlayer=i;
					alone=true;
					newMin=true;
				}
				else if (cellVal==min){
					alone=false;
				}
			}
			if(!alone){
				if(!newMin){
					break;
				}
				lowerLimit=min;
			}
		}
	}
	
	
	
	/** Diese Methode analysiert eine Tabellenspalte und speichert den Wert der Karte, welche die
	 *  Geierkarte erhält, in der Variable "min". Gibt es keine alleige Verliererkarte, so ist nach 
	 *  Ausführung dieser Methode die boolsche Variable "newMin" mit dem Wert "false" belegt.
	 */
	public void analyseColumnForMin(JTable table, int column){
		analyseColumnForMin(table.getModel(),column);
	}
	
	


	/** Gib den Wert der höchsten, nur von einem Spieler gespielten Karte zurück.*/
	public int getMax() {
		return max;
	}
	
	/** Gibt den Wert der niedrigsten, nur von einem Spieler gespielten Karte zurück.*/
	public int getMin() {
		return min;
	}

	/** Gibt an, ob es einen alleinigen Höchstbieter gab.*/
	public boolean isNewMax() {
		return newMax;
	}


	/** Gibt an, ob es einen alleinigne Niedrigstbieter gab.*/
	public boolean isNewMin() {
		return newMin;
	}

	/** Gibt die Spielernummer des alleinigen Höchstbieters zurück.*/
	public int getMaxPlayer() {
		return maxPlayer;
	}

	/** Gibt die Spielernummer des alleinigen Niedrigstbieters zurück.*/
	public int getMinPlayer() {
		return minPlayer;
	}


}
