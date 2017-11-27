package dealerStackGUI;

import java.awt.event.*;
import javax.swing.*;

public class DeleteButton extends JButton implements ActionListener{
	private DealerStackPanel panel;
	
	public DeleteButton(DealerStackPanel panel){
		super("Delete");
		this.panel=panel;
		this.panel.registerDeleteButton(this);
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		panel.delete();
	}


}
