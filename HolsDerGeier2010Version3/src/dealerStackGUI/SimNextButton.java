package dealerStackGUI;

import java.awt.event.*;
import javax.swing.*;

public class SimNextButton extends JButton implements ActionListener {
	private DealerStackPanel panel;
	
	public SimNextButton(DealerStackPanel panel){
		super("SimNext");
		this.panel=panel;
		panel.registerSimNextButton(this);
		addActionListener(this);
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent event){
		panel.simNext();
	}
}
