package enrollGUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TPlayersPerMatchField extends JTextField implements DocumentListener {
	
	private TournamentEnrollMediator med;
	
	public TPlayersPerMatchField(TournamentEnrollMediator med){
		this.med=med;
		this.med.registerTPlayersPerMatchField(this);
		this.getDocument().addDocumentListener(this);
	}
	
	public int getNumberOfPlayersPerMatch(){
		return Integer.parseInt(getText());
	}
	
	@Override
	public void insertUpdate(DocumentEvent e){
		med.tPlayersPerMatchFieldChanged();
	}
	
	@Override
	public void removeUpdate(DocumentEvent e){
		med.tPlayersPerMatchFieldChanged();
	}
	
	@Override
	public void changedUpdate(DocumentEvent e){
		med.tPlayersPerMatchFieldChanged();
	}
	
	
}
