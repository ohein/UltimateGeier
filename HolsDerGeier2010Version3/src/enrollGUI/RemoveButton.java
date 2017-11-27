package enrollGUI;

import java.awt.event.*;
import javax.swing.*;

public class RemoveButton extends JButton implements ActionListener {
	private GameEnrollMediator med;
	
	public RemoveButton(GameEnrollMediator med){
		super("Remove");
		this.med=med;
		this.med.registerRemoveButton(this);
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		med.remove();
	}
}
