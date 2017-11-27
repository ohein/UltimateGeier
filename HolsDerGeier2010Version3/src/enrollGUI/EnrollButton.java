package enrollGUI;

import java.awt.event.ActionEvent;

import javax.swing.JButton;


import java.awt.event.*;

public class EnrollButton extends JButton implements ActionListener{
	
	private GameEnrollMediator med;
	
	public EnrollButton(GameEnrollMediator med){
		super("Enroll");
		this.med=med;
		this.med.registerEnrollButton(this);
		addActionListener(this);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		med.enroll();
	}
}
