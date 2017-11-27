package smartStratPlayerPackage;

import java.util.ArrayList;

public class TransformedNormalForm {
	private NormalForm nf;
	
	private Double[][] data;
	private String[] rowHeader;
	private String[] colBottom;
	private String[] structureVariablesA;
	
	private String[] colHeader;
	private String[] rowBottom;
	
	private double bonus;
	
	public String[] getStructureVariablesA(){
		return structureVariablesA;
	}
	
	public TransformedNormalForm(NormalForm nf, int bonus){
		this.nf = nf;
		init(bonus);
		transform();
	}
	
//	public TransformedNormalForm(NormalForm nf){
//		this.nf = nf;
//		init();
//		transform();
//	}
	
	public Double getGameValue(){
		return 1/((-1)*data[0][0])-bonus;
	}
	
	public ArrayList<String> getStructuresOfAInBasis(){
		ArrayList<String> structuresInBasis = new ArrayList<String>();
		String currentBasis;
		for(int i=0; i < structureVariablesA.length;i++){
			currentBasis = structureVariablesA[i];
			for(int j=0; j<colHeader.length; j++){
				if(colHeader[j].equals(currentBasis)){
					structuresInBasis.add(currentBasis);
				}
			}
		}
		return structuresInBasis;
	}
	
	/** Diese Methode gibt den Anteil an, mit dem die Karte 'cardValue' von Spieler A gespielt
	 * werden muss, um miniMax-maessig gemischt zu sein */
	public Double getMyPortionOf(Integer cardValue){
		// Zuerst wird der Parameter-Kartenwert in den Spaltenüberschriften gesucht
		for(int i = 1; i < data[0].length; i++){
			if(cardValue.toString().equals(colHeader[i])){
				return data[0][i]*Math.pow(data[0][0],-1);
			}
		}
		// War der Parameter-Kartenwert hier nicht zu finden, so kann es sein, dass der ent-
		// sprechende Wert aus der Basis ausgeschieden ist, weshalb er nun in den Spalten-Unterschriften
		// gesucth werden muss
		for(int j = 0; j < data.length; j++){
			if(cardValue.toString().equals(rowHeader[j])){
				return 0d;
			}
		}
		// Ansonsten wirf eine IllegalArgument-Exception, da der Parameter-Kartenwert nicht zur Menge der Karten
		// des Spielers gehört
		throw new IllegalArgumentException("Parameter Value is not Element of Available Card Set");
	}
	
	/** Diese Methode gibt den Anteil an, mit dem die Karte 'cardValue' von Spieler A gespielt
	 * werden muss, um miniMax-maessig gemischt zu sein */
	public Double getMyPortionOf(String cardValue){
		// Zuerst wird der Parameter-Kartenwert in den Spaltenüberschriften gesucht
		for(int i = 1; i < data[0].length; i++){
			if(cardValue.equals(colHeader[i])){
				return data[0][i]*Math.pow(data[0][0],-1);
			}
		}
		// War der Parameter-Kartenwert hier nicht zu finden, so kann es sein, dass der ent-
		// sprechende Wert aus der Basis ausgeschieden ist, weshalb er nun in den Spalten-Unterschriften
		// gesucth werden muss
		for(int j = 0; j < data.length; j++){
			if(cardValue.equals(rowHeader[j])){
				return 0d;
			}
		}
		// Ansonsten wirf eine IllegalArgument-Exception, da der Parameter-Kartenwert nicht zur Menge der Karten
		// des Spielers gehört
		throw new IllegalArgumentException("Parameter Value is not Element of Available Card Set");
	}
	
	private void init(){
		init(0);
	}
	
