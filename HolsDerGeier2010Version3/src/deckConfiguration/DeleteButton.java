package deckConfiguration;

import javax.swing.JButton;
import java.awt.event.*;

public class DeleteButton extends JButton implements ActionListener{

	private DeckDialog deckDialog;
	
	public DeleteButton(DeckDialog deckDialog){
		super("Remove");
		this.deckDialog = deckDialog;
		deckDialog.registerDeleteButton(this);
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent event){
		deckDialog.deleteButtonPressed();
	}
}
