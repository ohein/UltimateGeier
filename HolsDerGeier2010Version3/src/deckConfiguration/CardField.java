package deckConfiguration;

import javax.swing.JTextField;

public class CardField extends JTextField {
	
	private DeckDialog deckDialog;
	
	public CardField(DeckDialog deckDialog){
		this.deckDialog = deckDialog;
		deckDialog.registerCardField(this);
	}
	
}
