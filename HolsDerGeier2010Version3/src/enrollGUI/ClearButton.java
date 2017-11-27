package enrollGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;


public class ClearButton extends JButton implements ActionListener {
	
	private GameEnrollMediator med;
	
	public ClearButton(GameEnrollMediator med){
		super("Clear");
		this.med=med;
		this.med.registerClearButton(this);
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		med.clear();
	}

}