	private void init(int bonus){
		/* Initialisieren der Zeilen- und Spaltenbeschriftungen */
		rowHeader = new String[nf.getNumberOfRows()+1];
		colHeader = new String[nf.getNumberOfCols()+1];
		colBottom = new String[nf.getNumberOfCols()+1];
		rowBottom = new String[nf.getNumberOfRows()+1];
		
		this.bonus = bonus;
		
		/* Speichern der Strukturvariablen		 */
		structureVariablesA = new String[nf.getNumberOfRows()];
		for(int i=0;i<nf.getNumberOfRows();i++){
			structureVariablesA[i] = nf.getRowHeader()[i];
		}
		
		
		/* Initialisieren der Datenmatrix mit den entsprechenden Werten */
		data = new Double[nf.getNumberOfRows()+1][nf.getNumberOfCols()+1];
		
		/* Nach der Initialisierung ist der Wert der oberen linken Ecke der Matrix,
		 * der gleichzeitig auch den reziproken Wert des Spielwertes mit getauschtem 
		 * Vorzeichen darstellt, immer 0
		 */
		data[0][0]=0d;
		rowHeader[0] = "-1/v";
		colBottom[0] = "\t (1)";
		
		colHeader[0] = "\t 1/v";
		rowBottom[0] = "(-1)";
		// Die rowHeader werden mit den Schlupfvariablen von Spieler B initialisiert
		// Die rowBottom werden mit den Strukturvariablen von Spieler A initiaisiert
		// Ausser der Zelle, der spaeter der reziproke Wert des Spieles zu entnehmen ist,
		//   werden alle weiteren Zellen der ersten Zeile mit einer 1 initialisiert
		
		// KORREKTUR == KORREKTUR == KORREKTUR == KORREKTUR == KORREKTUR == KORREKTUR == 
		
		// - Die rowHeader beinhalten jeweils die aktuellen Nicht-Basisvariablen von Spieler A
		//   und werden daher mit den Strukturvariablen von Spieler A initialisiert
		// - Die rowBottom beinhalten die  aktuellen Basisvariablen von Spieler B werden also
		//   mit den Schlupfvariablen von Spieler B initialisiert
		for(int i = 1; i < rowHeader.length; i++){
			data[i][0]=1d;
			rowHeader[i] = nf.getRowHeader()[i-1];
			rowBottom[i] = "SVB"+i;
		}
		// Die colHeader werden mit den Schlupfvariablen von Spieler A initialisiert
		// Die colBottom werden mit den Strukturvariablen von Spieler B initialisiert
		// Aussder der Zeile, der spaeter der reziproke Wert des Spieles zu entnehmen ist,
		//    werden alle weiteren Zellen der ersten Spalte mit einer 1 initialisiert
		// KORREKTUR == KORREKTUR == KORREKTUR == KORREKTUR == KORREKTUR == KORREKTUR == KORREKTUR
		// - Die colHeader beinhalten die aktuellen Basisvariablen von Spieler A und werden daher
		//   mit den Schlupfvariablen initialisiert
		// - Die colBottom beinhalten die aktuellen Nichtbasisvariablen von Spieler B 
		//   und werden daher mit den Sturkturvariablen von Spieler B initalisiert
		
		for(int j = 1; j < colHeader.length; j++){
			data[0][j]=1d;
			colHeader[j] = "SVA"+j;
			colBottom[j] =  nf.getColHeader()[j-1];
		}

		for(int i = 1; i < rowHeader.length; i++ ){
			for(int j = 1; j < colHeader.length; j++){
				data[i][j] = nf.getData()[i-1][j-1] + bonus;
			}
		}
	}
	
	public NormalForm getNf() {
		return nf;
	}

	public void setNf(NormalForm nf) {
		this.nf = nf;
	}

	public String[] getRowHeader() {
		return rowHeader;
	}

	public void setRowHeader(String[] rowHeader) {
		this.rowHeader = rowHeader;
	}

	public String[] getColBottom() {
		return colBottom;
	}

	public void setColBottom(String[] colBottom) {
		this.colBottom = colBottom;
	}

	public String[] getColHeader() {
		return colHeader;
	}

	public void setColHeader(String[] colHeader) {
		this.colHeader = colHeader;
	}

	public String[] getRowBottom() {
		return rowBottom;
	}

	public void setRowBottom(String[] rowBottom) {
		this.rowBottom = rowBottom;
	}

	public Double[][] getData() {
		return data;
	}

	public void setData(Double[][] data) {
		this.data = data;
	}
	
	@Override
	public String toString(){
		String res = "";
		for(String st : colHeader){
			res += st + "\t";
		}
		res += "\n";
		for(int i = 0; i < data.length; i++ ){
			res += rowHeader[i] + "\t";
			for( int j = 0; j < data[0].length; j++){
				res += String.format("%3.2f", data[i][j]);
				res += "\t";
			}
			res += rowBottom[i];
			res += "\n";
		}
		for(String st : colBottom){
			res += st + "\t";
		}
	
		
		return res;
	}
	
	protected void transform(){
	//	System.out.println(this);
		while(!checkIfFinished()){
			nextIteration();
			//System.out.println(this);
		}
		//System.out.println(this);
	}
	
