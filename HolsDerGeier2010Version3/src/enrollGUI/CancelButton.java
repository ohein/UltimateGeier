package enrollGUI;

import java.awt.event.*;
import javax.swing.*;

public class CancelButton extends JButton implements ActionListener{
	private GameEnrollMediator med;
	
	public CancelButton(GameEnrollMediator med){
		super("Cancel");
		this.med=med;
		med.registerCancelButton(this);
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		med.cancel();
	}

}
