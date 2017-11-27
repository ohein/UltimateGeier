package deckConfiguration;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import java.awt.event.*;

public class CardButton extends JButton implements ActionListener {
	
	private DeckDialog deckDialog;
	
	public CardButton(DeckDialog deckDialog){
		super("Add");
		this.deckDialog = deckDialog;
		deckDialog.registerCardButton(this);
		addActionListener(this);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent even){
		deckDialog.cardButtonPressed();
	}

}
