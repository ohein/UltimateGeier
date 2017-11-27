package enrollGUI;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MillisPRField extends JTextField implements DocumentListener{
	private GameEnrollMediator med;
	
	public MillisPRField(GameEnrollMediator med){
		super("1000");
		this.med=med;
		med.registerMillisPRField(this);
		/* Ausgekommentiert von Gruppe 2, wird nicht mehr benoetigt, Listener verhindet direkten
		 * Zugriff auf das Textfield
		 */
		//getDocument().addDocumentListener(this);
	}
	
	
	
	@Override
	public void insertUpdate(DocumentEvent e){
		System.out.println("insertUpdate feuert");
		med.millisPRFieldChanged();
	}
	
	@Override
	public void removeUpdate(DocumentEvent e){
		System.out.println("removeUpdate feuert");
		med.millisPRFieldChanged();
	}
	
	@Override
	public void changedUpdate(DocumentEvent e){
		System.out.println("changedUpdate feuert");
		med.millisPRFieldChanged();
	}
	
}
