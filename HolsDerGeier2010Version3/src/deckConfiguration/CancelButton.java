package deckConfiguration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class CancelButton extends JButton implements ActionListener{
	
	private DeckDialog deckDialog;
	
	public CancelButton(DeckDialog deckDialog){
		super("Cancel");
		this.deckDialog = deckDialog;
		deckDialog.registerCancelButton(this);
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		deckDialog.cancelButtonPressed();
	}
	
}
