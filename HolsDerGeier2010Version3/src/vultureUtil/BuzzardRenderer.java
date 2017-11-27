package vultureUtil;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.*;


public class BuzzardRenderer implements TableCellRenderer {

	private BuzzardGameModel gameModel;
		
	/** Konstruktor
	 * @param analyser Instanz eines VultureAnalysers, mit dessen Hilfe Informationen ueber die
	 * Spieltabelle gewonnen werden koennen	 */
	public BuzzardRenderer(BuzzardGameModel gameModel){
		this.gameModel=gameModel;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column){
		return new GetTableCellRendererComponent(table,value,isSelected,hasFocus,row,column).compute();
	}
		
	private class GetTableCellRendererComponent{
		private JTable table;
		private Object value;
		private boolean isSelected;
		private boolean hasFocus;
		private int row;
		private int column;
		
		GetTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column){
			this.table = table;
			this.value = value;
			this.isSelected = isSelected;
			this.hasFocus = hasFocus;
			this.row = row;
			this.column = column;
		}
		
		Component compute(){
			/* Das Rueckgabe-Element*/
			JLabel res = createAndConfigureLabel(table, value);
			
			/* Genau so verhält es sich, wenn es sich um die erste Zeile (also die Tierkartenzeile) handelt,
			 * da wir auch bei dieser direkt wissen, wie die enthaltenen Zellen zu formatieren sind		 */
			if(isFirstRow()){
				res.setBackground(new Color(220,220,220));
				return res;
			}
			/*Für die erste und letze Spalte ist das Format klar */
			else if(isFirstOrLastColumn()){
				res.setBackground(Color.LIGHT_GRAY);
				return res;
			}

			/* Sind noch keine entsprechenden Werte vorhanden, steht das Format
			 * ebenfalls direkt fest		 */
			else if(cellOrAnimalCardCellEmpty()){
				return res;
			}		
			/* Weiterhin muss der Wert der aktuellen Zelle ermittelt werden, da es jedoch vorkommen kann, dass
			 * in der aktuellen Zelle ein "X" steht (wenn ein Spieler disqualifiziert wurde), muss dies vor
			 * dem Parsen selbstverständlich ausgeschlossen werden. */
			else if(playerIsDisqualified()){
				res.setBackground(new Color(190,0,0));
				return res;
			}
			/* Wenn es um eine positive Karte (Maeusekarte) geht, dann... */
			else if(animalCardIsPositve()){
				/* Ist der Inhalt der aktuellen Zelle gleich dem Wert der höchsten Karte, welche von 
				 * nur einem einzigen Spieler geboten wird, so soll sich das Feld grün Färben*/
				if(currentValueIsUniqueMax()){
					res.setBackground(Color.GREEN);
				}			
				/* Ist der Wert der aktuellen Zelle höher als der Wert der höchsten Karte, welche nur
				 * von einem einzigen Spieler geboten wird, so wurde dieser Wert wohl nicht von nur
				 * einem Spieler geboten, und kann somit nicht gewinnen			 */
				else if(currentValueIsMaxButNotUnique()){
					res.setBackground(Color.YELLOW);
				}	
				/* Hat wohl leider nicht gereicht*/
				else  {			
				}
			}
			/* Wenn es um eine negative Karte (Geierkarte) geht, dann...*/
			else{
				/* Wenn der Spieler die niedrigste Karte geboten hat, erhaelt er die Geierkarte, was
				 * durch eine rote Zelle symbolisiert wird.			 */
				if(currentValueIsUniqueMinimum()){
					res.setBackground(Color.RED);
				}
				/* Wenn der Spieler eine niedrigere Karte geboten hat, als jene, die die Geierkarte 
				 * erhalten wird, dann war er wohl nicht alleiniger Niedrigstbieter			 */
				else if(currentValueIsMinButNotUnique()){
					res.setBackground(Color.YELLOW);
					
				}
				/* Souverän nicht Niedrigstbietender, die Geierkarte erhält jemand anderes */
				else{
				}
			}
			return res;
		}

		private boolean isFirstRow() {
			return row==0;
		}

		private boolean playerIsDisqualified() {
			return ((String)value).equals("X");
		}

		private boolean animalCardIsPositve() {
			return getAnimalCardValue() >= 0;
		}

		private boolean currentValueIsMinButNotUnique() {
			return getCurrentCellValue()<gameModel.getMinValueAtColumn(column);
		}

		private boolean currentValueIsUniqueMinimum() {
			return getCurrentCellValue()==gameModel.getMinValueAtColumn(column);
		}

		private boolean currentValueIsMaxButNotUnique() {
			return getCurrentCellValue()>gameModel.getMaxValueAtColumn(column);
		}

		private boolean currentValueIsUniqueMax() {
			return getCurrentCellValue() == gameModel.getMaxValueAtColumn(column);
		}

		private int getCurrentCellValue() {
			int currentCellValue =  Integer.parseInt((String)value);
			return currentCellValue;
		}

		private int getAnimalCardValue() {
			int animalCardValue = Integer.parseInt((String)table.getValueAt(0, column));
			return animalCardValue;
		}
		private boolean cellOrAnimalCardCellEmpty(){
			String animalCardCell = (String)table.getValueAt(0,column); // Zeile der Gewinnkarten, um die gespielt wird
			String currentCell = (String)value;
			return animalCardCell.equals("") ||
			  currentCell.equals("");
		}
		
		private boolean isValueValid(Object value){
			return !((String)value).equals("X");
		}
		
		private boolean isFirstOrLastColumn() {
			return column == 0 ||
					column == (table.getColumnCount()-1);
		}
		
		private JLabel createAndConfigureLabel(JTable table, Object value) {
			JLabel res = new JLabel((String)value);
			/* Ein paar allgemeine Einstellungen, die in jedem Falle vor-
			 * zunehmen sind */
			res.setOpaque(true);
			Border b = BorderFactory.createEmptyBorder(1,1,1,1);
			res.setBorder(b);
			res.setFont(table.getFont());
			res.setBackground(Color.WHITE);
			res.setForeground(Color.BLACK);
			return res;
		}
	}
}
