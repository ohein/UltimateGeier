package enrollGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class OKButton extends JButton implements ActionListener {
	private GameEnrollMediator med;
	
	public OKButton(GameEnrollMediator med){
		super("OK");
		this.med=med;
		this.med.registerOKButton(this);
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		med.ok();
	}

}
