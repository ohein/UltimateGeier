package enrollGUI;

import java.awt.*;
import javax.swing.*;

import player.HolsDerGeierSpieler;
import controller.*;

public class TournamentEnrollDialog extends GameEnrollDialog implements TournamentEnrollMediator{
	private TPlayersPerMatchField ppmField;
	private JLabel ppmLabel;
	//eingefuegt von Gruppe 2
	private int oldMiField;
	protected FastSimulationCheckBox fsBox;
	
	
	public TournamentEnrollDialog(JFrame owner, MainController controller ){
		super(owner, controller);
		//Eingefuegt von Gruppe 2
		fsBox = new FastSimulationCheckBox(this);
		
		ppmField = new TPlayersPerMatchField(this);
		ppmLabel = new JLabel("Players per Match");
		
		// Anordnen des PlayersPerMatch-Fields
		// Position von Gruppe 2 geaendert
		gbc = getGBC(2, 2, 1, 1);
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.weightx=2;
		gbc.anchor=GridBagConstraints.NORTHEAST;
		gbc.insets = new Insets(10,10,10,10);
		gblMiddle.setConstraints(ppmField, gbc);
		middlePanel.add(ppmField);
		// Anordnen des PlayersPerMatch-Labels
		gbc=getGBC(3,2,1,1);
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.weightx=10;
		gbc.anchor=GridBagConstraints.NORTHWEST;
		gbc.insets=new Insets(10,0,10,0);
		gblMiddle.setConstraints(ppmLabel,gbc);
		middlePanel.add(ppmLabel);

		//Editiert und eingefuegt von Gruppe 2 25.05.2012
		//Einfuegen fsCheckbox
		gbc = getGBC(2,1,1,1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx=1;
		gbc.anchor = GridBagConstraints.NORTHEAST;
		gbc.insets = new Insets(10,10,10,10);
		gblMiddle.setConstraints(fsBox, gbc);
		middlePanel.add(fsBox);
				
		//Einfuegen "Fast Simulation"-Label
		JLabel fsLabel = new JLabel("Fast Simulation");
		gbc = getGBC(3,1,1,1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx=10;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(10,0,10,0);
		gblMiddle.setConstraints(fsLabel,gbc);
		middlePanel.add(fsLabel);
	
		pack();
	}
	
	//================================================================================
	//================================================================================
	@Override
	public void registerTPlayersPerMatchField(TPlayersPerMatchField field){
		this.ppmField=field;
	}
	
	@Override
	public void tPlayersPerMatchFieldChanged(){
		numberOfRoundsFieldChanged();
	}
	
	@Override
	public void init(){
		okButton.setEnabled(false);
		clButton.setEnabled(false);
		rmButton.setEnabled(false);
	}
	



	@Override
	public void select() {
		// TODO Auto-generated method stub
		
	}


	@Override
	
	/**
	 * Gruppe 3 angepasst für Punkt 11 und 12
	 * die enroll() Methode ruft für jeden Spieler der eingeschrieben werden soll diese Methode auf
	 * 
	 * @param index: Indexwert der availablePlayerListe von dem Spieler, der eingeschrieben werden soll
	 */
	public void enrollPlayer(int index) {
		
		//int index =avList.getSelectedIndex();
		
		String classToInstantiate = avList.getElementAt(index);
		
		// Gruppe 3 Punkt 10 Automatische Namensgenerierung bei nicht Eingabe eines individuellen Namens:
		
		String objectName =  checkObjectName(classToInstantiate);
	
		String externalPlayerPath = vultureUtil.PropertyManager.getValue("playerPath");
		HolsDerGeierSpieler enrollingInstance;
		if(externalPlayerPath==null){
			enrollingInstance = (HolsDerGeierSpieler)storeAndLoad.MultiLoader.newInstance(classToInstantiate,
					objectName);
		}else{
			enrollingInstance =(HolsDerGeierSpieler)(storeAndLoad.MultiLoader.newInstance(externalPlayerPath,
					    		classToInstantiate,objectName));
		}
		System.out.println(enrollingInstance);
		rmButton.setEnabled(true);
		clButton.setEnabled(true);
		
		enList.addPlayer(enrollingInstance);
		enList.revalidate();
		numberOfRoundsFieldChanged();
	}



	@Override
	public void clear() {
		enList.clearList();
		rmButton.setEnabled(false);
		clButton.setEnabled(false);
		okButton.setEnabled(false);
		
	}



	@Override
	public void millisPRFieldChanged() {
		numberOfRoundsFieldChanged();
	}
		
	/* Eingefuegt von Gruppe 2
	 * Aendern von MiField durch anklicken der Checkbox, Zwischenspeichern des alten Inhalts
	 * Falls kein Zugriff auf MiField moeglich ist, wird eine Errormessage auf der Console ausgegeben
	 */
	@Override
	public void fastSimulationCheckBoxStateChanged() {
		try{
			if(fsBox.isSelected()){
				try{
					oldMiField = getMiFieldValue();
				}catch(NumberFormatException e){
					oldMiField = 1000;
				}
				miField.setText("0");
				miField.setEditable(false);
			}else{
				miField.setText(""+oldMiField);
				miField.setEditable(true);
			}
		}catch(Exception e){		
			System.out.println("Thread collision - cant edit Textfield directly, Error: " + e.getMessage());
		}
	}


	@Override
	public void enrollCheckBoxStateChanged() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void cancel() {
		setVisible(false);
	}



	@Override
	public void ok() {
		if(ppmField.getNumberOfPlayersPerMatch()>enList.getNumberOfElements()){
			JOptionPane.showMessageDialog(this,
					"Spieler Pro Runde kann nicht groesser als\n die Gesamtzahl der Spieler sein",
					"Ungueltige Anzahl Spieler pro Runde",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		BuzzardTournamentInfo info = new BuzzardTournamentInfo();
		/* Gruppe 2 getMiFieldValue() eingesetzt, Position innerhalb der Methode veraendert
		 * um Ueberpruefung auf gueltigen Wert in MiField durchzufuehren. Methode bricht ab falls eine
		 * Exception von getMiFieldValue() geliefert wird
		 */
		try{
			info.setWaitingTime(getMiFieldValue());
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null,
                "Sie muessen eine Zahl in das Feld Pause zwischen Runden eingeben",
                "Falsche Eingabe",                                       
                JOptionPane.WARNING_MESSAGE);
			return;
		}
		info.setDeck(new BuzzardDeck(new int[]{-5,-4,-3,-2,-1,1,2,3,4,5,6,7,8,9,10},
				BuzzardDeck.SHUFFLED));
		info.setPlayersPerMatch(ppmField.getNumberOfPlayersPerMatch());
		for(int i=0;i<enList.getNumberOfElements();i++){
			info.addPlayer(enList.getElementAt(i));
		}
		try{
			info.setNoOfMatches(Integer.parseInt(nrField.getText()));
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, 
					"Fehlerhafte Eingabe im Feld \"Anzahl der Runden\" - Bitte Eingabe korrigieren.",
					"Eingabefehler",
					JOptionPane.ERROR_MESSAGE);
		}
		info.setSimulated(enBox.isSelected());
		setVisible(false);
		pushBuzzardSeriesInfo(info);
		clear();
		
	}



	@Override
	public void remove() {
		int selectedIndex = enList.getSelectedIndex();
		if(!(selectedIndex>=0)){
			return;
		}
		enList.removeElementAt(selectedIndex);
		if(enList.getNumberOfElements()==0){
			clButton.setEnabled(false);
			rmButton.setEnabled(false);
			okButton.setEnabled(false);
		}
		
	}



	@Override
	public void numberOfRoundsFieldChanged() {
		if(!(nrField.getText().equals("")||enList.getNumberOfElements()==0
				||ppmField.getText().equals(""))){
			okButton.setEnabled(true);
		}
		else{
			okButton.setEnabled(false);
		}
	}
	
	@Override
	public void setPath(){
		 vultureUtil.PropertyManager.storeKeyVal("playerPath", sePaButton.getPath());
		 avList.refreshData();
		 avList.revalidate();
		 avList.repaint();
		 
	}
		
	//Eingefuegt von Gruppe 2
	public void registerFastSimulationCheckBox(FastSimulationCheckBox checkBox){
		this.fsBox=checkBox;
	}

	
}
