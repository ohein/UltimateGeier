package enrollGUI;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

/**
 *  @author Gruppe 2
 */
public class FastSimulationCheckBox extends JCheckBox implements ItemListener {

private TournamentEnrollMediator med;
	
	public FastSimulationCheckBox(TournamentEnrollMediator med){
		this.med=med;
		this.med.registerFastSimulationCheckBox(this);
		addItemListener(this);
	}
	
	public void itemStateChanged(ItemEvent e){
		med.fastSimulationCheckBoxStateChanged();
	}
}
