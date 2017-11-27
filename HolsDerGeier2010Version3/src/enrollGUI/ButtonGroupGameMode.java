package enrollGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

public class ButtonGroupGameMode extends ButtonGroup implements ActionListener{
	private GameEnrollMediator mediator;
	
	public ButtonGroupGameMode(GameEnrollMediator mediator){
		this.mediator = mediator;
	}
	
	@Override
	public void add(AbstractButton b){
		super.add(b);
		b.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		String ac = e.getActionCommand();
		if(ac.equals("Buzzard")){
			mediator.buzzardStateInvoked();
		}
		else if(ac.equals("Cinderella")){
			mediator.cinderellaStateInvoked();
		}
		else if(ac.equals("Drop")){
			mediator.dropStateInvoked();
		}
		
	}
}
