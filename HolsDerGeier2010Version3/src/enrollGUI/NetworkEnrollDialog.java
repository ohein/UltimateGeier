package enrollGUI;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import network.Server;

import controller.MainController;

/**
 * Modelliert des Netzwerk-Server-Guis
 * Geerbt wird von der Klasse GameEnrollDialog
 * 
 * @author Johannes Hohmann
 * @version 1
 * @datum 12.03.2012
 */

public class NetworkEnrollDialog extends GameEnrollDialog implements NetworkEnrollMediator{
	protected NetworkPlayerList neList;
	protected JScrollPane nePane;
	protected Server s;
	protected Thread t1;
	// Variablen für die Server-Informationen
	private JLabel portLabel;
	private PortField portField;
	private JLabel ipLabel;
	private IpField ipField;
	private InetAddress ip;
	private int port;
	
	public NetworkEnrollDialog(JFrame owner, MainController controller){
		super(owner, controller);
		
		this.controller=controller;
		neList = new NetworkPlayerList(this);
	
		ipLabel = new JLabel("IP-Adresse:");
		ipField = new IpField(this);
		portLabel = new JLabel("Port:");
		portField = new PortField(this);
		
		InetAddress ip2;
		try {
			ip2 = InetAddress.getLocalHost();
			ipField.setText(ip2.getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	
		gbc = getGBC(0,2,1,1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx=2;
		gbc.insets=new Insets(10,10,10,10);
		gbc.anchor = GridBagConstraints.NORTHEAST;
		gblMiddle.setConstraints(portField,gbc);
		middlePanel.add(portField);
		// Erzeugen und Anordnen des Labels fuer die Anzahl der Runden
		JLabel nrLabel = new JLabel("Port");
		gbc = getGBC(1,2,1,1);
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.weightx=10;
		gbc.insets = new Insets(10,0,10,0);
		gbc.anchor=GridBagConstraints.NORTHWEST;
		gblMiddle.setConstraints(nrLabel,gbc);
		middlePanel.add(nrLabel);
		// Anordnen des MillisPRFields(Feld fuer die Anzahl der Millisekunden zwischen den
		// Runden)
		gbc = getGBC(2,2,1,1);
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.weightx=2;
		gbc.anchor=GridBagConstraints.NORTHEAST;
		gbc.insets = new Insets(10,10,10,10);
		gblMiddle.setConstraints(ipField, gbc);
		middlePanel.add(ipField);
		// Erzeugen und Anordnen des Labels fuer das MillisPRField
		JLabel miLabel = new JLabel("IP-Adresse");
		gbc = getGBC(3,2,1,1);
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.weightx=10;
		gbc.anchor=GridBagConstraints.NORTHWEST;
		gbc.insets=new Insets(10,0,10,0);
		gblMiddle.setConstraints(miLabel,gbc);
		middlePanel.add(miLabel);
		
		gbc = getGBC(0,1,1,2);
		gbc.fill=GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 10, 10, 10);
		nePane = new JScrollPane(neList);
		gblTop.setConstraints(nePane,gbc);
		topPanel.add(nePane);
			
		//(Hoehen-) Anpassen der Liste mit den bereits enrolled Spielern
		gbc = getGBC(2,0,1,4);
		gbc.fill=GridBagConstraints.BOTH;
		gbc.insets = new Insets(10,10,10,10);
		gblTop.setConstraints(enPane, gbc);
			
		InetAddress ip = null;
		try {
			this.ip = InetAddress.getByName(ipField.getName());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("IP-Adresse FALSCH");
		}
		this.port = Integer.parseInt(portField.getText());		
		this.pack();
	}

	public void startThread(){
		System.out.println("Server Gestartet");
		s = new Server(this, neList,ip,port);
		t1 = new Thread(s); 
		t1.start();
	}
	
	@Override
	public void ok() {
		super.ok();
		t1.stop();
		s.socketClose();
	}

	@Override
	public void select() {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public void clearSelectionAvailablePlayerList(){
	      avList.clearSelection();
	}
	@Override
	public void clearSelectionNetworkPlayerList(){
	      neList.clearSelection();
	}
	
	@Override
	public void enroll() {
		if(avList.getSelectedIndex() > -1){
			super.enroll();
		}
		else{
			if(neList.getSelectedIndex() > -1){
				int index =neList.getSelectedIndex();
				if(!(index>=0)){
					return;
				}
				enList.addPlayer(neList.getElement(index));
				enList.revalidate();
			}
		}	
	}

	@Override
	public void registerPortField(PortField field) {
		// TODO Auto-generated method stub
		this.portField=field;
		portField.setEnabled(false);
	}

	@Override
	public void registerIpField(IpField field) {
		// TODO Auto-generated method stub
		this.ipField=field;
		ipField.setEnabled(false);
	}
	
	@Override
	public void IpFieldChanged() {
		// TODO Auto-generated method stub
		
	}	
}