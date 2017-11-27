package deckConfiguration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class OKButton extends JButton implements ActionListener{
	private DeckDialog deckDialog;
	
	public OKButton(DeckDialog deckDialog){
		super("OK");
		this.deckDialog = deckDialog;
		deckDialog.registerOKButton(this);
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		deckDialog.okButtonPressed();
	}
}
