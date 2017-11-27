package enrollGUI;

import java.awt.event.*;
import javax.swing.*;

public class EnrollCheckBox extends JCheckBox implements ItemListener {
	private GameEnrollMediator med;
	
	public EnrollCheckBox(GameEnrollMediator med){
		this.med=med;
		this.med.registerEnrollCheckBox(this);
		addItemListener(this);
	}
	
	@Override
	public void itemStateChanged(ItemEvent e){
		med.enrollCheckBoxStateChanged();
	}
}
