package dealerStackGUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import controller.*;
import java.util.*;

/** Diese Klasse fungiert sowohl als visuelles Element (JPanel) als auch als Mediator fuer die 
 * entsprechenden Komponenten*/ 
public class DealerStackPanel extends JPanel{
	private MainController controller;
	
	private DealerStateManager dealer;
	private GridBagLayout gbl;
	private GridBagConstraints gbc;
	
	/* Mediator-Klienten*/
	/** Die Liste mit den Spielen/Serien in der Dealer Simulationsliste	 */
	private RegisteredGamesList reList;
	private DeleteButton deButton;
	/** Die Liste, in der die registrierten Spieler sowie evtl. weitere Infos dargestellt
	 * werden	 */
	private GameInfoList geList;
	/** Dieser Knopf löst die Simulations der Serie/des nächsten Turniers im Dealer-Stack
	 * aus
	 */
	private SimNextButton siButton;
	
	/* Nicht-Mediator-Komponenten*/
	private JLabel nrLabel;
	private JLabel msLabel;
	private JLabel viLabel;
	private JButton closeButton;
	
	
	Queue<BuzzardSeriesInfo> series;
	BuzzardSeriesInfo info;
	
	
	private JPanel listButtonPanel;
	private JPanel labelPanel;
	private JPanel windowButtonPanel;

	
	
	private String nrLabelText = "Anzahl Matches: ";
	private String msLabelText = "Anzahl MilliSek: ";
	private String viLabelText = "Visualisiere: ";
	
	public DealerStackPanel(DealerStateManager dealer, MainController controller){
		this.dealer=dealer;
		this.controller = controller;
		series=dealer.getSeriesQueue();
		nrLabel = new JLabel(nrLabelText);
		msLabel = new JLabel(msLabelText);
		viLabel = new JLabel(viLabelText);
		siButton = new SimNextButton(this);
		deButton = new DeleteButton(this);
		geList = new GameInfoList(this);
		reList = new RegisteredGamesList(this);
		
		/* Erzeugen der Buttons, die nicht am Mediator-Pattern teilnehmen*/
		generateButtons();
		
		
		//Programmieren der grafischen Darstellung
		 // Allgemeine Festlegung des Layoutmanagers
		gbl = new GridBagLayout();
		setLayout(gbl);
		
		
		// Programmieren der grafischen Darstellung
		 // Angefangen wird bei der linken Seite des Fensters
		JScrollPane paneOne = new JScrollPane(reList);
		paneOne.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		listButtonPanel = new JPanel(new GridLayout(1,3,2,2));
		listButtonPanel.add(siButton);
		listButtonPanel.add(deButton);

		//Programmieren der grafischen Darstellung
		 // Jetzt wird die rechte Seite des Fensters erzeugt
		JScrollPane paneTwo = new JScrollPane(geList);
		paneTwo.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		labelPanel = new JPanel(new GridLayout(3,1,2,2));
		labelPanel.add(nrLabel);
		labelPanel.add(msLabel);
		labelPanel.add(viLabel);
		
		//Programmieren der grafischen Darstellung
		  // Jetzt wird der untere Teil des Fensters programmiert
		windowButtonPanel = new JPanel(new GridLayout(1,1));
		windowButtonPanel.add(closeButton);
		
		//Programmieren der grafischen Darstellung
		 // Anordnen der Komponenten auf der Pane
		   // Anordnen der Liste der registrierten Serien
		gbc = getGBC(0,0,1,2,65,90,GridBagConstraints.BOTH);
		gbc.insets = new Insets(10,10,5,30);
		gbl.setConstraints(paneOne, gbc);
		add(paneOne);
		   // Anordnen der Listen-Buttons
	    gbc = getGBC(0,2,1,1,65,0,GridBagConstraints.HORIZONTAL);
	    gbc.insets = new Insets(0,10,5,30);
		gbl.setConstraints(listButtonPanel, gbc);
		add(listButtonPanel);
		   // Anordnen der Liste der entsprechenden registrierten Spieler
		gbc = getGBC(1,0,1,1,35,70,GridBagConstraints.BOTH);
		gbc.insets = new Insets(10,0,5,10);
		gbl.setConstraints(paneTwo,gbc);
		add(paneTwo);
		  // Anordnen des LabelPanels
		gbc = getGBC(1,1,1,1,35,20,GridBagConstraints.BOTH);
		gbc.insets = new Insets(0,0,5,10);
		gbl.setConstraints(labelPanel,gbc);
		add(labelPanel);
		
		gbc = getGBC(0,3,2,1,100,0,GridBagConstraints.NONE);
		gbc.insets = new Insets(20,10,10,10);
		gbc.anchor=GridBagConstraints.CENTER;
		gbl.setConstraints(closeButton, gbc);
		
		add(closeButton);
		
		
	}
	

	
	
	
	private GridBagConstraints getGBC(int x, int y,int width, int height, int weightx, int weighty, int fill){
		GridBagConstraints res = new GridBagConstraints();
		res.gridx=x;
		res.gridy=y;
		res.gridwidth=width;
		res.gridheight=height;
		res.weightx=weightx;
		res.weighty=weighty;
		res.fill=fill;
		return res;
	}

	//Funktionale Methoden des Mediator-Patterns
	//==========================================
	void delete(){
		
	}
	
	/** Wird aufgerufen, wenn in der RegisteredPlayerList die ausgewaehlte BuzzardSeriesInfo 
	 * geaendert wurde	 */
	void regListValueChanged(){
		series = dealer.getSeriesQueue();
		Iterator<BuzzardSeriesInfo> it = series.iterator();
		int i=0;
		int selInd = reList.getSelectedIndex();
		while(i<=selInd&&it.hasNext()){
			info = it.next();
			i++;
		}
		nrLabel.setText(nrLabelText + String.valueOf(info.getNoOfMatches()));
		// Welch Freude... wir sehen den Fragezeichenoperator in action ;)
		viLabel.setText(viLabelText + (info.isSimulated()?"Ja":"Nein"));
		geList.clear();
		geList.displayPlayers(info.getPlayers());
	}
	
	void simNext(){
		controller.simNext();
	}
	
	public void init(){
		reList.clear();
		reList.displayGames(dealer.getSeriesQueue());
	}
	
	
	// Register-Methoden des Mediator-Patterns
	//==============================================
	void registerDeleteButton(DeleteButton button){
		this.deButton=button;
	}
	void registerRegisteredGamesList(RegisteredGamesList list){
		this.reList=list;
	}
	void registerGameInfoList(GameInfoList list){
		this.geList=list;
	}
	
	void registerSimNextButton(SimNextButton button){
		siButton = button;
	}
	
	
	private void generateButtons(){
		closeButton = new JButton("Back to main");
		closeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				// Zurueck zum Hauptmenue
			}
		});
	}
	
//	public static void main(String[] args){
//		JFrame frame = new JFrame();
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.add(new DealerStackPanel(new Dealer()));
//		frame.setBounds(100,100,300,400);
//		frame.setVisible(true);
//}	

}
