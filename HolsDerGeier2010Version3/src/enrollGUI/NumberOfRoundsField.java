package enrollGUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class NumberOfRoundsField extends JTextField implements DocumentListener{
	private GameEnrollMediator med;
	
	public NumberOfRoundsField(GameEnrollMediator med){
		this.med=med;
		this.med.registerNumberOfRoundsField(this);
		getDocument().addDocumentListener(this);
	}
	
	@Override
	public void insertUpdate(DocumentEvent e){
		System.out.println("insertUpdate feuert");
		med.numberOfRoundsFieldChanged();
	}
	
	@Override
	public void removeUpdate(DocumentEvent e){
		System.out.println("removeUpdate feuert");
		med.numberOfRoundsFieldChanged();
	}
	
	@Override
	public void changedUpdate(DocumentEvent e){
		System.out.println("changedUpdate feuert");
		med.numberOfRoundsFieldChanged();
	}

}