	protected void nextIteration(){
		int pivotCol = getIndexOfPivotCol();
		int pivotRow = getIndexOfPivotRow(pivotCol);
		//System.out.println("Pivotspalte: " + pivotCol);
		//System.out.println("Pivotzeile: " + pivotRow);
		
		//========= Tableau fuer die neuen Werte anlegen =============
		Double[][] dataNew = new Double[data.length][data[0].length];
		// Die nachfolgenden Schritte erfolgenden nach 
		// Bewersdorff, Jörg; Glück, Logik und Bluff - Mathematik im Spiel - Methodik, Ergebnisse und Grenzen; 
		// Wiesbaden, 2010; S. 276
		
		// "Die im Schnittpunkt der getauschten Zeile und Spalte stehende Zahl, das so genannte PIVOTELEMENT ('p'), wird 
		// durch seinen reziproken Wert ersetzt."
		dataNew[pivotRow][pivotCol] = 1d/data[pivotRow][pivotCol];
		
		// "Die anderen Zahlen der Pivotzeile('z') werden durch das Pivotelement dividiert."
		for(int j = 0; j < data[0].length; j++){
			if(!(j==pivotCol))
				dataNew[pivotRow][j] = data[pivotRow][j]/data[pivotRow][pivotCol];
		}
		
		// "Die anderen Zahlen der Pivotspalte ("s") werden mit umgekehrten Vorzeichen durch das Pivotelement dividiert"
		for(int i = 0; i < data.length; i++){
			if(!(i==pivotRow))
			dataNew[i][pivotCol] = data[i][pivotCol]/( (-1) * data[pivotRow][pivotCol]);
		}
		
		// "Bei den restlichen Zahlen ('r') folgt man der so genannten RECHTECKREGEL, wozo man das Rechteck bildet, welches
		// durch das Pivotelement und die aktuell zu transformierende Zahl festgelegt wird: Auf Basis der vier an den Ecken 
		// dieses Rechtecks stehenden Zahlen berechnet man mittels der Formel r - sz / p den neuen Wert."
		for(int i = 0; i < data.length; i++){
			for(int j = 0; j < data[0].length; j++){
				if(i == pivotRow || j == pivotCol){
					continue;
				}
				dataNew[i][j] = data[i][j] - (data[pivotRow][j]*data[i][pivotCol])/data[pivotRow][pivotCol];
			}
		}
		
		// "Die am Rande stehenden Variablen werden zwischen Pivotzeile und -spalte getauscht. Dabei ist beim Tausch vom 
		// linken zum unteren Rand und umgekehrt das Vorzeichen zu aendern."
		
		//=====Tausch der Beschriftungen durchführen=========
		String tmp = rowHeader[pivotRow];
		rowHeader[pivotRow] = colHeader[pivotCol];
		colHeader[pivotCol] = tmp;
		
		tmp = colBottom[pivotCol];
		colBottom[pivotCol] = rowBottom[pivotRow];
		rowBottom[pivotRow] = tmp;
		// ===================================================
		
		//============================================================
		
		//==== Tatsaechliche Aktualisierung des Tableaus
		copyMultiArray(dataNew, data);
		
		//=============================================
	}
	
	protected void copyMultiArray(Double[][] source, Double[][] target){
		for(int i = 0; i < source.length; i++){
			for(int j = 0; j < source[i].length; j++){
				target[i][j] = source[i][j];
			}
		}
	}
	
	protected boolean checkIfFinished(){
		boolean res = true;
		for(int i=1; i < data[0].length; i++){
			res = res && data[0][i]<=0;
		}
		return res;
	}
	
	protected int getIndexOfPivotRow(int pivotCol){
		int res = -1;
		Double currMin = Double.MAX_VALUE;
		
		for(int i=1; i < data.length; i++ ){
			if(data[i][pivotCol] > 0 && data[i][0]/data[i][pivotCol] < currMin ){
				res = i;
				currMin=data[i][0]/data[i][pivotCol];
			}
		}
		if(res == -1){
			throw new RuntimeException("Result should not be -1");
		}
		return res;
	}
	
	protected int getIndexOfPivotCol(){
		int res = 1;
		double currentMax = 0;
		for(int i=1; i < data[0].length; i++ ){
			if(data[0][i] > currentMax){
				res = i;
				currentMax = data[0][i];
			}
		}
		return res;
	}

}
