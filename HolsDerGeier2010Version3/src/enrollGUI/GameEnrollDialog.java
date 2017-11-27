package enrollGUI;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import player.HolsDerGeierSpieler;
import controller.BuzzardDeck;
import controller.BuzzardSeriesInfo;
import controller.MainController;


/**
 * BLÖDSINN: Mit Jigloo wurde hier nur mal kurz herumexperimentiert, aber was solls.
 * 
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class GameEnrollDialog extends JDialog implements GameEnrollMediator {
	protected AvailablePlayerList avList;
	protected CancelButton caButton;
	protected ClearButton clButton;
	protected EnrollButton enButton;
	protected EnrollCheckBox enBox;
	protected EnrolledPlayerList enList;
	protected MillisPRField miField;
	protected NumberOfRoundsField nrField;
	protected OKButton okButton;
	protected RemoveButton rmButton;
	protected JScrollPane avPane;
	protected JScrollPane enPane;
	protected SetPathButton sePaButton;
	
	
	/** Panel fuer den oberen Bereich des Dialogs */
	protected JPanel topPanel;
	
	/** JPanel fuer den mittleren Bereich des Dialogs */
	protected JPanel middlePanel;
	
	/** JPanel fue den mitteleren Bereich des Dialogs, welches unter dem 
	 *  ersten Mittelpanel angeordnet wird	 */
	protected JPanel middlePanel2;
	
	/** Button Group für die JRadioButtons im middlePanel2 */
	protected ButtonGroupGameMode group;
	
	/** JPanel fuer den untern Bereich des Dialogs*/
	protected JPanel bottomPanel;
	
	/** Panel fuer den EnrollButton sowie fuer den RemoveButton*/
	protected JPanel enrollButtonsPanel;
	
	/** Referenz auf die Hauptsteuerungsklasse dieses Programmes*/
	protected MainController controller;
	
	protected GridBagLayout gbl;
	protected GridBagConstraints gbc;
	protected GridBagLayout gblTop;
	protected GridBagLayout gblMiddle;
	
	public GameEnrollDialog(JFrame owner, MainController controller){
		super(owner,true);
		this.controller=controller;
		avList = new AvailablePlayerList(this);
		caButton = new CancelButton(this);
		clButton = new ClearButton(this);
		enButton = new EnrollButton(this);
		enBox = new EnrollCheckBox(this);
		enList = new EnrolledPlayerList(this);
		miField = new MillisPRField(this);
		nrField = new NumberOfRoundsField(this);
		okButton = new OKButton(this);
		rmButton = new RemoveButton(this);
		sePaButton=new SetPathButton(this, this);
		group = new ButtonGroupGameMode(this);
		createDialog();
		init();
		pack();
	}
	
	@Override
	public void buzzardStateInvoked(){
		if(!enBox.isEnabled())
			enBox.setEnabled(true);
	}
	
	@Override
	public void cinderellaStateInvoked(){
		if(enBox.isEnabled()){
			enBox.setSelected(false);
			enBox.setEnabled(false);
		}
	}
	
	@Override
	public void dropStateInvoked(){
		if(enBox.isEnabled()){
			enBox.setSelected(false);
			enBox.setEnabled(false);
		}
	}
	
	protected void pushBuzzardSeriesInfo(BuzzardSeriesInfo info){
		controller.pushBuzzardSeriesInfo(info);
	}
	

	
	protected void createDialog(){
		Container cp = getContentPane();
		topPanel = new JPanel();

		
		gbl = new GridBagLayout();
		gblTop = new GridBagLayout();
		cp.setLayout(gbl);
		topPanel.setLayout(gblTop);
		
		// Anordnen der AvailablePlayerList bzw. der diese umgebenden JScrollPane
		gbc = getGBC(0,0,1,2);
		gbc.fill=GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 10, 10, 10);
		avPane = new JScrollPane(avList);
		gblTop.setConstraints(avPane,gbc);
		topPanel.add(avPane);
		
		// Anordnen des EnrolledPlayerList bzw. der diese umgebenden JScrollPane
		gbc = getGBC(2,0,1,2);
		gbc.fill=GridBagConstraints.BOTH;
		gbc.insets = new Insets(10,10,10,10);
		enPane = new JScrollPane(enList);
		gblTop.setConstraints(enPane, gbc);
		topPanel.add(enPane);
		
		/* Erzeugen und Anordnen des Panels, welches sich zwischen den beiden JLists 
		 * befinden wird, und den EnrollButton sowie den RemoveButton enthalten wird */
		enrollButtonsPanel = new JPanel();
		enrollButtonsPanel.setLayout(new GridLayout(3,1,20,15));
		enrollButtonsPanel.add(enButton);
		enrollButtonsPanel.add(rmButton);
		enrollButtonsPanel.add(sePaButton);
		gbc = getGBC(1,0,1,1);
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10,0,0,0);
		gblTop.setConstraints(enrollButtonsPanel,gbc);
		topPanel.add(enrollButtonsPanel);
		topPanel.setBorder(BorderFactory.createTitledBorder("Registrieren der Spieler"));
		
		
		/* Erzeugen und Anordnen des MittelPanels, welches die Textfelder (und entsprechenden)
		 * Labels fuer die Rundenpausenzeit, Anzahl der Gewinnspiele sowie die Visualisierungs-
		 * Checkbox enthaelt
		 */
		middlePanel = new JPanel();
		gblMiddle = new GridBagLayout();
		middlePanel.setLayout(gblMiddle);
		middlePanel.setBorder(BorderFactory.createTitledBorder("Spieleinstellungen"));
		// Anordnen des NumberOfRoundsFields
		gbc = getGBC(0,0,1,1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx=2;
		gbc.insets=new Insets(10,10,10,10);
		gbc.anchor = GridBagConstraints.NORTHEAST;
		gblMiddle.setConstraints(nrField,gbc);
		middlePanel.add(nrField);
		// Erzeugen und Anordnen des Labels fuer die Anzahl der Runden
		JLabel nrLabel = new JLabel("Anzahl Gewinnspiele");
		gbc = getGBC(1,0,1,1);
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.weightx=10;
		gbc.insets = new Insets(10,0,10,0);
		gbc.anchor=GridBagConstraints.NORTHWEST;
		gblMiddle.setConstraints(nrLabel,gbc);
		middlePanel.add(nrLabel);
		// Anordnen des MillisPRFields(Feld fuer die Anzahl der Millisekunden zwischen den
		// Runden)
		gbc = getGBC(2,0,1,1);
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.weightx=2;
		gbc.anchor=GridBagConstraints.NORTHEAST;
		gbc.insets = new Insets(10,10,10,10);
		gblMiddle.setConstraints(miField, gbc);
		middlePanel.add(miField);
		// Erzeugen und Anordnen des Labels fuer das MillisPRField
		JLabel miLabel = new JLabel("Pause zwischen Runden (ms)");
		gbc = getGBC(3,0,1,1);
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.weightx=10;
		gbc.anchor=GridBagConstraints.NORTHWEST;
		gbc.insets=new Insets(10,0,10,0);
		gblMiddle.setConstraints(miLabel,gbc);
		middlePanel.add(miLabel);
		// Anordnen der EnrollCheckBox (VisualizeCheckBox)
		gbc = getGBC(0,1,1,1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx=1;
		gbc.anchor = GridBagConstraints.NORTHEAST;
		gbc.insets = new Insets(10,10,10,10);
		gblMiddle.setConstraints(enBox, gbc);
		middlePanel.add(enBox);
		// Erzeugen und Anornen des Panels der EnrollCheckBox (VisualizeCheckBox);
		JLabel viLabel = new JLabel("Visualize Simulation");
		gbc = getGBC(1,1,1,1);
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.weightx=10;
		gbc.anchor=GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(10,0,10,0);
		gblMiddle.setConstraints(viLabel,gbc);
		middlePanel.add(viLabel);
		
		/* Erzeugen und Anordnen des MiddlePanels2, in dem der zu
		 * spielende Modus selektiert werden kann (Buzzard, 
		 * Cinderella oder Drop)		 */
		middlePanel2 = new JPanel();
		middlePanel2.setBorder(BorderFactory.createTitledBorder("Spielmodus"));
		middlePanel2.setLayout(new GridLayout(1,3));
		JRadioButton tmpButton = new JRadioButton("Buzzard (Default)", true);
		tmpButton.setActionCommand("Buzzard");
		group.add(tmpButton);
		middlePanel2.add(tmpButton);
		
		tmpButton = new JRadioButton("Cinderella");
		tmpButton.setActionCommand("Cinderella");
		group.add(tmpButton);
		middlePanel2.add(tmpButton);
		
		tmpButton = new JRadioButton("Drop");
		tmpButton.setActionCommand("Drop");
		group.add(tmpButton);
		middlePanel2.add(tmpButton);
		
		
		/* Erzeugen und Anordnen des BottomPanels, welches den OKButton, den CancelButton
		 * sowie den ClearButton enhalten wird		 */
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1,3,10,10));
		bottomPanel.add(okButton);
		bottomPanel.add(caButton);
		bottomPanel.add(clButton);
		
		// Einfuegen des TopPanels in die ContentPane des Dialogs
		gbc = getGBC(0,0,1,1);
		gbc.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(topPanel, gbc);
		cp.add(topPanel);
		// Einfuegen des MiddlePanels in die ContentPane des Dialogs
		gbc = getGBC(0,1,1,1);
		gbc.fill=GridBagConstraints.BOTH;
		gbc.insets = new Insets(10,0,10,0);
		gbl.setConstraints(middlePanel,gbc);
		cp.add(middlePanel);
		
		// Einfuegen des middlePanel2 in die ContentPande des Dialogs
		gbc = getGBC(0,2,1,1);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10,0,10,0);
		gbl.setConstraints(middlePanel2, gbc);
		cp.add(middlePanel2);
		
		// Einfuegen des BottomPanels in die ContentPane des Dialogs
		gbc = getGBC(0,3,1,1);
		gbc.fill=GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0,0,10,0);
		gbl.setConstraints(bottomPanel,gbc);
		cp.add(bottomPanel);
	}
	
	protected GridBagConstraints getGBC(int gridx, int gridy, int gridwidth, int gridheight){
		GridBagConstraints res = new GridBagConstraints();
		res.anchor = GridBagConstraints.NORTH;
		res.weightx= gridwidth;
		res.weighty= gridheight;
		res.gridx=gridx;
		res.gridy=gridy;
		res.gridwidth=gridwidth;
		res.gridheight=gridheight;
		return res;
	}
	
	
	//===============================================================================
	//===============================================================================
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
	
	/**
	 * Gruppe 3 Punkt 11
	 * 
	 * Es wird geprüft ob Player selektiert wurden. Wenn ja wird in einer For
	 * Schleife die Methode enrollPlayer aufgerufen um für die ausgewählte
	 * Klasse einen Player anzulegen:
	 * 
	 */
	public void enroll() {
		int index = avList.getSelectedIndex();

		if (!(index >= 0)) {
			return;
		} else {
			for (int i = 0; i < avList.getSelectedIndices().length; i++) {
				enrollPlayer(avList.getSelectedIndices()[i]);
			}

		}

	}

	/**
	 * die enroll() Methode ruft für jeden Spieler der eingeschrieben werden
	 * soll diese Methode auf
	 * 
	 * @param index
	 *            : Indexwert der availablePlayerListe von dem Spieler, der
	 *            eingeschrieben werden soll
	 */

	public void enrollPlayer(int index) {

		System.out.println("Insgesamt: " + avList.getSelectedIndices().length);
		if (!(index >= 0)) {
			return;
		}
		String classToInstantiate = avList.getElementAt(index);

		// Gruppe 3 Punkt 10 Automatische Namensgenerierung bei nicht Eingabe
		// eines individuellen Namens:
		String objectName = checkObjectName(classToInstantiate);
			
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
	//Gruppe 2 ueberfluessig
	public void millisPRFieldChanged() {
		numberOfRoundsFieldChanged();
	}



	@Override
	public void enrollCheckBoxStateChanged() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void cancel() {
		setVisible(false);
	}

	private int[] getDeckCards(){
		try{
			FileInputStream fis = new FileInputStream("src/data/deck.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			DefaultListModel dlm = (DefaultListModel) ois.readObject();
			int[] res = new int[dlm.size()];
			System.err.print("Größe: ");
			System.err.println(dlm.size());
			for(int i = 0; i < dlm.size(); i++){
				res[i] = Integer.parseInt(String.valueOf(dlm.getElementAt(i)));
				System.out.println(res[i]);
			}
			return res;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public void ok() {
		BuzzardSeriesInfo info = new BuzzardSeriesInfo();
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
		
		System.err.println("CREATE");
		System.out.println(getDeckCards()[1]);
		info.setDeck(new BuzzardDeck(getDeckCards(),
				BuzzardDeck.SHUFFLED));
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

	//Eingefuegt von Gruppe 2
	public int getMiFieldValue() throws NumberFormatException{
			return Integer.parseInt(miField.getText());	
	}
		
	@Override
	//Gruppe 2 ueberpruefung von MiField entfernt, findet in getMiFieldValue() statt
	public void numberOfRoundsFieldChanged() {
		if(!(nrField.getText().equals("")||enList.getNumberOfElements()==0)){
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



	@Override
	public void registerAvailablePlayerList(AvailablePlayerList list){
		this.avList=list;
	}
	
	@Override
	public void registerNetworkPlayerList(NetworkPlayerList list){
		
	}

	@Override
	public void registerEnrolledPlayerList(EnrolledPlayerList list){
		this.enList = list;
	}
	
	@Override
	public void registerCancelButton(CancelButton button){
		this.caButton=button;
	}
	
	@Override
	public void registerClearButton(ClearButton button){
		this.clButton=button;
	}
	
	@Override
	public void registerEnrollButton(EnrollButton button){
		this.enButton=button;
	}
	
	@Override
	public void registerOKButton(OKButton button){
		this.okButton=button;
	}
	
	@Override
	public void registerRemoveButton(RemoveButton button){
		this.rmButton=button;
	}
	
	@Override
	public void registerEnrollCheckBox(EnrollCheckBox checkBox){
		this.enBox=checkBox;
	}
	
	@Override
	public void registerMillisPRField(MillisPRField field){
		this.miField=field;
	}
	
	@Override
	public void registerNumberOfRoundsField(NumberOfRoundsField nuField){
		this.nrField=nuField;
	}
	
	@Override
	public void registerSetPathButton(SetPathButton button){
		this.sePaButton=button;
	}
	
	
	/**
	 * Gruppe 3
	 * Diese Methode ist für die Namensvergabe beim Einschreiben der Spieler verantwortlich
	 * 
	 * @param className = Der Filename des Bots
	 * @return Wenn im Dialogfeld etwas eingegeben wurde wird der Wert zurückgegeben ansonsten der Filename ohne ".class"
	 */
	protected String checkObjectName(String className) {
		
		// String playerName = className.replace(".class", "");
		// neue Variante 24.06
		String playerName=className.substring(0,className.length()-6);
		
		String objectName = JOptionPane.showInputDialog(this,"Name der Instanz " + className + ":",playerName);
		
		if(objectName==null||objectName.equals("")){
			objectName = playerName;
		}
		return objectName;
	}

	@Override
	public void clearSelectionAvailablePlayerList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearSelectionNetworkPlayerList() {
		// TODO Auto-generated method stub
		
	}
}
