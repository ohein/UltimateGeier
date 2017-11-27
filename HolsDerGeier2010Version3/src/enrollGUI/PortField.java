package enrollGUI;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PortField extends JTextField implements DocumentListener{
	private NetworkEnrollMediator med;
	
	public PortField(NetworkEnrollMediator med){
		super("49166");
		this.med=med;
		med.registerPortField(this);
		getDocument().addDocumentListener(this);
	}
	
	
	
	@Override
	public void insertUpdate(DocumentEvent e){
		System.out.println("insertUpdate feuert");
	//	med.millisPRFieldChanged();
	}
	
	@Override
	public void removeUpdate(DocumentEvent e){
		System.out.println("removeUpdate feuert");
	//	med.millisPRFieldChanged();
	}
	
	@Override
	public void changedUpdate(DocumentEvent e){
		System.out.println("changedUpdate feuert");
	//	med.millisPRFieldChanged();
	}
	
}
